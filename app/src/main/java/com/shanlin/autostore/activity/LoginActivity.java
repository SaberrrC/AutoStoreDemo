package com.shanlin.autostore.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;


import com.google.gson.Gson;
import com.megvii.livenessdetection.LivenessLicenseManager;
import com.shanlin.autostore.AutoStoreApplication;
import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.WxTokenBean;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.bean.FaceLoginBean;
import com.shanlin.autostore.bean.MessageEvent;
import com.shanlin.autostore.bean.WxUserInfoBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.net.CustomCallBack;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.LogUtils;
import com.shanlin.autostore.utils.MPermissionUtils;
import com.shanlin.autostore.utils.SpUtils;
import com.shanlin.autostore.utils.ToastUtils;
import com.shanlin.autostore.wxapi.WXEntryActivity;
import com.shanlin.autostore.zhifubao.Base64;
import com.slfinance.facesdk.service.Manager;
import com.slfinance.facesdk.ui.LivenessActivity;
import com.slfinance.facesdk.util.ConUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

/**
 * Created by DELL on 2017/7/14 0014.
 */
public class LoginActivity extends BaseActivity {
    public static final String WX_APPID = "wxb51b89cba83263de";
    public static final String WX_SECRET = "1e73ced172f384ef6305e2276d2a9b96";
    public static final int REQUEST_CODE_LOGIN = 100;
    private static final int WX_CODE_LOGIN = 101;
    private AlertDialog dialog;
    private View dialogOpenWX;
    //微信登录
    private SendAuth.Req req;
    private IWXAPI api;
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

    @Override
    public int initLayout() {
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        return R.layout.activity_login;
    }

    public LoginActivity mLoginActivity;

    public void finishLoginActivity() {
        finish();
    }

    @Override
    public void initView() {
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
                MPermissionUtils.requestPermissionsResult(this, 1, new String[]{Manifest.permission.CAMERA}, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        //                        CommonUtils.toNextActivity(LoginActivity.this, MainActivity.class);
                        Intent intent = new Intent(LoginActivity.this, LivenessActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_LOGIN);
                    }

                    @Override
                    public void onPermissionDenied() {
                        MPermissionUtils.showTipsDialog(LoginActivity.this);
                    }
                });
                break;
            case R.id.btn_login_by_phone:
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
        api = WXAPIFactory.createWXAPI(this, WX_APPID, false);
        req = new SendAuth.Req();
        req.scope = WEIXIN_SCOPE;
        req.state = WEIXIN_STATE;
        api.registerApp(WX_APPID);
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
                String delta = data.getStringExtra("delta");
                String encode = Base64.encode(mLivenessImgBytes);
                HttpService httpService = CommonUtils.doNet();
                Call<FaceLoginBean> faceLoginBeanCall = httpService.postFaceLogin(encode);
                faceLoginBeanCall.enqueue(new CustomCallBack<FaceLoginBean>() {
                    @Override
                    public void success(String code, FaceLoginBean data, String msg) {
                        if (TextUtils.equals(code, "00")) {//成功
                            //保存token
                            AutoStoreApplication.isLogin = true;
                            SpUtils.saveString(LoginActivity.this, Constant.TOKEN, data.getData().getToken());
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra(Constant.MainActivityArgument.MAIN_ACTIVITY, Constant.MainActivityArgument.LOGIN);
                            startActivity(intent);
                            finish();
                            return;
                        }
                        if (TextUtils.equals(code, "01")) {//失败
                            ToastUtils.showToast("识别失败，请重试");
                            return;
                        }
                    }

                    @Override
                    public void error(Throwable ex, String code, String msg) {
                        ToastUtils.showToast(msg);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void getResult(final String code) {
                // https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
//                String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
//                        + WX_APPID
//                        + "&secret="
//                        + WX_SECRET
//                        + "&code="
//                        + code
//                        + "&grant_type=authorization_code";
//                OkHttpClient okHttpClient = new OkHttpClient();
//                Request request = new Request.Builder()
//                        .get()
//                        .url(path)
//                        .build();
//                okHttpClient.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(okhttp3.Call call, IOException e) {
//                        Log.e("tian", "请求token值失败");
//                    }
//
//                    @Override
//                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
//                        Log.e("tian", "请求token值成功");
//                        //请求成功
//                        String json = response.body().string();
//                        Log.e("tian", "请求的token的字符串:" + json);
//                        Gson gson = new Gson();
//                        WxTokenBean wxTokenBean = gson.fromJson(json, WxTokenBean.class);
//                        String access_token = wxTokenBean.getAccess_token();
//                        String openid = wxTokenBean.getOpenid();
//                        //TODO:token失效
//                        Log.e("tian", "token值" + access_token);
//                        getUserinfo(access_token, openid);
//                    }
//
//                }
// );
                HttpService httpService = CommonUtils.doNet();
                Call<WxTokenBean> call = httpService.getWxUserInfo("https://api.weixin.qq.com/sns/oauth2/access_token", WX_APPID, WX_SECRET, code, "authorization_code");
                call.enqueue(new retrofit2.Callback<WxTokenBean>() {
                    @Override
                    public void onResponse(Call<WxTokenBean> call, retrofit2.Response<WxTokenBean> response) {
                        WxTokenBean body = response.body();
                    }

                    @Override
                    public void onFailure(Call<WxTokenBean> call, Throwable t) {

                    }
                });

            }



    private void getUserinfo(String access_token, String openid) {
        String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + access_token + "&openid=" + openid;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(path)
                .build();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    //在产生事件的线程中执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventPostThread(MessageEvent messageEvent) {
        Log.d("xx", "onMessageEventPostThread: ");
        if (messageEvent.getMessage().equals("code")) {
            getResult(messageEvent.getMessage());
        }
    }
}
