package com.shanlin.android.autostore.presenter;

import com.shanlin.android.autostore.common.base.RxPresenter;
import com.shanlin.android.autostore.common.net.Api;
import com.shanlin.android.autostore.common.net.NetWorkUtil;
import com.shanlin.android.autostore.common.net.SubscriberWrapper;
import com.shanlin.android.autostore.entity.respone.RefundMoneyBean;
import com.shanlin.android.autostore.presenter.Contract.RefundMoneyActContract;

import javax.inject.Inject;

/**
 * Created by dell、 on 2017/8/16.
 */

public class RefundMoneyPresenter extends RxPresenter<RefundMoneyActContract.View> implements RefundMoneyActContract.Presenter{

    @Inject
    public RefundMoneyPresenter(Api apiService) {
        super(apiService);
    }

    @Override
    public void getRefoundMoneyInfo() {
        apiService.getRefundMoney()
                .compose(NetWorkUtil.<RefundMoneyBean>rxSchedulerHelper())
                .subscribe(new SubscriberWrapper<>(new SubscriberWrapper.CallBackListener<RefundMoneyBean>() {
                    @Override
                    public void onSuccess(String code, RefundMoneyBean data, String msg) {
                        mView.onGetInfoSuccess(code,data,msg);
                    }

                    @Override
                    public void onFailed(Throwable ex, String code, String msg) {
                        mView.onGetInfoFailed(ex,code,msg);
                    }
                }));

    }
}
