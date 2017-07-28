package com.shanlin.autostore.interf;

import com.shanlin.autostore.bean.CaptureBean;
import com.shanlin.autostore.bean.CheckUpdateBean;
import com.shanlin.autostore.bean.CodeBean;
import com.shanlin.autostore.bean.CreditBalanceCheckBean;
import com.shanlin.autostore.bean.FaceLoginBean;
import com.shanlin.autostore.bean.NumberLoginRsponseBean;
import com.shanlin.autostore.bean.RealNameAuthenBean;
import com.shanlin.autostore.bean.UserVertifyStatusBean;
import com.shanlin.autostore.bean.WxChatBean;
import com.shanlin.autostore.bean.WxTokenBean;
import com.shanlin.autostore.bean.WxUserInfoBean;
import com.shanlin.autostore.bean.sendbean.CodeSendBean;
import com.shanlin.autostore.bean.sendbean.NumberLoginBean;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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
     * 实名认证post
     *
     * @return
     */
    @POST("buybao/userverify")
    Call<RequestBody> sendData(@Field("idCard") String idCard, @Field("name") String name);

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
    Call<NumberLoginRsponseBean> postNumCodeLogin(@Body NumberLoginBean numberLoginBean);

    /**
     * 登陆页面脸部识别登陆
     */
    @POST("member/facelogin")
    @FormUrlEncoded
    Call<FaceLoginBean> postFaceLogin(@Field("imageBase64") String imageBase64);
    //微信登录取token
    @GET
    Call<WxTokenBean>getWxToken(@Url String url, @Query("appid")String appid, @Query("secret")String secret, @Query("code")String code,
                                @Query("grant_type")String grant_type);
    //微信登录取用户信息
    @GET
    Call<WxUserInfoBean>getWxUserInfo(@Url String url, @Query("access_token")String access_token, @Query("openid")String openid);

    //微信认证登录
    @POST("mockjsdata/35/wechat/wechatlogin")
    @FormUrlEncoded
    Call<NumberLoginBean> postWxTokenLogin(@Field("nickname") String nickname, @Field("openid") String openid,@Field("sex") String sex);


    /**
     * 微信订单接口
     */
    @POST("/wx/pay")
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
    Call<RealNameAuthenBean> realNameAuthen(@Field("idCard") String idcard,@Field("userName")
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
}
