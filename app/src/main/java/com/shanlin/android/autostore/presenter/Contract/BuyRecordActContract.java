package com.shanlin.android.autostore.presenter.Contract;

import com.shanlin.android.autostore.common.base.BasePresenter;
import com.shanlin.android.autostore.common.base.BaseView;
import com.shanlin.autostore.bean.resultBean.OrderHistoryBean;

/**
 * Created by dell、 on 2017/8/16.
 */

public interface BuyRecordActContract {
    interface View extends BaseView {

        void ongetOrderDataSuccess(String code, OrderHistoryBean data, String msg);

        void ongetOrderDataFailed(Throwable ex, String code, String msg);
    }

    interface Presenter extends BasePresenter<BuyRecordActContract.View> {

        void getOrderData(int pageno, int pageSize);
    }
}
