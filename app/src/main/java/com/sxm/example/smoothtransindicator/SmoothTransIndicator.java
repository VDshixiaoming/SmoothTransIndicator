package com.sxm.example.smoothtransindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by shixiaoming on 2017/12/20.
 */

public class SmoothTransIndicator extends View {

    private Path mPath;
    private Paint paintSelect;
    private Paint paintDefault;
    private Paint paintDismiss;
    int mNum;//个数
    float mRadius;//半径
    float mLength;//线长
    float mHeight;//线宽
    private float mOffset;//偏移量
    private int mSelected_color;//选中颜色
    private int mDefault_color;//默认颜色
    private int mIndicatorType;//点类型
    private int mDistanceType;//距离类型
    private float mDistance;//间隔距离
    private int mPosition;//第几张
    /**
     * 一个常量，用来计算绘制圆形贝塞尔曲线控制点的位置
     */
    private static final float M = 0.551915024494f;
    private float mPercent;
    private boolean mIsLeft;

    public SmoothTransIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setStyleable(context, attrs);
        paintDefault = new Paint();
        paintSelect = new Paint();
        paintDismiss = new Paint();
        mPath = new Path();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        //实心
        paintSelect.setStyle(Paint.Style.FILL);
        paintSelect.setColor(mSelected_color);
        paintSelect.setAntiAlias(true);
        paintSelect.setStrokeWidth(3);
        //空心
        paintDefault.setStyle(Paint.Style.FILL);
        paintDefault.setColor(mDefault_color);
        paintDefault.setAntiAlias(true);
        paintDefault.setStrokeWidth(3);

