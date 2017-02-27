package com.rxnetwork.manager;


import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.rxnetwork.util.RxUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * by y on 2017/2/22.
 */
public class RxNetWork {
    private int timeout_time = 15;
    private boolean isRetryOnConnectionFailure = true;
    private Gson mGson = null;
    private String baseUrl = null;
    private OkHttpClient mOkHttpClient = null;
    private Converter.Factory mConverterFactory = null;
    private CallAdapter.Factory mAdapterFactory = null;

    private Interceptor mLogInterceptor = null;
    private Interceptor mHeaderInterceptor = null;
    private Interceptor mCacheInterceptor = null;
    private Cache mCache = null;
    private CompositeSubscription compositeSubscription = null;

    private RxNetWork() {
    }

    private static final class RxNetWorkHolder {

        private static final RxNetWork rxNetWork = new RxNetWork();
    }

    public static RxNetWork getInstance() {
        return RxNetWorkHolder.rxNetWork;
    }

    public RxNetWork setRetryOnConnectionFailure(boolean retryOnConnectionFailure) {
        isRetryOnConnectionFailure = retryOnConnectionFailure;
        return this;
    }

    public RxNetWork setBaseUrl(@NonNull String url) {
        this.baseUrl = url;
        return this;
    }

    public RxNetWork setGson(@NonNull Gson gson) {
        this.mGson = gson;
        return this;
    }

    public RxNetWork setOkHttpClient(@NonNull OkHttpClient okHttpClient) {
        this.mOkHttpClient = okHttpClient;
        return this;
    }

    public RxNetWork setLogInterceptor(@NonNull Interceptor mLogInterceptor) {
        this.mLogInterceptor = mLogInterceptor;
        return this;
    }

    public RxNetWork setHeaderInterceptor(@NonNull Interceptor mHeaderInterceptor) {
        this.mHeaderInterceptor = mHeaderInterceptor;
        return this;
    }

    public RxNetWork setCacheInterceptor(@NonNull Interceptor mCacheInterceptor) {
        this.mCacheInterceptor = mCacheInterceptor;
        return this;
    }

    public RxNetWork setCache(@NonNull Cache mCache) {
        this.mCache = mCache;
        return this;
    }

    public RxNetWork setTimeoutTime(int timeout_time) {
        this.timeout_time = timeout_time;
        return this;
    }

    public RxNetWork setConverterFactory(@NonNull Converter.Factory factory) {
        this.mConverterFactory = factory;
        return this;

    }

    public RxNetWork setAdapterFactory(@NonNull CallAdapter.Factory factory) {
        this.mAdapterFactory = factory;
        return this;
    }

    public void clearSubscription() {
        if (!RxUtils.isEmpty(compositeSubscription) && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.clear();
        }
    }

    public static <T> T observable(Class<T> service) {
        return getInstance().getRetrofit().create(service);
    }

    public <M> void getApi(Observable<M> observable, final RxNetWorkListener<M> listener) {
        compositeSubscription = new CompositeSubscription();
        listener.onNetWorkStart();
        Subscription subscribe = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<M>() {
                    @Override
                    public void onCompleted() {
                        listener.onNetWorkCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onNetWorkError(e);
                    }

                    @Override
                    public void onNext(M m) {
                        listener.onNetWorkSuccess(m);
                    }
                });
        compositeSubscription.add(subscribe);
    }


    private Retrofit getRetrofit() {
        if (RxUtils.isEmpty(mOkHttpClient)) {
            mOkHttpClient = initOkHttp();
        }
        if (RxUtils.isEmpty(mConverterFactory)) {
            rxNetWorkConverterFactory();
        }
        if (RxUtils.isEmpty(mAdapterFactory)) {
            rxNetWorkAdapterFactory();
        }
        return new Retrofit.Builder()
                .client(mOkHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(mConverterFactory)
                .addCallAdapterFactory(mAdapterFactory)
                .build();
    }

    private OkHttpClient initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (!RxUtils.isEmpty(mLogInterceptor))
            builder.addInterceptor(mLogInterceptor);
        if (!RxUtils.isEmpty(mHeaderInterceptor))
            builder.addInterceptor(mHeaderInterceptor);
        if (!RxUtils.isEmpty(mCacheInterceptor) && !RxUtils.isEmpty(mCache)) {
            builder.cache(mCache);
            builder.addNetworkInterceptor(mCacheInterceptor);
            builder.addInterceptor(mCacheInterceptor);
        }
        builder.connectTimeout(timeout_time, TimeUnit.SECONDS)
                .writeTimeout(timeout_time, TimeUnit.SECONDS)
                .readTimeout(timeout_time, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(isRetryOnConnectionFailure);
        return builder.build();
    }

    private void rxNetWorkConverterFactory() {
        if (RxUtils.isEmpty(mGson)) {
            mGson = new Gson();
        }
        mConverterFactory = GsonConverterFactory.create(mGson);
    }

    private void rxNetWorkAdapterFactory() {
        mAdapterFactory = RxJavaCallAdapterFactory.create();
    }
}
