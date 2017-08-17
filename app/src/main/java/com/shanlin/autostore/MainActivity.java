package com.shanlin.autostore;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
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
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shanlin.android.autostore.common.image.ImageLoader;
import com.shanlin.android.autostore.common.utils.MPermissionUtils;
import com.shanlin.android.autostore.common.utils.SpUtils;
import com.shanlin.android.autostore.common.utils.ToastUtils;
import com.shanlin.android.autostore.common.zxing.activity.CaptureActivity;
import com.shanlin.android.autostore.entity.body.MemberUpdateSendBean;
import com.shanlin.android.autostore.ui.act.BuyRecordActivity;
import com.shanlin.android.autostore.ui.act.LivenessActivity;
import com.shanlin.android.autostore.ui.act.LoginActivity;
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
import com.shanlin.autostore.bean.resultBean.MemberUpdateBean;
import com.shanlin.autostore.bean.resultBean.PersonInfoBean;
import com.shanlin.autostore.bean.resultBean.RefundMoneyBean;
import com.shanlin.autostore.bean.resultBean.UserNumEverydayBean;
import com.shanlin.autostore.bean.resultBean.UserVertifyStatusBean;
import com.shanlin.autostore.bean.resultBean.WxUserInfoBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.constants.Constant_LeMaiBao;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.net.CustomCallBack;
import com.shanlin.autostore.utils.Base64;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.LogUtils;
import com.shanlin.autostore.utils.StatusBarUtils;
import com.shanlin.autostore.view.ProgressView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_CODE_REGEST = 101;
    private static final int REQUEST_CODE_SCAN   = 102;
    private static final int HEAD_IMG_REQUEST_CODE = 108;
    private ActionBarDrawerToggle mDrawerToggle;
    private static final String TAG = "wr";
    private DrawerLayout mDrawerLayout;
    private Toolbar      toolbar;
    private Dialog       mGateOpenDialog;
    private TextView     mBtnScan;
    private AlertDialog  mToFaceDialog;
    private byte[]       mLivenessImgBytes;
    private TextView     mTvIdentify;
    private Dialog  mLoginoutDialog;
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
    private       String         creditUsed;
    private       String         credit;
    private       ImageView      headImage;
    private       Dialog    dialog;
    public static boolean        state;
    private       WxUserInfoBean mWxUserInfoBean;
    private       int            maleCount;
    private String nickName;
    private String imageUrl;

    @Override
    public int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        StatusBarUtils.setColor(this, Color.WHITE);
        setTransAnim(false);
        mDrawerLayout = ((DrawerLayout) findViewById(R.id.activity_main));
        mTvIdentify = (TextView) findViewById(R.id.identify_tip);
        headImage = (ImageView) findViewById(R.id.iv_head_img);//头像
        mTvPhoneNum = (TextView) findViewById(R.id.textView);//用户手机号或者微信昵称
        headImage.setOnClickListener(this);
        mBtBanlance = (TextView) findViewById(R.id.btn_yu_e);
        mUserNum = (TextView) findViewById(R.id.user_num);
        pv = (ProgressView) findViewById(R.id.pv);
        mTvRefundMoney = (TextView) findViewById(R.id.tv_refund_money);//退款金额
        mBtBanlance.setOnClickListener(this);
        mTvIdentify.setOnClickListener(this);
        findViewById(R.id.btn_lemaibao).setOnClickListener(this);
        circle = findViewById(R.id.iv_circle);
        openLMB = (TextView) findViewById(R.id.btn_open_le_mai_bao);
        openLMB.setOnClickListener(this);
        mBtnScan = (TextView) findViewById(R.id.btn_scan_bg);
        token = SpUtils.getString(this, Constant.TOKEN, "");
        mUserPhone = SpUtils.getString(this, Constant.USER_PHONE_LOGINED, "");
        initScanAnim();
        Intent intent = getIntent();
        String faceVerify = intent.getStringExtra(Constant.FACE_VERIFY);
        mLoginBean = (LoginBean) intent.getSerializableExtra(Constant.USER_INFO);
        mWxUserInfoBean = (WxUserInfoBean) intent.getSerializableExtra(Constant.WX_INFO);
        sendUserDeviceID();
        if (TextUtils.isEmpty(faceVerify)) {
            return;
        }
        if (TextUtils.equals(faceVerify, Constant.FACE_VERIFY_OK)) {
            mTvIdentify.setVisibility(View.GONE);
            // howWelcomeDialog();
            return;
        }
        if (TextUtils.equals(faceVerify, Constant.FACE_VERIFY_NO)) {
            //            mTvIdentify.setVisibility(View.VISIBLE);
            showToFaceDialog();
        }
    }

    private void starttAnim() {
        if (mTvIdentify.getVisibility() == View.GONE) {
            return;
        }
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.00f, Animation.RELATIVE_TO_SELF, 0.00f, Animation.RELATIVE_TO_SELF, 0.00f, Animation.RELATIVE_TO_SELF, 0.00f);
        animation.setDuration(500);
        animation.setInterpolator(new BounceInterpolator());
        mTvIdentify.setAnimation(animation);
    }

    private void sendUserDeviceID() {
        String userDeviceId = SpUtils.getString(this, Constant.DEVICEID, "");
        MemberUpdateSendBean memberUpdateSendBean = new MemberUpdateSendBean(userDeviceId);
        Log.d(TAG, "sendUserDeviceID: " + memberUpdateSendBean.toString());
        CommonUtils.doNet().postMemberUpdate(token, memberUpdateSendBean).enqueue(new CustomCallBack<MemberUpdateBean>() {
            @Override
            public void success(String code, MemberUpdateBean data, String msg) {

            }

            @Override
            public void error(Throwable ex, String code, String msg) {

            }
        });


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
    protected void onStart() {
        super.onStart();
        //获取认证状态
        getUserAuthenStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //调用今日到店人数接口
        getUserNumToday();
    }

    private void updateHeadImg() {
        Call<PersonInfoBean> call = service.getPersonInfo(token);
        call.enqueue(new CustomCallBack<PersonInfoBean>() {
            @Override
            public void success(String code, PersonInfoBean data, String msg) {
                if (TextUtils.equals("200",code)) {
                    String avetorUrl = data.getData().getDate().getAvetorUrl();//头像连接
                    setHeadImg(avetorUrl);
                }
            }

            @Override
            public void error(Throwable ex, String code, String msg) {
                CommonUtils.debugLog(msg);
            }
        });
    }

    private void showBalanceDialog() {
        CommonUtils.debugLog("---------flag=" + state);
        if (state && credit != null) {
            View inflate = LayoutInflater.from(this).inflate(R.layout.get_available_balence_layout, null);
            inflate.findViewById(R.id.btn_diaolog_know).setOnClickListener(this);
            TextView tvCredit = ((TextView) inflate.findViewById(R.id.tv_credit_num));
            dialog = new Dialog (MainActivity.this);
            dialog.setContentView(inflate);
            dialog.setCanceledOnTouchOutside(false);
            tvCredit.setText(credit + "元可用额度");
            dialog.show();
        }
    }

    private void getUserAuthenStatus() {
        Call<UserVertifyStatusBean> call = service.getUserVertifyAuthenStatus(token);
        call.enqueue(new Callback<UserVertifyStatusBean>() {
            @Override
            public void onResponse(Call<UserVertifyStatusBean> call, Response<UserVertifyStatusBean> response) {
                UserVertifyStatusBean body = response.body();
                if (TextUtils.equals("200", body.getCode())) {
                    String status = body.getData().getVerifyStatus();
                    if (!Constant_LeMaiBao.AUTHEN_FINISHED.equals(status)) {
                        openLMB.setClickable(true);
                        CommonUtils.debugLog("----------top---------");
                        openLMB.setText("开通乐买宝");
                        state = true;
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
        //设置显示的用户名
        nickName = SpUtils.getString(this, Constant.WX_NICKNAME, "");
        if (TextUtils.isEmpty(nickName)) {
            mUserPhoneHide = mUserPhone.substring(0, 3) + "****" + mUserPhone.substring(7);
        } else {
            mUserPhoneHide = nickName;
        }
        mTvPhoneNum.setText(mUserPhoneHide);
        //设置用户的头像
        imageUrl = SpUtils.getString(this, Constant.USER_HEAD_URL, "");
        LogUtils.d("headimgurl ====================   " + nickName + "     " + imageUrl);
        setHeadImg(imageUrl);
        gson = new Gson();
        service = CommonUtils.doNet();
        //获取退款金额
        getRefundMoney();
    }

    private void setHeadImg(String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            //更新上传成功后的用户信息
            this.imageUrl = imageUrl;
            SpUtils.saveString(this,Constant.USER_HEAD_URL,imageUrl);
//            Glide.with(getApplicationContext()).load(imageUrl).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL) //设置缓存
//                    .into(new BitmapImageViewTarget(headImage) {
//                        @Override
//                        protected void setResource(Bitmap resource) {
//                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
//                            circularBitmapDrawable.setCircular(true);
//                            headImage.setImageResource(0);
//                            headImage.setBackgroundDrawable(circularBitmapDrawable);
//                        }
//                    });
            ImageLoader.getInstance().displayHeadImage(this,imageUrl,headImage);
        }
    }

    private void getUserNumToday() {
        Call<UserNumEverydayBean> call = service.getUserNumEveryday(token, CommonUtils.getCurrentTime(false), "1");
        CommonUtils.debugLog(CommonUtils.getCurrentTime(false));
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
                    int percent = total == 0 ? 0 : femaleCount * 100 / total;
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
                    showBalanceDialog();
                } else {
                    CommonUtils.showToast(MainActivity.this, body.getMessage());
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

    private String[] creditKeys = new String[]{Constant_LeMaiBao.CREDIT_BALANCE, Constant_LeMaiBao.CREDIT_USED};

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
                    CommonUtils.sendDataToNextActivity(this, MyLeMaiBaoActivity.class, creditKeys, new String[]{creditBalance, creditUsed});
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
                break;
            case R.id.btn_open_le_mai_bao: //开通乐买宝
                if (openLMB.isClickable()) {
                    CommonUtils.debugLog("-----------开通乐买宝");
                    CommonUtils.toNextActivity(this, OpenLeMaiBao.class);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
                break;
            case R.id.identify_tip://完善身份，智能购物
                CommonUtils.checkPermission(this, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Intent intent = new Intent(MainActivity.this, LivenessActivity.class);
                        intent.putExtra(Constant.MainActivityArgument.MAIN_ACTIVITY, Constant.MainActivityArgument.GATE);
                        startActivityForResult(intent, REQUEST_CODE_REGEST);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    }

                    @Override
                    public void onPermissionDenied() {
                        MPermissionUtils.showTipsDialog(MainActivity.this);
                    }
                });
                break;
            case R.id.btn_yu_e://退款金额
                Intent refundMoneyIntent = new Intent(this, RefundMoneyActivity.class);
                refundMoneyIntent.putExtra(Constant.REFUND_MONEY_BEAN, refundMoneyBean);
                startActivity(refundMoneyIntent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;

            case R.id.btn_diaolog_know:
                dialog.dismiss();
                state = false;
                break;

            case R.id.iv_head_img:
                //点击头像
                Intent headintent = new Intent(this,MyHeadImgActivity.class);
                headintent.putExtra(Constant.HEAD_IMG_URL,imageUrl);
                startActivityForResult(headintent,HEAD_IMG_REQUEST_CODE);
                break;

        }
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }

    private void toScan() {
        CommonUtils.checkPermission(this, new MPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                intent.putExtra(Constant_LeMaiBao.CREDIT_BALANCE, creditBalance);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
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
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            if (requestCode == HEAD_IMG_REQUEST_CODE) {

                if (TextUtils.isEmpty(nickName)) {
                    CommonUtils.debugLog(nickName + "------------");
                    updateHeadImg();
                }
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
        if (mGateOpenDialog == null) {
            mGateOpenDialog = new Dialog(this, R.style.MyDialogWithAnim);
            //点击其他地方消失
            mGateOpenDialog.setCanceledOnTouchOutside(true);
            //填充对话框的布局
            View viewGateOpen = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dialog_gateopen, null, false);
            viewGateOpen.setOnClickListener(new View.OnClickListener() {
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
        }
        mGateOpenDialog.show();//显示对话框
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(1, 10000);
    }

    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://闸机欢迎dialog
                    mGateOpenDialog.dismiss();
                    break;
                case 2://录入人脸成功后跳转至主页面
                    mWelcomeDialog1.dismiss();
                    break;
            }
        }
    };

    /**
     * 刷脸登陆成功 录入人脸成功后跳转至主页面
     */
    private void showWelcomeDialog() {
        if (mWelcomeDialog1 == null) {
            View viewWelcome = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dialog_welcome, null, false);
            viewWelcome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mWelcomeDialog1.dismiss();
                }
            });
            AutoUtils.autoSize(viewWelcome);
            mWelcomeDialog1 = CommonUtils.getDialog(this, viewWelcome, R.style.MyDialogWithAnim, true);
        }
        mWelcomeDialog1.show();
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(2, 3000);
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
        mToFaceDialog = CommonUtils.getDialog(this, viewToFace, R.style.MyDialogWithAnim, false);
        mToFaceDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                mTvIdentify.setVisibility(View.VISIBLE);
                starttAnim();
            }
        });
        mToFaceDialog.show();
    }

    private void showLoginoutDialog() {
        if (mLoginoutDialog != null) {
            mLoginoutDialog.show();
            return;
        }
        View viewLoginout = LayoutInflater.from(this).inflate(R.layout.layout_dialog_loginout, null);
        mLoginoutDialog =new Dialog(this, R.style.MyDialogStyle);
        mLoginoutDialog.setContentView(viewLoginout);
        mLoginoutDialog.setCanceledOnTouchOutside(false);
        mLoginoutDialog.show();
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
                cleanUserInfo();
                CommonUtils.doNet().getLoginOut(token).enqueue(new CustomCallBack<LoginOutBean>() {
                    @Override
                    public void success(String code, LoginOutBean data, String msg) {
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
    }

    private void cleanUserInfo() {
        SpUtils.saveString(MainActivity.this, Constant.TOKEN, "");
        SpUtils.saveString(MainActivity.this, Constant.USER_PHONE_LOGINED, "");
        SpUtils.saveString(MainActivity.this, Constant.USER_HEAD_URL, "");
        SpUtils.saveString(MainActivity.this, Constant.WX_NICKNAME, "");
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