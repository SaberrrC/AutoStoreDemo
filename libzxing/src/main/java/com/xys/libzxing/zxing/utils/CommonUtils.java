package com.xys.libzxing.zxing.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.xys.libzxing.R;


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

}
