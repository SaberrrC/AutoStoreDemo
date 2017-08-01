package com.shanlin.autostore.activity;

import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.bean.OpenGardQRBean;
import com.shanlin.autostore.bean.event.OpenGardEvent;
import com.shanlin.autostore.bean.paramsBean.OpenGardBody;
import com.shanlin.autostore.bean.resultBean.CaptureBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.net.CustomCallBack;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.LogUtils;
import com.shanlin.autostore.utils.SpUtils;
import com.shanlin.autostore.utils.ThreadUtils;
import com.shanlin.autostore.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Random;

import retrofit2.Call;

public class GateActivity extends BaseActivity {
    private TextView       mTvOpen;
    private ImageView      mIvBg;
    private ProgressBar    mPbOpen;
    private TextView       mTvProgress;
    private String         mResult;
    private OpenGardQRBean mOpenGardQRBean;
    private boolean toggen     = true;
    private boolean timeToggen = true;
    public  int     progress   = 0;

    @Override
    public int initLayout() {
        return R.layout.activity_gate;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        mTvOpen = (TextView) findViewById(R.id.tv_open);
        mPbOpen = (ProgressBar) findViewById(R.id.pb_open);
        mTvProgress = (TextView) findViewById(R.id.tv_progress);
        mResult = getIntent().getStringExtra(Constant.QR_GARD);
        Gson gson = new Gson();
        mOpenGardQRBean = gson.fromJson(mResult, OpenGardQRBean.class);
        sendOpenInfo();
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 80; i++) {
                    getRandomTime();
                    final int finalI = i;
                    progress = i;
                    ThreadUtils.runMain(new Runnable() {
                        @Override
                        public void run() {
                            mPbOpen.setProgress(finalI);
                            mTvProgress.setText(finalI + 1 + "%");
                            if (!timeToggen) {
                                return;
                            }
                        }
                    });

                }
            }
        }.start();

    }

    private void sendOpenInfo() {
        HttpService service = CommonUtils.doNet();
        String deviceId = SpUtils.getString(this, Constant.DEVICEID, "");
        OpenGardBody openGardBody = new OpenGardBody(mOpenGardQRBean.getDeviceId(), mOpenGardQRBean.getStoreId(), deviceId);
        LogUtils.d(openGardBody.toString());
        Call<CaptureBean> call = service.postGardOpen(SpUtils.getString(this, Constant.TOKEN, ""), openGardBody);
        call.enqueue(new CustomCallBack<CaptureBean>() {
            @Override
            public void success(String code, CaptureBean data, String msg) {
                ThreadUtils.runMainDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!toggen) {
                            return;
                        }
                        for (int i = progress; i < 100; i++) {
                            getRandomTime();
                            final int finalI = i;
                            ThreadUtils.runMain(new Runnable() {
                                @Override
                                public void run() {
                                    mPbOpen.setProgress(finalI);
                                    mTvProgress.setText(finalI + 1 + "%");
                                }
                            });
                            if (i == 99) {
                                ToastUtils.showToast("闸机开启失败,请重试");
                                Intent intent = new Intent(GateActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                return;
                            }
                        }
                    }
                }, 30000);
            }

            @Override
            public void error(Throwable ex, String code, String msg) {
                ToastUtils.showToast("闸机开启失败" + msg);
                Intent intent = new Intent(GateActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }

    private void getRandomTime() {
        SystemClock.sleep(new Random().nextInt(25) + 10);
    }


    @Override
    public void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {

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
                        getRandomTime();
                        final int finalI = i;
                        ThreadUtils.runMain(new Runnable() {
                            @Override
                            public void run() {
                                mPbOpen.setProgress(finalI);
                                mTvProgress.setText(finalI + 1 + "%");
                            }
                        });
                        if (i == 99) {
                            ToastUtils.showToast("闸机已经打开");
                            Intent intent = new Intent(GateActivity.this, MainActivity.class);
                            intent.putExtra(Constant.MainActivityArgument.MAIN_ACTIVITY, Constant.MainActivityArgument.GATE);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }
                }
            }.start();
        }
    }
}
