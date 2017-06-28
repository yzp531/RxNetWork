package com.rxnetwork.sample;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * by y on 28/06/2017.
 */

public class SimpleHeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader("key", "value")
                .build();
        return chain.proceed(request);
    }
}
