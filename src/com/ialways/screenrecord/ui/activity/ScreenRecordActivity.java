/**
 * Code from http://mattsnider.com/video-recording-with-mediaprojectionmanager/
 * author:mattsnider
 */
package com.ialways.screenrecord.ui.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ialways.screenrecord.R;
import com.ialways.screenrecord.ui.MainApp;
import com.ialways.screenrecord.ui.manager.AdMgr;
import com.ialways.screenrecord.ui.manager.ScreenRecordMgr;
import com.ialways.screenrecord.ui.service.ScreenRecordService;

/**
 * 项目名称：ScreenRecord 类名称：ScreenRecordActivity 类描述： 主Activity 创建人：dingchao
 * 创建时间：2015年10月21日 上午11:17:25 修改人：dingchao 修改时间：2015年10月21日 上午11:17:25 修改备注：
 * 
 * @version 1.0
 */
public class ScreenRecordActivity extends BaseFragmentActivity {

    private MediaProjectionManager mMediaMgr;

    private static final int REQUEST_CODE_CAPTURE = 1234;

    private MediaProjection mMediaProjection;

    private Button mStartBtn;

    private TextView mFileTv;
    
    private boolean isExit;

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

        mFileTv = (TextView) this.findViewById(R.id.file_path);
        mFileTv.setText(ScreenRecordMgr.shared().getCurFilePath());

        this.mMediaMgr = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

//        AdMgr.shared().showImageTextBanner(this);
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

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

        Timer tExit = null;
        if (isExit == false) {
            isExit = true; 
            // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000); 
        } else {
            AdMgr.shared().clear();
            finish();
            System.exit(0);
        }
    }
}
