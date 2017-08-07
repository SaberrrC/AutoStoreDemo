package com.shanlin.autostore.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

import com.shanlin.autostore.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shanlin on 2017-8-6.
 */

public class CircleView extends View {

    private Paint mPaint;
    private int   height;
    private int   width;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = widthMeasureSpec;
        height = heightMeasureSpec;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        LogUtils.d("width   " + width + " + " + height);
        Rect rect = new Rect(0, 0, width, height);
        canvas.drawRect(rect, mPaint);
        Region region = new Region();

    }

    List<Point> points = new ArrayList<>();

    private void initPointsCircular(Canvas canvas) {
        mPaint.setColor(Color.RED);
        for (int i = 0; i < 360; i += 1) {
            int x = (int) (width / 2 - 238 * Math.sin(Math.PI * (i - 90) / 180));
            int y = (int) (height / 2 - 238 - 10 + 238 * Math.cos(Math.PI * (i - 90) / 180));
            Point point = new Point(x, y);
            points.add(point);
            canvas.drawPoint(point.x, point.y, mPaint);
        }
    }


}
