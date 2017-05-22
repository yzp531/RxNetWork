package io.reactivex.network.manager;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * by y on 2017/2/22.
 */

public interface RxNetWorkListener<T> {

    void onNetWorkStart();

    void onNetWorkError(Throwable e);

    void onNetWorkComplete();

    void onNetWorkSuccess(T data);

    @NonNull
    Observable<T> getObservable(Retrofit retrofit);
}
