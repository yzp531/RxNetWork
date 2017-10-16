package com.rxnetwork.sample

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * by y on 2016/8/7.
 */
internal object Api {

    val ZL_BASE_API = "https://zhuanlan.zhihu.com/api/"

    internal interface ZLService {
        @Headers(SimpleCache.CACHE_CONTROL_AGE + SimpleCache.CACHE_STALE_SHORT)
        @GET("columns/" + "{suffix}/posts")
        fun getList(@Path("suffix") suffix: String, @Query("limit") limit: Int, @Query("offset") offset: Int): Observable<List<ListModel>>
    }

}
