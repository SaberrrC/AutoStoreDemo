package com.shanlin.autostore.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.shanlin.autostore.R;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.MPermissionUtils;
import com.shanlin.autostore.utils.StatusBarUtils;

/**
 * Created by DELL on 2017/7/19 0019.
 */
public class XieYiAndHeTongActivity extends AppCompatActivity {

    private WebView agreementText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xieyi_and_he_tong);
        StatusBarUtils.setColor(this, Color.TRANSPARENT);
        int state = getIntent().getIntExtra("state", 0);
        if (state == 1) {
            CommonUtils.initToolbar(this, "乐买宝用户服务合同", R.color.blcak, OpenLeMaiBao.class);
        } else {
            CommonUtils.initToolbar(this, "善林信用服务协议", R.color.blcak, OpenLeMaiBao.class);
        }
        agreementText = (WebView) findViewById(R.id.web_view);
        agreementText.setHorizontalScrollBarEnabled(false);//水平不显示
        agreementText.setVerticalScrollBarEnabled(false); //垂直不显示
        WebSettings webSettings = agreementText.getSettings();
        webSettings.setJavaScriptEnabled(true);
        agreementText.getSettings().setUseWideViewPort(true);//设置是当前html界面自适应屏幕
        agreementText.getSettings().setSupportZoom(true); //设置支持缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowFileAccess(true);
        agreementText.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        agreementText.getSettings().setDefaultTextEncodingName("utf-8");
        agreementText.setWebChromeClient(new WebChromeClient());
        agreementText.loadUrl("file:////android_asset/lemaibao.html");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
