package com.example.shanlin.facedemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.example.shanlin.facedemo.net.Connector;
import com.example.shanlin.facedemo.params.BaseParams;
import com.megvii.livenessdetection.LivenessLicenseManager;
import com.slfinance.facesdk.service.Manager;
import com.slfinance.facesdk.ui.LivenessActivity;
import com.slfinance.facesdk.util.ConUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FaceActivity extends AppCompatActivity {

    public static final int OK_CODE = 73;
    private Button                 mBtFaceCheck;
    private LivenessLicenseManager livenessLicenseManager;
    private boolean isLivenessLicenseGet = false;
    //活体照片路径
    private String  path                 = Environment.getExternalStorageDirectory() + "/faceImg/" + System.currentTimeMillis() + ".png";
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
    private Button mBtCheckFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);
        mBtFaceCheck = (Button) findViewById(R.id.bt_gacecheck);
        mBtFaceCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FaceActivity.this, LivenessActivity.class);
                startActivityForResult(intent, 100);
            }
        });
        mBtCheckFace = (Button) findViewById(R.id.bt_checkFace);
        mBtCheckFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                checkFace(mLivenessImgBytes);
            }
        });
        initLiveness();
    }

    private void initLiveness() {
        //初始化注册管理器
        manager = new Manager(this);
        //如果要使用活体检测抓人脸
        livenessLicenseManager = new LivenessLicenseManager(MyApplication.app);
        boolean livenessLicenseManagerIsRegisted = manager.registerLicenseManager(livenessLicenseManager);

        //可以选择给自己设备打标
        String uuid = ConUtil.getUUIDString(MyApplication.app);
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

    private static final int REQUEST_CODE_LIVENESS = 2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.d("resultCode==" + resultCode);
        if (requestCode == 100) {
            mLivenessImgBytes = data.getByteArrayExtra("image_best");
            String delta = data.getStringExtra("delta");
            LogUtils.d("delta==" + delta + "  mLivenessImgBytes==" + mLivenessImgBytes);
            Bitmap bitmap =  BitmapFactory.decodeByteArray(mLivenessImgBytes, 0, mLivenessImgBytes.length);
            saveBitmap(bitmap);
        }


    }

    //将图像保存到SD卡中
    public void saveBitmap(Bitmap mBitmap) {
        File dir = new File(Environment.getExternalStorageDirectory() + "/faceImg/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File f = new File(path);
        try {
            f.createNewFile();
        } catch (IOException e) {

        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO: 2017-7-16
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //比对身份证和人脸
    private void checkFace(byte[] livenessImgBytes) {
        List<KeyValue> list = new ArrayList<>();
        list.add(new KeyValue(Constant.FaceCardCompare.API_KEY, Constant.FACE_APP_KEY));
        list.add(new KeyValue(Constant.FaceCardCompare.API_SECRET, Constant.API_SECRET));
        list.add(new KeyValue(Constant.FaceCardCompare.IMAGE_BASE64_1, Base64Utils.encodeBytes(livenessImgBytes)));
        // list.add(new KeyValue(Constant.FaceCardCompare.IMAGE_BASE64_2, mIdInfo.getHeadImage()));
        MultipartBody body = new MultipartBody(list, "UTF-8");
        RequestParams req = new BaseParams(Constant.OptionUrl.FACE_COMPARE, BaseParams.HttpType.PostHttps).getParams();
        req.setRequestBody(body);
        Connector.getInstance().doHttpPost(req, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                try {
                    if (!TextUtils.isEmpty(s)) {
                        JSONObject jsonObject = new JSONObject(s);
                        String confidence = jsonObject.getString("confidence");
                        JSONObject thresholds = jsonObject.getJSONObject("thresholds");
                        String threshold_1e_6 = thresholds.getString("1e-3");//比对分在"threshold_1e_6"以上，为比对成功
                        if (Float.parseFloat(confidence) > OK_CODE) {
                            ToastUtils.showToast("比对值为：" + confidence + "\n" + "比对成功，证件符合同一个人！");
                            //上传人脸照片
                            uploadFaceImg();
                        }
                    } else {
                        //比对失败
                        ToastUtils.showToast("比对失败");
                    }
                } catch (JSONException e) {
                    ToastUtils.showToast(e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                ToastUtils.showToast(throwable.getMessage());
            }

            @Override
            public void onCancelled(CancelledException e) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 上传活体照片
     */
    public void uploadFaceImg() {


    }
}
