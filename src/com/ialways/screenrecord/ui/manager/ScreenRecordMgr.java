package com.ialways.screenrecord.ui.manager;

import java.io.IOException;
import java.nio.ByteBuffer;

import android.app.Activity;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.projection.MediaProjection;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;

import com.ialways.screenrecord.ui.Initializable;
import com.ialways.screenrecord.ui.MainApp;
import com.ialways.screenrecord.utils.storage.PathUtils;

/**
 * 项目名称：ScreenRecord 类名称：ScreenRecordMgr 类描述： 视屏录制管理器 创建人：dingchao
 * 创建时间：2015年10月21日 下午2:25:14 修改人：dingchao 修改时间：2015年10月21日 下午2:25:14 修改备注：初始创建
 * 
 * @version 1.0
 */
public class ScreenRecordMgr implements Initializable {

    // 30fps(帧率)
    private static final int FRAME_RATE = 30;

    private static final String VIDEO_MIME_TYPE = "video/avc";

    private int mTrackIndex = -1;

    private boolean mMuxerStarted = false;

    private MediaProjection mMediaProjection;

    private MediaMuxer mMuxer;

    private MediaCodec mVideoEncoder;

    private MediaCodec.BufferInfo mVideoBufferInfo;

    private Surface mInputSurface;

    private int mVideoWidth;

    private int mVideoHeight;

    private int mScreenDensity;

    private final Handler mDrainHandler = new Handler(Looper.getMainLooper());

    private Runnable mDrainEncoderRunnable;

    public static ScreenRecordMgr shared() {
        return MainApp.shared().get(ScreenRecordMgr.class);
    }

    public void init(Context context) {
        
        DisplayManager dm = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        Display defaultDisplay = dm.getDisplay(Display.DEFAULT_DISPLAY);
        if (defaultDisplay == null) {
            throw new RuntimeException("No display found.");
        }

        // Get the display size and density.
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mVideoWidth = metrics.widthPixels;
        mVideoHeight = metrics.heightPixels;
        mScreenDensity = metrics.densityDpi;
    }

    public void start(MediaProjection mediaProjection) {
        
        mDrainEncoderRunnable = new Runnable() {
            
            @Override
            public void run() {
                drainEncoder();
            }
        };
        
        prepareVideoEncoder();
        try {
            mMuxer = new MediaMuxer(PathUtils.getRecordFilePath(MainApp.shared()), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        } catch (IOException ioe) {
            throw new RuntimeException("MediaMuxer creation failed", ioe);
        }

        this.mMediaProjection = mediaProjection;
        // Start the video input.
        mMediaProjection.createVirtualDisplay("Recording Display", mVideoWidth, mVideoHeight, mScreenDensity,
                0 /* flags */, mInputSurface, null /* callback */, null /* handler */);
        
        // Start the encoders
        drainEncoder();
    }

    private void prepareVideoEncoder() {
        mVideoBufferInfo = new MediaCodec.BufferInfo();
        MediaFormat format = MediaFormat.createVideoFormat(VIDEO_MIME_TYPE, mVideoWidth, mVideoHeight);

        // Set some required properties. The media codec may fail if these
        // aren't defined.
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        format.setInteger(MediaFormat.KEY_BIT_RATE, 6000000); // 6Mbps
        format.setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE);
        format.setInteger(MediaFormat.KEY_CAPTURE_RATE, FRAME_RATE);
        format.setInteger(MediaFormat.KEY_REPEAT_PREVIOUS_FRAME_AFTER, 1000000 / FRAME_RATE);
        format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1); // 1 seconds
                                                                // between
                                                                // I-frames

        // Create a MediaCodec encoder and configure it. Get a Surface we can
        // use for recording into.
        try {
            mVideoEncoder = MediaCodec.createEncoderByType(VIDEO_MIME_TYPE);
            mVideoEncoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mInputSurface = mVideoEncoder.createInputSurface();
            mVideoEncoder.start();
        } catch (IOException e) {
            releaseEncoders();
        }
    }

    private boolean drainEncoder() {
        mDrainHandler.removeCallbacks(mDrainEncoderRunnable);
        while (true) {
            int bufferIndex = mVideoEncoder.dequeueOutputBuffer(mVideoBufferInfo, 0);

            if (bufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                // nothing available yet
                break;
            } else if (bufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                // should happen before receiving buffers, and should only
                // happen once
                if (mTrackIndex >= 0) {
                    throw new RuntimeException("format changed twice");
                }
                mTrackIndex = mMuxer.addTrack(mVideoEncoder.getOutputFormat());
                if (!mMuxerStarted && mTrackIndex >= 0) {
                    mMuxer.start();
                    mMuxerStarted = true;
                }
            } else if (bufferIndex < 0) {
                // not sure what's going on, ignore it
            } else {
                ByteBuffer encodedData = mVideoEncoder.getOutputBuffer(bufferIndex);
                if (encodedData == null) {
                    throw new RuntimeException("couldn't fetch buffer at index " + bufferIndex);
                }

                if ((mVideoBufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                    mVideoBufferInfo.size = 0;
                }

                if (mVideoBufferInfo.size != 0) {
                    if (mMuxerStarted) {
                        encodedData.position(mVideoBufferInfo.offset);
                        encodedData.limit(mVideoBufferInfo.offset + mVideoBufferInfo.size);
                        mMuxer.writeSampleData(mTrackIndex, encodedData, mVideoBufferInfo);
                    } else {
                        // muxer not started
                    }
                }

                mVideoEncoder.releaseOutputBuffer(bufferIndex, false);

                if ((mVideoBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    break;
                }
            }
        }

        mDrainHandler.postDelayed(mDrainEncoderRunnable, 10);
        return false;
    }

    public void releaseEncoders() {
        if (mDrainEncoderRunnable != null) {
            mDrainHandler.removeCallbacks(mDrainEncoderRunnable);
            mDrainEncoderRunnable = null;
        }
        if (mMuxer != null) {
            if (mMuxerStarted) {
                mMuxer.stop();
            }
            mMuxer.release();
            mMuxer = null;
            mMuxerStarted = false;
        }
        if (mVideoEncoder != null) {
            mVideoEncoder.stop();
            mVideoEncoder.release();
            mVideoEncoder = null;
        }
        if (mInputSurface != null) {
            mInputSurface.release();
            mInputSurface = null;
        }
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
        mVideoBufferInfo = null;
        mDrainEncoderRunnable = null;
        mTrackIndex = -1;
    }
}
