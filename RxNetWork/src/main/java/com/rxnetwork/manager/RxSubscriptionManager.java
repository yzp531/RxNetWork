package com.rxnetwork.manager;

import android.support.annotation.NonNull;

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

    public void clearSubscription() {
        compositeSubscription.clear();
    }

    public void unSubscription(@NonNull Subscription subscription) {
        compositeSubscription.remove(subscription);
    }

    public void addSubscription(Subscription subscription) {
        compositeSubscription.add(subscription);
    }

    public CompositeSubscription getCompositeSubscription() {
        return compositeSubscription;
    }
}
