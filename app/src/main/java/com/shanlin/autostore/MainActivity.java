package com.shanlin.autostore;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.shanlin.autostore.activity.GateActivity;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.fragment.BuyRecordFragment;
import com.shanlin.autostore.fragment.MainFragment;
import com.shanlin.autostore.fragment.VersionInfoFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ActionBarDrawerToggle mDrawerToggle;
    private static final String TAG = "wr";
    private DrawerLayout mDrawerLayout;
    private TextView toolbar_title;
    private Toolbar toolbar;
    private List<Fragment> fragments = new ArrayList<>();
    private Dialog mGateOpenDialog;


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
        initFragments();
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
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.location_2:
                chooseFragement("购买记录",1);
                break;
            case R.id.location_3:
                chooseFragement("版本信息",2);
                break;
            case R.id.location_4:
                break;
        }
    }

    private void chooseFragement(String title,int index) {
        toolbar_title.setText(title);
        toolbar_title.setTextColor(Color.BLACK);
        toolbar.setNavigationIcon(R.mipmap.nav_back);
        mDrawerLayout.closeDrawer(Gravity.LEFT);
        changeFrag(index);
    }

    /**
     * 初始化fargmenst
     */
    private void initFragments() {
        fragments.add(new MainFragment());
        fragments.add(new BuyRecordFragment());
        fragments.add(new VersionInfoFragment());
        changeFrag(0);
    }

    private void changeFrag(int curIndex) {
        for (int i = 0; i < fragments.size(); i++) {
            if (i == curIndex) {
                showFragment(fragments.get(i));
            } else {
                hideFragment(fragments.get(i));
            }
        }
    }


    protected void hideFragment(Fragment currFragment) {
        if (currFragment == null)
            return;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        currFragment.onPause();
        if (currFragment.isAdded()) {
            transaction.hide(currFragment);
            transaction.commitAllowingStateLoss();
        }
    }

    protected void showFragment(Fragment startFragment) {
        if (startFragment == null)
            return;
        FragmentTransaction startFragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        if (!startFragment.isAdded()) {
            startFragmentTransaction.add(R.id.container, startFragment);
        } else {
            startFragment.onResume();
            startFragmentTransaction.show(startFragment);
        }
        startFragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            int width = data.getExtras().getInt("width");
            int height = data.getExtras().getInt("height");
            String result = data.getExtras().getString("result");
            // TODO: 2017-7-17 判断 result 成功进入超市
            startActivity(new Intent(this, GateActivity.class));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String argument = intent.getStringExtra(Constant.MainActivityArgument.MAIN_ACTIVITY);
        if (TextUtils.equals(Constant.MainActivityArgument.GATE, argument)) {


            return;
        }


    }


//    private void showGateOpenDialog() {
//        mGateOpenDialog = new Dialog(this, R.style.MyDialogCheckVersion);
//        //点击其他地方消失
//        mGateOpenDialog.setCanceledOnTouchOutside(false);
//        //填充对话框的布局
//        mUpdateDialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dialog_gateopen, null, false);
//        //初始化控件
//        TextView tvContent = (TextView) mUpdateDialogView.findViewById(R.id.tv_version_content);
//        TextView tvCancle = (TextView) mUpdateDialogView.findViewById(R.id.tv_version_cancle);
//        TextView tvUpdate = (TextView) mUpdateDialogView.findViewById(R.id.tv_version_update);
//
//        tvCancle.setOnClickListener(this);
//        tvUpdate.setOnClickListener(this);
//        //将布局设置给Dialog
//        mGateOpenDialog.setContentView(mUpdateDialogView);
//        //获取当前Activity所在的窗体
//        Window dialogWindow = m.getWindow();
//        //设置Dialog从窗体底部弹出
//        dialogWindow.setGravity(Gravity.CENTER);
//        //获得窗体的属性
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        //        lp.y = 20;//设置Dialog距离底部的距离
//        //       将属性设置给窗体
//        dialogWindow.setAttributes(lp);
//        mGateOpenDialog.show();//显示对话框
//    }



}
