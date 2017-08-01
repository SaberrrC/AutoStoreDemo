package com.shanlin.autostore.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.WXPayTools;
import com.shanlin.autostore.WxMessageEvent;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.bean.paramsBean.AliPayOrderBody;
import com.shanlin.autostore.bean.paramsBean.LeMaiBaoPayBody;
import com.shanlin.autostore.bean.paramsBean.WXPayBody;
import com.shanlin.autostore.bean.resultBean.AliPayResultBean;
import com.shanlin.autostore.bean.resultBean.LeMaiBaoPayResultBean;
import com.shanlin.autostore.bean.resultBean.WxChatBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.constants.Constant_LeMaiBao;
import com.shanlin.autostore.constants.WXConstant;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.IpTools;
import com.shanlin.autostore.view.XNumberKeyboardView;
import com.shanlin.autostore.view.gridpasswordview.GridPasswordView;
import com.shanlin.autostore.zhifubao.OrderInfoUtil2_0;
import com.shanlin.autostore.zhifubao.PayKeys;
import com.shanlin.autostore.zhifubao.PayResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    private TextView availableBalence;
    private View availableDialogView;
    private AlertDialog availbleDialog;
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

    @Override
    public int initLayout() {
        return R.layout.activity_choose_pay_way;
    }

    @Override
    public void initView() {
        CommonUtils.initToolbar(this,"选择支付方式",R.color.blcak, MainActivity.class);
        availableDialogView = LayoutInflater.from(this).inflate(R.layout.get_available_balence_layout, null);
        availableDialogView.findViewById(R.id.btn_diaolog_know).setOnClickListener(this);
        availableBalance = ((TextView) findViewById(R.id.get_avaiable_balence));//显示可用余额
        findViewById(R.id.ll_pay_way_1).setOnClickListener(this);
        findViewById(R.id.ll_pay_way_2).setOnClickListener(this);
        findViewById(R.id.ll_pay_way_3).setOnClickListener(this);
        iconChoose = ((ImageView) findViewById(R.id.iv_icon_choose));//圆形logo根据状态切换
        availableBalence = (TextView)findViewById(R.id.get_avaiable_balence);//可用额度
        availableBalence.setOnClickListener(this);
        totalAmount = (TextView)findViewById(R.id.tv_totla_amount_to_pay);//应付总额
        dialogView = LayoutInflater.from(this).inflate(R.layout.input_psw_dialog_layout, null);
        dialogView.findViewById(R.id.iv_close_dialog).setOnClickListener(this);
        moneyNeedToPay = ((TextView) dialogView.findViewById(R.id.money_need_to_pay));
        initPswView();
        keyBoard = LayoutInflater.from(this).inflate(R.layout.keyboard, null);
        initPopwindow();
        initKeyBoard();
        EventBus.getDefault().register(this);
    }

    private void initPopwindow() {
        popTop = new PopupWindow(this);
        popTop.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popTop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popTop.setContentView(dialogView);
        popTop.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popTop.setOutsideTouchable(false);
        popTop.setFocusable(false);
        popTop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

        popBottom = new PopupWindow(this);
        popBottom.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popBottom.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popBottom.setContentView(keyBoard);
        popBottom.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popBottom.setOutsideTouchable(false);
        popBottom.setFocusable(false);
    }

    private StringBuilder sb = new StringBuilder();

    private void initKeyBoard() {
        xnumber = ((XNumberKeyboardView) keyBoard.findViewById(R.id.view_keyboard));
        keyBoard.findViewById(R.id.iv_disapper_keyboard).setOnClickListener(this);
        xnumber.setIOnKeyboardListener(new XNumberKeyboardView.IOnKeyboardListener() {
            @Override
            public void onInsertKeyEvent(String text) {
                if (sb.length() < 6) {
                    sb.append(text);
                    CommonUtils.debugLog("sb---"+sb.toString());
                    pswView.setPassword(sb.toString());
                }
            }

            @Override
            public void onDeleteKeyEvent() {
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
                popTop.dismiss();
                popBottom.dismiss();
                pswView.clearPassword();
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
                        CommonUtils.showToast(ChoosePayWayActivity.this,"支付密码错误,请重新输入!");
                        showInputPswDialog();
                        showKeyBoard();
                    } else if ("1".equals(payStatus)){
                        CommonUtils.showToast(ChoosePayWayActivity.this,"支付成功!");
                        CommonUtils.sendDataToNextActivity(ChoosePayWayActivity.this,
                                PayResultActivity.class,keys,new String[]{data.getPayAmount(),
                                        "乐买宝",data.getPaymentTime()});
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<LeMaiBaoPayResultBean> call, Throwable t) {

            }
        });
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        //支付参数
        deviceId = intent.getStringExtra(Constant_LeMaiBao.DEVICEDID);
        orderNo = intent.getStringExtra(Constant_LeMaiBao.ORDER_NO);
        totalMoney = intent.getStringExtra(Constant_LeMaiBao.TOTAL_AMOUNT);
        storeId = intent.getStringExtra(Constant_LeMaiBao.STORED_ID);
        token = intent.getStringExtra(Constant.TOKEN);
        creditBalance = intent.getStringExtra(Constant_LeMaiBao.CREDIT_BALANCE);
        totalAmount.setText("¥"+ (totalMoney == null ? "0.00" : totalMoney));
        moneyNeedToPay.setText("¥"+ (totalMoney == null ? "0.00" : totalMoney));
        service = CommonUtils.doNet();
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
                //买乐宝支付
                showInputPswDialog();
                showKeyBoard();
                break;
            case R.id.get_avaiable_balence:
                //可用额度领取
                showGetAvailableBalenceDialog();
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
                availbleDialog.dismiss();
                break;
            case R.id.iv_disapper_keyboard:
                if (popBottom.isShowing()) popBottom.dismiss();
                break;
        }
    }

    private void showGetAvailableBalenceDialog() {
        if (availbleDialog == null) {
            availbleDialog = CommonUtils.getDialog(this, availableDialogView,false);
        } else {
            availbleDialog.show();
        }
    }

    private void showInputPswDialog() {
        popTop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER_HORIZONTAL,0,-300);
        backgroundAlpha(0.5f);
    }

    private void showKeyBoard() {
        popBottom.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,0,0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Double.parseDouble(creditBalance == null ? "0.00" : creditBalance) >= Double.parseDouble
                (totalMoney)) {
            availableBalence.setClickable(creditBalance == null ? true : false);
            availableBalence.setText(creditBalance == null ? "开通乐买宝" : creditBalance+" 元可用");
        } else {
            availableBalence.setTextColor(Color.RED);
            availableBalence.setText(creditBalance+" 额度不足");
        }
    }

    @Subscribe(sticky = true)
    public void getWXPayResult(WxMessageEvent event){
        if (event.getCode().equals("8")) {
            message = event.getMessage();
            CommonUtils.sendDataToNextActivity(this,PayResultActivity.class,new
                    String[]{Constant_LeMaiBao.PAY_TYPE,Constant_LeMaiBao.TOTAL_AMOUNT,
                    Constant_LeMaiBao.PAY_TIME},new String[]{message,totalMoney,CommonUtils
                    .getCurrentTime(true)});
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
                if (TextUtils.equals("200", body.getCode())) {
                    WxChatBean.DataBean data = body.getData();
                    CommonUtils.debugLog("wxdata-----" + data.toString());
                    HashMap<String, String> paramsMap = new HashMap<String, String>();
                    //相同订单不能重复支付
                    if (!TextUtils.isEmpty(data.getPrepayid())) {
                        paramsMap.put(WXConstant.APPID, data.getAppid());
                        paramsMap.put(WXConstant.PARTENER_ID, data.getPartnerid());
                        paramsMap.put(WXConstant.PRE_PAY_ID, data.getPrepayid());
                        paramsMap.put(WXConstant.RANDOM_STRING, data.getNoncestr());
                        paramsMap.put(WXConstant.TIME, data.getTimestamp());
                        paramsMap.put(WXConstant.SIGN, data.getSign());
                        WXPayTools.pay(paramsMap, ChoosePayWayActivity.this);
                    } else {
                        Toast.makeText(ChoosePayWayActivity.this, "不能重复支付", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String message = response.message();
                    CommonUtils.debugLog(message);
                }
            }

            @Override
            public void onFailure(Call<WxChatBean> call, Throwable t) {
                CommonUtils.debugLog(t.getMessage());
            }
        });
    }
    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        CommonUtils.debugLog("设置透明度");
    }




    /**************************************************************************************
     *                                支付宝业务逻辑                                        *
     *                                                                                    *
     *//***********************************************************************************/

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = PayKeys.APPID;

    /** 支付宝账户登录授权业务：入参pid值 */
    public static final String PID = "";
    /** 支付宝账户登录授权业务：入参target_id值 */
    public static final String TARGET_ID = "";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
    public static final String RSA2_PRIVATE = PayKeys.PRIVATE;
    public static final String RSA_PRIVATE = "";

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

                    Log.d("wr***", "resultInfo="+resultInfo);
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
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;
//        Log.i("wr", "pay: "+orderInfo);
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
}
