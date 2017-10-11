package com.shanlin.android.autostore.common.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.shanlin.autostore.R;

/**
 * Created by cuieney on 17/08/2017.
 */

public class PopupUtils {
    public static PopupWindow getTopPopup(Context context,View view){
        PopupWindow popTop = new PopupWindow(context);
        popTop.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popTop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popTop.setContentView(view);
        popTop.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popTop.setOutsideTouchable(false);
        popTop.setFocusable(false);
        popTop.setAnimationStyle(R.style.popStyle);
        return popTop;
    }

    public static PopupWindow getBottomPopup(Context context,View view){
        PopupWindow popBottom = new PopupWindow(context);
        popBottom.setAnimationStyle(R.style.popStyle);
        popBottom.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popBottom.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popBottom.setContentView(view);
        popBottom.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popBottom.setOutsideTouchable(false);
        popBottom.setFocusable(false);
        return popBottom;
    }
}
