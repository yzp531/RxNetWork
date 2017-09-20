package io.reactivex.network.cache.apply.impl;


import com.google.gson.reflect.TypeToken;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.network.cache.LruDisk;
import io.reactivex.network.cache.apply.Apply;
import io.reactivex.network.cache.result.CacheResult;

/**
 * by y on 20/09/2017.
 */

public class ApplyImpl implements Apply {


    @Override
    public <T> Observable<CacheResult<T>> applyCacheNetWork(@NonNull final Object key,
                                                            Observable<T> observable,
                                                            final LruDisk lruDisk,
                                                            TypeToken<T> typeToken,
                                                            boolean network) {
        if (network) {
            return applyNetWork(key, observable, lruDisk, true);
        }
        final T query = lruDisk.query(key, typeToken);
        if (query == null) {
            return applyNetWork(key, observable, lruDisk, true);
        }
//        return observable.map(new Function<T, CacheResult<T>>() {
//            @Override
//            public CacheResult<T> apply(@NonNull T t) throws Exception {
//                return new CacheResult<>(query, key, CacheResult.CacheType.CACHE);
//            }
//        });
        return Observable.just(new CacheResult<>(query, key, CacheResult.CacheType.CACHE));
    }

    @Override
    public <T> Observable<CacheResult<T>> applyNetWork(@NonNull final Object key, Observable<T> observable, final LruDisk lruDisk, final boolean isInsert) {
        return observable.map(new Function<T, CacheResult<T>>() {
            @Override
            public CacheResult<T> apply(@NonNull T t) throws Exception {
                CacheResult<T> tCacheResult = new CacheResult<>(t, key, CacheResult.CacheType.NETWORK);
                if (isInsert) {
                    lruDisk.insert(key, tCacheResult.getResult());
                }
                return tCacheResult;
            }
        });
    }
}
