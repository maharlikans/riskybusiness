package com.slothproductions.riskybusiness.view;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
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

    private BoardScreen mBoardScreen;
    private Board mBoardData;
    private RelativeLayout mHexParent;      //RelativeLayout that is the parent of all the hexes
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

        mHexParent = (RelativeLayout)v.findViewById(R.id.hexParent);

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
                    //This is where add numbers should be implemented (i think)
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
     * <p>
     *     Iterates through the arraylist of hexes, grabbing what the roll value should be.
     *     The corresponding hex image from the view is then found, and its location is grabbed.
     *     The textview is placed relative to the location of the hex
     * </p>
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

    //NOTE: find...Corner() methods are used for testing purposes to find the locations of each corner.
    //Places a circle image at the top corner of all the hexes
    void findTopLeftCorner() {
        for (int i = 0; i< mBoardData.hexes.size(); i++) {
            ImageView iv = (ImageView)mHexParent.getChildAt(i);
            ImageView mTempCity = new ImageView(getActivity());
            mTempCity.setId((int)System.currentTimeMillis());
            mTempCity.setImageResource(getResources().getIdentifier("circle", "drawable", getActivity().getPackageName()));
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            lp.leftMargin = iv.getLeft()-25-63; //+162
            lp.topMargin = iv.getTop()-25-33; //-38
            mHexParent.addView(mTempCity, lp);
        }
    }

    //Places a circle image at the bottom corner of all the hexes
    void findBottomRightCorner() {
        for (int i = 0; i< mBoardData.hexes.size(); i++) {
            ImageView iv = (ImageView)mHexParent.getChildAt(i);
            ImageView mTempCity = new ImageView(getActivity());
            mTempCity.setId((int)System.currentTimeMillis());
            mTempCity.setImageResource(getResources().getIdentifier("circle", "drawable", getActivity().getPackageName()));
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            lp.leftMargin = iv.getLeft()-25+65; //+130
            lp.topMargin = iv.getTop()-25+192; //+112
            mHexParent.addView(mTempCity, lp);
        }
    }

    //Places a circle image at the top right corner of all the hexes
    void findTopRightCorner() {
        for (int i = 0; i< mBoardData.hexes.size(); i++) {
            ImageView iv = (ImageView)mHexParent.getChildAt(i);
            ImageView mTempCity = new ImageView(getActivity());
            mTempCity.setId((int)System.currentTimeMillis());
            mTempCity.setImageResource(getResources().getIdentifier("circle", "drawable", getActivity().getPackageName()));
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            lp.leftMargin = iv.getLeft()-25+65;
            lp.topMargin = iv.getTop()-25-31;
            mHexParent.addView(mTempCity, lp);
        }
    }

    //Places a circle image at the top left corner of all the hexes
    void findMidLeftCorner() {
        for (int i = 0; i< mBoardData.hexes.size(); i++) {
            ImageView iv = (ImageView)mHexParent.getChildAt(i);
            ImageView mTempCity = new ImageView(getActivity());
            mTempCity.setId((int)System.currentTimeMillis());
            mTempCity.setImageResource(getResources().getIdentifier("circle", "drawable", getActivity().getPackageName()));
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            lp.leftMargin = iv.getLeft()-25-125;
            lp.topMargin = iv.getTop()-25+78;
            mHexParent.addView(mTempCity, lp);
        }
    }

    //Places a circle image at the bottom left corner of all the hexes
    void findBottomLeftCorner() {
        for (int i = 0; i< mBoardData.hexes.size(); i++) {
            ImageView iv = (ImageView)mHexParent.getChildAt(i);
            ImageView mTempCity = new ImageView(getActivity());
            mTempCity.setId((int)System.currentTimeMillis());
            mTempCity.setImageResource(getResources().getIdentifier("circle", "drawable", getActivity().getPackageName()));
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            lp.leftMargin = iv.getLeft()-25-62;
            lp.topMargin = iv.getTop()-25+192;
            mHexParent.addView(mTempCity, lp);
        }
    }

    //Places a circle image at the bottom right corner of all the hexes
    void findMidRightCorner() {
        for (int i = 0; i< mBoardData.hexes.size(); i++) {
            ImageView iv = (ImageView)mHexParent.getChildAt(i);
            ImageView mTempCity = new ImageView(getActivity());
            mTempCity.setId((int)System.currentTimeMillis());
            mTempCity.setImageResource(getResources().getIdentifier("circle", "drawable", getActivity().getPackageName()));
            mTempCity.setRotation(-30);
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            lp.leftMargin = iv.getLeft()-25+130;
            lp.topMargin = iv.getTop()-25+82;
            mHexParent.addView(mTempCity, lp);
        }
    }

    /**Iterate through Hexes, and hex vertices, checking vertex locations, and seeing if tap location is a match
     * also checks to see if a location is available for an item to be placed.
     *
     * @param tapEvent
     * @return boolean
     *
     * returns true if item could be placed
     * returns false if tapLocation was not close enough to a corner to place it
     */
    boolean placeCornerObject(MotionEvent tapEvent) {
        //get location of x and y taps, and adjust for padding
        int xTap = (int)tapEvent.getX()-128;
        int yTap = (int)tapEvent.getY()-34;

        //for all of the hexes, check to see if the location tapped is equal to the location of any of their corners
        //right now it just checks topleftcorner
        for (int i =0; i < mBoardData.hexes.size(); i++) {
            Hex temp = mBoardData.hexes.get(i);
            for (int j = 0; j < 6; j++) {
                //check to see if vertex is available to be checked, then checks location compared to tap.
            }
            //grabbing the tile
            ImageView mTile = (ImageView) mHexParent.getChildAt(i);

            //gets the location of top vertex of tile
            int x = mTile.getLeft()-63;
            int y = mTile.getTop()-33;

            //range that is valid location
            int lowX = x-50;
            int highX = x+50;
            int lowY = y-50;
            int highY = y+50;

            //compare tap x,y locations against valid x and y range for top corner
            if (xTap >= lowX && xTap <=highX && yTap>=lowY && yTap<=highY) {
                //place object here
                ImageView mTempObject = new ImageView(getActivity());
                mTempObject.setId((int)System.currentTimeMillis());
                mTempObject.setImageResource(getResources().getIdentifier("circle", "drawable", getActivity().getPackageName()));
                LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
                lp.leftMargin = x-25; //note the -25 is to center the image on the location
                lp.topMargin = y-25;

                mHexParent.addView(mTempObject, lp);
                return true;
            }
        }

        //object can't be placed, make toast
        if (mLastToast!= null) {
            mLastToast.cancel();
        }
        mLastToast = Toast.makeText(getActivity(), "Invalid Corner Object Placement (needs to be on top left corner)",
                Toast.LENGTH_SHORT);
        mLastToast.show();

        return false;
    }

    @Override
    public void onStart() {
        Log.d("ONSTART", "Fragment Called Start");
        super.onStart();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                addNumbersToBoard();
            }
        }, 50);
        Log.d("ONSTART", "Fragment Finished Start");
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

