package com.shanlin.autostore.interf;

import com.shanlin.autostore.bean.LoginBean;
import com.shanlin.autostore.bean.MemberUpdateBean;
import com.shanlin.autostore.bean.RecorderBean;
import com.shanlin.autostore.bean.paramsBean.AliPayOrderBody;
import com.shanlin.autostore.bean.paramsBean.CodeSendBean;
import com.shanlin.autostore.bean.paramsBean.FaceLoginSendBean;
import com.shanlin.autostore.bean.paramsBean.LeMaiBaoPayBody;
import com.shanlin.autostore.bean.paramsBean.MemberUpdateSendBean;
import com.shanlin.autostore.bean.paramsBean.NumberLoginBean;
import com.shanlin.autostore.bean.paramsBean.RealNameAuthenBody;
import com.shanlin.autostore.bean.paramsBean.RealOrderBody;
import com.shanlin.autostore.bean.paramsBean.WechatLoginSendBean;
import com.shanlin.autostore.bean.paramsBean.WechatSaveMobileBody;
import com.shanlin.autostore.bean.resultBean.AliPayResultBean;
import com.shanlin.autostore.bean.resultBean.CaptureBean;
import com.shanlin.autostore.bean.resultBean.CheckUpdateBean;
import com.shanlin.autostore.bean.resultBean.CodeBean;
import com.shanlin.autostore.bean.resultBean.CreditBalanceCheckBean;
import com.shanlin.autostore.bean.resultBean.LeMaiBaoPayResultBean;
import com.shanlin.autostore.bean.resultBean.PswSettingBean;
import com.shanlin.autostore.bean.resultBean.RealNameAuthenBean;
import com.shanlin.autostore.bean.resultBean.RealOrderBean;
import com.shanlin.autostore.bean.resultBean.UserVertifyStatusBean;
import com.shanlin.autostore.bean.resultBean.WxChatBean;
import com.shanlin.autostore.bean.resultBean.WxTokenBean;
import com.shanlin.autostore.bean.resultBean.WxUserInfoBean;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by DELL on 2017/7/14 0014.
 */

public interface HttpService {

    /**
     * @param url
     * @return
     */
    @GET("mock/596850f4c7f609711fa0575a/example/{url}")
    Call doGet(@Path("url") String url);

    /**
     * 实名认证post -1082
     * @return
     */
    @POST("buybao/userverify")
    Call<RealNameAuthenBean> goRealNameAuthen(@Header("token") String token,@Body
            RealNameAuthenBody body);

    /**
     * 密码设置-1083
     * @param password
     * @return
     */
    @POST("buybao/setpassword")
    Call<PswSettingBean> goPswSetting (@Query("password") String password);

    /**
     * 二维码扫描 打开闸机
     */
    @POST("memberlogin")
    @FormUrlEncoded
    Call<CaptureBean> postCapture(@FieldMap Map<String, String> map);

    /**
     * 获取验证码
     */
    @POST("member/getverifycode")
    Call<CodeBean> postVerificationCode(@Body CodeSendBean codeSendBean);

    //更新
    @GET("client/version")
    Call<CheckUpdateBean> doGetCheckUpdate(@Query("type") int type);

    /**
     * 用手机号 验证码登陆
     */
    @POST("memberlogin")
    Call<LoginBean> postNumCodeLogin(@Body NumberLoginBean numberLoginBean);

    /**
     * 登陆页面脸部识别登陆
     */
    @POST("member/facelogin")
    Call<LoginBean> postFaceLogin(@Body FaceLoginSendBean faceLoginSendBean);

    //微信登录取token
    @GET
    Call<WxTokenBean> getWxToken(@Url String url, @Query("appid") String appid, @Query("secret") String secret, @Query("code") String code, @Query("grant_type") String grant_type);

    //微信登录取用户信息
    @GET
    Call<WxUserInfoBean> getWxUserInfo(@Url String url, @Query("access_token") String access_token, @Query("openid") String openid);

    //微信认证登录
    @POST("wechat/wechatlogin")
    Call<LoginBean> postWxTokenLogin(@Body WechatLoginSendBean wechatLoginSendBean);


    /**
     * 微信订单接口
     */
    @POST("wx/pay")
    @FormUrlEncoded
    Call<WxChatBean> postWxRequest(@FieldMap Map<String, String> map);

    /**
     * 乐买宝实名认证接口 -1082
     * @param idcard
     * @param username
     * @return
     */
    @POST("buybao/userverify")
    @FormUrlEncoded
    Call<RealNameAuthenBean> realNameAuthen(@Field("idCard") String idcard, @Field("userName")
                                            String username);

    /**
     * 获取用户乐买宝实名认证信息
     * @param token
     * @return
     */
    @GET("buybao/userverify/status")
    Call<UserVertifyStatusBean> getUserVertifyAuthenStatus(@Query("token") String token);

    /**
     * 获取用户信用和余额
     * @param token
     * @return
     */
    @GET("buybao/info")
    Call<CreditBalanceCheckBean> getUserCreditBalanceInfo(@Query("token") String token);

    /**
     * 更新临时订单成为正式订单-910
     * @return
     */
    @POST("order/confirm")
    Call<RealOrderBean> updateTempToReal(@Header("token") String token, @Body
            RealOrderBody body);


    /**
     * 乐买宝支付接口-931
     * @param token
     * @param body
     * @return
     */
    @POST("buybao/pay")
    Call<LeMaiBaoPayResultBean> lemaibaoPay(@Header("token") String token, @Body LeMaiBaoPayBody body);

    /**
     * 修改个人信息
     * @param token
     * @param memberUpdateSendBean
     * @return
     */
    @POST("member/update")
    Call<MemberUpdateBean> postMemberUpdate(@Header("token") String token, @Body MemberUpdateSendBean memberUpdateSendBean);

    /**
     * 查询订单历史记录
     */
    @GET("order/history")
    Call<RecorderBean> getRecorderList(@Header("token") String token,@Query("pageNumber") String
            pageNumber,@Query("pageSize") String pageSize);

    /**
     * 创建支付宝预订单
     * @return
     */
    @POST("ali/pay")
    Call<AliPayResultBean> createAliPreOrder(@Header("token") String token, @Body AliPayOrderBody
            body);

    /**
     * 微信认证登录绑定手机号
     */
    @POST("wechat/savemobile")
    Call<LoginBean> postWechatSavemobile(@Body WechatSaveMobileBody wechatSaveMobileBody);
}
