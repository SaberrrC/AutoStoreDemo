package com.shanlin.autostore.interf;

import com.shanlin.autostore.WxTokenBean;
import com.shanlin.autostore.bean.CaptureBean;
import com.shanlin.autostore.bean.CheckUpdateBean;
import com.shanlin.autostore.bean.CodeBean;
import com.shanlin.autostore.bean.FaceLoginBean;
import com.shanlin.autostore.bean.NumberLoginBean;
import com.shanlin.autostore.bean.WxChatBean;
import com.shanlin.autostore.bean.WxUserInfoBean;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
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
    @POST("mockjsdata/35/memberlogin")
    @FormUrlEncoded
    Call<CaptureBean> postCapture(@FieldMap Map<String, String> map);

    /**
     * 获取验证码
     */
    @POST("mockjsdata/35/member/getverifycode")
    @FormUrlEncoded
    Call<CodeBean> postVerificationCode(@Field("mobile") String mobile);

    //更新
    @GET("mockjsdata/35/client/version")
    Call<CheckUpdateBean>doGetCheckUpdate(@Query("type") int type);
    /**
     * 用手机号 验证码登陆
     */
    @POST("mockjsdata/35/memberlogin")
    @FormUrlEncoded
    Call<NumberLoginBean> postNumCodeLogin(@Field("userName") String userName, @Field("validCode") String validCode);

    /**
     * 登陆页面脸部识别登陆
     */
    @POST("mockjs/35/member/facelogin")
    @FormUrlEncoded
    Call<FaceLoginBean> postFaceLogin(@Field("imageBase64") String imageBase64);
   @GET
    Call<WxTokenBean>getWxUserInfo(@Url String url, @Path("appid")String appid, @Path("secret")String secret, @Path("code")String code,
                                   @Path("grant_type")String grant_type);
    /**
     * 微信订单接口
     */
    @POST("/wx/pay")
    @FormUrlEncoded
    Call<WxChatBean> postWxRequest(@FieldMap Map<String,String> map);
}
