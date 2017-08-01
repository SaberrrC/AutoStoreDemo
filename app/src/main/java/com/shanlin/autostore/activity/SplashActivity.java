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
import android.view.animation.LinearInterpolator;

import com.shanlin.autostore.R;
import com.shanlin.autostore.WxMessageEvent;
import com.shanlin.autostore.bean.resultBean.CheckUpdateBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.net.CustomCallBack;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.LogUtils;
import com.shanlin.autostore.utils.MPermissionUtils;
import com.shanlin.autostore.utils.SpUtils;
import com.shanlin.autostore.utils.StatusBarUtils;
import com.shanlin.autostore.utils.VersionManagementUtil;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;

/**
 * Created by DELL on 2017/7/16 0016.
 */

public class SplashActivity extends Activity {


    private AlertDialog updateDialog;
    private int         forceUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StatusBarUtils.setColor(this, Color.TRANSPARENT);
        EventBus.getDefault().post(new WxMessageEvent());
        LogUtils.d("token  " + SpUtils.getString(this, Constant.TOKEN, ""));
        CommonUtils.checkPermission(this, new MPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                checkUpdate();
                CommonUtils.netWorkWarranty();
            }

            @Override
            public void onPermissionDenied() {

            }
        });
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
        if (forceUpdate != 1)
            loadAnim();
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
                        //判断是否更新
                        // TODO: 2017-7-27 版本 0.1.1
                        String currentVersion = VersionManagementUtil.getVersion(SplashActivity.this);
                        String version = data.getVersion();
                        forceUpdate = data.getForceUpdate();
                        if (version != null) {
                            //比较版本，返回1需要更新
                            if (VersionManagementUtil.VersionComparison(version, currentVersion) == 1) {
                                //更新
                                showUpdateDialog(data.getForceUpdate(), data.getDownloadUrl());
                            } else {
                                loadAnim();
                            }
                        } else
                            loadAnim();
                        //不更新
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void error(Throwable ex, String code, String msg) {
                loadAnim();
            }
        });

    }

    private void upData(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showUpdateDialog(int forceUpdate, final String DownLoadUrl) {
        final AlertDialog.Builder adInfo = new AlertDialog.Builder(SplashActivity.this);
        switch (forceUpdate) {
            //强更
            case 1:
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

                    }
                });
                break;
            //非强更
            case 0:
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
                        loadAnim();
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
