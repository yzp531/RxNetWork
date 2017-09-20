package io.reactivex.network.cache;


import com.google.gson.reflect.TypeToken;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.network.cache.apply.impl.ApplyImpl;
import io.reactivex.network.cache.result.CacheResult;

/**
 * by y on 20/09/2017.
 */

public class RxCache {


    private ApplyImpl apply;
    private LruDisk lruDisk;

    private RxCache() {
        apply = new ApplyImpl();
    }

    public RxCache setDiskBuilder(DiskBuilder diskBuilder) {
        lruDisk = new LruDisk(diskBuilder.file, diskBuilder.version, diskBuilder.valueCount, diskBuilder.maxSize);
        return this;
    }

    public static class DiskBuilder {
        public File file;
        public int version = 1;
        public int valueCount = 1;
        public int maxSize = 50 * 1024 * 1024;

        public DiskBuilder(File file) {
            this.file = file;
        }

        public DiskBuilder(File file, int version) {
            this.file = file;
            this.version = version;
        }

        public DiskBuilder(File file, int version, int maxSize) {
            this.file = file;
            this.version = version;
            this.maxSize = maxSize;
        }

        public DiskBuilder(File file, int version, int valueCount, int maxSize) {
            this.file = file;
            this.version = version;
            this.valueCount = valueCount;
            this.maxSize = maxSize;
        }
    }

    /**
     * 优先级： 文件 --> 网络
     *
     * @param network :true 有网的情况下，获取网络数据并缓存，没网的情况再获取缓存
     */
    public <T> ObservableTransformer<T, CacheResult<T>> transformerCN(@NonNull final Object key, boolean network, TypeToken<T> typeToken) {
        return transformer(key, Type.CACHE_NETWORK, typeToken, network);
    }

    /**
     * 只走网络
     */
    public <T> ObservableTransformer<T, CacheResult<T>> transformerN() {
        return transformer("", Type.NETWORK, null, false);
    }

    private <T> ObservableTransformer<T, CacheResult<T>> transformer(@NonNull final Object key, final Type type, final TypeToken<T> typeToken, final boolean network) {
        return new ObservableTransformer<T, CacheResult<T>>() {
            @Override
            public ObservableSource<CacheResult<T>> apply(@NonNull Observable<T> upstream) {
                switch (type) {
                    case CACHE_NETWORK:
                        return apply.applyCacheNetWork(key, upstream, lruDisk, typeToken, network);
                    case NETWORK:
                        return apply.applyNetWork(key, upstream, lruDisk, false);
                }
                return null;
            }
        };
    }


    private static final class RxCacheHolder {
        private static final RxCache rxCache = new RxCache();
    }

    public static RxCache getInstance() {
        return RxCacheHolder.rxCache;
    }

    public boolean onDestroy() {
        return lruDisk != null && lruDisk.onDestroy();
    }
}
