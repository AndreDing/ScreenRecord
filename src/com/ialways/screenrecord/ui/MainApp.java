package com.ialways.screenrecord.ui;

import java.util.HashMap;

import android.app.Application;
import android.app.Service;
import android.content.Intent;

import com.ialways.screenrecord.ui.manager.AdMgr;

/**
 * 项目名称：ScreenRecord    
 * 类名称：MainApp    
 * 类描述：主Appliacation
 * 创建人：dingchao    
 * 创建时间：2015年10月22日 下午1:37:50    
 * 修改人：dingchao    
 * 修改时间：2015年10月22日 下午1:37:50    
 * 修改备注：    
 * @version 1.0   
 *
 */
public class MainApp extends Application {

    private static MainApp instance;

    final HashMap<Class<?>, Object> mShareds = new HashMap<>();

    final HashMap<Class<?>, Intent> mServiceMap = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        // 广告管理器需要预加载
        AdMgr.shared();
    }

    public static MainApp shared() {
        if (instance == null) {
            instance = new MainApp();
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    public synchronized <T> T get(Class<T> clazz) {
        if (clazz == null)
            return null;

        final Object old = mShareds.get(clazz);
        if (old != null)
            return (T) old;

        try {
            final T obj = clazz.newInstance();
            if (obj instanceof Initializable) {
                ((Initializable) obj).init(this);
            }
            mShareds.put(clazz, obj);
            return obj;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T extends Service> void startService(Class<T> service) {
        Intent intent = new Intent(this, service);
        this.startService(intent);
        mServiceMap.put(service, intent);
    }

    public <T extends Service> void stopService(Class<T> service) {
        Intent intent = mServiceMap.get(service);
        if (intent != null) {
            this.stopService(intent);
            mServiceMap.remove(service);
        }
    }
}
