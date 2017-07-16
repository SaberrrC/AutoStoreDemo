package com.shanlin.autostore.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.shanlin.autostore.utils.StatusBarUtils;
import com.zhy.autolayout.AutoLayoutActivity;

/**
 * Created by DELL on 2017/7/14 0014.
 */

public abstract class BaseActivity extends AutoLayoutActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        StatusBarUtils.setColor(this, Color.TRANSPARENT);
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
