package com.rxnetwork.sample

import android.app.Application
import android.content.Context
import io.reactivex.network.RxNetWork
import io.reactivex.network.cache.RxCache
import okhttp3.Cache
import java.io.File

/**
 * by y on 2017/2/27
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        /**
         * 建议这里设置统一设置参数
         */
        RxNetWork
                .instance
                .setBaseUrl(Api.ZL_BASE_API)
                .setCache(Cache(File(cacheDir, "SimpleCache"), (1024 * 1024 * 100).toLong()))
                .setCacheInterceptor(SimpleCache.SimpleCacheInterceptor())

        RxCache
                .instance
                .setDiskBuilder(RxCache.DiskBuilder(FileUtils.getDiskCacheDir(this, "RxCache")))
    }

    companion object {
        private var context: Context? = null

        val instance: App
            get() = (context as App?)!!
    }
}
