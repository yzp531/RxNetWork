package com.rxnetwork.sample;

import android.app.Application;
import android.content.Context;

import java.io.File;

import io.reactivex.network.manager.RxNetWork;
import okhttp3.Cache;

/**
 * by y on 2017/2/27
 */

public class App extends Application {

    private static Context instance;

    public static Context getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = getApplicationContext();
        /**
         * 建议这里设置统一设置参数
         */
        RxNetWork
                .getInstance()
                .setBaseUrl(Api.ZL_BASE_API)
                .setCache(new Cache(new File(getCacheDir(), "SimpleCache"), 1024 * 1024 * 100))
                .setCacheInterceptor(new SimpleCache.SimpleCacheInterceptor());
    }

}
