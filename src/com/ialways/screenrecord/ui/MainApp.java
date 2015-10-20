package com.ialways.screenrecord.ui;

import java.util.HashMap;

import android.app.Application;
import android.util.Log;

public class MainApp extends Application {

    private static MainApp instance;
    
    final HashMap<Class<?>, Object> mShareds = new HashMap<>();

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
}
