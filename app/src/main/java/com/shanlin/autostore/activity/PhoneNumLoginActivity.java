package com.shanlin.autostore.activity;

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

import com.shanlin.android.autostore.common.utils.MPermissionUtils;
import com.shanlin.android.autostore.common.utils.SpUtils;
import com.shanlin.android.autostore.common.utils.ToastUtils;
import com.shanlin.android.autostore.ui.act.LoginActivity;
import com.shanlin.android.autostore.ui.act.MainActivity;
import com.shanlin.autostore.AutoStoreApplication;
import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.bean.LoginBean;
import com.shanlin.autostore.bean.paramsBean.CodeSendBean;
import com.shanlin.autostore.bean.paramsBean.NumberLoginBean;
import com.shanlin.autostore.bean.paramsBean.WechatSaveMobileBody;
import com.shanlin.autostore.bean.resultBean.CodeBean;
import com.shanlin.autostore.bean.resultBean.WxUserInfoBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.net.CustomCallBack;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.ProgressDialog;
import com.shanlin.autostore.utils.StrUtils;
import com.shanlin.autostore.view.CountDownTextView;

import retrofit2.Call;
import tech.michaelx.authcode.AuthCode;
import tech.michaelx.authcode.CodeConfig;


/**
 * Created by DELL on 2017/7/14 0014.
 */
public class PhoneNumLoginActivity extends BaseActivity implements TextView.OnEditorActionListener {

    public static final int CODE_LENTH = 4;
    private EditText          mEtMsgCode;
    private EditText          mEtPhoneNum;
    private Button            mBtnBindOrLogin;
    private CountDownTextView mBtnGetMsgCode;
    private View              iconAndTitle;
    private View              noVipTip;
    private boolean togon = false;//控制验证码edittext是否需要在第四位输入后进行操作
    private ProgressDialog progressDialog;

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

    @Override
    public void initData() {
        progressDialog = new ProgressDialog(this);
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
        if (checkPhoneNum())
            return;
        if (!CommonUtils.checkNet()) {
            return;
        }
        mEtMsgCode.requestFocus();
        mBtnGetMsgCode.show(this, 60);
        progressDialog.show();
        deGetCodeFromNet();
    }

    private boolean checkPhoneNum() {
        String phone = mEtPhoneNum.getText().toString().trim();
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
     * 获取验证码
     */
    private void deGetCodeFromNet() {
        String phone = mEtPhoneNum.getText().toString().trim();
        HttpService service = CommonUtils.doNet();
        Call<CodeBean> call = service.postVerificationCode(new CodeSendBean(phone));
        call.enqueue(new CustomCallBack<CodeBean>() {
            @Override
            public void success(String code, CodeBean data, String msg) {
                if (!msg.contains("验证码发送成功，请注意查收")) {
                    error(null, code, msg);
                    return;
                }
                progressDialog.dismiss();
                ToastUtils.showToast(msg);
                CommonUtils.checkPermission(PhoneNumLoginActivity.this, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        //CommonUtils.toNextActivity(LoginActivity.this, MainActivity.class);
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
            public void error(Throwable ex, String code, String msg) {
                mBtnGetMsgCode.reset();
                progressDialog.dismiss();
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
        final HttpService service = CommonUtils.doNet();
        //判断是普通登陆还是微信绑定
        final WxUserInfoBean wxUserInfoBean = (WxUserInfoBean) getIntent().getSerializableExtra(Constant.WX_INFO);
        Call<LoginBean> loginBeanCall = null;
        if (wxUserInfoBean == null) {
            loginBeanCall = service.postNumCodeLogin(new NumberLoginBean(phone, msgCode));
        } else {//微信登录跳转过来
            WechatSaveMobileBody wechatSaveMobileBody = new WechatSaveMobileBody();
            wechatSaveMobileBody.setMobile(phone);
            wechatSaveMobileBody.setValidCode(msgCode);
            WechatSaveMobileBody.ExtraBean extraBean = new WechatSaveMobileBody.ExtraBean();
            extraBean.setNickname(wxUserInfoBean.getNickname());
            extraBean.setOpenid(wxUserInfoBean.getOpenid());
            extraBean.setSex(wxUserInfoBean.getSex() + "");
            wechatSaveMobileBody.setExtra(extraBean);
            loginBeanCall = service.postWechatSavemobile(wechatSaveMobileBody);
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEtMsgCode.getWindowToken(), 0);
        loginBeanCall.enqueue(new CustomCallBack<LoginBean>() {
            @Override
            public void success(String code, LoginBean data, String msg) {
                if (data.getData() == null) {
                    return;
                }
                AutoStoreApplication.isLogin = true;
                SpUtils.saveString(PhoneNumLoginActivity.this, Constant.TOKEN, data.getData().getToken());
                //保存用户乐买宝认证信息
//                CommonUtils.checkAuthenStatus(PhoneNumLoginActivity.this, service, data.getData().getToken());
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
            public void error(Throwable ex, String code, String msg) {
                mBtnGetMsgCode.reset();
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
