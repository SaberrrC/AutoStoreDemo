package com.shanlin.autostore;

import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.shanlin.autostore.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private ActionBarDrawerToggle mDrawerToggle;
    private static final String TAG = "wr";
    private DrawerLayout mDrawerLayout;
    private TextView toolbar_title;
    private Toolbar toolbar;


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
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        drawerSetting();
    }

    /**
     * 抽屉设置
     */
    private void drawerSetting() {
         mDrawerLayout.findViewById(R.id.location_2).setOnClickListener(this);
         mDrawerLayout.findViewById(R.id.location_3).setOnClickListener(this);
         mDrawerLayout.findViewById(R.id.location_4).setOnClickListener(this);
        ListView buyRecordLV = (ListView) mDrawerLayout.findViewById(R.id.mian_lv);
        buyRecordLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.location_2:
                toolbar_title.setText("购买记录");
                toolbar_title.setTextColor(Color.BLACK);
                toolbar.setNavigationIcon(R.mipmap.nav_back);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.location_3:
                break;
            case R.id.location_4:
                break;
        }
    }
}
