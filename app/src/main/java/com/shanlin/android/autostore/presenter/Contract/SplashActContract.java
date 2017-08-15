package com.shanlin.android.autostore.presenter.Contract;

import com.shanlin.android.autostore.common.base.BasePresenter;
import com.shanlin.android.autostore.common.base.BaseView;
import com.shanlin.android.autostore.entity.respone.CheckUpdateBean;
import com.shanlin.android.autostore.entity.respone.PersonInfoBean;

/**
 * Created by cuieney on 14/08/2017.
 */

public interface SplashActContract {
    interface View extends BaseView {
        void onTokenSuccess(String code, PersonInfoBean data, String msg);

        void onUpgradeSuccess(String code, CheckUpdateBean data, String msg);

        void onTokenFailed(Throwable ex, String code, String msg);

        void onUpgradeFailed(Throwable ex, String code, String msg);

    }

    interface Presenter extends BasePresenter<View> {
        void checkToken();

        void checkUpgrade(int type);
    }

}
