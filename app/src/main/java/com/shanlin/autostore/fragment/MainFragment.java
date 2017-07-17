package com.shanlin.autostore.fragment;

import android.content.Intent;
import android.view.View;

import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseFragment;
import com.xys.libzxing.zxing.activity.CaptureActivity;

/**
 * Created by DELL on 2017/7/17 0017.
 */

public class MainFragment extends BaseFragment {

    public static final int REQUEST_CODE = 100;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View rootview) {
        rootview.findViewById(R.id.btn_scan_bg).setOnClickListener(this);
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_main_layout;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_scan_bg:
                // TODO: 2017/7/17 0017  扫描二维码
                startActivityForResult(new Intent(getContext(), CaptureActivity.class), REQUEST_CODE);
                break;
        }
    }

}
