package com.shanlin.autostore.utils;

/**
 * Created by DELL on 2017/8/6 0006.
 */

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author MMF
 */
public class CameraUtil {

    /**
     * . 文件夹路径
     */
    public static final String FILE_PATH = Environment
            .getExternalStorageDirectory().toString() + "/mmf/";

    /**
     * @return intent .
     * @Title: selectPhoto
     * @Description: 相册获取图片
     */
    public static Intent selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        return intent;
    }

    /**
     * @param uri 文件路径
     * @return intent .
     * @Title: takePicture
     * @Description: 照相
     */
    public static Intent takePicture(Uri uri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    public static Intent cropPhoto(Context context,Uri uri, Uri cropUri, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
//        com.shanlin.autostore
        File file = createImageFile();
        if (Build.VERSION.SDK_INT >= 24) {
            Uri photoURI = FileProvider.getUriForFile(context,
                    "com.shanlin.autostore.fileprovider",
                    file);
            intent.setDataAndType(photoURI, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        } else {
            intent.setDataAndType(uri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);  //将裁剪的结果输出到指定的Uri
            Log.d("wr", "我是低版本");
        }

        intent.putExtra("crop", "true");//可裁剪
        intent.putExtra("aspectX", 1); //裁剪的宽比例
        intent.putExtra("aspectY", 1);  //裁剪的高比例
        intent.putExtra("outputX", outputX); //裁剪的宽度
        intent.putExtra("outputY", outputY);  //裁剪的高度
        intent.putExtra("scale", true); //支持缩放
        intent.putExtra("return-data", true); //若为true则表示返回数据
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//裁剪成的图片的格式
        intent.putExtra("noFaceDetection", true);  //启用人脸识别
        return intent;
    }

    private static File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }


    /**
     * @param context 上下文
     * @param uri 文件路径
     * @return 位图 .
     * @Title: getBitmapByUri
     * @Description: 通过URI获得Bitmap
     */
    public static Bitmap getBitmapByUri(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver()
                    .openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * @param context 上下文
     * @return 文件流
     * @Title: getInputStreamByUri
     * @Description: .通过路径获取文件流
     */
    public static InputStream getInputStreamByUri(Context context, Uri uri) {
        try {
            return context.getContentResolver().openInputStream(uri);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return 临时uri .
     * @Title: getTempUri
     * @Description: 获得临时的URI
     */
    public static Uri getTempUri() {
        String fileName = System.currentTimeMillis() + ".jpg";
        File out = new File(FILE_PATH);
        if (!out.exists()) {
            out.mkdirs();
        }
        out = new File(FILE_PATH, fileName);
        return Uri.fromFile(out);
    }

    /**
     * @param context 上下文
     * @param contentUri 目标uri
     * @return 文件路径 .
     * @Title: getPathFromUri
     * @Description: 通过URI获得文件路径
     */
    public static String getPathFromUri(Context context, Uri contentUri) {
        if (contentUri != null) {
            if (contentUri.getScheme().toString().compareTo("content") == 0) {
                String[] proj = { MediaStore.Images.Media.DATA };
                CursorLoader loader = new CursorLoader(context, contentUri,
                        proj, null, null, null);
                Cursor cursor = loader.loadInBackground();
                int index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(index);
            } else if (contentUri.getScheme().toString().compareTo("file") == 0) {
                String fileName = contentUri.toString().replace("file://", "");
                return fileName;
            }
        }
        return null;
    }

}