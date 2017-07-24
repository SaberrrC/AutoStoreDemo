package com.shanlin.autostore.interf;

import com.shanlin.autostore.bean.CaptureBean;
import com.shanlin.autostore.bean.CodeBean;
import com.shanlin.autostore.bean.FaceLoginBean;
import com.shanlin.autostore.bean.NumberLoginBean;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
}
