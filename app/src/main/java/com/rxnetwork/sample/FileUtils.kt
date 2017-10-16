package com.rxnetwork.sample

import android.content.Context
import android.os.Environment

import java.io.File

/**
 * by y on 20/09/2017.
 */

object FileUtils {


    fun getDiskCacheDir(context: Context, fileName: String): File {
        val cachePath: String
        cachePath = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
            val externalCacheDir = context.externalCacheDir!!
            externalCacheDir.path
        } else {
            context.cacheDir.path
        }
        return File(cachePath + File.separator + fileName)
    }

}