        paintDismiss.setStyle(Paint.Style.FILL);
        paintDismiss.setColor(mSelected_color);
        paintDismiss.setAntiAlias(true);
        paintDismiss.setStrokeWidth(3);
    }

    /**
     * 绘制   invalidate()后 执行
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ((mNum <= 0)) {
            return;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        canvas.translate(width / 2, height / 2);
        //初始化画笔
        initPaint();
        //距离
        switch (mDistanceType) {
            case DistanceType.BY_DISTANCE:

                break;
            case DistanceType.BY_RADIUS://圆心到 3倍半径 只有一个半径
                mDistance = 3 * mRadius;
                break;
            case DistanceType.BY_LAYOUT://布局等分
                if (mIndicatorType == IndicatorType.RECT) {
                    mDistance = width / (mNum + 1);
                } else {
                    mDistance = width / mNum;
                }
                break;
        }
        switch (mIndicatorType) {
            case IndicatorType.CIRCLE://圆
                for (int i = 0; i < mNum; i++) {//默认点 -(mNum - 1) * 0.5f * mDistance 第一个点
                    canvas.drawCircle(-(mNum - 1) * 0.5f * mDistance + i * mDistance, 0, mRadius, paintDefault);
                }
                //选中
                canvas.drawCircle(-(mNum - 1) * 0.5f * mDistance + mOffset, 0, mRadius, paintSelect);
                break;
            case IndicatorType.RECT://圆线
                int appearColor = GradientColorUtil.caculateColor(mDefault_color, mSelected_color, mPercent);
                int dismissColor = GradientColorUtil.caculateColor(mSelected_color, mDefault_color, mPercent);
                //第一个 线 选中 消失
                float leftClose = -(mNum) * 0.5f * mDistance + mPosition * mDistance - mRadius;
                float rightClose = leftClose + 2 * mRadius + mDistance - mOffset;
                float topClose = -mRadius;
                float bottomClose = mRadius;
                //圆
                RectF tip = new RectF(0, -mRadius, 0, mRadius);
                for (int i = mPosition + 3; i <= mNum; i++) {
                    tip.left = -(mNum) * 0.5f * mDistance + i * mDistance - mRadius;
                    tip.right = -(mNum) * 0.5f * mDistance + i * mDistance + mRadius;
                    canvas.drawRoundRect(tip, mRadius / 2, mRadius / 2, paintDefault);
                }
                for (int i = mPosition - 1; i >= 0; i--) {
                    tip.left = -(mNum) * 0.5f * mDistance + i * mDistance - mRadius;
                    tip.right = -(mNum) * 0.5f * mDistance + i * mDistance + mRadius;
                    canvas.drawRoundRect(tip, mRadius / 2, mRadius / 2, paintDefault);
                }
                RectF rectClose = new RectF(leftClose, topClose, rightClose, bottomClose);// 设置个新的长方形
                paintSelect.setColor(dismissColor);
                paintDismiss.setColor(dismissColor);
                canvas.drawRoundRect(rectClose, mRadius / 2, mRadius / 2, paintSelect);
                //第二个 线  显示
                if (mPosition < mNum - 1) {
                    float rightOpen = -(mNum) * 0.5f * mDistance + (mPosition + 2) * mDistance + mRadius;
                    float leftOpen = rightOpen - 2 * mRadius - mOffset;
                    float topOpen = -mRadius;
                    float bottomOpen = mRadius;
                    RectF rectOpen = new RectF(leftOpen, topOpen, rightOpen, bottomOpen);// 设置个新的长方形
                    paintSelect.setColor(appearColor);
                    canvas.drawRoundRect(rectOpen, mRadius / 2, mRadius / 2, paintSelect);
                }

                break;
        }
    }

    private Point[] mSpringPoint = new Point[6];


    class CenterPoint {
        float x;
        float y;
    }

    class Point {
        float x;
        float y;
    }

    /**
     * xml 参数设置  选中颜色 默认颜色  点大小 长度 距离 距离类型 类型 真实个数(轮播)
     *
     * @param context
     * @param attrs
     */
    private void setStyleable(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SmoothTransIndicator);
        mSelected_color = array.getColor(R.styleable.SmoothTransIndicator_selected_color, 0xffffffff);
        mDefault_color = array.getColor(R.styleable.SmoothTransIndicator_default_color, 0xffcdcdcd);
        mRadius = array.getDimension(R.styleable.SmoothTransIndicator_radius, 20);//px
        mLength = array.getDimension(R.styleable.SmoothTransIndicator_length, 2 * mRadius);//px
        mDistance = array.getDimension(R.styleable.SmoothTransIndicator_distance, 3 * mRadius);//px
        mDistanceType = array.getInteger(R.styleable.SmoothTransIndicator_distanceType, DistanceType.BY_RADIUS);
        mIndicatorType = array.getInteger(R.styleable.SmoothTransIndicator_indicatorType, IndicatorType.CIRCLE);
        mNum = array.getInteger(R.styleable.SmoothTransIndicator_num, 0);
        array.recycle();
        invalidate();
    }

    /**
     * 移动指示点
     *
     * @param percent  比例
     * @param position 第几个
     * @param isLeft   是否左滑
     */
    public void move(float percent, int position, boolean isLeft) {
        mPosition = position;
        mPercent = percent;
        mIsLeft = isLeft;
        switch (mIndicatorType) {
            case IndicatorType.RECT://圆线
                if (mPosition == mNum - 1 && !isLeft) {//第一个 右滑
                    mOffset = percent * mDistance;
                }
                if (mPosition == mNum - 1 && isLeft) {//最后一个 左滑
                    mOffset = percent * mDistance;
                } else {//中间
                    mOffset = percent * mDistance;
                }
                break;
            case IndicatorType.CIRCLE://圆
                if (mPosition == mNum - 1 && !isLeft) {//第一个 右滑
                    mOffset = (1 - percent) * (mNum - 1) * mDistance;
                } else if (mPosition == mNum - 1 && isLeft) {//最后一个 左滑
                    mOffset = (1 - percent) * (mNum - 1) * mDistance;
                } else {//中间的
                    mOffset = (percent + mPosition) * mDistance;
                }
                break;

        }

        invalidate();
    }

    /**
     * 个数
     *
     * @param num
     */
    public SmoothTransIndicator setNum(int num) {
        mNum = num;
        invalidate();
        return this;
    }

    /**
     * 类型
     *
     * @param indicatorType
     */
    public SmoothTransIndicator setType(int indicatorType) {
        mIndicatorType = indicatorType;
        invalidate();
        return this;
    }


    /**
     * 线,圆
     */
    public interface IndicatorType {
        int CIRCLE = 1;
        int RECT = 2;
    }


    /**
     * 半径
     *
     * @param radius
     */
    public SmoothTransIndicator setRadius(float radius) {
        this.mRadius = radius;
        invalidate();
        return this;
    }

    /**
     * 距离 在IndicatorDistanceType为BYDISTANCE下作用
     *
     * @param distance
     */
    public SmoothTransIndicator setDistance(float distance) {
        this.mDistance = distance;
        invalidate();
        return this;
    }

    /**
     * 距离类型
     *
     * @param mDistanceType
     */
    public SmoothTransIndicator setDistanceType(int mDistanceType) {
        this.mDistanceType = mDistanceType;
        invalidate();
        return this;
    }

    /**
     * 布局,距离,半径
     */
    public interface DistanceType { //
        int BY_RADIUS = 0;
        int BY_DISTANCE = 1;
        int BY_LAYOUT = 2;
    }

    public SmoothTransIndicator setViewPager(ViewPager viewPager) {
        setViewPager(viewPager, viewPager.getAdapter().getCount());
        return this;
    }

    public SmoothTransIndicator setViewPager(ViewPager viewpager, int CycleNumber) {
        mNum = CycleNumber;
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //记录上一次滑动的positionOffsetPixels值
            private int lastValue = -1;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                boolean isLeft = mIsLeft;
                if (lastValue / 10 > positionOffsetPixels / 10) {
                    //右滑
                    isLeft = false;
                } else if (lastValue / 10 < positionOffsetPixels / 10) {
                    //左滑
                    isLeft = true;
                }
                if (mNum > 0) {
                    move(positionOffset, position % mNum, isLeft);
                }
                lastValue = positionOffsetPixels;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return this;
    }


}
