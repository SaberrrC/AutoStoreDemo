package com.shanlin.autostore.activity;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
    private TextView availableBalence;
    private View availableDialogView;
    private AlertDialog availbleDialog;
    private ImageView iconChoose;

    @Override
    public int initLayout() {
        return R.layout.activity_choose_pay_way;
    }

    @Override
    public void initView() {
        CommonUtils.initToolbar(this,"选择支付方式",R.color.blcak, MainActivity.class);
        availableDialogView = LayoutInflater.from(this).inflate(R.layout.get_available_balence_layout, null);
        availableDialogView.findViewById(R.id.btn_diaolog_know).setOnClickListener(this);
        findViewById(R.id.ll_pay_way_1).setOnClickListener(this);
        findViewById(R.id.ll_pay_way_2).setOnClickListener(this);
        findViewById(R.id.ll_pay_way_3).setOnClickListener(this);
        iconChoose = ((ImageView) findViewById(R.id.iv_icon_choose));//圆形logo根据状态切换
        availableBalence = (TextView)findViewById(R.id.get_avaiable_balence);//可用额度
        availableBalence.setOnClickListener(this);
        dialogView = LayoutInflater.from(this).inflate(R.layout.input_psw_dialog_layout, null);
        dialogView.findViewById(R.id.iv_close_dialog).setOnClickListener(this);
        moneyNeedToPay = ((TextView) dialogView.findViewById(R.id.money_need_to_pay));
        pswView = ((GridPasswordView) dialogView.findViewById(R.id.pswView));
        pswView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {

            }

            @Override
            public void onInputFinish(String psw) {
                //// TODO: 2017/7/19 0019 联网发送数据
                dialog.dismiss();
                CommonUtils.toNextActivity(ChoosePayWayActivity.this,PayResultActivity.class);
            }
        });
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
            case R.id.get_avaiable_balence:
                //可用额度领取
                showGetAvailableBalenceDialog();
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
            case R.id.btn_diaolog_know:
                availbleDialog.dismiss();
                break;
        }
    }

    private void showGetAvailableBalenceDialog() {
        if (availbleDialog == null) {
            availbleDialog = CommonUtils.getDialog(this, availableDialogView,false);
        } else {
            availbleDialog.show();
        }
    }

    private void showInputPswDialog() {
        if (dialog == null) {
            dialog = CommonUtils.getDialog(this,dialogView,false);
        } else {
            dialog.show();
        }
    }
}
