package com.shanlin.android.autostore.ui.act;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.shanlin.android.autostore.common.base.BaseActivity;
import com.shanlin.android.autostore.common.utils.MPermissionUtils;
import com.shanlin.android.autostore.common.utils.SpUtils;
import com.shanlin.android.autostore.common.utils.ToastUtils;
import com.shanlin.android.autostore.entity.respone.LoginBean;
import com.shanlin.android.autostore.entity.respone.WxUserInfoBean;
import com.shanlin.android.autostore.presenter.Contract.LoginActContract;
import com.shanlin.android.autostore.presenter.LoginPresenter;
import com.shanlin.autostore.AutoStoreApplication;
import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.WxMessageEvent;
import com.shanlin.autostore.bean.paramsBean.WechatLoginSendBean;
import com.shanlin.autostore.bean.resultBean.WxTokenBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.livenesslib.LivenessActivity;
import com.shanlin.autostore.utils.Base64;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.LogUtils;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.zhy.autolayout.utils.AutoUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DELL on 2017/7/14 0014.
 */
public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginActContract.View {
    public static final int REQUEST_CODE_LOGIN = 100;

    private static final String WEIXIN_SCOPE = "snsapi_userinfo";// 用于请求用户信息的作用域
    private static final String WEIXIN_STATE = "login_state"; // 自定义

    //微信登录
    private SendAuth.Req req;
    private IWXAPI       api;
    private Dialog       WxLoginDialog;

    //人脸识别
    public static final int     OK_PERCENT           = 73;
    private             boolean isLivenessLicenseGet = false;
    //活体照片
    private byte[] mLivenessImgBytes;
    private Dialog mLoadingDialog;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
        setTransAnim(false);
    }

    private void showWxLoginDialog() {
        if (WxLoginDialog != null) {
            WxLoginDialog.show();
            return;
        }
        View dialogOpenWX = LayoutInflater.from(this).inflate(R.layout.dialog_open_wx, null);
        ButterKnife.bind(this, dialogOpenWX);
        WxLoginDialog = new Dialog(this, R.style.MyDialogWithAnim);
        WxLoginDialog.setContentView(dialogOpenWX);
        WxLoginDialog.setCanceledOnTouchOutside(false);
        WxLoginDialog.show();
    }

    private void dismissWxLoginDialog() {
        if (WxLoginDialog != null) {
            WxLoginDialog.dismiss();
        }
    }

    private void sendAuth() {
        req = new SendAuth.Req();
        req.scope = WEIXIN_SCOPE;
        req.state = WEIXIN_STATE;
        api.registerApp(Constant.APP_ID);
        api.sendReq(req);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.d("resultCode==" + resultCode);
        if (requestCode == REQUEST_CODE_LOGIN) {//人脸识别登陆
            if (TextUtils.equals(Constant.ON_BACK_PRESSED, data.getStringExtra(Constant.ON_BACK_PRESSED))) {
                return;
            }
            try {
                mLivenessImgBytes = data.getByteArrayExtra("image_best");
                if (mLivenessImgBytes == null || mLivenessImgBytes.length == 0) {
                    ToastUtils.showToast("信息匹配失败，请选择其他方式登陆");
                    return;
                }
                showLoadingDialog();
                String delta = data.getStringExtra("delta");
                String encode = Base64.encode(mLivenessImgBytes);
                String deviceId = SpUtils.getString(LoginActivity.this, Constant.DEVICEID, "");
                mPresenter.postFaceLogin(encode,deviceId);

            } catch (Exception e) {
                dismissLoadingDialog();
                e.printStackTrace();
            }
        }

    }

    private void getResult(final String code) {


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissLoadingDialog();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventPostThread(WxMessageEvent messageEvent) {
        if (TextUtils.equals(messageEvent.getMessage(), "1")) {
            mLoadingDialog.dismiss();
            return;
        }
        if (messageEvent.getMessage().equals("WxCode")) {
            showLoadingDialog();
            mPresenter.getResult(messageEvent.getCode());
        }
    }

    private void showLoadingDialog() {
        mLoadingDialog = new Dialog(this, R.style.MyDialogCheckVersion);
        //点击其他地方消失
        mLoadingDialog.setCanceledOnTouchOutside(false);
        //填充对话框的布局
        View viewToFace = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dialog_loading, null, false);
        //跳过
        AutoUtils.autoSize(viewToFace);
        //初始化控件
        //将布局设置给
        mLoadingDialog.setContentView(viewToFace);
        //获取当前Activity所在的窗体
        Window dialogWindow = mLoadingDialog.getWindow();
        //设置Dialog从窗体中间弹出
        dialogWindow.setGravity(Gravity.CENTER);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //        lp.y = 20;//设置Dialog距离底部的距离
        //       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        mLoadingDialog.show();//显示对话框
    }

    private void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    @OnClick({R.id.btn_login_by_face, R.id.btn_login_by_phone, R.id.btn_login_by_wx, R.id.tv_concel, R.id.tv_open})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login_by_face://使用人脸识别快速登录
                //是否有网络
                if (!CommonUtils.checkNet()) {
                    return;
                }
                if (!AutoStoreApplication.FACE) {//face++联网认证
                    ToastUtils.showToast("网络错误");
                    return;
                }
                CommonUtils.netWorkWarranty();
                CommonUtils.checkPermission(LoginActivity.this, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        //CommonUtils.toNextActivity(LoginActivity.this, MainActivity.class);
                        Intent intent = new Intent(LoginActivity.this, LivenessActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_LOGIN);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    }

                    @Override
                    public void onPermissionDenied() {
                        MPermissionUtils.showTipsDialog(LoginActivity.this);
                    }
                });
                break;
            case R.id.btn_login_by_phone://手机号登陆
                CommonUtils.toNextActivity(this, PhoneNumLoginActivity.class);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.btn_login_by_wx://微信登陆
                if (!CommonUtils.checkNet()) {
                    return;
                }
                showWxLoginDialog();
                break;
            case R.id.tv_open:
                dismissWxLoginDialog();
                if (!CommonUtils.checkNet()) {
                    return;
                }
                mPresenter.checkWxInstall(this);
                break;
            case R.id.tv_concel:
                dismissWxLoginDialog();
                break;
        }
    }

    /**
     * @param isWxInstalled
     */
    @Override
    public void onWxInstallCheckSuccess(boolean isWxInstalled) {
        if (isWxInstalled) {
            showLoadingDialog();
            sendAuth();
        } else {
            ToastUtils.showToast("请安装微信客户端再进行登陆");
        }
    }

    /**
     * wx获取token
     *
     * @param wxTokenBean
     */
    @Override
    public void onWxResultSuccess(WxTokenBean wxTokenBean) {
        String access_token = wxTokenBean.getAccess_token();
        String openid = wxTokenBean.getOpenid();
        mPresenter.getUserInfo(access_token, openid);
    }

    @Override
    public void onWxResultFailed(Throwable t) {
        dismissLoadingDialog();
        ToastUtils.showToast("微信登陆失败，请重试");
    }

    /**
     * 微信登录获取用户信息
     *
     * @param t
     */
    @Override
    public void onWxUserInfoFailed(Throwable t) {
        dismissLoadingDialog();
        ToastUtils.showToast("微信登录失败，请重试");
    }

    @Override
    public void onWxUserInfoSuccess(WxUserInfoBean userInfoBean, WechatLoginSendBean wxUserInfoBean) {
        mPresenter.postWxTokenLogin(userInfoBean, wxUserInfoBean);
    }

    /**
     * 微信token登录服务器
     *  @param code
     * @param data
     * @param msg
     * @param wxUserInfoBean
     */
    @Override
    public void onWxTokenLoginSuccess(String code, LoginBean data, String msg, WxUserInfoBean wxUserInfoBean) {
        dismissLoadingDialog();
        if (data.getData() == null) {
            ToastUtils.showToast(msg);
            return;
        }
        final String nickname = wxUserInfoBean.getNickname();
        SpUtils.saveString(LoginActivity.this, Constant.WX_NICKNAME, nickname);
        if (TextUtils.equals(data.getData().getFaceVerify(), "0")) {
            //没人脸认证 跳转手机号登陆界面
            Intent intent = new Intent(LoginActivity.this, PhoneNumLoginActivity.class);
            intent.putExtra(Constant.FACE_VERIFY, Constant.FACE_VERIFY_NO);
            intent.putExtra(Constant.USER_INFO, data);
            intent.putExtra(Constant.WX_INFO, wxUserInfoBean);
            // TODO: 2017-7-28 把微信消息传过去
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
        if (TextUtils.equals(data.getData().getFaceVerify(), "1")) {
            //已经验证 保存token和手机号 跳转到主页
            AutoStoreApplication.isLogin = true;
            SpUtils.saveString(LoginActivity.this, Constant.TOKEN, data.getData().getToken());
            if (TextUtils.isEmpty(data.getData().getAvetorUrl())) {
                SpUtils.saveString(LoginActivity.this, Constant.USER_HEAD_URL, wxUserInfoBean.getHeadimgurl());
            } else {
                SpUtils.saveString(LoginActivity.this, Constant.USER_HEAD_URL, data.getData().getAvetorUrl());
            }
            SpUtils.saveString(LoginActivity.this, Constant.USER_PHONE_LOGINED, data.getData().getMobile());
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(Constant.WX_INFO, wxUserInfoBean);
            intent.putExtra(Constant.FACE_VERIFY, Constant.FACE_VERIFY_OK);
            intent.putExtra(Constant.USER_INFO, data);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onWxTokenLoginFailed(Throwable ex, String code, String msg, WxUserInfoBean userInfoBean) {
        dismissLoadingDialog();
        if (TextUtils.equals(code, "204")) {//新用户请绑定手机号注册
            Intent intent = new Intent(LoginActivity.this, PhoneNumLoginActivity.class);
            intent.putExtra(Constant.FACE_VERIFY, Constant.FACE_VERIFY_NO);
            intent.putExtra(Constant.WX_INFO, userInfoBean);
            startActivity(intent);
            return;
        }
        ToastUtils.showToast(msg);
    }

    @Override
    public void onFaceLoginFailed(Throwable ex, String code, String msg) {
        dismissLoadingDialog();
        ToastUtils.showToast(msg);
    }

    /**
     * 人脸登录
     * @param code
     * @param data
     * @param msg
     */

    @Override
    public void onFaceLoginSuccess(String code, LoginBean data, String msg) {
        dismissLoadingDialog();
        if (data.getData() == null) {
            ToastUtils.showToast(msg);
            return;
        }
        if (TextUtils.equals(data.getData().getFaceVerify(), "0")) {
            //没人脸认证 跳转手机号登陆界面
            Intent intent = new Intent(LoginActivity.this, PhoneNumLoginActivity.class);
            intent.putExtra(Constant.FACE_VERIFY, Constant.FACE_VERIFY_NO);
            startActivity(intent);
        }
        if (TextUtils.equals(data.getData().getFaceVerify(), "1")) {
            //已经验证 保存token和手机号 跳转到主页
            AutoStoreApplication.isLogin = true;
            SpUtils.saveString(LoginActivity.this, Constant.TOKEN, data.getData().getToken());
            SpUtils.saveString(LoginActivity.this, Constant.USER_PHONE_LOGINED, data.getData().getMobile());
            if (!TextUtils.isEmpty(data.getData().getAvetorUrl())) {
                SpUtils.saveString(LoginActivity.this, Constant.USER_HEAD_URL, data.getData().getAvetorUrl());
            }
            //验证乐买宝实名是否认证
//            CommonUtils.checkAuthenStatus(LoginActivity.this, httpService, data.getData().getToken());
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(Constant.FACE_VERIFY, Constant.FACE_VERIFY_OK);
            intent.putExtra(Constant.USER_INFO, data);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }

}
