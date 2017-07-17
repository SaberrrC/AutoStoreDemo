package com.shanlin.autostore;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.shanlin.autostore.activity.BuyRecordActivity;
import com.shanlin.autostore.activity.GateActivity;
import com.shanlin.autostore.activity.MyLeiMaiBaoActivity;
import com.shanlin.autostore.activity.OpenLeMaiBao;
import com.shanlin.autostore.activity.VersionInfoActivity;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.utils.CommonUtils;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import static com.shanlin.autostore.activity.LoginActivity.REQUEST_CODE;

public class MainActivity extends BaseActivity {

    private ActionBarDrawerToggle mDrawerToggle;
    private static final String TAG = "wr";
    private DrawerLayout mDrawerLayout;
    private TextView     toolbar_title;
    private Toolbar      toolbar;
    private List<Fragment> fragments = new ArrayList<>();
    private TextPaint paint;
    private Dialog    mGateOpenDialog;
    private Dialog    mWelcomeDialog;
    private Button    mBtnScan;


    @Override
    public int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mDrawerLayout = ((DrawerLayout) findViewById(R.id.activity_main));
        toolbar_title = ((TextView) findViewById(R.id.toolbar_title));
        findViewById(R.id.btn_lemaibao).setOnClickListener(this);
        findViewById(R.id.btn_open_le_mai_bao).setOnClickListener(this);
        mBtnScan = (Button) findViewById(R.id.btn_scan_bg);
        mBtnScan.setOnClickListener(this);
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra(Constant.MainActivityArgument.MAIN_ACTIVITY);
        String key = intent.getStringExtra("key");
//        if (StrUtils.isEmpty(stringExtra)) {
//            return;
//        }
        if (TextUtils.equals(stringExtra, Constant.MainActivityArgument.LOGIN)) {
            showWelcomeDialog();
            return;
        }
        if (TextUtils.equals(key, "value")) {
            showWelcomeDialog();
            return;
        }
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
                CommonUtils.toNextActivity(v.getContext(), MainActivity.class);
                Log.d(TAG, "onClick: ");
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
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
                //退出
                break;
            case R.id.btn_lemaibao:
                Log.d(TAG, "onClick: ");
                CommonUtils.toNextActivity(this, MyLeiMaiBaoActivity.class);
                break;
            case R.id.btn_open_le_mai_bao:
                //开通乐买宝
                CommonUtils.toNextActivity(this, OpenLeMaiBao.class);
                break;
            case R.id.btn_scan_bg:
                startActivityForResult(new Intent(this, CaptureActivity.class), REQUEST_CODE);
                break;
        }
        mDrawerLayout.closeDrawer(Gravity.LEFT);
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
            showGateOpenDialog();
            return;
        }
        if (TextUtils.equals(argument, Constant.MainActivityArgument.LOGIN)) {
            showWelcomeDialog();
            return;
        }
    }


    private void showGateOpenDialog() {
        mGateOpenDialog = new Dialog(this, R.style.MyDialogCheckVersion);
        //点击其他地方消失
        mGateOpenDialog.setCanceledOnTouchOutside(true);
        //填充对话框的布局
        View viewGateOpen = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dialog_gateopen, null, false);
        AutoUtils.autoSize(viewGateOpen);
        //初始化控件
        //将布局设置给Dialog
        mGateOpenDialog.setContentView(viewGateOpen);
        //获取当前Activity所在的窗体
        Window dialogWindow = mGateOpenDialog.getWindow();
        //设置Dialog从窗体中间弹出
        dialogWindow.setGravity(Gravity.CENTER);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //        lp.y = 20;//设置Dialog距离底部的距离
        //       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        mGateOpenDialog.show();//显示对话框
    }

    private void showWelcomeDialog() {
        mWelcomeDialog = new Dialog(this, R.style.MyDialogCheckVersion);
        //点击其他地方消失
        mWelcomeDialog.setCanceledOnTouchOutside(true);
        //填充对话框的布局
        View viewWelcome = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dialog_welcome, null, false);
        AutoUtils.autoSize(viewWelcome);
        //初始化控件
        //将布局设置给
        mWelcomeDialog.setContentView(viewWelcome);
        //获取当前Activity所在的窗体
        Window dialogWindow = mWelcomeDialog.getWindow();
        //设置Dialog从窗体中间弹出
        dialogWindow.setGravity(Gravity.CENTER);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //        lp.y = 20;//设置Dialog距离底部的距离
        //       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        mWelcomeDialog.show();//显示对话框
    }
}
