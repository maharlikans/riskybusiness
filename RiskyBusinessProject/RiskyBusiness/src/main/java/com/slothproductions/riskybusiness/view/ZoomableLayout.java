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
        if (mScaleFactor == 2) {
            mScaleFactor = 1;
        }
        else {
            mScaleFactor = 2;
        }
        invalidate();
        return true;
    }

    public boolean Pan(MotionEvent start, float x, float y) {
        mCenterX += x;
        mCenterY += y;
        invalidate();
        return true;
    }

    public boolean isZoom() {
        if (mScaleFactor == 2) {
            return true;
        }
        else {
            return false;
        }
    }

    public float getPanX() {
        return mCenterX;
    }

    public float getPanY() {
        return mCenterY;
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
