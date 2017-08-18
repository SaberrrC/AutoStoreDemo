package com.shanlin.android.autostore.ui.act;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.shanlin.android.autostore.common.base.BaseActivity;
import com.shanlin.android.autostore.common.constants.Constant_LeMaiBao;
import com.shanlin.android.autostore.common.constants.WXConstant;
import com.shanlin.android.autostore.common.utils.CommonUtils;
import com.shanlin.android.autostore.common.utils.IpTools;
import com.shanlin.android.autostore.common.utils.PopupUtils;
import com.shanlin.android.autostore.entity.body.AliPayOrderBody;
import com.shanlin.android.autostore.entity.body.LeMaiBaoPayBody;
import com.shanlin.android.autostore.entity.body.WXPayBody;
import com.shanlin.android.autostore.entity.respone.AliPayResultBean;
import com.shanlin.android.autostore.entity.respone.CreditBalanceCheckBean;
import com.shanlin.android.autostore.entity.respone.LeMaiBaoPayResultBean;
import com.shanlin.android.autostore.entity.respone.PayResult;
import com.shanlin.android.autostore.entity.respone.UserVertifyStatusBean;
import com.shanlin.android.autostore.entity.respone.WxChatBean;
import com.shanlin.android.autostore.presenter.Contract.PayWayContract;
import com.shanlin.android.autostore.presenter.PayWayPresenter;
import com.shanlin.android.autostore.ui.view.ProgressDialog;
import com.shanlin.android.autostore.ui.view.XNumberKeyboardView;
import com.shanlin.android.autostore.ui.view.gridpasswordview.GridPasswordView;
import com.shanlin.autostore.R;
import com.shanlin.autostore.WXPayTools;
import com.shanlin.autostore.WxMessageEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by cuieney on 17/08/2017.
 *
 */

public class ChoosePayWayActivity extends BaseActivity<PayWayPresenter> implements PayWayContract.View {


    @BindView(R.id.tv_countdown_time)
    TextView countDownTime;
    @BindView(R.id.tv_totla_amount_to_pay)
    TextView totalAmount;
    @BindView(R.id.iv_icon_choose)
    ImageView iconChoose;
    @BindView(R.id.get_avaiable_balence)
    TextView availableBalance;
    @BindView(R.id.tv_not_enough)
    TextView moneyNotEnough;
    @BindView(R.id.ll_pay_way_1)
    LinearLayout way1;
    @BindView(R.id.ll_pay_way_2)
    LinearLayout way2;
    @BindView(R.id.ll_pay_way_3)
    LinearLayout way3;


    //dialog
    private View dialogView;
    private Dialog dialog;
    private TextView moneyNeedToPay;
    private GridPasswordView pswView;
    private ProgressDialog progressDialog;
    private PopupWindow popTop;
    private View keyBoard;
    private PopupWindow popBottom;
    private XNumberKeyboardView xnumber;

