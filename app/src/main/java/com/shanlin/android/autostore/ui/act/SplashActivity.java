package com.shanlin.android.autostore.ui.act;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.shanlin.android.autostore.common.base.BaseActivity;
import com.shanlin.android.autostore.common.constants.Constant;
import com.shanlin.android.autostore.common.utils.CommonUtils;
import com.shanlin.android.autostore.common.utils.MPermissionUtils;
import com.shanlin.android.autostore.common.utils.SpUtils;
import com.shanlin.android.autostore.common.utils.ThreadUtils;
import com.shanlin.android.autostore.common.utils.ToastUtils;
import com.shanlin.android.autostore.common.utils.VersionManagementUtil;
import com.shanlin.android.autostore.entity.event.WxMessageEvent;
import com.shanlin.android.autostore.entity.respone.CheckUpdateBean;
import com.shanlin.android.autostore.entity.respone.PersonInfoBean;
import com.shanlin.android.autostore.presenter.Contract.SplashActContract;
import com.shanlin.android.autostore.presenter.SplashPresenter;
import com.shanlin.autostore.MainActivity;
import com.shanlin.autostore.R;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by cuieney on 14/08/2017.
 */

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashActContract.View {

    private AlertDialog updateDialog;
    private static final int REQUEST_CODE_DOWN = 100;
    private String forceUpdate;


    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void initData() {
        EventBus.getDefault().post(new WxMessageEvent());
        ThreadUtils.runMainDelayed(() -> CommonUtils.checkPermission(mContext, new MPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                if (!CommonUtils.checkNet()) {
                    toLoginActivity();
                    return;
                }
                CommonUtils.getDevicedID();
                CommonUtils.netWorkWarranty();
                mPresenter.checkUpgrade(2);
            }

            @Override
            public void onPermissionDenied() {
                MPermissionUtils.showTipsDialog(mContext);
            }
        }), 2000);
    }


    @Override
    public void onTokenSuccess(String code, PersonInfoBean response, String msg) {
        if (response.getData() == null) {
            toLoginActivity();
            return;
        }
        toMainActivity(response);
    }

    @Override
    public void onUpgradeSuccess(String code, CheckUpdateBean data, String msg) {
        if (data == null) {
            mPresenter.checkToken();
            return;
        }
        if (data.getData() == null) {
            mPresenter.checkToken();
            return;
        }
        //成功
        try {
            //判断是否更新
            // TODO: 2017-7-27 版本 0.1.1
            String currentVersion = VersionManagementUtil.getVersion(mContext);
            String version = data.getData().getVersion();
            forceUpdate = data.getData().getForceUpdate();
            if (version == null) {
                mPresenter.checkToken();
                return;
            }
            //比较版本，返回1需要更新
            if (VersionManagementUtil.VersionComparison(version, currentVersion) == 1) {
                //更新
                showUpdateDialog(data.getData().getForceUpdate(), data.getData().getDownloadUrl());
            } else {
                mPresenter.checkToken();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mPresenter.checkToken();
        }
    }

    @Override
    public void onTokenFailed(Throwable ex, String code, String msg) {
        toLoginActivity();
    }

    @Override
    public void onUpgradeFailed(Throwable ex, String code, String msg) {
        mPresenter.checkToken();
    }

    private void toMainActivity(PersonInfoBean data) {
        SpUtils.saveString(this, Constant.USER_PHONE_LOGINED, data.getData().getDate().getMobile());
        Intent intent = new Intent(mContext, MainActivity.class);
        if (TextUtils.isEmpty(data.getData().getDate().getFaceToken())) {
            intent.putExtra(Constant.FACE_VERIFY, Constant.FACE_VERIFY_NO);
        } else {
            intent.putExtra(Constant.FACE_VERIFY, Constant.FACE_VERIFY_OK);
        }
        startActivity(intent);
        finish();
    }

    private void toLoginActivity() {
        CommonUtils.toNextActivity(mContext, LoginActivity.class);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateDialog != null)
            updateDialog.dismiss();
    }

    private void showUpdateDialog(String forceUpdate, final String DownLoadUrl) {
        final AlertDialog.Builder adInfo = new AlertDialog.Builder(mContext);
        switch (forceUpdate) {
            //强更
            case "1":
                CommonUtils.checkPermission(mContext, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        adInfo.setTitle("版本更新");
                        adInfo.setMessage("此版本不可用，请更新");
                        updateDialog = adInfo.setPositiveButton("更新", (dialog, which) -> {
                            // TODO: 2017/7/26  更新， 进行web跳转 下载apk
                            upData(DownLoadUrl);
                        }).create();
                        updateDialog.setCancelable(false);
                        updateDialog.setCanceledOnTouchOutside(false);
                        updateDialog.show();
                    }

                    @Override
                    public void onPermissionDenied() {
                        mPresenter.checkToken();
                    }
                });
                break;
            //非强更
            case "0":
                adInfo.setTitle("版本更新");
                adInfo.setMessage("发现新版本，请更新");
                updateDialog = adInfo.setPositiveButton("更新", (dialog, which) -> {
                    // TODO: 2017/7/26  更新， 进行web跳转 下载apk
                    upData(DownLoadUrl);
                }).setNegativeButton("取消", (dialog, which) -> {
                    updateDialog.dismiss();
                    mPresenter.checkToken();
                }).create();
                updateDialog.setCancelable(false);
                updateDialog.setCanceledOnTouchOutside(false);
                updateDialog.show();
                break;
        }
    }

    private void upData(String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        try {
            startActivityForResult(intent, REQUEST_CODE_DOWN);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast("更新失败");
            mPresenter.checkToken();
        }
    }

}
