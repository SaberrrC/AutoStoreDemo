package com.shanlin.autostore.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.autostore.R;
import com.google.gson.Gson;
import com.shanlin.autostore.WxTokenBean;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
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
//微信 appid wxb6efa4e2eb4098 appsecret:a8cbd8f46d8133cc3e847f18f0eed3b6
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    public static final String WX_APPID = "wxb51b89cba83263de";
    public static final String WX_SECRET = "1e73ced172f384ef6305e2276d2a9b96";
    private IWXAPI api;
    /*private SendAuth.Req req;
    private static final String WEIXIN_SCOPE = "snsapi_userinfo";// 用于请求用户信息的作用域
    private static final String WEIXIN_STATE = "login_state"; // 自定义*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        //注册
        regisWX();
        //判断是否安装客户端]
        boolean isInstalled = api.isWXAppInstalled() && api.isWXAppSupportAPI();
        Log.e("tian", "是否安装" + isInstalled);
        if (isInstalled) {
            //授权登陆
            sendAuth();
        } else {
            Toast.makeText(this,  "请安装微信客户端再进行登陆", Toast.LENGTH_SHORT).show();
        }
        //注意：*/
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            api = WXAPIFactory.createWXAPI(this, WX_APPID, false);
            api.handleIntent(getIntent(), this);
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
        api.sendReq(req);
    }


    private void regisWX() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, WX_APPID, true);
        api.registerApp(WX_APPID);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
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
        if (ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX == resp.getType()) {
            //分享回调 TODO:
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    Log.e("tian", "微信分享成功");
                    break;
                case BaseResp.ErrCode.ERR_SENT_FAILED:
                    Log.e("tian", "微信分享失败" + resp.errStr);
                    break;
            }
            finish();
        } else if (ConstantsAPI.COMMAND_SENDAUTH == resp.getType()) {
            //授权回调
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    //获取请求码
                    SendAuth.Resp sendAuthResp = (SendAuth.Resp) resp;
                    String code = sendAuthResp.code;
                    Log.e("tian", "请求码" + code);
                    //请求accessToken
                   /* Intent intent = new Intent();
                    intent.putExtra("code", code);
                    setResult(2, intent);
                    Log.e("tiantian","微信登陆成功设置result");
                    WXEntryActivity.this.finish();*/
                    SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("code", code);
                    edit.commit();
                    finish();
                    //getResult(code);
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    /*Intent intent1 = new Intent();
                    Log.e("tiantian","微信登陆取消设置result");
                    intent1.putExtra("code", "cancel");
                    setResult(2, intent1);*/
                    SharedPreferences sp1 = getSharedPreferences("config", MODE_PRIVATE);
                    SharedPreferences.Editor edit1 = sp1.edit();
                    edit1.putString("code", "cancel");
                    edit1.commit();
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    SharedPreferences sp2 = getSharedPreferences("config", MODE_PRIVATE);
                    SharedPreferences.Editor edit2 = sp2.edit();
                    edit2.putString("code", "cancel");
                    edit2.commit();
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_UNSUPPORT:
                    SharedPreferences sp3 = getSharedPreferences("config", MODE_PRIVATE);
                    SharedPreferences.Editor edit3 = sp3.edit();
                    edit3.putString("code", "cancel");
                    edit3.commit();
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
                // https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
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
                        getUserinfo(access_token, openid);

                    }


                });
            }
        }.start();
    }

    private void getUserinfo(String access_token, String openid) {
        String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + access_token + "&openid=" + openid;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(path)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                String userInfoJson = response.body().string();
//                Log.e("tian", "用户信息:" + userInfoJson);
//                Gson gson = new Gson();
//                WxUserInfoBean wxUserInfoBean = gson.fromJson(userInfoJson, WxUserInfoBean.class);
//                String nickname = wxUserInfoBean.getNickname();
//                final String openid1 = wxUserInfoBean.getOpenid();
//                String headimgurl = wxUserInfoBean.getHeadimgurl();
//                int sex = wxUserInfoBean.getSex();
//                if (nickname == null) {
//                    nickname = "";/}
//                if (headimgurl == null) {
//                    headimgurl = "";
//                }
//
//                String weixinsex = "";
//                if (sex == 0) {
//                    weixinsex = "0";
//                } else if (sex == 1) {
//                    weixinsex = "1";
//                }
//                final String finalNickname = nickname;
//                final String finalWeixinsex = weixinsex;
//                final String finalHeadimgurl = headimgurl;
//                final Intent intent = new Intent(WXEntryActivity.this, RegisterNextActivity.class);
//                intent.putExtra(IntentKeyUtils.LOGIN_TYPE, "weixin");
//                intent.putExtra(IntentKeyUtils.OPEN_ID, openid1);
//                intent.putExtra(IntentKeyUtils.USER_LOGIN_NICKNAME, finalNickname);
//                intent.putExtra(IntentKeyUtils.USER_SEX, finalWeixinsex);
//                intent.putExtra(IntentKeyUtils.USER_AVATAR, finalHeadimgurl);
//                intent.putExtra(IntentKeyUtils.OPEN_ID, openid1);
//                intent.putExtra(IntentKeyUtils.LOGIN_TYPE, "weixin");
//                HashMap<String, String> receivemap = CommonMethod.getRequestBaseMapWithoutUid();
//                receivemap.put("identityType", "2");
//                receivemap.put("openId", openid1);
//                receivemap.put("channel", "android");
//                receivemap.put("version", AppUtils.getAppName(WXEntryActivity.this));
//                receivemap.put("avatar", finalHeadimgurl);
//                receivemap.put("nickname", finalNickname);
//                receivemap.put("sex", finalWeixinsex);
//                final JSONObject jsonObject = new JSONObject(receivemap);
//                //第三方登陆
//                HashMap<String, String> hashMap = new HashMap<>();
//                hashMap.put("receiveData", jsonObject.toString());
//                HttpService api = RetrofitHelper.createApi(HttpService.class);
//                api.requestThridLogin(hashMap)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new RxSubscriber<ThridLoginBean>() {
//                            @Override
//                            public void onSuccess(ThridLoginBean thridLoginBean) {
//                                Log.e("thrid", "status" + thridLoginBean.getStatus() + "mes" + thridLoginBean.getMessage());
//                                SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
//                                SharedPreferences.Editor edit = sharedPreferences.edit();
//                                edit.putString(IntentKeyUtils.USER_TOKEN, thridLoginBean.getData().getToken());
//                                edit.putString(IntentKeyUtils.USER_ID, thridLoginBean.getData().getUserInfo().getId());
//                                edit.putString(IntentKeyUtils.USER_AVATAR, finalHeadimgurl);
//                                String userdata = thridLoginBean.getData().getUserInfo().getUserdata();
//                                Log.e("tiantian", "微信登陆的userdata" + userdata);
//                                try {
//                                    JSONObject jsonObject1 = new JSONObject(userdata);
//                                    String interest = jsonObject1.getString("interest");
//                                    String birthDay = jsonObject1.getString("birthDay");
//                                    String nickname = jsonObject1.getString("nickname");
//                                    Log.e("tiantian", "微信登陆的兴趣" + interest + "生日" + birthDay);
//                                    if (birthDay == null || interest == null || "".equals(birthDay) || "".equals(interest) || "null".equals(interest) || "null".equals(birthDay)) {
//                                        edit.commit();
//                                        intent.putExtra(IntentKeyUtils.USER_TOKEN, thridLoginBean.getData().getToken());
//                                        intent.putExtra(IntentKeyUtils.USER_ID, thridLoginBean.getData().getUserInfo().getId());
//                                        startActivity(intent);
//                                        finish();
//                                    } else {
//                                        if (interest == null || "".equals(interest) || "null".equals(interest)) {
//                                            edit.putString(IntentKeyUtils.USER_LOGIN_INTEREST_TAG, "");
//                                        } else {
//                                            edit.putString(IntentKeyUtils.USER_LOGIN_INTEREST_TAG, interest);
//                                        }
//                                        if (birthDay == null || "".equals(birthDay) || "null".equals(birthDay)) {
//                                            edit.putString(IntentKeyUtils.USER_LOGIN_BIRTHDAY, "");
//                                        } else {
//                                            edit.putString(IntentKeyUtils.USER_LOGIN_BIRTHDAY, birthDay);
//                                        }
//                                        String phone = (String) thridLoginBean.getData().getUserInfo().getPhone();
//                                        if (phone == null || "".equals(phone) || "null".equals(phone)) {
//                                            edit.putString(IntentKeyUtils.USER_LOGIN_PHONE, "");
//                                        } else {
//                                            edit.putString(IntentKeyUtils.USER_LOGIN_PHONE, phone);
//                                        }
//                                        String password = (String) thridLoginBean.getData().getUserInfo().getPassword();
//
//                                        if (password == null || "".equals(password) || "null".equals(password)) {
//                                            edit.putString(IntentKeyUtils.USER_LOGIN_PSW, "");
//                                        } else {
//                                            edit.putString(IntentKeyUtils.USER_LOGIN_PSW, password);
//                                        }
//                                        if (nickname == null || "".equals(nickname) || "null".equals(nickname)) {
//                                            edit.putString(IntentKeyUtils.USER_LOGIN_NICKNAME, "");
//                                        } else {
//                                            edit.putString(IntentKeyUtils.USER_LOGIN_NICKNAME, nickname);
//                                        }
//                                        edit.commit();
//                                        Intent intent1 = new Intent(WXEntryActivity.this, IndexActivity.class);
//                                        startActivity(intent1);
//                                        finish();
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                super.onError(e);
//                                Log.e("thrid", "第三方登录失败");
//                            }
//                        });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
