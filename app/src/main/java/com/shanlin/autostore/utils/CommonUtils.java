package com.shanlin.autostore.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shanlin.autostore.R;

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

    /**
     * toast
     * @param context
     * @param text
     */
    public static void showToast (Context context,String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 初始化toolbar
     * @param activity
     * @param str
     * @param desActivity
     */
    public static void initToolbar(final Activity activity, String str, int colorRes,final Class
            desActivity) {
        Toolbar tb = (Toolbar) activity.findViewById(R.id.toolbar);
        TextView title = (TextView) activity.findViewById(R.id.toolbar_title);
        tb.setNavigationIcon(R.mipmap.nav_back);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.toNextActivity(activity,desActivity);
                activity.finish();
            }
        });
        title.setText(str);
        title.setTextColor(activity.getResources().getColor(colorRes));
    }
}