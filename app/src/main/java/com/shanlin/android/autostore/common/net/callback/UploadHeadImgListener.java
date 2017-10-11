package com.shanlin.android.autostore.common.net.callback;

/**
 * Created by cuieney on 18/08/2017.
 */

public interface UploadHeadImgListener {
    void onRequestProgress(long bytesWritten, long contentLength);
}
