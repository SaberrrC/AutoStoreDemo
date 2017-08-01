package com.shanlin.autostore.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shanlin.autostore.AutoStoreApplication;
import com.shanlin.autostore.R;
import com.shanlin.autostore.bean.resultBean.UserVertifyStatusBean;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.constants.Constant_LeMaiBao;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.net.LoggingInterceptor;
import com.shanlin.autostore.utils.env.DeviceInfo;

import java.io.File;
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
     * 获取用户乐买宝认证信息
     * @param context
     * @param service
     * @param token
     */
    public static void checkAuthenStatus(final Context context, HttpService service, String token) {
        Call<UserVertifyStatusBean> call = service.getUserVertifyAuthenStatus(token);
        call.enqueue(new Callback<UserVertifyStatusBean>() {
            @Override
            public void onResponse(Call<UserVertifyStatusBean> call, Response<UserVertifyStatusBean> response) {
                UserVertifyStatusBean body = response.body();
                if ("200".equals(body.getCode())) {
                    String status = body.getData().getVerifyStatus();
                    Log.d("wr", "-----------------authen_status="+status);
                    SpUtils.saveString(context, Constant_LeMaiBao.AUTHEN_STATE_KEY, status);
                } else {
                    Toast.makeText(context, "未获取到认证数据", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserVertifyStatusBean> call, Throwable t) {
                Toast.makeText(context, "获取信息失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * 创建dialog对象
     *
     * @param context
     * @param view
     */
    public static AlertDialog getDialog(Context context, View view, boolean cancelable) {
        AlertDialog dialog = new AlertDialog.Builder(context).setView(view).create();
        dialog.setCancelable(cancelable);
        dialog.show();
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
    public static void sendDataToNextActivity(Context context, Class activity,String[] key,String[]
                                              data) {
        Intent intent = new Intent(context,activity);
        for (int i = 0; i < key.length; i++) {
            intent.putExtra(key[i],data[i]);
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
     * @param type  true 详细时间,精确到秒  false 精确到日
     * @return
     */
    public static String getCurrentTime (boolean type) {
        SimpleDateFormat formatter = new SimpleDateFormat (type ? "yyyy-MM-dd  HH:mm:ss " : "yyyy-MM-dd");
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
     * @param content
     */
    public static void debugLog (String content) {
        if (true) {
            Log.d("wr", "----946----"+content);
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
        File dir = new File(Environment.getExternalStorageDirectory() + AutoStoreApplication.getApp().getPackageName());
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
            ToastUtils.showToast("无网络");
            return false;
        }
        return true;
    }

    public static void checkPermission(Activity activity,MPermissionUtils.OnPermissionListener listener) {
        MPermissionUtils.requestPermissionsResult(activity, 1, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_CONTACTS},listener);
    }
    public static void getDevicedID() {
        String deviceId = JPushInterface.getRegistrationID(AutoStoreApplication.getApp());
        if (!TextUtils.isEmpty(deviceId)) {
            SpUtils.saveString(AutoStoreApplication.getApp(), Constant.DEVICEID, deviceId);
            LogUtils.d(Constant.DEVICEID + "   " + deviceId);
        } else {

        }
    }
}
