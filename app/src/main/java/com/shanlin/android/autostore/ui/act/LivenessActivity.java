package com.shanlin.android.autostore.ui.act;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.megvii.livenessdetection.DetectionConfig;
import com.megvii.livenessdetection.DetectionFrame;
import com.megvii.livenessdetection.Detector;
import com.megvii.livenessdetection.Detector.DetectionFailedType;
import com.megvii.livenessdetection.Detector.DetectionListener;
import com.megvii.livenessdetection.Detector.DetectionType;
import com.megvii.livenessdetection.FaceQualityManager;
import com.megvii.livenessdetection.FaceQualityManager.FaceQualityErrorType;
import com.megvii.livenessdetection.bean.FaceIDDataStruct;
import com.megvii.livenessdetection.bean.FaceInfo;
import com.shanlin.android.autostore.common.constants.Constant;
import com.shanlin.android.autostore.common.livenesslib.util.ConUtil;
import com.shanlin.android.autostore.common.livenesslib.util.DialogUtil;
import com.shanlin.android.autostore.common.livenesslib.util.ICamera;
import com.shanlin.android.autostore.common.livenesslib.util.IDetection;
import com.shanlin.android.autostore.common.livenesslib.util.IMediaPlayer;
import com.shanlin.android.autostore.common.livenesslib.util.Screen;
import com.shanlin.android.autostore.common.livenesslib.util.SensorUtil;
import com.shanlin.android.autostore.common.utils.StatusBarUtils;
import com.shanlin.autostore.R;

import org.json.JSONObject;

import java.util.List;

import static com.shanlin.autostore.R.layout.liveness_layout;

public class LivenessActivity extends Activity implements TextureView.SurfaceTextureListener {

    private TextureView        camerapreview;
    private ProgressBar        mProgressBar;// 网络上传请求验证时出现的ProgressBar
    private LinearLayout       headViewLinear;// "请在光线充足的情况下进行检测"这个视图
    private LinearLayout       rootView;// 根视图
    private Detector           mDetector;// 活体检测器
    private ICamera            mICamera;// 照相机工具类
    private Handler            mainHandler;
    private Handler            mHandler;
    private JSONObject         jsonObject;
    private IMediaPlayer       mIMediaPlayer;// 多媒体工具类
    private IDetection         mIDetection;
    private DialogUtil         mDialogUtil;
    private TextView           promptText;
    private boolean            isHandleStart;// 是否开始检测
    private FaceQualityManager mFaceQualityManager;
    private SensorUtil         sensorUtil;

