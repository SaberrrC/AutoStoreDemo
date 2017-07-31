package com.shanlin.autostore.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * @author:Zou ChangCheng
 * @date:2017/7/31
 * @project:autostore
 * @detail:
 */

public class VersionManagementUtil {
    private static final String TAG = "VersionManagementUtil";
    private static Context mContext;
    //单例
    private static final VersionManagementUtil INSTANCE = new VersionManagementUtil();

    public static VersionManagementUtil getInstance(Context mContext) {
        VersionManagementUtil.mContext = mContext;
        return VersionManagementUtil.INSTANCE;
    }


    public static String getVersion(Context mContext) {
        PackageManager manager = mContext.getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(mContext.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "1.0.0";
        }

    }

    public static int VersionComparison(String versionServer, String versionLocal) {
        String version1 = versionServer;
        String version2 = versionLocal;
        if (version1 == null || version1.length() == 0 || version2 == null || version2.length() == 0)
            throw new IllegalArgumentException("Invalid parameter!");
        int index1 = 0;
        int index2 = 0;
        while (index1 < version1.length() && index2 < version2.length()) {
            int[] number1 = getValue(version1, index1);
            int[] number2 = getValue(version2, index2);

            if (number1[0] < number2[0]) {
                return -1;
            } else if (number1[0] > number2[0]) {
                return 1;
            } else {
                index1 = number1[1] + 1;
                index2 = number2[1] + 1;
            }
        }
        if (index1 == version1.length() && index2 == version2.length())
            return 0;
        if ((index1 < version1.length()))
            return 1;
        else
            return -1;
    }

    private static int[] getValue(String version, int index) {
        int[] value_index = new int[2];
        StringBuilder sb = new StringBuilder();
        while (index < version.length() && version.charAt(index) != '.') {
            sb.append(version.charAt(index));
            index++;
        }
        value_index[0] = Integer.parseInt(sb.toString());
        value_index[1] = index;
        return value_index;
    }

}
