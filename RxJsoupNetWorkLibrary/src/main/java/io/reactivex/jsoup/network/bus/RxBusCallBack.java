package io.reactivex.jsoup.network.bus;

/**
 * by y on 2017/2/23
 */

public interface RxBusCallBack<T> {
    void onBusNext(T t);

    void onBusError(Throwable throwable);

    Class<T> busOfType();
}

