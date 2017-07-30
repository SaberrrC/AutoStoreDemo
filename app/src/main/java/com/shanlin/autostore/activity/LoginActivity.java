package com.shanlin.autostore.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.megvii.livenessdetection.LivenessLicenseManager;
import com.shanlin.autostore.AutoStoreApplication;
import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.WxMessageEvent;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.bean.LoginBean;
import com.shanlin.autostore.bean.paramsBean.FaceLoginSendBean;
import com.shanlin.autostore.bean.resultBean.WxUserInfoBean;
import com.shanlin.autostore.bean.sendbean.WechatLoginSendBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.net.CustomCallBack;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.LogUtils;
import com.shanlin.autostore.utils.MPermissionUtils;
import com.shanlin.autostore.utils.SpUtils;
import com.shanlin.autostore.utils.ToastUtils;
import com.shanlin.autostore.zhifubao.Base64;
import com.slfinance.facesdk.service.Manager;
import com.slfinance.facesdk.ui.LivenessActivity;
import com.slfinance.facesdk.util.ConUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.autolayout.utils.AutoUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 2017/7/14 0014.
 */
public class LoginActivity extends BaseActivity {
    public static final int    REQUEST_CODE_LOGIN = 100;
    public static final String TYPE_WX            = "1";
    private AlertDialog  dialog;
    private View         dialogOpenWX;
    //微信登录
    private SendAuth.Req req;
    private IWXAPI       api;
    private static final String WEIXIN_SCOPE = "snsapi_userinfo";// 用于请求用户信息的作用域
    private static final String WEIXIN_STATE = "login_state"; // 自定义

