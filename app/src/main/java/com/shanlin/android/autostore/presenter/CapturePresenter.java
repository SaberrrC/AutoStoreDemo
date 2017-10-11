package com.shanlin.android.autostore.presenter;

import com.shanlin.android.autostore.common.base.RxPresenter;
import com.shanlin.android.autostore.common.net.Api;
import com.shanlin.android.autostore.common.net.NetWorkUtil;
import com.shanlin.android.autostore.common.net.SubscriberWrapper;
import com.shanlin.android.autostore.entity.body.RealOrderBody;
import com.shanlin.android.autostore.entity.respone.RealOrderBean;
import com.shanlin.android.autostore.presenter.Contract.CaptureContract;

import javax.inject.Inject;

/**
 * Created by cuieney on 16/08/2017.
 */

public class CapturePresenter extends RxPresenter<CaptureContract.View> implements CaptureContract.Presenter {
    @Inject
    public CapturePresenter(Api apiService) {
        super(apiService);
    }

    @Override
    public void updateTempToReal(RealOrderBody body) {
        apiService.updateTempToReal(body)
                .compose(NetWorkUtil.rxSchedulerHelper())
                .subscribe(new SubscriberWrapper<RealOrderBean>(new SubscriberWrapper.CallBackListener<RealOrderBean>() {
                    @Override
                    public void onSuccess(String code, RealOrderBean data, String msg) {
                        mView.onUpdateTempToRealSuccess(code,data,msg);
                    }

                    @Override
                    public void onFailed(Throwable ex, String code, String msg) {
                        mView.onUpdateTempToRealFailed(ex,code,msg);
                    }
                }));
    }
}
