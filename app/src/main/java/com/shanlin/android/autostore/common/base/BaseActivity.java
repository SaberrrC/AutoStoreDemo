package com.shanlin.android.autostore.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.shanlin.android.autostore.App;
import com.shanlin.android.autostore.di.component.ActivityComponent;
import com.shanlin.android.autostore.di.component.DaggerActivityComponent;
import com.shanlin.android.autostore.di.module.ActivityModule;

import javax.inject.Inject;


/**
 * Created by cuieney on 14/08/2017.
 */

public abstract class BaseActivity<T extends BasePresenter> extends SimpleActivity implements BaseView{
    @Inject
    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        initInject();
        if (mPresenter != null)
            mPresenter.attachView(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }
}
