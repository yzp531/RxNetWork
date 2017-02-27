package com.rxnetwork.sample;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * by y on 2016/8/7.
 */
public class Api {

    static final String ZL_BASE_API = "https://zhuanlan.zhihu.com/api/";

    interface ZLService {
        @GET("columns/" + "{suffix}/posts")
        Observable<List<ListModel>> getList(@Path("suffix") String suffix, @Query("limit") int limit, @Query("offset") int offset);
    }

}
