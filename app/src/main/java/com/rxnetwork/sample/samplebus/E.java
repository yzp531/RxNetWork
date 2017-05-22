package com.rxnetwork.sample.samplebus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.rxnetwork.sample.R;

import io.reactivex.jsoup.network.bus.RxBus;

/**
 * by y on 2017/5/22
 */

public class E extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e);

        findViewById(R.id.A).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getInstance().post("A", "A activity接收到消息了");
            }
        });
        findViewById(R.id.B).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getInstance().post("B", "B activity接收到消息了");
            }
        });
        findViewById(R.id.C).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getInstance().post("C", "C activity接收到消息了");
            }
        });
        findViewById(R.id.D).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getInstance().post("D", "D activity接收到消息了");
            }
        });

    }

    @Override
    public void onBusNext(String s) {

    }
}
