package com.shanlin.autostore.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseFragment;

/**
 * Created by DELL on 2017/7/17 0017.
 */

public class VersionInfoFragment extends BaseFragment {

    private ImageView logo;
    private TextView name;
    private TextView version;

    @Override
    protected void initData() {
    }

    @Override
    protected void initView(View rootview) {
        logo = ((ImageView) rootview.findViewById(R.id.logo));
        name = ((TextView) rootview.findViewById(R.id.name));
        version = ((TextView) rootview.findViewById(R.id.version));
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_version_info_layout;
    }

    @Override
    public void onClick(View v) {

    }
}
