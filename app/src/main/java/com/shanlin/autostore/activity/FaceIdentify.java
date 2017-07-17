package com.shanlin.autostore.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.utils.CommonUtils;

/**
 * Created by DELL on 2017/7/16 0016.
 */
public class FaceIdentify extends BaseActivity {

    @Override
    public int initLayout() {
        return R.layout.activity_face_identify;
    }

    @Override
    public void initView() {
        initToolBar();


    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.face_identi_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    @Override
    public void initData() {
        //暂时直接跳转主页面
        CommonUtils.toNextActivity(this, MainActivity.class);
    }

    @Override
    public void onClick(View v) {

    }


}
