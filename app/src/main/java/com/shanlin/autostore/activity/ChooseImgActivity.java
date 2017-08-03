package com.shanlin.autostore.activity;

import android.view.View;

import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.utils.CommonUtils;

/**
 * Created by DELL on 2017/8/3 0003.
 */

public class ChooseImgActivity extends BaseActivity {

    @Override
    public int initLayout() {
        return R.layout.activity_choose_head_img;
    }

    @Override
    public void initView() {
        CommonUtils.initToolbar(this,"选择图片",R.color.blcak, MainActivity.class);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }
}
