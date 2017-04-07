package com.rxnetwork.manager;

import android.support.annotation.NonNull;

import com.rxnetwork.util.LogI;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


/**
 * by y on 2017/3/10
 */

public class RxSubscriptionManager {

    private CompositeSubscription compositeSubscription;

    private RxSubscriptionManager() {
        compositeSubscription = new CompositeSubscription();
    }

    public static RxSubscriptionManager getInstance() {
        return RxSubscriptionHolder.RX_SUBSCRIPTION_MANAGER;
    }

    private static final class RxSubscriptionHolder {
        private static final RxSubscriptionManager RX_SUBSCRIPTION_MANAGER = new RxSubscriptionManager();
    }

    /**
     * 取消所有订阅
     */
    public void clearSubscription() {
        LogI.i("isUnsubscribed:" + compositeSubscription.isUnsubscribed() +
                "  hasSubscriptions:" + compositeSubscription.hasSubscriptions());
        if (!compositeSubscription.isUnsubscribed() && compositeSubscription.hasSubscriptions()) {
            compositeSubscription.clear();
            LogI.i("compositeSubscription.clear");
        }
        LogI.i("isUnsubscribed:" + compositeSubscription.isUnsubscribed() +
                "  hasSubscriptions:" + compositeSubscription.hasSubscriptions());
    }

    /**
     * 取消订阅
     */
    public void unSubscription(@NonNull Subscription subscription) {
        LogI.i("isUnsubscribed:" + compositeSubscription.isUnsubscribed() +
                "  hasSubscriptions:" + compositeSubscription.hasSubscriptions());
        if (!subscription.isUnsubscribed()) {
            compositeSubscription.remove(subscription);
            LogI.i("compositeSubscription.remove");
        }
        LogI.i(subscription);
        LogI.i("isUnsubscribed:" + compositeSubscription.isUnsubscribed() +
                "  hasSubscriptions:" + compositeSubscription.hasSubscriptions());
    }

    /**
     * 添加订阅
     */
    public void addSubscription(Subscription subscription) {
        LogI.i("addSubscription：" + subscription);
        compositeSubscription.add(subscription);
    }

    public CompositeSubscription getCompositeSubscription() {
        return compositeSubscription;
    }
}
