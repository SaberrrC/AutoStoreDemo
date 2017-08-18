package com.shanlin.android.autostore.common.net;


import com.shanlin.android.autostore.entity.body.AliPayOrderBody;
import com.shanlin.android.autostore.entity.body.CodeSendBean;
import com.shanlin.android.autostore.entity.body.LeMaiBaoPayBody;
import com.shanlin.android.autostore.entity.body.MemberUpdateSendBean;
import com.shanlin.android.autostore.entity.body.NumberLoginBean;
import com.shanlin.android.autostore.entity.body.PswSettingBody;
import com.shanlin.android.autostore.entity.body.RealNameAuthenBody;
import com.shanlin.android.autostore.entity.body.OpenGardBody;
import com.shanlin.android.autostore.entity.body.RealOrderBody;
import com.shanlin.android.autostore.entity.body.WXPayBody;
import com.shanlin.android.autostore.entity.body.WechatSaveMobileBody;
import com.shanlin.android.autostore.entity.respone.AliPayResultBean;
import com.shanlin.android.autostore.entity.respone.CaptureBean;
import com.shanlin.android.autostore.entity.respone.CheckUpdateBean;
import com.shanlin.android.autostore.entity.respone.CodeBean;
import com.shanlin.android.autostore.entity.respone.CreditBalanceCheckBean;
import com.shanlin.android.autostore.entity.respone.LeMaiBaoPayResultBean;
import com.shanlin.android.autostore.entity.respone.LoginBean;
import com.shanlin.android.autostore.entity.respone.LogoutBean;
import com.shanlin.android.autostore.entity.respone.MemberUpdateBean;
import com.shanlin.android.autostore.entity.respone.PersonInfoBean;
import com.shanlin.android.autostore.entity.respone.PswSettingBean;
import com.shanlin.android.autostore.entity.respone.RealNameAuthenBean;
import com.shanlin.android.autostore.entity.respone.RealOrderBean;
import com.shanlin.android.autostore.entity.respone.RefundMoneyBean;
import com.shanlin.android.autostore.entity.respone.UserNumEverydayBean;
import com.shanlin.android.autostore.entity.respone.UserVertifyStatusBean;
import com.shanlin.android.autostore.entity.respone.WxChatBean;
import com.shanlin.android.autostore.entity.respone.WxUserInfoBean;
import com.shanlin.android.autostore.entity.body.FaceLoginSendBean;
import com.shanlin.android.autostore.entity.body.WechatLoginSendBean;
import com.shanlin.android.autostore.entity.respone.OrderDetailBean;
import com.shanlin.android.autostore.entity.respone.OrderHistoryBean;
import com.shanlin.android.autostore.entity.respone.WxTokenBean;

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
     *
     * @param token
     * @return
     */
    @GET("buybao/userverify/status")
    Flowable<UserVertifyStatusBean> getUserVertifyAuthenStatus(@Query("token") String token);

    /**
     * 微信登录
     */
    @GET
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

    /**
     * 查询订单历史记录
     */
    @GET("order/history")
    Flowable<OrderHistoryBean> getOrderHistory(@Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize);


    /**
     * 订单详情
     */
    @GET("order/details")
    Flowable<OrderDetailBean> getOrderDetail(@Query("orderNo") String orderNo);

    /**
     * 更新临时订单成为正式订单-910
     *
     * @return
     */
    @POST("order/confirm")
    Flowable<RealOrderBean> updateTempToReal(@Body RealOrderBody body);

    /**
     * 实名认证post -1082
     *
     * @return
     */
    @POST("buybao/userverify")
    Flowable<RealNameAuthenBean> goRealNameAuthen(@Body RealNameAuthenBody body);

    /**
     * 密码设置-1083
     *
     * @return
     */
    @POST("buybao/setpassword")
    Flowable<PswSettingBean> goPswSetting(@Body PswSettingBody body);

    /**
     * 二维码扫描 打开闸机
     */
    @POST("device/open")
    Flowable<CaptureBean> postGardOpen(@Body OpenGardBody openGardBody);


    /**
     * 乐买宝支付接口-931
     *
     * @param body
     * @return
     */
    @POST("buybao/pay")
    Flowable<LeMaiBaoPayResultBean> lemaibaoPay(@Body LeMaiBaoPayBody body);


    /**
     * 创建支付宝预订单
     *
     * @return
     */
    @POST("ali/pay")
    Flowable<AliPayResultBean> createAliPreOrder(@Body AliPayOrderBody body);


    /**
     * 微信订单接口
     */
    @POST("wx/pay")
    Flowable<WxChatBean> postWxRequest(@Body WXPayBody body);

}
