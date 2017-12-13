package io.reactivex.network;

/**
 * by y on 2017/2/22.
 */

public interface RxNetWorkTaskListener<T> extends RxNetWorkListener<T> {
    Object getTag();
}
