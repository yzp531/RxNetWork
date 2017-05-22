package io.reactivex.jsoup.network.bus;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * by y on 2017/5/22
 */

class RxBusObserver<T> extends DisposableObserver<T> {
    @Override
    public void onNext(@NonNull T t) {

    }

    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
