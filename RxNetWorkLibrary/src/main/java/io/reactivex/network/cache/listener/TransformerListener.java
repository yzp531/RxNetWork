package io.reactivex.network.cache.listener;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.network.cache.result.CacheResult;

/**
 * by y on 20/09/2017.
 */

public interface TransformerListener<T> {
    Observable<CacheResult<T>> transformer(@NonNull Object key, Observable<T> observable);
}
