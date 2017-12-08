package io.reactivex.network


import android.support.v4.util.ArrayMap
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.network.util.RxUtils
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * by y on 2017/2/22.
 */
class RxNetWork private constructor() {
    private val arrayMap: ArrayMap<Any, Disposable> = ArrayMap()
    private var timeout_time = 15
    private var isRetryOnConnectionFailure = true
    private var mGson: Gson? = null
    private var baseUrl: String? = null
    private var mOkHttpClient: OkHttpClient? = null
    private var retrofit: Retrofit? = null
    private var mConverterFactory: Converter.Factory? = null
    private var mAdapterFactory: CallAdapter.Factory? = null

    private var mLogInterceptor: Interceptor? = null
    private var mHeaderInterceptor: Interceptor? = null

    private object RxNetWorkHolder {
        val rxNetWork = RxNetWork()
    }

    fun setRetryOnConnectionFailure(retryOnConnectionFailure: Boolean): RxNetWork {
        isRetryOnConnectionFailure = retryOnConnectionFailure
        return this
    }

    fun setBaseUrl(url: String): RxNetWork {
        this.baseUrl = url
        return this
    }

    fun setGson(gson: Gson): RxNetWork {
        this.mGson = gson
        return this
    }

    fun setOkHttpClient(okHttpClient: OkHttpClient): RxNetWork {
        this.mOkHttpClient = okHttpClient
        return this
    }

    fun setLogInterceptor(mLogInterceptor: Interceptor): RxNetWork {
        this.mLogInterceptor = mLogInterceptor
        return this
    }

    fun setHeaderInterceptor(mHeaderInterceptor: Interceptor): RxNetWork {
        this.mHeaderInterceptor = mHeaderInterceptor
        return this
    }

    fun setTimeoutTime(timeout_time: Int): RxNetWork {
        this.timeout_time = timeout_time
        return this
    }

    fun setConverterFactory(factory: Converter.Factory): RxNetWork {
        this.mConverterFactory = factory
        return this
    }

    fun setRetrofit(retrofit: Retrofit): RxNetWork {
        this.retrofit = retrofit
        return this
    }


    fun setAdapterFactory(factory: CallAdapter.Factory): RxNetWork {
        this.mAdapterFactory = factory
        return this
    }

    fun <M> getApi(mObservable: Observable<M>, listener: RxNetWorkListener<M>): Disposable =
            getApi(TAG, mObservable, listener)


    fun <M> getApi(tag: Any, observable: Observable<M>, listener: RxNetWorkListener<M>?): Disposable {
        if (listener == null) {
            throw NullPointerException("listener is null")
        }
        listener.onNetWorkStart()
        val disposable = observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<M>() {

                    override fun onError(e: Throwable) {
                        listener.onNetWorkError(e)
                    }

                    override fun onComplete() {
                        listener.onNetWorkComplete()
                    }

                    override fun onNext(m: M) {
                        listener.onNetWorkSuccess(m)
                    }
                })
        arrayMap.put(tag, disposable)
        return disposable
    }

    fun cancel(tag: Any) {
        val disposable = arrayMap[tag]
        if (!RxUtils.isEmpty(disposable) && !disposable!!.isDisposed) {
            disposable.dispose()
            arrayMap.remove(tag)
        }
    }

    fun cancelAll() {
        for ((key) in arrayMap) {
            cancel(key)
        }
    }

    private fun getRetrofit(): Retrofit? {
        if (RxUtils.isEmpty(mOkHttpClient)) {
            mOkHttpClient = initOkHttp()
        }
        if (RxUtils.isEmpty(mConverterFactory)) {
            rxNetWorkConverterFactory()
        }
        if (RxUtils.isEmpty(mAdapterFactory)) {
            rxNetWorkAdapterFactory()
        }
        if (RxUtils.isEmpty(retrofit)) {
            retrofit = initRetrofit()
        }
        return retrofit
    }

    private fun initRetrofit(): Retrofit {
        return Retrofit.Builder()
                .client(mOkHttpClient!!)
                .baseUrl(baseUrl!!)
                .addConverterFactory(mConverterFactory!!)
                .addCallAdapterFactory(mAdapterFactory!!)
                .build()
    }

    private fun initOkHttp(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (!RxUtils.isEmpty(mLogInterceptor))
            builder.addInterceptor(mLogInterceptor!!)
        if (!RxUtils.isEmpty(mHeaderInterceptor))
            builder.addInterceptor(mHeaderInterceptor!!)
        builder.connectTimeout(timeout_time.toLong(), TimeUnit.SECONDS)
                .writeTimeout(timeout_time.toLong(), TimeUnit.SECONDS)
                .readTimeout(timeout_time.toLong(), TimeUnit.SECONDS)
        builder.retryOnConnectionFailure(isRetryOnConnectionFailure)
        return builder.build()
    }

    private fun rxNetWorkConverterFactory() {
        if (RxUtils.isEmpty(mGson)) {
            mGson = Gson()
        }
        mConverterFactory = GsonConverterFactory.create(mGson!!)
    }

    private fun rxNetWorkAdapterFactory() {
        mAdapterFactory = RxJava2CallAdapterFactory.create()
    }

    companion object {

        val TAG = "RxNetWork"

        val instance: RxNetWork
            get() = RxNetWorkHolder.rxNetWork

        fun <T> observable(service: Class<T>): T = instance.getRetrofit()!!.create(service)
    }
}
