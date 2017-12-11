package com.rxnetwork.sample;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.File;

import io.reactivex.network.RxNetWork;
import io.reactivex.network.cache.RxCache;
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
                .setBaseUrl(Api.ZL_BASE_API);

        RxCache
                .getInstance()
                .setDiskBuilder(new RxCache.DiskBuilder(FileUtils.getDiskCacheDir(this, "RxCache")));
    }

    public int getVersion() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

}
