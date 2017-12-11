package io.reactivex.network;

/**
 * by y on 2017/2/22.
 */

public interface RxNetWorkListener<T> {

    void onNetWorkStart();

    void onNetWorkError(Throwable e);

    void onNetWorkComplete();

    void onNetWorkSuccess(T data);
}
