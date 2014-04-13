package com.slothproductions.riskybusiness.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class StaticLayout extends ViewGroup {
    private static final String TAG = "Background Layout";

    private float mCenterX;
    private float mCenterY;

    private float mScaleFactorX = 1.f;
    private float mScaleFactorY = 1.f;

    private boolean defaultDimensionsSet;

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
        //make sure this method is not called more than once
        if (defaultDimensionsSet) {
            return;
        }
        defaultDimensionsSet = true;

        //Set the display width and height
        DisplayMetrics display = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(display);
        int width = display.widthPixels;
        int height = display.heightPixels;
        mHeight = height;
        mWidth = width;
        Log.d(TAG, "Display Width: " + mWidth);
        Log.d(TAG, "Display Height: " + mHeight);

        //set the center based on the display width and height
        mCenterX = (float) (mWidth / 2.0);
        mCenterY = (float) (mHeight / 2.0);

        //Set scale factor to fit entire display on screen
        if (mWidth < 2560 || mHeight < 1504) {
            mScaleFactorX = mWidth / (float) 2560;
            mScaleFactorY = mHeight / (float) 1504;
        }
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
        int startLeft, startTop, endRight, endBottom;
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            LayoutParams lp = v.getLayoutParams();
            startLeft = l;
            startTop = t;
            Log.d(TAG, "Measured Width: " + v.getMeasuredWidth());
            Log.d(TAG, "Measured Height: " + v.getMeasuredHeight());
            endRight = startLeft + v.getMeasuredWidth();
            endBottom = startTop + v.getMeasuredHeight();
            v.layout(startLeft,startTop,endRight,endBottom);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!defaultDimensionsSet) {
            setDimensions();
        }

        //measure all the child views
        super.measureChildren(widthMeasureSpec, heightMeasureSpec);
        //measure self
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }
}