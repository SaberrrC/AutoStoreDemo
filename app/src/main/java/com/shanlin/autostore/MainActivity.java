package com.shanlin.autostore;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
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
import com.shanlin.autostore.activity.LoginActivity;
import com.shanlin.autostore.activity.MyLeMaiBaoActivity;
import com.shanlin.autostore.activity.OpenLeMaiBao;
import com.shanlin.autostore.activity.RefundMoneyActivity;
import com.shanlin.autostore.activity.SaveFaceActivity;
import com.shanlin.autostore.activity.VersionInfoActivity;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.bean.CaptureBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.net.NetCallBack;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.MPermissionUtils;
import com.shanlin.autostore.utils.StatusBarUtils;
import com.shanlin.autostore.utils.ThreadUtils;
import com.shanlin.autostore.utils.ToastUtils;
import com.shanlin.autostore.view.NumAnim;
import com.shanlin.autostore.view.ProgressView;
import com.shanlin.autostore.zhifubao.Base64;
import com.slfinance.facesdk.ui.LivenessActivity;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_CODE_REGEST = 101;
    private static final int REQUEST_CODE_SCAN   = 102;
    private ActionBarDrawerToggle mDrawerToggle;
    private static final String TAG = "wr";
    private DrawerLayout mDrawerLayout;
    private Toolbar      toolbar;
    private Dialog       mGateOpenDialog;
    private AlertDialog  mWelcomeDialog;
    private Button       mBtnScan;
    private AlertDialog  mToFaceDialog;
    private byte[]       mLivenessImgBytes;
    private TextView     mTvIdentify;
    private AlertDialog  mLoginoutDialog;
    private TextView     mBtBanlance;
    private AlertDialog  mWelcomeDialog1;
    private long lastTime = 0;
    private ProgressView pv;
    private TextView     mUserNum;

    @Override
    public int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        StatusBarUtils.setColor(this, Color.WHITE);
        mDrawerLayout = ((DrawerLayout) findViewById(R.id.activity_main));
        mTvIdentify = (TextView) findViewById(R.id.identify_tip);
        mBtBanlance = (TextView) findViewById(R.id.btn_yu_e);
        mUserNum = (TextView) findViewById(R.id.user_num);
        pv = (ProgressView) findViewById(R.id.pv);
        mBtBanlance.setOnClickListener(this);
        mTvIdentify.setOnClickListener(this);
        findViewById(R.id.btn_lemaibao).setOnClickListener(this);
        findViewById(R.id.btn_open_le_mai_bao).setOnClickListener(this);
        mBtnScan = (Button) findViewById(R.id.btn_scan_bg);
        mBtnScan.setOnClickListener(this);
        Intent intent = getIntent();
        String faceVerify = intent.getStringExtra(Constant.FACE_VERIFY);
        if (TextUtils.isEmpty(faceVerify)) {
            return;
        }
        //刷脸登陆成功 从登录界面人脸验证跳转
        if (TextUtils.equals(faceVerify, Constant.FACE_VERIFY_OK)) {
            mTvIdentify.setVisibility(View.GONE);
            showWelcomeDialog();
            return;
        }
        //未注册用户 从手机号 验证码页面跳转
        if (TextUtils.equals(faceVerify, Constant.FACE_VERIFY_NO)) {
            mTvIdentify.setVisibility(View.VISIBLE);
            showToFaceDialog();
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        pv.setGirlPercent(60);
        NumAnim.startAnim(mUserNum, 100000000, 2000);
        pv.flush();
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
            case R.id.location_2://侧滑 购买记录
                Intent intent = new Intent(this, BuyRecordActivity.class);
                Intent mainIntent = getIntent();
                if (mainIntent != null) {
                    intent.putExtra(Constant.USER_INFO, mainIntent.getSerializableExtra(Constant.USER_INFO));
                }
                startActivity(intent);
                break;
            case R.id.location_3:
                CommonUtils.toNextActivity(this, VersionInfoActivity.class);
                break;
            case R.id.location_4://退出登陆
                showLoginoutDialog();
                break;
            case R.id.btn_lemaibao:
                CommonUtils.toNextActivity(this, MyLeMaiBaoActivity.class);
                break;
            case R.id.btn_open_le_mai_bao: //开通乐买宝
                CommonUtils.toNextActivity(this, OpenLeMaiBao.class);
                break;
            case R.id.btn_scan_bg://扫一扫
                MPermissionUtils.requestPermissionsResult(this, 1, new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS}, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class), REQUEST_CODE_SCAN);
                    }

                    @Override
                    public void onPermissionDenied() {
                        MPermissionUtils.showTipsDialog(MainActivity.this);
                    }
                });
                break;
            case R.id.identify_tip://完善身份，智能购物
                MPermissionUtils.requestPermissionsResult(this, 1, new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS}, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Intent intent = new Intent(MainActivity.this, LivenessActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_REGEST);
                    }

                    @Override
                    public void onPermissionDenied() {
                        MPermissionUtils.showTipsDialog(MainActivity.this);
                    }
                });

                break;
            case R.id.btn_yu_e://退款金额
                //                startActivity(new Intent(this, BalanceActivity.class));//订单余额
                startActivity(new Intent(this, RefundMoneyActivity.class));
                break;
        }
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN) {//二维码
            if (data == null) {
                return;
            }
            int width = data.getExtras().getInt("width");
            int height = data.getExtras().getInt("height");
            String result = data.getExtras().getString("result");
            // TODO: 2017-7-17 判断 result 成功进入超市
            HttpService service = CommonUtils.doNet();
            Map<String, String> map = new HashMap<>();
            map.put("code", "1");
            map.put("deviceId", "00001");
            map.put("storeId", "00001");
            Call<CaptureBean> call = service.postCapture(map);
            // 创建 网络请求接口 的实例
            // 发送网络请求(异步)
            call.enqueue(NetCallBack.getInstance().getCaptureCallBack());
        }
        if (requestCode == REQUEST_CODE_REGEST) {//人脸识别成功 拿到图片跳转
            try {
                if (data == null) {
                    ToastUtils.showToast("人脸识别失败");
                    return;
                }
                mLivenessImgBytes = data.getByteArrayExtra("image_best");
                if (mLivenessImgBytes == null || mLivenessImgBytes.length == 0) {
                    ToastUtils.showToast("人脸识别失败");
                    return;
                }
                String delta = data.getStringExtra("delta");
                String encode = Base64.encode(mLivenessImgBytes);
                //                Bitmap bitmap = BitmapFactory.decodeByteArray(mLivenessImgBytes, 0, mLivenessImgBytes.length);
                //                File file = CommonUtils.saveBitmap(bitmap);
                Intent intent = new Intent(this, SaveFaceActivity.class);
                intent.putExtra(Constant.SaveFaceActivity.IMAGE_BASE64, encode);//图片base64
                intent.putExtra(Constant.FACE_VERIFY, Constant.FACE_VERIFY_NO);
                intent.putExtra(Constant.USER_INFO, getIntent().getSerializableExtra(Constant.USER_INFO));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String argument = intent.getStringExtra(Constant.FACE_VERIFY);
        if (TextUtils.equals(Constant.MainActivityArgument.GATE, argument)) {
            showGateOpenDialog();
            return;
        }
        if (TextUtils.equals(argument, Constant.FACE_VERIFY_OK)) {
            showWelcomeDialog();
            return;
        }
        if (TextUtils.equals(argument, Constant.FACE_REGESTED_OK)) {//从录入人脸页面跳转
            showWelcomeDialog();
            mTvIdentify.setVisibility(View.GONE);
            return;
        }
    }

    /**
     * 扫完二维码后提示
     */
    private void showGateOpenDialog() {
        mGateOpenDialog = new Dialog(this, R.style.MyDialogCheckVersion);
        //点击其他地方消失
        mGateOpenDialog.setCanceledOnTouchOutside(true);
        //填充对话框的布局
        View viewGateOpen = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dialog_gateopen, null, false);
        viewGateOpen.findViewById(R.id.rl_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGateOpenDialog.dismiss();
            }
        });
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

    /**
     * 刷脸登陆成功 录入人脸成功后跳转至主页面
     */
    private void showWelcomeDialog() {
        View viewWelcome = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dialog_welcome, null, false);
        AutoUtils.autoSize(viewWelcome);
        mWelcomeDialog1 = CommonUtils.getDialog(this, viewWelcome, true);
        ThreadUtils.runMainDelayed(new Runnable() {
            @Override
            public void run() {
                mWelcomeDialog1.dismiss();
            }
        }, 3000);
    }

    /**
     * 提醒用户完成人脸录入的弹窗
     */
    private void showToFaceDialog() {
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
        mToFaceDialog = CommonUtils.getDialog(this, viewToFace, false);
    }

    private void showLoginoutDialog() {
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
                // TODO: 2017-7-18 登出的操作
                CommonUtils.toNextActivity(MainActivity.this, LoginActivity.class);
                finish();
            }
        });
        AutoUtils.autoSize(viewLoginout);
        mLoginoutDialog = CommonUtils.getDialog(this, viewLoginout, false);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastTime > 2000) {
            ToastUtils.showToast("再按一次退出邻家智能GO便利店");
            lastTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }


}
