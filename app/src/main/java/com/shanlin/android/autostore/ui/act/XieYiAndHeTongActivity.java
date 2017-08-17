package com.shanlin.android.autostore.ui.act;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shanlin.android.autostore.common.base.SimpleActivity;
import com.shanlin.android.autostore.common.utils.MPermissionUtils;
import com.shanlin.autostore.R;
import com.shanlin.autostore.activity.OpenLeMaiBao;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.LogUtils;
import com.shanlin.autostore.utils.StatusBarUtils;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.BindView;

/**
 * Created by DELL on 2017/7/19 0019.
 */
public class XieYiAndHeTongActivity extends SimpleActivity {

    @BindView(R.id.web_view)
    WebView agreementText;

    @Override
    public int initLayout() {
        return R.layout.activity_xieyi_and_he_tong;
    }

    @Override
    public void initData() {
        StatusBarUtils.setColor(this, Color.TRANSPARENT);
        initWebView();
        initWebViewData();
    }

    private void initWebViewData() {
        int state = getIntent().getIntExtra("state", 0);
        if (state == 1) {
            CommonUtils.initToolbar(this, "乐买宝用户服务合同", R.color.blcak, null);
            agreementText.loadUrl("file:////android_asset/lemaibao.html");
        } else {
            CommonUtils.initToolbar(this, "善林信用服务协议", R.color.blcak,null);
            agreementText.loadUrl("file:////android_asset/shanlinzhengxin.html");
        }
        agreementText.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtils.d("onPageStarted------------" + System.currentTimeMillis());
            }
        });
    }

    private void initWebView() {
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
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
