package com.shanlin.android.autostore.presenter.Contract;

import com.shanlin.android.autostore.common.base.BasePresenter;
import com.shanlin.android.autostore.common.base.BaseView;
import com.shanlin.android.autostore.entity.body.MemberUpdateSendBean;
import com.shanlin.android.autostore.entity.respone.MemberUpdateBean;
import com.shanlin.android.autostore.entity.respone.PersonInfoBean;

/**
 * Created by cuieney on 15/08/2017.
 */

public interface HeadImgActContract {
    interface View extends BaseView{
        void onUploadSuccess(String code, MemberUpdateBean data, String msg);

        void onUploadFailed(Throwable ex, String code, String msg);

    }
    interface Presenter extends BasePresenter<View>{
        void uploadHeadImg(MemberUpdateSendBean bean);
    }
}
