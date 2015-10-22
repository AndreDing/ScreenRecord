package com.ialways.screenrecord.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.ialways.screenrecord.ui.manager.ScreenRecordMgr;
import com.ialways.screenrecord.ui.manager.SensorMgr;

/**
 * 项目名称：ScreenRecord    
 * 类名称：ScreenRecordService    
 * 类描述： 启动摇一摇Service
 * 创建人：dingchao    
 * 创建时间：2015年10月22日 下午1:38:38    
 * 修改人：dingchao    
 * 修改时间：2015年10月22日 下午1:38:38    
 * 修改备注：    
 * @version 1.0  
 *
 */
public class ScreenRecordService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 启动摇一摇监听
        SensorMgr.shared().register();

        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        // 终止摇一摇监听
        SensorMgr.shared().unRegister();
        
        ScreenRecordMgr.shared().releaseEncoders();

        System.out.println("ScreenRecordService onDestroy");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
