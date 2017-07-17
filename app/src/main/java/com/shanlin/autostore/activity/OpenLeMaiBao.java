package com.shanlin.autostore.activity;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
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

    @Override
    public int initLayout() {
        return R.layout.activity_open_lei_mai_bao;
    }

    @Override
    public void initView() {
        CommonUtils.initToolbar(this,"开启乐买宝", R.color.blcak, MainActivity.class);
        nextOrConfirm = ((Button) findViewById(R.id.btn_nextstep_and_confirm));
        line2Step1 = findViewById(R.id.line2_step1);
        line2Step2 = findViewById(R.id.line2_step2);
        rlStep1 = findViewById(R.id.rl_step1);
        rlStep2 = findViewById(R.id.rl_step2);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_nextstep_and_confirm:
                if (nextOrConfirm.getText().equals("下一步")) {
                    changeUI(2);
                } else {
                    CommonUtils.toNextActivity(this,ChoosePayWayActivity.class);
                }
                break;
        }
    }

    private void changeUI(int step) {
        nextOrConfirm.setText(step == 1 ? "下一步" : "确定");
        rlStep1.setVisibility(step == 1 ? View.VISIBLE : View.GONE);
        rlStep2.setVisibility(step == 1 ? View.GONE : View.VISIBLE);
        line2Step1.setBackgroundColor(step == 1 ? Color.parseColor("#d6d6d6") : Color.parseColor("#FCC70D"));
        line2Step2.setBackgroundColor(step == 1 ? Color.parseColor("#d6d6d6") : Color.parseColor
                ("#FCC70D"));
    }


}
