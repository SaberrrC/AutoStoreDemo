package com.sljr.automarket.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sljr.automarket.R;
import com.sljr.automarket.base.BaseActivity;

/**
 * Created by DELL on 2017/7/14 0014.
 */
public class PhoneNumLoginActivity extends BaseActivity {

    private EditText mEtPhoneNum;
    private EditText mEtCertifcode;

    @Override
    public int initLayout() {
        return R.layout.activity_phone_num_login;
    }

    @Override
    public void initView() {
        mEtPhoneNum = ((EditText) findViewById(R.id.login_et_phoneNum));
        mEtCertifcode = ((EditText) findViewById(R.id.login_et_certifcode));
        ((Button) findViewById(R.id.btn_login)).setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_login:
                String num = mEtPhoneNum.getText().toString().trim();
                String psw = mEtPhoneNum.getText().toString().trim();


                break;
        }
    }
}
