package com.shanlin.android.autostore.presenter;

import com.shanlin.android.autostore.common.base.RxPresenter;
import com.shanlin.android.autostore.common.net.Api;
import com.shanlin.android.autostore.common.net.NetWorkUtil;
import com.shanlin.android.autostore.common.net.SubscriberWrapper;
import com.shanlin.android.autostore.entity.body.AliPayOrderBody;
import com.shanlin.android.autostore.entity.body.LeMaiBaoPayBody;
import com.shanlin.android.autostore.entity.body.WXPayBody;
import com.shanlin.android.autostore.entity.respone.AliPayResultBean;
import com.shanlin.android.autostore.entity.respone.CreditBalanceCheckBean;
import com.shanlin.android.autostore.entity.respone.LeMaiBaoPayResultBean;
import com.shanlin.android.autostore.entity.respone.UserVertifyStatusBean;
import com.shanlin.android.autostore.entity.respone.WxChatBean;
import com.shanlin.android.autostore.presenter.Contract.PayWayContract;

import javax.inject.Inject;

/**
 * Created by cuieney on 17/08/2017.
 */

public class PayWayPresenter extends RxPresenter<PayWayContract.View> implements PayWayContract.Presenter {

    @Inject
    public PayWayPresenter(Api apiService) {
        super(apiService);
    }

    @Override
    public void lemaibaoPay(LeMaiBaoPayBody body) {
        apiService.lemaibaoPay(body)
                .compose(NetWorkUtil.rxSchedulerHelper())
                .subscribe(new SubscriberWrapper<LeMaiBaoPayResultBean>(new SubscriberWrapper.CallBackListener<LeMaiBaoPayResultBean>() {
                    @Override
                    public void onSuccess(String code, LeMaiBaoPayResultBean data, String msg) {
                        mView.onLMbPaySuccess(code,data,msg);
                    }

                    @Override
                    public void onFailed(Throwable ex, String code, String msg) {
                        mView.onFailed(ex,code,msg);
                    }
                }));
    }

    @Override
    public void createAliPreOrder(AliPayOrderBody body) {
        apiService.createAliPreOrder(body)
                .compose(NetWorkUtil.rxSchedulerHelper())
                .subscribe(new SubscriberWrapper<AliPayResultBean>(new SubscriberWrapper.CallBackListener<AliPayResultBean>() {
                    @Override
                    public void onSuccess(String code, AliPayResultBean data, String msg) {
                        mView.onAliPaySuccess(code, data, msg);
                    }

                    @Override
                    public void onFailed(Throwable ex, String code, String msg) {
                        mView.onFailed(ex,code,msg);
                    }
                }));
    }

    @Override
    public void postWxRequest(WXPayBody body) {
        apiService.postWxRequest(body)
                .compose(NetWorkUtil.rxSchedulerHelper())
                .subscribe(new SubscriberWrapper<WxChatBean>(new SubscriberWrapper.CallBackListener<WxChatBean>() {
                    @Override
                    public void onSuccess(String code, WxChatBean data, String msg) {
                        mView.onWechatPaySuccess(code, data, msg);
                    }

                    @Override
                    public void onFailed(Throwable ex, String code, String msg) {
                        mView.onFailed(ex,code,msg);
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
                        mView.onUserVertifyAuthenStatusSuccess(code, data, msg);
                    }

                    @Override
                    public void onFailed(Throwable ex, String code, String msg) {
                        mView.onFailed(ex,code,msg);
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
                        mView.onUserCreditBalanceInfoSuccess(code, data, msg);
                    }

                    @Override
                    public void onFailed(Throwable ex, String code, String msg) {
                        mView.onFailed(ex,code,msg);
                    }
                }));
    }
}
