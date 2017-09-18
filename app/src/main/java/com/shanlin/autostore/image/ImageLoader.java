package com.shanlin.autostore.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by cuieney on 10/08/2017.
 */

public class ImageLoader implements IImageLoader{

    private static class ImageLoaderHolder {
        private static final ImageLoader INSTANCE = new ImageLoader();
    }

    private ImageProxy proxy;

    private ImageLoader() {
        proxy = new ImageProxy();
    }

    public static final ImageLoader getInstance() {
        return ImageLoaderHolder.INSTANCE;
    }


    @Override
    public void displayHeadImage(Context context, String url, ImageView imageView) {
        proxy.displayHeadImage(context,url,imageView);
    }

    @Override
    public void displayImage(Context context, String url, ImageView imageView) {
        proxy.displayImage(context,url,imageView);
    }

    @Override
    public void displayImage(Context context, File file, ImageView imageView) {
        proxy.displayImage(context,file,imageView);
    }

    @Override
    public void displayImage(Context context, int resId, ImageView imageView) {
        proxy.displayImage(context,resId,imageView);
    }

    @Override
    public void displayImage(Context context, Bitmap bitmap, ImageView imageView) {
        proxy.displayImage(context,bitmap,imageView);
    }

    @Override
    public void displayCircleImage(Context context, String url, ImageView imageView) {
        proxy.displayCircleImage(context,url,imageView);
    }

    @Override
    public void displayCircleImage(Context context, File file, ImageView imageView) {
        proxy.displayCircleImage(context,file,imageView);
    }

    @Override
    public void displayCircleImage(Context context, int resId, ImageView imageView) {
        proxy.displayCircleImage(context,resId,imageView);
    }

    @Override
    public void displayCircleImage(Context context, Bitmap bitmap, ImageView imageView) {
        proxy.displayCircleImage(context,bitmap,imageView);
    }
}
