package com.shanlin.android.autostore.presenter;

import android.widget.Toast;
import com.shanlin.android.autostore.App;
import com.shanlin.android.autostore.common.base.RxPresenter;
import com.shanlin.android.autostore.common.net.Api;
import com.shanlin.android.autostore.common.net.NetWorkUtil;
import com.shanlin.android.autostore.common.net.SubscriberWrapper;
import com.shanlin.android.autostore.common.utils.SpUtils;
import com.shanlin.android.autostore.entity.body.CodeSendBean;
import com.shanlin.android.autostore.entity.body.NumberLoginBean;
import com.shanlin.android.autostore.entity.body.WechatSaveMobileBody;
import com.shanlin.android.autostore.entity.respone.CodeBean;
import com.shanlin.android.autostore.entity.respone.LoginBean;
import com.shanlin.android.autostore.entity.respone.UserVertifyStatusBean;
import com.shanlin.android.autostore.presenter.Contract.PhoneLoginActContract;
import com.shanlin.autostore.constants.Constant_LeMaiBao;

import javax.inject.Inject;

/**
 * Created by dell、 on 2017/8/15.
 */

public class PhoneLoginPresenter extends RxPresenter<PhoneLoginActContract.View> implements PhoneLoginActContract.Presenter {

    @Inject
    public PhoneLoginPresenter(Api apiService) {
        super(apiService);
    }

    @Override
    public void getMsgCode(String phone) {
        apiService.postVerificationCode(new CodeSendBean(phone))
                .compose(NetWorkUtil.<CodeBean>rxSchedulerHelper())
                .subscribe(new SubscriberWrapper<CodeBean>(new SubscriberWrapper.CallBackListener<CodeBean>() {
                    @Override
                    public void onSuccess(String code, CodeBean data, String msg) {
                        mView.onMsgCodeSuccess(code,data,msg);
                    }

                    @Override
                    public void onFailed(Throwable ex, String code, String msg) {
                        mView.onMsgCodeFailed(ex,code,msg);
                    }
                }));
    }

    @Override
    public void doPhoneLogin(String phone, String msgCode) {
        apiService.postNumCodeLogin(new NumberLoginBean(phone,msgCode))
                .compose(NetWorkUtil.<LoginBean>rxSchedulerHelper())
                .subscribe(new SubscriberWrapper<LoginBean>(new SubscriberWrapper.CallBackListener<LoginBean>() {
                    @Override
                    public void onSuccess(String code, LoginBean data, String msg) {
                        mView.onPhoneLoginSuccess(code,data,msg);
                    }

                    @Override
                    public void onFailed(Throwable ex, String code, String msg) {
                        mView.onPhoneLoginFailed(ex,code,msg);
                    }
                }));
        }

    @Override
    public void doWXLogin(WechatSaveMobileBody body) {
        apiService.postWechatSavemobile(body)
                .compose(NetWorkUtil.<LoginBean>rxSchedulerHelper())
                .subscribe(new SubscriberWrapper<LoginBean>(new SubscriberWrapper.CallBackListener<LoginBean>() {
                    @Override
                    public void onSuccess(String code, LoginBean data, String msg) {
                        mView.onPhoneLoginSuccess(code,data,msg);
                    }

                    @Override
                    public void onFailed(Throwable ex, String code, String msg) {
                        mView.onPhoneLoginFailed(ex,code,msg);
                    }
                }));
        }

    @Override
    public void getUserAuthenStatus(String token) {
        apiService.getUserVertifyAuthenStatus(token)
                .compose(NetWorkUtil.<UserVertifyStatusBean>rxSchedulerHelper())
                .subscribe(new SubscriberWrapper<UserVertifyStatusBean>(new SubscriberWrapper.CallBackListener<UserVertifyStatusBean>() {
                    @Override
                    public void onSuccess(String code, UserVertifyStatusBean data, String msg) {
                        String status = data.getData().getVerifyStatus();
                        SpUtils.saveString(App.getInstance(), Constant_LeMaiBao.AUTHEN_STATE_KEY, status);
                    }

                    @Override
                    public void onFailed(Throwable ex, String code, String msg) {
                        Toast.makeText(App.getInstance(), "获取信息失败", Toast.LENGTH_SHORT).show();
                    }
                }));
    }


}
