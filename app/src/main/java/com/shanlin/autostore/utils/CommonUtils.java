package com.shanlin.autostore.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;

import com.shanlin.autostore.AutoStoreApplication;

import java.io.File;

/**
 * Created by DELL on 2017/7/14 0014.
 */

public class CommonUtils {

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
     * toast
     *
     * @param context
     * @param text
     */
    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static File getPackagedirectory() {
        File dir = new File(Environment.getExternalStorageDirectory() + AutoStoreApplication.getApp().getPackageName());
        return dir;
    }

}
