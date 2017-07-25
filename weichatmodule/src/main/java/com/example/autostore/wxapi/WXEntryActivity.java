package com.example.autostore.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.autostore.R;
import com.example.autostore.WxTokenBean;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.GetMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//微信界面
//微信 appid
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    public static final String WX_APPID = "wxb51b89cba83263de";
    public static final String WX_SECRET = "1e73ced172f384ef6305e2276d2a9b96";
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        //注册
        regisWX();
        //判断是否安装客户端]
        boolean isInstalled = api.isWXAppInstalled() && api.isWXAppSupportAPI();
        if (isInstalled) {
            Log.d("tian", "是否安装: " + isInstalled);
            //授权登陆
            sendAuth();
        } else {
            Toast.makeText(this, "请安装微信客户端再进行登陆", Toast.LENGTH_SHORT).show();
        }
        //注意：*/
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            api = WXAPIFactory.createWXAPI(this, WX_APPID, false);
            Intent intent = getIntent();
            boolean b = api.handleIntent(intent, this);
            Log.d("tian", "handleIntent: " + b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = getIntent();
        String login = intent.getStringExtra("wx_type");
        if (login != null && "1".equals(login)) {
            sendAuth();
        }
    }

    private static final String WEIXIN_SCOPE = "snsapi_userinfo";// 用于请求用户信息的作用域
    private static final String WEIXIN_STATE = "login_state"; // 自定义
    private SendAuth.Req req;

    private void sendAuth() {
        req = new SendAuth.Req();
        req.scope = WEIXIN_SCOPE;
        req.state = WEIXIN_STATE;
        boolean b = api.sendReq(req);
        Log.d("tian", "sendAuth: "+b);
//      WXTextObject text=new WXTextObject();
//        text.text="text";
//        WXMediaMessage msg=new WXMediaMessage();
//        msg.mediaObject=text;
//        msg.description="text";
//        SendMessageToWX.Req r=new SendMessageToWX.Req();
//        r.transaction=String.valueOf(System.currentTimeMillis());
//        r.transaction=new GetMessageFromWX.Req().transaction;
//        r.message=msg;
//        api.sendReq(r);

    }


    private void regisWX() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, WX_APPID, true);
        api.registerApp(WX_APPID);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        Log.e("tian", "onReq");
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                api.handleIntent(getIntent(), this);
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                break;
            default:
                break;
        }
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        Log.e("tian", "resp回调接口执行");
        if (ConstantsAPI.COMMAND_SENDAUTH == resp.getType()) {
            //授权回调
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    //获取请求码
                    SendAuth.Resp sendAuthResp = (SendAuth.Resp) resp;
                    String code = sendAuthResp.code;
                    Log.e("tian", "请求码" + code);
                    //请求accessToken
                    Intent intent = new Intent();
                    intent.putExtra("code", code);
                    setResult(2, intent);
                    Log.e("tiantian", "微信登陆成功设置result");
                    WXEntryActivity.this.finish();
                    finish();
                    getResult(code);
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    Intent intent1 = new Intent();
                    Log.e("tiantian", "微信登陆取消设置result");
                    intent1.putExtra("code", "cancel");
                    setResult(2, intent1);
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_UNSUPPORT:
                    finish();
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void onBackPressed() {
        SharedPreferences sp3 = getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor edit3 = sp3.edit();
        edit3.putString("code", "cancel");
        edit3.commit();
        finish();
    }

    private void getResult(final String code) {
        new Thread() {
            @Override
            public void run() {
                String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                        + WX_APPID
                        + "&secret="
                        + WX_SECRET
                        + "&code="
                        + code
                        + "&grant_type=authorization_code";
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .get()
                        .url(path)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("tian", "请求token值失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.e("tian", "请求token值成功");
                        //请求成功
                        String json = response.body().string();
                        Log.e("tian", "请求的token的字符串:" + json);
                        Gson gson = new Gson();
                        WxTokenBean wxTokenBean = gson.fromJson(json, WxTokenBean.class);
                        String access_token = wxTokenBean.getAccess_token();
                        String openid = wxTokenBean.getOpenid();
                        //TODO:token失效
                        Log.e("tian", "token值" + access_token);
                    }


                });
            }
        }.start();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }
}
