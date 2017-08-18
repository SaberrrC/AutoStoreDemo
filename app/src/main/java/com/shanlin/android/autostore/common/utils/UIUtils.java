package com.shanlin.android.autostore.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


/**
 * UI中工具处理
 *
 * @author liuyang
 */
public class UIUtils {

    /**
     * 此处是设定判断一段时间内不进行相同的提示，加载的时候进行
     */
    private static String mOldMsg;
    protected static Toast mToast       = null;
    private static   long  mLastTime    = 0;
    private static   long  mCurrentTime = 0;


    /**
     * default
     **/
    public static void defaultToast(Context context, String txt) {
        if (context != null) {
            Toast toast = Toast.makeText(context, txt, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static void centerToast(Context context, String txt) {
        Toast toast = Toast.makeText(context, txt, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void rightToast(Context context, String txt) {
        Toast toast = Toast.makeText(context, txt, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.RIGHT, 0, 0);
        toast.show();
    }

    public static void showToast(Context context, int id) {
        if (context != null && context.getResources() != null) {
            String msg = context.getResources().getString(id);
            defaultToast(context, msg);
        }
    }

    public static void showToastTiming(Context context, String showString, int... time) {
        int timeSpace = 10000; //此处如果没有进行设置就进行默认的是10S
        if (time.length > 0) {
            timeSpace = time[0];
        }
        if (mToast == null) {
            mToast = Toast.makeText(context, showString, Toast.LENGTH_SHORT);
            mToast.show();
            mLastTime = System.currentTimeMillis();
        } else {
            mCurrentTime = System.currentTimeMillis();
            if (TextUtils.equals(showString, mOldMsg)) {
                if (mCurrentTime - mLastTime > timeSpace) {
                    mToast.show();
                }
            } else {
                mOldMsg = showString;
                mToast.setText(showString);
                mToast.show();
            }
        }
        mLastTime = mCurrentTime;
    }

    /**
     * 此处进行的是toast的多次提示，根据不同的时间间隔进行显示toast提示，相同的提示
     *
     * @param context
     * @param id
     * @param time    毫秒为单位
     */

    public static void showToastTiming(Context context, int id, int... time) {
        String showString = context.getString(id);
        showToastTiming(context, showString, time);
    }


    public static <T extends View> T findViewById(View view, int viewId) {
        return view == null ? null : (T) view.findViewById(viewId);
    }

    /**
     * 根据url打开相应网页
     *
     * @param context
     * @param url
     */
    public static void openBrowser(Context context, String url) {
        Intent intent = new Intent();
        Uri uri = Uri.parse(url);
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<?> cls, String intentKey, String intentValue, boolean finishSelf) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, cls);
        if (!TextUtils.isEmpty(intentKey) && !TextUtils.isEmpty(intentValue)) {
            intent.putExtra(intentKey, intentValue);
        }
        context.startActivity(intent);
        if (context instanceof Activity) {
            if (finishSelf) {
                ((Activity) context).finish();
            }
            //			((Activity) context).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        }
    }

    public static void startActivity(Context context, Class<?> cls, Bundle bundle, boolean finishSelf) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
        if (context instanceof Activity) {
            if (finishSelf) {
                ((Activity) context).finish();
            }
            //			((Activity) context).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        }
    }

    public static void startActivity(Context context, Class<?> cls, Bundle bundle, boolean finishSelf, String agentCode, String agentValue, String type) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
        if (context instanceof Activity) {
            if (finishSelf) {
                ((Activity) context).finish();
            }
            //			((Activity) context).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        }
    }

    public static void startActivityForResult(Context context, Class<?> cls, int requestCode, Bundle bundle, String agentCode, String agentValue, String type) {
        if (context == null || !(context instanceof Activity)) {
            return;
        }
        Activity activity = (Activity) context;
        Intent intent = new Intent(activity, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 隐藏输入法
     *
     * @param context
     * @param view
     */

    public static void hideSoftInput(Context context, View view) {
        if (context == null || ((Activity) context).getWindow() == null || ((Activity) context).getWindow().getAttributes() == null || view == null) {
            return;
        }
        InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 此处是进行弹出软键盘的方法，该方法是进行有998的延迟进行软键盘的弹出
     * 这种弹出方式是因为在页面绘制是覆盖掉了edittext的自动打开软键盘，故采用这样的方式进行页面的显示
     */

    public static void showSoftInput(final EditText editText) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }
        }, 500);
    }


    /**
     * 获取屏幕的高度
     */

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 从URL中取出title
     *
     * @param url
     * @return
     */
    public static String parseTitleFromUrl(String url) {
        if (TextUtils.isEmpty(url) || !url.startsWith("http") || !url.contains("?")) {
            return "";
        }
        String[] split1 = url.split("\\?");
        if (split1.length < 2) {
            return "";
        }
        String[] split2 = split1[1].split("&");
        for (String str : split2) {
            if (str.contains("title")) {
                String[] split3 = str.split("=");
                if (split3.length < 2) {
                    return "";
                }
                return split3[1];
            }
        }
        return "";
    }

    /***
     * 设置进度条的值，小于95%时默认添加5%
     */
    public static void setProgressValue(ProgressBar progressBar, int value, int maxValue) {
        if (progressBar == null) {
            return;
        }
        if (maxValue == 0) {
            progressBar.setProgress(0);
            return;
        }
        progressBar.setMax(maxValue);
        float progress = (float) value / maxValue;
        if (progress < 0.01f) {
            progress = 0f;
        } else if (progress >= 0.01f && progress <= 0.3f) {
            progress = (progress - 0.01f) * 1.4f + 0.01f;
        } else if (progress > 0.3f && progress <= 0.6f) {
            progress = (progress - 0.3f) * 1.1f + 0.3f;
        } else if (progress > 0.6f && progress <= 0.9f) {
            progress = (progress - 0.6f) * 0.7f + 0.6f;
        } else if (progress > 0.9f && progress <= 1f) {
            progress = (progress - 0.9f) * 0.4f + 0.9f;
        }
        progressBar.setProgress(Math.round(maxValue * progress));
    }


}
