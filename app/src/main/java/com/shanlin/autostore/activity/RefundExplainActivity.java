package com.shanlin.autostore.activity;

import android.view.View;

import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.utils.CommonUtils;

public class RefundExplainActivity extends BaseActivity {

    @Override
    public int initLayout() {
        return R.layout.activity_refund_explain;
    }

    @Override
    public void initView() {
        CommonUtils.initToolbar(this,"退款说明",R.color.black,null);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {

    }
}
