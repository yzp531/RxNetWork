package com.rxnetwork.sample;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * by y on 2016/8/7.
 */
class Api {

    static final String ZL_BASE_API = "https://zhuanlan.zhihu.com/api/";

    interface ZLService {
        @Headers(SimpleCache.CACHE_CONTROL_AGE + SimpleCache.CACHE_STALE_SHORT)
        @GET("columns/" + "{suffix}/posts")
        Observable<List<ListModel>> getList(@Path("suffix") String suffix, @Query("limit") int limit, @Query("offset") int offset);
    }

}
