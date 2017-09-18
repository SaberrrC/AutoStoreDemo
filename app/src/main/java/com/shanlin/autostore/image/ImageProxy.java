package com.shanlin.autostore.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.shanlin.autostore.R;

import java.io.File;

/**
 * Created by cuieney on 10/08/2017.
 */

final class ImageProxy implements IImageLoader{

    private static final int HEAD_PLACEHOLDER = R.mipmap.default_head;
    private static final int IMAGE_PLACEHOLDER = R.mipmap.holder_img;
    private static final int ERROR = R.mipmap.error_img;

    @Override
    public void displayHeadImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url)
                .placeholder(HEAD_PLACEHOLDER)
                .error(HEAD_PLACEHOLDER)
                .transform(new GlideCircleTransform(context))
                .into(imageView);
    }

    @Override
    public void displayImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url)
                .placeholder(IMAGE_PLACEHOLDER)
                .error(ERROR)
                .into(imageView);
    }

    @Override
    public void displayImage(Context context, File file, ImageView imageView) {
        Glide.with(context).load(file)
                .placeholder(IMAGE_PLACEHOLDER)
                .error(ERROR)
                .into(imageView);
    }

    @Override
    public void displayImage(Context context, int resId, ImageView imageView) {
        Glide.with(context).load(resId)
                .placeholder(IMAGE_PLACEHOLDER)
                .error(ERROR)
                .into(imageView);
    }

    @Override
    public void displayImage(Context context, Bitmap bitmap, ImageView imageView) {
        Glide.with(context).load(bitmap)
                .placeholder(IMAGE_PLACEHOLDER)
                .error(ERROR)
                .into(imageView);
    }

    @Override
    public void displayCircleImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url)
                .placeholder(IMAGE_PLACEHOLDER)
                .error(ERROR)
                .transform(new GlideCircleTransform(context))
                .into(imageView);
    }

    @Override
    public void displayCircleImage(Context context, File file, ImageView imageView) {
        Glide.with(context).load(file)
                .placeholder(IMAGE_PLACEHOLDER)
                .error(ERROR)
                .transform(new GlideCircleTransform(context))
                .into(imageView);
    }

    @Override
    public void displayCircleImage(Context context, int resId, ImageView imageView) {
        Glide.with(context).load(resId)
                .placeholder(IMAGE_PLACEHOLDER)
                .error(ERROR)
                .transform(new GlideCircleTransform(context))
                .into(imageView);
    }

    @Override
    public void displayCircleImage(Context context, Bitmap bitmap, ImageView imageView) {
        Glide.with(context).load(bitmap)
                .placeholder(IMAGE_PLACEHOLDER)
                .error(ERROR)
                .transform(new GlideCircleTransform(context))
                .into(imageView);
    }
}
