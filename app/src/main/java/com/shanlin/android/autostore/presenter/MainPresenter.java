package com.shanlin.android.autostore.presenter;

import com.shanlin.android.autostore.common.base.RxPresenter;
import com.shanlin.android.autostore.common.net.Api;
import com.shanlin.android.autostore.common.net.NetWorkUtil;
import com.shanlin.android.autostore.common.net.SubscriberWrapper;
import com.shanlin.android.autostore.entity.body.MemberUpdateSendBean;
import com.shanlin.android.autostore.entity.respone.CreditBalanceCheckBean;
import com.shanlin.android.autostore.entity.respone.LogoutBean;
import com.shanlin.android.autostore.entity.respone.MemberUpdateBean;
import com.shanlin.android.autostore.entity.respone.PersonInfoBean;
import com.shanlin.android.autostore.entity.respone.RefundMoneyBean;
import com.shanlin.android.autostore.entity.respone.UserNumEverydayBean;
import com.shanlin.android.autostore.entity.respone.UserVertifyStatusBean;
import com.shanlin.android.autostore.presenter.Contract.MainActContract;

import javax.inject.Inject;

/**
 * Created by cuieney on 15/08/2017.
 */

public class MainPresenter extends RxPresenter<MainActContract.View> implements MainActContract.Presenter {

    @Inject
    public MainPresenter(Api apiService) {
        super(apiService);
    }


    @Override
    public void postMemberUpdate(MemberUpdateSendBean memberUpdateSendBean) {
        apiService.postMemberUpdate(memberUpdateSendBean)
                .compose(NetWorkUtil.<MemberUpdateBean>rxSchedulerHelper())
                .subscribe(new SubscriberWrapper<MemberUpdateBean>(new SubscriberWrapper.CallBackListener<MemberUpdateBean>() {
                    @Override
                    public void onSuccess(String code, MemberUpdateBean data, String msg) {

                    }

                    @Override
                    public void onFailed(Throwable ex, String code, String msg) {

                    }
                }));
    }

    @Override
    public void loginOut() {
        apiService.logout()
                .compose(NetWorkUtil.rxSchedulerHelper())
                .subscribe(new SubscriberWrapper<LogoutBean>(new SubscriberWrapper.CallBackListener<LogoutBean>() {
                    @Override
                    public void onSuccess(String code, LogoutBean data, String msg) {
                        mView.onLogoutSuccess(code, data, msg);
                    }

                    @Override
                    public void onFailed(Throwable ex, String code, String msg) {
                        mView.onLogoutFailed(ex, code, msg);
                    }
                }));
    }

    @Override
    public void getRefundMoney() {
        apiService.getRefundMoney()
                .compose(NetWorkUtil.rxSchedulerHelper())
                .subscribe(new SubscriberWrapper<RefundMoneyBean>(new SubscriberWrapper.CallBackListener<RefundMoneyBean>() {
                    @Override
                    public void onSuccess(String code, RefundMoneyBean data, String msg) {
                        mView.onGetRefundMoneySuccess(code,data,msg);
                    }

                    @Override
                    public void onFailed(Throwable ex, String code, String msg) {
                        mView.onGetRefundMoneyFailed(ex,code,msg);
                    }
                }));
    }

    @Override
    public void getUserNumEveryday(String date, String storeId) {
    apiService.getUserNumEveryday(date,storeId)
            .compose(NetWorkUtil.rxSchedulerHelper())
            .subscribe(new SubscriberWrapper<UserNumEverydayBean>(new SubscriberWrapper.CallBackListener<UserNumEverydayBean>() {
                @Override
                public void onSuccess(String code, UserNumEverydayBean data, String msg) {
                    mView.onGetUserNumEverydaySuccess(code,data,msg);
                }

                @Override
                public void onFailed(Throwable ex, String code, String msg) {
                    mView.onGetUserNumEverydayFailed(ex,code,msg);
                }
            }));
    }

    @Override
    public void getUserCreditBalanceInfo() {
        apiService.getUserCreditBalanceInfo()
                .compose(NetWorkUtil.rxSchedulerHelper())
                .subscribe(new SubscriberWrapper<CreditBalanceCheckBean>(new SubscriberWrapper.CallBackListener<CreditBalanceCheckBean>() {
                    @Override
                    public void onSuccess(String code, CreditBalanceCheckBean data, String msg) {
                        mView.onGetUserCreditBalanceInfoSuccess(code,data,msg);
                    }

                    @Override
                    public void onFailed(Throwable ex, String code, String msg) {
                        mView.onGetUserCreditBalanceInfoFailed(ex,code,msg);
                    }
                }));
    }

    @Override
    public void getPersonInfo() {
        apiService.getPersonInfo()
                .compose(NetWorkUtil.rxSchedulerHelper())
                .subscribe(new SubscriberWrapper<PersonInfoBean>(new SubscriberWrapper.CallBackListener<PersonInfoBean>() {
                    @Override
                    public void onSuccess(String code, PersonInfoBean data, String msg) {
                        mView.onGetPersonInfoSuccess(code,data,msg);
                    }

                    @Override
                    public void onFailed(Throwable ex, String code, String msg) {
                        mView.onGetPersonInfoFailed(ex,code,msg);
                    }
                }));
    }

    @Override
    public void getUserVertifyAuthenStatus() {
        apiService.getUserVertifyAuthenStatus()
                .compose(NetWorkUtil.rxSchedulerHelper())
                .subscribe(new SubscriberWrapper<UserVertifyStatusBean>(new SubscriberWrapper.CallBackListener<UserVertifyStatusBean>() {
                    @Override
                    public void onSuccess(String code, UserVertifyStatusBean data, String msg) {
                        mView.onGetUserVertifyAuthenStatusSuccess(code,data,msg);
                    }

                    @Override
                    public void onFailed(Throwable ex, String code, String msg) {
                        mView.onGetUserVertifyAuthenStatusFailed(ex,code,msg);
                    }
                }));
    }
}
