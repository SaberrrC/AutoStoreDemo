package com.shanlin.autostore.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.megvii.livenessdetection.LivenessLicenseManager;
import com.shanlin.autostore.AutoStoreApplication;
import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;
import com.shanlin.autostore.base.BaseActivity;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.utils.CommonUtils;
import com.shanlin.autostore.utils.LogUtils;
import com.shanlin.autostore.utils.MPermissionUtils;
import com.shanlin.autostore.utils.ToastUtils;
import com.slfinance.facesdk.service.Manager;
import com.slfinance.facesdk.ui.LivenessActivity;
import com.slfinance.facesdk.util.ConUtil;

import java.util.Map;

/**
 * Created by DELL on 2017/7/14 0014.
 */
public class LoginActivity extends BaseActivity {

    public static final int REQUEST_CODE_LOGIN = 100;
    private AlertDialog dialog;
    private View        dialogOpenWX;

    //人脸识别
    public static final int OK_PERCENT = 73;
    private LivenessLicenseManager livenessLicenseManager;
    private boolean isLivenessLicenseGet = false;
    //活体照片路径
    private Manager manager;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Map<String, Long> managerRegistResult = (Map<String, Long>) msg.obj;
            if (managerRegistResult != null) {
                //活体检测功能授权成功
                isLivenessLicenseGet = managerRegistResult.get(livenessLicenseManager.getVersion()) > 0;
            }
            return true;
        }
    });
    private byte[] mLivenessImgBytes;

    @Override
    public int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        findViewById(R.id.btn_login_by_face).setOnClickListener(this);
        findViewById(R.id.btn_login_by_phone).setOnClickListener(this);
        findViewById(R.id.btn_login_by_wx).setOnClickListener(this);
        dialogOpenWX = LayoutInflater.from(this).inflate(R.layout.dialog_open_wx, null);
        dialogOpenWX.findViewById(R.id.tv_open).setOnClickListener(this);
        dialogOpenWX.findViewById(R.id.tv_concel).setOnClickListener(this);
        dialog = new AlertDialog.Builder(this).setView(dialogOpenWX).create();
        //人脸识别
        initLiveness();

    }

    private void initLiveness() {
        //初始化注册管理器
        manager = new Manager(this);
        //如果要使用活体检测抓人脸
        livenessLicenseManager = new LivenessLicenseManager(AutoStoreApplication.getApp());
        boolean livenessLicenseManagerIsRegisted = manager.registerLicenseManager(livenessLicenseManager);
        //可以选择给自己设备打标
        String uuid = ConUtil.getUUIDString(AutoStoreApplication.getApp());
        //异步进行网络注册请求
        final String finalUuid = uuid;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Long> managerRegistResult = manager.takeLicenseFromNetwork(finalUuid);
                handler.sendMessage(handler.obtainMessage(1, managerRegistResult));
            }
        }).start();
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_login_by_face://使用人脸识别快速登录
                //                                CommonUtils.toNextActivity(this,MainActivity.class);
                MPermissionUtils.requestPermissionsResult(this, 1, new String[]{Manifest.permission.CAMERA}, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Intent intent = new Intent(LoginActivity.this, LivenessActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_LOGIN);
                    }

                    @Override
                    public void onPermissionDenied() {
                        MPermissionUtils.showTipsDialog(LoginActivity.this);
                    }
                });
                break;
            case R.id.btn_login_by_phone:
                CommonUtils.toNextActivity(this, PhoneNumLoginActivity.class);
                break;
            case R.id.btn_login_by_wx:
                dialog.show();
                break;
            case R.id.tv_open:
                // TODO: 2017/7/16 0016 wx登录
                break;
            case R.id.tv_concel:
                dialog.dismiss();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.d("resultCode==" + resultCode);
        if (requestCode == REQUEST_CODE_LOGIN) {
            try {
                if (data == null) {
                    return;
                }
                mLivenessImgBytes = data.getByteArrayExtra("image_best");
                if (mLivenessImgBytes == null || mLivenessImgBytes.length == 0) {
                    ToastUtils.showToast("人脸识别失败");
                    return;
                }
                String delta = data.getStringExtra("delta");
                LogUtils.d("delta==" + delta + "  mLivenessImgBytes==" + mLivenessImgBytes);
                Bitmap bitmap = BitmapFactory.decodeByteArray(mLivenessImgBytes, 0, mLivenessImgBytes.length);
                CommonUtils.saveBitmap(bitmap);
                // TODO: 2017-7-17 发送到服务器进行比对
                //            Intent intent = new Intent(this, MainActivity.class);
                //            intent.putExtra(Constant.MainActivityArgument.MAIN_ACTIVITY, Constant.MainActivityArgument.LOGIN);
                //            startActivity(intent);
                //            finish();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(Constant.MainActivityArgument.MAIN_ACTIVITY, Constant.MainActivityArgument.LOGIN);
                startActivity(intent);

                //                Intent intent = new Intent(this, MainActivity.class);
                //                //                intent.putExtra("key","value");
                //                intent.putExtra(Constant.MainActivityArgument.MAIN_ACTIVITY, Constant.MainActivityArgument.LOGIN);
                //                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
