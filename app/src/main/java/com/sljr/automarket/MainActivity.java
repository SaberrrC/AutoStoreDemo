package com.sljr.automarket;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sljr.automarket.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private Button btn;
    private TextView tv;
    private static final String TAG = "wr";

    @Override
    public int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        btn = ((Button) findViewById(R.id.btn));
        btn.setOnClickListener(this);
        tv = ((TextView) findViewById(R.id.tv));
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }
}
