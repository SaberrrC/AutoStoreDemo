package com.shanlin.autostore.activity;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.utils.CommonUtils;

/**
 * Created by DELL on 2017/7/19 0019.
 */
public class XieYiAndHeTongActivity extends BaseActivity {

    private TextView title;
    private WebView  agreementText;

    @Override
    public int initLayout() {
        return R.layout.activity_xieyi_and_he_tong;
    }

    @Override
    public void initView() {
        int state = getIntent().getIntExtra("state", 0);
        if (state == 1) {
            CommonUtils.initToolbar(this, "乐买宝用户服务合同", R.color.blcak, OpenLeMaiBao.class);
        } else {
            CommonUtils.initToolbar(this, "善林信用服务协议", R.color.blcak, OpenLeMaiBao.class);
        }
        agreementText =  (WebView) findViewById(R.id.web_view);
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
        agreementText.loadUrl("file:////android_asset/ShnLinCreditAerviceAgreement.html");
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }
}
