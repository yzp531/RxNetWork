package com.rxnetwork.sample;

import android.app.Application;

import com.rxnetwork.manager.RxNetWork;

/**
 * by y on 2017/2/27
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 建议这里设置统一设置参数
         */
        RxNetWork
                .getInstance()
                .setBaseUrl("your base url");
    }

}
