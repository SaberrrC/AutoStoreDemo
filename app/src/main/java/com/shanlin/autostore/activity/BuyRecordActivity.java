package com.shanlin.autostore.activity;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.utils.CommonUtils;

/**
 * Created by DELL on 2017/7/17 0017.
 */

public class BuyRecordActivity extends BaseActivity {

    private ListView buyRecordLV;
    private Toolbar toolbar;
    private TextView title;


    @Override
    public int initLayout() {
        return R.layout.activity_buyrecord_layout;
    }

    @Override
    public void initView() {
        buyRecordLV = ((ListView) findViewById(R.id.lv_buy_record));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = ((TextView) findViewById(R.id.toolbar_title));
        title.setText("购买记录");
        title.setTextColor(Color.BLACK);
        toolbar.setNavigationIcon(R.mipmap.nav_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.toNextActivity(v.getContext(), MainActivity.class);
                finish();
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

    }
}
