package com.shanlin.android.autostore.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shanlin.android.autostore.common.base.BaseActivity;
import com.shanlin.android.autostore.common.constants.Constant;
import com.shanlin.android.autostore.common.utils.CommonUtils;
import com.shanlin.android.autostore.common.utils.SpUtils;
import com.shanlin.android.autostore.common.utils.ThreadUtils;
import com.shanlin.android.autostore.common.utils.ToastUtils;
import com.shanlin.android.autostore.entity.body.OpenGardBody;
import com.shanlin.android.autostore.entity.respone.CaptureBean;
import com.shanlin.android.autostore.presenter.Contract.GateContract;
import com.shanlin.android.autostore.presenter.GatePresenter;
import com.shanlin.autostore.*;
import com.shanlin.autostore.bean.OpenGardQRBean;
import com.shanlin.autostore.bean.event.OpenGardEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Random;

import butterknife.BindView;

/**
 * Created by cuieney on 17/08/2017.
 */

public class GateActivity extends BaseActivity<GatePresenter> implements GateContract.View {
    @BindView(R.id.tv_open)
    TextView mTvOpen;
    @BindView(R.id.iv_gate_waiting)
    ImageView mIvFly;
    @BindView(R.id.pb_open)
    ProgressBar mPbOpen;
    @BindView(R.id.tv_progress)
    TextView mTvProgress;


    private String mResult;
    private OpenGardQRBean mOpenGardQRBean;
    private boolean toggen = true;
    private boolean timeToggen = true;
    public int progress = 0;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_gate;
    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
        mResult = getIntent().getStringExtra(Constant.QR_GARD);
        CommonUtils.initToolbar(this, "扫一扫", R.color.black, null);
        startAnim();
        Gson gson = new Gson();
        mOpenGardQRBean = gson.fromJson(mResult, OpenGardQRBean.class);
        mPresenter.postGardOpen(new OpenGardBody(mOpenGardQRBean.getDeviceId(), mOpenGardQRBean.getStoreId(), SpUtils.getString(this, Constant.DEVICEID, "")));
    }


    @Override
    public void onGardOpenSuccess(String code, CaptureBean data, String msg) {
        ThreadUtils.runMainDelayed(() -> {
            if (!toggen) {
                return;
            }
            for (int i = progress; i < 100; i++) {
                SystemClock.sleep(new Random().nextInt(25) + 10);
                final int finalI = i;
                ThreadUtils.runMain(() -> {
                    mPbOpen.setProgress(finalI);
                    mTvProgress.setText(finalI + 1 + "%");
                });
                if (i == 99) {
                    ToastUtils.showToast("闸机开启失败,请重试");
                    Intent intent = new Intent(GateActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
                    return;
                }
            }
        }, 30000);
    }

    @Override
    public void onGardOpenFailed(Throwable ex, String code, String msg) {
        ToastUtils.showToast("闸机开启失败" + msg);
        Intent intent = new Intent(GateActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
        return;
    }

    private void startAnim() {
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -0.05f,
                Animation.RELATIVE_TO_SELF, 0.05f);
        animation.setDuration(300);
        animation.setRepeatMode(TranslateAnimation.REVERSE);
        animation.setRepeatCount(Integer.MAX_VALUE);
        mIvFly.setAnimation(animation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getJpushInfo(OpenGardEvent openGardEvent) {
        String s = openGardEvent.toString();
        if (TextUtils.equals(openGardEvent.getStoreId(), mOpenGardQRBean.getStoreId()) && TextUtils.equals(openGardEvent.getType(), "1")) {
            toggen = false;
            timeToggen = false;
            //闸机开了
            new Thread() {
                @Override
                public void run() {
                    for (int i = progress; i < 100; i++) {
                        SystemClock.sleep(new Random().nextInt(25) + 10);
                        final int finalI = i;
                        ThreadUtils.runMain(() -> {
                            mPbOpen.setProgress(finalI);
                            mTvProgress.setText(finalI + 1 + "%");
                        });
                        if (i == 99) {
                            ToastUtils.showToast("闸机已经打开");
                            Intent intent = new Intent(GateActivity.this, MainActivity.class);
                            intent.putExtra(Constant.MainActivityArgument.MAIN_ACTIVITY, Constant.MainActivityArgument.GATE);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            return;
                        }
                    }
                }
            }.start();
        }
    }

}
