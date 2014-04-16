package com.slothproductions.riskybusiness.view;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.View.R;
import com.slothproductions.riskybusiness.model.Board;
import com.slothproductions.riskybusiness.model.Coordinate;
import com.slothproductions.riskybusiness.model.Edge;
import com.slothproductions.riskybusiness.model.Hex;
import com.slothproductions.riskybusiness.model.MilitaryUnit;
import com.slothproductions.riskybusiness.model.Vertex;

import java.util.ArrayList;

//TODO: update all the methods to work with the new way of placing objects
//This includes adding functionality to work with roads, and adjusting the method signatures as appropriate.

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
    private BoardButtonsFragment mBoardButtonsFragment;

    //Arrays of each type of view element
    private ArrayList<ImageView> mSoldiers;
    private ArrayList<ImageView> mSettlements;
    private ArrayList<ImageView> mCities;
    private ArrayList<ImageView> mRoads;

    public BoardObjectManager(Board boardData, ZoomableLayout layout, Activity activity, BoardScreenMainFragment manager) {
        //initialize main variables
        mBoardBacklog = boardData;
        mBoardLayout = layout;
        mGameBoardActivity = activity;
        mManagingFragment = manager;
        mBoardButtonsFragment = (BoardButtonsFragment)((BoardScreen)mGameBoardActivity).getButtonsFragment();

        //Initialize lists
        mSoldiers = new ArrayList<ImageView>();
        mSettlements = new ArrayList<ImageView>();
        mCities = new ArrayList<ImageView>();
        mRoads = new ArrayList<ImageView>();

    }

    public void callActionFromMenuSelection(MenuItem action, Coordinate coordinate) {
        if (action.getItemId() == R.id.road) {
            //call build road from game loop
            buildItem(0, "road", coordinate);
        }
        else if (action.getItemId() == R.id.soldier) {
            buildItem(1, "soldier", coordinate);
        }
        else if (action.getItemId() == R.id.settlement) {
            buildItem(2, "settlement", coordinate);
        }
        else if (action.getItemId() == R.id.city) {
            //remove settlement at coordinate
            buildItem(3, "city", coordinate);
        }
        else {
            //move soldier
        }
    }

    public void buildItem(int identifier, String name, Coordinate coordinate) {
        //add Item to the appropriate vertex/edge

        //create the visible image and place it on the screen
        ImageView item = new ImageView(mGameBoardActivity);
        item.setId((int)System.currentTimeMillis());
        item.setImageResource(mGameBoardActivity.getResources().getIdentifier(name, "drawable", mGameBoardActivity.getPackageName()));
        item.setRotation(coordinate.getRotation());
        placeImage((int)coordinate.getX(), (int)coordinate.getY(), item);
        switch(identifier) {
            case 0:
                mRoads.add(item);
                break;
            case 1:
                mSoldiers.add(item);
                break;
            case 2:
                mSettlements.add(item);
                break;
            case 3:
                mCities.add(item);
                break;
        }
    }

    //Creates the appropriate menu for the screen based on the tap event
    public void findMenu(MotionEvent event) {

        //scan through hex corners to see if tap location matches a corner
        Coordinate c = new Coordinate(event.getX(), event.getY());
        Vertex v = null;
        Edge e = null;
        if (checkCornerLocations(c, v)) {
            //if this is true, then the coordinate is reassigned to the exact vertex location, and v is assigned to the vertex the object is being placed
            //we are now calling the show popup menu in board buttons fragment
            mBoardButtonsFragment.showPopUp(c, v);
        }
        else if (checkEdgeLocations(c, e)) {
            //need to apply right rotation at some point...
            mBoardButtonsFragment.showPopUp(c, e);
        }
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
        normalizeLevels();
    }


    /**Iterate through Hexes, and hex edges, checking edge locations, and seeing if tap location is a match
     * also checks to see if a location is available for an item to be placed.
     */
    public boolean checkEdgeLocations(Coordinate coordinate, Edge e) {
        //for all of the hexes, check to see if the location tapped is equal to the location of any of their corners
        for (int i = 0; i < mBoardBacklog.hexes.size(); i++) {
            Hex temp = mBoardBacklog.hexes.get(i);

            //grabbing the tile
            ImageView mTile = (ImageView) mBoardLayout.getChildAt(i + 1); // i+1 because background image is at i = 0;

            //tries adding to each of the corners, if it is a valid location, returns true, otherwise checks the rest of the corners and continues
            if (addTopLeftEdge(coordinate, mTile)) {
                //assign vertex v to index 0 of the hex temp's list
                return true;
            }
            if (addTopRightEdge(coordinate, mTile)) {
                //assign vertex v to index 1 of the hex temp's list
                return true;
            }
            if (addTopEdge(coordinate, mTile)) {
                //assign vertex v to index 2 of the hex temp's list
                return true;
            }
            if (addBottomLeftEdge(coordinate, mTile)) {
                //assign vertex v to index 3 of the hex temp's list
                return true;
            }
            if (addBottomRightEdge(coordinate, mTile)) {
                //assign vertex v to index 4 of the hex temp's list
                return true;
            }
            if (addBottomEdge(coordinate, mTile)) {
                //assign vertex v to index 5 of the hex temp's list
                return true;
            }
        }

        //object can't be placed, make toast
        mManagingFragment.createToast("Select the corner or edge of a tile for available actions", false);
        return false;
    }

    /**Iterate through Hexes, and hex vertices, checking vertex locations, and seeing if tap location is a match
     * if it is, then it return the vertex where the location is a match, the coordinate of the vertex, and true;
     */
    public boolean checkCornerLocations(Coordinate coordinate, Vertex v) {
        //adjust the given coordinate for zoom/pan
        coordinate.mapZoomCoordinates(mBoardLayout);

        //for all of the hexes, check to see if the location tapped is equal to the location of any of their corners
        for (int i =0; i < mBoardBacklog.hexes.size(); i++) {
            Hex temp = mBoardBacklog.hexes.get(i);

            //grabbing the tile
            ImageView mTile = (ImageView) mBoardLayout.getChildAt(i+1); // i+1 because background image is at i = 0;

            //tries adding to each of the corners, if it is a valid location, returns true, otherwise checks the rest of the corners and continues
            if (addTopLeftCorner(coordinate, mTile)) {
                //assign vertex v to index 0 of the hex temp's list
                return true;
            }
            if (addTopRightCorner(coordinate, mTile)) {
                //assign vertex v to index 1 of the hex temp's list
                return true;
            }
            if (addTopRightCorner(coordinate, mTile)) {
                //assign vertex v to index 2 of the hex temp's list
                return true;
            }
            if (addMidRightCorner(coordinate, mTile)) {
                //assign vertex v to index 3 of the hex temp's list
                return true;
            }
            if (addBottomRightCorner(coordinate, mTile)) {
                //assign vertex v to index 4 of the hex temp's list
                return true;
            }
            if (addBottomLeftCorner(coordinate, mTile)) {
                //assign vertex v to index 5 of the hex temp's list
                return true;
            }
            if (addMidLeftCorner(coordinate, mTile)) {
                //assign vertex v to index 6 of the hex temp's list
                return true;
            }
        }

        //Now corner location found for menu
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
     * @param coordinate the coordinate that holds the tap locations, and mapped values
     * @param mTile the tile that the image will be placed at the corner of
     * @return true if the object could be placed, false otherwise
     */
    boolean addTopLeftCorner(Coordinate coordinate, ImageView mTile) {
        int tapX = (int)coordinate.getX();
        int tapY = (int)coordinate.getY();

        //gets the location of top left vertex of tile
        int x = mTile.getLeft()+64;
        int y = mTile.getTop();

        //range that is valid location
        int lowX = x-35;
        int highX = x+35;
        int lowY = y-35;
        int highY = y+35;

        //compare tap x,y locations against valid x and y range for top corner
        if (tapX >= lowX && tapX <=highX && tapY>=lowY && tapY<=highY) {
            coordinate.setMappedX(x);
            coordinate.setMappedY(y);
            return true;
        }
        return false;
    }

    boolean addTopRightCorner(Coordinate coordinate, ImageView mTile) {
        int tapX = (int)coordinate.getX();
        int tapY = (int)coordinate.getY();

        //gets the location of Top Right vertex of tile
        int x = mTile.getLeft()+196;
        int y = mTile.getTop();

        //range that is valid location
        int lowX = x-35;
        int highX = x+35;
        int lowY = y-35;
        int highY = y+35;

        //compare tap x,y locations against valid x and y range for top corner
        if (tapX >= lowX && tapX <=highX && tapY>=lowY && tapY<=highY) {
            coordinate.setMappedX(x);
            coordinate.setMappedY(y);
            return true;
        }
        return false;
    }

    boolean addMidRightCorner(Coordinate coordinate, ImageView mTile) {
        int tapX = (int)coordinate.getX();
        int tapY = (int)coordinate.getY();

        //gets the location of Middle Right vertex of tile
        int x = mTile.getLeft()+260;
        int y = mTile.getTop()+112;

        //range that is valid location
        int lowX = x-35;
        int highX = x+35;
        int lowY = y-35;
        int highY = y+35;

        //compare tap x,y locations against valid x and y range for top corner
        if (tapX >= lowX && tapX <=highX && tapY>=lowY && tapY<=highY) {
            coordinate.setMappedX(x);
            coordinate.setMappedY(y);
            return true;
        }
        return false;
    }

    boolean addBottomRightCorner(Coordinate coordinate, ImageView mTile) {
        int tapX = (int)coordinate.getX();
        int tapY = (int)coordinate.getY();

        //gets the location of Bottom Right vertex of tile
        int x = mTile.getLeft()+196;
        int y = mTile.getTop()+224;

        //range that is valid location
        int lowX = x-35;
        int highX = x+35;
        int lowY = y-35;
        int highY = y+35;

        //compare tap x,y locations against valid x and y range for top corner
        if (tapX >= lowX && tapX <=highX && tapY>=lowY && tapY<=highY) {
            coordinate.setMappedX(x);
            coordinate.setMappedY(y);
            return true;
        }
        return false;
    }

    boolean addBottomLeftCorner(Coordinate coordinate, ImageView mTile) {
        int tapX = (int)coordinate.getX();
        int tapY = (int)coordinate.getY();

        //gets the location of Bottom Left vertex of tile
        int x = mTile.getLeft()+64;
        int y = mTile.getTop()+224;

        //range that is valid location
        int lowX = x-35;
        int highX = x+35;
        int lowY = y-35;
        int highY = y+35;

        //compare tap x,y locations against valid x and y range for top corner
        if (tapX >= lowX && tapX <=highX && tapY>=lowY && tapY<=highY) {
            coordinate.setMappedX(x);
            coordinate.setMappedY(y);
            return true;
        }
        return false;
    }

    boolean addMidLeftCorner(Coordinate coordinate, ImageView mTile) {
        int tapX = (int)coordinate.getX();
        int tapY = (int)coordinate.getY();

        //gets the location of Middle Left vertex of tile
        int x = mTile.getLeft();
        int y = mTile.getTop()+112;

        //range that is valid location
        int lowX = x-35;
        int highX = x+35;
        int lowY = y-35;
        int highY = y+35;

        //compare tap x,y locations against valid x and y range for top corner
        if (tapX >= lowX && tapX <=highX && tapY>=lowY && tapY<=highY) {
            coordinate.setMappedX(x);
            coordinate.setMappedY(y);
            return true;
        }
        return false;
    }

    boolean addTopEdge(Coordinate coordinate, ImageView mTile) {
        int tapX = (int)coordinate.getX();
        int tapY = (int)coordinate.getY();

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
            coordinate.setMappedX(x);
            coordinate.setMappedY(y);
            return true;
        }
        return false;
    }

    boolean addTopLeftEdge(Coordinate coordinate, ImageView mTile) {
        int tapX = (int)coordinate.getX();
        int tapY = (int)coordinate.getY();

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
            coordinate.setRotation(-60);
            coordinate.setMappedX(x);
            coordinate.setMappedY(y);
            return true;
        }
        return false;
    }

    boolean addTopRightEdge(Coordinate coordinate, ImageView mTile) {
        int tapX = (int)coordinate.getX();
        int tapY = (int)coordinate.getY();

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
            coordinate.setRotation(60);
            coordinate.setMappedX(x);
            coordinate.setMappedY(y);
            return true;
        }
        return false;
    }

    boolean addBottomRightEdge(Coordinate coordinate, ImageView mTile) {
        int tapX = (int)coordinate.getX();
        int tapY = (int)coordinate.getY();

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
            coordinate.setRotation(-60);
            coordinate.setMappedX(x);
            coordinate.setMappedY(y);
            return true;
        }
        return false;
    }

    boolean addBottomLeftEdge(Coordinate coordinate, ImageView mTile) {
        int tapX = (int)coordinate.getX();
        int tapY = (int)coordinate.getY();

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
            coordinate.setRotation(60);
            coordinate.setMappedX(x);
            coordinate.setMappedY(y);
            return true;
        }
        return false;
    }

    boolean addBottomEdge(Coordinate coordinate, ImageView mTile) {
        int tapX = (int)coordinate.getX();
        int tapY = (int)coordinate.getY();

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
            coordinate.setMappedX(x);
            coordinate.setMappedY(y);
            return true;
        }
        return false;
    }
    
}
