package com.shanlin.autostore.activity;

import android.content.Intent;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.bean.OpenGardQRBean;
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

import retrofit2.Call;

public class GateActivity extends BaseActivity {
    private TextView    mTvOpen;
    private ImageView   mIvBg;
    private ProgressBar mPbOpen;
    private TextView    mTvProgress;
    private String      mResult;

    @Override
    public int initLayout() {
        return R.layout.activity_gate;
    }

    @Override
    public void initView() {
        mTvOpen = (TextView) findViewById(R.id.tv_open);
        mPbOpen = (ProgressBar) findViewById(R.id.pb_open);
        mTvProgress = (TextView) findViewById(R.id.tv_progress);
        mResult = getIntent().getStringExtra(Constant.QR_GARD);


        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 80; i++) {
                    SystemClock.sleep(20);
                    final int finalI = i;
                    ThreadUtils.runMain(new Runnable() {
                        @Override
                        public void run() {
                            mPbOpen.setProgress(finalI);
                            mTvProgress.setText(finalI + 1 + "%");
                        }
                    });
                    if (i == 79) {
                        sendOpenInfo();
                    }
                }
            }
        }.start();

    }

    private void sendOpenInfo() {
        Gson gson = new Gson();
        OpenGardQRBean openGardQRBean = gson.fromJson(mResult, OpenGardQRBean.class);
        HttpService service = CommonUtils.doNet();
        OpenGardBody openGardBody = new OpenGardBody(openGardQRBean.getDeviceId(), openGardQRBean.getStoreId(), SpUtils.getString(this, Constant.DEVICEID, ""));
        LogUtils.d(openGardBody.toString());
        Call<CaptureBean> call = service.postGardOpen(SpUtils.getString(this, Constant.TOKEN, ""), openGardBody);
        call.enqueue(new CustomCallBack<CaptureBean>() {
            @Override
            public void success(String code, CaptureBean data, String msg) {
                new Thread() {
                    @Override
                    public void run() {
                        for (int i = 80; i < 100; i++) {
                            SystemClock.sleep(20);
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

            @Override
            public void error(Throwable ex, String code, String msg) {
                for (int i = 80; i < 100; i++) {
                    SystemClock.sleep(20);
                    final int finalI = i;
                    ThreadUtils.runMain(new Runnable() {
                        @Override
                        public void run() {
                            mPbOpen.setProgress(finalI);
                            mTvProgress.setText(finalI + 1 + "%");
                        }
                    });
                    if (i == 99) {
                        ToastUtils.showToast("闸机开启失败" + msg);
                        Intent intent = new Intent(GateActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {

    }
}
