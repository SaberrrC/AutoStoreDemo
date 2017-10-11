package com.shanlin.android.autostore.ui.act;

import android.content.Intent;
import android.widget.TextView;

import com.shanlin.android.autostore.common.base.SimpleActivity;
import com.shanlin.android.autostore.common.utils.CommonUtils;
import com.shanlin.autostore.R;
import com.shanlin.android.autostore.common.constants.Constant_LeMaiBao;

import butterknife.BindView;

/**
 * Created by cuieney on 17/08/2017.
 */

public class PayResultActivity extends SimpleActivity {
    @BindView(R.id.tv_pay_type)
    TextView payType;
    @BindView(R.id.tv_pay_money)
    TextView payMoney;
    @BindView(R.id.tv_pay_time)
    TextView payTime;

    @Override
    public int initLayout() {
        return R.layout.activity_pay_result;
    }

    @Override
    public void initData() {
        CommonUtils.initToolbar(this,"支付成功",R.color.blcak, MainActivity.class);
        Intent intent = getIntent();
        String totalMoney = intent.getStringExtra(Constant_LeMaiBao.TOTAL_AMOUNT);
        String type = intent.getStringExtra(Constant_LeMaiBao.PAY_TYPE);
        payType.setText(type+"为您付款成功");
        payMoney.setText("¥"+totalMoney);
        payTime.setText(CommonUtils.getCurrentTime(true));
    }

}
