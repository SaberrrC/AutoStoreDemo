package com.shanlin.autostore;

import android.content.Context;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;

/**
 * Created by Sxf on 2017/7/25.
 */
public class WXPayTools {
    private static String APP_ID="wxb51b89cba83263de";
    private static IWXAPI api;

    /**
     * 1.//在Manifest中给调用的Activity添加intent-filter
     * @param map
     * @param context
     */
    public static void pay(HashMap<String,String> map, Context context){
        api= WXAPIFactory.createWXAPI(context,APP_ID);
        api.registerApp(APP_ID);
        PayReq payReq=new PayReq();
        payReq.appId=APP_ID;
        payReq.packageValue="Sign=WXPay";//固定值
        payReq.partnerId=map.get("partnerid");//子商户号
        payReq.prepayId=map.get("prepayid");
        payReq.nonceStr=map.get("noncestr");
        payReq.timeStamp=map.get("timestamp");//s
        payReq.sign=map.get("sign");
        api.sendReq(payReq);
    }
}
