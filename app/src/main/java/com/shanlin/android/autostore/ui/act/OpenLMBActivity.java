package com.shanlin.android.autostore.ui.act;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanlin.android.autostore.common.base.BaseActivity;
import com.shanlin.android.autostore.common.constants.Constant;
import com.shanlin.android.autostore.common.utils.CommonUtils;
import com.shanlin.android.autostore.common.utils.SpUtils;
import com.shanlin.android.autostore.entity.respone.PswSettingBean;
import com.shanlin.android.autostore.entity.respone.RealNameAuthenBean;
import com.shanlin.android.autostore.entity.respone.UserVertifyStatusBean;
import com.shanlin.android.autostore.presenter.Contract.OpenLMBActContract;
import com.shanlin.android.autostore.presenter.OpenLMBPresenter;
import com.shanlin.autostore.R;
import com.shanlin.autostore.constants.Constant_LeMaiBao;
import com.shanlin.autostore.utils.ProgressDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell、 on 2017/8/17.
 */

public class OpenLMBActivity extends BaseActivity<OpenLMBPresenter> implements OpenLMBActContract.View{

    @BindView(R.id.btn_nextstep_and_confirm)
    TextView nextOrConfirm;
    @BindView(R.id.line2_step1)
    View line2Step1;
    @BindView(R.id.line2_step2)
    View line2Step2;
    @BindView(R.id.rl_step1)
    View rlStep1;
    @BindView(R.id.rl_step2)
    View rlStep2;
    @BindView(R.id.iv_step2)
    ImageView ivStep2;
    @BindView(R.id.line1_step2)
    View line1Step2;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_id_num)
    EditText etIdNum;
    @BindView(R.id.et_psw)
    EditText etPsw;
    @BindView(R.id.et_psw_again)
    EditText etPswAgain;

    private boolean isClick;
    private String token;
    private String status;
    private ProgressDialog progressDialog;
    
    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_open_lei_mai_bao;
    }

    @Override
    public void initData() {
        CommonUtils.initToolbar(this, "开启乐买宝", R.color.blcak, MainActivity.class);
        token = SpUtils.getString(this, Constant.TOKEN, "");
        progressDialog = new ProgressDialog(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        status = getIntent().getStringExtra(Constant_LeMaiBao.AUTHEN_STATUS);
        Log.d("wr", "onRestart:------- "+status);
        judgeEmpty();
    }

    @OnClick({R.id.service_shou_quan_bottom,R.id.tv_xie_yi,R.id.service_shou_quan,R.id.btn_nextstep_and_confirm})
    public void onClick (View v) {

        switch (v.getId()) {

            case R.id.btn_nextstep_and_confirm:
//                mPresenter.getUserAuthenStatus();
                isClick = true;
                judgeEmpty();
                break;
            case R.id.tv_xie_yi:
            case R.id.service_shou_quan_bottom:
                Intent intent1 = new Intent(this, XieYiAndHeTongActivity.class);
                intent1.putExtra("state", 1);
                startActivity(intent1);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.service_shou_quan:
                Intent intent2 = new Intent(this, XieYiAndHeTongActivity.class);
                intent2.putExtra("state", 2);
                startActivity(intent2);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
        }
    }

    @Override
    public void onRealAuthenSuccess(String code, RealNameAuthenBean data, String msg) {
            changeUI(2);
            CommonUtils.showToast(OpenLMBActivity.this, msg);
            if (progressDialog.isShowing()) progressDialog.dismiss();
    }

    @Override
    public void onRealNameAuthenFailed(Throwable ex, String code, String msg) {
            CommonUtils.debugLog(msg);
            if (progressDialog.isShowing()) progressDialog.dismiss();
    }

    @Override
    public void onPswSettingSuccess(String code, PswSettingBean data, String msg) {
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            CommonUtils.showToast(OpenLMBActivity.this, msg);
            if (progressDialog.isShowing()) progressDialog.dismiss();
    }

    @Override
    public void onPswSettingFailed(Throwable ex, String code, String msg) {
            CommonUtils.debugLog(msg);
            if (progressDialog.isShowing()) progressDialog.dismiss();
    }

    private void judgeEmpty() {
        if (Constant_LeMaiBao.AUTHEN_NOT.equals(status)) {
            //第一步
            String name = etName.getText().toString().trim();
            String idNum = etIdNum.getText().toString().trim();
            if (!isClick) return;
            if (TextUtils.isEmpty(name)) {
                CommonUtils.showToast(this,"姓名不能为空");
                return;
            }
            if (TextUtils.isEmpty(idNum)) {
                CommonUtils.showToast(this,"身份证号不能为空");
                return;
            }

            if (idNum.length() < 18) {
                CommonUtils.showToast(this,"身份证号码格式错误");
                return;
            }
            if (!progressDialog.isShowing()) progressDialog.show();
            //调用实名认证接口
            mPresenter.doRealNameAuthen(name,idNum);
        } else if (Constant_LeMaiBao.AUTHEN_REAL_NAME.equals(status)) {
            //第二步
            changeUI(2);
            String psw = etPsw.getText().toString().trim();
            String psw2 = etPswAgain.getText().toString().trim();
            if (!isClick) return;
            if (TextUtils.isEmpty(psw)) {
                CommonUtils.showToast(this,"密码不能为空");
                return;
            }
            if (psw.length() < 6) {
                CommonUtils.showToast(this,"请输入六位数密码");
                return;
            }

            if (!TextUtils.equals(psw,psw2)) {
                CommonUtils.showToast(this,"密码不一致,请重新输入");
                etPswAgain.setText("");
                return;
            }
            if (!progressDialog.isShowing()) progressDialog.show();
            //调用密码认证接口
            mPresenter.doPasswordSetting(psw2);
        }
    }

    private void changeUI(int step) {
        nextOrConfirm.setText(step == 1 ? "下一步" : "确定");
        rlStep1.setVisibility(step == 1 ? View.VISIBLE : View.GONE);
        rlStep2.setVisibility(step == 1 ? View.GONE : View.VISIBLE);
        ivStep2.setImageResource(step == 1 ? R.mipmap.step2 : R.mipmap.icon_step2);
        line2Step1.setBackgroundColor(step == 1 ? Color.parseColor("#d6d6d6") : Color.parseColor("#FCC70D"));
        line1Step2.setBackgroundColor(step == 1 ? Color.parseColor("#d6d6d6") : Color.parseColor("#FCC70D"));
        line2Step2.setBackgroundColor(step == 2 ? Color.parseColor("#FCC70D") : Color.parseColor("#d6d6d6"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        isClick = false;
    }
}
