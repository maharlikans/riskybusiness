package com.slothproductions.riskybusiness.model;

import com.slothproductions.riskybusiness.view.ZoomableLayout;


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
        int zoomLevelX = (int)layout.getZoomX();
        int zoomLevelY = (int)layout.getZoomY();

        float centerX = layout.getPanX();
        x = x-centerX; //subtract center x
        x = x/zoomLevelX; //adjust for zoom
        x = x + centerX; //re add center

        float centerY = layout.getPanY();
        y = y-centerY; //subtract center x
        y = y/zoomLevelY; //adjust for zoom
        y = y + centerY; //re add center
    }

}
