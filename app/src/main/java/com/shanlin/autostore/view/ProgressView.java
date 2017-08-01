package com.shanlin.autostore.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.shanlin.autostore.R;


/**
 * Created by DELL on 2017/7/20 0020.
 */

public class ProgressView extends View {

    private static final String TAG = "wr";

    private final Bitmap boyBM,girlBM;
    private final int OFF_SET = 5,radius,bottom;
    private final int size;
    private int top;
    private float lefts = 100;//左边中间直线的距离
    private final Paint paintLeft,paintRight,paint_white;
    private final Path right_path;

    static Handler h = new Handler();

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        boyBM = BitmapFactory.decodeResource(context.getResources(), R.mipmap.boy);
        girlBM = BitmapFactory.decodeResource(context.getResources(), R.mipmap.girl);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressView);
        radius = typedArray.getInteger(R.styleable.ProgressView_radius, 10);//圆角半径
        size = typedArray.getDimensionPixelSize(R.styleable.ProgressView_size_text, 10);//圆角半径
        typedArray.recycle();

        paintLeft = new Paint();
        paintLeft.setAntiAlias(true);
        paintLeft.setFilterBitmap(true);
        paintLeft.setStyle(Paint.Style.FILL);
        paintLeft.setShader(new LinearGradient(0,top+radius,lefts,top+radius,Color.parseColor
                ("#EE4E4F"),Color.parseColor("#F89292"), Shader.TileMode.CLAMP));
        paintLeft.setTextSize(size);

        paintRight = new Paint();
        paintRight.setAntiAlias(true);
        paintRight.setFilterBitmap(true);
        paintRight.setStyle(Paint.Style.FILL);
        paintRight.setShader(new LinearGradient(0,top+radius,lefts,top+radius,Color.parseColor
                ("#079BDD"),Color.parseColor("#52B8E6"), Shader.TileMode.CLAMP));
        paintRight.setTextSize(size);

        paint_white = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_white.setColor(Color.WHITE);
        paint_white.setStyle(Paint.Style.FILL);

        top = girlBM.getHeight()+OFF_SET;
        bottom = top + radius*2;

        right_path = new Path();
    }

    public void flush() {
        h.post(new Runnable() {
            @Override
            public void run() {
                doNumAnim(0,textLeft);
            }
        });
    }

    Canvas  com_canvas;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        com_canvas = canvas;
        //背景线条位置
        RectF left = new RectF(radius,top,lefts+7*radius/2,bottom);
        RectF right = new RectF(radius,top,getWidth(),bottom);
        //这里出现radius/2的偏差,不知道什么原因
        RectF rf1 = new RectF(lefts+2*radius,top,lefts+4*radius,bottom);
        RectF rf2 = new RectF(getWidth()-2*radius,top,getWidth(),bottom);

        right_path.moveTo(lefts+4*radius,top);
        right_path.lineTo(getWidth()-radius,top);
        right_path.addArc(rf2,-90,180);
        right_path.lineTo(lefts+4*radius,bottom);
        right_path.lineTo(lefts+4*radius,top);
        right_path.close();

        canvas.drawText(textLeft + "%", girlBM.getWidth() + 2*radius,
                girlBM.getHeight() - radius,
                paintLeft);

        canvas.drawText(textRight + "%", getWidth() - 2*boyBM.getWidth(), boyBM.getHeight
                        () - radius,
                paintRight);

        canvas.drawBitmap(girlBM,radius,0,null);
        canvas.drawBitmap(boyBM,getWidth()-3*boyBM.getWidth(),0,null);
        if (textLeft != 0) {
            canvas.drawPath(right_path,paintRight);
            canvas.drawArc(rf1,-90,180,false,paint_white);
            canvas.drawRoundRect(left,radius,top+radius,paintLeft);
        } else {
            canvas.drawRoundRect(right,radius,top+radius,paintRight);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "width="+getWidth()+"-------"+"height="+getHeight());
        int width =  MeasureSpec.getSize(widthMeasureSpec)+2*radius;
        int height = bottom;
        setMeasuredDimension(width, height);
    }

    private int textLeft;
    private int textRight;

    public void setGirlPercent(int text) {
        textLeft = text;
    }
    public void setBoyPercent(int text) {
        textRight = text;
    }




    public void doNumAnim(int startNum, int endNum) {

        ValueAnimator animator = ValueAnimator.ofInt(startNum,endNum);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                num2s(value);
                invalidate();
            }
        });
        animator.start();
    }

    /**
     * 百分比转距离
     * @param value
     */
    public void num2s (int value) {
        lefts = value*(getWidth()-radius-2)/100-2*radius;
    }
}