    private HandlerThread mHandlerThread = new HandlerThread("videoEncoder");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(liveness_layout);
        StatusBarUtils.setColor(this, Color.TRANSPARENT);
        init();
        initData();
    }

    private void init() {
        sensorUtil = new SensorUtil(this);
        Screen.initialize(this);
        mainHandler = new Handler();
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
        mIMediaPlayer = new IMediaPlayer(this);
        mDialogUtil = new DialogUtil(this);
        rootView = (LinearLayout) findViewById(R.id.liveness_layout_rootRel);
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        tb.setNavigationIcon(R.mipmap.nav_back);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(Constant.ON_BACK_PRESSED, Constant.ON_BACK_PRESSED);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        title.setText("人脸识别");
        title.setTextColor(getResources().getColor(R.color.black));
        mIDetection = new IDetection(this, rootView);
        mICamera = new ICamera();
        promptText = (TextView) findViewById(R.id.liveness_layout_promptText);
        camerapreview = (TextureView) findViewById(R.id.liveness_layout_textureview);
        openCamera();
        camerapreview.setSurfaceTextureListener(this);
        mProgressBar = (ProgressBar) findViewById(R.id.liveness_layout_progressbar);
        mProgressBar.setVisibility(View.INVISIBLE);
        headViewLinear = (LinearLayout) findViewById(R.id.liveness_layout_bottom_tips_head);
        headViewLinear.setVisibility(View.VISIBLE);
        mIDetection.viewsInit();
    }

    public boolean isIn = true;

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 初始化活体检测器
        DetectionConfig config = new DetectionConfig.Builder().build();
        mDetector = new Detector(this, config);
        boolean initSuccess = mDetector.init(this, ConUtil.readModel(this), "");
        if (!initSuccess) {
            mDialogUtil.showDialog(getString(R.string.meglive_detect_initfailed));
        }
        // 初始化动画
        new Thread(new Runnable() {
            @Override
            public void run() {
                mIDetection.animationInit();
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isHandleStart = false;
    }

    private void openCamera() {
        Camera mCamera = mICamera.openCamera(LivenessActivity.this);
        if (mCamera != null) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(1, cameraInfo);
            // 获取到相机分辨率对应的显示大小，并把这个值复制给camerapreview
            RelativeLayout.LayoutParams layout_params = mICamera.getLayoutParam();
            camerapreview.setLayoutParams(layout_params);
            // 初始化人脸质量检测管理类
            mFaceQualityManager = new FaceQualityManager(1 - 0.5f, 0.5f);
            mIDetection.mCurShowIndex = -1;
        } else {
            mDialogUtil.showDialog(getString(R.string.meglive_camera_initfailed));
        }
    }

    /**
     * 开始检测
     */
    private void handleStart() {
        if (isHandleStart)
            return;
        isHandleStart = true;
        mainHandler.post(mTimeoutRunnable);
        jsonObject = new JSONObject();
    }

    private Runnable mTimeoutRunnable = new Runnable() {
        @Override
        public void run() {
            // 倒计时开始
            initDetecteSession();
            if (mIDetection.mDetectionSteps != null) {
                setHideText("请眨眼");
                changeType(mIDetection.mDetectionSteps.get(0), 10);
            }
        }
    };

    private void setHideText(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        promptText.setText(text);
    }

    private void initDetecteSession() {
        if (mICamera.mCamera == null)
            return;
        mProgressBar.setVisibility(View.INVISIBLE);
        mIDetection.detectionTypeInit();
        mCurStep = 0;
        mDetector.reset();
        setHideText("请眨眼");
        mDetector.changeDetectionType(mIDetection.mDetectionSteps.get(0));
    }

    public class MyPreviewCallback implements PreviewCallback {
        /**
         * 照相机预览数据回调 （PreviewCallback的接口回调方法）
         */
        @Override
        public void onPreviewFrame(final byte[] data, Camera camera) {
            try {
                Size previewsize = camera.getParameters().getPreviewSize();
                // 活体检测器检测
                mDetector.doDetection(data, previewsize.width, previewsize.height, 360 - mICamera.getCameraAngle(LivenessActivity.this));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class MyDetectionListener implements DetectionListener {
        /**
         * 活体验证成功 （DetectionListener的接口回调方法）
         */
        @Override
        public DetectionType onDetectionSuccess(final DetectionFrame validFrame) {
            mIMediaPlayer.reset();
            mCurStep++;
            if (mCurStep == mIDetection.mDetectionSteps.size()) {
                mProgressBar.setVisibility(View.VISIBLE);
                getLivenessData();
            } else {
                setHideText("请眨眼");
                changeType(mIDetection.mDetectionSteps.get(mCurStep), 10);
            }
            // 检测器返回值：如果不希望检测器检测则返回DetectionType.DONE，如果希望检测器检测动作则返回要检测的动作
            return mCurStep >= mIDetection.mDetectionSteps.size() ? DetectionType.DONE : mIDetection.mDetectionSteps.get(mCurStep);
        }

        private void getLivenessData() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final FaceIDDataStruct idDataStruct = mDetector.getFaceIDDataStruct();
                    String delta = idDataStruct.delta;
                    for (String key : idDataStruct.images.keySet()) {
                        byte[] data = idDataStruct.images.get(key);
                        if (key.equals("image_best")) {
                            byte[] imageBestData = data;// 这是最好的一张图片
                            Intent intent = new Intent();
                            intent.putExtra("image_best", imageBestData);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else if (key.equals("image_env")) {
                            byte[] imageEnvData = data;// 这是一张全景图
                        } else {
                        }
                    }
                }
            }).start();
        }

        /**
         * 活体检测失败 （DetectionListener的接口回调方法）
         */
        @Override
        public void onDetectionFailed(final DetectionFailedType type) {
            mHasSurface = true;
            doPreview();
            // 添加活体检测回调 （本Activity继承了DetectionListener）
            mDetector.setDetectionListener(new MyDetectionListener());
            // 添加相机预览回调（本Activity继承了PreviewCallback）
            mICamera.actionDetect(new MyPreviewCallback());
            mProgressBar.setVisibility(View.INVISIBLE);
            mIDetection.detectionTypeInit();
            mCurStep = 0;
            mDetector.reset();
            setHideText("请眨眼");
            mDetector.changeDetectionType(mIDetection.mDetectionSteps.get(0));
        }


        /**
         * 活体验证中（这个方法会持续不断的回调，返回照片detection信息） （DetectionListener的接口回调方法）
         */
        @Override
        public void onFrameDetected(long timeout, DetectionFrame detectionFrame) {
            faceOcclusion(detectionFrame);
        }
    }

    /**
     * 照镜子环节
     * 流程：1,先从返回的DetectionFrame中获取FaceInfo。在FaceInfo中可以先判断这张照片上的人脸是否有被遮挡的状况
     * ，入股有直接return
     * 2,如果没有遮挡就把SDK返回的DetectionFramed传入人脸质量检测管理类mFaceQualityManager中获取FaceQualityErrorType的list
     * 3.通过返回的list来判断这张照片上的人脸是否合格。
     * 如果返回list为空或list中FaceQualityErrorType的对象数量为0则表示这张照片合格开始进行活体检测
     */
    private void faceOcclusion(DetectionFrame detectionFrame) {
        mFailFrame++;
        if (detectionFrame != null) {
            FaceInfo faceInfo = detectionFrame.getFaceInfo();
            if (faceInfo != null) {
                if (faceInfo.eyeLeftOcclusion > 0.5 || faceInfo.eyeRightOcclusion > 0.5) {
                    if (mFailFrame > 10) {
                        mFailFrame = 0;
                    }
                    return;
                }
                if (faceInfo.mouthOcclusion > 0.5) {
                    if (mFailFrame > 10) {
                        mFailFrame = 0;
                    }
                    return;
                }
                boolean faceTooLarge = faceInfo.faceTooLarge;
                mIDetection.checkFaceTooLarge(faceTooLarge);
            }
        }
        // 从人脸质量检测管理类中获取错误类型list
        faceInfoChecker(mFaceQualityManager.feedFrame(detectionFrame));
    }

    private int mFailFrame = 0;

    public void faceInfoChecker(List<FaceQualityErrorType> errorTypeList) {
        if (errorTypeList == null || errorTypeList.size() == 0)
            handleStart();
        else {
            String infoStr = "";
            FaceQualityErrorType errorType = errorTypeList.get(0);
            if (errorType == FaceQualityErrorType.FACE_NOT_FOUND) {
                infoStr = getString(R.string.face_not_found);
            } else if (errorType == FaceQualityErrorType.FACE_POS_DEVIATED) {
                infoStr = getString(R.string.face_not_found);
            } else if (errorType == FaceQualityErrorType.FACE_NONINTEGRITY) {
                infoStr = getString(R.string.face_not_found);
            } else if (errorType == FaceQualityErrorType.FACE_TOO_DARK) {
                infoStr = getString(R.string.face_too_dark);
            } else if (errorType == FaceQualityErrorType.FACE_TOO_BRIGHT) {
                infoStr = getString(R.string.face_too_bright);
            } else if (errorType == FaceQualityErrorType.FACE_TOO_SMALL) {
                infoStr = getString(R.string.face_too_small);
            } else if (errorType == FaceQualityErrorType.FACE_TOO_LARGE) {
                infoStr = getString(R.string.face_too_large);
            } else if (errorType == FaceQualityErrorType.FACE_TOO_BLURRY) {
                infoStr = getString(R.string.face_too_blurry);
            } else if (errorType == FaceQualityErrorType.FACE_OUT_OF_RECT) {
                infoStr = getString(R.string.face_out_of_rect);
            }

            if (mFailFrame > 10) {
                mFailFrame = 0;
                setHideText(infoStr);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(Constant.ON_BACK_PRESSED, Constant.ON_BACK_PRESSED);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private int mCurStep = 0;// 检测动作的次数

    public void changeType(final Detector.DetectionType detectiontype, long timeout) {
        // 动画切换
        mIDetection.changeType(detectiontype, timeout);
        // 语音播放
        if (mCurStep == 0) {
            mIMediaPlayer.doPlay(mIMediaPlayer.getSoundRes(detectiontype));
        } else {
            mIMediaPlayer.doPlay(R.raw.meglive_well_done);
            mIMediaPlayer.setOnCompletionListener(detectiontype);
        }
    }

    private boolean mHasSurface = false;

    /**
     * TextureView启动成功后 启动相机预览和添加活体检测回调
     * （TextureView.SurfaceTextureListener的接口回调方法）
     */
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mHasSurface = true;
        doPreview();
        // 添加活体检测回调 （本Activity继承了DetectionListener）
        mDetector.setDetectionListener(new MyDetectionListener());
        // 添加相机预览回调（本Activity继承了PreviewCallback）
        mICamera.actionDetect(new MyPreviewCallback());
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    /**
     * TextureView销毁后 （TextureView.SurfaceTextureListener的接口回调方法）
     */
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mHasSurface = false;
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    private void doPreview() {
        if (!mHasSurface)
            return;
        mICamera.startPreview(camerapreview.getSurfaceTexture());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainHandler.removeCallbacksAndMessages(null);
        mICamera.closeCamera();
        mIMediaPlayer.close();
        if (mDetector != null)
            mDetector.release();
        mDialogUtil.onDestory();
        mIDetection.onDestroy();
        sensorUtil.release();
    }
}