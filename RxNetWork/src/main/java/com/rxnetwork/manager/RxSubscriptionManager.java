package com.rxnetwork.manager;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * by y on 2017/3/10
 */

public class RxSubscriptionManager {

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    private RxSubscriptionManager() {
    }

    public static RxSubscriptionManager getInstance() {
        return new RxSubscriptionManager();
    }

    public void clearSubscription() {
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.clear();
        }
    }

    public void unSubscription() {
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Subscription subscription) {
        if (compositeSubscription != null) {
            compositeSubscription.add(subscription);
        }
    }
}
