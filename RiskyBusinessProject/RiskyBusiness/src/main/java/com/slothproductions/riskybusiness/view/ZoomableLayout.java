package com.slothproductions.riskybusiness.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.RelativeLayout;
import android.view.GestureDetector;

import java.io.Console;
import java.util.jar.Attributes;

public class ZoomableLayout extends RelativeLayout {
    private static String TAG = "Zoom Layout";

    //These two constants specify the minimum and maximum zoom
    private static float STANDARD_ZOOM = 1f;
    private static float MAX_ZOOM = 2f;
    private float MIN_ZOOM;

    boolean isZoomed = false;

    private float mCenterX;
    private float mCenterY;

    private float mCurrentCenterX;
    private float mCurrentCenterY;
    private float mScaleFactor = 1.f;

    int mWidth;
    int mHeight;

    public ZoomableLayout(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public ZoomableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public ZoomableLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWillNotDraw(false);
    }

    public boolean Zoom(MotionEvent event) {
        Log.d(TAG, "Zoom Called");
        mCurrentCenterX = event.getX();
        mCurrentCenterY = event.getY();
        if (isZoomed) {
            mScaleFactor = STANDARD_ZOOM;
            isZoomed = false;
        }
        else {
            mScaleFactor = MAX_ZOOM;
            isZoomed = true;
        }
        invalidate();
        return true;
    }

    public boolean Pan(MotionEvent start, float x, float y) {
        mCurrentCenterX += x;
        mCurrentCenterY += y;
        if (isInBoundsX() && isInBoundsY()) {
        }
        else if (isInBoundsX() && !isInBoundsY()) {
            mCurrentCenterY -=y;
        }
        else if (!isInBoundsX() && isInBoundsY()) {
            mCurrentCenterX-=x;
        }
        else {
            mCurrentCenterX-=x;
            mCurrentCenterY -=y;
        }
        invalidate();
        return false;
    }

    public boolean isZoom() {
        return isZoomed;
    }

    public boolean isInBoundsX() {
        if (mCurrentCenterX >= mWidth) {
            return false;
        }
        else if (mCurrentCenterX <= 0) {
            return false;
        }
        return true;
    }

    public boolean isInBoundsY() {
        if (mCurrentCenterY >= mHeight) {
            return false;
        }
        else if (mCurrentCenterY <= 0) {
            return false;
        }
        return true;
    }

    public float getPanX() {
        return mCurrentCenterX;
    }

    public float getPanY() {
        return mCurrentCenterY;
    }

    public float getZoom() {
        return mScaleFactor;
    }

    public void setDimensions(int width, int height) {
        mHeight = height;
        mWidth = width;
        mCurrentCenterX = mCenterX = (float)(width/2.0);
        mCurrentCenterY = mCenterY = (float)(height/2.0);
        if (mWidth < 2560 || mHeight < 1504) {
            MIN_ZOOM = mWidth/(float)2560;
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.scale(mScaleFactor, mScaleFactor, mCurrentCenterX, mCurrentCenterY);
        canvas.save();
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    /*
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int)(widthMeasureSpec*mScaleFactor);
        int height = (int)(heightMeasureSpec*mScaleFactor);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }*/
}
