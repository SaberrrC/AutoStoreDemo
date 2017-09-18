package com.shanlin.autostore.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by cuieney on 2017/3/27.
 */

public interface IImageLoader {
    void displayHeadImage(Context context, String url, ImageView imageView);

    void displayImage(Context context, String url, ImageView imageView);

    void displayImage(Context context, File file, ImageView imageView);

    void displayImage(Context context, int resId, ImageView imageView);

    void displayImage(Context context, Bitmap bitmap, ImageView imageView);


    void displayCircleImage(Context context, String url, ImageView imageView);

    void displayCircleImage(Context context, File file, ImageView imageView);

    void displayCircleImage(Context context, int resId, ImageView imageView);

    void displayCircleImage(Context context, Bitmap bitmap, ImageView imageView);

}
