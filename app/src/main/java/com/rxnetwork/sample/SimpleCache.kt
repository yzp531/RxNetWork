package com.rxnetwork.sample

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * by y on 28/06/2017.
 */

object SimpleCache {


    const val CACHE_STALE_SHORT = 60
    const val CACHE_STALE_LONG = 60 * 60 * 24 * 7
    const val CACHE_CONTROL_AGE = "Cache-Control: public, max-age="

    private val isNetworkConnected: Boolean
        get() {
            val mConnectivityManager = App.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable
            }
            return false
        }


    class SimpleCacheInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            if (!isNetworkConnected) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build()
            }
            val originalResponse = chain.proceed(request)
            return if (isNetworkConnected) {
                val cacheControl = request.cacheControl().toString()
                originalResponse.newBuilder().header("Cache-Control", cacheControl)
                        .removeHeader("Pragma").build()
            } else {
                originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_LONG)
                        .removeHeader("Pragma").build()
            }
        }
    }
}
