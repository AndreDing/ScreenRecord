package com.ialways.screenrecord.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.ialways.screenrecord.ui.manager.ScreenRecordMgr;
import com.ialways.screenrecord.ui.manager.SensorMgr;

public class ScreenRecordService extends Service {

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        System.out.println("ScreenRecordService onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

        // 启动摇一摇监听
        SensorMgr.shared().register();

        System.out.println("ScreenRecordService onStart");
        return super.onStartCommand(intent, flags, startId);
    }

    // @Override
    // public void onStart(Intent intent, int startId) {
    // // TODO Auto-generated method stub
    // super.onStart(intent, startId);
    //
    // // 启动摇一摇监听
    // SensorMgr.shared().register();
    //
    // System.out.println("ScreenRecordService onStart");
    // }
    //
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        // 终止摇一摇监听
        SensorMgr.shared().unRegister();
        
        ScreenRecordMgr.shared().releaseEncoders();

        System.out.println("ScreenRecordService onDestroy");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

}
