package com.shanlin.android.autostore.presenter;

import com.shanlin.android.autostore.common.base.RxPresenter;
import com.shanlin.android.autostore.common.net.Api;
import com.shanlin.android.autostore.common.net.NetWorkUtil;
import com.shanlin.android.autostore.common.net.SubscriberWrapper;
import com.shanlin.android.autostore.common.net.UploadFileRequestBody;
import com.shanlin.android.autostore.common.net.UploadSubscriberWrapper;
import com.shanlin.android.autostore.entity.body.MemberUpdateSendBean;
import com.shanlin.android.autostore.entity.respone.MemberUpdateBean;
import com.shanlin.android.autostore.presenter.Contract.HeadImgActContract;

import javax.inject.Inject;

/**
 * Created by cuieney on 15/08/2017.
 */

public class HeadImgPresenter extends RxPresenter<HeadImgActContract.View> implements HeadImgActContract.Presenter {
    @Inject
    public HeadImgPresenter(Api apiService) {
        super(apiService);
    }

    @Override
    public void uploadHeadImg(MemberUpdateSendBean bean) {

        UploadSubscriberWrapper<MemberUpdateBean> wrapper = new UploadSubscriberWrapper<>(new UploadSubscriberWrapper.CallBackListener<MemberUpdateBean>() {
            @Override
            public void onSuccess(String code, MemberUpdateBean data, String msg) {
                mView.onUploadSuccess(code,data,msg);
            }

            @Override
            public void onFailed(Throwable ex, String code, String msg) {
                mView.onUploadFailed(ex, code, msg);
            }

            @Override
            public void onProgress(int progress) {
                mView.onProgress(progress);
            }
        });

        apiService.postMemberUpdate(bean)
                .compose(NetWorkUtil.rxSchedulerHelper())
                .subscribe(wrapper);
    }
}
