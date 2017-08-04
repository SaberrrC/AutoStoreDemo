package com.shanlin.autostore.activity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shanlin.autostore.R;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.LogUtils;
import com.shanlin.autostore.utils.MPermissionUtils;
import com.shanlin.autostore.utils.StatusBarUtils;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by DELL on 2017/7/19 0019.
 */
public class XieYiAndHeTongActivity extends AppCompatActivity {

    private WebView agreementText;
    private Dialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xieyi_and_he_tong);
        StatusBarUtils.setColor(this, Color.TRANSPARENT);
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
        int state = getIntent().getIntExtra("state", 0);
        if (state == 1) {
            CommonUtils.initToolbar(this, "乐买宝用户服务合同", R.color.blcak, OpenLeMaiBao.class);
            agreementText.loadUrl("file:////android_asset/lemaibao.html");
        } else {
            CommonUtils.initToolbar(this, "善林信用服务协议", R.color.blcak, OpenLeMaiBao.class);
            agreementText.loadUrl("file:////android_asset/shanlinzhengxin.html");
        }
        agreementText.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissLoadingDialog();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtils.d("onPageStarted------------" + System.currentTimeMillis());
                showLoadingDialog();
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showLoadingDialog() {
        mLoadingDialog = new Dialog(this, R.style.MyDialogCheckVersion);
        //点击其他地方消失
        mLoadingDialog.setCanceledOnTouchOutside(false);
        //填充对话框的布局
        View viewToFace = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dialog_loading, null, false);
        //跳过
        AutoUtils.autoSize(viewToFace);
        //初始化控件
        //将布局设置给
        mLoadingDialog.setContentView(viewToFace);
        //获取当前Activity所在的窗体
        Window dialogWindow = mLoadingDialog.getWindow();
        //设置Dialog从窗体中间弹出
        dialogWindow.setGravity(Gravity.CENTER);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //        lp.y = 20;//设置Dialog距离底部的距离
        //       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        mLoadingDialog.show();//显示对话框
    }

    private void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
}
