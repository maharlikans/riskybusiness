package com.slothproductions.riskybusiness.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class StaticLayout extends ViewGroup {
    private static String TAG = "Background Layout";

    private float mCenterX;
    private float mCenterY;

    private float mScaleFactorX = 1.f;
    private float mScaleFactorY = 1.f;

    int mWidth;
    int mHeight;

    public StaticLayout(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public StaticLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public StaticLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWillNotDraw(false);
    }

    public void setDimensions() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        mHeight = height;
        mWidth = width;
        mCenterX = (float) (width / 2.0);
        mCenterY = (float) (height / 2.0);
        if (mWidth < 2560 || mHeight < 1504) {
            mScaleFactorX = mWidth / (float) 2560;
            mScaleFactorY = mHeight / (float) 1504;
        }
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.scale(mScaleFactorX, mScaleFactorY, 0, 0);
        canvas.save();
        super.dispatchDraw(canvas);
        canvas.save();
        canvas.restore();
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.layout(l,t,r,b);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setDimensions();
        super.measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }
    /*
    @Override
    protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < getChildCount(); i++) {
            super.measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
    }*/

}