package com.shanlin.android.autostore.presenter.Contract;

import com.shanlin.android.autostore.common.base.BaseActivity;
import com.shanlin.android.autostore.common.base.BasePresenter;
import com.shanlin.android.autostore.common.base.BaseView;
import com.shanlin.android.autostore.entity.respone.LoginBean;
import com.shanlin.android.autostore.entity.respone.WxUserInfoBean;
import com.shanlin.android.autostore.presenter.LoginPresenter;
import com.shanlin.android.autostore.entity.body.WechatLoginSendBean;
import com.shanlin.android.autostore.entity.respone.WxTokenBean;
import com.tencent.mm.opensdk.openapi.IWXAPI;

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

        void checkWxInstall(BaseActivity<LoginPresenter> view, IWXAPI api);


        void getResult(String code);

        void getUserInfo(String access_token, String openid);

        void postWxTokenLogin(WxUserInfoBean userInfoBean, WechatLoginSendBean sendBean);

        void postFaceLogin(String encode, String deviceId);
    }

}