    //人脸识别
    public static final int OK_PERCENT = 73;
    private LivenessLicenseManager livenessLicenseManager;
    private boolean isLivenessLicenseGet = false;
    //活体照片路径
    private Manager manager;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Map<String, Long> managerRegistResult = (Map<String, Long>) msg.obj;
            if (managerRegistResult != null) {
                //活体检测功能授权成功
                isLivenessLicenseGet = managerRegistResult.get(livenessLicenseManager.getVersion()) > 0;
            }
            return true;
        }
    });
    private byte[] mLivenessImgBytes;
    private Dialog mLoadingDialog;

    @Override
    public int initLayout() {

        return R.layout.activity_login;
    }

    public LoginActivity mLoginActivity;

    @Override
    public void initView() {
        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {//判断是否已经注册EventBus
            EventBus.getDefault().register(this);
        }
        mLoginActivity = this;
        findViewById(R.id.btn_login_by_face).setOnClickListener(this);
        findViewById(R.id.btn_login_by_phone).setOnClickListener(this);
        findViewById(R.id.btn_login_by_wx).setOnClickListener(this);
        dialogOpenWX = LayoutInflater.from(this).inflate(R.layout.dialog_open_wx, null);
        dialogOpenWX.findViewById(R.id.tv_open).setOnClickListener(this);
        dialogOpenWX.findViewById(R.id.tv_concel).setOnClickListener(this);
        dialog = new AlertDialog.Builder(this).setView(dialogOpenWX).create();
        //人脸识别
        initLiveness();
    }

    private void initLiveness() {
        //初始化注册管理器
        manager = new Manager(this);
        //如果要使用活体检测抓人脸
        livenessLicenseManager = new LivenessLicenseManager(AutoStoreApplication.getApp());
        boolean livenessLicenseManagerIsRegisted = manager.registerLicenseManager(livenessLicenseManager);
        //可以选择给自己设备打标
        String uuid = ConUtil.getUUIDString(AutoStoreApplication.getApp());
        //异步进行网络注册请求
        final String finalUuid = uuid;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Long> managerRegistResult = manager.takeLicenseFromNetwork(finalUuid);
                handler.sendMessage(handler.obtainMessage(1, managerRegistResult));
            }
        }).start();
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_by_face://使用人脸识别快速登录
                //                                CommonUtils.toNextActivity(this,MainActivity.class);
                MPermissionUtils.requestPermissionsResult(this, 1, new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS}, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        //CommonUtils.toNextActivity(LoginActivity.this, MainActivity.class);
                        Intent intent = new Intent(LoginActivity.this, LivenessActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_LOGIN);
                    }

                    @Override
                    public void onPermissionDenied() {
                        MPermissionUtils.showTipsDialog(LoginActivity.this);
                    }
                });
                break;
            case R.id.btn_login_by_phone://手机号登陆
                CommonUtils.toNextActivity(this, PhoneNumLoginActivity.class);
                break;
            case R.id.btn_login_by_wx:
                dialog.show();
                break;
            case R.id.tv_open:
                // TODO: 2017/7/16 0016 wx登录
                sendAuth();
                dialog.dismiss();
                break;
            case R.id.tv_concel:
                dialog.dismiss();
                break;
        }
    }

    private void sendAuth() {
        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, false);
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
        if (requestCode == REQUEST_CODE_LOGIN) {
            try {
                if (data == null) {
                    ToastUtils.showToast("人脸识别失败");
                    return;
                }
                mLivenessImgBytes = data.getByteArrayExtra("image_best");
                if (mLivenessImgBytes == null || mLivenessImgBytes.length == 0) {
                    ToastUtils.showToast("人脸识别失败");
                    return;
                }
                showLoadingDialog();
                String delta = data.getStringExtra("delta");
                String encode = Base64.encode(mLivenessImgBytes);
                final HttpService httpService = CommonUtils.doNet();
                Call<LoginBean> faceLoginBeanCall = httpService.postFaceLogin(new FaceLoginSendBean(encode, SpUtils.getString(LoginActivity.this, Constant.DEVICEID, "")));
                faceLoginBeanCall.enqueue(new CustomCallBack<LoginBean>() {
                    @Override
                    public void success(String code, LoginBean data, String msg) {
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
                            //验证乐买宝实名是否认证
                            CommonUtils.checkAuthenStatus(LoginActivity.this,httpService,data
                                    .getData()
                                    .getToken());

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra(Constant.FACE_VERIFY, Constant.FACE_VERIFY_OK);
                            intent.putExtra(Constant.USER_INFO, data);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void error(Throwable ex, String code, String msg) {
                        dismissLoadingDialog();
                        ToastUtils.showToast(msg);
                    }
                });
            } catch (Exception e) {
                dismissLoadingDialog();
                e.printStackTrace();
            }
        }

    }

    private void getResult(final String code) {

        HttpService httpService = CommonUtils.doNet();
        Call<com.shanlin.autostore.bean.resultBean.WxTokenBean> call = httpService.getWxToken("https://api.weixin.qq.com/sns/oauth2/access_token", Constant.APP_ID, Constant.APP_SECRET, code, "authorization_code");
        call.enqueue(new retrofit2.Callback<com.shanlin.autostore.bean.resultBean.WxTokenBean>() {
            @Override
            public void onResponse(Call<WxTokenBean> call, retrofit2.Response<WxTokenBean> response) {
                if (!response.isSuccessful()) {
                    ToastUtils.showToast(response.message());
                    return;
                }
                WxTokenBean wxTokenBean = response.body();
                String access_token = wxTokenBean.getAccess_token();
                String openid = wxTokenBean.getOpenid();
                getUserinfo(access_token, openid);
            }

            @Override
            public void onFailure(Call<WxTokenBean> call, Throwable t) {
                ToastUtils.showToast("微信登陆失败，请重试");
            }
        });

    }


    private void getUserinfo(String access_token, String openid) {
        final HttpService httpService = CommonUtils.doNet();
        Call<com.shanlin.autostore.bean.resultBean.WxUserInfoBean> call = httpService.getWxUserInfo("https://api.weixin.qq.com/sns/userinfo?access_token", access_token, openid);
        ToastUtils.showToast("微信");
        call.enqueue(new Callback<WxUserInfoBean>() {
            @Override
            public void onResponse(Call<WxUserInfoBean> call, Response<WxUserInfoBean> response) {
                if (!response.isSuccessful()) {
                    ToastUtils.showToast("微信登陆失败，请重试");
                    return;
                }
                final WxUserInfoBean wxUserInfoBean = response.body();
                int sex = wxUserInfoBean.getSex();
                String unionid = wxUserInfoBean.getUnionid();
                String openid = wxUserInfoBean.getOpenid();
                String nickname = wxUserInfoBean.getNickname();
                WechatLoginSendBean sendBean = new WechatLoginSendBean();
                sendBean.setType(TYPE_WX);
                sendBean.setUnionid(unionid);
                WechatLoginSendBean.ExtraBean extraBean = new WechatLoginSendBean.ExtraBean();
                extraBean.setSex(sex + "");
                extraBean.setOpenid(openid);
                extraBean.setNickname(nickname);
                sendBean.setExtra(extraBean);
                Call<LoginBean> loginBeanCall = httpService.postWxTokenLogin(sendBean);
                loginBeanCall.enqueue(new CustomCallBack<LoginBean>() {
                    public void success(String code, LoginBean data, String msg) {
                        if (data.getData() == null) {
                            ToastUtils.showToast(msg);
                            return;
                        }
                        if (TextUtils.equals(data.getData().getFaceVerify(), "0")) {
                            //没人脸认证 跳转手机号登陆界面
                            Intent intent = new Intent(LoginActivity.this, PhoneNumLoginActivity.class);
                            intent.putExtra(Constant.FACE_VERIFY, Constant.FACE_VERIFY_NO);
                            intent.putExtra(Constant.USER_INFO, data);
                            // TODO: 2017-7-28 把微信消息传过去
                            startActivity(intent);
                        }
                        if (TextUtils.equals(data.getData().getFaceVerify(), "1")) {
                            //已经验证 保存token和手机号 跳转到主页
                            AutoStoreApplication.isLogin = true;
                            SpUtils.saveString(LoginActivity.this, Constant.TOKEN, data.getData().getToken());
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra(Constant.FACE_VERIFY, Constant.FACE_VERIFY_OK);
                            intent.putExtra(Constant.USER_INFO, data);
                            startActivity(intent);
                            finish();
                        }


                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra(Constant.USER_INFO, data);
                        finish();
                    }

                    @Override
                    public void error(Throwable ex, String code, String msg) {
                        if (TextUtils.equals(code, "204")) {//新用户请绑定手机号注册
                            Intent intent = new Intent(LoginActivity.this, PhoneNumLoginActivity.class);
                            intent.putExtra(Constant.FACE_VERIFY, Constant.FACE_VERIFY_NO);
                            intent.putExtra(Constant.WX_INFO, wxUserInfoBean);
                            startActivity(intent);
                            return;
                        }
                        // TODO: 2017/7/26  登录失败，暂时无数据，跳到main
                        ToastUtils.showToast("微信登录失败，请重试");
                    }
                });


            }

            @Override
            public void onFailure(Call<com.shanlin.autostore.bean.resultBean.WxUserInfoBean> call, Throwable t) {
                ToastUtils.showToast("微信登录失败，请重试");
            }
        });


        //        call.enqueue(new Callback<WxUserInfoBean>() {
        //            @Override
        //            public void onResponse(Call<WxUserInfoBean> call, Response<WxUserInfoBean> response) {
        //                LogUtils.d("微信2");
        //                if (response.isSuccessful()) {
        //                    WxUserInfoBean body = response.body();
        //
        //
        //                    final WechatLoginSendBean wechatLoginSendBean = new WechatLoginSendBean();
        //                    wechatLoginSendBean.setType(TYPE_WX);
        ////                    wechatLoginSendBean.setUnionid(body.getUnionid());
        //                    wechatLoginSendBean.setUnionid(body.unionid);
        //                    //后台查询用户信息sendbean
        //                    WechatLoginSendBean.ExtraBean extra = wechatLoginSendBean.getExtra();
        ////                    extra.setNickname(body.getNickname());
        ////                    extra.setOpenid(body.getOpenid());
        ////                    extra.setSex(body.getSex() + "");
        //                    // TODO: 2017-7-28
        //                    Call<LoginBean> WXLoginCall = httpService.postWxTokenLogin(wechatLoginSendBean);
        //                    WXLoginCall.enqueue(new CustomCallBack<LoginBean>() {
        //                        @Override
        //                        public void success(String code, LoginBean data, String msg) {
        //
        //                            if (data.getData() == null) {
        //                                ToastUtils.showToast(msg);
        //                                return;
        //                            }
        //                            if (TextUtils.equals(data.getData().getFaceVerify(), "0")) {
        //                                //没人脸认证 跳转手机号登陆界面
        //                                Intent intent = new Intent(LoginActivity.this, PhoneNumLoginActivity.class);
        //                                intent.putExtra(Constant.FACE_VERIFY, Constant.FACE_VERIFY_NO);
        //                                // TODO: 2017-7-28 把微信消息传过去
        //
        //
        //                                startActivity(intent);
        //                            }
        //                            if (TextUtils.equals(data.getData().getFaceVerify(), "1")) {
        //                                //已经验证 保存token和手机号 跳转到主页
        //                                AutoStoreApplication.isLogin = true;
        //                                SpUtils.saveString(LoginActivity.this, Constant.TOKEN, data.getData().getToken());
        //                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        //                                intent.putExtra(Constant.FACE_VERIFY, Constant.FACE_VERIFY_OK);
        //                                intent.putExtra(Constant.USER_INFO, data);
        //                                startActivity(intent);
        //                                finish();
        //                            }
        //
        //
        //                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        //                            intent.putExtra(Constant.USER_INFO, data);
        //                            finish();
        //                        }
        //
        //                        @Override
        //                        public void error(Throwable ex, String code, String msg) {
        //
        //                            // TODO: 2017/7/26  登录失败，暂时无数据，跳到main
        //                            Log.d("xx", "error: " + msg);
        //                            ToastUtils.showToast("微信登录失败，请重试");
        //                        }
        //                    });
        //                }
        //            }
        //
        //            @Override
        //            public void onFailure(Call<WxUserInfoBean> call, Throwable t) {
        //                ToastUtils.showToast("微信登录失败，请重试");
        //            }
        //        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //    在产生事件的线程中执行
    @Subscribe
    public void onMessageEventPostThread(WxMessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("WxCode")) {
            getResult(messageEvent.getCode());
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
        mLoadingDialog.dismiss();
    }

}
