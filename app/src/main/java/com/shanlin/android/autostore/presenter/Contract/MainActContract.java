package com.shanlin.android.autostore.presenter.Contract;

import com.shanlin.android.autostore.common.base.BasePresenter;
import com.shanlin.android.autostore.common.base.BaseView;
import com.shanlin.android.autostore.entity.body.MemberUpdateSendBean;
import com.shanlin.android.autostore.entity.respone.CreditBalanceCheckBean;
import com.shanlin.android.autostore.entity.respone.LogoutBean;
import com.shanlin.android.autostore.entity.respone.PersonInfoBean;
import com.shanlin.android.autostore.entity.respone.RefundMoneyBean;
import com.shanlin.android.autostore.entity.respone.UserNumEverydayBean;
import com.shanlin.android.autostore.entity.respone.UserVertifyStatusBean;

/**
 * Created by cuieney on 15/08/2017.
 */

public interface MainActContract {
    interface View extends BaseView {
        void onLogoutSuccess(String code, LogoutBean data, String msg);

        void onLogoutFailed(Throwable ex, String code, String msg);

        void onGetRefundMoneySuccess(String code, RefundMoneyBean data, String msg);

        void onGetRefundMoneyFailed(Throwable ex, String code, String msg);

        void onGetUserNumEverydaySuccess(String code, UserNumEverydayBean data, String msg);

        void onGetUserNumEverydayFailed(Throwable ex, String code, String msg);

        void onGetUserCreditBalanceInfoSuccess(String code, CreditBalanceCheckBean data, String msg);

        void onGetUserCreditBalanceInfoFailed(Throwable ex, String code, String msg);

        void onGetPersonInfoSuccess(String code, PersonInfoBean data, String msg);

        void onGetPersonInfoFailed(Throwable ex, String code, String msg);

        void onGetUserVertifyAuthenStatusSuccess(String code, UserVertifyStatusBean data, String msg);

        void onGetUserVertifyAuthenStatusFailed(Throwable ex, String code, String msg);

    }

    interface Presenter extends BasePresenter<MainActContract.View> {
        void postMemberUpdate(MemberUpdateSendBean memberUpdateSendBean);

        void loginOut();

        void getRefundMoney();

        void getUserNumEveryday(String date, String storeId);

        void getUserCreditBalanceInfo();

        void getPersonInfo();

        void getUserVertifyAuthenStatus();
    }

}
