package io.reactivex.network;

/**
 * by y on 11/12/2017.
 */

public class RxNetWorkTask<T> {

    private T data;
    private Object tag;

    public RxNetWorkTask(T data, Object tag) {
        this.data = data;
        this.tag = tag;
    }

    public RxNetWorkTask() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }
}
