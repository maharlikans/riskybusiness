package com.slothproductions.riskybusiness.view;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
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
import com.slothproductions.riskybusiness.model.GameLoop;
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
    private GameLoop mGameLoop;
    private Activity mGameBoardActivity;
    private BoardScreenMainFragment mManagingFragment;
    private BoardButtonsFragment mBoardButtonsFragment;

    //Arrays of each type of view element
    private ArrayList<ImageView> mSoldiers;
    private ArrayList<ImageView> mSettlements;
    private ArrayList<ImageView> mCities;
    private ArrayList<ImageView> mRoads;

    boolean mSoldierAction;

    //used for determining which edge and vertex to use for object placement
    private Vertex v;
    private Edge e;

    //for soldier movement
    private Vertex startVertex;
    private ImageView mSoldierMoving;

    private ArrayList<Hex> mAdjacentHexes;

    int vertexIndexAdded;
    int edgeIndexAdded;
    int hexIndexAdded;

    public BoardObjectManager(Board boardData, ZoomableLayout layout, Activity activity, BoardScreenMainFragment manager) {
        //initialize main variables
        mBoardBacklog = boardData;
        mBoardLayout = layout;
        mGameBoardActivity = activity;
        mManagingFragment = manager;
        mBoardButtonsFragment = (BoardButtonsFragment)((BoardScreen)mGameBoardActivity).getButtonsFragment();
        mGameLoop = ((BoardScreen) mGameBoardActivity).getGameLoop();

        //Initialize lists
        mSoldiers = new ArrayList<ImageView>();
        mSettlements = new ArrayList<ImageView>();
        mCities = new ArrayList<ImageView>();
        mRoads = new ArrayList<ImageView>();

        mAdjacentHexes = new ArrayList<Hex>();
    }

    //This method is called from the board buttons class. Based on the menu item selected, it will call the appropriate action
    //Note: to make this work with the model, it may need to take an edge or vertex
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
            removeSettlement(coordinate);
            buildItem(3, "city", coordinate);
        }
        else if (action.getItemId() == R.id.repairsettlement) {
            //repair settlement
        }
        else if (action.getItemId() == R.id.repaircity) {
            //repaircity
        }
        else {
            setMoveSoldierState(coordinate);
        }
    }

    //this method builds an item on the screen at the given coordinate
    //this should probably take an enum instead of a string.. it is probably fine as is
    public void buildItem(int identifier, String name, Coordinate coordinate) {
        //add Item to the appropriate vertex/edge

        //create the visible image and place it on the screen
        ImageView item = new ImageView(mGameBoardActivity);
        item.setId((int)System.currentTimeMillis());
        item.setImageResource(mGameBoardActivity.getResources().getIdentifier(name, "drawable", mGameBoardActivity.getPackageName()));
        item.setRotation(coordinate.getRotation());

        //places the image on the screen
        placeImage((int) coordinate.getX(), (int) coordinate.getY(), item);

        //this should be unnecessary, should be able to do this elsewhere.
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

        //this makes sure that all the objects are placed at the proper z index
        normalizeLevels();
    }

    //move soldier
    public void setMoveSoldierState(Coordinate coordinate) {
        mSoldierAction = true;
        startVertex = v;
        ImageView tempSoldier;
        float x;
        float y;
        for (int i = 0; i < mSoldiers.size(); i ++) {
            tempSoldier = mSoldiers.get(i);
            x = ((int)coordinate.getX())-(tempSoldier.getWidth()/2) - tempSoldier.getDrawable().getIntrinsicWidth()/2;
            y = ((int)coordinate.getY())-(tempSoldier.getHeight()/2) - tempSoldier.getDrawable().getIntrinsicHeight()/2;
            if (x == tempSoldier.getLeft()) {
                if (y == tempSoldier.getTop()) {
                    mSoldierMoving = tempSoldier;
                    return;
                }
            }
        }

        //if we get this far, there is a problem with the above code...
    }

    public void moveSoldier(Coordinate c) {
        mSoldierAction = false;
        checkCornerLocations(c);
        if (mAdjacentHexes.size() == 1) {
            assignVertexFromIndex();
        }
        mGameLoop.moveSoldier(startVertex, v);
        translateImage((int) c.getX(), (int) c.getY(), mSoldierMoving);
    }

    public void removeSettlement(Coordinate coordinate) {
        ImageView tempSettlement;
        float x;
        float y;
        for (int i = 0; i < mSettlements.size(); i ++) {
            tempSettlement = mSettlements.get(i);
            x = ((int)coordinate.getX())-(tempSettlement.getWidth()/2) - tempSettlement.getDrawable().getIntrinsicWidth()/2;
            y = ((int)coordinate.getY())-(tempSettlement.getHeight()/2) - tempSettlement.getDrawable().getIntrinsicHeight()/2;
            if (x == tempSettlement.getLeft()) {
                if (y == tempSettlement.getTop()) {
                    mBoardLayout.removeView(tempSettlement);
                    mSettlements.remove(i);
                    return;
                }
            }
        }
    }

    //Creates the appropriate menu for the screen based on the tap event
    //This will call the popup menu in the BoardButtons class
    public void findMenu(MotionEvent event) {
        Coordinate c = new Coordinate(event.getX(), event.getY());

        if (mSoldierAction) {
            moveSoldier(c);
            return;
        }

        //scan through hex corners to see if tap location matches a corner
        v = null;
        e = null;
        if (checkCornerLocations(c)) {
            if (mAdjacentHexes.size() == 1) {
                assignVertexFromIndex();
            }
            else {
                v = mBoardBacklog.getVertex(mAdjacentHexes, 0);
            }
            // popup menu in board buttons fragment
            mBoardButtonsFragment.showPopUp(c, v);
        }
        else if (checkEdgeLocations(c)) {
            if (mAdjacentHexes.size() == 1) {
                assignEdgeFromIndex();
            }
            else {
                mBoardBacklog.getEdge(mAdjacentHexes, 0);
            }
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
    }

    //im sure this will need some fixing..
    public void translateImage(int newX, int newY, ImageView image) {
        newX = newX-(image.getWidth()/2) - image.getDrawable().getIntrinsicWidth()/2;
        newY = newY-(image.getHeight()/2) - image.getDrawable().getIntrinsicHeight()/2;
        int oldX = image.getLeft();
        int oldY = image.getTop();

        Animation translation = new TranslateAnimation(oldX, newX, oldY, newY);
        translation.setDuration(1000);
        translation.setFillBefore(true);
        translation.setFillAfter(true);
        translation.setFillEnabled(true);
        image.startAnimation(translation);
    }

    /**Iterate through Hexes, and hex vertices, checking vertex locations, and seeing if tap location is a match
     * if it is, then it return the vertex where the location is a match, the coordinate of the vertex, and true;
     */
    public boolean checkCornerLocations(Coordinate coordinate) {
        mAdjacentHexes.removeAll(mAdjacentHexes);
        vertexIndexAdded = -1;
        hexIndexAdded = -1;

        //adjust the given coordinate for zoom/pan
        coordinate.mapZoomCoordinates(mBoardLayout);

        //for all of the hexes, check to see if the location tapped is equal to the location of any of their corners
        for (int i =0; i < mBoardBacklog.getHexesSize(); i++) {
            Hex tempHex = mBoardBacklog.getHex(i);

            //grabbing the tile
            ImageView mTile = (ImageView) mBoardLayout.getChildAt(i+1); // i+1 because background image is at i = 0;

            //tries adding to each of the corners, if it is a valid location, returns true, otherwise checks the rest of the corners and continues
            if (addTopRightCorner(coordinate, mTile)) {
                mAdjacentHexes.add(tempHex);
                vertexIndexAdded = 0;
                hexIndexAdded = i;
                continue;
            }
            else if (addMidRightCorner(coordinate, mTile)) {
                mAdjacentHexes.add(tempHex);
                vertexIndexAdded = 1;
                hexIndexAdded = i;
                continue;
            }
            else if (addBottomRightCorner(coordinate, mTile)) {
                mAdjacentHexes.add(tempHex);
                vertexIndexAdded = 2;
                hexIndexAdded = i;
                continue;
            }
            else if (addBottomLeftCorner(coordinate, mTile)) {
                mAdjacentHexes.add(tempHex);
                vertexIndexAdded = 3;
                hexIndexAdded = i;
                continue;
            }
            else if (addMidLeftCorner(coordinate, mTile)) {
                mAdjacentHexes.add(tempHex);
                vertexIndexAdded = 4;
                hexIndexAdded = i;
                continue;
            }
            else if (addTopLeftCorner(coordinate, mTile)) {
                mAdjacentHexes.add(tempHex);
                vertexIndexAdded = 5;
                hexIndexAdded = i;
            }
        }

        if (mAdjacentHexes.size()!= 0) {
            return true;
        }
        return false;
    }

    /**Iterate through Hexes, and hex edges, checking edge locations, and seeing if tap location is a match
     * also checks to see if a location is available for an item to be placed.
     */
    public boolean checkEdgeLocations(Coordinate coordinate) {
        mAdjacentHexes.removeAll(mAdjacentHexes);
        edgeIndexAdded = -1;
        hexIndexAdded = -1;

        //for all of the hexes, check to see if the location tapped is equal to the location of any of their corners
        for (int i = 0; i < mBoardBacklog.getHexesSize(); i++) {
            Hex tempHex = mBoardBacklog.getHex(i);

            //grabbing the tile
            ImageView mTile = (ImageView) mBoardLayout.getChildAt(i + 1); // i+1 because background image is at i = 0;

            //tries adding to each of the corners, if it is a valid location, returns true, otherwise checks the rest of the corners and continues
            if (addTopRightEdge(coordinate, mTile)) {
                mAdjacentHexes.add(tempHex);
                hexIndexAdded = i;
                edgeIndexAdded = 0;
                continue;
            }
            if (addBottomRightEdge(coordinate, mTile)) {
                mAdjacentHexes.add(tempHex);
                edgeIndexAdded = 1;
                hexIndexAdded = i;
                continue;
            }
            if (addBottomEdge(coordinate, mTile)) {
                mAdjacentHexes.add(tempHex);
                edgeIndexAdded = 2;
                hexIndexAdded = i;
                continue;
            }
            if (addBottomLeftEdge(coordinate, mTile)) {
                mAdjacentHexes.add(tempHex);
                edgeIndexAdded = 3;
                hexIndexAdded = i;
                continue;
            }
            if (addTopLeftEdge(coordinate, mTile)) {
                mAdjacentHexes.add(tempHex);
                edgeIndexAdded = 4;
                hexIndexAdded = i;
                continue;
            }
            if (addTopEdge(coordinate, mTile)) {
                mAdjacentHexes.add(tempHex);
                edgeIndexAdded = 5;
                hexIndexAdded = i;
            }
        }

        if (mAdjacentHexes.size() > 0) {
            return true;
        }
        //object can't be placed, make toast
        mManagingFragment.createToast("Select the corner or edge of a tile for available actions", false);
        return false;
    }

    public void assignVertexFromIndex() {
        if (hexIndexAdded == 7) {
            if (vertexIndexAdded == 0) {
                v = mBoardBacklog.getVertex(mAdjacentHexes, 0);
                return;
            }
        }
        //runs through each of the hex indices that could have a floating vertex. if the vertexadded is equal to the counter, than v is the first vertex
        int j = 0;
        for (int i = 9; i <=17; i+=2) {
            if (hexIndexAdded == i) {
                if (vertexIndexAdded == j) {
                    v = mBoardBacklog.getVertex(mAdjacentHexes, 0);
                    return;
                }
            }
            j++;
        }
        //v = mBoardData.getVertex(mAdjacentHexes, 1);
    }

    public void assignEdgeFromIndex() {
        int edgeParameter = 2;
        if (hexIndexAdded == 7) {
            if (edgeIndexAdded == 0) {
                edgeParameter = 0;
            }
            else if (edgeIndexAdded == 4) {
                edgeParameter = 1;
            }
        }
        else if (hexIndexAdded == 8) {
            if (edgeIndexAdded == 0) {
                edgeParameter = 0;
            }
            else {
                edgeParameter = 1;
            }
        }
        else {
            int j = 0;
            for (int i = 9; i <= 18; i++) {
                if (hexIndexAdded == i) {
                    if (edgeIndexAdded == j) {
                        edgeParameter = 0;
                        break;
                    } else if (edgeIndexAdded == j + 1) {
                        edgeParameter = 1;
                        break;
                    }
                }
                if (i > 10 && i % 2 == 0) {
                    j++;
                }
            }
        }
        e = mBoardBacklog.getEdge(mAdjacentHexes, edgeParameter);
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
