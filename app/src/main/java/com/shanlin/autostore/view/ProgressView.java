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
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.shanlin.autostore.R;
import com.shanlin.autostore.utils.CommonUtils;


/**
 * Created by DELL on 2017/7/20 0020.
 */

public class ProgressView extends View {

    private static final String TAG = "wr";

    private final Bitmap boyBM,girlBM;
    private final int OFF_SET = 10,radius,bottom;
    private final int size;
    private int top;
    private float lefts = 100;//左边中间直线的距离
    private final Paint paintLeft,paintRight,paint_white;
    private final Path right_path;

    static Handler h = new Handler();
    private int value;
    private int width;

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
        float v = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, new DisplayMetrics());
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //背景线条位置
        RectF left = new RectF(0,top,lefts+2*radius+radius/3,bottom);
        RectF right = new RectF(0,top,width,bottom);
        //这里出现radius/2的偏差,不知道什么原因
        RectF rf1 = new RectF(lefts+radius,top,lefts+3*radius-radius/2,bottom);
        RectF rf2 = new RectF(getWidth()-2*radius,top,getWidth(),bottom);

        right_path.moveTo(lefts+3*radius,top);
        right_path.lineTo(getWidth()-radius,top);
        right_path.addArc(rf2,-90,180);
        right_path.lineTo(lefts+3*radius,bottom);
        right_path.lineTo(lefts+3*radius,top);
        right_path.close();

        canvas.drawText(value + "%", girlBM.getWidth() + 2*radius, girlBM
                        .getHeight() - girlBM.getHeight()/5,
                paintLeft);

        canvas.drawText((total == 0 ? 0 : (100 - value)) + "%", getWidth() - 2*boyBM.getWidth(),
                boyBM
                .getHeight
                        () - boyBM.getHeight()/5,
                paintRight);

        canvas.drawBitmap(girlBM,radius,0,null);
        canvas.drawBitmap(boyBM,getWidth()-3*boyBM.getWidth(),0,null);
        if (textLeft != 0) {
            canvas.drawRoundRect(left,radius,top+radius,paintLeft);
            canvas.drawArc(rf1,-90,180,false,paint_white);
            canvas.drawPath(right_path,paintRight);
        } else {
            canvas.drawRoundRect(right,radius,top+radius,paintRight);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        CommonUtils.debugLog("width="+getWidth()+"-------"+"height="+getHeight());
        width = MeasureSpec.getSize(widthMeasureSpec);
        int height = bottom;
        setMeasuredDimension(width, height);
    }

    private int textLeft;
    private int total;

    public void setGirlPercent(int text) {
        textLeft = text;
    }
    public void setTotalPeople(int totalPeople) {
        total = totalPeople;
    }


    public void doNumAnim(int startNum, int endNum) {

        ValueAnimator animator = ValueAnimator.ofInt(startNum,endNum);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value = (int) animation.getAnimatedValue();
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
