package com.shanlin.autostore.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.shanlin.autostore.R;

/**
 * Created by DELL on 2017/8/6 0006.
 */

public class ProgressDialog extends Dialog {

    private final View viewToFace;

    public ProgressDialog(Context context) {
        super(context, R.style.MyDialogCheckVersion);

        //点击其他地方消失
        setCanceledOnTouchOutside(false);
        //填充对话框的布局
        viewToFace = ((Activity)context).getLayoutInflater().inflate(R.layout
                        .data_loading,
                null,
                false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(viewToFace);
    }

//    private static Dialog getInstance (Context context) {
//
//        if (dialog == null) {
//            dialog = new Dialog(context, );
//            //点击其他地方消失
//            dialog.setCanceledOnTouchOutside(false);
//            //填充对话框的布局
//            View viewToFace = ((Activity)context).getLayoutInflater().inflate(R.layout
//                    .data_loading,
//                    null,
//                    false);
//            //将布局设置给
//            dialog.setContentView(viewToFace);
//            //获取当前Activity所在的窗体
//            Window dialogWindow = dialog.getWindow();
//            //设置Dialog从窗体中间弹出
//            dialogWindow.setGravity(Gravity.CENTER);
//            //获得窗体的属性
//            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
//            //        lp.y = 20;//设置Dialog距离底部的距离
//            //       将属性设置给窗体
//            dialogWindow.setAttributes(lp);
//        }
//
//        return dialog;
//    }

//    public void showLoadingDialog(Context context) {
//        Dialog instance = getInstance(context);
//        instance.show();//显示对话框
//    }
//
//    public static void dismissLoadingDialog(Context context) {
//        Dialog instance = getInstance(context);
//        instance.dismiss();//消失
//    }
}
