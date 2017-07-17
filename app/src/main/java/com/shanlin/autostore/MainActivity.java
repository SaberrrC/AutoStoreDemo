package com.shanlin.autostore;

import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.shanlin.autostore.activity.BuyRecordActivity;
import com.shanlin.autostore.activity.VersionInfoActivity;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ActionBarDrawerToggle mDrawerToggle;
    private static final String TAG = "wr";
    private DrawerLayout mDrawerLayout;
    private TextView toolbar_title;
    private Toolbar toolbar;
    private List<Fragment> fragments = new ArrayList<>();
    private TextPaint paint;


    @Override
    public int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mDrawerLayout = ((DrawerLayout) findViewById(R.id.activity_main));
        toolbar_title = ((TextView) findViewById(R.id.toolbar_title));
    }

    @Override
    public void initData() {
        initToolBar();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.toNextActivity(v.getContext(),MainActivity.class);
                Log.d(TAG, "onClick: ");
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R
                .string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        drawerSetting();
    }

    /**
     * 抽屉设置
     */
    private void drawerSetting() {
         mDrawerLayout.findViewById(R.id.location_2).setOnClickListener(this);
         mDrawerLayout.findViewById(R.id.location_3).setOnClickListener(this);
         mDrawerLayout.findViewById(R.id.location_4).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.location_2:
                CommonUtils.toNextActivity(this, BuyRecordActivity.class);
                break;
            case R.id.location_3:
                CommonUtils.toNextActivity(this, VersionInfoActivity.class);
                break;
            case R.id.location_4:
                break;
        }
    }
}
