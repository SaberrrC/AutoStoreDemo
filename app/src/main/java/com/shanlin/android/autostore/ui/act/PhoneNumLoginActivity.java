package com.shanlin.android.autostore.ui.act;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.shanlin.android.autostore.common.base.BaseActivity;
import com.shanlin.android.autostore.common.utils.CommonUtils;
import com.shanlin.android.autostore.common.utils.MPermissionUtils;
import com.shanlin.android.autostore.common.utils.SpUtils;
import com.shanlin.android.autostore.common.utils.ToastUtils;
import com.shanlin.android.autostore.entity.body.WechatSaveMobileBody;
import com.shanlin.android.autostore.entity.respone.CodeBean;
import com.shanlin.android.autostore.entity.respone.LoginBean;
import com.shanlin.android.autostore.entity.respone.WxUserInfoBean;
import com.shanlin.android.autostore.presenter.Contract.PhoneLoginActContract;
import com.shanlin.android.autostore.presenter.PhoneLoginPresenter;
import com.shanlin.autostore.AutoStoreApplication;
import com.shanlin.autostore.R;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.utils.ProgressDialog;
import com.shanlin.autostore.utils.StrUtils;
import com.shanlin.autostore.view.CountDownTextView;
import butterknife.BindView;
import butterknife.OnClick;
import tech.michaelx.authcode.AuthCode;
import tech.michaelx.authcode.CodeConfig;

/**
 * Created by dell、 on 2017/8/15.
 */

public class PhoneNumLoginActivity extends BaseActivity<PhoneLoginPresenter> implements PhoneLoginActContract.View,TextView.OnEditorActionListener {

    @BindView(R.id.et_msg_code)
    EditText mEtMsgCode;
    @BindView(R.id.et_phone_num)
    EditText mEtPhoneNum;
    @BindView(R.id.btn_bind_or_login)
    Button mBtnBindOrLogin;
    @BindView(R.id.btn_get_msgcode)
    CountDownTextView mBtnGetMsgCode;
    @BindView(R.id.icon_title)
    View iconAndTitle;
    @BindView(R.id.no_vip_tip)
    View noVipTip;

