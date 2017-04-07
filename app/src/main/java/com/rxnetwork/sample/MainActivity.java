package com.rxnetwork.sample;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rxnetwork.bus.RxBus;
import com.rxnetwork.bus.RxBusCallBack;
import com.rxnetwork.manager.RxNetWork;
import com.rxnetwork.manager.RxNetWorkListener;
import com.rxnetwork.manager.RxSubscriptionManager;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

public class MainActivity extends AppCompatActivity
        implements RxNetWorkListener<List<ListModel>>, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private MainAdapter adapter;
    private static final String BUS_TAG_ONE = "BUS_TAG_ONE";
    private static final String BUS_TAG_TWO = "BUS_TAG_TWO";
    private Subscription dailySubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new MainAdapter(new ArrayList<ListModel>());
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        });
        final TextView textView = (TextView) findViewById(R.id.bus_message);
        final Subscription one_subscription = RxBus.getInstance().toSubscription(
                BUS_TAG_ONE,
                String.class,
                new RxBusCallBack<String>() {
                    @Override
                    public void onNext(String data) {
                        textView.setText(TextUtils.concat("RxBus Message:", data));
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                });
        final Subscription two_subscription = RxBus.getInstance().toSubscription(
                BUS_TAG_TWO,
                String.class,
                new RxBusCallBack<String>() {
                    @Override
                    public void onNext(String data) {
                        textView.setText(TextUtils.concat("RxBus Message:", data));
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                });

        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getInstance().send(BUS_TAG_ONE);
                textView.setText("");
            }
        });

        findViewById(R.id.btn_send_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                RxBus.getInstance().send(BUS_TAG_TWO);
            }
        });

        findViewById(R.id.btn_unregister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                RxBus.getInstance().unregister(BUS_TAG_TWO, two_subscription);
            }
        });
        findViewById(R.id.btn_unregisterAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                RxBus.getInstance().unregister(BUS_TAG_ONE, one_subscription);
            }
        });
        findViewById(R.id.btn_unregister_network).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxSubscriptionManager.getInstance().unSubscription(dailySubscription);
            }
        });
    }

    @Override
    public void onRefresh() {
        dailySubscription = RxNetWork
                .getInstance()
                .setBaseUrl(Api.ZL_BASE_API)
                .setLogInterceptor(new SimpleLogInterceptor())
                .getApi(RxNetWork.observable(Api.ZLService.class).getList("daily", 20, 0), this);
    }

    @Override
    public void onNetWorkStart() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onNetWorkError(Throwable e) {
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getApplicationContext(), "net work error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNetWorkCompleted() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onNetWorkSuccess(List<ListModel> data) {
        adapter.clear();
        adapter.addAll(data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        RxSubscriptionManager.getInstance().clearSubscription();
        // 由于 Subscription 会在 onError 或 onCompleted 后自动取消订阅，所以这里不是必须的
        //只有突发情况下，由使用者手动取消订阅，例如 退出Activity 时 网络请求还没有 完成，这个时候就应该由使用者手动取消订阅
    }
}
