package com.shanlin.autostore.activity;

import android.app.Dialog;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.bean.LoginBean;
import com.shanlin.autostore.bean.MemberUpdateBean;
import com.shanlin.autostore.bean.paramsBean.MemberUpdateSendBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.net.CustomCallBack;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.SpUtils;
import com.shanlin.autostore.utils.ToastUtils;
import com.zhy.autolayout.utils.AutoUtils;

import retrofit2.Call;

public class SaveFaceActivity extends BaseActivity {

    private TextView  mTvSave;
    private Dialog    mSaveFaceDialog;
    private Dialog    mLoadingDialog;
    private LoginBean mLoginBean;

    @Override
    public int initLayout() {
        return R.layout.activity_save_face;
    }

    @Override
    public void initView() {
        CommonUtils.initToolbar(this, "人脸资料录入成功", R.color.black, null);
        mTvSave = (TextView) findViewById(R.id.tv_save);
        mTvSave.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mLoginBean = (LoginBean) getIntent().getSerializableExtra(Constant.USER_INFO);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_save:
                if (!CommonUtils.checkNet()) {
                    return;
                }
                //显示loading页面
                showLoadingDialog();
                doMemberUpdate();
                break;
        }
    }

    private void doMemberUpdate() {
//        String userDeviceId = mLoginBean.getData().getUserDeviceId();
        String userDeviceId = SpUtils.getString(this, Constant.DEVICEID, "");
        String imageBase64 = getIntent().getStringExtra(Constant.SaveFaceActivity.IMAGE_BASE64);
        HttpService httpService = CommonUtils.doNet();
        MemberUpdateSendBean memberUpdateSendBean = new MemberUpdateSendBean(userDeviceId);
        memberUpdateSendBean.imageBase64 = imageBase64;
        Call<MemberUpdateBean> memberUpdateBeanCall = httpService.postMemberUpdate(mLoginBean.getData().getToken(), memberUpdateSendBean);
        mMemberUpdateBeanCustomCallBack.setJumpLogin(false);
        memberUpdateBeanCall.enqueue(mMemberUpdateBeanCustomCallBack);
    }

    private CustomCallBack<MemberUpdateBean> mMemberUpdateBeanCustomCallBack = new CustomCallBack<MemberUpdateBean>() {
        @Override
        public void success(String code, MemberUpdateBean data, String msg) {
            mLoadingDialog.dismiss();
            ToastUtils.showToast(msg);
            Intent intent = new Intent(SaveFaceActivity.this, MainActivity.class);
            intent.putExtra(Constant.MainActivityArgument.MAIN_ACTIVITY, Constant.FACE_REGESTED_OK);
            startActivity(intent);
        }

        @Override
        public void error(Throwable ex, String code, String msg) {
            mLoadingDialog.dismiss();
            ToastUtils.showToast(msg);
        }
    };

    private void showLoadingDialog() {
        mLoadingDialog = new Dialog(this, R.style.MyDialogCheckVersion);
        //点击其他地方消失
        mLoadingDialog.setCanceledOnTouchOutside(false);
        //填充对话框的布局
        View viewToFace = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dialog_loading, null, false);
        //跳过
        AutoUtils.autoSize(viewToFace);
        //初始化控件
        //将布局设置给
        mLoadingDialog.setContentView(viewToFace);
        //获取当前Activity所在的窗体
        Window dialogWindow = mLoadingDialog.getWindow();
        //设置Dialog从窗体中间弹出
        dialogWindow.setGravity(Gravity.CENTER);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //        lp.y = 20;//设置Dialog距离底部的距离
        //       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        mLoadingDialog.show();//显示对话框
    }

}
