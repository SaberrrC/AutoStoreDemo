package com.shanlin.android.autostore.presenter.Contract;

import com.shanlin.android.autostore.common.base.BasePresenter;
import com.shanlin.android.autostore.common.base.BaseView;
import com.shanlin.autostore.bean.resultBean.OrderDetailBean;
import com.shanlin.autostore.bean.resultBean.OrderHistoryBean;

/**
 * Created by dell、 on 2017/8/16.
 */

public interface OrderDetailActContract {
    interface View extends BaseView {
        void ongetOrderDetailSuccess(String code, OrderDetailBean data, String msg);

        void ongetOrderDetailFailed(Throwable ex, String code, String msg);
    }

    interface Presenter extends BasePresenter<OrderDetailActContract.View> {
        void getOrderDetail(String token, OrderHistoryBean.DataBean.ListBean bean);
    }
}
