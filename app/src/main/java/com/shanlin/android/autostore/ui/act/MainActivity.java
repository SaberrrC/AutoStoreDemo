package com.shanlin.android.autostore.ui.act;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
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
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shanlin.android.autostore.common.base.BaseActivity;
import com.shanlin.android.autostore.common.image.ImageLoader;
import com.shanlin.android.autostore.common.utils.CommonUtils;
import com.shanlin.android.autostore.common.utils.MPermissionUtils;
import com.shanlin.android.autostore.common.utils.SpUtils;
import com.shanlin.android.autostore.common.utils.ToastUtils;
import com.shanlin.android.autostore.entity.body.MemberUpdateSendBean;
import com.shanlin.android.autostore.entity.respone.CreditBalanceCheckBean;
import com.shanlin.android.autostore.entity.respone.LogoutBean;
import com.shanlin.android.autostore.entity.respone.PersonInfoBean;
import com.shanlin.android.autostore.entity.respone.RefundMoneyBean;
import com.shanlin.android.autostore.entity.respone.UserNumEverydayBean;
import com.shanlin.android.autostore.entity.respone.UserVertifyStatusBean;
import com.shanlin.android.autostore.presenter.Contract.MainActContract;
import com.shanlin.android.autostore.presenter.MainPresenter;
import com.shanlin.autostore.R;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.constants.Constant_LeMaiBao;
import com.shanlin.autostore.utils.Base64;
import com.shanlin.autostore.utils.StatusBarUtils;
import com.shanlin.autostore.view.ProgressView;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by cuieney on 15/08/2017.
 */

public class MainActivity extends BaseActivity<MainPresenter> implements MainActContract.View {

