package com.ialways.screenrecord.ui.manager;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;

import com.ialways.screenrecord.ui.Initializable;
import com.ialways.screenrecord.ui.MainApp;
import com.ialways.screenrecord.ui.activity.ScreenRecordActivity;
import com.ialways.screenrecord.ui.service.ScreenRecordService;

/**
 * 项目名称：ScreenRecord 类名称：SensorMgr 类描述： 手机摇一摇功能管理器 创建人：dingchao 创建时间：2015年10月21日
 * 下午2:22:43 修改人：dingchao 修改时间：2015年10月21日 下午2:22:43 修改备注：初始创建
 * 
 * @version 1.0
 */
public class SensorMgr implements Initializable {

    // 震动持续时间
    private final static int VIBRATOR_DURATION = 1000;

    // 加速度传感器
    private SensorManager mSensorMgr;

    // 震动服务
    private Vibrator mVibrator;

    private SensorEventListener mSensorListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            int sensorType = event.sensor.getType();
            // values[0]:X轴，values[1]：Y轴，values[2]：Z轴
            float[] values = event.values;
            if (sensorType == Sensor.TYPE_ACCELEROMETER) {
                if ((Math.abs(values[0]) > 17 || Math.abs(values[1]) > 17 || Math.abs(values[2]) > 17)) {
                    // 摇动手机后停止视频录制，并通过震动提示
                    mVibrator.vibrate(VIBRATOR_DURATION);
                    ScreenRecordMgr.shared().releaseEncoders();
                    ScreenRecordActivity.start(MainApp.shared());
                    MainApp.shared().stopService(ScreenRecordService.class);
                }
            }
        }

        /*
         * 当传感器精度改变时回调该方法(non-Javadoc)
         * 
         * @see
         * android.hardware.SensorEventListener#onAccuracyChanged(android.hardware
         * .Sensor, int)
         */
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public static SensorMgr shared() {
        return MainApp.shared().get(SensorMgr.class);
    }

    @Override
    public void init(Context ctx) {
        // TODO Auto-generated method stub
        this.mSensorMgr = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        this.mVibrator = (Vibrator) ctx.getSystemService(Service.VIBRATOR_SERVICE);
    }

    public void register(SensorEventListener sensorListener) {
        mSensorMgr.registerListener(sensorListener, mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unRegister(SensorEventListener sensorListener) {
        mSensorMgr.unregisterListener(sensorListener);
    }

    public void register() {
        mSensorMgr.registerListener(mSensorListener, mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unRegister() {
        mSensorMgr.unregisterListener(mSensorListener);
    }
}
