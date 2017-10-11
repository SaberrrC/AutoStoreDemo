package com.shanlin.android.autostore.presenter.Contract;

import com.shanlin.android.autostore.common.base.BasePresenter;
import com.shanlin.android.autostore.common.base.BaseView;
import com.shanlin.android.autostore.entity.body.RealOrderBody;
import com.shanlin.android.autostore.entity.respone.RealOrderBean;

import retrofit2.http.Body;

/**
 * Created by cuieney on 16/08/2017.
 */

public interface CaptureContract {
    interface View extends BaseView{
        void onUpdateTempToRealSuccess(String code, RealOrderBean data, String msg);
        void onUpdateTempToRealFailed(Throwable ex, String code, String msg);
    }
    interface Presenter extends BasePresenter<View>{
        void updateTempToReal(RealOrderBody body);
    }
}
