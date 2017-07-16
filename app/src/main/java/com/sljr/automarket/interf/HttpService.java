package com.sljr.automarket.interf;

import com.sljr.automarket.base.ListBaseBean;
import com.sljr.automarket.base.ObjBaseBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by DELL on 2017/7/14 0014.
 */

public interface HttpService {

    /**
     * 用于json对象
     * @param url
     * @return
     */
    @GET("mock/596850f4c7f609711fa0575a/example/{url}")
    Call<ObjBaseBean> ObjGet(@Path("url") String url);

    /**
     * 用于json数组
     * @param url
     * @return
     */
    @GET("mock/596850f4c7f609711fa0575a/example/{url}")
    Call<ListBaseBean> ListGet(@Path("url") String url);


    @POST("mock/596850f4c7f609711fa0575a/example/{path}")
    Call<RequestBody> sendData(@Path("path") String path, @Body Class bean);

}
