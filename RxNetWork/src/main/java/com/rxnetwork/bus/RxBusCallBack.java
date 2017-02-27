package com.rxnetwork.bus;

/**
 * by y on 2017/2/23
 */

public interface RxBusCallBack<T> {
    void onNext(T data);

    void onError(Throwable throwable);
}
