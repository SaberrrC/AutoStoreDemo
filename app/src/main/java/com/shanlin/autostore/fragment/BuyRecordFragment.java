package com.shanlin.autostore.fragment;

import android.view.View;
import android.widget.ListView;

import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseFragment;

/**
 * Created by DELL on 2017/7/17 0017.
 */

public class BuyRecordFragment extends BaseFragment {

    private ListView buyRecordLV;

    @Override
    protected void initData() {
//        BuyRecordAdapter<>
//        buyRecordLV.setAdapter();
    }

    @Override
    protected void initView(View rootview) {
        buyRecordLV = ((ListView) rootview.findViewById(R.id.lv_buy_record));
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_buyrecord_layout;
    }

    @Override
    public void onClick(View v) {

    }
}
