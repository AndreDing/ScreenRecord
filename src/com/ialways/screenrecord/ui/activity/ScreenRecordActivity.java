/**
 * Code from http://mattsnider.com/video-recording-with-mediaprojectionmanager/
 * author:mattsnider
 */
package com.ialways.screenrecord.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.ialways.screenrecord.ui.manager.ScreenRecordMgr;

public class ScreenRecordActivity extends FragmentActivity {

    private MediaProjectionManager mMediaMgr;

    private static final int REQUEST_CODE_CAPTURE = 1234;

    private MediaProjection mMediaProjection;

    public static void start(Context ctx, int tab) {
        Intent i = new Intent(ctx, ScreenRecordActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ctx.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        // TODO Auto-generated method stub
        super.onCreate(bundle);


        this.mMediaMgr = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        Intent mPermissionIntent = mMediaMgr.createScreenCaptureIntent();
        this.startActivityForResult(mPermissionIntent, REQUEST_CODE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, requestCode, intent);

        if (requestCode == REQUEST_CODE_CAPTURE && resultCode == RESULT_OK) {
            mMediaProjection = mMediaMgr.getMediaProjection(resultCode, intent);
            ScreenRecordMgr.shared().start(mMediaProjection);
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        ScreenRecordMgr.shared().releaseEncoders();
    }
}
