package com.shanlin.autostore.activity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.shanlin.android.autostore.ui.act.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.WXPayTools;
import com.shanlin.autostore.WxMessageEvent;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.bean.paramsBean.AliPayOrderBody;
import com.shanlin.autostore.bean.paramsBean.LeMaiBaoPayBody;
import com.shanlin.autostore.bean.paramsBean.WXPayBody;
import com.shanlin.autostore.bean.resultBean.AliPayResultBean;
import com.shanlin.autostore.bean.resultBean.CreditBalanceCheckBean;
import com.shanlin.autostore.bean.resultBean.LeMaiBaoPayResultBean;
import com.shanlin.autostore.bean.resultBean.PayResult;
import com.shanlin.autostore.bean.resultBean.UserVertifyStatusBean;
import com.shanlin.autostore.bean.resultBean.WxChatBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.constants.Constant_LeMaiBao;
import com.shanlin.autostore.constants.WXConstant;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.IpTools;
import com.shanlin.autostore.utils.ProgressDialog;
import com.shanlin.autostore.view.XNumberKeyboardView;
import com.shanlin.autostore.view.gridpasswordview.GridPasswordView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DELL on 2017/7/17 0017.
 */
public class ChoosePayWayActivity extends BaseActivity{

    private View dialogView;
    private TextView moneyNeedToPay;
    private GridPasswordView pswView;
    private ImageView iconChoose;
    private TextView totalAmount;
    private HttpService service;
    private String deviceId;
    private String orderNo;
    private String totalMoney;
    private String storeId;
    private String token;
    private String timestamp;
    private View keyBoard;
    private XNumberKeyboardView xnumber;
    private PopupWindow popTop;
    private PopupWindow popBottom;
    private String creditBalance;
    private TextView availableBalance;
    private String message;
    private View way1;
    private View way2;
    private View way3;
    private Intent intent;
    private TextView moneyNotEnough;
    private String status;
    private String credit;
    private AlertDialog dialog;
    private TextView countDownTime;
    private ProgressDialog progressDialog;

    @Override
    public int initLayout() {
        return R.layout.activity_choose_pay_way;
    }

    @Override
    public void initView() {
        CommonUtils.initToolbar(this,"选择支付方式",R.color.blcak, MainActivity.class);
        availableBalance = ((TextView) findViewById(R.id.get_avaiable_balence));//显示可用余额
        moneyNotEnough = ((TextView) findViewById(R.id.tv_not_enough));//余额不足
        way1 = findViewById(R.id.ll_pay_way_1);
        way1.setOnClickListener(this);
        way2 = findViewById(R.id.ll_pay_way_2);
        way2.setOnClickListener(this);
        way3 = findViewById(R.id.ll_pay_way_3);
        way3.setOnClickListener(this);
        iconChoose = ((ImageView) findViewById(R.id.iv_icon_choose));//圆形logo根据状态切换
        totalAmount = (TextView)findViewById(R.id.tv_totla_amount_to_pay);//应付总额
        countDownTime = (TextView)findViewById(R.id.tv_countdown_time);//应付总额
//        intPayTiem();
        dialogView = LayoutInflater.from(this).inflate(R.layout.input_psw_dialog_layout, null);
        dialogView.findViewById(R.id.iv_close_dialog).setOnClickListener(this);
        moneyNeedToPay = ((TextView) dialogView.findViewById(R.id.money_need_to_pay));
        initPswView();
        keyBoard = LayoutInflater.from(this).inflate(R.layout.keyboard, null);
        initPopwindow();
        initKeyBoard();
        EventBus.getDefault().register(this);
    }