    //data
    private StringBuilder sb = new StringBuilder();
    private Intent intent;
    private String deviceId;
    private String orderNo;
    private String totalMoney;
    private String storeId;
    private String[] keys = new String[]{Constant_LeMaiBao.TOTAL_AMOUNT, Constant_LeMaiBao
            .PAY_TYPE, Constant_LeMaiBao.PAY_TIME};
    private String status;
    private String creditBalance;
    private String credit;
    private String message;
    private String timestamp;

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_choose_pay_way;
    }

    @Override
    public void initData() {
        CommonUtils.initToolbar(this,"选择支付方式",R.color.blcak, MainActivity.class);
        EventBus.getDefault().register(this);
        initDialog();
        initPswView();
        initKeyBoard();
        initPopwindow();

        initParams();
    }

    private void initParams() {
        intent = getIntent();
        //支付参数
        deviceId = intent.getStringExtra(Constant_LeMaiBao.DEVICEDID);
        orderNo = intent.getStringExtra(Constant_LeMaiBao.ORDER_NO);
        totalMoney = intent.getStringExtra(Constant_LeMaiBao.TOTAL_AMOUNT);
        storeId = intent.getStringExtra(Constant_LeMaiBao.STORED_ID);
        totalAmount.setText("¥" + (totalMoney == null ? "0.00" : totalMoney));
        moneyNeedToPay.setText("¥" + (totalMoney == null ? "0.00" : totalMoney));
        progressDialog = new ProgressDialog(this);
    }

    private void initDialog() {
        dialogView = LayoutInflater.from(this).inflate(R.layout.input_psw_dialog_layout, null);
        dialogView.findViewById(R.id.iv_close_dialog).setOnClickListener(view -> {
            if (popTop.isShowing()) popTop.dismiss();
        });
        moneyNeedToPay = ((TextView) dialogView.findViewById(R.id.money_need_to_pay));
    }

    private Handler handler = new Handler();

    private void initPswView() {
        pswView = ((GridPasswordView) dialogView.findViewById(R.id.pswView));
        pswView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {

            }

            @Override
            public void onInputFinish(String psw) {
                popBottom.dismiss();
                progressDialog.show();
                //调用乐买宝支付接口
                mPresenter.lemaibaoPay(new LeMaiBaoPayBody(deviceId, orderNo, psw, totalMoney, "2"));
            }
        });
    }

    private void initPopwindow() {
        popTop = PopupUtils.getTopPopup(getApplicationContext(), dialogView);
        popTop.setOnDismissListener(() -> {
            CommonUtils.setBackgroundAlpha(ChoosePayWayActivity.this, 1f);
            popBottom.dismiss();
            way2.setClickable(true);
            way3.setClickable(true);
        });

        popBottom = PopupUtils.getBottomPopup(getApplicationContext(), keyBoard);
        popBottom.setOnDismissListener(() -> popTop.dismiss());
    }

    private void initKeyBoard() {
        keyBoard = LayoutInflater.from(this).inflate(R.layout.keyboard, null);
        xnumber = ((XNumberKeyboardView) keyBoard.findViewById(R.id.view_keyboard));
        keyBoard.findViewById(R.id.iv_disapper_keyboard).setOnClickListener(view -> {
            if (popBottom.isShowing()) popBottom.dismiss();
        });
        xnumber.setIOnKeyboardListener(new XNumberKeyboardView.IOnKeyboardListener() {
            @Override
            public void onInsertKeyEvent(String text) {
                if (!popTop.isShowing()) return;
                if (sb.length() < 6) {
                    sb.append(text);
                    CommonUtils.debugLog("sb---" + sb.toString());
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

    @OnClick(R.id.ll_pay_way_1)
    void lmbPayWay() {
        if (!Constant_LeMaiBao.AUTHEN_FINISHED.equals(status)) {
            //开通乐买宝
            CommonUtils.toNextActivity(this, OpenLMBActivity.class);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else {
            //买乐宝支付
            showInputPswPop();
        }
    }

    @OnClick(R.id.ll_pay_way_2)
    void aliPayWay() {
        mPresenter.createAliPreOrder(new AliPayOrderBody
                (deviceId, orderNo, totalMoney, storeId));
    }

    @OnClick(R.id.ll_pay_way_3)
    void wechatPayWay() {
        mPresenter.postWxRequest(new WXPayBody(deviceId, IpTools
                .getIPAddress(this), orderNo, Integer.parseInt(totalMoney.replace(".", "")) + "", "2"));
    }

    @Override
    public void onAliPaySuccess(String code, AliPayResultBean data, String msg) {
            String alipay = data.getData().getAlipay();
            timestamp = data.getData().getTimestamp();
            CommonUtils.debugLog(alipay);
            CommonUtils.debugLog(timestamp);
            pay(alipay);
    }

    @Override
    public void onWechatPaySuccess(String code, WxChatBean bean, String msg) {
            WxChatBean.DataBean data = bean.getData();
            CommonUtils.debugLog("wxdata-----" + data.toString());
            HashMap<String, String> paramsMap = new HashMap<>();
            paramsMap.put(WXConstant.APPID, data.getAppid());
            paramsMap.put(WXConstant.PARTENER_ID, data.getPartnerid());
            paramsMap.put(WXConstant.PRE_PAY_ID, data.getPrepayid());
            paramsMap.put(WXConstant.RANDOM_STRING, data.getNoncestr());
            paramsMap.put(WXConstant.TIME, data.getTimestamp());
            paramsMap.put(WXConstant.SIGN, data.getSign());
            WXPayTools.pay(paramsMap, ChoosePayWayActivity.this);

    }

    @Override
    public void onLMbPaySuccess(String code, LeMaiBaoPayResultBean bean, String msg) {
        LeMaiBaoPayResultBean.DataBean data = bean.getData();
        String payStatus = data.getPayStatus();
        if ("3".equals(payStatus)) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    CommonUtils.showToast(ChoosePayWayActivity.this, "支付密码错误,请重新输入!");
                    showInputPswPop();
                }
            },500);
        } else if ("1".equals(payStatus)) {
            progressDialog.dismiss();
            CommonUtils.showToast(ChoosePayWayActivity.this, "支付成功!");
            CommonUtils.sendDataToNextActivity(ChoosePayWayActivity.this,
                    PayResultActivity.class, keys, new String[]{data.getPayAmount(),
                            "乐买宝", data.getPaymentTime()});
            finish();
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                   progressDialog.dismiss();
                   CommonUtils.showToast(ChoosePayWayActivity.this,msg);
                }
            },500);
        }
    }

    @Override
    public void onUserVertifyAuthenStatusSuccess(String code, UserVertifyStatusBean data, String msg) {
        status = data.getData().getVerifyStatus();
        if (!Constant_LeMaiBao.AUTHEN_FINISHED.equals(status)) {
            judgeStatus(status);
            MainActivity.state = true;
        } else {
            //获取用户信用额度
            mPresenter.getUserCreditBalanceInfo();
        }
    }

    @Override
    public void onUserCreditBalanceInfoSuccess(String code, CreditBalanceCheckBean data, String msg) {
        creditBalance = data.getData().getCreditBalance();//可用额度
        credit = data.getData().getCredit();//信用额度
        showBalanceDialog();
        judgeStatus("2");
    }

    @Override
    public void onFailed(Throwable ex, String code, String msg) {
        progressDialog.dismiss();
        CommonUtils.showToast(this,msg);
    }


    private void showBalanceDialog() {
        if (MainActivity.state && credit != null) {
            //可用额度领取
            View view = LayoutInflater.from(ChoosePayWayActivity.this).inflate(R.layout
                    .get_available_balence_layout, null);
            view.findViewById(R.id.btn_diaolog_know).setOnClickListener(view1 -> {
                dialog.dismiss();
                MainActivity.state = false;
                judgeStatus("2");
            });
            TextView tvCredit = ((TextView) view.findViewById(R.id.tv_credit_num));
            CommonUtils.debugLog("----------choose--------2");
            dialog = new Dialog(ChoosePayWayActivity.this, R.style.MyDialogCheckVersion);
                    dialog.setContentView(view);
            tvCredit.setText(credit + "元可用额度");
            dialog.setCanceledOnTouchOutside(false);
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
                    .parseDouble(totalMoney)) {
                availableBalance.setText((creditBalance == null ? credit : creditBalance) + " 元可用");
                moneyNotEnough.setVisibility(View.INVISIBLE);
            } else {
                availableBalance.setText((creditBalance == null ? credit : creditBalance) + " 元");
                iconChoose.setImageResource(R.mipmap.icon_gray);
                moneyNotEnough.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showInputPswPop() {
        sb.delete(0, 6);
        pswView.clearPassword();
        popTop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER_HORIZONTAL, 0, -300);
        showKeyBoard();
        CommonUtils.setBackgroundAlpha(this, 0.5f);
        way2.setClickable(false);
        way3.setClickable(false);
    }

    public void showKeyBoard() {
        Log.d("show", "showKeyBoard: ");
        popBottom.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.getUserVertifyAuthenStatus();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getWXPayResult(WxMessageEvent event) {
        if (event.getCode().equals("8")) {
            message = event.getMessage();
            CommonUtils.sendDataToNextActivity(this, PayResultActivity.class, new
                    String[]{Constant_LeMaiBao.PAY_TYPE, Constant_LeMaiBao.TOTAL_AMOUNT,
                    Constant_LeMaiBao.PAY_TIME}, new String[]{message, totalMoney, CommonUtils
                    .getCurrentTime(true)});
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /**
     * 支付宝支付业务
     */
    public void pay(final String order) {

        Runnable payRunnable = () -> {
            PayTask alipay = new PayTask(ChoosePayWayActivity.this);
            Map<String, String> result = alipay.payV2(order, true);
            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
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
        }
    };
}
