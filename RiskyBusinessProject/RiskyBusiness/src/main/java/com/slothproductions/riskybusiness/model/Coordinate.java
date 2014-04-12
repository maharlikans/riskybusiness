package com.slothproductions.riskybusiness.model;

import android.util.Log;

import com.slothproductions.riskybusiness.view.ZoomableLayout;


public class Coordinate {
    private static final String TAG = "Coordinate";
    private float x;
    private float y;
    private float unMappedX;
    private float unMappedY;

    //information about zoom and pan needed to map the coordinate
    private float mZoomLevelX;
    private float mZoomLevelY;
    private float mBaseZoomX;
    private float mBaseZoomY;
    private float mCenterX;
    private float mCenterY;

    public Coordinate (float x, float y) {
        this.x = unMappedX = x;
        this.y = unMappedY = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getUnMappedX() {
        return unMappedX;
    }

    public float getUnMappedY() {
        return unMappedY;
    }

    public void setMappedX(float x) {
        this.x = x;
        unMapCoordinates();
    }

    public void setMappedY(float y) {
        this.y = y;
        unMapCoordinates();
    }

    /**Maps the coordinates from the location tapped on the screen to the given layout
     * takes into account zoom and pan factor. Everything is spread out so it is easy to see what is going on.
     *
     * @param layout
     */
    public void mapZoomCoordinates(ZoomableLayout layout) {
        mZoomLevelX = layout.getZoomX();
        mZoomLevelY = layout.getZoomY();
        mBaseZoomX = layout.getBaseZoomX();
        mBaseZoomY = layout.getBaseZoomY();

        Log.d(TAG, "Zoom Level X: " + mZoomLevelX);
        Log.d(TAG, "Zoom Level Y: " + mZoomLevelY);

        //adjust x and y coordinates for physical tap on larger screen
        x*=mBaseZoomX;
        y*=mBaseZoomY;

        mCenterX = layout.getPanX();
        x = x-mCenterX; //subtract center x
        x = x/mZoomLevelX; //adjust for zoom
        x = x + mCenterX; //re add center

        mCenterY = layout.getPanY();
        y = y-mCenterY; //subtract center y
        y = y/mZoomLevelY; //adjust for zoom
        y = y + mCenterY; //re add center
    }

    public void unMapCoordinates() {
        unMappedX = x - mCenterX;
        unMappedX = unMappedX * mZoomLevelX;
        unMappedX = unMappedX + mCenterX;
        unMappedX = unMappedX / mBaseZoomX;

        unMappedY = y - mCenterY;
        unMappedY = unMappedY * mZoomLevelY;
        unMappedY = unMappedY + mCenterY;
        unMappedY = unMappedY / mBaseZoomY;
    }
}
