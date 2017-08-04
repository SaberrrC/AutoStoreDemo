package com.shanlin.autostore.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.takephoto.CustomHelper;
import com.shanlin.autostore.takephoto.ResultActivity;
import com.shanlin.autostore.utils.CommonUtils;

/**
 * Created by DELL on 2017/8/2 0002.
 */

public class MyHeadImgActivity extends TakePhotoActivity implements View.OnClickListener {

    private PopupWindow popHeadChoose;
    private CustomHelper customHelper;
    private ImageView imgLarge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        initView();
    }

    public int initLayout() {
        return R.layout.activity_my_head_img;
    }

    public void initView() {
        CommonUtils.initToolbar(this,"我的头像", R.color.blcak, MainActivity.class);
        ((Button) findViewById(R.id.btn_change_head_img)).setOnClickListener(this);
        imgLarge = (ImageView) findViewById(R.id.iv_head_img_large);
        initPop();
    }

    private void initPop() {
        popHeadChoose = new PopupWindow(this);
        View headChoose = LayoutInflater.from(this).inflate(R.layout.choose_head_image_pop, null);
        headChoose.findViewById(R.id.tv_head_pop_camera).setOnClickListener(this);
        headChoose.findViewById(R.id.tv_head_pop_book).setOnClickListener(this);
        headChoose.findViewById(R.id.tv_head_pop_concel).setOnClickListener(this);
        popHeadChoose.setContentView(headChoose);
        popHeadChoose.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popHeadChoose.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popHeadChoose.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popHeadChoose.setOutsideTouchable(false);
        popHeadChoose.setFocusable(false);
        customHelper = CustomHelper.of(headChoose);
        popHeadChoose.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                CommonUtils.setBackgroundAlpha(MyHeadImgActivity.this,1f);
            }
        });
    }


    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
//        Glide.with(this).load(new File(result.getImage().getCompressPath())).into(imgLarge);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        CommonUtils.debugLog(msg);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    private void showImg(TImage image) {
        Intent intent=new Intent(this,ResultActivity.class);
        intent.putExtra("image",image);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_change_head_img) {
            showPopWindow();
        } else if (v.getId() == R.id.tv_head_pop_concel) {
            if (popHeadChoose.isShowing()) {
                popHeadChoose.dismiss();
            }
        } else {
            customHelper.onClick(v,getTakePhoto());
        }
    }

    private void showPopWindow() {
        if (!popHeadChoose.isShowing()) {
            popHeadChoose.showAtLocation(getWindow().getDecorView(), Gravity
                    .CENTER_HORIZONTAL|Gravity.BOTTOM,
                    0,20);
            CommonUtils.setBackgroundAlpha(this,0.5f);
        }
    }
}
