package com.slothproductions.riskybusiness.view;

import android.content.Context;
import android.graphics.Canvas;
import android.nfc.Tag;
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

import com.slothproductions.riskybusiness.model.Coordinate;

import java.io.Console;
import java.util.jar.Attributes;

public class ZoomableLayout extends RelativeLayout {
    private static String TAG = "Zoom Layout";

    //These two constants specify the default zoom for the layout. The default zoom is the amount needed to properly adjust the size to the screen.
    private float BASE_ZOOM_X;
    private float BASE_ZOOM_Y;

    boolean isZoomed;

    private float mCenterX;
    private float mCenterY;

    //the current center position of the layout. Used for panning, and mapping coordinates
    private float mCurrentCenterX;
    private float mCurrentCenterY;

    //Scale factor in the x and y direction of the layout
    private float mScaleFactorX = 1.f;
    private float mScaleFactorY = 1.f;

    //used to ensure setDimension is only called once
    private boolean defaultDimensionsSet;

    //screen width and height
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

    /**
     * Zooms in the layout centered on the touch location of the event
     * @param event The motionevent that starts the zoom. The tap locations are used to determine where the new zoomed in or out view will be centered
     * @return true if it was able to zoom (right now it always is)
     *
     */
    public boolean Zoom(MotionEvent event) {
        Log.d(TAG, "Zoom Called");

        //maps the coordinates to the proper location on the layout
        Coordinate mapCoord = new Coordinate(event.getX(), event.getY());
        mapCoord.mapZoomCoordinates(this);
        mCurrentCenterX = mapCoord.getX();
        mCurrentCenterY = mapCoord.getY();

        //if zoomed out, zoom in, otherwise, zoom out.
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
        //redraw the canvas
        invalidate();
        return true;
    }

    /**
     * Called when a scroll movement is detected. Moves the image around opposite the direction of the scroll (natural pan direction).
     * @param start
     * @param x the horizontal distance from the previous touch location
     * @param y the vertical distance from the previous touch location
     * @return true if the image could be panned, false if both the x and y bounds have been reached, and the image cannot be panned;
     */
    public boolean Pan(MotionEvent start, float x, float y) {
        //this is to account for a difference in scale factor, so the pan is always the same speed
        x = 3*(x/mScaleFactorX);
        y = 3*(y/mScaleFactorY);

        //adjust the new center for pan
        mCurrentCenterX += x;
        mCurrentCenterY += y;

        //checks the bounds of the new location, and readjusts if it is not valid
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
            return false;
        }
        //redraws the canvas
        invalidate();
        return true;
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
        Log.d(TAG, "Current X Center: " + mCurrentCenterX);
        return mCurrentCenterX;
    }

    public float getPanY() {
        Log.d(TAG, "Current Y Center: " + mCurrentCenterY);
        return mCurrentCenterY;
    }

    public float getZoomX() {
        return mScaleFactorX;
    }

    public float getZoomY() {
        return mScaleFactorY;
    }

    public float getBaseZoomX() {
        return BASE_ZOOM_X;
    }

    public float getBaseZoomY() {
        return BASE_ZOOM_Y;
    }

    /**
     * Sets the dimensions of the layout. Note: this should only be called once
     * Assigns screen width, screen height, base zoom, center, and currentcenter.
     *
     */
    public void setDimensions() {
        //make sure this method is not called more than once
        if (defaultDimensionsSet) {
            return;
        }
        defaultDimensionsSet = true;

        Log.d(TAG, "Setting Dimensions");

        //Gets the display size
        DisplayMetrics display = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(display);
        mWidth = display.widthPixels;
        mHeight = display.heightPixels;

        BASE_ZOOM_X = 1;
        BASE_ZOOM_Y = 1;

        //assigns the center location. This is always the same, regardless of screen size
        mCurrentCenterX = mCenterX = (float)(2560/2.0);
        mCurrentCenterY = mCenterY = (float)(1504/2.0);

        //sets the base zoom factor based on the screen dimensions, compared to the actual layout size
        if (mWidth < 2560 || mHeight < 1504) {
            BASE_ZOOM_X = mScaleFactorX = 2560 / (float) mWidth;
            BASE_ZOOM_Y = mScaleFactorY = 1504 / (float) mHeight;
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
        canvas.scale(mScaleFactorX, mScaleFactorY, mCurrentCenterX, mCurrentCenterY);
        canvas.save();
        super.dispatchDraw(canvas);
        canvas.save();
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!defaultDimensionsSet) {
            setDimensions();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
