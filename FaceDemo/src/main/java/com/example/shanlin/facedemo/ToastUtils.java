package com.example.shanlin.facedemo;

import android.widget.Toast;

public class ToastUtils {

    private static Toast toast;

    public static void showToast(final String text) {
        ThreadUtils.runMain(new Runnable() {
            @Override
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(MyApplication.app, text, Toast.LENGTH_SHORT);
                } else {
                    toast.setText(text);
                }
                toast.show();
            }
        });
    }
}
