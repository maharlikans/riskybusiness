package com.slothproductions.riskybusiness.model;

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

    //this is spread out just so that I can see what is going on.
    public void mapZoomCoordinates(int zoomLevel) {
        x = x-128; //adjust for padding
        x = x-1280; //subtract center x
        x = x/zoomLevel; //adjust for zoom
        x = x + 1280; //re add center
        x = x - 64; // adjust for padding/2

        y = y-32; //adjust for padding
        y = y-752; //subtract center x
        y = y/zoomLevel; //adjust for zoom
        y = y + 752; //re add center
        y = y - 16; // adjust for padding/2
    }

}
