package io.reactivex.network.cache.result;

/**
 * by y on 20/09/2017.
 */

public class CacheResult<T> {


    private T result;
    private Object key;
    private CacheType type;

    public CacheResult(T result, Object key, CacheType type) {
        this.result = result;
        this.key = key;
        this.type = type;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public CacheType getType() {
        return type;
    }

    public void setType(CacheType type) {
        this.type = type;
    }

    public enum CacheType {
        NETWORK, CACHE
    }
}
