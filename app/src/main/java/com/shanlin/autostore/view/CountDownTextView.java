package com.shanlin.autostore.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.shanlin.autostore.R;

public class CountDownTextView extends AppCompatTextView {
    private int max;
    private boolean isClickable;
    private Context mContext;
    public CountDownTextView(Context context) {
        super(context);
        initData(context);
    }

    public CountDownTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData(context);

    }

    public CountDownTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);

    }

    private void initData(Context context) {
        this.mContext = context;
        isClickable = true;
        setTextColor(context.getResources().getColor(R.color.light_blue));
        setText("获取验证码");
    }


    @Override
    public void setClickable(boolean clickable) {
        super.setClickable(isClickable);
    }

    @Override
    public void setBackground(Drawable background) {
        setBackground(background);
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    setText(max+" 秒  ");
                    break;
                case 1:
                    setText("获取验证码");
                    isClickable = true;
                    setClickable(isClickable);
                    setTextColor(mContext.getResources().getColor(R.color.light_blue));
                    break;
            }
        }
    };


    /*
                * @param context （上下文）
                * @param time（倒计时时间）
                * @param view(要隐藏的view)
                * */
    public void show(Context context, int time){
        isClickable = false;
        setClickable(isClickable);
        setTextColor(context.getResources().getColor(R.color.light_blue));
        this.max = time;
        setText(max+" 秒  ");
        CountDown();
    }
    private void CountDown(){
        postDelayed(new Runnable() {
            @Override
            public void run() {
                max--;
                if(max >= 0){
                    mHandler.sendEmptyMessage(0);
                    CountDown();
                }else{
                    mHandler.sendEmptyMessage(1);
                }
            }
        },1000);
    }

    public void reset() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessage(1);
    }

}
