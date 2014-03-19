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
    private static float MIN_ZOOM = 1f;
    private static float MAX_ZOOM = 2f;

    private float mCenterX;
    private float mCenterY;
    private float mScaleFactor = 1.f;

    DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
    int displayWidth = metrics.widthPixels;
    int displayHeight = metrics.heightPixels;

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
        mCenterX = event.getX();
        mCenterY = event.getY();
        if (mScaleFactor == MAX_ZOOM) {
            mScaleFactor = MIN_ZOOM;
        }
        else {
            mScaleFactor = MAX_ZOOM;
        }
        invalidate();
        return true;
    }

    public boolean Pan(MotionEvent start, float x, float y) {
        mCenterX += x;
        mCenterY += y;
        if (isInBoundsX() && isInBoundsY()) {
        }
        else if (isInBoundsX() && !isInBoundsY()) {
            mCenterY -=y;
        }
        else if (!isInBoundsX() && isInBoundsY()) {
            mCenterX-=x;
        }
        else {
            mCenterX-=x;
            mCenterY -=y;
        }
        invalidate();
        return false;
    }

    public boolean isZoom() {
        if (mScaleFactor == 2) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isInBoundsX() {
        if (mCenterX >= 2560) {
            return false;
        }
        else if (mCenterX <= 0) {
            return false;
        }
        return true;
    }

    public boolean isInBoundsY() {
        if (mCenterY >= 1504) {
            return false;
        }
        else if (mCenterY <= 0) {
            return false;
        }
        return true;
    }

    public float getPanX() {
        return mCenterX;
    }

    public float getPanY() {
        return mCenterY;
    }

    public float getZoom() {
        return mScaleFactor;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.scale(mScaleFactor, mScaleFactor, mCenterX, mCenterY);
        canvas.save();
        super.dispatchDraw(canvas);
        canvas.restore();
    }
}
