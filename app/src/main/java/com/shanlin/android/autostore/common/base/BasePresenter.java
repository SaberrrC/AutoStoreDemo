package com.shanlin.android.autostore.common.base;

/**
 * Created by cuieney on 14/08/2017.
 */

public interface BasePresenter<T extends BaseView> {
    void attachView(T view);

    void detachView();
}
