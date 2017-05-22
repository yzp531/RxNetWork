package io.reactivex.network.util;

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
}