    public static final int CODE_LENTH = 4;
    private ProgressDialog progressDialog;
    private boolean togon = false;//控制验证码edittext是否需要在第四位输入后进行操作
    private WxUserInfoBean wxUserInfoBean;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_phone_num_login;
    }

    @Override
    public void initData() {
        progressDialog = new ProgressDialog(this);
        mBtnGetMsgCode.setClickable(true);
        mEtMsgCode.setOnEditorActionListener(this);
        mEtPhoneNum.setOnEditorActionListener(this);
        Intent intent = getIntent();
        if (intent != null) {
            String stringExtra = intent.getStringExtra(Constant.FACE_VERIFY);
            if (TextUtils.equals(stringExtra, Constant.FACE_VERIFY_NO)) {//用户没有人脸认证 显示
                mBtnBindOrLogin.setText("绑定");
                iconAndTitle.setVisibility(View.GONE);
                noVipTip.setVisibility(View.VISIBLE);
                return;
            }
        }
        mBtnBindOrLogin.setText("登录");
        iconAndTitle.setVisibility(View.VISIBLE);
        noVipTip.setVisibility(View.GONE);
        mEtMsgCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!togon) {
                    return;
                }
                if (editable.length() >= 4) {
                    mEtMsgCode.setSelection(editable.length());
                }
                togon = false;
            }
        });
    }

    @OnClick({R.id.btn_get_msgcode,R.id.btn_bind_or_login})
    public void OnClick(View v){

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
     * 绑定或者登录校验
     */
    private void bindOrLogin() {
        String phone = mEtPhoneNum.getText().toString().trim();
        String msgCode = mEtMsgCode.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showToast("请输入手机号");
            return;
        }
        if (!StrUtils.isMobileNO(phone)) {
            ToastUtils.showToast("手机号格式有误");
            return;
        }
        if (TextUtils.isEmpty(msgCode)) {
            ToastUtils.showToast("请输入验证码");
            return;
        }
        if (!CommonUtils.checkNet()) {
            return;
        }

        //判断是普通登陆还是微信绑定
        wxUserInfoBean = (WxUserInfoBean) getIntent().getSerializableExtra(Constant.WX_INFO);

        if (wxUserInfoBean == null) {
            //调用手机号登陆接口
            mPresenter.doPhoneLogin(phone,msgCode);
        } else {//微信登录跳转过来
            WechatSaveMobileBody wechatSaveMobileBody = new WechatSaveMobileBody();
            wechatSaveMobileBody.setMobile(phone);
            wechatSaveMobileBody.setValidCode(msgCode);
            WechatSaveMobileBody.ExtraBean extraBean = new WechatSaveMobileBody.ExtraBean();
            extraBean.setNickname(wxUserInfoBean.getNickname());
            extraBean.setOpenid(wxUserInfoBean.getOpenid());
            extraBean.setSex(wxUserInfoBean.getSex() + "");
            wechatSaveMobileBody.setExtra(extraBean);
            //调用微信登陆接口
            mPresenter.doWXLogin(wechatSaveMobileBody);
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEtMsgCode.getWindowToken(), 0);
    }

    private boolean checkPhoneNum(String phone) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showToast("请输入手机号");
            return true;
        }
        if (!StrUtils.isMobileNO(phone)) {//手机号格式不对
            ToastUtils.showToast("手机号格式有误");
            return true;
        }
        return false;
    }

    /**
     * 验证码开始倒计时
     */
    private void doCountDowntime() {
        //是否有网络
        String phone = mEtPhoneNum.getText().toString().trim();
        if (checkPhoneNum(phone))
            return;
        if (!CommonUtils.checkNet()) {
            return;
        }
        mEtMsgCode.requestFocus();
        mBtnGetMsgCode.show(this, 60);
        //调用获取验证码接口
        mPresenter.getMsgCode(phone);
        progressDialog.show();
    }

    @Override
    public void onMsgCodeSuccess(String code, CodeBean data, String msg) {
        progressDialog.dismiss();
        ToastUtils.showToast(msg);
        CommonUtils.checkPermission(PhoneNumLoginActivity.this, new MPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                togon = true;
                CodeConfig config = new CodeConfig.Builder().codeLength(CODE_LENTH) // 设置验证码长度
                        //                                .smsFromStart(10690333) // 设置验证码发送号码前几位数字
                        .smsFrom(10690333031369L) // 如果验证码发送号码固定，则可以设置验证码发送完整号码
                        .smsBodyStartWith("验证码") // 设置验证码短信开头文字
                        .smsBodyContains("在3分钟内有效。如非本人操作请忽略本短信") // 设置验证码短信内容包含文字
                        .build();
                AuthCode.getInstance().with(PhoneNumLoginActivity.this).config(config).into(mEtMsgCode);
            }

            @Override
            public void onPermissionDenied() {
            }
        });
    }

    @Override
    public void onMsgCodeFailed(Throwable ex, String code, String msg) {
        mBtnGetMsgCode.reset();
        progressDialog.dismiss();
        ToastUtils.showToast(msg);
    }

    @Override
    public void onPhoneLoginSuccess(String code, LoginBean data, String msg) {
        if (data.getData() == null) {
            return;
        }
        AutoStoreApplication.isLogin = true;
        SpUtils.saveString(PhoneNumLoginActivity.this, Constant.TOKEN, data.getData().getToken());
        //保存用户乐买宝认证信息
        mPresenter.getUserAuthenStatus(data.getData().getToken());
        SpUtils.saveString(PhoneNumLoginActivity.this, Constant.USER_PHONE_LOGINED, data.getData().getMobile());
        SpUtils.saveString(PhoneNumLoginActivity.this, Constant.TOKEN, data.getData().getToken());
        Intent intent = new Intent(PhoneNumLoginActivity.this, MainActivity.class);
        intent.putExtra(Constant.FACE_VERIFY, data.getData().getFaceVerify());
        if (wxUserInfoBean != null) {
            SpUtils.saveString(PhoneNumLoginActivity.this, Constant.WX_NICKNAME, wxUserInfoBean.getNickname());
        }
        if (!TextUtils.isEmpty(data.getData().getAvetorUrl())) {
            SpUtils.saveString(PhoneNumLoginActivity.this, Constant.USER_HEAD_URL, data.getData().getAvetorUrl());
        } else if (wxUserInfoBean != null){
            SpUtils.saveString(PhoneNumLoginActivity.this, Constant.USER_HEAD_URL, wxUserInfoBean.getHeadimgurl());
        }
        intent.putExtra(Constant.USER_INFO, data);
        startActivity(intent);
        killActivity(LoginActivity.class);
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    public void onPhoneLoginFailed(Throwable ex, String code, String msg) {
        mBtnGetMsgCode.reset();
        ToastUtils.showToast(msg);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            bindOrLogin();
            return true;
        }
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            if (checkPhoneNum(v.getText().toString()))
                return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //号码回显
        String phone = SpUtils.getString(PhoneNumLoginActivity.this, Constant.USER_PHONE_HISTORY, "");
        mEtPhoneNum.setText(phone);
        mEtPhoneNum.setSelection(phone.length());
    }

    @Override
    protected void onPause() {
        super.onPause();
        SpUtils.saveString(PhoneNumLoginActivity.this, Constant.USER_PHONE_HISTORY, mEtPhoneNum.getText().toString().trim());
    }
}
