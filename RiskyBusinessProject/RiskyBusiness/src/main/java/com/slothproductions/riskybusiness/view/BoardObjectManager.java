package com.slothproductions.riskybusiness.view;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.View.R;
import com.slothproductions.riskybusiness.model.Board;
import com.slothproductions.riskybusiness.model.Building;
import com.slothproductions.riskybusiness.model.BuildingType;
import com.slothproductions.riskybusiness.model.Coordinate;
import com.slothproductions.riskybusiness.model.Edge;
import com.slothproductions.riskybusiness.model.GameLoop;
import com.slothproductions.riskybusiness.model.Hex;
import com.slothproductions.riskybusiness.model.MilitaryUnit;
import com.slothproductions.riskybusiness.model.Player;
import com.slothproductions.riskybusiness.model.Vertex;

import java.util.ArrayList;

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

    //used for determining which edge and vertex to use for object placement
    private Vertex v;
    private Edge e;

    //for soldier movement
    private Vertex startVertex;
    private Coordinate startCoordinate;
    private ArrayList<ImageView> mSoldiersMoving;
    private ArrayList<ImageView> mSoldiersAttacking;

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

        //Initialize lists
        mSoldiers = new ArrayList<ImageView>();
        mSettlements = new ArrayList<ImageView>();
        mCities = new ArrayList<ImageView>();
        mRoads = new ArrayList<ImageView>();

        mAdjacentHexes = new ArrayList<Hex>();
        mSoldiersAttacking = new ArrayList<ImageView>();
        mSoldiersMoving = new ArrayList<ImageView>();
    }

    //Creates the appropriate menu for the screen based on the tap event
    //This will call the popup menu in the BoardButtons class
    public void findMenu(MotionEvent event) {
        Coordinate c = new Coordinate(event.getX(), event.getY());
        Log.d(TAG, "Checking if solider is null");
        if (mSoldiersMoving.size()>0) {
            Log.d(TAG, "Soldiers are selected to move, move them");
            moveSoldier(c);
            return;
        }
        if (mSoldiersAttacking.size() > 0) {
            attack(c);
            return;
        }

        //scan through hex corners to see if tap location matches a corner
        v = null;
        e = null;
        if (checkCornerLocations(c)) {
            try {
                if (mAdjacentHexes.size() == 1) {
                    assignVertexFromIndex();
                } else {
                    v = mBoardBacklog.getVertex(mAdjacentHexes, 0);
                }
            }
            catch (Exception e) {
                return;
            }
            // popup menu in board buttons fragment
            mBoardButtonsFragment.showActionsMenu(c, v);
        }
        else if (checkEdgeLocations(c)) {
            try {
                if (mAdjacentHexes.size() == 1) {
                    assignEdgeFromIndex();
                } else {
                    e = mBoardBacklog.getEdge(mAdjacentHexes, 0);
                }
            }
            catch (Exception e) {
                return;
            }
            mBoardButtonsFragment.showActionsMenu(c, e);
        }
    }

    //This method is called from the board buttons class. Based on the menu item selected, it will call the appropriate action
    //Note: to make this work with the model, it may need to take an edge or vertex
    public void callActionFromMenuSelection(MenuItem action, Coordinate coordinate) {
        if (mGameLoop == null) {
            mGameLoop = ((BoardScreen) mGameBoardActivity).getGameLoop();
        }
        if (action.getItemId() == R.id.road) {
            if (mGameLoop.buildRoad(e)) {
                buildItem(0, "road", coordinate);
            }
        }
        else if (action.getItemId() == R.id.soldier) {
            if (mGameLoop.buildMilitaryUnit(v)) {
                buildItem(1, "soldier", coordinate);
            }
        }
        else if (action.getItemId() == R.id.settlement) {
            if (mGameLoop.buildSettlement(v)) {
                buildItem(2, "settlement", coordinate);
            }
        }
        else if (action.getItemId() == R.id.city) {
            if (mGameLoop.buildCity(v)) {
                removeSettlement(coordinate);
                buildItem(3, "city", coordinate);
            }
        }
        else if (action.getItemId() == R.id.repairsettlement) {
            mGameLoop.getCurrentGameState().repairSettlement(v);
        }
        else if (action.getItemId() == R.id.repaircity) {
            mGameLoop.getCurrentGameState().repairCity(v);
        }
        else if (action.getItemId() == R.id.move) {
            mBoardButtonsFragment.setMilitaryNumberPicker(coordinate, v, true);
        }
        else if (action.getItemId() == R.id.attack) {
            mBoardButtonsFragment.setMilitaryNumberPicker(coordinate, v, false);
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

        item.setColorFilter(mGameLoop.getCurrentGameState().getCurrentPlayer().getColor());

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

    //prepare a soldier to be moved
    public void setNumberMoving(Coordinate coordinate, int numMoving) {
        startVertex = v;
        mSoldiersMoving = findImagesFromList(mSoldiers, coordinate, numMoving);
    }

    public void moveSoldier(Coordinate c) {
        checkCornerLocations(c);
        try {
            if (mAdjacentHexes.size() == 1) {
                assignVertexFromIndex();
            } else {
                v = mBoardBacklog.getVertex(mAdjacentHexes, 0);
            }
        }
        catch (IndexOutOfBoundsException e) {
            mBoardButtonsFragment.createToast("", false);
        }
        if (!mGameLoop.moveSoldier(startVertex, v, mSoldiersMoving.size())) {
            Log.d(TAG, "Soldier could not be moved");
            mSoldiersMoving.removeAll(mSoldiersMoving);
            return;
        }
        //should only translate if it could move
        translateImageList(mSoldiersMoving, c);
        mSoldiersMoving.removeAll(mSoldiersMoving);
    }

    public void setNumberAttacking(Coordinate coordinate, int num) {
        startVertex = v;
        startCoordinate = coordinate;
        mSoldiersAttacking = findImagesFromList(mSoldiers, coordinate, num);
    }

    public void attack(final Coordinate c) {
        final int numAttacking = mSoldiersAttacking.size(); //number of soldiers attacking
        //get the vertex that the military is attacking
        checkCornerLocations(c);
        try {
            if (mAdjacentHexes.size() == 1) {
                assignVertexFromIndex();
            } else {
                v = mBoardBacklog.getVertex(mAdjacentHexes, 0);
            }
        }
        catch (Exception e) {
            return;
        }
        //attacking military unit
        MilitaryUnit militaryFromBeginning = startVertex.getImmutable().getMilitary();
        Player attacking = militaryFromBeginning.getPlayer();
        final MilitaryUnit militaryFromEnd = startVertex.getImmutable().getMilitary();

        //defending military unit and building
        Building buildingToBeginning = v.getBuilding();
        MilitaryUnit militaryToBeginning = v.getImmutable().getMilitary();
        final Player defending;
        if (militaryToBeginning != null) {
            defending = militaryToBeginning.getPlayer();
        }
        else {
            defending = buildingToBeginning.getPlayer();
        }

        //get all the views at the vertex moving to, military and settlements or cities.
        ImageView buildingAtTo = findImageFromList(mSettlements, c);
        if (buildingAtTo == null) {
            buildingAtTo = findImageFromList(mCities, c);
        }
        final ArrayList<ImageView> soldiersDefending = findImagesFromList(mSoldiers, c);

        if (!mGameLoop.attack(startVertex, v, numAttacking)) {
            mSoldiersAttacking.removeAll(mSoldiersAttacking);
            return;
        }

        Building buildingToEnd = v.getBuilding();
        final MilitaryUnit militaryToEnd = v.getImmutable().getMilitary();

        Handler handler = new Handler();

        if (buildingToEnd.getType() == BuildingType.EMPTY) {
            Log.d(TAG, "The building on the to vertex is null, movement will be determined by soldiers");
            if (militaryToEnd == null) {
                Log.d(TAG, "Everybody died, move the attacking soldiers there, then delete them.");
                translateImageList(mSoldiersAttacking, c);
                Log.d(TAG, "Number of soldiers attacking = " + mSoldiersAttacking.size());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        removeSoldiers(mSoldiersAttacking, mSoldiersAttacking.size());
                        removeSoldiers(soldiersDefending, soldiersDefending.size());
                    }
                }, 1000);
                Log.d(TAG, "Number of soldiers alive = " + mSoldiersAttacking.size());
            }
            else if (militaryToEnd.getPlayer() == attacking) {
                Log.d(TAG, "Attack Successful, move attacking army soldiers");
                //attack was successful, move all players there, then remove the ones that died
                translateImageList(mSoldiersAttacking, c);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        removeSoldiers(soldiersDefending, soldiersDefending.size());
                        removeSettlement(c);
                        removeSoldiers(mSoldiersAttacking, numAttacking - militaryToEnd.getHealth());
                    }
                }, 1000);
            }
            else {
                Log.d(TAG, "Attack was not successful, move players there and back");
                //attack was not successful, move players there, then delete any who died, and move them back if any remain
                //remove any units that got destroyed
                translateImageList(mSoldiersAttacking, c);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        removeSoldiers(soldiersDefending, soldiersDefending.size() - militaryToEnd.getHealth());
                        removeSoldiers(mSoldiersAttacking, numAttacking - militaryToEnd.getHealth());
                        translateImageList(mSoldiersAttacking, startCoordinate);
                    }
                }, 1000);
            }

            if (buildingToBeginning.getType() != BuildingType.EMPTY) {
                Log.d(TAG, "A building was destroyed in the attack, remove it from the board");
                //remove building from board
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        removeSettlement(c);
                    }
                }, 1000);
            }
        }
        else {
            Log.d(TAG, "Their is a building on the to vertex, movement will occur if the building's owner is the attacking player");
            if (buildingToEnd.getPlayer() == attacking) {
                Log.d(TAG, "attack was successful, The player destroyed a city, and now owns that settlement");
                //should remove the city and add a settlement with the current color
                translateImageList(mSoldiersAttacking, c);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        removeImage(mCities, c);
                        buildItem(2, "settlement", c);
                    }
                }, 1000);
            }
            else if (buildingToBeginning.getType() != buildingToEnd.getType()) {
                Log.d(TAG, "City was downgraded to a settlement, and is owned by the defending player");
                translateImageList(mSoldiersAttacking, c);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (soldiersDefending != null) {
                            removeSoldiers(mSoldiersAttacking, soldiersDefending.size()/3);
                        }
                        translateImageList(mSoldiersAttacking, startCoordinate);
                        removeImage(mCities, c);
                        buildItem(2, "settlement", c, defending);
                    }
                }, 1000);
            }
            else {
                Log.d(TAG, "Attack was not successful, ");
                translateImageList(mSoldiersAttacking, c);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (soldiersDefending != null) {
                            removeSoldiers(mSoldiersAttacking, soldiersDefending.size()/3);
                        }
                        translateImageList(mSoldiersAttacking, startCoordinate);
                    }
                }, 1000);
            }
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSoldiersAttacking.removeAll(mSoldiersAttacking);
                startVertex = null;
                startCoordinate = null;
                v = null;
            }
        }, 1000);
    }

    public void removeSettlement(Coordinate coordinate) {
        ImageView tempSettlement;
        for (int i = 0; i < mSettlements.size(); i ++) {
            tempSettlement = mSettlements.get(i);
            int topLeftx = (int) tempSettlement.getX();
            int topLeftY = (int) tempSettlement.getY();
            int bottomRightx = topLeftx + tempSettlement.getMeasuredWidth();
            int bottomRighty = topLeftY + tempSettlement.getMeasuredHeight();
            if (coordinate.getX() >= topLeftx && coordinate.getX() <= bottomRightx && coordinate.getY() >= topLeftY && coordinate.getY() <= bottomRighty) {
                mBoardLayout.removeView(tempSettlement);
                mSettlements.remove(i);
                return;
            }
        }
    }

    public void removeImage(ArrayList<ImageView> list, Coordinate coordinate) {
        for (int i = 0; i < list.size(); i ++) {
            ImageView tempImage = list.get(i);
            int topLeftx = (int) tempImage.getX();
            int topLeftY = (int) tempImage.getY();
            int bottomRightx = topLeftx + tempImage.getMeasuredWidth();
            int bottomRighty = topLeftY + tempImage.getMeasuredHeight();
            if (coordinate.getX() >= topLeftx && coordinate.getX() <= bottomRightx && coordinate.getY() >= topLeftY && coordinate.getY() <= bottomRighty) {
                mBoardLayout.removeView(tempImage);
                list.remove(i);
                return;
            }
        }
    }

    public void removeSoldiers(ArrayList<ImageView> imagesToRemove, int numToRemove) {
        ArrayList<ImageView> tempList = new ArrayList<ImageView>();
        for (int i = 0; i < numToRemove; i ++) {
            tempList.add(imagesToRemove.get(i));
            mBoardLayout.removeView(imagesToRemove.get(i));
            Log.d(TAG, "Soldier Removed");
        }
        mSoldiersAttacking.removeAll(tempList);
        mSoldiers.removeAll(tempList);
    }

    public ImageView findImageFromList(ArrayList<ImageView> list, Coordinate coordinate) {
        for (int i = 0; i < list.size(); i ++) {
            ImageView tempItem = list.get(i);
            int topLeftx = (int) tempItem.getX();
            int topLeftY = (int) tempItem.getY();
            int bottomRightx = topLeftx + tempItem.getMeasuredWidth();
            int bottomRighty = topLeftY + tempItem.getMeasuredHeight();
            if (coordinate.getX() >= topLeftx && coordinate.getX() <= bottomRightx && coordinate.getY() >= topLeftY && coordinate.getY() <= bottomRighty) {
                return tempItem;
            }
        }
        return null;
    }

    public ArrayList<ImageView> findImagesFromList(ArrayList<ImageView> fromList, Coordinate coordinate, int max) {
        ArrayList<ImageView> toList = new ArrayList<ImageView>();
        for (int i = 0; i < fromList.size(); i ++) {
            ImageView item = fromList.get(i);
            int topLeftx = (int) item.getX();
            int topLeftY = (int) item.getY();
            int bottomRightx = topLeftx + item.getMeasuredWidth();
            int bottomRighty = topLeftY + item.getMeasuredHeight();
            if (coordinate.getX() >= topLeftx && coordinate.getX() <= bottomRightx && coordinate.getY() >= topLeftY && coordinate.getY() <= bottomRighty) {
                toList.add(item);
                if (toList.size() >= max) {
                    return toList;
                }
            }
        }
        return toList;
    }

    public ArrayList<ImageView> findImagesFromList(ArrayList<ImageView> fromList, Coordinate coordinate) {
        ArrayList<ImageView> toList = new ArrayList<ImageView>();
        for (int i = 0; i < fromList.size(); i ++) {
            ImageView item = fromList.get(i);
            int topLeftx = (int) item.getX();
            int topLeftY = (int) item.getY();
            int bottomRightx = topLeftx + item.getMeasuredWidth();
            int bottomRighty = topLeftY + item.getMeasuredHeight();
            if (coordinate.getX() >= topLeftx && coordinate.getX() <= bottomRightx && coordinate.getY() >= topLeftY && coordinate.getY() <= bottomRighty) {
                toList.add(item);
            }
        }
        return toList;
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
        lp.leftMargin = x-image.getDrawable().getIntrinsicWidth()/2;
        lp.topMargin = y-image.getDrawable().getIntrinsicHeight()/2;

        mBoardLayout.addView(image, lp);
    }

    public void translateImage(final int x, final int y, final ImageView image) {
        int newX= x-image.getDrawable().getIntrinsicWidth()/2;
        int newY = y-image.getDrawable().getIntrinsicHeight()/2;
        int oldX = image.getLeft();
        int oldY = image.getTop();

        Animation translation = new TranslateAnimation(0, newX-oldX, 0, newY - oldY);
        translation.setDuration(700);
        translation.setFillAfter(true);
        translation.setFillEnabled(true);
        translation.setFillBefore(false);
        translation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(TAG, "Animation Started");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "Animation Ended");
                mBoardLayout.removeView(image);
                placeImage(x, y, image);
                Log.d(TAG, "Image Swapped");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) image.getLayoutParams();
        mBoardLayout.removeView(image);
        image.setAnimation(translation);
        translation.startNow();
        mBoardLayout.addView(image, lp);
    }

    public void translateImageList(ArrayList<ImageView> images, Coordinate c) {
        for (ImageView image : images) {
           translateImage((int)c.getX(), (int)c.getY(), image);
        }
    }

    public void buildItem(int identifier, String name, Coordinate coordinate, Player p) {
        //add Item to the appropriate vertex/edge

        //create the visible image and place it on the screen
        ImageView item = new ImageView(mGameBoardActivity);
        item.setId((int)System.currentTimeMillis());
        item.setImageResource(mGameBoardActivity.getResources().getIdentifier(name, "drawable", mGameBoardActivity.getPackageName()));
        item.setRotation(coordinate.getRotation());

        item.setColorFilter(p.getColor());

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
        if (hexIndexAdded % 2 == 0) {
            v = mBoardBacklog.getVertex(mAdjacentHexes, 0);
            return;
        }
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
        v = mBoardBacklog.getVertex(mAdjacentHexes, 1);
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
                if (i >= 11 && i % 2 != 0) {
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
