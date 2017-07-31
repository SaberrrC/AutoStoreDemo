package com.shanlin.autostore.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by Sxf on 2017/7/25.
 */

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private IWXAPI api;
    private String APP_ID="wxb51b89cba83263de";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this,APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e("aa","code"+resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int errCode = resp.errCode;
            switch (errCode){
                //success
                case 0:
                    Toast.makeText(this,"支付成功",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                //error
                case -1:
                    Toast.makeText(this,"支付失败",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                //用户取消
                case -2:
                    Toast.makeText(this,"支付取消",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    }
}
