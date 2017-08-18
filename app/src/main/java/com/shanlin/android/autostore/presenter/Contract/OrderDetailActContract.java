package com.shanlin.android.autostore.presenter.Contract;

import com.shanlin.android.autostore.common.base.BasePresenter;
import com.shanlin.android.autostore.common.base.BaseView;
import com.shanlin.android.autostore.entity.respone.OrderDetailBean;
import com.shanlin.android.autostore.entity.respone.OrderHistoryBean;

/**
 * Created by dell、 on 2017/8/16.
 */

public interface OrderDetailActContract {
    interface View extends BaseView {
        void ongetOrderDetailSuccess(String code, OrderDetailBean data, String msg);

        void ongetOrderDetailFailed(Throwable ex, String code, String msg);
    }

    interface Presenter extends BasePresenter<OrderDetailActContract.View> {
        void getOrderDetail(OrderHistoryBean.DataBean.ListBean bean);
    }
}
