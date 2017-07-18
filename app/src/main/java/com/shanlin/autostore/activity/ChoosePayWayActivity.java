package com.shanlin.autostore.activity;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jungly.gridpasswordview.GridPasswordView;
import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.zhifubao.PayActivity;

/**
 * Created by DELL on 2017/7/17 0017.
 */
public class ChoosePayWayActivity extends BaseActivity{

    private View dialogView;
    private AlertDialog dialog;
    private TextView moneyNeedToPay;
    private GridPasswordView pswView;

    @Override
    public int initLayout() {
        return R.layout.activity_choose_pay_way;
    }

    @Override
    public void initView() {
        CommonUtils.initToolbar(this,"选择支付方式",R.color.blcak, MainActivity.class);
        findViewById(R.id.ll_pay_way_1).setOnClickListener(this);
        findViewById(R.id.ll_pay_way_2).setOnClickListener(this);
        findViewById(R.id.ll_pay_way_3).setOnClickListener(this);
        dialogView = LayoutInflater.from(this).inflate(R.layout.input_psw_dialog_layout, null);
        dialogView.findViewById(R.id.iv_close_dialog).setOnClickListener(this);
        moneyNeedToPay = ((TextView) dialogView.findViewById(R.id.money_need_to_pay));
        pswView = ((GridPasswordView) dialogView.findViewById(R.id.pswView));
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ll_pay_way_1:
                //买乐宝支付
                showInputPswDialog();
                break;
            case R.id.ll_pay_way_2:
                //支付宝支付
                CommonUtils.toNextActivity(this, PayActivity.class);
                break;
            case R.id.ll_pay_way_3:
                //微信支付
                break;

            case R.id.iv_close_dialog:
                dialog.dismiss();
                break;
        }
    }

    private void showInputPswDialog() {
        dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();
        dialog.show();
    }
}
