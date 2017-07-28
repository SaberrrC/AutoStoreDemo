package com.shanlin.autostore.activity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.animation.LinearInterpolator;

import com.shanlin.autostore.R;
import com.shanlin.autostore.bean.CheckUpdateBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.net.CustomCallBack;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.LogUtils;
import com.shanlin.autostore.utils.MPermissionUtils;
import com.shanlin.autostore.utils.SpUtils;
import com.shanlin.autostore.utils.StatusBarUtils;

import retrofit2.Call;

/**
 * Created by DELL on 2017/7/16 0016.
 */

public class SplashActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StatusBarUtils.setColor(this, Color.TRANSPARENT);
        LogUtils.d("token  " + SpUtils.getString(this, Constant.TOKEN, ""));
        MPermissionUtils.requestPermissionsResult(this, 1, new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_CONTACTS}, new MPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                loadAnim();
            }

            @Override
            public void onPermissionDenied() {
                MPermissionUtils.showTipsDialog(SplashActivity.this);
            }
        });
        checkUpdate();
    }

    private void checkUpdate() {
        HttpService service = CommonUtils.doNet();
        Call<CheckUpdateBean> call = service.doGetCheckUpdate(2);
        call.enqueue(new CustomCallBack<CheckUpdateBean>() {
            @Override
            public void success(String code, CheckUpdateBean data, String msg) {
                //成功
                if (data != null) {
                    try {
                        PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                        String version = data.getVersion();
                        // TODO: 2017-7-27 版本 0.1.1
//                        if (version != null && Integer.valueOf(version).intValue() > packageInfo.versionCode) {
//                            showUpdateDialog(data.getForceUpdate(), data.getDownloadUrl());
//                        }
                        //判断是否更新
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void error(Throwable ex, String code, String msg) {

            }
        });

    }

    private void showUpdateDialog(int forceUpdate, String DownLoadUrl) {
        AlertDialog.Builder adInfo = new AlertDialog.Builder(SplashActivity.this);
        switch (forceUpdate) {
            //强更
            case 0:
                adInfo.setTitle("版本更新");
                adInfo.setMessage("此版本不可用，请更新");
                AlertDialog updateDialog = adInfo.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: 2017/7/26  更新， 进行webView跳转 下载apk
                    }
                }).create();
                updateDialog.setCancelable(false);
                updateDialog.setCanceledOnTouchOutside(false);
                updateDialog.show();
                break;
            //非强更
            case 1:
                break;
            default:
                break;
        }
    }

    /**
     * splash页面等待时长动画
     */
    private void loadAnim() {
        ValueAnimator animator = ValueAnimator.ofInt(3, 0);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (value == 0) {
                    CommonUtils.toNextActivity(SplashActivity.this, LoginActivity.class);
                    finish();
                }
            }
        });
        animator.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
