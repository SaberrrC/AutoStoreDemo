package com.shanlin.android.autostore.presenter.Contract;

import com.shanlin.android.autostore.common.base.BasePresenter;
import com.shanlin.android.autostore.common.base.BaseView;
import com.shanlin.android.autostore.entity.respone.PswSettingBean;
import com.shanlin.android.autostore.entity.respone.RealNameAuthenBean;
import com.shanlin.android.autostore.entity.respone.UserVertifyStatusBean;

/**
 * Created by dell、 on 2017/8/17.
 */

public interface OpenLMBActContract {

    interface View extends BaseView {

        void onRealAuthenSuccess(String code, RealNameAuthenBean data, String msg);

        void onRealNameAuthenFailed(Throwable ex, String code, String msg);

        void onPswSettingSuccess(String code, PswSettingBean data, String msg);

        void onPswSettingFailed(Throwable ex, String code, String msg);
    }

    interface Presenter extends BasePresenter<OpenLMBActContract.View> {

        void doRealNameAuthen(String name, String idNum);

        void doPasswordSetting(String psw2);
    }
}
