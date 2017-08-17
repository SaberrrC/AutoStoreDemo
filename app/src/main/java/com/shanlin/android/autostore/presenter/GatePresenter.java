package com.shanlin.android.autostore.presenter;

import com.shanlin.android.autostore.common.base.RxPresenter;
import com.shanlin.android.autostore.common.net.Api;
import com.shanlin.android.autostore.common.net.NetWorkUtil;
import com.shanlin.android.autostore.common.net.SubscriberWrapper;
import com.shanlin.android.autostore.entity.body.OpenGardBody;
import com.shanlin.android.autostore.entity.respone.CaptureBean;
import com.shanlin.android.autostore.presenter.Contract.GateContract;

import javax.inject.Inject;

/**
 * Created by cuieney on 17/08/2017.
 */

public class GatePresenter extends RxPresenter<GateContract.View> implements GateContract.Presenter {
    @Inject
    public GatePresenter(Api apiService) {
        super(apiService);
    }

    @Override
    public void postGardOpen(OpenGardBody body) {
        apiService.postGardOpen(body)
                .compose(NetWorkUtil.rxSchedulerHelper())
                .subscribe(new SubscriberWrapper<CaptureBean>(new SubscriberWrapper.CallBackListener<CaptureBean>() {
                    @Override
                    public void onSuccess(String code, CaptureBean data, String msg) {
                        mView.onGardOpenSuccess(code,data,msg);
                    }

                    @Override
                    public void onFailed(Throwable ex, String code, String msg) {
                        mView.onGardOpenFailed(ex,code,msg);
                    }
                }));
    }
}
