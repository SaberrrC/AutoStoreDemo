package com.shanlin.autostore.activity;

import android.content.Intent;
import android.view.View;

import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.utils.CommonUtils;

/**
 * Created by DELL on 2017/7/19 0019.
 */
public class XieYiAndHeTongActivity extends BaseActivity{

    @Override
    public int initLayout() {
        return R.layout.activity_xieyi_and_he_tong;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            CommonUtils.initToolbar(this,"乐买宝用户服务合同",R.color.blcak,OpenLeMaiBao.class);
        } else if (requestCode == 2){
            CommonUtils.initToolbar(this,"善林信用服务协议",R.color.blcak,OpenLeMaiBao.class);
        }
    }
}
