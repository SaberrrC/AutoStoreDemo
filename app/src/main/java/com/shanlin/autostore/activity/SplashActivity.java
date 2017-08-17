package com.shanlin.autostore.activity;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.animation.LinearInterpolator;

import com.shanlin.android.autostore.common.utils.MPermissionUtils;
import com.shanlin.android.autostore.common.utils.SpUtils;
import com.shanlin.android.autostore.common.utils.ThreadUtils;
import com.shanlin.android.autostore.common.utils.ToastUtils;
import com.shanlin.android.autostore.common.utils.VersionManagementUtil;
import com.shanlin.android.autostore.ui.act.LoginActivity;
import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.WxMessageEvent;
import com.shanlin.autostore.bean.resultBean.CheckUpdateBean;
import com.shanlin.autostore.bean.resultBean.PersonInfoBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.net.CustomCallBack;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.StatusBarUtils;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 2017/7/16 0016.
 */

public class SplashActivity extends Activity {

    private AlertDialog updateDialog;
    private String      forceUpdate;
    private static final int REQUEST_CODE_DOWN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StatusBarUtils.setColor(this, Color.TRANSPARENT);
        EventBus.getDefault().post(new WxMessageEvent());
        //        loadAnim();
        ThreadUtils.runMainDelayed(new Runnable() {
            @Override
            public void run() {
                CommonUtils.checkPermission(SplashActivity.this, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        if (!CommonUtils.checkNet()) {
                            toLoginActivity();
                            return;
                        }
                        CommonUtils.getDevicedID();
                        CommonUtils.netWorkWarranty();
                        checkUpdate();
                    }

                    @Override
                    public void onPermissionDenied() {
                        MPermissionUtils.showTipsDialog(SplashActivity.this);
                    }
                });
            }
        }, 2000);
    }

    private void checkToken() {
        String token = SpUtils.getString(this, Constant.TOKEN, "");
        HttpService httpService = CommonUtils.doNet();
        Call<PersonInfoBean> call = httpService.getPersonInfo(token);
        //        mPersonInfoBeanCustomCallBack.setJumpLogin(false);
        call.enqueue(new Callback<PersonInfoBean>() {

            @Override
            public void onResponse(Call<PersonInfoBean> call, Response<PersonInfoBean> response) {
                if (!response.isSuccessful()) {
                    toLoginActivity();
                    return;
                }
                if (response.body() == null) {
                    toLoginActivity();
                    return;
                }
                PersonInfoBean data = response.body();
                if (data.getData() == null) {
                    toLoginActivity();
                    return;
                }
                toMainActivity(data);
            }

            @Override
            public void onFailure(Call<PersonInfoBean> call, Throwable ex) {
                toLoginActivity();
            }
        });

    }

    private void toMainActivity(PersonInfoBean data) {
        SpUtils.saveString(this, Constant.USER_PHONE_LOGINED, data.getData().getDate().getMobile());
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        if (TextUtils.isEmpty(data.getData().getDate().getFaceToken())) {
            intent.putExtra(Constant.FACE_VERIFY, Constant.FACE_VERIFY_NO);
        } else {
            intent.putExtra(Constant.FACE_VERIFY, Constant.FACE_VERIFY_OK);
        }
        startActivity(intent);
        finish();
    }

    private void toLoginActivity() {
        CommonUtils.toNextActivity(SplashActivity.this, LoginActivity.class);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateDialog != null)
            updateDialog.dismiss();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //        if (forceUpdate != 1)
        //            loadAnim();
    }

    private void checkUpdate() {
        HttpService service = CommonUtils.doNet();
        Call<CheckUpdateBean> call = service.doGetCheckUpdate(2);
        mCheckUpdateBeanCustomCallBack.setJumpLogin(false);
        call.enqueue(mCheckUpdateBeanCustomCallBack);
    }

    CustomCallBack<CheckUpdateBean> mCheckUpdateBeanCustomCallBack = new CustomCallBack<CheckUpdateBean>() {
        @Override
        public void success(String code, CheckUpdateBean data, String msg) {
            if (data == null) {
                checkToken();
                return;
            }
            if (data.getData() == null) {
                checkToken();
                return;
            }
            //成功
            try {
                //判断是否更新
                // TODO: 2017-7-27 版本 0.1.1
                String currentVersion = VersionManagementUtil.getVersion(SplashActivity.this);
                String version = data.getData().getVersion();
                forceUpdate = data.getData().getForceUpdate();
                if (version == null) {
                    checkToken();
                    return;
                }
                //比较版本，返回1需要更新
                if (VersionManagementUtil.VersionComparison(version, currentVersion) == 1) {
                    //更新
                    showUpdateDialog(data.getData().getForceUpdate(), data.getData().getDownloadUrl());
                } else {
                    checkToken();
                }
            } catch (Exception e) {
                e.printStackTrace();
                checkToken();
            }
        }

        @Override
        public void error(Throwable ex, String code, String msg) {
            checkToken();
        }
    };

    private void upData(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
//        Uri content_url = Uri.parse("http://127.0.0.1:8080//app.apk");
        intent.setData(content_url);
        try {
            startActivityForResult(intent,REQUEST_CODE_DOWN);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast("更新失败");
            checkToken();
        }
    }

    private void showUpdateDialog(String forceUpdate, final String DownLoadUrl) {
        final AlertDialog.Builder adInfo = new AlertDialog.Builder(SplashActivity.this);
        switch (forceUpdate) {
            //强更
            case "1":
                CommonUtils.checkPermission(SplashActivity.this, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        adInfo.setTitle("版本更新");
                        adInfo.setMessage("此版本不可用，请更新");
                        updateDialog = adInfo.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO: 2017/7/26  更新， 进行web跳转 下载apk
                                upData(DownLoadUrl);
                            }
                        }).create();
                        updateDialog.setCancelable(false);
                        updateDialog.setCanceledOnTouchOutside(false);
                        updateDialog.show();
                    }

                    @Override
                    public void onPermissionDenied() {
                        checkToken();
                    }
                });
                break;
            //非强更
            case "0":
                adInfo.setTitle("版本更新");
                adInfo.setMessage("发现新版本，请更新");
                updateDialog = adInfo.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: 2017/7/26  更新， 进行web跳转 下载apk
                        upData(DownLoadUrl);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateDialog.dismiss();
                        checkToken();
                    }
                }).create();
                updateDialog.setCancelable(false);
                updateDialog.setCanceledOnTouchOutside(false);
                updateDialog.show();
                break;
        }
    }

    /**
     * splash页面等待时长动画
     */
    private void loadAnim() {
        ValueAnimator animator = ValueAnimator.ofInt(2, 0);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(2000);
        //        animator.addUpdateListener(this);
        animator.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
