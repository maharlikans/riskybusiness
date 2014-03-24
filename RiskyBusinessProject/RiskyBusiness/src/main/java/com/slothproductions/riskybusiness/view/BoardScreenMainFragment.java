package com.slothproductions.riskybusiness.view;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.PopupMenu;

import com.View.R;
import com.slothproductions.riskybusiness.model.Board;
import com.slothproductions.riskybusiness.model.Coordinate;
import com.slothproductions.riskybusiness.model.DiceRoll;
import com.slothproductions.riskybusiness.model.Hex;

public class BoardScreenMainFragment extends Fragment {

    private final static String TAG = "Board Screen";

    private BoardScreen mBoardScreen;
    private Board mBoardData;
    private ZoomableLayout mHexParent;      //RelativeLayout that is the parent of all the hexes
    private Button mBtnOptions;
    private Button mBtnEndTurn;
    private Button mBtnBuild;
    private Button mBtnTrade;


    private BuildItem buildItem;

    private Toast mLastToast;

    private int height;
    private int width;

    private int dice1;
    private int dice2;

    private DiceRoll viceDice = new DiceRoll();

    public static enum BuildItem {
        NONE, ROAD, SOLDIER, SETTLEMENT, CITY
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("ONCREATE", "OnCreate was called");
        super.onCreate(savedInstanceState);
        mBoardScreen = new BoardScreen();

        String[] players = new String[]{"Player1", "Player2", "Player3", "Player4"};
        Log.d("BOARDDATA", "Board Data Start");
        mBoardData = new Board(players);
        Log.d("BOARDDATA", "Board Data Finished");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.d("VIEWCALLED", "View was called");
        View v = inflater.inflate(R.layout.fragment_board_screen, parent, false);
        Log.d("VIEWCALLED", "View was inflated");

        mHexParent = (ZoomableLayout)v.findViewById(R.id.hexParent);
        //Note: this code should probably go somewhere else I'll fix it later
        mHexParent.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    Log.d(TAG, "Double Tap Event Detected");
                    mHexParent.Zoom(e);
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    ImageView item = new ImageView(getActivity());
                    item.setId((int)System.currentTimeMillis());
                    Log.d(TAG, "Single Tap Detected");
                    switch (buildItem) {
                        case NONE:
                            Log.d(TAG, "No Build Item Selected");
                            break;
                        case ROAD:
                            Log.d(TAG, "Road Will be Placed at Tap Location");
                            item.setImageResource(getResources().getIdentifier("road", "drawable", getActivity().getPackageName()));
                            placeSideObject(e, item);
                            break;
                        case SOLDIER:
                            Log.d(TAG, "Soldier will be Placed at Tap Location");
                            item.setImageResource(getResources().getIdentifier("circle", "drawable", getActivity().getPackageName()));
                            placeCornerObject(e, item);
                            break;
                        case SETTLEMENT:
                            Log.d(TAG, "BuildingType will be Placed at Tap Location");
                            item.setImageResource(getResources().getIdentifier("settlement", "drawable", getActivity().getPackageName()));
                            placeCornerObject(e, item);
                            break;
                        case CITY:
                            Log.d(TAG, "City will be Placed at Tap Location");
                            item.setImageResource(getResources().getIdentifier("city", "drawable", getActivity().getPackageName()));
                            placeCornerObject(e, item);
                            break;
                    }

                    buildItem = buildItem.NONE;

                    //For Debugging
                    String s = "Tap X = " + e.getX() + " Tap Y  = " + e.getY();
                    String s2 = "Layout X = " + width + " Layout Y  = " + height;
                    Log.d(TAG, s2);
                    Log.d(TAG, s);

