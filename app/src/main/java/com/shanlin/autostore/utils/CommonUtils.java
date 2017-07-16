package com.shanlin.autostore.utils;

import android.content.Context;
import android.content.Intent;

/**
 * Created by DELL on 2017/7/14 0014.
 */

public class CommonUtils {

    /**
     * 跳转页面
     * @param context
     * @param activity
     */
    public static void toNextActivity (Context context, Class activity) {
        context.startActivity(new Intent(context,activity));
    }
}
