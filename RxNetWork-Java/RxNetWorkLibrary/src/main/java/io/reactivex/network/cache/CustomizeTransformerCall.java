package io.reactivex.network.cache;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.network.cache.result.CacheResult;

/**
 * by y on 12/10/2017.
 */

public interface CustomizeTransformerCall {
    <T> ObservableSource<CacheResult<T>> applyCustomize(@NonNull Object key, Observable<T> upstream);
}
