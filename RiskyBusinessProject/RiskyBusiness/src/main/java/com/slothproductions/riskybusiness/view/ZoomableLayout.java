package com.slothproductions.riskybusiness.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.RelativeLayout;

import java.io.Console;
import java.util.jar.Attributes;

public class ZoomableLayout extends RelativeLayout {
    private static String TAG = "Zoom Layout";
    private static float MIN_ZOOM = 1f;
    private static float MAX_ZOOM = 5f;


    private float scaleFactor =1.f;
    private ScaleGestureDetector detector;

    public ZoomableLayout(Context context) {
        super(context);
        setWillNotDraw(false);
        detector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    public ZoomableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        detector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    public ZoomableLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWillNotDraw(false);
        detector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "ZoomView Detected Touch Event");
        detector.onTouchEvent(event);
        super.onTouchEvent(event);
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.scale(scaleFactor, scaleFactor);
        canvas.save();
        Log.d(TAG, "ZoomView Drawing Scaled Canvas");
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.d(TAG, "ZoomView Setting Scale Factor");
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM));
            invalidate();
            return true;
        }
    }
}
