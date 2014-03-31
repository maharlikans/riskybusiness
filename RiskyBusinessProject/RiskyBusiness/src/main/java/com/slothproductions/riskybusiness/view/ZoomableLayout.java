package com.slothproductions.riskybusiness.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.view.GestureDetector;

import java.io.Console;
import java.util.jar.Attributes;

public class ZoomableLayout extends RelativeLayout {
    private static String TAG = "Zoom Layout";

    //These two constants specify the minimum and maximum zoom
    private float ZOOM_X;
    private float ZOOM_Y;

    boolean isZoomed = false;

    private float mCenterX;
    private float mCenterY;

    private float mCurrentCenterX;
    private float mCurrentCenterY;
    private float mScaleFactorX = 1.f;
    private float mScaleFactorY = 1.f;

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
            mScaleFactorX /= 2.0;
            mScaleFactorY /= 2.0;
            isZoomed = false;
        }
        else {
            mScaleFactorX *= 2.0;
            mScaleFactorY *= 2.0;
            isZoomed = true;
        }
        invalidate();
        return true;
    }

    public boolean Pan(MotionEvent start, float x, float y) {
        x = 3*(x/mScaleFactorX);
        y = 3*(y/mScaleFactorY);
        mCurrentCenterX += x;
        mCurrentCenterY += y;
        if (isInBoundsX() && isInBoundsY()) {
        }
        else if (isInBoundsX()) {
            mCurrentCenterY -=y;
        }
        else if (isInBoundsY()) {
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
        if (mCurrentCenterX >= 2560) {
            return false;
        }
        else if (mCurrentCenterX <= 0) {
            return false;
        }
        return true;
    }

    public boolean isInBoundsY() {
        if (mCurrentCenterY >= 1504) {
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

    public float getZoomX() {
        return mScaleFactorX;
    }

    public float getZoomY() {
        return mScaleFactorY;
    }

    public void setDimensions() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();  // deprecated
        int height = display.getHeight();

        mHeight = height;
        mWidth = width;
        mCurrentCenterX = mCenterX = (float)(2560/2.0);
        mCurrentCenterY = mCenterY = (float)(1504/2.0);
        if (mWidth < 2560 || mHeight < 1504) {
            mScaleFactorX = 2560 / (float) mWidth;
            mScaleFactorY = 1504 / (float) mHeight;
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.scale(mScaleFactorX, mScaleFactorY, mCurrentCenterX, mCurrentCenterY);
        canvas.save();
        super.dispatchDraw(canvas);
        canvas.save();
        canvas.restore();
    }

    /*
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
    */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setDimensions();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
