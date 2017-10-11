package com.shanlin.android.autostore.ui.act;

import android.graphics.Color;

import com.shanlin.android.autostore.common.base.SimpleActivity;
import com.shanlin.android.autostore.common.utils.CommonUtils;
import com.shanlin.android.autostore.common.utils.StatusBarUtils;
import com.shanlin.autostore.R;

/**
 * Created by DELL on 2017/7/17 0017.
 */

public class VersionInfoActivity extends SimpleActivity {

    @Override
    public int initLayout() {
        return R.layout.activity_version_info_layout;
    }

    @Override
    public void initData() {
        StatusBarUtils.setColor(this, Color.WHITE);
        CommonUtils.initToolbar(this, "版本信息", R.color.black, null);
    }
}
