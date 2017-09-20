package io.reactivex.network.util;

import java.io.IOException;

import okhttp3.internal.cache.DiskLruCache;

/**
 * by y on 20/09/2017.
 */

public class OkioUtils {


    public static void abort(DiskLruCache.Editor editor) {
        if (editor != null) {
            try {
                editor.abort();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void snapshot(DiskLruCache.Snapshot snapshot) {
        if (snapshot != null) {
            snapshot.close();
        }
    }
}
