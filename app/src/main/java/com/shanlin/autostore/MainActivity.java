package com.shanlin.autostore;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shanlin.autostore.activity.BuyRecordActivity;
import com.shanlin.autostore.activity.LoginActivity;
import com.shanlin.autostore.activity.MyHeadImgActivity;
import com.shanlin.autostore.activity.MyLeMaiBaoActivity;
import com.shanlin.autostore.activity.OpenLeMaiBao;
import com.shanlin.autostore.activity.RefundMoneyActivity;
import com.shanlin.autostore.activity.SaveFaceActivity;
import com.shanlin.autostore.activity.VersionInfoActivity;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.bean.LoginBean;
import com.shanlin.autostore.bean.resultBean.CreditBalanceCheckBean;
import com.shanlin.autostore.bean.resultBean.LoginOutBean;
import com.shanlin.autostore.bean.resultBean.RefundMoneyBean;
import com.shanlin.autostore.bean.resultBean.UserNumEverydayBean;
import com.shanlin.autostore.bean.resultBean.UserVertifyStatusBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.constants.Constant_LeMaiBao;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.livenesslib.LivenessActivity;
import com.shanlin.autostore.net.CustomCallBack;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.MPermissionUtils;
import com.shanlin.autostore.utils.SpUtils;
import com.shanlin.autostore.utils.StatusBarUtils;
import com.shanlin.autostore.utils.ThreadUtils;
import com.shanlin.autostore.utils.ToastUtils;
import com.shanlin.autostore.view.ProgressView;
import com.shanlin.autostore.zhifubao.Base64;
import com.shanlin.autostore.zxing.activity.CaptureActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_CODE_REGEST = 101;
    private static final int REQUEST_CODE_SCAN   = 102;
    private ActionBarDrawerToggle mDrawerToggle;
    private static final String TAG = "wr";
    private DrawerLayout mDrawerLayout;
    private Toolbar      toolbar;
    private Dialog       mGateOpenDialog;
    private TextView     mBtnScan;
    private AlertDialog  mToFaceDialog;
    private byte[]       mLivenessImgBytes;
    private TextView     mTvIdentify;
    private AlertDialog  mLoginoutDialog;
    private TextView     mBtBanlance;
    private AlertDialog  mWelcomeDialog1;
    private long lastTime = 0;
    private ProgressView pv;
    private TextView     mUserNum;
    private TextView     openLMB;
    private HttpService  service;
    private Gson         gson;
    private LoginBean    mLoginBean;
    private int          femaleCount;
    private String       token;
    private TextView     mTvRefundMoney;
    private TextView     mTvPhoneNum;
    private String       mUserPhone;
    private String       mUserPhoneHide;
    private RefundMoneyBean refundMoneyBean = null;
    private String creditBalance;
    private int    total;
    private View   circle;
    private java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
    private int maleCount;
    private String creditUsed;
    private ImageView headImage;
    private String credit;
    private View inflate;
    private AlertDialog dialog;
    private String status;

    @Override
    public int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        StatusBarUtils.setColor(this, Color.WHITE);
        mDrawerLayout = ((DrawerLayout) findViewById(R.id.activity_main));
        mTvIdentify = (TextView) findViewById(R.id.identify_tip);
        headImage = (ImageView) findViewById(R.id.iv_head_img);//头像
        headImage.setOnClickListener(this);
        mBtBanlance = (TextView) findViewById(R.id.btn_yu_e);
        mUserNum = (TextView) findViewById(R.id.user_num);
        pv = (ProgressView) findViewById(R.id.pv);
        mTvPhoneNum = (TextView) findViewById(R.id.textView);
        mTvRefundMoney = (TextView) findViewById(R.id.tv_refund_money);//退款金额
        mBtBanlance.setOnClickListener(this);
        mTvIdentify.setOnClickListener(this);
        findViewById(R.id.btn_lemaibao).setOnClickListener(this);
        circle = findViewById(R.id.iv_circle);
        openLMB = (TextView) findViewById(R.id.btn_open_le_mai_bao);
        openLMB.setOnClickListener(this);
        mBtnScan = (TextView) findViewById(R.id.btn_scan_bg);
        initScanAnim();
        Intent intent = getIntent();
        String faceVerify = intent.getStringExtra(Constant.FACE_VERIFY);
        mLoginBean = (LoginBean) intent.getSerializableExtra(Constant.USER_INFO);
        if (TextUtils.isEmpty(faceVerify)) {
            return;
        }
        if (TextUtils.equals(faceVerify, Constant.FACE_VERIFY_OK)) {
            mTvIdentify.setVisibility(View.GONE);
//            showWelcomeDialog();
            return;
        }
        if (TextUtils.equals(faceVerify, Constant.FACE_VERIFY_NO)) {
            mTvIdentify.setVisibility(View.VISIBLE);
            showToFaceDialog();
        }
    }

    private void initScanAnim() {
        final ValueAnimator animator = ValueAnimator.ofFloat(0.5f, 1.0f);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                circle.setScaleX(value);
                circle.setScaleY(value);
                circle.setAlpha(1 - value);
                if (value == 1.0f) {
                    toScan();
                }
            }
        });

        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animator.start();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //调用今日到店人数接口
        getUserNumToday();
        //获取认证状态
        getUserAuthenStatus();

    }

    boolean flag;

    private void getUserAuthenStatus() {
        Call<UserVertifyStatusBean> call = service.getUserVertifyAuthenStatus(token);
        call.enqueue(new Callback<UserVertifyStatusBean>() {
            @Override
            public void onResponse(Call<UserVertifyStatusBean> call, Response<UserVertifyStatusBean> response) {
                UserVertifyStatusBean body = response.body();
                if (TextUtils.equals("200",body.getCode())) {
                    status = body.getData().getVerifyStatus();
                    if (!Constant_LeMaiBao.AUTHEN_FINISHED.equals(status)) {
                        openLMB.setClickable(true);
                        CommonUtils.debugLog("----------top---------");
                        openLMB.setText("开通乐买宝");
                        flag = true;
                    } else {
                        //获取用户信用额度
                        openLMB.setClickable(false);
                        getUserCreditBalenceInfo(service);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserVertifyStatusBean> call, Throwable t) {
                    CommonUtils.debugLog(t.getMessage());
            }
        });
    }

    @Override
    public void initData() {
        initToolBar();
        inflate = LayoutInflater.from(this).inflate(R.layout.get_available_balence_layout, null);
        inflate.findViewById(R.id.btn_diaolog_know).setOnClickListener(this);
        dialog = CommonUtils.getDialog(this, inflate, false);
        token = SpUtils.getString(this, Constant.TOKEN, "");
        mUserPhone = SpUtils.getString(this, Constant.USER_PHONE_LOGINED, "");
        mUserPhoneHide = mUserPhone.substring(0, 3) + "****" + mUserPhone.substring(7);
        mTvPhoneNum.setText(mUserPhoneHide);
        gson = new Gson();
        service = CommonUtils.doNet();
        //获取退款金额
        getRefundMoney();
    }

    private void getUserNumToday() {
        Call<UserNumEverydayBean> call = service.getUserNumEveryday(token, CommonUtils
                .getCurrentTime(false), "1");
        CommonUtils.debugLog(CommonUtils
                .getCurrentTime(false));
        call.enqueue(new Callback<UserNumEverydayBean>() {
            @Override
            public void onResponse(Call<UserNumEverydayBean> call, Response<UserNumEverydayBean> response) {
                UserNumEverydayBean body = response.body();
                CommonUtils.debugLog("--------" + body.toString());
                if (TextUtils.equals("200", body.getCode())) {
                    total = body.getData().getMemberCount();
                    //女性
                    femaleCount = body.getData().getFemaleCount();
                    maleCount = body.getData().getMaleCount();
                    CommonUtils.debugLog("总人数---" + total);
                    mUserNum.setText(total + "");
                    int percent = total == 0 ? 0 : femaleCount*100/total;
                    pv.setGirlPercent(percent);
                    pv.setBoyPercent(total == 0 ? 0 : 100 - percent);
                    pv.flush();
                }
            }

            @Override
            public void onFailure(Call<UserNumEverydayBean> call, Throwable t) {
                CommonUtils.debugLog(t.getMessage());
            }
        });
    }

    private void getUserCreditBalenceInfo(HttpService service) {
        Call<CreditBalanceCheckBean> call = service.getUserCreditBalanceInfo(token);
        call.enqueue(new Callback<CreditBalanceCheckBean>() {
            @Override
            public void onResponse(Call<CreditBalanceCheckBean> call, Response<CreditBalanceCheckBean> response) {
                CreditBalanceCheckBean body = response.body();
                if (TextUtils.equals("200", body.getCode())) {
                    CommonUtils.debugLog(body.toString() + "----------" + token);
                    creditBalance = body.getData().getCreditBalance();//可用额度
                    credit = body.getData().getCredit();//信用额度
                    creditUsed = body.getData().getCreditUsed();//已用额度
                    openLMB.setText("¥" + (creditBalance == null ? "0.00" : creditBalance));

                    if (flag && credit != null){
                        dialog.show();
                        flag = false;
                    }
                } else {
                    CommonUtils.showToast(MainActivity.this,body.getMessage());
                }
            }

            @Override
            public void onFailure(Call<CreditBalanceCheckBean> call, Throwable t) {

            }
        });
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.toNextActivity(v.getContext(), MainActivity.class);
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

    private String[] creditKeys = new String[]{Constant_LeMaiBao.CREDIT_BALANCE,Constant_LeMaiBao
            .CREDIT_USED};
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
                if (!openLMB.isClickable()) {
                    CommonUtils.sendDataToNextActivity(this, MyLeMaiBaoActivity.class,creditKeys,
                            new String[]{creditBalance,creditUsed});
                }
                break;
            case R.id.btn_open_le_mai_bao: //开通乐买宝
                if (openLMB.isClickable()) {
                    CommonUtils.debugLog("-----------开通乐买宝");
                    CommonUtils.toNextActivity(this,OpenLeMaiBao.class);
                }
                break;
            case R.id.identify_tip://完善身份，智能购物
                CommonUtils.checkPermission(this, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Intent intent = new Intent(MainActivity.this, LivenessActivity.class);
                        intent.putExtra(Constant.MainActivityArgument.MAIN_ACTIVITY, Constant.MainActivityArgument.GATE);
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
                Intent refundMoneyIntent = new Intent(this, RefundMoneyActivity.class);
                refundMoneyIntent.putExtra(Constant.REFUND_MONEY_BEAN, refundMoneyBean);
                startActivity(refundMoneyIntent);
                break;

            case R.id.btn_diaolog_know:
                dialog.dismiss();
                break;

            case R.id.iv_head_img:
                //点击头像
                CommonUtils.toNextActivity(this, MyHeadImgActivity.class);
                break;

        }
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    private void toScan() {
        CommonUtils.checkPermission(this, new MPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                intent.putExtra(Constant_LeMaiBao.CREDIT_BALANCE,creditBalance);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            }

            @Override
            public void onPermissionDenied() {
                MPermissionUtils.showTipsDialog(MainActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_REGEST) {//人脸识别成功 拿到图片跳转
            if (TextUtils.equals(Constant.ON_BACK_PRESSED, data.getStringExtra(Constant.ON_BACK_PRESSED))) {
                return;
            }
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
        String argument = intent.getStringExtra(Constant.MainActivityArgument.MAIN_ACTIVITY);
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
     * 扫完二维码开闸机
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
        ThreadUtils.runMainDelayed(new Runnable() {
            @Override
            public void run() {
                mGateOpenDialog.dismiss();
            }
        }, 10000);
    }

    /**
     * 刷脸登陆成功 录入人脸成功后跳转至主页面
     */
    private void showWelcomeDialog() {
        if (mWelcomeDialog1 != null) {
            mWelcomeDialog1.show();
            return;
        }
        View viewWelcome = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dialog_welcome, null, false);
        viewWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWelcomeDialog1.dismiss();
            }
        });
        AutoUtils.autoSize(viewWelcome);
        mWelcomeDialog1 = CommonUtils.getDialog(this, viewWelcome, true);
        ThreadUtils.runMainDelayed(new Runnable() {
            @Override
            public void run() {
                mWelcomeDialog1.dismiss();
            }
        }, 3000);
        mWelcomeDialog1.show();
    }

    /**
     * 提醒用户完成人脸录入的弹窗
     */
    private void showToFaceDialog() {
        if (mToFaceDialog != null) {
            mToFaceDialog.show();
            return;
        }
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
        mToFaceDialog.show();
    }

    private void showLoginoutDialog() {
        if (mLoginoutDialog != null) {
            mLoginoutDialog.show();
            return;
        }
        View viewLoginout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dialog_loginout, null, false);
        TextView tvNum = (TextView) viewLoginout.findViewById(R.id.tv_num);
        tvNum.setText(mUserPhoneHide);
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
                CommonUtils.doNet().getLoginOut(token).enqueue(new CustomCallBack<LoginOutBean>() {
                    @Override
                    public void success(String code, LoginOutBean data, String msg) {
                        SpUtils.saveString(MainActivity.this, Constant.TOKEN, "");
                        SpUtils.saveString(MainActivity.this, Constant.USER_PHONE_LOGINED, "");
//                        //清空用户数据
//                        SpUtils.saveBoolean(MainActivity.this,Constant_LeMaiBao.GET_BALENCE,
//                                false);
//                        SpUtils.saveBoolean(MainActivity.this,Constant_LeMaiBao.AUTHEN,false);
//                        SpUtils.saveString(MainActivity.this,Constant_LeMaiBao.CREDIT,"0.00");
//                        SpUtils.saveBoolean(MainActivity.this,Constant_LeMaiBao.PASSWORD,false);

                        CommonUtils.toNextActivity(MainActivity.this, LoginActivity.class);
                        finish();
                    }

                    @Override
                    public void error(Throwable ex, String code, String msg) {
                        CommonUtils.toNextActivity(MainActivity.this, LoginActivity.class);
                        finish();
                    }
                });
            }
        });
        AutoUtils.autoSize(viewLoginout);
        mLoginoutDialog = CommonUtils.getDialog(this, viewLoginout, false);
        mLoginoutDialog.show();
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

    public void getRefundMoney() {
        CommonUtils.doNet().getRefundMoney(token).enqueue(new CustomCallBack<RefundMoneyBean>() {
            @Override
            public void success(String code, RefundMoneyBean data, String msg) {
                MainActivity.this.refundMoneyBean = data;
                List<RefundMoneyBean.DataBean> beanList = data.getData();
                if (beanList == null || beanList.size() == 0) {
                    mTvRefundMoney.setText("¥0.00");
                }
                double sum = 0.00;
                for (RefundMoneyBean.DataBean dataBean : beanList) {
                    String balance = dataBean.getBalance();
                    if (TextUtils.isEmpty(balance)) {
                        continue;
                    }
                    double refundMoney = Double.parseDouble(balance);
                    sum += refundMoney;
                }
                if (sum == 0.00) {
                    mTvRefundMoney.setText("¥0" + df.format(sum));
                } else {
                    mTvRefundMoney.setText("¥" + df.format(sum));
                }
            }

            @Override
            public void error(Throwable ex, String code, String msg) {
                mTvRefundMoney.setText("¥0.00");
            }
        });
    }
}
