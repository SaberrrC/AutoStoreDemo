package com.sljr.automarket.activity;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.sljr.automarket.R;
import com.sljr.automarket.base.BaseActivity;
import com.sljr.automarket.utils.CommonUtils;

/**
 * Created by DELL on 2017/7/14 0014.
 */

public class SplashActivity1 extends BaseActivity {

    private TextView mCountDownTime;

    @Override
    public int initLayout() {
        return R.layout.activity_splash1;
    }

    @Override
    public void initView() {
        mCountDownTime = ((TextView) findViewById(R.id.tv_countdown_time));
    }

    @Override
    public void initData() {
        ValueAnimator animator = ValueAnimator.ofInt(5,0);
        animator.setDuration(6000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int time = (int)animation.getAnimatedValue();
                mCountDownTime.setText(time+"s");
                if (time == 0) {
                    CommonUtils.toNextActivity(SplashActivity1.this,SplashActivity2
                            .class);
                    finish();
                }
            }
        });
        animator.start();
    }

    @Override
    public void onClick(View v) {

    }
}
