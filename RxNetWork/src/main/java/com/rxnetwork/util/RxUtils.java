package com.rxnetwork.util;

import java.util.Map;

/**
 * by y on 2017/2/22.
 */

public class RxUtils {

    public static boolean isEmpty(Object... objects) {
        for (Object o : objects) {
            if (o == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(Map map, Object key) {
        return map.containsKey(key);
    }
}
