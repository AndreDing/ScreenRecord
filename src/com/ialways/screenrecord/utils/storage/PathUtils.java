package com.ialways.screenrecord.utils.storage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ialways.screenrecord.common.Constants;

import android.content.Context;

public final class PathUtils {

    /** 应用目录数据保存根目录 */
    public static final String PATH_APP_DIR = "screenrecord";

    /** 相对根目录：字体资源存储目录 */
    public static final String PATH_SUB_DIR_FONT = "font";

    /** 相对根目录：crash资源存储目录 */
    public static final String PATH_SUB_DIR_CRASH = "crash";

    /** 相对根目录：缓冲目录 */
    public static final String PATH_SUB_DIR_CACHE = "cache";

    /** 屏幕录制资源目录 */
    public static final String PATH_SUB_DIR_RECORD = "movie/record";

    /** plugin 保存位置：默认统一保存在内置存储空间，只有在内置空间不足时才会保存在外置SD卡 */
    public static final String PATH_SUB_DIR_DEX = "plugin";

    private PathUtils() {
    }

    private static File makeDir(Context context, boolean isPrivate) {
        File storageDir = StorageUtils.getStorageDir(context, isPrivate);
        File appDir = new File(storageDir, PATH_APP_DIR);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        return appDir;
    }

    private static File makeSubDir(Context context, String subPath, boolean isPrivate) {
        File appDir = PathUtils.makeDir(context, isPrivate);
        File subDir = new File(appDir, subPath);
        if (!subDir.exists()) {
            subDir.mkdirs();
        }
        return subDir;
    }

    public static String getRecordFilePath(Context context) {
        String parentPath = getRecordDir(context).getAbsolutePath();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");     
        String date = sDateFormat.format(new Date());  
        return parentPath + Constants.PunctuationConstants.SLASH + date + Constants.FileConstants.MOVIE_RECORD_FORMAT;
    }

    public static File getRecordDir(Context context) {
        return PathUtils.makeSubDir(context, PATH_SUB_DIR_RECORD, false);
    }

    public static File getFontDir(Context context) {
        return PathUtils.makeSubDir(context, PATH_SUB_DIR_FONT, false);
    }

    public static File getCacheDir(Context context) {
        return PathUtils.makeSubDir(context, PATH_SUB_DIR_CACHE, false);
    }

    /**
     * Android 4.1.2 Api 中有对 Dex 做了限制，Dex 文件只能放在私有目录下
     */
    public static File getDexDir(Context context) {
        return PathUtils.makeSubDir(context, PATH_SUB_DIR_DEX, true);
    }

}
