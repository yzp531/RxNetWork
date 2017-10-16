package io.reactivex.network.bus

import io.reactivex.observers.DisposableObserver
import io.reactivex.subjects.Subject

/**
 * by y on 2017/5/22
 */

internal class RxBusEvent {
    var subject: Subject<Any>? = null
    var disposable: DisposableObserver<*>? = null
}
