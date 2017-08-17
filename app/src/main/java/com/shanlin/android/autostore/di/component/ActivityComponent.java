package com.shanlin.android.autostore.di.component;

import android.app.Activity;

import com.shanlin.android.autostore.di.PerActivity;
import com.shanlin.android.autostore.di.module.ActivityModule;
import com.shanlin.android.autostore.ui.act.BuyRecordActivity;
import com.shanlin.android.autostore.ui.act.ChoosePayWayActivity;
import com.shanlin.android.autostore.ui.act.GateActivity;
import com.shanlin.android.autostore.ui.act.LoginActivity;
import com.shanlin.android.autostore.ui.act.MainActivity;
import com.shanlin.android.autostore.ui.act.MyHeadImgActivity;
import com.shanlin.android.autostore.ui.act.OpenLMBActivity;
import com.shanlin.android.autostore.ui.act.OrderDetailActivity;
import com.shanlin.android.autostore.ui.act.PhoneNumLoginActivity;
import com.shanlin.android.autostore.ui.act.RefundMoneyActivity;
import com.shanlin.android.autostore.ui.act.SaveFaceActivity;
import com.shanlin.android.autostore.ui.act.SplashActivity;

import dagger.Component;

/**
 * Created by cuieney on 14/08/2017.
 *
 */

@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    Activity getActivity();

    void inject(PhoneNumLoginActivity activity);
    void inject(SplashActivity activity);
    void inject(MainActivity activity);
    void inject(LoginActivity loginActivity);
    void inject(MyHeadImgActivity activity);
    void inject(OrderDetailActivity buyRecordActivity);
    void inject(BuyRecordActivity buyRecordActivity);
    void inject(RefundMoneyActivity activity);
    void inject(OpenLMBActivity activity);
    void inject(SaveFaceActivity saveFaceActivity);
    void inject(GateActivity activity);
    void inject(ChoosePayWayActivity activity);
}
