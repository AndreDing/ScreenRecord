package com.ialways.screenrecord.utils.storage;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.StatFs;

public class StatFsCompat {

    interface StatFsCompatImpl {
        public long getBlockSizeLong(StatFs statfs);

        public long getBlockCountLong(StatFs statfs);

        public long getAvailableBlocksLong(StatFs statfs);
    }

    static class EarlyStatFsCompatImpl implements StatFsCompatImpl {

        @SuppressWarnings("deprecation")
        @Override
        public long getBlockSizeLong(StatFs statfs) {
            return statfs.getBlockSize();
        }

        @SuppressWarnings("deprecation")
        @Override
        public long getBlockCountLong(StatFs statfs) {
            return statfs.getBlockCount();
        }

        @SuppressWarnings("deprecation")
        @Override
        public long getAvailableBlocksLong(StatFs statfs) {
            return statfs.getAvailableBlocks();
        }
    }

    static class JellyBeanMr2StatFsCompatImpl implements StatFsCompatImpl {

        @Override
        public long getBlockSizeLong(StatFs statfs) {
            return StatFsCompatJellyBeanMr2.getBlockSizeLong(statfs);
        }

        @Override
        public long getBlockCountLong(StatFs statfs) {
            return StatFsCompatJellyBeanMr2.getBlockCountLong(statfs);
        }

        @Override
        public long getAvailableBlocksLong(StatFs statfs) {
            return StatFsCompatJellyBeanMr2.getAvailableBlocksLong(statfs);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static class StatFsCompatJellyBeanMr2 {

        private StatFsCompatJellyBeanMr2() {
            super();
        }

        public static long getBlockSizeLong(StatFs statfs) {
            return statfs.getBlockSizeLong();
        }

        public static long getBlockCountLong(StatFs statfs) {
            return statfs.getBlockCountLong();
        }

        public static long getAvailableBlocksLong(StatFs statfs) {
            return statfs.getAvailableBlocksLong();
        }

    }

    public static StatFsCompat get(String path) {
        return new StatFsCompat(path);
    }

    private StatFs statFs;
    private final StatFsCompatImpl impl;

    private StatFsCompat(String path) {
        this(Build.VERSION.SDK_INT, path);
    }

    private StatFsCompat(int apiVersion, String path) {
        super();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            impl = new JellyBeanMr2StatFsCompatImpl();
        } else {
            impl = new EarlyStatFsCompatImpl();
        }
        statFs = new StatFs(path);
    }

    public long getBlockSizeLong() {
        return impl.getBlockSizeLong(statFs);
    }

    public long getBlockCountLong() {
        return impl.getBlockCountLong(statFs);
    }

    public long getAvailableBlocksLong() {
        return impl.getAvailableBlocksLong(statFs);
    }

}
