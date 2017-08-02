package com.shanlin.autostore.activity;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.utils.CommonUtils;

/**
 * Created by DELL on 2017/8/2 0002.
 */

public class MyHeadImgActivity extends BaseActivity {

    private PopupWindow popBottom;

    @Override
    public int initLayout() {
        return R.layout.activity_my_head_img;
    }

    @Override
    public void initView() {
        CommonUtils.initToolbar(this,"我的头像", R.color.blcak, MainActivity.class);
        ((Button) findViewById(R.id.btn_change_head_img)).setOnClickListener(this);
        initPop();
    }

    private void initPop() {
        popBottom = new PopupWindow(this);
        popBottom.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popBottom.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        popBottom.setContentView();
        popBottom.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popBottom.setOutsideTouchable(false);
        popBottom.setFocusable(false);
        popBottom.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                popTop.dismiss();
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        showPopWindow();
    }

    private void showPopWindow() {

    }
}
