package com.shanlin.autostore.activity;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.view.CountDownTextView;

/**
 * Created by DELL on 2017/7/14 0014.
 */
public class PhoneNumLoginActivity extends BaseActivity {

    private EditText mEtMsgCode;
    private EditText mEtPhoneNum;
    private Button mBtnBindOrLogin;
    private CountDownTextView mBtnGetMsgCode;
    private View iconAndTitle;
    private View noVipTip;

    @Override
    public int initLayout() {
        return R.layout.activity_phone_num_login;
    }

    @Override
    public void initView() {
        iconAndTitle = findViewById(R.id.icon_title);
        noVipTip = findViewById(R.id.no_vip_tip);
        mEtPhoneNum = ((EditText) findViewById(R.id.et_phone_num));
        mEtMsgCode = ((EditText) findViewById(R.id.et_msg_code));
        mBtnBindOrLogin = ((Button) findViewById(R.id.btn_bind_or_login));
        mBtnGetMsgCode = ((CountDownTextView) findViewById(R.id.btn_get_msgcode));
        mBtnGetMsgCode.setOnClickListener(this);
        mBtnGetMsgCode.setClickable(true);
        mBtnBindOrLogin.setOnClickListener(this);
        mBtnBindOrLogin.setText("登录");
        mEtMsgCode.setOnEditorActionListener(mOnEditorActionListener);
    }
    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                bindOrLogin();
                return true;
            }
            return false;
        }
    };

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_get_msgcode:
                //获取验证码
                doCountDowntime();
                break;

            case R.id.btn_bind_or_login:
                //绑定或者登录
                bindOrLogin();
                break;
        }
    }

    /**
     * 验证码开始倒计时
     */
    private void doCountDowntime() {
        mBtnGetMsgCode.show(this,60);
    }

    /**
     * 绑定或者登录校验
     */
    private void bindOrLogin() {
        String num = mEtPhoneNum.getText().toString().trim();
        String msgCode = mEtMsgCode.getText().toString().trim();

        if (TextUtils.isEmpty(num) && TextUtils.isEmpty(msgCode)) {
            CommonUtils.showToast(this,"号码或手机验证码为空");
            return;
        }

        // TODO: 2017/7/16 0016  调用登录接口,根据状态码判断情况
        int state = 0;

        if (state == 0) {
            CommonUtils.toNextActivity(this, MainActivity.class);
            finish();
        }

        if (state == 1){
            //未注册
            mBtnBindOrLogin.setText("绑定");
            iconAndTitle.setVisibility(View.GONE);
            noVipTip.setVisibility(View.VISIBLE);
            return;
        }

        mBtnBindOrLogin.setText("登录");
        iconAndTitle.setVisibility(View.VISIBLE);
        noVipTip.setVisibility(View.GONE);
    }
}
