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
import com.shanlin.autostore.bean.CodeBean;
import com.shanlin.autostore.bean.NumberLoginRsponseBean;
import com.shanlin.autostore.bean.sendbean.CodeSendBean;
import com.shanlin.autostore.bean.sendbean.NumberLoginBean;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.net.CustomCallBack;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.StrUtils;
import com.shanlin.autostore.utils.ToastUtils;
import com.shanlin.autostore.utils.env.DeviceInfo;
import com.shanlin.autostore.view.CountDownTextView;

import retrofit2.Call;

/**
 * Created by DELL on 2017/7/14 0014.
 */
public class PhoneNumLoginActivity extends BaseActivity implements TextView.OnEditorActionListener {

    private EditText          mEtMsgCode;
    private EditText          mEtPhoneNum;
    private Button            mBtnBindOrLogin;
    private CountDownTextView mBtnGetMsgCode;
    private View              iconAndTitle;
    private View              noVipTip;

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
        mEtMsgCode.setOnEditorActionListener(this);
        mEtPhoneNum.setOnEditorActionListener(this);
    }

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
        //是否有网络
        String networkTypeName = DeviceInfo.getNetworkTypeName();
        if (TextUtils.isEmpty(networkTypeName)) {//没网络
            ToastUtils.showToast("无网络");
            return;
        }
        if (checkPhoneNum())
            return;
        mEtMsgCode.requestFocus();
        mBtnGetMsgCode.show(this, 60);
        deGetCodeFromNet();
    }

    private boolean checkPhoneNum() {
        String phone = mEtPhoneNum.getText().toString().trim();
        if (!StrUtils.isMobileNO(phone)) {//手机号格式不对
            ToastUtils.showToast("请输入正确的手机号");
            return true;
        }
        return false;
    }


    /**
     * 获取验证码
     */
    private void deGetCodeFromNet() {
        String phone = mEtPhoneNum.getText().toString().trim();
        HttpService service = CommonUtils.doNet();
        // @Field
        Call<CodeBean> call = service.postVerificationCode(new CodeSendBean(phone));
        // 创建 网络请求接口 的实例
        // 发送网络请求(异步)
        call.enqueue(new CustomCallBack<CodeBean>() {
            @Override
            public void success(String code, CodeBean data, String msg) {
                ToastUtils.showToast(msg);
            }

            @Override
            public void error(Throwable ex, String code, String msg) {
                mBtnGetMsgCode.reset();
                ToastUtils.showToast(msg);
            }
        });

    }

    /**
     * 绑定或者登录校验
     */
    private void bindOrLogin() {
        String phone = mEtPhoneNum.getText().toString().trim();
        String msgCode = mEtMsgCode.getText().toString().trim();
        if (!StrUtils.isMobileNO(phone)) {
            ToastUtils.showToast("请输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(msgCode)) {
            ToastUtils.showToast("请输入验证码");
            return;
        }
        // TODO: 2017/7/16 0016  调用登录接口,根据状态码判断情况
        HttpService service = CommonUtils.doNet();
        Call<NumberLoginRsponseBean> call = service.postNumCodeLogin(new NumberLoginBean(phone, msgCode));
        call.enqueue(new CustomCallBack<NumberLoginRsponseBean>() {
            @Override
            public void success(String code, NumberLoginRsponseBean data, String msg) {
                if (data.getData() == null) {
                    return;
                }
                int state = 0;
                state = Integer.parseInt(data.getData().getFaceVerify());
                if (state == 0) {
                    CommonUtils.toNextActivity(PhoneNumLoginActivity.this, MainActivity.class);
                    killActivity(LoginActivity.class);
                    finish();
                }
                if (state == 1) {
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

            @Override
            public void error(Throwable ex, String code, String msg) {
                ToastUtils.showToast(msg);
            }
        });


    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            bindOrLogin();
            return true;
        }
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            if (checkPhoneNum())
                return true;
        }
        return false;
    }
}
