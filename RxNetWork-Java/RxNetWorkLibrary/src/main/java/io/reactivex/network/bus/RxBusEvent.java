package io.reactivex.network.bus;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.Subject;

/**
 * by y on 2017/5/22
 */

class RxBusEvent {
    Subject<Object> subject;
    DisposableObserver disposable;

    RxBusEvent() {
    }
}
