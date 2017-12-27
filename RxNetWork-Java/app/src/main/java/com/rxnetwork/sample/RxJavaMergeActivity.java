package com.rxnetwork.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.socks.library.KLog;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.network.RxNetWork;
import io.reactivex.network.RxNetWorkListener;
import io.reactivex.schedulers.Schedulers;

/**
 * by y.
 * <p>
 * Description:
 */

public class RxJavaMergeActivity extends AppCompatActivity {


    private Observable<List<ListModel>> daily;
    private Observable<String> daily2;
    private Observable<Object> daily3;
    private Observable<Object> daily4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava_merge);
        daily = RxNetWork.observable(Api.ZLService.class).getList("daily", 20, 0);
        daily2 = RxNetWork.observable(Api.ZLService.class).getString("daily", 20, 0);
        daily3 = RxNetWork.observable(Api.ZLService.class).getObject("daily", 20, 0);
        daily4 = RxNetWork.observable(Api.ZLService.class).getObject("daily", 20, 0);
        findViewById(R.id.btn_network).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        network();
                    }
                });
        findViewById(R.id.btn_network_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                network2();
            }
        });


    }

    private void network() {
        Observable<Object> merge = Observable.mergeArray(daily, daily2);
        RxNetWork.getInstance().getApi(merge, new RxNetWorkListener<Object>() {
            @Override
            public void onNetWorkStart() {
                KLog.i("onNetWorkStart");
            }

            @Override
            public void onNetWorkError(Throwable e) {
                KLog.i("onNetWorkError:" + e.getMessage());
            }

            @Override
            public void onNetWorkComplete() {
                KLog.i("onNetWorkComplete");
            }

            @Override
            public void onNetWorkSuccess(Object data) {
                KLog.i(data);
            }
        });

    }

    private void network2() {

        //如果希望一个接口报错了还能继续走下去,使用 onErrorReturn 返回一个需要的对象即可
        //如果希望一个接口报错了直接停止发射, 使用 onErrorResumeNext 直接return 一个错误,根据 message 去判断是哪个接口出错,或者自定义 Exception

        Observable<Object> objectObservable =
                daily
                        .subscribeOn(Schedulers.io())
                        .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends List<ListModel>>>() {
                            @Override
                            public ObservableSource<? extends List<ListModel>> apply(Throwable throwable) throws Exception {
                                return Observable.error(new RuntimeException("0"));
                            }
                        })
                        .flatMap(new Function<Object, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Object o) throws Exception {
                                return daily.onErrorResumeNext(new Function<Throwable, ObservableSource<? extends List<ListModel>>>() {
                                    @Override
                                    public ObservableSource<? extends List<ListModel>> apply(Throwable throwable) throws Exception {
                                        return Observable.error(new RuntimeException("1"));
                                    }
                                });
                            }
                        })
                        .flatMap(new Function<Object, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Object o) throws Exception {
                                return daily4.onErrorReturn(new Function<Throwable, Object>() {
                                    @Override
                                    public Object apply(Throwable throwable) throws Exception {
                                        return "";
                                    }
                                });
                            }
                        })
                        .flatMap(new Function<Object, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Object o) throws Exception {
                                return daily4.onErrorResumeNext(new Function<Throwable, ObservableSource<?>>() {
                                    @Override
                                    public ObservableSource<?> apply(Throwable throwable) throws Exception {
                                        return Observable.error(new RuntimeException("3"));
                                    }
                                });
                            }
                        })
                        .flatMap(new Function<Object, ObservableSource<?>>() {
                            @Override
                            public ObservableSource<?> apply(Object o) throws Exception {
                                return daily4.onErrorResumeNext(new Function<Throwable, ObservableSource<?>>() {
                                    @Override
                                    public ObservableSource<?> apply(Throwable throwable) throws Exception {
                                        return Observable.error(new RuntimeException("4"));
                                    }
                                });
                            }
                        });

        RxNetWork.getInstance()
                .getApi(objectObservable,
                        new RxNetWorkListener<Object>() {
                            @Override
                            public void onNetWorkStart() {

                            }

                            @Override
                            public void onNetWorkError(Throwable e) {
                                KLog.i("onError:" + e.toString());
                                String message = e.getMessage();
                                switch (message) {
                                    case "0":
                                        break;
                                    case "1":
                                        break;
                                    case "2":
                                        break;
                                    case "3":
                                        break;
                                    case "4":
                                        break;
                                }
                            }

                            @Override
                            public void onNetWorkComplete() {
                                KLog.i("onComplete");
                            }

                            @Override
                            public void onNetWorkSuccess(Object data) {
                                KLog.i(data);
                            }
                        });
    }

}
