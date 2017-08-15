package com.shanlin.android.autostore.common.net;


import com.shanlin.android.autostore.entity.body.MemberUpdateSendBean;
import com.shanlin.android.autostore.entity.respone.CheckUpdateBean;
import com.shanlin.android.autostore.entity.respone.CreditBalanceCheckBean;
import com.shanlin.android.autostore.entity.respone.LogoutBean;
import com.shanlin.android.autostore.entity.respone.MemberUpdateBean;
import com.shanlin.android.autostore.entity.respone.PersonInfoBean;
import com.shanlin.android.autostore.entity.respone.RefundMoneyBean;
import com.shanlin.android.autostore.entity.respone.UserNumEverydayBean;
import com.shanlin.android.autostore.entity.respone.UserVertifyStatusBean;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
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
