package com.shanlin.android.autostore.presenter.Contract;

import com.shanlin.android.autostore.common.base.BaseActivity;
import com.shanlin.android.autostore.common.base.BasePresenter;
import com.shanlin.android.autostore.common.base.BaseView;
import com.shanlin.android.autostore.presenter.LoginPresenterImpl;
import com.shanlin.autostore.bean.LoginBean;
import com.shanlin.autostore.bean.paramsBean.WechatLoginSendBean;
import com.shanlin.autostore.bean.resultBean.WxTokenBean;
import com.shanlin.autostore.bean.resultBean.WxUserInfoBean;

/**
 * Created by dell、 on 2017/8/15.
 */

public interface LoginActContract {
    interface View extends BaseView {

        void onWxInstallCheckSuccess(boolean isWxInstalled);

        void onWxResultSuccess(WxTokenBean wxTokenBean);

        void onWxResultFailed(Throwable t);

        void onWxUserInfoFailed(Throwable t);

        void onWxUserInfoSuccess(WxUserInfoBean userInfoBean, WechatLoginSendBean wxUserInfoBean);

        void onWxTokenLoginSuccess(String code, LoginBean data, String msg, WxUserInfoBean userInfoBean);

        void onWxTokenLoginFailed(Throwable ex, String code, String msg, WxUserInfoBean userInfoBean);

        void onFaceLoginFailed(Throwable ex, String code, String msg);

        void onFaceLoginSuccess(String code, LoginBean data, String msg);


    }

    interface Presenter extends BasePresenter<View> {
        void getWxInfo();

        void checkWxInstall(BaseActivity<LoginPresenterImpl> view);


        void getResult(String code);

        void getUserInfo(String access_token, String openid);

        void postWxTokenLogin(WxUserInfoBean userInfoBean, WechatLoginSendBean sendBean);

        void postFaceLogin(String encode, String deviceId);
    }

}
