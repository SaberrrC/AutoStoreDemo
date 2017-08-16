package com.shanlin.android.autostore.presenter;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.shanlin.android.autostore.common.base.BaseActivity;
import com.shanlin.android.autostore.common.base.RxPresenter;
import com.shanlin.android.autostore.common.net.Api;
import com.shanlin.android.autostore.common.net.NetWorkUtil;
import com.shanlin.android.autostore.common.net.SubscriberWrapper;
import com.shanlin.android.autostore.entity.respone.LoginBean;
import com.shanlin.android.autostore.entity.respone.WxUserInfoBean;
import com.shanlin.android.autostore.presenter.Contract.LoginActContract;
import com.shanlin.autostore.bean.paramsBean.FaceLoginSendBean;
import com.shanlin.autostore.bean.paramsBean.WechatLoginSendBean;
import com.shanlin.autostore.bean.resultBean.WxTokenBean;
import com.shanlin.autostore.constants.Constant;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by dell、 on 2017/8/15.
 */

public class LoginPresenter extends RxPresenter<LoginActContract.View> implements LoginActContract.Presenter {

    public static final String TYPE_WX = "1";

    @Inject
    public LoginPresenter(Api apiService) {
        super(apiService);
    }

    @Override
    public void getWxInfo() {

    }

    @Override
    public void checkWxInstall(BaseActivity<LoginPresenter> view, IWXAPI api) {
        boolean isInstalled1 = api.isWXAppInstalled() && api.isWXAppSupportAPI();
        boolean isInstalled = isWeixinAvilible(view);
        mView.onWxInstallCheckSuccess(isInstalled);
    }

    /**
     * 获取微信返回数据
     *
     * @param code
     */
    @Override
    public void getResult(String code) {
        apiService.getWxToken("https://api.weixin.qq.com/sns/oauth2/access_token", Constant.APP_ID, Constant.APP_SECRET, code, "authorization_code").compose(NetWorkUtil.rxSchedulerHelper()).subscribe(new Subscriber<WxTokenBean>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Integer.MAX_VALUE);
            }

            @Override
            public void onNext(WxTokenBean wxTokenBean) {
                mView.onWxResultSuccess(wxTokenBean);
            }

            @Override
            public void onError(Throwable t) {
                mView.onWxResultFailed(t);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void getUserInfo(String access_token, String openid) {
        apiService.getWxUserInfo("https://api.weixin.qq.com/sns/userinfo?access_token", access_token, openid)
                .compose(NetWorkUtil.rxSchedulerHelper())
                .subscribe(new Subscriber<WxUserInfoBean>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Integer.MAX_VALUE);
                    }

                    @Override
                    public void onNext(WxUserInfoBean wxUserInfoBean) {
                        int sex = wxUserInfoBean.getSex();
                        String unionid = wxUserInfoBean.getUnionid();
                        String openid = wxUserInfoBean.getOpenid();
                        String nickname = wxUserInfoBean.getNickname();
                        WechatLoginSendBean sendBean = new WechatLoginSendBean();
                        sendBean.setType(TYPE_WX);
                        sendBean.setUnionid(unionid);
                        WechatLoginSendBean.ExtraBean extraBean = new WechatLoginSendBean.ExtraBean();
                        extraBean.setSex(sex + "");
                        extraBean.setOpenid(openid);
                        extraBean.setNickname(nickname);
                        sendBean.setExtra(extraBean);
                        mView.onWxUserInfoSuccess(wxUserInfoBean, sendBean);
                    }

                    @Override
                    public void onError(Throwable t) {
                        mView.onWxUserInfoFailed(t);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 微信登录后台
     *
     * @param userInfoBean
     * @param sendBean
     */
    @Override
    public void postWxTokenLogin(WxUserInfoBean userInfoBean, WechatLoginSendBean sendBean) {
        apiService.postWxTokenLogin(sendBean)
                .compose(NetWorkUtil.rxSchedulerHelper())
                .subscribe(new SubscriberWrapper<LoginBean>(new SubscriberWrapper.CallBackListener<LoginBean>() {

            @Override
            public void onSuccess(String code, LoginBean data, String msg) {
                mView.onWxTokenLoginSuccess(code, data, msg, userInfoBean);
            }

            @Override
            public void onFailed(Throwable ex, String code, String msg) {
                mView.onWxTokenLoginFailed(ex, code, msg, userInfoBean);
            }
        }));
    }

    @Override
    public void postFaceLogin(String encode, String deviceId) {
        apiService.postFaceLogin(new FaceLoginSendBean(encode, deviceId)).compose(NetWorkUtil.rxSchedulerHelper()).subscribe(new SubscriberWrapper<>(new SubscriberWrapper.CallBackListener<LoginBean>() {
            @Override
            public void onSuccess(String code, LoginBean data, String msg) {
                mView.onFaceLoginSuccess(code, data, msg);
            }

            @Override
            public void onFailed(Throwable ex, String code, String msg) {
                mView.onFaceLoginFailed(ex, code, msg);
            }
        }));

    }

    private boolean isWeixinAvilible(BaseActivity<LoginPresenter> view) {
        final PackageManager packageManager = view.getPackageManager();
        //  获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        //   获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }
}
