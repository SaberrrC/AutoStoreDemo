package com.shanlin.autostore.wxapi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.shanlin.autostore.WxMessageEvent;
import com.shanlin.autostore.wechat.R;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import org.greenrobot.eventbus.EventBus;

//微信界面
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    public static final String WX_APPID = "wxb51b89cba83263de";
    private IWXAPI api;
    private boolean isFrist = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        //注意：*/
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            api = WXAPIFactory.createWXAPI(this, WX_APPID, false);
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
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
       if (ConstantsAPI.COMMAND_SENDAUTH == resp.getType()) {
            //授权回调
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    //获取请求码
                    SendAuth.Resp sendAuthResp = (SendAuth.Resp) resp;
                    String code = sendAuthResp.code;
                    Log.e("tian", "请求码" + code);
                    WXEntryActivity.this.finish();
                    WxMessageEvent wxMessageEvent = new WxMessageEvent();
                    wxMessageEvent.setCode(code);
                    wxMessageEvent.setMessage("WxCode");
                    EventBus.getDefault().postSticky(wxMessageEvent);
                    finish();
                    //getResult(code);
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    /*Intent intent1 = new Intent();
                    Log.e("tiantian","微信登陆取消设置result");
                    intent1.putExtra("code", "cancel");
                    setResult(2, intent1);*/
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
        finish();
    }






    @Override
    protected void onResume() {
        super.onResume();

    }
}
