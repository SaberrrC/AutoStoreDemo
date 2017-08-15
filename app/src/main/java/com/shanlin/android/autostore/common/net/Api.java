package com.shanlin.android.autostore.common.net;


import com.shanlin.android.autostore.entity.body.CodeSendBean;
import com.shanlin.android.autostore.entity.body.MemberUpdateSendBean;
import com.shanlin.android.autostore.entity.body.NumberLoginBean;
import com.shanlin.android.autostore.entity.body.WechatSaveMobileBody;
import com.shanlin.android.autostore.entity.respone.CheckUpdateBean;
import com.shanlin.android.autostore.entity.respone.CodeBean;
import com.shanlin.android.autostore.entity.respone.CreditBalanceCheckBean;
import com.shanlin.android.autostore.entity.respone.LoginBean;
import com.shanlin.android.autostore.entity.respone.LogoutBean;
import com.shanlin.android.autostore.entity.respone.MemberUpdateBean;
import com.shanlin.android.autostore.entity.respone.CheckUpdateBean;
import com.shanlin.android.autostore.entity.respone.PersonInfoBean;
import com.shanlin.android.autostore.entity.respone.RefundMoneyBean;
import com.shanlin.android.autostore.entity.respone.UserNumEverydayBean;
import com.shanlin.android.autostore.entity.respone.UserVertifyStatusBean;
import com.shanlin.android.autostore.entity.respone.WxUserInfoBean;
import com.shanlin.autostore.bean.paramsBean.FaceLoginSendBean;
import com.shanlin.autostore.bean.paramsBean.WechatLoginSendBean;
import com.shanlin.autostore.bean.resultBean.WxTokenBean;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by cuieney on 14/08/2017.
 */

public interface Api {
    /**
     * 用户信息查询
     * @return
     */
    @GET("member/info")
    Flowable<PersonInfoBean> getPersonInfo();


    @GET("client/version")
    Flowable<CheckUpdateBean> doGetCheckUpdate(@Query("type") int type);

    /**
     * 获取验证码
     */
    @POST("member/getverifycode")
    Flowable<CodeBean> postVerificationCode(@Body CodeSendBean codeSendBean);

    /**
     * 用手机号 验证码登陆
     */
    @POST("memberlogin")
    Flowable<LoginBean> postNumCodeLogin(@Body NumberLoginBean numberLoginBean);

    /**
     * 微信认证登录绑定手机号
     */
    @POST("wechat/savemobile")
    Flowable<LoginBean> postWechatSavemobile(@Body WechatSaveMobileBody wechatSaveMobileBody);

    /**
     * 获取用户乐买宝实名认证信息
     * @param token
     * @return
     */
    @GET("buybao/userverify/status")
    Flowable<UserVertifyStatusBean> getUserVertifyAuthenStatus(@Query("token") String token);
    /*
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



    /**
     * 会员登出
     */
    @GET("memberlogout")
    Flowable<LogoutBean> logout();

    /**
     * 退款金额
     */
    @GET("refund/query")
    Flowable<RefundMoneyBean> getRefundMoney();

    /**
     * 获取每日到店用户
     *
     * @param date
     * @param storeId
     * @return
     */
    @GET("store/statisc")
    Flowable<UserNumEverydayBean> getUserNumEveryday(@Query("date") String date, @Query("storeId") String storeId);


    /**
     * 获取用户信用和余额
     *
     * @return
     */
    @GET("buybao/info")
    Flowable<CreditBalanceCheckBean> getUserCreditBalanceInfo();


    /**
     * 获取用户乐买宝实名认证信息
     *
     * @return
     */
    @GET("buybao/userverify/status")
    Flowable<UserVertifyStatusBean> getUserVertifyAuthenStatus();


    /**
     * 修改个人信息
     *
     * @param memberUpdateSendBean
     * @return
     */
    @POST("member/update")
    Flowable<MemberUpdateBean> postMemberUpdate(@Body MemberUpdateSendBean memberUpdateSendBean);

}
