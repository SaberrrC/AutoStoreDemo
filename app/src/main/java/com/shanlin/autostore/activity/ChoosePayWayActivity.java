package com.shanlin.autostore.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.jungly.gridpasswordview.GridPasswordView;
import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.bean.paramsBean.AliPayOrderBody;
import com.shanlin.autostore.bean.resultBean.AliPayResultBean;
import com.shanlin.autostore.bean.resultBean.WxChatBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.constants.Constant_LeMaiBao;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.net.CustomCallBack;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.env.DeviceInfo;
import com.shanlin.autostore.view.XNumberKeyboardView;
import com.shanlin.autostore.zhifubao.OrderInfoUtil2_0;
import com.shanlin.autostore.zhifubao.PayKeys;
import com.shanlin.autostore.zhifubao.PayResult;

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
    private AlertDialog dialog;
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

    @Override
    public int initLayout() {
        return R.layout.activity_choose_pay_way;
    }

    @Override
    public void initView() {
        CommonUtils.initToolbar(this,"选择支付方式",R.color.blcak, MainActivity.class);
        availableDialogView = LayoutInflater.from(this).inflate(R.layout.get_available_balence_layout, null);
        availableDialogView.findViewById(R.id.btn_diaolog_know).setOnClickListener(this);
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
        initKeyBoard();
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
                if (psw.length() < 6) {
//                    CommonUtils.showToast(ChoosePayWayActivity.this,"请输入六位数密码!");
                } else {
                    CommonUtils.debugLog("yyyyyyy");
                    popTop.dismiss();
                    popBottom.dismiss();
                    CommonUtils.toNextActivity(ChoosePayWayActivity.this,PayResultActivity.class);
                }
            }

            @Override
            public void onInputFinish(String psw) {
                //// TODO: 2017/7/19 0019 联网发送数据

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
//                requestWxInfo();
//                WXPayTools.pay("wx201410272009395522657a690389285100","C380BEC2BFD727A4B6845133519F3AD6",ChoosePayWayActivity.this);
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
        popTop = new PopupWindow(this);
        popTop.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popTop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popTop.setContentView(dialogView);
        popTop.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popTop.setOutsideTouchable(false);
        popTop.setFocusable(false);
        popTop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER_HORIZONTAL,0,-300);
        backgroundAlpha(0.5f);
        popTop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void showKeyBoard() {
        popBottom = new PopupWindow(this);
        popBottom.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popBottom.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popBottom.setContentView(keyBoard);
        popBottom.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popBottom.setOutsideTouchable(false);
        popBottom.setFocusable(false);
        popBottom.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,0,0);
    }

    private void requestWxInfo(){
        HttpService httpService = CommonUtils.doNet();
        HashMap<String,String> map=new HashMap<>();
        map.put("deviceId", DeviceInfo.getDeviceId());
        map.put("ip","123.12.12.123");
        map.put("orderNo","123456");
        map.put("payAmount","17.1");
        map.put("storeId","1");
        httpService.postWxRequest(map).enqueue(new CustomCallBack<WxChatBean>() {
            @Override
            public void success(String code, WxChatBean data, String msg) {
                if(code.equals(200)){
                    if(data!=null){
                        WxChatBean.WxResponseBean wxResponseBean = data.data;
                        String prepay_id = wxResponseBean.prepay_id;
                        String sign = wxResponseBean.sign;
                        //WXPayTools.pay(prepay_id,sign,ChoosePayWayActivity.this);
                    }
                }
            }

            @Override
            public void error(Throwable ex, String code, String msg) {
                Toast.makeText(ChoosePayWayActivity.this,code+msg,Toast.LENGTH_SHORT).show();
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
