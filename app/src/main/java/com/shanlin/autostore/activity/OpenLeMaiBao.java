package com.shanlin.autostore.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.bean.paramsBean.PswSettingBody;
import com.shanlin.autostore.bean.paramsBean.RealNameAuthenBody;
import com.shanlin.autostore.bean.resultBean.PswSettingBean;
import com.shanlin.autostore.bean.resultBean.RealNameAuthenBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.constants.Constant_LeMaiBao;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.SpUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 2017/7/17 0017.
 */

public class OpenLeMaiBao extends BaseActivity {

    private Button nextOrConfirm;
    private View line2Step1;
    private View line2Step2;
    private View rlStep1;
    private View rlStep2;
    private ImageView ivStep2;
    private View line1Step2;
    private HttpService service;
    private EditText etName;
    private EditText etIdNum;
    private EditText etPsw;
    private EditText etPswAgain;
    private String token;

    @Override
    public int initLayout() {
        return R.layout.activity_open_lei_mai_bao;
    }

    @Override
    public void initView() {
        CommonUtils.initToolbar(this,"开启乐买宝", R.color.blcak, MainActivity.class);
        nextOrConfirm = ((Button) findViewById(R.id.btn_nextstep_and_confirm));
        findViewById(R.id.tv_xie_yi).setOnClickListener(this);//合同
        findViewById(R.id.service_shou_quan).setOnClickListener(this);//信用授权
        nextOrConfirm.setOnClickListener(this);
        ivStep2 = ((ImageView) findViewById(R.id.iv_step2));
        line2Step1 = findViewById(R.id.line2_step1);
        line1Step2 = findViewById(R.id.line1_step2);
        line2Step2 = findViewById(R.id.line2_step2);
        rlStep1 = findViewById(R.id.rl_step1);
        rlStep2 = findViewById(R.id.rl_step2);

        //姓名和身份证,密码
        etName = ((EditText) findViewById(R.id.et_name));
        etIdNum = ((EditText) findViewById(R.id.et_id_num));
        etPsw = ((EditText) findViewById(R.id.et_psw));
        etPswAgain = ((EditText) findViewById(R.id.et_psw_again));
    }

    @Override
    public void initData() {
        service = CommonUtils.doNet();
        token = SpUtils.getString(this, Constant.TOKEN, "");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_nextstep_and_confirm:
                judgeEmpty();
                break;

            case R.id.tv_xie_yi:
                Intent intent1 = new Intent(this,XieYiAndHeTongActivity.class);
                intent1.putExtra("state",1);
                startActivity(intent1);
                break;

            case R.id.service_shou_quan:
                Intent intent2 = new Intent(this,XieYiAndHeTongActivity.class);
                intent2.putExtra("state",2);
                startActivity(intent2);
                break;
        }
    }

    private void judgeEmpty() {
        if (!SpUtils.getBoolean(OpenLeMaiBao.this,Constant_LeMaiBao.AUTHEN,false)) {
            //第一步
            String name = etName.getText().toString().trim();
            String idNum = etIdNum.getText().toString().trim();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(idNum)) {
                CommonUtils.showToast(this,"请输入完整认证信息!");
                return;
            }

            //调用实名认证接口
            doRealNameAuthen(name, idNum);
        } else if (SpUtils.getBoolean(OpenLeMaiBao.this,Constant_LeMaiBao.AUTHEN,false) && !SpUtils
                .getBoolean(OpenLeMaiBao.this,Constant_LeMaiBao.PASSWORD,false)){
            //第二步
            changeUI(2);
            String psw = etPsw.getText().toString().trim();
            String psw2 = etPswAgain.getText().toString().trim();
            if (TextUtils.isEmpty(psw) || TextUtils.isEmpty(psw2)) {
                CommonUtils.showToast(this,"请输入完整密码!");
                return;
            }

            if (!TextUtils.equals(psw,psw2)) {
                CommonUtils.showToast(this,"密码不一致,请重新输入!");
                etPswAgain.setText("");
                return;
            }
            //调用密码认证接口
            doPswSetting(psw2);
        }
    }

    private void doPswSetting(String psw2) {
        Call<PswSettingBean> call = service.goPswSetting(token,new PswSettingBody(psw2));
        call.enqueue(new Callback<PswSettingBean>() {
            @Override
            public void onResponse(Call<PswSettingBean> call, Response<PswSettingBean> response) {
                PswSettingBean body = response.body();
                if (TextUtils.equals("200",body.getCode())) {
                    CommonUtils.showToast(OpenLeMaiBao.this,body.getMessage());
                    SpUtils.saveBoolean(OpenLeMaiBao.this, Constant_LeMaiBao.PASSWORD,true);
                    Intent data = new Intent();
                    data.putExtra("success",true);
                    OpenLeMaiBao.this.setResult(500,data);
                    finish();
                } else {
                    CommonUtils.showToast(OpenLeMaiBao.this,body.getMessage());
                }
            }

            @Override
            public void onFailure(Call<PswSettingBean> call, Throwable t) {
                    CommonUtils.debugLog(t.getMessage());
            }
        });
    }

    private void doRealNameAuthen(String name, String idNum) {
        Call<RealNameAuthenBean> call = service.goRealNameAuthen(token,new
                RealNameAuthenBody
                (idNum,
                name));
        call.enqueue(new Callback<RealNameAuthenBean>() {
            @Override
            public void onResponse(Call<RealNameAuthenBean> call, Response<RealNameAuthenBean> response) {
                RealNameAuthenBean body = response.body();
                if (TextUtils.equals("200",body.getCode())) {
                    CommonUtils.showToast(OpenLeMaiBao.this,body.getMessage());
                    changeUI(2);
                    SpUtils.saveBoolean(OpenLeMaiBao.this, Constant_LeMaiBao.AUTHEN,true);
                } else {
                    CommonUtils.showToast(OpenLeMaiBao.this,body.getMessage());
                }
            }

            @Override
            public void onFailure(Call<RealNameAuthenBean> call, Throwable t) {
                CommonUtils.debugLog(t.getMessage());
            }
        });
    }

    private void changeUI(int step) {
        nextOrConfirm.setText(step == 1 ? "下一步" : "确定");
        rlStep1.setVisibility(step == 1 ? View.VISIBLE : View.GONE);
        rlStep2.setVisibility(step == 1 ? View.GONE : View.VISIBLE);
        ivStep2.setImageResource(step == 1 ? R.mipmap.step2 : R.mipmap.icon_step2);
        line2Step1.setBackgroundColor(step == 1 ? Color.parseColor("#d6d6d6") : Color.parseColor("#FCC70D"));
        line1Step2.setBackgroundColor(step == 1 ? Color.parseColor("#d6d6d6") : Color.parseColor
                ("#FCC70D"));
        line2Step2.setBackgroundColor(step == 2 ? Color.parseColor("#FCC70D") : Color.parseColor
                ("#d6d6d6"));
    }
}
