package com.shanlin.autostore.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.megvii.licensemanager.Manager;
import com.megvii.livenessdetection.LivenessLicenseManager;
import com.shanlin.android.autostore.common.utils.MPermissionUtils;
import com.shanlin.android.autostore.common.utils.SpUtils;
import com.shanlin.android.autostore.common.utils.ThreadUtils;
import com.shanlin.android.autostore.common.utils.ToastUtils;
import com.shanlin.autostore.AutoStoreApplication;
import com.shanlin.autostore.R;
import com.shanlin.autostore.bean.resultBean.UserVertifyStatusBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.constants.Constant_LeMaiBao;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.livenesslib.util.ConUtil;
import com.shanlin.autostore.net.LoggingInterceptor;
import com.shanlin.autostore.utils.env.DeviceInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DELL on 2017/7/14 0014.
 */

public class CommonUtils {



    /**
     * bitmap转为base64
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


//    /**
//     * 获取用户乐买宝认证信息
//     *
//     * @param context
//     * @param service
//     * @param token
//     */
//    public static void checkAuthenStatus(final Context context, HttpService service, String token) {
//        Call<UserVertifyStatusBean> call = service.getUserVertifyAuthenStatus(token);
//        call.enqueue(new Callback<UserVertifyStatusBean>() {
//            @Override
//            public void onResponse(Call<UserVertifyStatusBean> call, Response<UserVertifyStatusBean> response) {
//                UserVertifyStatusBean body = response.body();
//                if ("200".equals(body.getCode())) {
//                    String status = body.getData().getVerifyStatus();
//                    Log.d("wr", "-----------------authen_status=" + status);
//                    SpUtils.saveString(context, Constant_LeMaiBao.AUTHEN_STATE_KEY, status);
//                } else {
//                    //                    Toast.makeText(context, "未获取到认证数据", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserVertifyStatusBean> call, Throwable t) {
//                Toast.makeText(context, "获取信息失败", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);
        CommonUtils.debugLog("设置透明度");
    }

    /**
     * 创建dialog对象
     *
     * @param context
     * @param view
     */
    public static Dialog getDialog(Context context, View view, boolean cancelable) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(cancelable);
        return dialog;
    }

    public static AlertDialog getDialog(Context context, View view, int style, boolean cancelable) {
        AlertDialog dialog = new AlertDialog.Builder(context, style).setView(view).create();
        dialog.setCancelable(cancelable);
        return dialog;
    }

    /**
     * 跳转页面
     *
     * @param context
     * @param activity
     */
    public static void toNextActivity(Context context, Class activity) {
        context.startActivity(new Intent(context, activity));
    }

    /**
     * 携带数据跳转页面
     *
     * @param context
     * @param activity
     */
    public static void sendDataToNextActivity(Context context, Class activity, String[] key, String[] data) {
        Intent intent = new Intent(context, activity);
        for (int i = 0; i < key.length; i++) {
            intent.putExtra(key[i], data[i]);
        }
        context.startActivity(intent);
    }

    /**
     * 网络连接工具,get,post通用
     *
     * @return
     */
    public static HttpService doNet() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new LoggingInterceptor());//使用自定义的Log拦截器
        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(Constant.BASE_URL).client(client).build();
        HttpService service = retrofit.create(HttpService.class);
        return service;
    }

    /**
     * 获取当前系统时间
     *
     * @param type true 详细时间,精确到秒  false 精确到日
     * @return
     */
    public static String getCurrentTime(boolean type) {
        SimpleDateFormat formatter = new SimpleDateFormat(type ? "yyyy-MM-dd  HH:mm:ss " : "yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * toast
     *
     * @param context
     * @param text
     */
    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * debug log信息
     *
     * @param content
     */
    public static void debugLog(String content) {
        if (true) {
            Log.d("wr", "----946----" + content);
        }
    }

    /**
     * 初始化toolbar
     *
     * @param activity
     * @param str
     * @param desActivity
     */
    public static void initToolbar(final Activity activity, String str, int colorRes, final Class desActivity) {
        Toolbar tb = (Toolbar) activity.findViewById(R.id.toolbar);
        TextView title = (TextView) activity.findViewById(R.id.toolbar_title);
        tb.setNavigationIcon(R.mipmap.nav_back);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (desActivity != null) {
                    CommonUtils.toNextActivity(activity, desActivity);
                }
                activity.finish();
            }
        });
        title.setText(str);
        title.setTextColor(activity.getResources().getColor(colorRes));
    }

    public static File getPackagedirectory() {
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + AutoStoreApplication.getApp().getPackageName());
        return dir;
    }

    private static final String path = Environment.getExternalStorageDirectory() + "/com.shanlinjinrong.lifehelper/" + "/faceImg/" + System.currentTimeMillis() + ".png";

    //将图像保存到SD卡中
    public static File saveBitmap(Bitmap mBitmap) {
        File dir = new File(Environment.getExternalStorageDirectory() + "/com.shanlinjinrong.lifehelper/faceImg");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File f = new File(Environment.getExternalStorageDirectory() + "/com.shanlinjinrong.lifehelper/faceImg/" + System.currentTimeMillis() + ".png");
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
        mBitmap.compress(Bitmap.CompressFormat.PNG, 20, fOut);
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
        return f;
    }

    public static boolean checkNet() {
        String networkTypeName = DeviceInfo.getNetworkTypeName();
        if (TextUtils.isEmpty(networkTypeName)) {//没网络
            ToastUtils.showToast("网络错误");
            return false;
        }
        return true;
    }

    public static void checkPermission(Activity activity, MPermissionUtils.OnPermissionListener listener) {
        MPermissionUtils.requestPermissionsResult(activity, 1, new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS}, listener);
    }

    public static void getDevicedID() {
        ThreadUtils.runSub(new Runnable() {
            @Override
            public void run() {
                String deviceId = JPushInterface.getRegistrationID(AutoStoreApplication.getApp());
                if (!TextUtils.isEmpty(deviceId)) {
                    SpUtils.saveString(AutoStoreApplication.getApp(), Constant.DEVICEID, deviceId);
                    saveDevicedID(deviceId);
                } else {
                    deviceId = SpUtils.getString(AutoStoreApplication.getApp(), Constant.DEVICEID, "");
                    if (!TextUtils.isEmpty(deviceId)) {
                        LogUtils.d(Constant.DEVICEID + "   " + deviceId);
                        return;
                    }
                    deviceId = getDIsKDevicedID();
                    if (!TextUtils.isEmpty(deviceId)) {
                        SpUtils.saveString(AutoStoreApplication.getApp(), Constant.DEVICEID, deviceId);
                    }
                }
                LogUtils.d(Constant.DEVICEID + "   " + deviceId);
            }
        });
    }

    /**
     * 联网授权
     */
    public static void netWorkWarranty() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Manager manager = new Manager(AutoStoreApplication.getApp());
                LivenessLicenseManager licenseManager = new LivenessLicenseManager(AutoStoreApplication.getApp());
                manager.registerLicenseManager(licenseManager);
                manager.takeLicenseFromNetwork(ConUtil.getUUIDString(AutoStoreApplication.getApp()));
                if (licenseManager.checkCachedLicense() > 0) {//成功
                    AutoStoreApplication.FACE = true;
                } else {//失败
                    AutoStoreApplication.FACE = false;
                }
            }
        }).start();
    }

    //将图像保存到SD卡中
    public static void saveDevicedID(String deviceId) {
        File dir = new File(getPackagedirectory() + "/jpush");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String path = getPackagedirectory() + "/jpush" + "/deviceid.txt";
        File f = new File(path);
        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
            fOut.write(deviceId.getBytes());
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getDIsKDevicedID() {
        File dir = new File(getPackagedirectory() + "/jpush");
        String str = "";
        if (!dir.exists()) {
            dir.mkdirs();
            return str;
        }
        String path = getPackagedirectory() + "/jpush/" + "deviceid.txt";
        File file = new File(path);
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            // size  为字串的长度 ，这里一次性读完
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            str = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return str;
    }

}
