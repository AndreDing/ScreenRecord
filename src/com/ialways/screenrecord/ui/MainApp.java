package com.ialways.screenrecord.ui;

import java.util.HashMap;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Intent;

public class MainApp extends Application {

    private static MainApp instance;
    
    final HashMap<Class<?>, Object> mShareds = new HashMap<>();
    
    final HashMap<Class<?>, Intent> mServiceMap = new HashMap<>();

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        instance = this;
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
        System.out.println("startService");
        Intent intent = new Intent(this, service);
        this.startService(intent);
        mServiceMap.put(service, intent);
    }
    
    public <T extends Service> void stopService(Class<T> service)  {
        Intent intent = mServiceMap.get(service);
        if (intent != null) {
            this.stopService(intent);
            mServiceMap.remove(service);
        }
    }
}
