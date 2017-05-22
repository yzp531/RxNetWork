package io.reactivex.jsoup.network.manager;

import org.jsoup.nodes.Document;

/**
 * by y on 2017/2/22.
 */

public interface RxJsoupNetWorkListener<T> {

    void onNetWorkStart();

    void onNetWorkError(Throwable e);

    void onNetWorkComplete();

    void onNetWorkSuccess(T t);

    T getT(Document document);
}
