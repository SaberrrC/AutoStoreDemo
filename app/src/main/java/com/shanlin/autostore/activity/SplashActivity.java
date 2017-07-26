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
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.shanlin.autostore.R;
import com.shanlin.autostore.bean.CheckUpdateBean;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.MPermissionUtils;
import com.shanlin.autostore.utils.StatusBarUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 2017/7/16 0016.
 */

public class SplashActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StatusBarUtils.setColor(this, Color.TRANSPARENT);
        MPermissionUtils.requestPermissionsResult(this, 1, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE}, new MPermissionUtils.OnPermissionListener() {
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
        call.enqueue(new Callback<CheckUpdateBean>() {
            @Override
            public void onResponse(Call<CheckUpdateBean> call, Response<CheckUpdateBean> response) {
                //成功
                CheckUpdateBean body = response.body();
                try {
                    PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    //判断是否更新
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(Call<CheckUpdateBean> call, Throwable t) {
                Log.d("xx", "getMinVersion:faile ");
            }
        });
    }

    private void showUpdateDialog(int forceUpdate) {
        switch (forceUpdate) {
            //强更
            case 0:
                AlertDialog.Builder adInfo = new AlertDialog.Builder(SplashActivity.this);
                adInfo.setTitle("版本更新");
                adInfo.setMessage("此版本不可用，请更新");
                adInfo.setPositiveButton("更新",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
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
