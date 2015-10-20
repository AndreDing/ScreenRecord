package com.ialways.screenrecord.utils.storage;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

class StorageManagerCompat {

    interface StorageManagerCompatImpl {
        public Object getStorageManager(Context context);

        public String[] getVolumePaths(Object manager);
    }

    static class EarlyStorageManagerCompatImpl implements StorageManagerCompatImpl {

        @Override
        public Object getStorageManager(Context context) {
            return null;
        }

        @Override
        public String[] getVolumePaths(Object manager) {
            return null;
        }
    }

    static class GingerbreadStorageManagerCompatImpl implements StorageManagerCompatImpl {

        @Override
        public Object getStorageManager(Context context) {
            return StorageManagerCompatGingerbread.getStorageManager(context);
        }

        @Override
        public String[] getVolumePaths(Object manager) {
            return StorageManagerCompatGingerbread.getVolumePaths(manager);
        }
    }

    public static StorageManagerCompat get(Context context) {
        return new StorageManagerCompat(context);
    }

    private final Object manager;
    private final StorageManagerCompatImpl impl;

    private StorageManagerCompat(Context context) {
        this(Build.VERSION.SDK_INT, context);
    }

    private StorageManagerCompat(int apiVersion, Context context) {
        super();

        checkPermission(context);

        if (apiVersion >= Build.VERSION_CODES.GINGERBREAD) {
            impl = new GingerbreadStorageManagerCompatImpl();
        } else {
            impl = new EarlyStorageManagerCompatImpl();
        }
        manager = impl.getStorageManager(context);
    }

    /**
     * 判断当前进程是否有写外置存储权限和
     * @param context
     */
    private void checkPermission(Context context) {
        if (context.checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && context.checkCallingOrSelfPermission(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS) != PackageManager.PERMISSION_GRANTED) {
        }
    }

    /**
     * 仅仅在Android2.3版本以后有效
     * @return
     */
    public String[] getVolumePaths() {
        return impl.getVolumePaths(manager);
    }
}
