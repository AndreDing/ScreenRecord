package com.ialways.screenrecord.utils.storage;

import java.lang.reflect.Method;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.storage.StorageManager;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
class StorageManagerCompatGingerbread {

    public static Object getStorageManager(Context context) {
        StorageManager manager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        return manager;
    }

    public static String[] getVolumePaths(Object manager) {
        String[] paths = null;
        try {
            Class<?> clazz = StorageManager.class;
            Method method = clazz.getMethod("getVolumePaths");
            method.setAccessible(true);
            Object invoke = method.invoke(manager);
            if (invoke != null && invoke instanceof String[]) {
                paths = (String[]) invoke;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paths;
    }
}
