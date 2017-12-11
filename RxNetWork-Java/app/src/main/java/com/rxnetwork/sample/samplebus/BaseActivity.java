package com.rxnetwork.sample.samplebus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import io.reactivex.network.bus.RxBus;
import io.reactivex.network.bus.RxBusCallBack;

/**
 * by y on 2017/5/22
 */

public abstract class BaseActivity extends AppCompatActivity implements RxBusCallBack<String> {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.getInstance().register(getClass().getSimpleName(), this);
    }

    @Override
    public void onBusError(Throwable throwable) {
        Toast.makeText(getApplicationContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public Class<String> busOfType() {
        return String.class;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unregister(getClass().getSimpleName());
    }

    protected void startActivity(Class<?> clz) {
        Intent intent = new Intent(getApplicationContext(), clz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
