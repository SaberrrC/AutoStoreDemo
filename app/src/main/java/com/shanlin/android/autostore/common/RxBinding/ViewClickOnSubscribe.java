package com.shanlin.android.autostore.common.RxBinding;

import android.os.Looper;
import android.view.View;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;


/**
 * Created by cuieney on 2017/3/23.
 */

final class ViewClickOnSubscribe implements ObservableOnSubscribe<Void> {
    private final View view;

    ViewClickOnSubscribe(View view) {
        this.view = view;
    }


    @Override
    public void subscribe(@NonNull ObservableEmitter<Void> e) throws Exception {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new IllegalStateException(
                    "Must be called from the main thread. Was: " + Thread.currentThread());
        }
        View.OnClickListener listener = view1 -> {
            if (e.isDisposed()) {
                e.onNext(null);
            }
        };
        view.setOnClickListener(listener);
    }
}