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
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.utils.ThreadUtils;
import com.zhy.autolayout.utils.AutoUtils;

public class SaveFaceActivity extends BaseActivity {

    private TextView mTvSave;
    private Dialog   mSaveFaceDialog;
    private Dialog   mLoadingDialog;

    @Override
    public int initLayout() {
        return R.layout.activity_save_face;
    }

    @Override
    public void initView() {
        mTvSave = (TextView) findViewById(R.id.tv_save);
        mTvSave.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_save:
                //显示loading页面
                showLoadingDialog();
                // TODO: 2017-7-18 联网发送数据
                ThreadUtils.runMainDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mLoadingDialog.dismiss();
                        Intent intent = new Intent(SaveFaceActivity.this, MainActivity.class);
                        intent.putExtra(Constant.FACE_VERIFY, Constant.FACE_REGESTED_OK);
                        startActivity(intent);
                    }
                }, 3000);
                break;
        }
    }

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
