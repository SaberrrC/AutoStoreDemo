package com.shanlin.autostore.base;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.shanlin.autostore.R;
import com.shanlin.android.autostore.common.utils.MPermissionUtils;
import com.shanlin.autostore.utils.StatusBarUtils;

import java.util.ArrayList;

/**
 * Created by DELL on 2017/7/14 0014.
 */

public abstract class BaseActivity extends SoftInputBaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        addActivity(this);
        StatusBarUtils.setColor(this, Color.TRANSPARENT);
        initView();
        initData();
    }

    private static ArrayList<Activity> listOfActivity = new ArrayList<>();

    public void addActivity(Activity a) {
        listOfActivity.add(a);
    }

    public void killActivity(Class<?> cls) {
        for (int i = 0; i < listOfActivity.size(); i++) {
            Activity activity = listOfActivity.get(i);
            if (activity.getClass().equals(cls)) {
                activity.finish();
            }
        }
    }

    /**
     * 初始化布局
     *
     * @return
     */

    public abstract int initLayout();

    /**
     * 初始化控件
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public boolean transAnim = true;

    public void setTransAnim(boolean transAnim) {
        this.transAnim = transAnim;
    }

    @Override
    public void finish() {
        super.finish();
        if (transAnim) {
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
    }
}
