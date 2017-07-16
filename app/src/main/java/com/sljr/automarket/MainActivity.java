package com.sljr.automarket;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sljr.automarket.base.BaseActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public void test(View view) {
        RetrofitUtils<ObjDataBean> netUtil = new RetrofitUtils();

        Call<ObjDataBean> call = netUtil.ObjGet("mock");
        call.enqueue(new Callback<ObjDataBean>() {
            @Override
            public void onResponse(Call<ObjDataBean> call, Response<ObjDataBean> response) {
                ObjDataBean body = response.body();
                Log.d(TAG, "onResponse: "+body);
            }

            @Override
            public void onFailure(Call<ObjDataBean> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
