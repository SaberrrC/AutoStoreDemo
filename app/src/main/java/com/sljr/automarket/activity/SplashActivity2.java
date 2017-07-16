package com.sljr.automarket.activity;

import android.view.View;
import android.widget.Button;

import com.sljr.automarket.R;
import com.sljr.automarket.base.BaseActivity;
import com.sljr.automarket.utils.CommonUtils;

/**
 * Created by DELL on 2017/7/14 0014.
 */

public class SplashActivity2 extends BaseActivity {

    @Override
    public int initLayout() {
        return R.layout.activity_splash2;
    }

    @Override
    public void initView() {
        ((Button) findViewById(R.id.login_type_wx)).setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.login_type_wx:

                break;

            case R.id.login_type_phoneNum:
                CommonUtils.toNextActivity(this,LoginActivity.class);
                break;

            case R.id.login_type_face:

                break;
        }
    }
}
