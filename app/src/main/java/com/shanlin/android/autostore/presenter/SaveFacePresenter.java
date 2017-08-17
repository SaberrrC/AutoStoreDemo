package com.shanlin.android.autostore.presenter;

import com.shanlin.android.autostore.common.base.RxPresenter;
import com.shanlin.android.autostore.common.net.Api;
import com.shanlin.android.autostore.common.net.NetWorkUtil;
import com.shanlin.android.autostore.common.net.SubscriberWrapper;
import com.shanlin.android.autostore.entity.body.MemberUpdateSendBean;
import com.shanlin.android.autostore.entity.respone.MemberUpdateBean;
import com.shanlin.android.autostore.presenter.Contract.SaveFaceActContract;

import javax.inject.Inject;

/**
 * Created by dell、 on 2017/8/16.
 */

public class SaveFacePresenter extends RxPresenter<SaveFaceActContract.View> implements SaveFaceActContract.Presenter {

    @Inject
    public SaveFacePresenter(Api apiService) {
        super(apiService);
    }

    @Override
    public void doMemberUpdate(MemberUpdateSendBean bean) {
        apiService.postMemberUpdate(bean).compose(NetWorkUtil.rxSchedulerHelper()).subscribe(new SubscriberWrapper<MemberUpdateBean>(new SubscriberWrapper.CallBackListener<MemberUpdateBean>() {
            @Override
            public void onSuccess(String code, MemberUpdateBean data, String msg) {
                mView.doMemberUpdateSuccess(code, data, msg);
            }

            @Override
            public void onFailed(Throwable ex, String code, String msg) {
                mView.doMemberUpdateFailed(ex, code, msg);
            }
        }));
    }
}