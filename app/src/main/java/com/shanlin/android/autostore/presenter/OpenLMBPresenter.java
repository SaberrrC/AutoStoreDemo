package com.shanlin.android.autostore.presenter;

import com.shanlin.android.autostore.common.base.RxPresenter;
import com.shanlin.android.autostore.common.net.Api;
import com.shanlin.android.autostore.common.net.NetWorkUtil;
import com.shanlin.android.autostore.common.net.SubscriberWrapper;
import com.shanlin.android.autostore.entity.body.PswSettingBody;
import com.shanlin.android.autostore.entity.body.RealNameAuthenBody;
import com.shanlin.android.autostore.entity.respone.PswSettingBean;
import com.shanlin.android.autostore.entity.respone.RealNameAuthenBean;
import com.shanlin.android.autostore.entity.respone.UserVertifyStatusBean;
import com.shanlin.android.autostore.presenter.Contract.OpenLMBActContract;

import javax.inject.Inject;

/**
 * Created by dell、 on 2017/8/17.
 */

public class OpenLMBPresenter extends RxPresenter<OpenLMBActContract.View> implements OpenLMBActContract.Presenter {

    @Inject
    public OpenLMBPresenter(Api apiService) {
        super(apiService);
    }

    @Override
    public void doRealNameAuthen(String name, String idNum) {
        apiService.goRealNameAuthen(new RealNameAuthenBody(idNum,name))
                .compose(NetWorkUtil.rxSchedulerHelper())
                .subscribe(new SubscriberWrapper<RealNameAuthenBean>(new SubscriberWrapper.CallBackListener<RealNameAuthenBean>() {
                    @Override
                    public void onSuccess(String code, RealNameAuthenBean data, String msg) {
                        mView.onRealAuthenSuccess(code,data,msg);
                    }

                    @Override
                    public void onFailed(Throwable ex, String code, String msg) {
                        mView.onRealNameAuthenFailed(ex,code,msg);
                    }
                }));
    }

    @Override
    public void doPasswordSetting(String psw2) {
        apiService.goPswSetting(new PswSettingBody(psw2))
                .compose(NetWorkUtil.rxSchedulerHelper())
                .subscribe(new SubscriberWrapper<PswSettingBean>(new SubscriberWrapper.CallBackListener<PswSettingBean>() {
                    @Override
                    public void onSuccess(String code, PswSettingBean data, String msg) {
                        mView.onPswSettingSuccess(code,data,msg);
                    }

                    @Override
                    public void onFailed(Throwable ex, String code, String msg) {
                        mView.onPswSettingFailed(ex,code,msg);
                    }
                }));
    }
}
