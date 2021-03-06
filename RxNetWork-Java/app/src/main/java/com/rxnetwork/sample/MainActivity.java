package com.rxnetwork.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.rxnetwork.sample.samplebus.A;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.network.RxNetWork;
import io.reactivex.network.RxNetWorkTask;
import io.reactivex.network.RxNetWorkTaskListener;
import io.reactivex.network.bus.RxBus;
import io.reactivex.network.bus.SimpleRxBusCallBack;
import io.reactivex.network.cache.RxCache;
import io.reactivex.network.cache.result.CacheResult;


public class MainActivity extends AppCompatActivity
        implements RxNetWorkTaskListener<RxNetWorkTask<List<ListModel>>>, View.OnClickListener {

    private static final String BUS_TAG = "bus_tag";

    private MainAdapter adapter;
    private AppCompatTextView textView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        textView = (AppCompatTextView) findViewById(R.id.bus_message);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        adapter = new MainAdapter(new ArrayList<ListModel>());
        recyclerView.setAdapter(adapter);


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
        findViewById(R.id.btn_test_rxjava).setOnClickListener(this);


//        Observable<CacheResult<Object>> daily = RxNetWork
//                .observable(Api.ZLService.class)
//                .getList("daily", 20, 0)
//                .compose(RxCache.getInstance().customizeTransformer("", new CustomizeTransformerCall() {
//                    @Override
//                    public <T> ObservableSource<CacheResult<T>> applyCustomize(@NonNull Object key, Observable<T> upstream) {
//                        return Observable.just(null);
//                    }
//                }));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                RxBus.getInstance().post(BUS_TAG, "传递对象");
                break;
            case R.id.btn_unregister:
                KLog.i(RxBus.getInstance().unregister(BUS_TAG));
                break;
            case R.id.btn_test_bus:
                startActivity(A.class);
                break;
            case R.id.btn_start_network:
                Observable<List<ListModel>> daily = RxNetWork
                        .observable(Api.ZLService.class)
                        .getList("daily", 20, 0)
                        .compose(RxCache.getInstance().transformerCN("1", true, new TypeToken<List<ListModel>>() {
                        }))
                        .map(new Function<CacheResult<List<ListModel>>, List<ListModel>>() {
                            @Override
                            public List<ListModel> apply(CacheResult<List<ListModel>> listCacheResult) throws Exception {
                                return listCacheResult.getResult();
                            }
                        });
                RxNetWork
                        .getInstance()
                        .setLogInterceptor(new SimpleLogInterceptor())
                        .getApiTask(getClass().getSimpleName(), daily, this);
                break;
            case R.id.btn_cancel_network:
                RxNetWork.getInstance().cancel(getClass().getSimpleName());
                break;
            case R.id.btn_test_rxjava:
                startActivity(RxJavaMergeActivity.class);
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
    public void onNetWorkStart() {
        adapter.clear();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNetWorkError(Throwable e) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), "net work error:" + e.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNetWorkComplete() {
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onNetWorkSuccess(RxNetWorkTask<List<ListModel>> data) {
        Toast.makeText(getApplicationContext(), String.valueOf(data.getTag()), Toast.LENGTH_SHORT).show();
        adapter.addAll(data.getData());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        KLog.i(RxBus.getInstance().unregisterAll());
    }

    @Override
    public Object getTag() {
        return new Random().nextInt();
    }
}
