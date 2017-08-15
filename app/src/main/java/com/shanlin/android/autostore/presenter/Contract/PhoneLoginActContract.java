package com.shanlin.android.autostore.presenter.Contract;

import com.shanlin.android.autostore.common.base.BasePresenter;
import com.shanlin.android.autostore.common.base.BaseView;
import com.shanlin.autostore.bean.LoginBean;
import com.shanlin.autostore.bean.paramsBean.WechatSaveMobileBody;
import com.shanlin.autostore.bean.resultBean.CodeBean;

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
