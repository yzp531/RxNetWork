package com.rxnetwork.bus;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * by y on 2017/2/23
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
@Deprecated
public class RxBusOlder {
    private final ArrayMap<Object, List<Subject>> rxMap;
    private List<Subject> rxList;

    private RxBusOlder() {
        rxMap = new ArrayMap<>();
    }

    public static RxBusOlder getInstance() {
        return RxbusHolder.rxBus;
    }

    private static class RxbusHolder {
        private static final RxBusOlder rxBus = new RxBusOlder();
    }

    public void send(@NonNull Object tag) {
        send(tag, "");
    }

    public void send(@NonNull Object tag, @NonNull Object object) {
        List<Subject> subjects = rxMap.get(tag);
        if (null != subjects && !subjects.isEmpty()) {
            for (Subject s : subjects) {
                s.onNext(object);
            }
        }
    }

    public void unregister(@NonNull Object tag, @NonNull Observable observable) {
        List<Subject> subjects = rxMap.get(tag);
        if (null != subjects) {
            subjects.remove(observable);
            if (subjects.isEmpty()) {
                rxMap.remove(tag);
            }
        }
    }

    public <T> Observable<T> toObserverable(@NonNull Object tag) {
        rxList = rxMap.get(tag);
        if (null == rxList) {
            rxList = new ArrayList<>();
            rxMap.put(tag, rxList);
        }
        Subject<T, T> subject = PublishSubject.create();
        rxList.add(subject);
        return subject;
    }

    public void clearAllRxBus() {
        if (rxList != null) {
            rxList.clear();
        }
        rxMap.clear();
    }
}