    private void intPayTiem() {
        ValueAnimator animator = ValueAnimator.ofInt(180,0);
        animator.setDuration(180000);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                countDownTime.setText("剩余支付时间: "+value +"s");
                if (value == 0) {
                    finish();
                    CommonUtils.showToast(ChoosePayWayActivity.this,"订单已失效,请重新下单");
                }
            }
        });
    }

    private void initPopwindow() {
        popTop = new PopupWindow(this);
        popTop.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popTop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popTop.setContentView(dialogView);
        popTop.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popTop.setOutsideTouchable(false);
        popTop.setFocusable(false);
        popTop.setAnimationStyle(R.style.popStyle);
        popTop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                CommonUtils.setBackgroundAlpha(ChoosePayWayActivity.this,1f);
                popBottom.dismiss();
                way2.setClickable(true);
                way3.setClickable(true);
            }
        });

        popBottom = new PopupWindow(this);
        popBottom.setAnimationStyle(R.style.popStyle);
        popBottom.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popBottom.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popBottom.setContentView(keyBoard);
        popBottom.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popBottom.setOutsideTouchable(false);
        popBottom.setFocusable(false);
        popBottom.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popTop.dismiss();
            }
        });
    }

    private StringBuilder sb = new StringBuilder();

    private void initKeyBoard() {
        xnumber = ((XNumberKeyboardView) keyBoard.findViewById(R.id.view_keyboard));
        keyBoard.findViewById(R.id.iv_disapper_keyboard).setOnClickListener(this);
        xnumber.setIOnKeyboardListener(new XNumberKeyboardView.IOnKeyboardListener() {
            @Override
            public void onInsertKeyEvent(String text) {
                if (!popTop.isShowing()) return;
                if (sb.length() < 6) {
                    sb.append(text);
                    CommonUtils.debugLog("sb---"+sb.toString());
                    pswView.setPassword(sb.toString());
                }
            }

            @Override
            public void onDeleteKeyEvent() {
                if (!popTop.isShowing()) return;
                if (sb.length() > 0)
                sb = sb.deleteCharAt(sb.length() - 1);
                pswView.setPassword(sb.toString());
            }
        });
    }


    private void initPswView() {
        pswView = ((GridPasswordView) dialogView.findViewById(R.id.pswView));
        pswView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {

            }

            @Override
            public void onInputFinish(String psw) {
                progressDialog.show();
                popBottom.dismiss();
                //调用乐买宝支付接口
                leMaiBaoPay(psw);
            }
        });
    }

    private String[] keys = new String[]{Constant_LeMaiBao.TOTAL_AMOUNT,Constant_LeMaiBao
            .PAY_TYPE,Constant_LeMaiBao.PAY_TIME};

    private void leMaiBaoPay(String psw) {
        Call<LeMaiBaoPayResultBean> call = service.lemaibaoPay(token, new LeMaiBaoPayBody(deviceId, orderNo, psw, totalMoney, "2"));
        call.enqueue(new Callback<LeMaiBaoPayResultBean>() {
            @Override
            public void onResponse(Call<LeMaiBaoPayResultBean> call, Response<LeMaiBaoPayResultBean> response) {
                LeMaiBaoPayResultBean body = response.body();
                if (TextUtils.equals("200",body.getCode())) {
                    LeMaiBaoPayResultBean.DataBean data = body.getData();
                    String payStatus = data.getPayStatus();
                    if ("3".equals(payStatus)) {
                        progressDialog.dismiss();
                        CommonUtils.showToast(ChoosePayWayActivity.this,"支付密码错误,请重新输入!");
                        showInputPswPop();
                    } else if ("1".equals(payStatus)){
                        progressDialog.dismiss();
                        CommonUtils.showToast(ChoosePayWayActivity.this,"支付成功!");
                        CommonUtils.sendDataToNextActivity(ChoosePayWayActivity.this,
                                PayResultActivity.class,keys,new String[]{data.getPayAmount(),
                                        "乐买宝",data.getPaymentTime()});
                        finish();
                    }
                } else {
                    progressDialog.dismiss();
                    CommonUtils.showToast(ChoosePayWayActivity.this,body.getMessage());
                }
            }

            @Override
            public void onFailure(Call<LeMaiBaoPayResultBean> call, Throwable t) {
                    progressDialog.dismiss();
            }
        });
    }

    @Override
    public void initData() {
        intent = getIntent();
        //支付参数
        deviceId = intent.getStringExtra(Constant_LeMaiBao.DEVICEDID);
        orderNo = intent.getStringExtra(Constant_LeMaiBao.ORDER_NO);
        totalMoney = intent.getStringExtra(Constant_LeMaiBao.TOTAL_AMOUNT);
        storeId = intent.getStringExtra(Constant_LeMaiBao.STORED_ID);
        token = intent.getStringExtra(Constant.TOKEN);
        totalAmount.setText("¥"+ (totalMoney == null ? "0.00" : totalMoney));
        moneyNeedToPay.setText("¥"+ (totalMoney == null ? "0.00" : totalMoney));
        service = CommonUtils.doNet();
        progressDialog = new ProgressDialog(this);
    }

    private void doAliPay(String deviceId, String orderNo, String totalMoney, String storeId,
                          String token) {
        Call<AliPayResultBean> call = service.createAliPreOrder(token,new AliPayOrderBody
                (deviceId,
                orderNo, totalMoney, storeId));
        call.enqueue(new Callback<AliPayResultBean>() {
            @Override
            public void onResponse(Call<AliPayResultBean> call, Response<AliPayResultBean> response) {
                AliPayResultBean body = response.body();
                if (TextUtils.equals("200",body.getCode())) {
                    String alipay = body.getData().getAlipay();
                    timestamp = body.getData().getTimestamp();
                    CommonUtils.debugLog(alipay);
                    CommonUtils.debugLog(timestamp);
                    pay(alipay);
                } else {
                    CommonUtils.showToast(ChoosePayWayActivity.this,body.getMessage());
                }
            }

            @Override
            public void onFailure(Call<AliPayResultBean> call, Throwable t) {
                CommonUtils.debugLog(t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ll_pay_way_1:
                if (!Constant_LeMaiBao.AUTHEN_FINISHED.equals(status)) {
                    //开通乐买宝
                    CommonUtils.toNextActivity(this,OpenLeMaiBao.class);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else {
                    //买乐宝支付
                    showInputPswPop();
                }
                break;
            case R.id.ll_pay_way_2:
                //支付宝支付
                doAliPay(deviceId,orderNo,totalMoney,storeId,token);
                break;
            case R.id.ll_pay_way_3:
                //微信支付
                requestWxInfo();
                break;
            case R.id.iv_close_dialog:
                if (popTop.isShowing()) popTop.dismiss();
                break;
            case R.id.btn_diaolog_know:
                dialog.dismiss();
                MainActivity.state = false;
                judgeStatus("2");
                break;
            case R.id.iv_disapper_keyboard:
                if (popBottom.isShowing()) popBottom.dismiss();
                break;
        }
    }

    private void getUserAuthenStatus() {
        Call<UserVertifyStatusBean> call = service.getUserVertifyAuthenStatus(token);
        call.enqueue(new Callback<UserVertifyStatusBean>() {
            @Override
            public void onResponse(Call<UserVertifyStatusBean> call, Response<UserVertifyStatusBean> response) {
                UserVertifyStatusBean body = response.body();
                if (TextUtils.equals("200",body.getCode())) {
                    status = body.getData().getVerifyStatus();
                    if (!Constant_LeMaiBao.AUTHEN_FINISHED.equals(status)) {
                        judgeStatus(status);
                        MainActivity.state = true;
                    } else {
                        //获取用户信用额度
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
                    showBalanceDialog();
                    judgeStatus("2");
                } else {
                    CommonUtils.showToast(ChoosePayWayActivity.this,body.getMessage());
                }
            }

            @Override
            public void onFailure(Call<CreditBalanceCheckBean> call, Throwable t) {

            }
        });
    }

    private void showInputPswPop() {
        sb.delete(0,6);
        pswView.clearPassword();
        popTop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER_HORIZONTAL,0,-300);
        showKeyBoard();
        CommonUtils.setBackgroundAlpha(this,0.5f);
        way2.setClickable(false);
        way3.setClickable(false);
    }

    public void showKeyBoard () {
        popBottom.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,0,0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUserAuthenStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void showBalanceDialog() {
        if (MainActivity.state && credit != null) {
            //可用额度领取
            View view = LayoutInflater.from(ChoosePayWayActivity.this).inflate(R.layout
                    .get_available_balence_layout, null);
            view.findViewById(R.id.btn_diaolog_know).setOnClickListener(ChoosePayWayActivity.this);
            TextView tvCredit = ((TextView) view.findViewById(R.id.tv_credit_num));
            CommonUtils.debugLog("----------choose--------2");
            dialog = new AlertDialog.Builder(ChoosePayWayActivity.this)
                    .setView(view)
                    .create();
            tvCredit.setText(credit+"元可用额度");
            dialog.setCancelable(false);
            dialog.show();
        }
    }


    private void judgeStatus(String status) {

        if (!Constant_LeMaiBao.AUTHEN_FINISHED.equals(status)) {
            availableBalance.setText("领取额度");
            moneyNotEnough.setVisibility(View.INVISIBLE);
            iconChoose.setImageResource(R.mipmap.icon_yellow);
        } else {
            iconChoose.setImageResource(R.mipmap.icon_yellow);
            if (Double.parseDouble(creditBalance == null ? credit : creditBalance) >= Double
                    .parseDouble(totalMoney)){
                availableBalance.setText((creditBalance == null ? credit : creditBalance)+" 元可用");
                moneyNotEnough.setVisibility(View.INVISIBLE);
            } else {
                availableBalance.setText((creditBalance == null ? credit : creditBalance)+" 元");
                iconChoose.setImageResource(R.mipmap.icon_gray);
                moneyNotEnough.setVisibility(View.VISIBLE);
            }
        }
    }


    @Subscribe(threadMode= ThreadMode.MAIN)
    public void getWXPayResult(WxMessageEvent event){
        if (event.getCode().equals("8")) {
            message = event.getMessage();
            CommonUtils.sendDataToNextActivity(this,PayResultActivity.class,new
                    String[]{Constant_LeMaiBao.PAY_TYPE,Constant_LeMaiBao.TOTAL_AMOUNT,
                    Constant_LeMaiBao.PAY_TIME},new String[]{message,totalMoney,CommonUtils
                    .getCurrentTime(true)});
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }

    /**
     * 微信支付请求订单信息接口
     */
    private void requestWxInfo() {
        Call<WxChatBean> call = service.postWxRequest(token, new WXPayBody(deviceId, IpTools
                .getIPAddress(this), orderNo, Integer.parseInt(totalMoney.replace(".", "")) + "", "2"));

        call.enqueue(new Callback<WxChatBean>() {
            @Override
            public void onResponse(Call<WxChatBean> call, Response<WxChatBean> response) {
                WxChatBean body = response.body();
                String s = body.toString();
                CommonUtils.debugLog(s);
                if (TextUtils.equals("200", body.getCode())) {
                    WxChatBean.DataBean data = body.getData();
                    CommonUtils.debugLog("wxdata-----" + data.toString());
                    HashMap<String, String> paramsMap = new HashMap<String, String>();
                        paramsMap.put(WXConstant.APPID, data.getAppid());
                        paramsMap.put(WXConstant.PARTENER_ID, data.getPartnerid());
                        paramsMap.put(WXConstant.PRE_PAY_ID, data.getPrepayid());
                        paramsMap.put(WXConstant.RANDOM_STRING, data.getNoncestr());
                        paramsMap.put(WXConstant.TIME, data.getTimestamp());
                        paramsMap.put(WXConstant.SIGN, data.getSign());
                        WXPayTools.pay(paramsMap, ChoosePayWayActivity.this);
                } else {
                    String message = body.getMessage();
                    CommonUtils.debugLog(message);
                    CommonUtils.showToast(ChoosePayWayActivity.this,body.getMessage());
                }
            }

            @Override
            public void onFailure(Call<WxChatBean> call, Throwable t) {
                CommonUtils.debugLog(t.getMessage());
                Log.e("aa",t.toString());
            }
        });
    }





    /**************************************************************************************
     *                                支付宝业务逻辑                                        *
     *//***********************************************************************************/
    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    CommonUtils.debugLog(resultInfo);
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(ChoosePayWayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        CommonUtils.sendDataToNextActivity(ChoosePayWayActivity.this,
                                PayResultActivity.class,new String[]{Constant_LeMaiBao
                                        .TOTAL_AMOUNT,
                                        Constant_LeMaiBao.PAY_TYPE},new String[]{totalMoney,"支付宝"});
                                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(ChoosePayWayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }

                default:
                    break;
            }
        };
    };

    /**
     * 支付宝支付业务
     */
    public void pay(final String order) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(ChoosePayWayActivity.this);
                Map<String, String> result = alipay.payV2(order, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
