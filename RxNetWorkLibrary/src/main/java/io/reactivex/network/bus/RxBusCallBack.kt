package io.reactivex.network.bus

/**
 * by y on 2017/2/23
 */

interface RxBusCallBack<T> {
    fun onBusNext(t: T)

    fun onBusError(throwable: Throwable)

    fun busOfType(): Class<T>
}

