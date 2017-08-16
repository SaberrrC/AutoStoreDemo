package com.shanlin.android.autostore.presenter;

import com.shanlin.android.autostore.common.base.RxPresenter;
import com.shanlin.android.autostore.common.net.Api;
import com.shanlin.android.autostore.common.net.NetWorkUtil;
import com.shanlin.android.autostore.common.net.SubscriberWrapper;
import com.shanlin.android.autostore.presenter.Contract.BuyRecordActContract;
import com.shanlin.autostore.bean.resultBean.OrderHistoryBean;

import javax.inject.Inject;

/**
 * Created by dell、 on 2017/8/16.
 */

public class BuyRecordPresenter extends RxPresenter<BuyRecordActContract.View> implements BuyRecordActContract.Presenter {

    @Inject
    public BuyRecordPresenter(Api apiService) {
        super(apiService);
    }

    @Override
    public void getOrderData(String token, int pageno, int pageSize) {
        apiService.getOrderHistory(token, pageno, pageSize).compose(NetWorkUtil.rxSchedulerHelper()).subscribe(new SubscriberWrapper<>(new SubscriberWrapper.CallBackListener<OrderHistoryBean>() {
            @Override
            public void onSuccess(String code, OrderHistoryBean data, String msg) {
                mView.ongetOrderDataSuccess(code, data, msg);
            }

            @Override
            public void onFailed(Throwable ex, String code, String msg) {
                mView.ongetOrderDataFailed(ex, code, msg);
            }
        }, false));
    }
}
