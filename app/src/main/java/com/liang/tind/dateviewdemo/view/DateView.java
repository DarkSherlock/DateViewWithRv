package com.liang.tind.dateviewdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.liang.tind.dateviewdemo.util.UIutils;


/**
 * Created by Sherlock on 2017/8/1.
 */

public class DateView extends View {

    private static final String TAG = "DateView";
    /**
     * 时钟刻度的长度与圆形背景半径比的系数，就是设置刻度横线长度为半径的0.25倍
     */
    private static final float R_QUARTER = 0.25f;
    /**
     * 圆形背景画笔
     */
    private Paint mCirclePaint;
    /**
     *指针画笔
     */
    private Paint mCursor;
    /**
     * 根据recyclerview 滑动传进来的进度，指针根据此字段来决定旋转的角度大小
     */
    private float mProcess ;

    public DateView(Context context) {
        this(context, null);
    }

    public DateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //圆形背景颜色
        mCirclePaint.setColor(Color.LTGRAY);
        mCirclePaint.setStyle(Paint.Style.FILL);

        mCursor = new Paint(Paint.ANTI_ALIAS_FLAG);

        mCursor.setStyle(Paint.Style.STROKE);
        mCursor.setStrokeWidth(2);
    }

    /**
     * 设置指针转动角度比率
     * @param process
     */
    public void setProcess(float process) {
        this.mProcess = process;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        if (modeWidth == MeasureSpec.AT_MOST || modeHeight == MeasureSpec.AT_MOST) {
            sizeHeight = UIutils.dip2px(getContext(), 20f);
            sizeWidth = UIutils.dip2px(getContext(), 20f);
        }

        setMeasuredDimension(sizeWidth, sizeHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        int radius = width / 2;//圆形背景半径
        canvas.translate(width / 2, height / 2);

        canvas.save();
        //画灰色圆形背景
        canvas.drawCircle(0, 0, width / 2, mCirclePaint);

        //画12 3 6 9 四个刻度   长度为半径（width/2)的0.25
        mCursor.setColor(Color.parseColor("#FFAAAAAA"));

        canvas.drawLine(0, -height / 2, 0, ((radius * R_QUARTER) - height / 2), mCursor);//12
        canvas.drawLine(width / 2, 0, (width / 2 - (radius * R_QUARTER)), 0, mCursor);//3
        canvas.drawLine(0, height / 2, 0, (height / 2 - (radius * R_QUARTER)), mCursor);//6
        canvas.drawLine(-width / 2, 0, (-width / 2 + (radius * R_QUARTER)), 0, mCursor);//9

        //画根据传进来的process 转动的指针
        int stopX = (int) (0.6 * (width / 2) * Math.sin(mProcess * 2 * Math.PI));
        int stopY = (int) (0.6 * (width / 2) * Math.cos(mProcess * 2 * Math.PI));
//        Log.e(TAG, "onDraw: stopX ==" + stopX);
//        Log.e(TAG, "onDraw: stopY ==" + stopY);
//        Log.e(TAG, "Math.sin(mProcess * 2 * Math.PI) ==" + Math.sin(mProcess * 2 * Math.PI));
//        Log.e(TAG, "Math.cos(mProcess * 2 * Math.PI) ==" + Math.cos(mProcess * 2 * Math.PI));
        mCursor.setColor(Color.WHITE);
        canvas.drawLine(0, 0, stopX, -stopY, mCursor);
    }


}
