package com.shanlin.autostore.utils;

/**
 * Created by DELL on 2017/7/20 0020.
 */

public class NumUtil {

    public static String NumberFormat(float f,int m){
        return String.format("%."+m+"f",f);
    }

    public static float NumberFormatFloat(float f,int m){
        String strfloat = NumberFormat(f,m);
        return Float.parseFloat(strfloat);
    }
}
