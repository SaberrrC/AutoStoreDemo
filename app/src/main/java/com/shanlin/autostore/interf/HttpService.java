package com.shanlin.autostore.interf;

import com.shanlin.autostore.bean.CaptureResponse;
import com.shanlin.autostore.bean.CodeResponse;
import com.shanlin.autostore.bean.NumberLoginResponse;

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
    @POST("mockjsdata/35/device/operate")
    @FormUrlEncoded
    Call<CaptureResponse> postCapture(@FieldMap Map<String, String> map);

    /**
     * 获取验证码
     */
    @POST("mockjsdata/35/member/getverifycode")
    @FormUrlEncoded
    Call<CodeResponse> postVerificationCode(@Field("mobile") String mobile);

    /**
     * 登陆
     */
    @POST("mockjsdata/35/memberLogin")
    @FormUrlEncoded
    Call<NumberLoginResponse> postNumCodeLogin(@Field("userName") String userName, @Field("validCode") String validCode);





}
