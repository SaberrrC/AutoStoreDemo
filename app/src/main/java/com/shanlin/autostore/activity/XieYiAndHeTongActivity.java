package com.shanlin.autostore.activity;

import android.view.View;
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
        agreementText = (WebView) findViewById(R.id.web_view);
        agreementText.loadUrl("https://m.baidu.com/?from=1000953f");
        // agreementText.loadUrl("file:////android_asset/registrationAgreement.htm");
        //自适应屏幕
        agreementText.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        agreementText.getSettings().setLoadWithOverviewMode(true);

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }
}