    @BindView(R.id.toolbar_title)
    ImageView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.identify_tip)
    TextView mTvIdentify;
    @BindView(R.id.tv_today_user_num)
    TextView tvTodayUserNum;
    @BindView(R.id.user_num)
    TextView mUserNum;
    @BindView(R.id.pv)
    ProgressView pv;
    @BindView(R.id.btn_scan_bg)
    TextView mBtnScan;
    @BindView(R.id.iv_circle)
    ImageView circle;
    @BindView(R.id.btn_lemaibao)
    TextView btnLemaibao;
    @BindView(R.id.btn_open_le_mai_bao)
    TextView openLMB;
    @BindView(R.id.cvl)
    View cvl;
    @BindView(R.id.btn_yu_e)
    TextView mBtBanlance;
    @BindView(R.id.tv_refund_money)
    TextView mTvRefundMoney;
    @BindView(R.id.iv_head_img)
    ImageView headImage;
    @BindView(R.id.textView)
    TextView mTvPhoneNum;
    @BindView(R.id.location_1)
    LinearLayout location1;
    @BindView(R.id.hl1)
    View hl1;
    @BindView(R.id.location_2)
    LinearLayout location2;
    @BindView(R.id.hl2)
    View hl2;
    @BindView(R.id.location_3)
    LinearLayout location3;
    @BindView(R.id.hl3)
    View hl3;
    @BindView(R.id.hl4)
    View hl4;
    @BindView(R.id.location_4)
    LinearLayout location4;
    @BindView(R.id.hl5)
    View hl5;
    @BindView(R.id.activity_main)
    DrawerLayout mDrawerLayout;

    //sp
    private String token;
    private String mUserPhone;
    private String nickName;
    private String imageUrl;

    //data
    private String creditBalance;
    public static boolean state; //买乐宝状态
    private String creditUsed; //已用信用额度
    private String credit;//信用额度
    private byte[] mLivenessImgBytes;//人脸识别
    private String mUserPhoneHide;
    private Gson gson;
    private RefundMoneyBean refundMoneyBean;
    private int total;
    private int femaleCount;
    private int maleCount;

    private java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
    private String[] creditKeys = new String[]{Constant_LeMaiBao.CREDIT_BALANCE, Constant_LeMaiBao.CREDIT_USED};


    //menu
    private ActionBarDrawerToggle mDrawerToggle;

    //dialog
    private AlertDialog mToFaceDialog;
    private Dialog dialog;
    private Dialog mLoginoutDialog;

    //contract
    private static final int REQUEST_CODE_REGEST = 101;
    private static final int REQUEST_CODE_SCAN = 102;
    private static final int HEAD_IMG_REQUEST_CODE = 108;
    private String status;
    private Dialog mGateOpenDialog;
    private AlertDialog mWelcomeDialog1;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        StatusBarUtils.setColor(this, Color.WHITE);
        setTransAnim(false);

        initIntend();
        initScanAnim();
        initToolBar();
        sendUserDeviceID();

        token = SpUtils.getString(this, Constant.TOKEN, "");
        mUserPhone = SpUtils.getString(this, Constant.USER_PHONE_LOGINED, "");
        nickName = SpUtils.getString(this, Constant.WX_NICKNAME, "");
        imageUrl = SpUtils.getString(this, Constant.USER_HEAD_URL, "");

        if (TextUtils.isEmpty(nickName)) {
            mUserPhoneHide = mUserPhone.substring(0, 3) + "****" + mUserPhone.substring(7);
        } else {
            mUserPhoneHide = nickName;
        }
        mTvPhoneNum.setText(mUserPhoneHide);
        //设置用户的头像
        if (!TextUtils.isEmpty(imageUrl)) {
            setHeadImg(imageUrl);
        }
        gson = new Gson();

        //获取退款金额
        mPresenter.getRefundMoney();
    }

    private void initToolBar() {
        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(v -> CommonUtils.toNextActivity(v.getContext(), MainActivity.class));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(mContext, mDrawerLayout, toolbar, R.string.open, R.string.close) {
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
    }

    private void initIntend() {
        Intent intent = getIntent();
        String faceVerify = intent.getStringExtra(Constant.FACE_VERIFY);
        if (TextUtils.isEmpty(faceVerify)) {
            return;
        }
        if (TextUtils.equals(faceVerify, Constant.FACE_VERIFY_OK)) {
            mTvIdentify.setVisibility(View.GONE);
            return;
        }
        if (TextUtils.equals(faceVerify, Constant.FACE_VERIFY_NO)) {
            showToFaceDialog();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDrawerLayout.closeDrawer(Gravity.LEFT,false);
    }

    @Override
    public void onLogoutSuccess(String code, LogoutBean data, String msg) {
        CommonUtils.toNextActivity(MainActivity.this, LoginActivity.class);
        finish();
    }

    @Override
    public void onLogoutFailed(Throwable ex, String code, String msg) {
        CommonUtils.toNextActivity(MainActivity.this, LoginActivity.class);
        finish();
    }

    @Override
    public void onGetRefundMoneySuccess(String code, RefundMoneyBean data, String msg) {
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
    public void onGetRefundMoneyFailed(Throwable ex, String code, String msg) {
        mTvRefundMoney.setText("¥0.00");
    }

    @Override
    public void onGetUserNumEverydaySuccess(String code, UserNumEverydayBean data, String msg) {
        total = data.getData().getMemberCount();
        //女性
        femaleCount = data.getData().getFemaleCount();
        maleCount = data.getData().getMaleCount();
        CommonUtils.debugLog("总人数---" + total);
        mUserNum.setText(total + "");
        int percent = total == 0 ? 0 : femaleCount * 100 / total;
        pv.setGirlPercent(percent);
        pv.setBoyPercent(total == 0 ? 0 : 100 - percent);
        pv.flush();
    }

    @Override
    public void onGetUserNumEverydayFailed(Throwable ex, String code, String msg) {
        CommonUtils.debugLog(msg);
    }

    @Override
    public void onGetUserCreditBalanceInfoSuccess(String code, CreditBalanceCheckBean data, String msg) {
        creditBalance = data.getData().getCreditBalance();//可用额度
        credit = data.getData().getCredit();//信用额度
        creditUsed = data.getData().getCreditUsed();//已用额度
        openLMB.setText("¥" + (creditBalance == null ? "0.00" : creditBalance));
        showBalanceDialog();
    }

    @Override
    public void onGetUserCreditBalanceInfoFailed(Throwable ex, String code, String msg) {
        CommonUtils.debugLog(msg);
    }

    @Override
    public void onGetPersonInfoSuccess(String code, PersonInfoBean data, String msg) {
        setHeadImg(data.getData().getDate().getAvetorUrl());//头像连接
    }

    @Override
    public void onGetPersonInfoFailed(Throwable ex, String code, String msg) {
        CommonUtils.debugLog(msg);
    }

    @Override
    public void onGetUserVertifyAuthenStatusSuccess(String code, UserVertifyStatusBean data, String msg) {
        status = data.getData().getVerifyStatus();
        if (!Constant_LeMaiBao.AUTHEN_FINISHED.equals(status)) {
            openLMB.setClickable(true);
            CommonUtils.debugLog("----------top---------");
            openLMB.setText("开通乐买宝");
            state = true;
        } else {
            //获取用户信用额度
            openLMB.setClickable(false);
            mPresenter.getUserCreditBalanceInfo();
        }
    }

    @Override
    public void onGetUserVertifyAuthenStatusFailed(Throwable ex, String code, String msg) {
        CommonUtils.debugLog(msg);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //获取认证状态
        mPresenter.getUserVertifyAuthenStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //调用今日到店人数接口
        mPresenter.getUserNumEveryday(CommonUtils.getCurrentTime(false), "1");
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
                    mPresenter.getPersonInfo();
                }
            }
        }
    }

    private void initScanAnim() {
        final ValueAnimator animator = ValueAnimator.ofFloat(0.5f, 1.0f);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(200);
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            circle.setScaleX(value);
            circle.setScaleY(value);
            circle.setAlpha(1 - value);
            if (value == 1.0f) {
                toScan();
            }
        });

        circle.setOnClickListener(v -> animator.start());
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

    private void sendUserDeviceID() {
        String userDeviceId = SpUtils.getString(this, Constant.DEVICEID, "");
        MemberUpdateSendBean memberUpdateSendBean = new MemberUpdateSendBean(userDeviceId);
        mPresenter.postMemberUpdate(memberUpdateSendBean);
    }


    private void showToFaceDialog() {
        if (mToFaceDialog != null) {
            mToFaceDialog.show();
            return;
        }
        View viewToFace = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dialog_toface, null, false);
        //跳过
        viewToFace.findViewById(R.id.tv_cancle).setOnClickListener(view -> mToFaceDialog.dismiss());
        //去完成
        viewToFace.findViewById(R.id.tv_ok).setOnClickListener(view -> {
            //去人脸识别 完成后提交
            Intent intent = new Intent(MainActivity.this, LivenessActivity.class);
            startActivityForResult(intent, REQUEST_CODE_REGEST);
            mToFaceDialog.dismiss();
        });
        AutoUtils.autoSize(viewToFace);
        mToFaceDialog = CommonUtils.getDialog(this, viewToFace, R.style.MyDialogWithAnim, false);
        mToFaceDialog.setOnDismissListener(dialogInterface -> {
            mTvIdentify.setVisibility(View.VISIBLE);
            startAnim();
        });
        mToFaceDialog.show();
    }

    private void startAnim() {
        if (mTvIdentify.getVisibility() == View.GONE) {
            return;
        }
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.00f, Animation.RELATIVE_TO_SELF, 0.00f, Animation.RELATIVE_TO_SELF, 0.00f, Animation.RELATIVE_TO_SELF, 0.00f);
        animation.setDuration(500);
        animation.setInterpolator(new BounceInterpolator());
        mTvIdentify.setAnimation(animation);
    }

    private void showBalanceDialog() {
        CommonUtils.debugLog("---------flag=" + state);
        if (state && credit != null) {
            View inflate = LayoutInflater.from(this).inflate(R.layout.get_available_balence_layout, null);
            inflate.findViewById(R.id.btn_diaolog_know).setOnClickListener(view -> {
                dialog.dismiss();
                state = false;
            });
            TextView tvCredit = ((TextView) inflate.findViewById(R.id.tv_credit_num));
            dialog = new Dialog(MainActivity.this,R.style.MyDialogCheckVersion);
            dialog.setContentView(inflate);
            dialog.setCanceledOnTouchOutside(false);
            tvCredit.setText(credit + "元可用额度");
            dialog.show();
        }
    }

    private void setHeadImg(String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            //更新上传成功后的用户信息
            this.imageUrl = imageUrl;
            SpUtils.saveString(this, Constant.USER_HEAD_URL, imageUrl);
            ImageLoader.getInstance().displayHeadImage(this, imageUrl, headImage);
        }
    }

    private void showLoginoutDialog() {
        if (mLoginoutDialog != null) {
            mLoginoutDialog.show();
            return;
        }
        View viewLoginout = LayoutInflater.from(this).inflate(R.layout.layout_dialog_loginout, null);
        mLoginoutDialog = new Dialog(this, R.style.MyDialogWithAnim);
        mLoginoutDialog.setContentView(viewLoginout);
        mLoginoutDialog.setCanceledOnTouchOutside(true);
        mLoginoutDialog.show();
        TextView tvNum = (TextView) viewLoginout.findViewById(R.id.tv_num);
        tvNum.setText(mUserPhoneHide);
        viewLoginout.findViewById(R.id.tv_cancle).setOnClickListener(view -> mLoginoutDialog.dismiss());
        viewLoginout.findViewById(R.id.tv_loginout).setOnClickListener(view -> {
            mLoginoutDialog.dismiss();
            cleanUserInfo();
            mPresenter.loginOut();
        });
    }

    private void cleanUserInfo() {
        SpUtils.saveString(MainActivity.this, Constant.TOKEN, "");
        SpUtils.saveString(MainActivity.this, Constant.USER_PHONE_LOGINED, "");
        SpUtils.saveString(MainActivity.this, Constant.USER_HEAD_URL, "");
        SpUtils.saveString(MainActivity.this, Constant.WX_NICKNAME, "");
    }

    @OnClick(R.id.location_2) void menuOpBuyRecord(){
        Intent intent = new Intent(this, BuyRecordActivity.class);
        Intent mainIntent = getIntent();
        if (mainIntent != null) {
            intent.putExtra(Constant.USER_INFO, mainIntent.getSerializableExtra(Constant.USER_INFO));
        }
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @OnClick(R.id.location_3) void toVersionInfo(){
        CommonUtils.toNextActivity(this, VersionInfoActivity.class);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @OnClick(R.id.location_4) void logout(){
        showLoginoutDialog();
    }

    @OnClick(R.id.btn_lemaibao) void toLemaibao(){
        if (!openLMB.isClickable()) {
            CommonUtils.sendDataToNextActivity(this, MyLMBActivity.class, creditKeys, new String[]{creditBalance, creditUsed});
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }

    @OnClick(R.id.btn_open_le_mai_bao) void openLemaibao(){
        if (openLMB.isClickable()) {
            CommonUtils.debugLog("-----------开通乐买宝");
            CommonUtils.sendDataToNextActivity(this,OpenLMBActivity.class,new String[]{Constant_LeMaiBao.AUTHEN_STATUS},new String[]{status});
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }

    @OnClick(R.id.identify_tip) void identify(){
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
    }

    @OnClick(R.id.btn_yu_e) void refundAmount(){
        Intent refundMoneyIntent = new Intent(this, RefundMoneyActivity.class);
        refundMoneyIntent.putExtra(Constant.REFUND_MONEY_BEAN, refundMoneyBean);
        startActivity(refundMoneyIntent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @OnClick(R.id.iv_head_img) void headImgClick(){
        Intent headintent = new Intent(this, MyHeadImgActivity.class);
        headintent.putExtra(Constant.HEAD_IMG_URL, imageUrl);
        startActivityForResult(headintent, HEAD_IMG_REQUEST_CODE);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
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
            mWelcomeDialog1 = com.shanlin.autostore.utils.CommonUtils.getDialog(this, viewWelcome, R.style.MyDialogWithAnim, true);
        }
        mWelcomeDialog1.show();
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(2, 3000);
    }
}
