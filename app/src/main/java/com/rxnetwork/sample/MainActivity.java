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

import com.rxnetwork.sample.samplebus.A;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.network.bus.RxBus;
import io.reactivex.network.bus.SimpleRxBusCallBack;
import io.reactivex.network.manager.RxNetWork;
import io.reactivex.network.manager.RxNetWorkListener;


public class MainActivity extends AppCompatActivity
        implements RxNetWorkListener<List<ListModel>>, View.OnClickListener {

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
                startActivity(A.class);
                break;
            case R.id.btn_start_network:
                RxNetWork
                        .getInstance()
                        .setBaseUrl(Api.ZL_BASE_API)
                        .setLogInterceptor(new SimpleLogInterceptor())
                        .getApi(getClass().getSimpleName(),
                                RxNetWork.observable(Api.ZLService.class)
                                        .getList("daily", 20, 0), this);
                break;
            case R.id.btn_cancel_network:
                RxNetWork.getInstance().cancel(getClass().getSimpleName());
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
    public void onNetWorkSuccess(List<ListModel> data) {
        adapter.addAll(data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        KLog.i(RxBus.getInstance().unregisterAll());
    }
}
