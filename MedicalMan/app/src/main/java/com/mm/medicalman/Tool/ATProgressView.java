package com.mm.medicalman.Tool;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.mm.medicalman.R;

/**
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class ATProgressView extends View {
    private static final float MIDDLE_LAYER_LARGE_SCALE = 51 / 62.F;
    private static final float SMALL_CIRCLE_DEGREE_OFFSET = 0.7F;
    private static final int DEF_VIEW_SIZE = 250;

    private int midlleLayerProgressColor;
    private int midlleLayerProgressWidth;
    private int smallCircleSolideColor;
    private int smallCircleStrokeColor;
    private int smallCircleSize;
    private int smallCircleStrokeWidth;

    private Point circleCenterPoint;

    private int viewSize;
    private Paint progressPaint;
    private Paint smallCirclePaint;
    private Paint smallCircleInnerPaint;
    private Paint mTextDescPaint;
    private float mMiddleLayerSize;
    private float drgeePercent;
    private int[] doughnutColors;
    private float proress;

    public ATProgressView(Context context) {
        this(context, null);
    }

    public ATProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ATProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ATProgressView, defStyleAttr, R.style.def_progress_style);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ATProgressView_midlle_layer_progress_color:
                    midlleLayerProgressColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ATProgressView_midlle_layer_progress_width:
                    midlleLayerProgressWidth = typedArray.getDimensionPixelOffset(attr, 0);
                    break;
                case R.styleable.ATProgressView_midlle_layer_small_circle_solide_color:
                    smallCircleSolideColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ATProgressView_midlle_layer_small_circle_stroke_color:
                    smallCircleStrokeColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ATProgressView_midlle_layer_small_circle_size:
                    smallCircleSize = typedArray.getDimensionPixelSize(attr, 0);
                    break;
                case R.styleable.ATProgressView_midlle_layer_small_circle_stroke_width:
                    smallCircleStrokeWidth = typedArray.getDimensionPixelOffset(attr, 0);
                    break;
            }
        }
        typedArray.recycle();
        initData();
    }

    private void initData() {
        progressPaint = creatPaint(midlleLayerProgressColor, 0, Paint.Style.STROKE, midlleLayerProgressWidth);
        smallCirclePaint = creatPaint(smallCircleStrokeColor, 0, Paint.Style.FILL, 0);
        smallCircleInnerPaint = creatPaint(smallCircleSolideColor, 0, Paint.Style.FILL, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize;
        int heightSize;

        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            widthSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEF_VIEW_SIZE, getResources().getDisplayMetrics());
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        }

        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            heightSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEF_VIEW_SIZE, getResources().getDisplayMetrics());
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewSize = w - h >= 0 ? h : w;
        circleCenterPoint = new Point(viewSize / 2, viewSize / 2);

        mMiddleLayerSize = viewSize * MIDDLE_LAYER_LARGE_SCALE;

//        doughnutColors = new int[]{midlleLayerProgressColor,
//                getResources().getColor(R.color.colorPrimary),
//                getResources().getColor(R.color.colorAccent),
//                getResources().getColor(R.color.textHighLightColor),
//                getResources().getColor(android.R.color.holo_green_light),
//                getResources().getColor(android.R.color.holo_purple)};

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawMiddleProgressLayer(canvas);

    }
    /**
     * 绘制中间进度的背景和进度
     *
     * @param canvas 画布
     */
    private void drawMiddleProgressLayer(Canvas canvas) {

        float ddx = (viewSize - mMiddleLayerSize) / 2;
        //外切圆的坐标计算,绘制的扇形在矩形内的外切圆,注意画笔的宽度
        RectF oval = new RectF(ddx + midlleLayerProgressWidth, ddx + midlleLayerProgressWidth, viewSize - ddx - midlleLayerProgressWidth, viewSize - ddx - midlleLayerProgressWidth);

        // 注意扫过的扇形的范围(进度)要和绘制的小圆点保持一致,所以我们需要从-90度开始
//        progressPaint.setShader(new SweepGradient(0, 0, doughnutColors, null));
        canvas.drawArc(oval, -90, 360 * drgeePercent, false, progressPaint);

        // 由于前面绘制了一个小圆,所以我们弧度的角度不能用于计算圆的坐标,我们需要大概的加上那么一两度来计算,
        // 由于android坐标系的问题以及角度在不同象限内的问题,所以我们需要计算几种情况
        // 0-90,90-180 ,180-270,270-360
        float animDegree = 360 * drgeePercent + SMALL_CIRCLE_DEGREE_OFFSET;
        float xiaoyuanDegree;
        float xiaoYuanX = 0, xiaoYuanY = 0;
        int tempD = (int) animDegree;
        if (tempD >= 0 && tempD < 90) {
            // 第一象限内,sin和cons正常
            xiaoyuanDegree = animDegree;
            float hudu = (float) Math.abs(Math.PI * xiaoyuanDegree / 180);
            float sinAX = (float) Math.abs(Math.sin(hudu) * (mMiddleLayerSize / 2 + midlleLayerProgressWidth / 4));
            float cosAY = (float) Math.abs(Math.cos(hudu) * (mMiddleLayerSize / 2 + midlleLayerProgressWidth / 4));

            xiaoYuanX = (viewSize - 2 * midlleLayerProgressWidth - 2 * sinAX) / 2 + 2 * sinAX;
            xiaoYuanY = viewSize / 2 + midlleLayerProgressWidth - cosAY;
        } else if (tempD >= 90 && tempD < 180) {
            // 第二象限内,sin和cos互换
            xiaoyuanDegree = animDegree - 90;
            float hudu = (float) Math.abs(Math.PI * xiaoyuanDegree / 180);
            float sinAX = (float) Math.abs(Math.cos(hudu) * (mMiddleLayerSize / 2 + midlleLayerProgressWidth / 4));
            float cosAY = (float) Math.abs(Math.sin(hudu) * (mMiddleLayerSize / 2 + midlleLayerProgressWidth / 4));

            xiaoYuanX = (viewSize - 2 * midlleLayerProgressWidth - 2 * sinAX) / 2 + 2 * sinAX;
            xiaoYuanY = viewSize / 2 + cosAY - midlleLayerProgressWidth;

        } else if (tempD >= 180 && tempD < 270) {
            // 第三象限,sin和cos正常,但是x和y的坐标计算方法发生改变
            xiaoyuanDegree = animDegree - 180;
            float hudu = (float) Math.abs(Math.PI * xiaoyuanDegree / 180);
            float sinAX = (float) Math.abs(Math.sin(hudu) * (mMiddleLayerSize / 2 - midlleLayerProgressWidth));
            float cosAY = (float) Math.abs(Math.cos(hudu) * (mMiddleLayerSize / 2));

            xiaoYuanX = viewSize / 2 - sinAX;
            xiaoYuanY = viewSize / 2 + cosAY - midlleLayerProgressWidth;

        } else if (tempD >= 270 && tempD < 360) {
            // 第四象限内,sin和cos互换,但是x和y的坐标也发生了改变
            xiaoyuanDegree = animDegree - 270;
            float hudu = (float) Math.abs(Math.PI * xiaoyuanDegree / 180);
            float sinAX = (float) Math.abs(Math.cos(hudu) * (mMiddleLayerSize / 2 + midlleLayerProgressWidth / 4));
            float cosAY = (float) Math.abs(Math.sin(hudu) * (mMiddleLayerSize / 2 - midlleLayerProgressWidth));

            xiaoYuanX = viewSize / 2 - sinAX + midlleLayerProgressWidth;
            xiaoYuanY = viewSize / 2 - cosAY;
        }

        canvas.drawCircle(xiaoYuanX, xiaoYuanY, smallCircleSize / 2, smallCirclePaint);
        canvas.drawCircle(xiaoYuanX, xiaoYuanY, (smallCircleSize - smallCircleStrokeWidth) / 2, smallCircleInnerPaint);
    }

    /**
     * 初始化画笔
     *
     * @param paintColor 画笔颜色
     * @param textSize   文字大小
     * @param style      画笔风格
     * @param lineWidth  画笔宽度
     * @return 画笔
     */
    private Paint creatPaint(int paintColor, int textSize, Paint.Style style, int lineWidth) {
        Paint paint = new Paint();
        paint.setColor(paintColor);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(lineWidth);
        paint.setDither(true);
        paint.setTextSize(textSize);
        paint.setStyle(style);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        return paint;
    }

    private ValueAnimator getValA() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, proress);
        valueAnimator.setDuration(100);//持续时间
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(0);
        return valueAnimator;
    }

    /**
     * 开始
     */
    public void startCountdown() {
        setClickable(false);
        final ValueAnimator valA = getValA();
        valA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                drgeePercent = Float.valueOf(valA.getAnimatedValue().toString());
                invalidate();
            }
        });
        valA.start();
    }

    public float getProress() {
        return proress;
    }

    public void setProress(float proress) {
        this.proress = proress;
    }
}
