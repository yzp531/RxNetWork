package com.rxnetwork.sample;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.rxnetwork.manager.RxNetWork;
import com.rxnetwork.manager.RxNetWorkListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements RxNetWorkListener<List<ListModel>>, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private MainAdapter adapter;

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

        RxNetWork.getInstance().getApi(

                RxNetWork.observable(Api.ZLService.class).getList("", 20, 0),

                new RxNetWorkListener<List<ListModel>>() {
                    @Override
                    public void onNetWorkStart() {

                    }

                    @Override
                    public void onNetWorkError(Throwable e) {

                    }

                    @Override
                    public void onNetWorkCompleted() {

                    }

                    @Override
                    public void onNetWorkSuccess(List<ListModel> data) {

                    }
                }

        );
    }

    @Override
    public void onRefresh() {
        RxNetWork
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
        RxNetWork.getInstance().clearSubscription();
    }
}
