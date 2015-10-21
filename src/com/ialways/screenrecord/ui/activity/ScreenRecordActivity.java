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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ialways.screenrecord.R;
import com.ialways.screenrecord.ui.MainApp;
import com.ialways.screenrecord.ui.manager.ScreenRecordMgr;
import com.ialways.screenrecord.ui.service.ScreenRecordService;

/**
 * 项目名称：ScreenRecord 类名称：ScreenRecordActivity 类描述： 主Activity 创建人：dingchao
 * 创建时间：2015年10月21日 上午11:17:25 修改人：dingchao 修改时间：2015年10月21日 上午11:17:25 修改备注：
 * 
 * @version 1.0
 */
public class ScreenRecordActivity extends FragmentActivity {

    private MediaProjectionManager mMediaMgr;

    private static final int REQUEST_CODE_CAPTURE = 1234;

    private MediaProjection mMediaProjection;

    private Button mStartBtn;

    /**
     * start(回到主界面同一调用此方法)
     * 
     * @param
     * @return
     */
    public static void start(Context ctx) {
        Intent i = new Intent(ctx, ScreenRecordActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ctx.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_sreen_record);

        mStartBtn = (Button) this.findViewById(R.id.start_record);
        mStartBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                // 进行权限验证
                Intent mPermissionIntent = mMediaMgr.createScreenCaptureIntent();
                ScreenRecordActivity.this.startActivityForResult(mPermissionIntent, REQUEST_CODE_CAPTURE);
            }
        });

        this.mMediaMgr = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, requestCode, intent);

        if (requestCode == REQUEST_CODE_CAPTURE && resultCode == RESULT_OK) {
            mMediaProjection = mMediaMgr.getMediaProjection(resultCode, intent);
            ScreenRecordMgr.shared().start(mMediaProjection);

            MainApp.shared().startService(ScreenRecordService.class);
            this.finish();
        }
    }
}
