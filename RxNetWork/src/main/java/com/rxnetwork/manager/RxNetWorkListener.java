package com.rxnetwork.manager;

/**
 * by y on 2017/2/22.
 */

public interface RxNetWorkListener<T>{

    void onNetWorkStart();

    void onNetWorkError(Throwable e);

    void onNetWorkCompleted();

    void onNetWorkSuccess(T data);
}
