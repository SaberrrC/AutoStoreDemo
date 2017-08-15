package com.shanlin.android.autostore.common.net;


import com.shanlin.android.autostore.entity.respone.CheckUpdateBean;
import com.shanlin.android.autostore.entity.respone.PersonInfoBean;
import com.shanlin.autostore.bean.LoginBean;
import com.shanlin.autostore.bean.paramsBean.FaceLoginSendBean;
import com.shanlin.autostore.bean.paramsBean.WechatLoginSendBean;
import com.shanlin.autostore.bean.resultBean.WxTokenBean;
import com.shanlin.autostore.bean.resultBean.WxUserInfoBean;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by cuieney on 14/08/2017.
 */

public interface Api {
    /**
     * 用户信息查询
     *
     * @return
     */
    @GET("member/info")
    Flowable<PersonInfoBean> getPersonInfo();


    @GET("client/version")
    Flowable<CheckUpdateBean> doGetCheckUpdate(@Query("type") int type);

    /**
     * 微信登录
     */
    Flowable<WxUserInfoBean> getWxUserInfo(@Url String url, @Query("access_token") String access_token, @Query("openid") String openid);


    /**
     * 微信登录取token
     */
    @GET
    Flowable<WxTokenBean> getWxToken(@Url String url, @Query("appid") String appid, @Query("secret") String secret, @Query("code") String code, @Query("grant_type") String grant_type);


    //微信认证登录
    @POST("wechat/wechatlogin")
    Flowable<LoginBean> postWxTokenLogin(@Body WechatLoginSendBean wechatLoginSendBean);


    /**
     * 登陆页面脸部识别登陆
     */
    @POST("member/facelogin")
    Flowable<LoginBean> postFaceLogin(@Body FaceLoginSendBean faceLoginSendBean);


}
