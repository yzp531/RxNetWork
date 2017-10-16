package io.reactivex.network.cache.apply

import com.google.gson.reflect.TypeToken

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.annotations.NonNull
import io.reactivex.network.cache.CustomizeTransformerCall
import io.reactivex.network.cache.LruDisk
import io.reactivex.network.cache.result.CacheResult

/**
 * by y on 20/09/2017.
 */

interface Apply {
    fun <T> applyCacheNetWork(@NonNull key: Any, observable: Observable<T>, lruDisk: LruDisk, typeToken: TypeToken<T>, network: Boolean): Observable<CacheResult<T>>

    fun <T> apply(@NonNull key: Any, observable: Observable<T>, lruDisk: LruDisk, isInsert: Boolean): Observable<CacheResult<T>>

    fun <T> applyCustomize(@NonNull key: Any, upstream: Observable<T>, customizeTransformerCall: CustomizeTransformerCall): ObservableSource<CacheResult<T>>
}
