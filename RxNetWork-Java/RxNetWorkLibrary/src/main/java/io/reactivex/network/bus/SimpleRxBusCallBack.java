package io.reactivex.network.bus;

/**
 * by y on 2017/5/22
 */

public abstract class SimpleRxBusCallBack<T> implements RxBusCallBack<T> {
    @Override
    public void onBusNext(T t) {

    }

    @Override
    public void onBusError(Throwable throwable) {

    }
}
