package com.shanlin.android.autostore.presenter;

import com.shanlin.android.autostore.common.base.RxPresenter;
import com.shanlin.android.autostore.common.net.Api;
import com.shanlin.android.autostore.common.net.NetWorkUtil;
import com.shanlin.android.autostore.common.net.SubscriberWrapper;
import com.shanlin.android.autostore.presenter.Contract.OrderDetailActContract;
import com.shanlin.android.autostore.entity.respone.OrderDetailBean;
import com.shanlin.android.autostore.entity.respone.OrderHistoryBean;

import javax.inject.Inject;

/**
 * Created by dell、 on 2017/8/16.
 */

public class OrderDetailPresenter extends RxPresenter<OrderDetailActContract.View> implements OrderDetailActContract.Presenter {

    @Inject
    public OrderDetailPresenter(Api apiService) {
        super(apiService);
    }

    @Override
    public void getOrderDetail(OrderHistoryBean.DataBean.ListBean mBean) {
        apiService.getOrderDetail(mBean.getOrderNo()).compose(NetWorkUtil.rxSchedulerHelper()).subscribe(new SubscriberWrapper<>(new SubscriberWrapper.CallBackListener<OrderDetailBean>() {
            @Override
            public void onSuccess(String code, OrderDetailBean data, String msg) {
                mView.ongetOrderDetailSuccess(code, data, msg);
            }

            @Override
            public void onFailed(Throwable ex, String code, String msg) {
                mView.ongetOrderDetailFailed(ex, code, msg);
            }
        }, false));
    }
}