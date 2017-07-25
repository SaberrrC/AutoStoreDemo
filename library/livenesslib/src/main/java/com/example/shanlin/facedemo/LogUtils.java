package com.example.shanlin.facedemo;

/**
 * Created by wy on 2017/4/10.
 */

import android.util.Log;


/**
 * Created by th on 2016/12/1.
 */

public class LogUtils {


    private LogUtils() {
    }

    /**
     * Application TAG,use "logcat -s TAG"
     */
    private static String TAG = "ShanLin";

    public static void v(Object msg) {
        Log.v(TAG, getLogTitle() + msg);
    }

    public static void d(Object msg) {
        Log.d(TAG, getLogTitle() + msg);
    }

    public static void i(Object msg) {
    }

    public static void w(Object msg) {
    }

    public static void e(Object msg) {
    }


    /**
     * make log title
     *
     * @return title_name
     */
    private static String getLogTitle() {
        StackTraceElement elm = Thread.currentThread().getStackTrace()[4];
        String className = elm.getClassName();
        int dot = className.lastIndexOf('.');
        if (dot != -1) {
            className = className.substring(dot + 1);
        }

        return className + "." + elm.getMethodName() + "(" + elm.getLineNumber() + ")" + ": ";
    }

}