package com.shanlin.autostore.activity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.animation.LinearInterpolator;

import com.shanlin.autostore.R;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.MPermissionUtils;
import com.shanlin.autostore.utils.StatusBarUtils;

/**
 * Created by DELL on 2017/7/16 0016.
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StatusBarUtils.setColor(this, Color.TRANSPARENT);
        MPermissionUtils.requestPermissionsResult(this, 1, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_CONTACTS}, new MPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                loadAnim();
            }

            @Override
            public void onPermissionDenied() {
                MPermissionUtils.showTipsDialog(SplashActivity.this);
            }
        });
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
