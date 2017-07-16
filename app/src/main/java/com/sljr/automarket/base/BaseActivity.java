package com.sljr.automarket.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by DELL on 2017/7/14 0014.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        initView();
        initData();
    }

    /**
     * 初始化布局
     * @return
     */
    public abstract int initLayout();

    /**
     * 初始化控件
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();


}
