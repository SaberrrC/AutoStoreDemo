package com.shanlin.autostore.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.utils.CommonUtils;

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
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_nextstep_and_confirm:
                //实名认证姓名和身份证非空判断
                judgeEmpty(true);

                if (nextOrConfirm.getText().equals("下一步")) {
                    changeUI(2);
                } else {
                    CommonUtils.toNextActivity(this,ChoosePayWayActivity.class);
                }
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

    private void judgeEmpty(boolean step) {
        if (step) {
            //第一步
            String name = etName.getText().toString().trim();
            String idNum = etIdNum.getText().toString().trim();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(idNum)) {
                Toast.makeText(this, "请输入完整认证信息!", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {

        }
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
