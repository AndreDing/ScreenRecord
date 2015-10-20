package com.ialways.screenrecord.ui;

import android.app.Application;

public class MainApp extends Application {

    private static MainApp instance;

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
}
