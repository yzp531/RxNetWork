package com.rxnetwork.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.rxnetwork.sample.samplebus.A;
import com.socks.library.KLog;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.jsoup.network.bus.RxBus;
import io.reactivex.jsoup.network.bus.SimpleRxBusCallBack;
import io.reactivex.jsoup.network.manager.RxJsoupNetWork;
import io.reactivex.jsoup.network.manager.RxJsoupNetWorkListener;
import io.reactivex.observers.DisposableObserver;


public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final String BUS_TAG = "bus_tag";
    private static final String NET_TAG = "net_tag";

    private AppCompatTextView textView;
    private AppCompatTextView netTv;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (AppCompatTextView) findViewById(R.id.bus_message);
        netTv = (AppCompatTextView) findViewById(R.id.tv_net);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        RxBus.getInstance().register(BUS_TAG,
                new SimpleRxBusCallBack<String>() {
                    @Override
                    public void onBusNext(String s) {
                        super.onBusNext(s);
                        textView.setText(TextUtils.concat("RxBus Message:", s));
                    }

                    @Override
                    public Class<String> busOfType() {
                        return String.class;
                    }
                });
        findViewById(R.id.btn_send).setOnClickListener(this);
        findViewById(R.id.btn_unregister).setOnClickListener(this);
        findViewById(R.id.btn_test_bus).setOnClickListener(this);
        findViewById(R.id.btn_start_network).setOnClickListener(this);
        findViewById(R.id.btn_cancel_network).setOnClickListener(this);

    }

    private void mergeNetWork() {
        final List<String> list = new ArrayList<>();
        list.add("http://m.7kk.com/album/photos/67795-1");
        list.add("http://m.7kk.com/album/photos/67795-2");

        Observable<List<ImageModel>> objectObservable1 = Observable
                .create(
                        new ObservableOnSubscribe<List<ImageModel>>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<List<ImageModel>> e) throws Exception {
                                Document document = RxJsoupNetWork.getT(list.get(0));
                                List<ImageModel> list = new ArrayList<>();
                                Elements img = document.select("img.lazy-img");
                                ImageModel imageDetailModel;
                                for (Element element : img) {
                                    imageDetailModel = new ImageModel();
                                    String attr = element.attr("data-original");
                                    imageDetailModel.url = attr.replace("219_300", "800_0");
                                    list.add(imageDetailModel);
                                }
                                e.onNext(list);
                                e.onComplete();
                            }
                        });
        Observable<List<ImageModel>> objectObservable2 = Observable
                .create(
                        new ObservableOnSubscribe<List<ImageModel>>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<List<ImageModel>> e) throws Exception {
                                Document document = RxJsoupNetWork.getT(list.get(1));
                                List<ImageModel> list = new ArrayList<>();
                                Elements img = document.select("img.lazy-img");
                                ImageModel imageDetailModel;
                                for (Element element : img) {
                                    imageDetailModel = new ImageModel();
                                    String attr = element.attr("data-original");
                                    imageDetailModel.url = attr.replace("219_300", "800_0");
                                    list.add(imageDetailModel);
                                }
                                e.onNext(list);
                                e.onComplete();
                            }
                        });

        RxJsoupNetWork.getInstance().getApi(
                getClass().getSimpleName(),
                Observable.mergeArray(objectObservable1, objectObservable2)
                , new DisposableObserver<List<ImageModel>>() {
                    @Override
                    public void onNext(@NonNull List<ImageModel> o) {
                        KLog.i(o.size());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                RxBus.getInstance().post(BUS_TAG, "解绑之后除非再次注册,否则会强制抛出异常");
                break;
            case R.id.btn_unregister:
                KLog.i(RxBus.getInstance().unregister(BUS_TAG));
                break;
            case R.id.btn_test_bus:
//                mergeNetWork();
                startActivity(A.class);
                break;
            case R.id.btn_cancel_network:
                RxJsoupNetWork.getInstance().cancel(NET_TAG);
                break;
            case R.id.btn_start_network:
                RxJsoupNetWork.getInstance().getApi(NET_TAG, "http://www.baidu.com"
                        , new RxJsoupNetWorkListener<String>() {
                            @Override
                            public void onNetWorkStart() {
                                netTv.setText("");
                                progressBar.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onNetWorkError(Throwable e) {
                                netTv.setText("");
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onNetWorkComplete() {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onNetWorkSuccess(String data) {
                                netTv.setText(data);
                            }

                            @Override
                            public String getT(Document document) {
                                return document.toString();
                            }

                        });
                break;
        }
        textView.setText("");
    }

    protected void startActivity(Class<?> clz) {
        Intent intent = new Intent(getApplicationContext(), clz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        KLog.i(RxBus.getInstance().unregisterAll());
    }
}
