package com.shanlin.android.autostore.di.module;

import com.shanlin.android.autostore.common.zxing.camera.CameraManager;
import com.shanlin.android.autostore.common.zxing.decode.DecodeThread;
import com.shanlin.android.autostore.common.zxing.utils.BeepManager;
import com.shanlin.android.autostore.common.zxing.utils.CaptureActivityHandler;
import com.shanlin.android.autostore.common.zxing.utils.InactivityTimer;
import com.shanlin.android.autostore.di.PerActivity;
import com.shanlin.android.autostore.ui.act.CaptureActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by cuieney on 16/08/2017.
 */

@Module
public class CaptureModule {
    private CaptureActivity mActivity;

    public CaptureModule(CaptureActivity activity) {
        this.mActivity = activity;
    }


    @Provides
    @PerActivity
    public CameraManager povidesCameraManager(){
        return new CameraManager(mActivity);
    }

    @Provides
    @PerActivity
    public CaptureActivityHandler providesCaptureActivityHandler(CameraManager cameraManager){
        return new CaptureActivityHandler(mActivity,cameraManager, DecodeThread.ALL_MODE);
    }

    @Provides
    @PerActivity
    public InactivityTimer providesInactivityTimer(){
        return new InactivityTimer(mActivity);
    }

    @Provides
    @PerActivity
    public BeepManager providesBeepManager(){
        return new BeepManager(mActivity);
    }

}
