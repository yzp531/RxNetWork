package io.reactivex.network.manager;


import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.network.util.RxUtils;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * by y on 2017/2/22.
 */
public class RxNetWork {

    public static final String TAG = "RxNetWork";
    private final ArrayMap<Object, Disposable> arrayMap;
    private int timeout_time = 15;
    private boolean isRetryOnConnectionFailure = true;
    private Gson mGson = null;
    private String baseUrl = null;
    private OkHttpClient mOkHttpClient = null;
    private Retrofit retrofit = null;
    private Converter.Factory mConverterFactory = null;
    private CallAdapter.Factory mAdapterFactory = null;

    private Interceptor mLogInterceptor = null;
    private Interceptor mHeaderInterceptor = null;
    private Interceptor mCacheInterceptor = null;
    private Cache mCache = null;

    private RxNetWork() {
        arrayMap = new ArrayMap<>();
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

    public RxNetWork setRetrofit(@NonNull Retrofit retrofit) {
        this.retrofit = retrofit;
        return this;
    }


    public RxNetWork setAdapterFactory(@NonNull CallAdapter.Factory factory) {
        this.mAdapterFactory = factory;
        return this;
    }

    public static <T> T observable(Class<T> service) {
        return getInstance().getRetrofit().create(service);
    }

    public <M> Disposable getApi(@NonNull Observable<M> mObservable, final RxNetWorkListener<M> listener) {
        return getApi(TAG, mObservable, listener);
    }


    public <M> Disposable getApi(@NonNull Object tag, @NonNull Observable<M> observable, final RxNetWorkListener<M> listener) {
        if (listener == null) {
            throw new NullPointerException("listener is null");
        }
        listener.onNetWorkStart();
        Disposable disposable =
                observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<M>() {

                            @Override
                            public void onError(Throwable e) {
                                listener.onNetWorkError(e);
                            }

                            @Override
                            public void onComplete() {
                                listener.onNetWorkComplete();
                            }

                            @Override
                            public void onNext(M m) {
                                listener.onNetWorkSuccess(m);
                            }
                        });
        arrayMap.put(tag, disposable);
        return disposable;
    }

    public void cancel(@NonNull Object tag) {
        Disposable disposable = arrayMap.get(tag);
        if (!RxUtils.isEmpty(disposable) && !disposable.isDisposed()) {
            disposable.dispose();
            arrayMap.remove(tag);
        }
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
        if (RxUtils.isEmpty(retrofit)) {
            retrofit = initRetrofit();
        }
        return retrofit;
    }

    private Retrofit initRetrofit() {
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
        mAdapterFactory = RxJava2CallAdapterFactory.create();
    }
}
