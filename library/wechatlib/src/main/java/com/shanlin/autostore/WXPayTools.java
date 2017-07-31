package com.shanlin.autostore;

import android.content.Context;
import android.util.Log;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;

/**
 * Created by Sxf on 2017/7/25.
 */
public class WXPayTools {
    private static String APP_ID="wxb51b89cba83263de";
    private static String APP_SECRET="1e73ced172f384ef6305e2276d2a9b96";
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
//        payReq.packageValue="Sign=WXPay";//固定值
//        payReq.partnerId=map.get("partnerId");//子商户号
//        payReq.prepayId=map.get("prepayId");
//        payReq.nonceStr=map.get("nonceStr");
//        payReq.timeStamp=map.get("timeStamp");//s
//        payReq.sign=map.get("sign");

        //TestData
        payReq.partnerId="1486219452";//子商户号
        payReq.prepayId="wx2017072712525528dfade2ae0240070621";
        payReq.packageValue="Sign=WXPay";//固定值
        payReq.nonceStr="MJ2bIOBeWamtUvMv";
        payReq.timeStamp="1501131179";//s
        payReq.sign="8C0AADD0F6A7EB749FDC0A58516B2D48";
        api.sendReq(payReq);
    }
}
