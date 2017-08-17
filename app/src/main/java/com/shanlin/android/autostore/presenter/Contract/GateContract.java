package com.shanlin.android.autostore.presenter.Contract;

import com.shanlin.android.autostore.common.base.BasePresenter;
import com.shanlin.android.autostore.common.base.BaseView;
import com.shanlin.android.autostore.entity.body.OpenGardBody;
import com.shanlin.android.autostore.entity.respone.CaptureBean;

/**
 * Created by cuieney on 17/08/2017.
 */

public interface GateContract {

    interface View extends BaseView{
        void onGardOpenSuccess(String code, CaptureBean data, String msg);
        void onGardOpenFailed(Throwable ex, String code, String msg);
    }

    interface Presenter extends BasePresenter<View>{
        void postGardOpen(OpenGardBody body);
    }

}
