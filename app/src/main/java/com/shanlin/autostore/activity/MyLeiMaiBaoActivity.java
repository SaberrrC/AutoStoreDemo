package com.shanlin.autostore.activity;

import android.view.View;

import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.utils.CommonUtils;

/**
 * Created by DELL on 2017/7/17 0017.
 */

public class MyLeiMaiBaoActivity extends BaseActivity {

    @Override
    public int initLayout() {

        return R.layout.activity_my_lei_mai_bao;
    }

    @Override
    public void initView() {
        CommonUtils.initToolbar(this,"我的乐买宝", R.color.blcak,MainActivity.class);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }
}
