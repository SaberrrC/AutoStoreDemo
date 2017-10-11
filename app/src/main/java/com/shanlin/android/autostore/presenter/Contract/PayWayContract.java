package com.shanlin.android.autostore.presenter.Contract;

import com.shanlin.android.autostore.common.base.BasePresenter;
import com.shanlin.android.autostore.common.base.BaseView;
import com.shanlin.android.autostore.entity.body.AliPayOrderBody;
import com.shanlin.android.autostore.entity.body.LeMaiBaoPayBody;
import com.shanlin.android.autostore.entity.body.WXPayBody;
import com.shanlin.android.autostore.entity.respone.AliPayResultBean;
import com.shanlin.android.autostore.entity.respone.CreditBalanceCheckBean;
import com.shanlin.android.autostore.entity.respone.LeMaiBaoPayResultBean;
import com.shanlin.android.autostore.entity.respone.UserVertifyStatusBean;
import com.shanlin.android.autostore.entity.respone.WxChatBean;

/**
 * Created by cuieney on 17/08/2017.
 */

public interface PayWayContract {
    interface View extends BaseView{
        void onAliPaySuccess(String code, AliPayResultBean data, String msg);
        void onWechatPaySuccess(String code, WxChatBean data, String msg);
        void onLMbPaySuccess(String code, LeMaiBaoPayResultBean data, String msg);
        void onUserVertifyAuthenStatusSuccess(String code, UserVertifyStatusBean data, String msg);
        void onUserCreditBalanceInfoSuccess(String code, CreditBalanceCheckBean data, String msg);
        void onFailed(Throwable ex, String code, String msg);
    }

    interface Presenter extends BasePresenter<View>{
        void lemaibaoPay(LeMaiBaoPayBody body);
        void createAliPreOrder(AliPayOrderBody body);
        void postWxRequest(WXPayBody body);
        void getUserVertifyAuthenStatus();
        void getUserCreditBalanceInfo();
    }
}
