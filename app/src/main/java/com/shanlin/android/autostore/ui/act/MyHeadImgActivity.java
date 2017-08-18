package com.shanlin.android.autostore.ui.act;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shanlin.android.autostore.App;
import com.shanlin.android.autostore.common.base.BaseActivity;
import com.shanlin.android.autostore.common.image.ImageLoader;
import com.shanlin.android.autostore.common.net.CountingRequestBody;
import com.shanlin.android.autostore.common.net.callback.UploadHeadImgListener;
import com.shanlin.android.autostore.common.utils.CommonUtils;
import com.shanlin.android.autostore.common.utils.LogUtil;
import com.shanlin.android.autostore.common.utils.SpUtils;
import com.shanlin.android.autostore.common.utils.ToastUtils;
import com.shanlin.android.autostore.entity.body.MemberUpdateSendBean;
import com.shanlin.android.autostore.entity.respone.MemberUpdateBean;
import com.shanlin.android.autostore.presenter.Contract.HeadImgActContract;
import com.shanlin.android.autostore.presenter.HeadImgPresenter;
import com.shanlin.autostore.*;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.utils.CameraUtil;
import com.shanlin.autostore.utils.StatusBarUtils;
import com.theartofdev.edmodo.cropper.CropImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by cuieney on 15/08/2017.
 */

public class MyHeadImgActivity extends BaseActivity<HeadImgPresenter> implements HeadImgActContract.View{

    @BindView(R.id.iv_head_img_large)
    ImageView imgLarge;

    private String token;


    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_my_head_img;
    }

    @Override
    public void initData() {
        CommonUtils.initToolbar(this, "我的头像", R.color.blcak, MainActivity.class);
        StatusBarUtils.setColor(this, Color.WHITE);

        token = SpUtils.getString(this, Constant.TOKEN, "");
        String imgUrl = getIntent().getStringExtra(Constant.HEAD_IMG_URL);

        if (!TextUtils.isEmpty(imgUrl)) {
            ImageLoader.getInstance().displayImage(this, imgUrl, imgLarge);
        } else {
            imgLarge.setImageResource(R.mipmap.imageselector_photo);
        }
    }

    @OnClick(R.id.btn_change_head_img)
    void selectImg() {
        CropImage.activity()
                .start(this);
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

    private void sendImgToServer(Bitmap bitmap) {
        Toast.makeText(this, "正在上传用户头像信息，请稍等", Toast.LENGTH_LONG).show();
        String s = CommonUtils.bitmapToBase64(bitmap);
        MemberUpdateSendBean memberUpdateSendBean = new MemberUpdateSendBean(s, JPushInterface
                .getRegistrationID(this));
        mPresenter.uploadHeadImg(new Gson().toJson(memberUpdateSendBean));
    }


    @Override
    public void onUploadSuccess(String code, MemberUpdateBean data, String msg) {
        CommonUtils.debugLog(msg + "-------------");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onUploadFailed(Throwable ex, String code, String msg) {
        ToastUtils.showToast("上传失败请重试");
    }

    @Override
    public void onProgress(int progress) {
        LogUtil.e(progress);
    }

}
