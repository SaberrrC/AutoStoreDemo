package com.shanlin.autostore;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.alipay.sdk.app.PayTask;
import com.shanlin.autostore.activity.BuyRecordActivity;
import com.shanlin.autostore.activity.GateActivity;
import com.shanlin.autostore.activity.MyLeMaiBaoActivity;
import com.shanlin.autostore.activity.OpenLeMaiBao;
import com.shanlin.autostore.activity.SaveFaceActivity;
import com.shanlin.autostore.activity.VersionInfoActivity;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.LogUtils;
import com.shanlin.autostore.utils.ThreadUtils;
import com.shanlin.autostore.utils.ToastUtils;
import com.slfinance.facesdk.ui.LivenessActivity;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_CODE_REGEST = 101;
    private static final int REQUEST_CODE_SCAN   = 102;
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
    private Dialog    mToFaceDialog;
    private byte[]    mLivenessImgBytes;
    private TextView  mTvIdentify;
    private Dialog    mLoginoutDialog;


    @Override
    public int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mDrawerLayout = ((DrawerLayout) findViewById(R.id.activity_main));
        toolbar_title = ((TextView) findViewById(R.id.toolbar_title));
        mTvIdentify = (TextView) findViewById(R.id.identify_tip);
        mTvIdentify.setOnClickListener(this);
        findViewById(R.id.btn_lemaibao).setOnClickListener(this);
        findViewById(R.id.btn_open_le_mai_bao).setOnClickListener(this);
        mBtnScan = (Button) findViewById(R.id.btn_scan_bg);
        mBtnScan.setOnClickListener(this);
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra(Constant.MainActivityArgument.MAIN_ACTIVITY);
        stringExtra = Constant.MainActivityArgument.UNREGEST_USER;
        if (TextUtils.isEmpty(stringExtra)) {
            return;
        }
        //刷脸登陆成功
        if (TextUtils.equals(stringExtra, Constant.MainActivityArgument.LOGIN)) {
            showWelcomeDialog();
            ThreadUtils.runMainDelayed(new Runnable() {
                @Override
                public void run() {
                    mWelcomeDialog.dismiss();
                }
            }, 3000);
            return;
        }
        //刷脸登陆 完成后是未注册用户
        if (TextUtils.equals(stringExtra, Constant.MainActivityArgument.UNREGEST_USER)) {
            showToFaceDialog();
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
                showLoginoutDialog();
                break;
            case R.id.btn_lemaibao:
                Log.d(TAG, "onClick: ");
                CommonUtils.toNextActivity(this, MyLeMaiBaoActivity.class);
                break;
            case R.id.btn_open_le_mai_bao:
                //开通乐买宝
                CommonUtils.toNextActivity(this, OpenLeMaiBao.class);
                break;
            case R.id.btn_scan_bg://扫一扫
                startActivityForResult(new Intent(this, CaptureActivity.class), REQUEST_CODE_SCAN);
                break;
            case R.id.identify_tip:

                // 必须异步调用
                Thread   payThread   = new Thread(payRunnable);
                payThread.start();


                Intent intent = new Intent(MainActivity.this, LivenessActivity.class);
                startActivityForResult(intent, REQUEST_CODE_REGEST);
                break;
        }
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }


    final String orderInfo = "dsfasdfasdf";   // 订单信息

    Runnable payRunnable = new Runnable() {

        @Override
        public void run() {
            PayTask alipay = new PayTask(MainActivity.this);
            Map<String, String> result = alipay.payV2(orderInfo, true);
//            NotificationCompat.MessagingStyle.Message msg = new Message();
//            msg.what = SDK_PAY_FLAG;
//            msg.obj = result;
//            mHandler.sendMessage(msg);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN) {
            if (data == null) {
                return;
            }
            int width = data.getExtras().getInt("width");
            int height = data.getExtras().getInt("height");
            String result = data.getExtras().getString("result");
            // TODO: 2017-7-17 判断 result 成功进入超市
            startActivity(new Intent(this, GateActivity.class));
        }
        if (requestCode == REQUEST_CODE_REGEST) {//人脸识别成功 拿到图片跳转
            try {
                if (data == null) {
                    return;
                }
                mLivenessImgBytes = data.getByteArrayExtra("image_best");
                if (mLivenessImgBytes == null || mLivenessImgBytes.length == 0) {
                    ToastUtils.showToast("人脸识别失败");
                    return;
                }
                String delta = data.getStringExtra("delta");
                LogUtils.d("delta==" + delta + "  mLivenessImgBytes==" + mLivenessImgBytes);
                Bitmap bitmap = BitmapFactory.decodeByteArray(mLivenessImgBytes, 0, mLivenessImgBytes.length);
                File file = CommonUtils.saveBitmap(bitmap);
                ToastUtils.showToast("人脸识别成功");
                Intent intent = new Intent(this, SaveFaceActivity.class);
                intent.putExtra(Constant.SaveFaceActivity.IMAGE_PATH, file.getAbsolutePath());//图片路径传过去
                intent.putExtra(Constant.MainActivityArgument.MAIN_ACTIVITY, Constant.MainActivityArgument.UNREGEST_USER);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }

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
        if (TextUtils.equals(argument, Constant.MainActivityArgument.REGESTED_USER)) {
            mTvIdentify.setVisibility(View.GONE);
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
        viewWelcome.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWelcomeDialog.dismiss();
            }
        });
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

    /**
     * 完成人脸识别弹窗
     */
    private void showToFaceDialog() {
        mToFaceDialog = new Dialog(this, R.style.MyDialogCheckVersion);
        //点击其他地方消失
        mToFaceDialog.setCanceledOnTouchOutside(false);
        //填充对话框的布局
        View viewToFace = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dialog_toface, null, false);
        //跳过
        viewToFace.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mToFaceDialog.dismiss();
            }
        });
        //去完成
        viewToFace.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //去人脸识别 完成后提交
                Intent intent = new Intent(MainActivity.this, LivenessActivity.class);
                startActivityForResult(intent, REQUEST_CODE_REGEST);
                mToFaceDialog.dismiss();
            }
        });
        AutoUtils.autoSize(viewToFace);
        //初始化控件
        //将布局设置给
        mToFaceDialog.setContentView(viewToFace);
        //获取当前Activity所在的窗体
        Window dialogWindow = mToFaceDialog.getWindow();
        //设置Dialog从窗体中间弹出
        dialogWindow.setGravity(Gravity.CENTER);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //        lp.y = 20;//设置Dialog距离底部的距离
        //       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        mToFaceDialog.show();//显示对话框
    }

    private void showLoginoutDialog() {
        mLoginoutDialog = new Dialog(this, R.style.MyDialogCheckVersion);
        //点击其他地方消失
        mLoginoutDialog.setCanceledOnTouchOutside(false);
        //填充对话框的布局
        View viewLoginout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dialog_loginout, null, false);
        viewLoginout.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginoutDialog.dismiss();
            }
        });
        viewLoginout.findViewById(R.id.tv_loginout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginoutDialog.dismiss();
                //// TODO: 2017-7-18 登出的操作
            }
        });


        AutoUtils.autoSize(viewLoginout);
        //初始化控件
        //将布局设置给
        mLoginoutDialog.setContentView(viewLoginout);
        //获取当前Activity所在的窗体
        Window dialogWindow = mLoginoutDialog.getWindow();
        //设置Dialog从窗体中间弹出
        dialogWindow.setGravity(Gravity.CENTER);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //        lp.y = 20;//设置Dialog距离底部的距离
        //       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        mLoginoutDialog.show();//显示对话框
    }
}
