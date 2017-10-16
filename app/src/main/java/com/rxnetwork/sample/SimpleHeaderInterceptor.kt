package com.rxnetwork.sample

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * by y on 28/06/2017.
 */

class SimpleHeaderInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
                .newBuilder()
                .addHeader("key", "value")
                .build()
        return chain.proceed(request)
    }
}
