package com.shanlin.android.autostore.common.base;

import com.shanlin.android.autostore.common.net.Api;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by cuieney on 14/08/2017.
 */

public class RxPresenter<T extends BaseView> implements BasePresenter<T> {

    protected Api apiService;

    public RxPresenter(Api apiService) {
        this.apiService = apiService;
    }

    protected T mView;

    protected CompositeDisposable mCompositeDisposable;


    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    protected void addSubscrebe(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void attachView(T view) {
        WeakReference<T> tSoftReference = new WeakReference<>(view);
        this.mView = tSoftReference.get();
    }

    @Override
    public void detachView() {
        this.mView = null;
        unSubscribe();
    }
}
