package com.shanlin.android.autostore.common.Rx;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;

import io.reactivex.Observable;


/**
 * Created by cuieney on 2017/3/23.
 */

public class RxView {
    @CheckResult
    @NonNull
    public static Observable<Void> clicks(@NonNull View view) {
        return Observable.create(new ViewClickOnSubscribe(view));
    }

}
