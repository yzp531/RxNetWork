package io.reactivex.network.cache;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.network.cache.result.CacheResult;

/**
 * by y on 20/09/2017.
 */

public class RxCache {


    private RxCache() {
    }

    private static final class RxCacheHolder {
        private static final RxCache rxCache = new RxCache();
    }

    public static RxCache getInstance() {
        return RxCacheHolder.rxCache;
    }

    /**
     * 优先级： 内存 --> 文件 --> 网络
     */
    public <T> ObservableTransformer<T, CacheResult<T>> transformerMCN(@NonNull final Object key) {
        return transformer(key, Type.MEMORY_CACHE_NETWORK);
    }

    /**
     * 优先级： 文件 --> 网络
     */
    public <T> ObservableTransformer<T, CacheResult<T>> transformerCN(@NonNull final Object key) {
        return transformer(key, Type.CACHE_NETWORK);
    }

    /**
     * 优先级： 内存 -->  网络
     */
    public <T> ObservableTransformer<T, CacheResult<T>> transformerMN(@NonNull final Object key) {
        return transformer(key, Type.MEMORY_NETWORK);
    }

    private <T> ObservableTransformer<T, CacheResult<T>> transformer(@NonNull final Object key, final Type type) {
        return new ObservableTransformer<T, CacheResult<T>>() {
            @Override
            public ObservableSource<CacheResult<T>> apply(@NonNull Observable<T> upstream) {
                switch (type) {
                    case MEMORY_CACHE_NETWORK:
                        return null;
                    case MEMORY_NETWORK:
                        return null;
                    case CACHE_NETWORK:
                        return null;
                }
                return null;
            }
        };
    }
}
