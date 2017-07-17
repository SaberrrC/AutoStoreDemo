package com.shanlin.autostore.activity;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.utils.CommonUtils;

/**
 * Created by DELL on 2017/7/17 0017.
 */

public class VersionInfoActivity extends BaseActivity {

    private ImageView logo;
    private TextView name;
    private TextView version;
    private TextView title;
    private Toolbar toolbar;

    @Override
    public int initLayout() {
        return R.layout.activity_version_info_layout;
    }

    @Override
    public void initView() {
        toolbar = ((Toolbar) findViewById(R.id.toolbar));
        toolbar.setNavigationIcon(R.mipmap.nav_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.toNextActivity(v.getContext(), MainActivity.class);
                finish();
            }
        });
        title = ((TextView) findViewById(R.id.toolbar_title));
        title.setText("版本信息");
        title.setTextColor(Color.BLACK);
        logo = ((ImageView) findViewById(R.id.logo));
        name = ((TextView) findViewById(R.id.name));
        version = ((TextView) findViewById(R.id.version));
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }
}
