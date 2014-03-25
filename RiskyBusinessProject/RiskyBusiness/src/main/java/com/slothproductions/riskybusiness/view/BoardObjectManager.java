package com.slothproductions.riskybusiness.view;

import android.app.Activity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.View.R;
import com.slothproductions.riskybusiness.model.Board;
import com.slothproductions.riskybusiness.model.Coordinate;
import com.slothproductions.riskybusiness.model.Hex;
import com.slothproductions.riskybusiness.model.MilitaryUnit;

import java.util.ArrayList;

/**
 * The purpose of this class is to manage all the objects being placed on the board (ie, Cities, Settlements, Soldiers, Cities, etc..
 * Note that this is NOT a model object. The only data/algorithms that this class holds is strictly views/layouts, and methods for placement, etc.
 * This class still calls methods from the model Board class in order for that class to handle backend data. This class is essentially designed to clean up
 * the code in BoardScreenMain Fragment
 */
public class BoardObjectManager {
    private static final String TAG = "Board Object Manager";

    private Board mBoardBacklog;             //Model board class
    private ZoomableLayout mBoardLayout;     //Board Layout
    private Activity mGameBoardActivity;
    private BoardScreenMainFragment mManagingFragment;

    //Arrays of each type of view element
    private ArrayList<ImageView> mSoldiers;
    private ArrayList<ImageView> mSettlements;
    private ArrayList<ImageView> mCities;
    private ArrayList<ImageView> mRoads;

    private BoardObject mCurrentBuildItem;   //Last selected board object to be placed

    public BoardObjectManager(Board boardData, ZoomableLayout layout, Activity activity, BoardScreenMainFragment manager) {
        //initialize main variables
        mBoardBacklog = boardData;
        mBoardLayout = layout;
        mGameBoardActivity = activity;
        mManagingFragment = manager;

        //Initialize lists
        mSoldiers = new ArrayList<ImageView>();
        mSettlements = new ArrayList<ImageView>();
        mCities = new ArrayList<ImageView>();
        mRoads = new ArrayList<ImageView>();

        //set current build item
        mCurrentBuildItem = BoardObject.NONE;
    }

    public void setCurrentBuildItem(MenuItem newBuildItem) {
        if (newBuildItem.getItemId() == R.id.road) {
            mCurrentBuildItem = BoardObject.ROAD;
        }
        else if (newBuildItem.getItemId() == R.id.soldier) {
            mCurrentBuildItem = BoardObject.SOLDIER;
        }
        else if (newBuildItem.getItemId() == R.id.settlement) {
            mCurrentBuildItem = BoardObject.SETTLEMENT;
        }
        else {
            mCurrentBuildItem = BoardObject.CITY;
        }
    }

    public void BuildItem(MotionEvent event) {
        ImageView item = new ImageView(mGameBoardActivity);
        item.setId((int)System.currentTimeMillis());
        switch (mCurrentBuildItem) {
            case NONE:
                Log.d(TAG, "No Build Item Selected");
                break;
            case ROAD:
                Log.d(TAG, "Road Will be Placed at Tap Location");
                item.setImageResource(mGameBoardActivity.getResources().getIdentifier("road", "drawable", mGameBoardActivity.getPackageName()));
                if (placeSideObject(event, item)) {
                    mRoads.add(item);
                    normalizeLevels();
                }
                break;
            case SOLDIER:
                Log.d(TAG, "Soldier will be Placed at Tap Location");
                item.setImageResource(mGameBoardActivity.getResources().getIdentifier("soldier", "drawable", mGameBoardActivity.getPackageName()));
                if (placeCornerObject(event, item)) {
                    mSoldiers.add(item);
                }
                break;
            case SETTLEMENT:
                Log.d(TAG, "BuildingType will be Placed at Tap Location");
                item.setImageResource(mGameBoardActivity.getResources().getIdentifier("settlement", "drawable", mGameBoardActivity.getPackageName()));
                if (placeCornerObject(event, item)) {
                    mSettlements.add(item);
                }
                break;
            case CITY:
                Log.d(TAG, "City will be Placed at Tap Location");
                item.setImageResource(mGameBoardActivity.getResources().getIdentifier("city", "drawable", mGameBoardActivity.getPackageName()));
                if (placeCornerObject(event, item)) {
                    mCities.add(item);
                }
                break;
        }

        mCurrentBuildItem = BoardObject.NONE;
    }

