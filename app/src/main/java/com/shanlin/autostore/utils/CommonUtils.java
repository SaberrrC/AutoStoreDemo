package com.shanlin.autostore.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shanlin.autostore.AutoStoreApplication;
import com.shanlin.autostore.R;
import com.shanlin.autostore.constants.Constant;
import com.shanlin.autostore.interf.HttpService;
import com.shanlin.autostore.net.LoggingInterceptor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DELL on 2017/7/14 0014.
 */

public class CommonUtils {

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
     * toast
     *
     * @param context
     * @param text
     */
    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
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
}
