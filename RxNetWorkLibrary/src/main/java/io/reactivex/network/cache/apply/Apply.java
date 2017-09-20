package io.reactivex.network.cache.apply;

import com.google.gson.reflect.TypeToken;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.network.cache.LruDisk;
import io.reactivex.network.cache.result.CacheResult;

/**
 * by y on 20/09/2017.
 */

public interface Apply {
    <T> Observable<CacheResult<T>> applyCacheNetWork(@NonNull Object key, Observable<T> observable, LruDisk lruDisk, TypeToken<T> typeToken, boolean network);

    <T> Observable<CacheResult<T>> applyNetWork(@NonNull Object key, Observable<T> observable, LruDisk lruDisk, boolean isInsert);
}
