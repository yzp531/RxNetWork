package com.rxnetwork.util;

import android.util.Log;

/**
 * by y on 2017/4/7
 */

public class LogI {

    private static final String TAG = "RxNetWork: ";

    private static boolean isLog = true;

    public static void isLog(boolean isLog) {
        LogI.isLog = isLog;
    }

    public static void i(Object tag, Object o) {
        if (isLog)
            Log.i(String.valueOf(tag), String.valueOf(o));
    }

    public static void i(Object o) {
        if (isLog)
            Log.i(TAG, String.valueOf(o));
    }
}
