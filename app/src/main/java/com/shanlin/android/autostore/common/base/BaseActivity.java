package com.shanlin.android.autostore.common.base;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.shanlin.android.autostore.App;
import com.shanlin.android.autostore.common.utils.MPermissionUtils;
import com.shanlin.android.autostore.di.component.ActivityComponent;
import com.shanlin.android.autostore.di.component.DaggerActivityComponent;
import com.shanlin.android.autostore.di.module.ActivityModule;
import com.shanlin.autostore.R;
import com.shanlin.android.autostore.common.utils.StatusBarUtils;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by cuieney on 14/08/2017.
 */

public abstract class BaseActivity<T extends BasePresenter> extends AutoLayoutActivity implements BaseView{
    @Inject
    protected T mPresenter;
    protected Activity mContext;
    private Unbinder mUnBinder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        mUnBinder = ButterKnife.bind(this);
        mContext = this;
        initInject();
        addActivity(this);
        StatusBarUtils.setColor(this, Color.TRANSPARENT);
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        initData();
    }

    protected abstract void initInject();


    protected ActivityComponent getActivityComponent(){
        return  DaggerActivityComponent.builder()
                .appComponent(App.getInstance().getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }

    protected ActivityModule getActivityModule(){
        return new ActivityModule(this);
    }

    /**
     * 初始化布局
     *
     */

    public abstract int initLayout();


    /**
     * 初始化数据
     */
    public abstract void initData();



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
        mUnBinder.unbind();
    }


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
}
