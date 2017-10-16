package io.reactivex.network.bus

import android.support.v4.util.ArrayMap
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.network.util.RxUtils
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 * by y on 2017/2/22.
 */
class RxBus private constructor() {
    private val rxBusEventArrayMap: ArrayMap<Any, RxBusEvent> = ArrayMap()

    private object RxBusHolder {
        val rxBus = RxBus()
    }

    fun post(obj: Any): Boolean = post(obj, obj)

    fun post(tag: Any, obj: Any): Boolean {
        val rxBusEvent = rxBusEventArrayMap[tag]
        if (!RxUtils.isEmpty(rxBusEvent)) {
            rxBusEvent!!.subject!!.onNext(obj)
            return true
        }
        return false
    }

    /**
     * 接受消息
     *
     * @param tag      标志
     * @param callBack 回调
     */
    fun <T> register(tag: Any,
                     callBack: RxBusCallBack<T>): DisposableObserver<*>? {
        var rxBusEvent = rxBusEventArrayMap[tag]
        if (RxUtils.isEmpty(rxBusEvent)) {
            rxBusEvent = RxBusEvent()
            rxBusEvent.subject = PublishSubject.create<Any>().toSerialized()
            rxBusEvent.disposable = rxBusEvent.subject!!
                    .ofType(callBack.busOfType())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : RxBusObserver<T>() {
                        override fun onError(@io.reactivex.annotations.NonNull e: Throwable) {
                            super.onError(e)
                            callBack.onBusError(e)
                        }

                        override fun onNext(@io.reactivex.annotations.NonNull t: T) {
                            super.onNext(t)
                            callBack.onBusNext(t)
                        }
                    })
            rxBusEventArrayMap.put(tag, rxBusEvent)
        }
        return rxBusEvent!!.disposable
    }

    /**
     * 取消订阅
     *
     * @param tag 标志
     * @return true 取消成功
     */
    fun unregister(tag: Any): Boolean {
        val rxBusEvent = rxBusEventArrayMap[tag]
        if (RxUtils.isEmpty(rxBusEvent)) {
            return true
        }
        val subject = rxBusEvent!!.subject
        val disposable = rxBusEvent!!.disposable
        if (!disposable!!.isDisposed) {
            disposable.dispose()
        }
        if (!subject!!.hasObservers()) {
            rxBusEventArrayMap.remove(tag)
            return true
        }
        return false
    }

    /**
     * 取消所有订阅
     *
     * @return 返回false 证明还有订阅没有被取消,注意内存泄漏...
     */
    fun unregisterAll(): Boolean {
        for ((key) in rxBusEventArrayMap) {
            val unregister = unregister(key)
            if (!unregister) {
                return false
            }
        }
        return true
    }

    companion object {

        val instance: RxBus
            get() = RxBusHolder.rxBus
    }
}