                    return super.onSingleTapConfirmed(e);
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float x, float y) {
                    Log.d(TAG, "Scroll Detected");
                    mHexParent.Pan(e1, x, y);
                    return super.onScroll(e1, e2, x, y);
                }
            });

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (gestureDetector.onTouchEvent(motionEvent))
                    return true;
                return false;
            }
        });

        mBtnOptions = (Button)v.findViewById(R.id.optionsButton);
        mBtnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog();
            }
        });

        //Adds functionality to the End Turn Button
        mBtnEndTurn = (Button)v.findViewById(R.id.endTurnButton);
             mBtnEndTurn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if (mBtnEndTurn.getText().equals("End Turn")) {
                            showEndTurnDialog();
                        } else if (mBtnEndTurn.getText().equals("Roll Dice")) {
                           showRollDialog();
                          }
                   }
              });

        buildItem = BuildItem.NONE;
        //Adds functionality to the Build Button
        mBtnBuild = (Button)v.findViewById(R.id.buildButton);
        mBtnBuild.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(getActivity(), mBtnBuild);

                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        setBuildItem(item);
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });

        //Adds functionality to the Trade Button
        mBtnTrade = (Button)v.findViewById(R.id.tradeButton);
        mBtnTrade.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View v){
                ((BoardScreen)getActivity()).onTradeButtonPressed();
            }
        });

        ViewTreeObserver vto = mHexParent.getViewTreeObserver();
        if (vto != null) {
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    //remove listener to ensure only one call is made.
                    mHexParent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    addNumbersToBoard();
                    DisplayMetrics displaymetrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                    height = displaymetrics.heightPixels;
                    width = displaymetrics.widthPixels;
                }});
            }

        addResourcesToBoard();

        return v;
    }

    //loop through indices, check which resource in board, color appropriately using code similar to below
    void addResourcesToBoard() {
        for (int i = 0; i < mBoardData.hexes.size(); i++) {
            ImageView iv = (ImageView)mHexParent.getChildAt(i);
            switch(mBoardData.hexes.get(i).type) {
                case LUMBER:
                    iv.setImageResource(getResources().getIdentifier("forestresource", "drawable", getActivity().getPackageName()));
                    break;
                case BRICK:
                    iv.setImageResource(getResources().getIdentifier("brickresource", "drawable", getActivity().getPackageName()));
                    break;
                case WOOL:
                    iv.setImageResource(getResources().getIdentifier("pasturesresource", "drawable", getActivity().getPackageName()));
                    break;
                case GRAIN:
                    iv.setImageResource(getResources().getIdentifier("fields", "drawable", getActivity().getPackageName()));
                    break;
                case ORE:
                    iv.setImageResource(getResources().getIdentifier("mountainresource", "drawable", getActivity().getPackageName()));
                    break;
                case GOLD:
                    iv.setImageResource(getResources().getIdentifier("goldresource", "drawable", getActivity().getPackageName()));
            }
        }
    }

    /**
     * adds dice roll values to the board and displays them
     *<p>
     *Iterates through the arraylist of hexes, grabbing what the roll value should be.
     *The corresponding hex image from the view is then found, and its location is grabbed.
     *The textview is placed relative to the location of the hex
     *
     */
    void addNumbersToBoard() {
        int l = mBoardData.hexes.size();
        for (int i = 0; i < l; i++) {
            ImageView iv = (ImageView)mHexParent.getChildAt(i);
            TextView tv = new TextView(getActivity());
            tv.setId((int)System.currentTimeMillis());

            int x = iv.getLeft();
            int y = iv.getTop();

            y += iv.getDrawable().getIntrinsicHeight()/2;

            ImageView back = new ImageView(getActivity());
            back.setId((int)System.currentTimeMillis());
            back.setImageResource(getResources().getIdentifier("textback", "drawable", getActivity().getPackageName()));

            y -= back.getDrawable().getIntrinsicHeight()/2;

            placeImage(x,y,back);

            if (mBoardData.hexes.get(i).roll < 10) {
                x += 15;
            }

            tv.setText(Integer.toString(mBoardData.hexes.get(i).roll));
            tv.setTextSize(20);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setTextColor(getResources().getColor(R.color.blue_background));

            placeText(x-26,y-30,tv);
        }
    }

    void setBuildItem(MenuItem item) {
        if (item.getItemId() == R.id.road) {
            buildItem = BuildItem.ROAD;
        }
        else if (item.getItemId() == R.id.soldier) {
            buildItem = BuildItem.SOLDIER;
        }
        else if (item.getItemId() == R.id.settlement) {
            buildItem = BuildItem.SETTLEMENT;
        }
        else {
            buildItem = BuildItem.CITY;
        }
    }

    /**places given image centered at the specified x and y coordinates
     *
     * @param x The x coordinate for item placement
     * @param y The y coordinate for item placement
     * @param image The item that will be placed on the screen
     */
    void placeImage(int x, int y, ImageView image) {
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);

        //center object on location with margins
        lp.leftMargin = x-(image.getWidth()/2) - image.getDrawable().getIntrinsicWidth()/2;
        lp.topMargin = y-(image.getHeight()/2) - image.getDrawable().getIntrinsicHeight()/2;

        mHexParent.addView(image, lp);
    }

    void placeText(int x, int y, TextView text) {
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);

        //center object on location with margins
        lp.leftMargin = x;
        lp.topMargin = y;

        mHexParent.addView(text, lp);
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
        int x = mTile.getLeft()-63;
        int y = mTile.getTop()-33;

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
        int x = mTile.getLeft()+65;
        int y = mTile.getTop()-31;

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
        int x = mTile.getLeft()+130;
        int y = mTile.getTop()+82;

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
        int x = mTile.getLeft()+65;
        int y = mTile.getTop()+192;

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
        int x = mTile.getLeft()-62;
        int y = mTile.getTop()+192;

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
        int x = mTile.getLeft()-125;
        int y = mTile.getTop()+78;

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

    /**Iterate through Hexes, and hex vertices, checking vertex locations, and seeing if tap location is a match
     * also checks to see if a location is available for an item to be placed.
     *
     * @param tapEvent the event for the tap, retrieved from the onTouchEvent method
     * @param mCornerObject the Object that will be placed at the corner
     * @return true if the object could be placed, false otherwise
     */
    boolean placeCornerObject(MotionEvent tapEvent, ImageView mCornerObject) {
        //get location of x and y taps, and adjust for padding
        int x,y;
        if (mHexParent.isZoom()) {
            Coordinate coordinate = new Coordinate(tapEvent.getX(),tapEvent.getY());
            coordinate.mapZoomCoordinates(mHexParent);
            x = (int)coordinate.getX();
            y = (int)coordinate.getY();
        }
        else {
            x = (int)(tapEvent.getX()-128);
            y = (int)(tapEvent.getY()-32);
        }

        //for all of the hexes, check to see if the location tapped is equal to the location of any of their corners
        for (int i =0; i < mBoardData.hexes.size(); i++) {
            Hex temp = mBoardData.hexes.get(i);
            for (int j = 0; j < 6; j++) {
                //check to see if vertex is available to be checked, then checks location compared to tap.
            }
            //grabbing the tile
            ImageView mTile = (ImageView) mHexParent.getChildAt(i);

            //tries adding to each of the corners, if it is a valid location, returns true, otherwise checks the rest of the corners and continues
            if (addTopLeftCorner(x, y, mTile, mCornerObject) || addTopRightCorner(x, y, mTile, mCornerObject) || addMidRightCorner(x, y, mTile, mCornerObject)
                    || addBottomRightCorner(x, y, mTile, mCornerObject) || addBottomLeftCorner(x, y, mTile, mCornerObject) || addMidLeftCorner(x, y, mTile, mCornerObject)) {
                return true;
            }
        }

        //object can't be placed, make toast
        if (mLastToast!= null) {
            mLastToast.cancel();
        }
        mLastToast = Toast.makeText(getActivity(), "Invalid Object Placement (needs to be placed on the corner of a tile)",
                Toast.LENGTH_SHORT);
        mLastToast.show();

        return false;
    }

    boolean addTopEdge(int tapX, int tapY, ImageView mTile, ImageView mSideObject) {
        //gets the location of Top edge of a tile
        int x = mTile.getLeft();
        int y = mTile.getTop()-32;

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
        int x = mTile.getLeft()-95;
        int y = mTile.getTop()+22;

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
        int x = mTile.getLeft()+98;
        int y = mTile.getTop()+25;

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
        int x = mTile.getLeft()+98;
        int y = mTile.getTop()+138;

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
        int x = mTile.getLeft()-95;
        int y = mTile.getTop()+135;

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
        int x = mTile.getLeft();
        int y = mTile.getTop()+192;

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

    /**Iterate through Hexes, and hex edges, checking edge locations, and seeing if tap location is a match
     * also checks to see if a location is available for an item to be placed.
     *
     * @param tapEvent the event for the tap, retrieved from the onTouchEvent method
     * @param mSideObject the Object that will be placed at the corner
     * @return true if the object could be placed, false otherwise
     */
    boolean placeSideObject(MotionEvent tapEvent, ImageView mSideObject) {
        //get location of x and y taps, and adjust for padding
        int x,y;
        if (mHexParent.isZoom()) {
            Coordinate coordinate = new Coordinate(tapEvent.getX(),tapEvent.getY());
            coordinate.mapZoomCoordinates(mHexParent);
            x = (int)coordinate.getX();
            y = (int)coordinate.getY();
        }
        else {
            x = (int)(tapEvent.getX()-128);
            y = (int)(tapEvent.getY()-32);
        }

        //for all of the hexes, check to see if the location tapped is equal to the location of any of their corners
        for (int i =0; i < mBoardData.hexes.size(); i++) {
            Hex temp = mBoardData.hexes.get(i);
            for (int j = 0; j < 6; j++) {
                //check to see if vertex is available to be checked, then checks location compared to tap.
            }
            //grabbing the tile
            ImageView mTile = (ImageView) mHexParent.getChildAt(i);

            //tries adding to each of the corners, if it is a valid location, returns true, otherwise checks the rest of the corners and continues
            if (addTopLeftEdge(x, y, mTile, mSideObject) || addTopRightEdge(x, y, mTile, mSideObject) || addTopEdge(x, y, mTile, mSideObject)
                    || addBottomRightEdge(x, y, mTile, mSideObject) || addBottomLeftEdge(x, y, mTile, mSideObject) || addBottomEdge(x, y, mTile, mSideObject)) {
                return true;
            }
        }

        //object can't be placed, make toast
        if (mLastToast!= null) {
            mLastToast.cancel();
        }
        mLastToast = Toast.makeText(getActivity(), "Invalid Object Placement (needs to be placed on the corner of a tile)",
                Toast.LENGTH_SHORT);
        mLastToast.show();

        return false;
    }

    public void showOptionsDialog() {
        AlertDialog.Builder alertOptionsDialog = new AlertDialog.Builder(getActivity());

        alertOptionsDialog.setTitle("Options");
        alertOptionsDialog.setCancelable(false);

        alertOptionsDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (mLastToast!= null) {
                    mLastToast.cancel();
                }
                mLastToast = Toast.makeText(getActivity(), "Going to Game Setup page...",
                        Toast.LENGTH_SHORT);
                mLastToast.show();
            }
        });

        alertOptionsDialog.setNeutralButton("How to Play", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getActivity().getApplicationContext(), GameRules.class);
                startActivity(i);
            }
        });

        alertOptionsDialog.setNegativeButton("Save and Return to Game", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (mLastToast!= null) {
                    mLastToast.cancel();
                }
                mLastToast = Toast.makeText(getActivity(), "Game Saved!",
                        Toast.LENGTH_SHORT);
                mLastToast.show();

            }
        });

        alertOptionsDialog.show();
    }

    public void showTradeDialog(){
        AlertDialog.Builder alertTradeDialog = new AlertDialog.Builder(getActivity());

        alertTradeDialog.setTitle("Trade");
        alertTradeDialog.setMessage("Trade resources with other players?");

        alertTradeDialog.setPositiveButton("Trade", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (mLastToast!= null) {
                    mLastToast.cancel();
                }
                mLastToast = Toast.makeText(getActivity(), "Trade all the resources!",
                        Toast.LENGTH_SHORT);
                mLastToast.show();
            }
        });

        alertTradeDialog.setNegativeButton("Cancel",  new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                if (mLastToast!= null) {
                    mLastToast.cancel();
                }
                mLastToast = Toast.makeText(getActivity(), "Trade Canceled", Toast.LENGTH_SHORT);
                mLastToast.show();
            }
        });

        alertTradeDialog.show();
    }

    public void showEndTurnDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setTitle("End Turn?");
        alertDialog.setMessage("Do you want to end your turn?");

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (mLastToast!= null) {
                    mLastToast.cancel();
                }
                mLastToast = Toast.makeText(getActivity(), "Turn ended", Toast.LENGTH_SHORT);
                mLastToast.show();
                mBtnEndTurn.setText("Roll Dice");

            }
        });
        alertDialog.setNegativeButton("No",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                if (mLastToast!= null) {
                    mLastToast.cancel();
                }
                mLastToast = Toast.makeText(getActivity(), "Turn not ended", Toast.LENGTH_SHORT);
                mLastToast.show();

            }
        });

        alertDialog.show();
    }

    public void showRollDialog() {
        viceDice.roll();
        dice1 = viceDice.getFirstDice();
        dice2 = viceDice.getSecondDice();

        ImageView outputdice1 = new ImageView(getActivity());
        outputdice1.setId((int)System.currentTimeMillis());



        LayoutParams lpDice1 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lpDice1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        lpDice1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

       switch(dice1){
           case 1:
               outputdice1.setImageResource(getResources().getIdentifier("dice1", "drawable", getActivity().getPackageName()));
               mHexParent.removeView(outputdice1);
               mHexParent.addView(outputdice1, lpDice1);
               break;
           case 2:
               outputdice1.setImageResource(getResources().getIdentifier("dice2", "drawable", getActivity().getPackageName()));
               mHexParent.removeView(outputdice1);
               mHexParent.addView(outputdice1, lpDice1);
           case 3:
               outputdice1.setImageResource(getResources().getIdentifier("dice3", "drawable", getActivity().getPackageName()));
               mHexParent.removeView(outputdice1);
               mHexParent.addView(outputdice1, lpDice1);        //Crashes here?
               break;
           case 4:
               outputdice1.setImageResource(getResources().getIdentifier("dice4", "drawable", getActivity().getPackageName()));
               mHexParent.removeView(outputdice1);
               mHexParent.addView(outputdice1, lpDice1);
               break;
           case 5:
               outputdice1.setImageResource(getResources().getIdentifier("dice5", "drawable", getActivity().getPackageName()));
               mHexParent.removeView(outputdice1);
               mHexParent.addView(outputdice1, lpDice1);
               break;
           case 6:
               outputdice1.setImageResource(getResources().getIdentifier("dice6", "drawable", getActivity().getPackageName()));
               mHexParent.removeView(outputdice1);
               mHexParent.addView(outputdice1, lpDice1);
               break;
       }

        ImageView outputdice2 = new ImageView(getActivity());
        outputdice2.setId((int)System.currentTimeMillis()+1);

        LayoutParams lpDice2 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lpDice2.addRule(RelativeLayout.RIGHT_OF, outputdice1.getId());
        lpDice2.addRule(RelativeLayout.ALIGN_BOTTOM, outputdice1.getId());


        switch (dice2) {
          case 1:
              outputdice2.setImageResource(getResources().getIdentifier("dice1", "drawable", getActivity().getPackageName()));
              mHexParent.removeView(outputdice2);
              mHexParent.addView(outputdice2, lpDice2);
              break;
          case 2:
              outputdice2.setImageResource(getResources().getIdentifier("dice2", "drawable", getActivity().getPackageName()));
              mHexParent.removeView(outputdice2);
              mHexParent.addView(outputdice2, lpDice2);
              break;
          case 3:
              outputdice2.setImageResource(getResources().getIdentifier("dice3", "drawable", getActivity().getPackageName()));
              mHexParent.removeView(outputdice2);
              mHexParent.addView(outputdice2, lpDice2);
              break;
          case 4:
              outputdice2.setImageResource(getResources().getIdentifier("dice4", "drawable", getActivity().getPackageName()));
              mHexParent.removeView(outputdice2);
              mHexParent.addView(outputdice2, lpDice2);
              break;
          case 5:
              outputdice2.setImageResource(getResources().getIdentifier("dice5", "drawable", getActivity().getPackageName()));
              mHexParent.removeView(outputdice2);
              mHexParent.addView(outputdice2, lpDice2);
              break;
          case 6:
              outputdice2.setImageResource(getResources().getIdentifier("dice6", "drawable", getActivity().getPackageName()));
              mHexParent.removeView(outputdice2);
              mHexParent.addView(outputdice2, lpDice2);
              break;

      }

        mBtnEndTurn.setText("End Turn");


    }
}

