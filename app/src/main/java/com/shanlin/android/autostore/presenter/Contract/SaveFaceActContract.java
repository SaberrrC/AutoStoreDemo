package com.shanlin.android.autostore.presenter.Contract;

import com.shanlin.android.autostore.common.base.BasePresenter;
import com.shanlin.android.autostore.common.base.BaseView;
import com.shanlin.android.autostore.entity.body.MemberUpdateSendBean;
import com.shanlin.android.autostore.entity.respone.MemberUpdateBean;

/**
 * Created by dell、 on 2017/8/16.
 */

public interface SaveFaceActContract {
    interface View extends BaseView {

        void doMemberUpdateSuccess(String code, MemberUpdateBean data, String msg);

        void doMemberUpdateFailed(Throwable ex, String code, String msg);
    }

    interface Presenter extends BasePresenter<SaveFaceActContract.View> {

        void doMemberUpdate(MemberUpdateSendBean memberUpdateSendBean);
        
    }
}
