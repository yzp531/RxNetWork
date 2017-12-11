package com.rxnetwork.sample.samplebus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.rxnetwork.sample.R;

/**
 * by y on 2017/5/22
 */

public class B extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        findViewById(R.id.btn_simple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(C.class);
            }
        });
    }

    @Override
    public void onBusNext(String s) {
        Toast.makeText(getApplicationContext(), getClass().getSimpleName() + " : : : " + s, Toast.LENGTH_SHORT).show();
    }
}
