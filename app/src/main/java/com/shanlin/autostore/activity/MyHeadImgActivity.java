package com.shanlin.autostore.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.gallery.GlideLoader;
import com.shanlin.autostore.utils.CameraUtil;
import com.shanlin.autostore.utils.CommonUtils;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2017/8/2 0002.
 */

public class MyHeadImgActivity extends BaseActivity {

    private static final int REQUEST_CODE_TAKE_PICTURE = 1001;
    private static final int REQ_CODE_CUT = 1003;
    private PopupWindow popHeadChoose;
    private ImageView imgLarge;
    private ImageConfig imageConfig;
    private ArrayList<String> path = new ArrayList<>();
    private Uri imgUrl;

    @Override
    public int initLayout() {
        return R.layout.activity_my_head_img;
    }

    @Override
    public void initView() {
        CommonUtils.initToolbar(this,"我的头像", R.color.blcak, MainActivity.class);
        ((Button) findViewById(R.id.btn_change_head_img)).setOnClickListener(this);
        imgLarge = (ImageView) findViewById(R.id.iv_head_img_large);
        initPop();
    }

    @Override
    public void initData() {
        initConfig();
    }

    private void initConfig() {
         imageConfig
                =  new ImageConfig.Builder(
                // GlideLoader 可用自己用的缓存库
                new GlideLoader())
                // 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
                .steepToolBarColor(getResources().getColor(R.color.alpha))
                // 标题的背景颜色 （默认黑色）
                .titleBgColor(getResources().getColor(R.color.alpha))
                // 提交按钮字体的颜色  （默认白色）
                .titleSubmitTextColor(getResources().getColor(R.color.white))
                // 标题颜色 （默认白色）
                .titleTextColor(getResources().getColor(R.color.white))
                // 开启多选   （默认为多选）  (单选 为 singleSelect)
                .singleSelect()
                .crop(500,500,500,500)
                // 多选时的最大数量   （默认 9 张）
                .mutiSelectMaxSize(9)
                // 已选择的图片路径
                .pathList(path)
                // 拍照后存放的图片路径（默认 /temp/picture）
                .filePath("/ImageSelector/Pictures")
                // 开启拍照功能 （默认开启）
                .showCamera()
                .build();
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
        popHeadChoose.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                CommonUtils.setBackgroundAlpha(MyHeadImgActivity.this,1f);
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_change_head_img:
                showPopWindow();
                break;
            case R.id.tv_head_pop_concel:
                if (popHeadChoose.isShowing()) {
                    popHeadChoose.dismiss();
                }
                break;
            case R.id.tv_head_pop_book:
                ImageSelector.open(MyHeadImgActivity.this, imageConfig); // 开启图片选择器
                break;
            case R.id.tv_head_pop_camera:
                imgUrl = CameraUtil.getTempUri();
                startActivityForResult(CameraUtil.takePicture(imgUrl), REQUEST_CODE_TAKE_PICTURE);
                break;
            default:
                break;
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

    private void disPopWindow () {
        if (popHeadChoose.isShowing()) {
            popHeadChoose.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case REQUEST_CODE_TAKE_PICTURE:
                    if (data != null) {
                        Uri uri = data.getData();
                        startActivityForResult(CameraUtil.cropPhoto(uri, imgUrl, 150, 150), REQ_CODE_CUT);
                    } else {
                        startActivityForResult(CameraUtil.cropPhoto(imgUrl, imgUrl, 150, 150), REQ_CODE_CUT);
                    }
                    break;
                case REQ_CODE_CUT:
                    imgLarge.setImageBitmap(CameraUtil.getBitmapByUri(this,imgUrl));

                    break;
                case ImageSelector.IMAGE_REQUEST_CODE:
                    List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
                    for (String path : pathList) {
                        Log.i("ImagePathList", path);
                        Glide.with(this).load(path).into(imgLarge);
                        disPopWindow();
                    }
                    break;
            }
        }
    }
}
