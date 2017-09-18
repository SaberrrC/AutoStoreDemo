package com.shanlin.autostore.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.constants.Constant_LeMaiBao;
import com.shanlin.autostore.utils.CommonUtils;

/**
 * Created by DELL on 2017/7/19 0019.
 */
public class PayResultActivity extends BaseActivity {

    private TextView payType;
    private TextView payTime;
    private TextView payMoney;

    @Override
    public int initLayout() {
        return R.layout.activity_pay_result;
    }

    @Override
    public void initView() {
        CommonUtils.initToolbar(this,"支付成功",R.color.blcak, MainActivity.class);
        payType = ((TextView) findViewById(R.id.tv_pay_type));
        payMoney = ((TextView) findViewById(R.id.tv_pay_money));
        payTime = ((TextView) findViewById(R.id.tv_pay_time));
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        String totalMoney = intent.getStringExtra(Constant_LeMaiBao.TOTAL_AMOUNT);
        String type = intent.getStringExtra(Constant_LeMaiBao.PAY_TYPE);
        payType.setText(type+"为您付款成功");
        payMoney.setText("¥"+totalMoney);
        payTime.setText(CommonUtils.getCurrentTime(true));
    }

    @Override
    public void onClick(View v) {

    }
}
