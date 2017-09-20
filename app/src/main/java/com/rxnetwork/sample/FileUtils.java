package com.rxnetwork.sample;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * by y on 20/09/2017.
 */

public class FileUtils {


    public static File getDiskCacheDir(Context context, String fileName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            File externalCacheDir = context.getExternalCacheDir();
            assert externalCacheDir != null;
            cachePath = externalCacheDir.getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + fileName);
    }

}
