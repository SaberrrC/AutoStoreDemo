package com.shanlin.autostore.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.shanlin.android.autostore.common.image.ImageLoader;
import com.shanlin.android.autostore.common.utils.SpUtils;
import com.shanlin.android.autostore.common.utils.ToastUtils;
import com.shanlin.android.autostore.entity.body.MemberUpdateSendBean;
import com.shanlin.android.autostore.ui.act.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.bean.resultBean.MemberUpdateBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.net.CustomCallBack;
import com.shanlin.autostore.utils.CameraUtil;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.StatusBarUtils;
import com.theartofdev.edmodo.cropper.CropImage;

import cn.jpush.android.api.JPushInterface;
import retrofit2.Call;

/**
 * Created by DELL on 2017/8/2 0002.
 */

public class MyHeadImgActivity extends BaseActivity {

    private static final int REQUEST_CODE_TAKE_PICTURE = 1001;
    private static final int REQ_CODE_CUT = 1003;
    private PopupWindow popHeadChoose;
    private ImageView imgLarge;
    private Uri imgUrl;
    private HttpService service;
    private String token;

    @Override
    public int initLayout() {
        return R.layout.activity_my_head_img;
    }

    @Override
    public void initView() {
        CommonUtils.initToolbar(this,"我的头像", R.color.blcak, MainActivity.class);
        StatusBarUtils.setColor(this, Color.WHITE);
        ((Button) findViewById(R.id.btn_change_head_img)).setOnClickListener(this);
        imgLarge = (ImageView) findViewById(R.id.iv_head_img_large);
        initPop();
    }

    @Override
    public void initData() {
        service = CommonUtils.doNet();
        token = SpUtils.getString(this, Constant.TOKEN, "");
        String imgUrl = getIntent().getStringExtra(Constant.HEAD_IMG_URL);
        CommonUtils.debugLog(imgUrl+"myhead---------");
        if (!TextUtils.isEmpty(imgUrl)) {
//            Glide.with(this).load(imgUrl).into(imgLarge);
            ImageLoader.getInstance().displayImage(this,imgUrl,imgLarge);
        } else {
            imgLarge.setImageResource(R.mipmap.imageselector_photo);
        }
    }

//    private void initConfig() {
//         imageConfig
//                =  new ImageConfig.Builder(
//                // GlideLoader 可用自己用的缓存库
//                new GlideLoader())
//                // 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
//                .steepToolBarColor(getResources().getColor(R.color.white))
//                // 标题的背景颜色 （默认黑色）
//                .titleBgColor(getResources().getColor(R.color.white))
//                // 提交按钮字体的颜色  （默认白色）
//                .titleSubmitTextColor(getResources().getColor(R.color.blcak))
//                // 标题颜色 （默认白色）
//                .titleTextColor(getResources().getColor(R.color.blcak))
//                // 开启多选   （默认为多选）  (单选 为 singleSelect)
//                .singleSelect()
//                .crop(500,500,500,500)
//                // 多选时的最大数量   （默认 9 张）
//                .mutiSelectMaxSize(1)
//                // 已选择的图片路径
//                .pathList(path)
//                // 拍照后存放的图片路径（默认 /temp/picture）
//                .filePath("/ImageSelector/Pictures")
//                // 开启拍照功能 （默认开启）
//                .showCamera()
//                .build();
//    }

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
//                showPopWindow();
                CropImage.activity()
                        .start(this);
                break;
            case R.id.tv_head_pop_concel:
                if (popHeadChoose.isShowing()) {
                    popHeadChoose.dismiss();
                }
                break;
            case R.id.tv_head_pop_book:
//                ImageSelector.open(MyHeadImgActivity.this, imageConfig); // 开启图片选择器
                break;
            case R.id.tv_head_pop_camera:
                imgUrl = CameraUtil.getTempUri();
                startActivityForResult(CameraUtil.takePicture(imgUrl), REQUEST_CODE_TAKE_PICTURE);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
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

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Bitmap bitmap = CameraUtil.getBitmapByUri(this, resultUri);
                imgLarge.setImageBitmap(bitmap);
                sendImgToServer(bitmap);
                return;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                ToastUtils.showToast("请重试");
                return;
            }
        }

    }

    /**
     * 上传头像到服务器
     */
    private void sendImgToServer(Bitmap bitmap) {
        Toast.makeText(this, "正在上传用户头像信息，请稍等", Toast.LENGTH_LONG).show();
        String s = CommonUtils.bitmapToBase64(bitmap);

        Call<MemberUpdateBean> call = service.postMemberUpdate(token, new MemberUpdateSendBean(s, JPushInterface
                .getRegistrationID(this)));
        call.enqueue(new CustomCallBack<MemberUpdateBean>() {
            @Override
            public void success(String code, MemberUpdateBean data, String msg) {
                CommonUtils.debugLog(msg+"-------------");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void error(Throwable ex, String code, String msg) {
            }
        });
    }
}
