package com.shanlin.android.autostore.presenter.Contract;

import com.shanlin.android.autostore.common.base.BasePresenter;
import com.shanlin.android.autostore.common.base.BaseView;
import com.shanlin.android.autostore.entity.body.WechatSaveMobileBody;
import com.shanlin.android.autostore.entity.respone.CodeBean;
import com.shanlin.android.autostore.entity.respone.LoginBean;

/**
 * Created by dell、 on 2017/8/15.
 */

public interface PhoneLoginActContract {

    interface View extends BaseView {
        void onMsgCodeSuccess(String code, CodeBean data, String msg);

        void onPhoneLoginSuccess(String code, LoginBean data, String msg);

        void onMsgCodeFailed(Throwable ex, String code, String msg);

        void onPhoneLoginFailed(Throwable ex, String code, String msg);

    }

    interface Presenter extends BasePresenter<PhoneLoginActContract.View> {
        void getMsgCode(String phone);

        void doPhoneLogin(String phone,String msgCode);

        void doWXLogin(WechatSaveMobileBody body);

        void getUserAuthenStatus(String token);
    }
}