    /**places given image centered at the specified x and y coordinates
     *
     * @param x The x coordinate for item placement
     * @param y The y coordinate for item placement
     * @param image The item that will be placed on the screen
     */
    public void placeImage(int x, int y, ImageView image) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        //center object on location with margins
        lp.leftMargin = x-(image.getWidth()/2) - image.getDrawable().getIntrinsicWidth()/2;
        lp.topMargin = y-(image.getHeight()/2) - image.getDrawable().getIntrinsicHeight()/2;

        mBoardLayout.addView(image, lp);
    }

    /**Iterate through Hexes, and hex edges, checking edge locations, and seeing if tap location is a match
     * also checks to see if a location is available for an item to be placed.
     *
     * @param tapEvent the event for the tap, retrieved from the onTouchEvent method
     * @param mSideObject the Object that will be placed at the corner
     * @return true if the object could be placed, false otherwise
     */
    public boolean placeSideObject(MotionEvent tapEvent, ImageView mSideObject) {
        int x,y;
        //get location of x and y taps, and adjust for zoom/pan
        Coordinate coordinate = new Coordinate(tapEvent.getX(),tapEvent.getY());
        if (mBoardLayout.isZoom()) {
            coordinate.mapZoomCoordinates(mBoardLayout);
        }
        x = (int)coordinate.getX();
        y = (int)coordinate.getY();

        //for all of the hexes, check to see if the location tapped is equal to the location of any of their corners
        for (int i =0; i < mBoardBacklog.hexes.size(); i++) {
            Hex temp = mBoardBacklog.hexes.get(i);
            for (int j = 0; j < 6; j++) {
                //check to see if vertex is available to be checked, then checks location compared to tap.
            }
            //grabbing the tile
            ImageView mTile = (ImageView) mBoardLayout.getChildAt(i);

            //tries adding to each of the corners, if it is a valid location, returns true, otherwise checks the rest of the corners and continues
            if (addTopLeftEdge(x, y, mTile, mSideObject) || addTopRightEdge(x, y, mTile, mSideObject) || addTopEdge(x, y, mTile, mSideObject)
                    || addBottomRightEdge(x, y, mTile, mSideObject) || addBottomLeftEdge(x, y, mTile, mSideObject) || addBottomEdge(x, y, mTile, mSideObject)) {
                return true;
            }
        }

        //object can't be placed, make toast
        mManagingFragment.createToast("Invalid Object Placement (needs to be placed on the edge of a tile)", false);

        return false;
    }

    /**Iterate through Hexes, and hex vertices, checking vertex locations, and seeing if tap location is a match
     * also checks to see if a location is available for an item to be placed.
     *
     * @param tapEvent the event for the tap, retrieved from the onTouchEvent method
     * @param mCornerObject the Object that will be placed at the corner
     * @return true if the object could be placed, false otherwise
     */
    public boolean placeCornerObject(MotionEvent tapEvent, ImageView mCornerObject) {
        int x,y;
        //get location of x and y taps, and adjust for zoom/pan
        Coordinate coordinate = new Coordinate(tapEvent.getX(),tapEvent.getY());
        if (mBoardLayout.isZoom()) {
            coordinate.mapZoomCoordinates(mBoardLayout);
        }
        x = (int)coordinate.getX();
        y = (int)coordinate.getY();

        //for all of the hexes, check to see if the location tapped is equal to the location of any of their corners
        for (int i =0; i < mBoardBacklog.hexes.size(); i++) {
            Hex temp = mBoardBacklog.hexes.get(i);
            for (int j = 0; j < 6; j++) {
                //check to see if vertex is available to be checked, then checks location compared to tap.
            }
            //grabbing the tile
            ImageView mTile = (ImageView) mBoardLayout.getChildAt(i);

            //tries adding to each of the corners, if it is a valid location, returns true, otherwise checks the rest of the corners and continues
            if (addTopLeftCorner(x, y, mTile, mCornerObject) || addTopRightCorner(x, y, mTile, mCornerObject) || addMidRightCorner(x, y, mTile, mCornerObject)
                    || addBottomRightCorner(x, y, mTile, mCornerObject) || addBottomLeftCorner(x, y, mTile, mCornerObject) || addMidLeftCorner(x, y, mTile, mCornerObject)) {
                return true;
            }
        }

        //object can't be placed, make toast
        mManagingFragment.createToast("Invalid Object Placement (needs to be placed on the corner of a tile)", false);

        return false;
    }

    /**
     * normalizes the z-index levels of game board objects
     */
    public void normalizeLevels() {
        bringToFront(mSettlements);
        bringToFront(mCities);
        bringToFront(mSoldiers);
    }

    /**
     * brings an array of imageviews to the front of the screen (z-index)
     * @param images
     */
    public void bringToFront(ArrayList<ImageView> images) {
        for (int i =0; i < images.size(); i++) {
            images.get(i).bringToFront();
        }
    }


    /**Places a specified object at the top left corner of a specified tile if the tap location
     * is close enough to the corner of the object
     *
     * NOTE: The other add..Corner() methods do the same thing, but with different corners
     *
     * @param tapX the x coordinate for the tap
     * @param tapY the y coordinate for the tap
     * @param mTile the tile that the image will be placed at the corner of
     * @param mCornerObject The object that will be placed at the corner
     * @return true if the object could be placed, false otherwise
     */
    boolean addTopLeftCorner(int tapX, int tapY, ImageView mTile, ImageView mCornerObject) {
        //gets the location of top left vertex of tile
        int x = mTile.getLeft()+64;
        int y = mTile.getTop();

        //range that is valid location
        int lowX = x-50;
        int highX = x+50;
        int lowY = y-50;
        int highY = y+50;

        //compare tap x,y locations against valid x and y range for top corner
        if (tapX >= lowX && tapX <=highX && tapY>=lowY && tapY<=highY) {
            placeImage(x, y, mCornerObject);
            return true;
        }
        return false;
    }

    boolean addTopRightCorner(int tapX, int tapY, ImageView mTile, ImageView mCornerObject) {
        //gets the location of Top Right vertex of tile
        int x = mTile.getLeft()+196;
        int y = mTile.getTop();

        //range that is valid location
        int lowX = x-50;
        int highX = x+50;
        int lowY = y-50;
        int highY = y+50;

        //compare tap x,y locations against valid x and y range for corner
        if (tapX >= lowX && tapX <=highX && tapY>=lowY && tapY<=highY) {
            placeImage(x, y, mCornerObject);
            return true;
        }
        return false;
    }

    boolean addMidRightCorner(int tapX, int tapY, ImageView mTile, ImageView mCornerObject) {
        //gets the location of Middle Right vertex of tile
        int x = mTile.getLeft()+260;
        int y = mTile.getTop()+112;

        //range that is valid location
        int lowX = x-50;
        int highX = x+50;
        int lowY = y-50;
        int highY = y+50;

        //compare tap x,y locations against valid x and y range for corner
        if (tapX >= lowX && tapX <=highX && tapY>=lowY && tapY<=highY) {
            placeImage(x, y, mCornerObject);
            return true;
        }
        return false;
    }

    boolean addBottomRightCorner(int tapX, int tapY, ImageView mTile, ImageView mCornerObject) {
        //gets the location of Bottom Right vertex of tile
        int x = mTile.getLeft()+196;
        int y = mTile.getTop()+224;

        //range that is valid location
        int lowX = x-50;
        int highX = x+50;
        int lowY = y-50;
        int highY = y+50;

        //compare tap x,y locations against valid x and y range for corner
        if (tapX >= lowX && tapX <=highX && tapY>=lowY && tapY<=highY) {
            placeImage(x, y, mCornerObject);
            return true;
        }
        return false;
    }

    boolean addBottomLeftCorner(int tapX, int tapY, ImageView mTile, ImageView mCornerObject) {
        //gets the location of Bottom Left vertex of tile
        int x = mTile.getLeft()+64;
        int y = mTile.getTop()+224;

        //range that is valid location
        int lowX = x-50;
        int highX = x+50;
        int lowY = y-50;
        int highY = y+50;

        //compare tap x,y locations against valid x and y range for corner
        if (tapX >= lowX && tapX <=highX && tapY>=lowY && tapY<=highY) {
            placeImage(x, y, mCornerObject);
            return true;
        }
        return false;
    }

    boolean addMidLeftCorner(int tapX, int tapY, ImageView mTile, ImageView mCornerObject) {
        //gets the location of Middle Left vertex of tile
        int x = mTile.getLeft();
        int y = mTile.getTop()+112;

        //range that is valid location
        int lowX = x-50;
        int highX = x+50;
        int lowY = y-50;
        int highY = y+50;

        //compare tap x,y locations against valid x and y range for corner
        if (tapX >= lowX && tapX <=highX && tapY>=lowY && tapY<=highY) {
            placeImage(x, y, mCornerObject);
            return true;
        }
        return false;
    }

    boolean addTopEdge(int tapX, int tapY, ImageView mTile, ImageView mSideObject) {
        //gets the location of Top edge of a tile
        int x = mTile.getLeft()+130;
        int y = mTile.getTop();

        //range that is valid location
        int lowX = x-50;
        int highX = x+50;
        int lowY = y-20;
        int highY = y+20;

        //compare tap x,y locations against valid x and y range for corner
        if (tapX >= lowX && tapX <=highX && tapY>=lowY && tapY<=highY) {
            placeImage(x, y, mSideObject);
            return true;
        }
        return false;
    }

    boolean addTopLeftEdge(int tapX, int tapY, ImageView mTile, ImageView mSideObject) {
        //gets the location of top left edge of tile
        int x = mTile.getLeft()+32;
        int y = mTile.getTop()+56;

        //range that is valid location
        int lowX = x-20;
        int highX = x+20;
        int lowY = y-50;
        int highY = y+50;

        //compare tap x,y locations against valid x and y range for top left edge
        if (tapX >= lowX && tapX <=highX && tapY>=lowY && tapY<=highY) {
            mSideObject.setRotation(-60);
            placeImage(x, y, mSideObject);
            return true;
        }
        return false;
    }

    boolean addTopRightEdge(int tapX, int tapY, ImageView mTile, ImageView mSideObject) {
        //gets the location of Top Right edge of tile
        int x = mTile.getLeft()+228;
        int y = mTile.getTop()+56;

        //range that is valid location
        int lowX = x-20;
        int highX = x+20;
        int lowY = y-50;
        int highY = y+50;

        //compare tap x,y locations against valid x and y range for edge
        if (tapX >= lowX && tapX <=highX && tapY>=lowY && tapY<=highY) {
            mSideObject.setRotation(60);
            placeImage(x, y, mSideObject);
            return true;
        }
        return false;
    }

    boolean addBottomRightEdge(int tapX, int tapY, ImageView mTile, ImageView mSideObject) {
        //gets the location of Bottom Right edge of tile
        int x = mTile.getLeft()+228;
        int y = mTile.getTop()+168;

        //range that is valid location
        int lowX = x-20;
        int highX = x+20;
        int lowY = y-50;
        int highY = y+50;

        //compare tap x,y locations against valid x and y range for edge
        if (tapX >= lowX && tapX <=highX && tapY>=lowY && tapY<=highY) {
            mSideObject.setRotation(-60);
            placeImage(x, y, mSideObject);
            return true;
        }
        return false;
    }

    boolean addBottomLeftEdge(int tapX, int tapY, ImageView mTile, ImageView mSideObject) {
        //gets the location of Bottom Left edge of tile
        int x = mTile.getLeft()+32;
        int y = mTile.getTop()+168;

        //range that is valid location
        int lowX = x-20;
        int highX = x+20;
        int lowY = y-50;
        int highY = y+50;

        //compare tap x,y locations against valid x and y range for edge
        if (tapX >= lowX && tapX <=highX && tapY>=lowY && tapY<=highY) {
            mSideObject.setRotation(60);
            placeImage(x, y, mSideObject);
            return true;
        }
        return false;
    }

    boolean addBottomEdge(int tapX, int tapY, ImageView mTile, ImageView mSideObject) {
        //gets the location of the middle of the bottom edge of tile
        int x = mTile.getLeft()+130;
        int y = mTile.getTop()+224;

        //range that is valid location
        int lowX = x-50;
        int highX = x+50;
        int lowY = y-20;
        int highY = y+20;

        //compare tap x,y locations against valid x and y range for edge
        if (tapX >= lowX && tapX <=highX && tapY>=lowY && tapY<=highY) {
            placeImage(x, y, mSideObject);
            return true;
        }
        return false;
    }
    
}
