package com.slothproductions.riskybusiness.view;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

import com.View.R;
import com.slothproductions.riskybusiness.model.Board;
import com.slothproductions.riskybusiness.model.DiceRoll;
import com.slothproductions.riskybusiness.model.Hex;

public class BoardScreenMainFragment extends Fragment {

    private final static String TAG = "Board Screen";

    private BoardScreen mBoardScreen;
    private Board mBoardData;
    private ZoomableLayout mHexParent;      //RelativeLayout that is the parent of all the hexes
    private Button mBtnPause;
    private Button mBtnEndTurn;
    private Button mBtnBuild;
    private Button mBtnTrade;

    private Toast mLastToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("ONCREATE", "OnCreate was called");
        super.onCreate(savedInstanceState);
        mBoardScreen = new BoardScreen();

        Log.d("BOARDDATA", "Board Data Start");
        mBoardData = new Board(4);
        Log.d("BOARDDATA", "Board Data Finished");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.d("VIEWCALLED", "View was called");
        View v = inflater.inflate(R.layout.fragment_board_screen, parent, false);
        Log.d("VIEWCALLED", "View was inflated");

        mHexParent = (ZoomableLayout)v.findViewById(R.id.hexParent);
        mHexParent.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    Log.d(TAG, "Double Tap Event Detected");
                    mHexParent.zoom(e);
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    Log.d(TAG, "Single Tap Detected, not Zooming");
                    return super.onSingleTapConfirmed(e);
                }
            });

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (gestureDetector.onTouchEvent(motionEvent))
                    return true;
                return false;
            }
        });

        mBtnPause = (Button)v.findViewById(R.id.pauseButton);
        mBtnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPauseDialog();
            }
        });

        //Adds functionality to the End Turn Button
        mBtnEndTurn = (Button)v.findViewById(R.id.endTurnButton);
        mBtnEndTurn.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v){
                showEndTurnDialog();
            }
        });

        //Adds functionality to the Build Button
        mBtnBuild = (Button)v.findViewById(R.id.buildButton);
        mBtnBuild.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View v){
                showBuildDialog();
            }
        });

        //Adds functionality to the Trade Button
        mBtnTrade = (Button)v.findViewById(R.id.tradeButton);
        mBtnTrade.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View v){
                showTradeDialog();
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
                }});
            }

        addColorsToBoard();

        return v;
    }

    //loop through indices, check which resource in board, color appropriately using code similar to below
    void addColorsToBoard() {
        for (int i = 0; i < mBoardData.hexes.size(); i++) {
            ImageView iv = (ImageView)mHexParent.getChildAt(i);
            switch(mBoardData.hexes.get(i).type) {
                case LUMBER:
                    iv.setColorFilter(Color.GREEN);
                    break;
                case BRICK:
                    iv.setColorFilter(Color.RED);
                    break;
                case WOOL:
                    iv.setColorFilter(Color.LTGRAY);
                    break;
                case GRAIN:
                    iv.setColorFilter(Color.GRAY);
                    break;
                case ORE:
                    iv.setColorFilter(Color.DKGRAY);
                    break;
                case DESERT:
                    iv.setColorFilter(Color.YELLOW);
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
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            if (mBoardData.hexes.get(i).roll < 0) {
                continue;
            }
            else if (mBoardData.hexes.get(i).roll > 9) {
                lp.leftMargin = iv.getLeft()-35;
                lp.rightMargin = iv.getRight()+35;
                lp.topMargin = iv.getTop()+35;
                lp.bottomMargin = iv.getBottom()-35;
            }
            else {
                lp.leftMargin = iv.getLeft()-15;
                lp.rightMargin = iv.getRight()+15;
                lp.topMargin = iv.getTop()+35;
                lp.bottomMargin = iv.getBottom()-35;
            }
            tv.setText(Integer.toString(mBoardData.hexes.get(i).roll));
            tv.setTextSize(30);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setTextColor(getResources().getColor(R.color.blue_background));
            mHexParent.addView(tv, lp);
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
        lp.leftMargin = x-image.getWidth()/2 - image.getDrawable().getIntrinsicWidth()/2;
        lp.topMargin = y-(image.getHeight()/2) - image.getDrawable().getIntrinsicHeight()/2;

        mHexParent.addView(image, lp);
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
        int xTap = (int)tapEvent.getX()-128;
        int yTap = (int)tapEvent.getY()-34;

        //for all of the hexes, check to see if the location tapped is equal to the location of any of their corners
        for (int i =0; i < mBoardData.hexes.size(); i++) {
            Hex temp = mBoardData.hexes.get(i);
            for (int j = 0; j < 6; j++) {
                //check to see if vertex is available to be checked, then checks location compared to tap.
            }
            //grabbing the tile
            ImageView mTile = (ImageView) mHexParent.getChildAt(i);

            //tries adding to each of the corners, if it is a valid location, returns true, otherwise checks the rest of the corners and continues
            if (addTopLeftCorner(xTap, yTap, mTile, mCornerObject) || addTopRightCorner(xTap, yTap, mTile, mCornerObject) || addMidRightCorner(xTap, yTap, mTile, mCornerObject)
                    || addBottomRightCorner(xTap, yTap, mTile, mCornerObject) || addBottomLeftCorner(xTap, yTap, mTile, mCornerObject) || addMidLeftCorner(xTap, yTap, mTile, mCornerObject)) {
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

    public void showPauseDialog() {
        AlertDialog.Builder alertpauseDialog = new AlertDialog.Builder(getActivity());

        alertpauseDialog.setTitle("Pause Screen");
        alertpauseDialog.setMessage("Game Paused.");

        alertpauseDialog.setPositiveButton("Return", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (mLastToast!= null) {
                    mLastToast.cancel();
                }
                mLastToast = Toast.makeText(getActivity(), "Returning to game...",
                        Toast.LENGTH_SHORT);
                mLastToast.show();
            }
        });

        alertpauseDialog.show();
    }

    public void showBuildDialog(){
        AlertDialog.Builder alertBuildDialog = new AlertDialog.Builder(getActivity());

        alertBuildDialog.setTitle("Build");
        alertBuildDialog.setMessage("Build stuff");

        alertBuildDialog.setPositiveButton("Build", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (mLastToast!= null) {
                    mLastToast.cancel();
                }
                mLastToast = Toast.makeText(getActivity(), "Build all the stuffs",
                        Toast.LENGTH_SHORT);
                mLastToast.show();
            }
        });

        alertBuildDialog.setNegativeButton("Cancel" , new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
                if (mLastToast!= null) {
                    mLastToast.cancel();
                }
                mLastToast = Toast.makeText(getActivity(), "Build Canceled", Toast.LENGTH_SHORT);
                mLastToast.show();
            }
        });

        alertBuildDialog.show();
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
        final DiceRoll dRoll = new DiceRoll();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setTitle("End Turn?");
        alertDialog.setMessage("Do you want to end your turn?");

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (mLastToast!= null) {
                    mLastToast.cancel();
                }
                mLastToast = Toast.makeText(getActivity(), "You rolled a " + dRoll.roll().toString(),
                        Toast.LENGTH_SHORT);
                mLastToast.show();

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
}

