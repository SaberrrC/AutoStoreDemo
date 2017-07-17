package com.shanlin.autostore.activity;

import android.content.Intent;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.utils.ThreadUtils;

public class GateActivity extends BaseActivity {
    private TextView    mTvOpen;
    private ImageView   mIvBg;
    private ProgressBar mPbOpen;
    private TextView    mTvProgress;

    @Override
    public int initLayout() {
        return R.layout.activity_gate;
    }

    @Override
    public void initView() {
        mTvOpen = (TextView) findViewById(R.id.tv_open);
        mPbOpen = (ProgressBar) findViewById(R.id.pb_open);
        mTvProgress = (TextView) findViewById(R.id.tv_progress);
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
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
    public void initData() {

    }

    @Override
    public void onClick(View view) {

    }
}
