package com.shanlin.autostore.utils;

import android.widget.Toast;

import com.shanlin.autostore.AutoStoreApplication;

public class ToastUtils {

    private static Toast toast;

    public static void showToast(final String text) {
        ThreadUtils.runMain(new Runnable() {
            @Override
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(AutoStoreApplication.getApp(), text, Toast.LENGTH_SHORT);
                } else {
                    toast.setText(text);
                }
                toast.show();
            }
        });
    }

}
