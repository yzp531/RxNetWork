package com.rxnetwork.bus;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.rxnetwork.util.LogI;
import com.rxnetwork.util.RxUtils;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;
import rx.subscriptions.CompositeSubscription;

/**
 * by y on 2017/2/22.
 */
public class RxBus {

    private final ArrayMap<Object, Subject<Object, Object>> rxMap;
    private CompositeSubscription compositeSubscription;

    private static final String TAG = "RxBus";

    private RxBus() {
        rxMap = new ArrayMap<>();
        compositeSubscription = new CompositeSubscription();
    }

    public static RxBus getInstance() {
        return RxBusHolder.rxBus;
    }

    private static class RxBusHolder {
        private static final RxBus rxBus = new RxBus();
    }

    /**
     * 发送一个带tag,但不携带信息的消息
     *
     * @param tag 标志
     */
    public void send(@NonNull Object tag) {
        send(tag, tag);
    }

    /**
     * 发送一个带tag,携带信息的消息
     *
     * @param tag     标志
     * @param message 内容
     */
    public void send(@NonNull Object tag,
                     @NonNull Object message) {
        if (RxUtils.isEmpty(rxMap, tag)) {
            LogI.i(TAG, "tag:" + tag + "  message:" + message);
            rxMap.get(tag).onNext(message);
        }
    }

    /**
     * 接受消息
     *
     * @param tag      标志
     * @param service  类型
     * @param callBack 回调
     */
    public <T> Subscription toSubscription(@NonNull final Object tag,
                                           @NonNull Class<T> service,
                                           @NonNull final RxBusCallBack<T> callBack) {
        Subject<Object, Object> subject = rxMap.get(tag);
        if (RxUtils.isEmpty(subject)) {
            subject = new SerializedSubject<>(PublishSubject.create());
            rxMap.put(tag, subject);
        }
        Subscription subscribe = subject
                .ofType(service)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new RxBusSubscriber<T>() {
                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                callBack.onError(e);
                            }

                            @Override
                            public void onNext(T t) {
                                super.onNext(t);
                                callBack.onNext(t);
                            }
                        });
        compositeSubscription.add(subscribe);
        return subscribe;

    }


    /**
     * 取消订阅
     *
     * @param tag          标志
     * @param subscription 当前tag关联的subscription
     */
    public void unregister(@NonNull Object tag, @NonNull Subscription subscription) {
        LogI.i(TAG, "RxMap:" + rxMap.size() +
                "  isUnsubscribed:" + compositeSubscription.isUnsubscribed() +
                "  hasSubscriptions:" + compositeSubscription.hasSubscriptions());
        if (!subscription.isUnsubscribed()) {
            compositeSubscription.remove(subscription);
            LogI.i(TAG, "compositeSubscription.remove");
        }

        if (RxUtils.isEmpty(rxMap, tag)) {
            rxMap.remove(tag);
            LogI.i(TAG, "unregister:" + tag);
        }
        LogI.i(TAG, "RxMap:" + rxMap.size() +
                "  isUnsubscribed:" + compositeSubscription.isUnsubscribed() +
                "  hasSubscriptions:" + compositeSubscription.hasSubscriptions());
    }

    /**
     * 取消所有订阅
     */
    public void unregisterAll() {
        LogI.i(TAG, "RxMap:" + rxMap.size() +
                "  isUnsubscribed:" + compositeSubscription.isUnsubscribed() +
                "  hasSubscriptions:" + compositeSubscription.hasSubscriptions());

        if (!compositeSubscription.isUnsubscribed() && compositeSubscription.hasSubscriptions()) {
            compositeSubscription.clear();
            LogI.i(TAG, "compositeSubscription.clear");
        }

        rxMap.clear();

        LogI.i(TAG, "RxMap:" + rxMap.size() +
                "  isUnsubscribed:" + compositeSubscription.isUnsubscribed() +
                "  hasSubscriptions:" + compositeSubscription.hasSubscriptions());
    }

    public CompositeSubscription getCompositeSubscription() {
        return compositeSubscription;
    }

    private static class RxBusSubscriber<T> extends Subscriber<T> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(T t) {

        }
    }
}
