package com.slothproductions.riskybusiness.model;

import android.util.Log;

import com.slothproductions.riskybusiness.view.ZoomableLayout;


public class Coordinate {
    private static final String TAG = "Coordinate";
    private float x;
    private float y;
    private float unMappedX;
    private float unMappedY;

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

    /**Maps the coordinates from the location tapped on the screen to the given layout
     * takes into account zoom and pan factor. Everything is spread out so it is easy to see what is going on.
     *
     * @param layout
     */
    public void mapZoomCoordinates(ZoomableLayout layout) {
        float zoomLevelX = layout.getZoomX();
        float zoomLevelY = layout.getZoomY();
        float baseZoomX = layout.getBaseZoomX();
        float baseZoomY = layout.getBaseZoomY();

        Log.d(TAG, "Zoom Level X: " + zoomLevelX);
        Log.d(TAG, "Zoom Level Y: " + zoomLevelY);

        //adjust x and y coordinates for physical tap on larger screen
        x*=baseZoomX;
        y*=baseZoomY;

        float centerX = layout.getPanX();
        x = x-centerX; //subtract center x
        x = x/zoomLevelX; //adjust for zoom
        x = x + centerX; //re add center

        float centerY = layout.getPanY();
        y = y-centerY; //subtract center y
        y = y/zoomLevelY; //adjust for zoom
        y = y + centerY; //re add center
    }

}
