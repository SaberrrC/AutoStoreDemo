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
}
