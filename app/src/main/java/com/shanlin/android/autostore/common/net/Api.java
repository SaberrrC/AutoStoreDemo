package com.shanlin.android.autostore.common.net;


import com.shanlin.android.autostore.entity.respone.CheckUpdateBean;
import com.shanlin.android.autostore.entity.respone.PersonInfoBean;
import com.shanlin.autostore.bean.LoginBean;
import com.shanlin.autostore.bean.paramsBean.CodeSendBean;
import com.shanlin.autostore.bean.paramsBean.NumberLoginBean;
import com.shanlin.autostore.bean.paramsBean.WechatSaveMobileBody;
import com.shanlin.autostore.bean.resultBean.CodeBean;
import com.shanlin.autostore.bean.resultBean.UserVertifyStatusBean;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

}
