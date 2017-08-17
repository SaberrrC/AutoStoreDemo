package com.shanlin.android.autostore.ui.act;

import android.graphics.Color;

import com.shanlin.android.autostore.common.base.SimpleActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.StatusBarUtils;

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
