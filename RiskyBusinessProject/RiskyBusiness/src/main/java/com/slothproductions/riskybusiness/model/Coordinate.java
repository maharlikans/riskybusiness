package com.slothproductions.riskybusiness.model;

import com.slothproductions.riskybusiness.view.ZoomableLayout;

/**
 * Created by Joseph on 3/19/14.
 */
public class Coordinate {
    private float x;
    private float y;

    public Coordinate (float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    /**Maps the coordinates from the location tapped on the screen to the given layout
     * takes into account zoom and pan factor. Everything is spread out so it is easy to see what is going on.
     *
     * @param layout
     */
    public void mapZoomCoordinates(ZoomableLayout layout) {
        int zoomLevel = (int)layout.getZoom();

        float centerX = layout.getPanX();
        x = x-128; //adjust for padding
        x = x-centerX; //subtract center x
        x = x/zoomLevel; //adjust for zoom
        x = x + centerX; //re add center
        x = x - 64; // adjust for padding/2

        float centerY = layout.getPanY();
        y = y-32; //adjust for padding
        y = y-centerY; //subtract center x
        y = y/zoomLevel; //adjust for zoom
        y = y + centerY; //re add center
        y = y - 16; // adjust for padding/2
    }

}
