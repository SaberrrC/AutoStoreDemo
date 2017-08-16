package com.shanlin.android.autostore.presenter.Contract;

import com.shanlin.android.autostore.common.base.BasePresenter;
import com.shanlin.android.autostore.common.base.BaseView;
import com.shanlin.android.autostore.entity.respone.RefundMoneyBean;

/**
 * Created by dell、 on 2017/8/15.
 */

public interface RefundMoneyActContract {

    interface View extends BaseView {
        void onGetInfoSuccess(String code, RefundMoneyBean data, String msg);

        void onGetInfoFailed(Throwable ex, String code, String msg);
    }

    interface Presenter extends BasePresenter<RefundMoneyActContract.View> {

        void getRefoundMoneyInfo();
    }
}
