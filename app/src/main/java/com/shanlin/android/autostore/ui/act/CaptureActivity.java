package com.shanlin.android.autostore.ui.act;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.Result;
import com.shanlin.android.autostore.App;
import com.shanlin.android.autostore.common.base.BaseActivity;
import com.shanlin.android.autostore.common.utils.SpUtils;
import com.shanlin.android.autostore.common.utils.ToastUtils;
import com.shanlin.android.autostore.common.zxing.camera.CameraManager;
import com.shanlin.android.autostore.common.zxing.utils.BeepManager;
import com.shanlin.android.autostore.common.zxing.utils.CaptureActivityHandler;
import com.shanlin.android.autostore.common.zxing.utils.CommonUtils;
import com.shanlin.android.autostore.common.zxing.utils.InactivityTimer;
import com.shanlin.android.autostore.common.zxing.utils.StatusBarUtils;
import com.shanlin.android.autostore.di.component.DaggerCaptureComponent;
import com.shanlin.android.autostore.di.module.CaptureModule;
import com.shanlin.android.autostore.entity.body.RealOrderBody;
import com.shanlin.android.autostore.entity.respone.RealOrderBean;
import com.shanlin.android.autostore.presenter.CapturePresenter;
import com.shanlin.android.autostore.presenter.Contract.CaptureContract;
import com.shanlin.autostore.R;
import com.shanlin.autostore.activity.ChoosePayWayActivity;
import com.shanlin.autostore.activity.GateActivity;
import com.shanlin.autostore.bean.paramsBean.ZXingOrderBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.constants.Constant_LeMaiBao;
import com.shanlin.autostore.utils.StrUtils;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by cuieney on 16/08/2017.
 */

public class CaptureActivity extends BaseActivity<CapturePresenter> implements CaptureContract.View, SurfaceHolder.Callback {

    @Inject
    CameraManager cameraManager;
    @Inject
    CaptureActivityHandler handler;
    @Inject
    InactivityTimer inactivityTimer;
    @Inject
    BeepManager beepManager;

    @BindView(R.id.capture_preview)
    SurfaceView scanPreview;
    @BindView(R.id.capture_scan_line)
    ImageView scanLine;
    @BindView(R.id.capture_crop_view)
    RelativeLayout scanCropView;
    @BindView(R.id.capture_container)
    RelativeLayout scanContainer;

    private Rect mCropRect;
    private String creditBalence;
    private boolean isHasSurface = false;
    private Gson gson = new Gson();
    private String[] aliArgs = new String[]{Constant_LeMaiBao.DEVICEDID, Constant_LeMaiBao.ORDER_NO, Constant_LeMaiBao.TOTAL_AMOUNT, Constant_LeMaiBao.STORED_ID, Constant.TOKEN, Constant_LeMaiBao.CREDIT_BALANCE};
    private String devicedID;
    private String orderNo;

    @Override
    protected void initInject() {
        DaggerCaptureComponent.builder()
                .appComponent(App.getInstance().getAppComponent())
                .captureModule(new CaptureModule(this))
                .build().inject(this);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_capture;
    }

    @Override
    public void initData() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_capture);
        StatusBarUtils.setColor(this, Color.WHITE);
        CommonUtils.initToolbar(this, "扫一扫", R.color.black, null);

        scanLineAnim();

        creditBalence = getIntent().getStringExtra(Constant_LeMaiBao.CREDIT_BALANCE);
    }

    private void scanLineAnim() {
        ViewTreeObserver vto = scanLine.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                scanLine.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int height = scanLine.getHeight();
                TranslateAnimation animation = new TranslateAnimation(0, 0, -height, 0);
                animation.setDuration(3000);
                animation.setRepeatCount(-1);
                animation.setRepeatMode(Animation.RESTART);
                scanLine.startAnimation(animation);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isHasSurface) {
            initCamera(scanPreview.getHolder());
        } else {
            scanPreview.getHolder().addCallback(this);
        }
        inactivityTimer.onResume();
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            initCrop();
        } catch (IOException ioe) {
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            displayFrameworkBugMessageAndExit();
        }
    }

    @Override
    public void onUpdateTempToRealSuccess(String code, RealOrderBean data, String msg) {
        String totalAmount = data.getData().getTotalAmount();//应付总金额
        com.shanlin.android.autostore.common.utils.CommonUtils.sendDataToNextActivity(
                CaptureActivity.this,
                ChoosePayWayActivity.class,
                aliArgs,
                new String[]{devicedID, orderNo, totalAmount, "2", SpUtils.getString(this, Constant.TOKEN, ""), creditBalence});
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    @Override
    public void onUpdateTempToRealFailed(Throwable ex, String code, String msg) {
        ToastUtils.showToast("二维码无法识别");
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    public void handleDecode(Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();
        String result = rawResult.getText();
        //处理逻辑
        if (result.contains("打开闸机")) {//扫描闸机的二维码
            String deviceId = SpUtils.getString(this, Constant.DEVICEID, "");
            if (StrUtils.isEmpty(deviceId)) {
                ToastUtils.showToast("网络异常，请稍后再试");
                com.shanlin.autostore.utils.CommonUtils.getDevicedID();
                finish();
                return;
            }
            Intent intent = new Intent(this, GateActivity.class);
            intent.putExtra(Constant.QR_GARD, result);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            return;
        }

        //扫描订单处理逻辑
        if (result.contains("订单支付") || result.contains("orderNo")) {
            //订单号信息
            ZXingOrderBean zXingOrderBean = gson.fromJson(result, ZXingOrderBean.class);
            //调用生成正式订单接口
            devicedID = zXingOrderBean.getDeviceId();
            orderNo = zXingOrderBean.getOrderNo();
            mPresenter.updateTempToReal(new RealOrderBody(orderNo));
        } else {
            com.shanlin.autostore.utils.CommonUtils.showToast(this,"扫描超时,请重试");
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }

    }



    public Rect getCropRect() {
        return mCropRect;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(surfaceHolder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isHasSurface = false;
    }



    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.zxing_bar_name));
        builder.setMessage("Camera error");
        builder.setPositiveButton("OK", (dialog, which) -> finish());
        builder.setOnCancelListener(dialog -> finish());
        builder.show();
    }

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
