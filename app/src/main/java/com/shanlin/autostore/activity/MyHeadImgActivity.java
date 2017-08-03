package com.shanlin.autostore.activity;

import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.utils.CommonUtils;

/**
 * Created by DELL on 2017/8/2 0002.
 */

public class MyHeadImgActivity extends BaseActivity {

    private PopupWindow popHeadChoose;

    @Override
    public int initLayout() {
        return R.layout.activity_my_head_img;
    }

    @Override
    public void initView() {
        CommonUtils.initToolbar(this,"我的头像", R.color.blcak, MainActivity.class);
        ((Button) findViewById(R.id.btn_change_head_img)).setOnClickListener(this);
        ImageView imgLarge = (ImageView) findViewById(R.id.iv_head_img_large);
        imgLarge.setOnClickListener(this);
        initPop();
    }

    private void initPop() {
        popHeadChoose = new PopupWindow(this);
        View headChoose = LayoutInflater.from(this).inflate(R.layout.choose_head_image_pop, null);
        headChoose.findViewById(R.id.tv_head_pop_camera).setOnClickListener(this);
        headChoose.findViewById(R.id.tv_head_pop_book).setOnClickListener(this);
        headChoose.findViewById(R.id.tv_head_pop_concel).setOnClickListener(this);
        popHeadChoose.setWidth(355);
        popHeadChoose.setHeight(180);
        popHeadChoose.setContentView(headChoose);
        popHeadChoose.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popHeadChoose.setOutsideTouchable(false);
        popHeadChoose.setFocusable(false);
        popHeadChoose.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                CommonUtils.setBackgroundAlpha(MyHeadImgActivity.this,1f);
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_change_head_img:
                showPopWindow();
                break;
            //拍照
            case R.id.tv_head_pop_camera:

                break;
            //从相册中选
            case R.id.tv_head_pop_book:

                break;
            //拍照
            case R.id.tv_head_pop_concel:
                if (popHeadChoose.isShowing()) {
                    popHeadChoose.dismiss();
                }
                break;
        }

    }

    private void showPopWindow() {
        if (!popHeadChoose.isShowing()) {
            popHeadChoose.showAtLocation(getWindow().getDecorView(), Gravity.CENTER_HORIZONTAL,0,0);
            CommonUtils.setBackgroundAlpha(this,0.5f);
        }
    }
}
