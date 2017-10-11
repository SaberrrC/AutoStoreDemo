package com.shanlin.android.autostore.presenter;

import com.shanlin.android.autostore.common.base.RxPresenter;
import com.shanlin.android.autostore.common.net.Api;
import com.shanlin.android.autostore.common.net.NetWorkUtil;
import com.shanlin.android.autostore.common.net.SubscriberWrapper;
import com.shanlin.android.autostore.entity.respone.CheckUpdateBean;
import com.shanlin.android.autostore.entity.respone.PersonInfoBean;
import com.shanlin.android.autostore.presenter.Contract.SplashActContract;

import javax.inject.Inject;

/**
 * Created by cuieney on 14/08/2017.
 */

public class SplashPresenter extends RxPresenter<SplashActContract.View> implements SplashActContract.Presenter {


    @Inject
    public SplashPresenter(Api apiService) {
        super(apiService);
    }
    @Override
    public void checkToken() {
        apiService.getPersonInfo()
                .compose(NetWorkUtil.<PersonInfoBean>rxSchedulerHelper())
                .subscribe(new SubscriberWrapper<>(new SubscriberWrapper.CallBackListener<PersonInfoBean>() {
                    @Override
                    public void onSuccess(String code, PersonInfoBean data, String msg) {
                        mView.onTokenSuccess(code,data,msg);
                    }

                    @Override
                    public void onFailed(Throwable ex, String code, String msg) {
                        mView.onTokenFailed(ex,code,msg);
                    }
                }));
    }

    @Override
    public void checkUpgrade(int type) {
        apiService.doGetCheckUpdate(type)
                .compose(NetWorkUtil.<CheckUpdateBean>rxSchedulerHelper())
                .subscribe(new SubscriberWrapper<CheckUpdateBean>(new SubscriberWrapper.CallBackListener<CheckUpdateBean>() {
                    @Override
                    public void onSuccess(String code, CheckUpdateBean data, String msg) {
                        mView.onUpgradeSuccess(code,data,msg);
                    }

                    @Override
                    public void onFailed(Throwable ex, String code, String msg) {
                        mView.onUpgradeFailed(ex,code,msg);
                    }
                }));
    }
}
