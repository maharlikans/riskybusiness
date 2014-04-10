package com.slothproductions.riskybusiness.view;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;

import com.View.R;
import com.slothproductions.riskybusiness.model.Board;
import com.slothproductions.riskybusiness.model.DiceRoll;

public class BoardScreenMainFragment extends Fragment {

    private final static String TAG = "Board Screen Fragment";

    //Data and Managing Objects
    private Board mBoardData;           //Model Board class used for the backend
    private BoardScreen mBoardScreen;   //BoardScreen that manages this fragment
    private Activity mActivity;         //Activity that can be used instead of calling getActivity() every time
    private BoardObjectManager mBoardObjectManager; //BoardObjectManager is used to keep track of all the view elements specific to the playable game board

    //Layouts
    private ZoomableLayout mHexParent;  //ZoomableLayout that holds all of the physical board objects
    private StaticLayout mMainView;

    //Controllers
    private OnTouchListener GameBoardController;

    //Screen Dimensions
    private int mHeight;
    private int mWidth;

    //Last toast variable is kept track of in order to cancel last toast upon creating new one
    private Toast mLastToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "onCreate completed");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        //Inflate the View
        View v = inflater.inflate(R.layout.fragment_board_screen, parent, false);
        Log.d(TAG, "View inflated");

        //initialize variables based on the activity of superclass
        mActivity = getActivity();
        mBoardScreen = (BoardScreen)mActivity;

        mBoardData = mBoardScreen.getBoard();

        //map the layouts in the view to the class variables
        mHexParent = (ZoomableLayout)v.findViewById(R.id.hexParent);
        mMainView = (StaticLayout)v.findViewById(R.id.mainLayout);

        //creates the controller to manage the board actions (pan/zoom, object placement, etc)
        setGameBoardController();

        //Adds the resource Icons to the board
        addResourcesToBoard();

        //Creates the view tree observer necessary for adding the visual elements after the hexparent is inflated
        setDelayedInitializations();

        //Initializes the game board object manager
        mBoardObjectManager = new BoardObjectManager(mBoardData, mHexParent, mActivity, this);

        return v;
    }

    void setGameBoardController() {
        final GestureDetector.SimpleOnGestureListener movementDetector = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Log.d(TAG, "Double Tap Event Detected");
                mHexParent.Zoom(e);
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.d(TAG, "Single Tap Detected");
                mBoardObjectManager.BuildItem(e);
                Log.d(TAG, "Item Built");
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float x, float y) {
                Log.d(TAG, "Scroll Detected");
                mHexParent.Pan(e1, x, y);
                return super.onScroll(e1, e2, x, y);
            }
        };
        GameBoardController = new OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(mActivity, movementDetector);

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (gestureDetector.onTouchEvent(motionEvent))
                    return true;
                return false;
            }
        };

        mHexParent.setOnTouchListener(GameBoardController);
    }

    void setDelayedInitializations() {
        //This code will be executed once the hex layout view is inflated
        ViewTreeObserver vto = mHexParent.getViewTreeObserver();
        if (vto != null) {
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    //remove listener to ensure only one call is made.
                    mHexParent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    addNumbersToBoard();
                    getScreenDimensions();
                }});
        }
    }

    //loop through indices, check which resource in board, color appropriately using code similar to below
    void addResourcesToBoard() {
        for (int i = 0; i < mBoardData.hexes.size(); i++) {
            ImageView iv = (ImageView)mHexParent.getChildAt(i+1);
            switch(mBoardData.hexes.get(i).type) {
                case LUMBER:
                    iv.setImageResource(getResources().getIdentifier("forestresource", "drawable", mActivity.getPackageName()));
                    break;
                case BRICK:
                    iv.setImageResource(getResources().getIdentifier("brickresource", "drawable", mActivity.getPackageName()));
                    break;
                case WOOL:
                    iv.setImageResource(getResources().getIdentifier("pasturesresource", "drawable", mActivity.getPackageName()));
                    break;
                case GRAIN:
                    iv.setImageResource(getResources().getIdentifier("fields", "drawable", mActivity.getPackageName()));
                    break;
                case ORE:
                    iv.setImageResource(getResources().getIdentifier("mountainresource", "drawable", mActivity.getPackageName()));
                    break;
                case GOLD:
                    iv.setImageResource(getResources().getIdentifier("goldresource", "drawable", mActivity.getPackageName()));
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
            ImageView iv = (ImageView)mHexParent.getChildAt(i+1);
            TextView tv = new TextView(mActivity);
            tv.setId((int)System.currentTimeMillis());

            int x = iv.getLeft();
            int y = iv.getTop();

            ImageView textBackground = new ImageView(mActivity);
            textBackground.setId((int)System.currentTimeMillis());
            textBackground.setImageResource(getResources().getIdentifier("textback", "drawable", mActivity.getPackageName()));

            x+= iv.getWidth()/2;
            y+= iv.getHeight()/2;

            placeImage(x,y,textBackground);

            tv.setText(Integer.toString(mBoardData.hexes.get(i).roll));
            tv.setTextSize(20);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setTextColor(getResources().getColor(R.color.blue_background));
            tv.setGravity(Gravity.CENTER);

            LayoutParams lp = new LayoutParams(60,60);
            lp.leftMargin = x-30;
            lp.topMargin = y-30;
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
        lp.leftMargin = x-(image.getWidth()/2) - image.getDrawable().getIntrinsicWidth()/2;
        lp.topMargin = y-(image.getHeight()/2) - image.getDrawable().getIntrinsicHeight()/2;

        mHexParent.addView(image, lp);
    }

    /**
     * Simplified code for creating a toast
     * @param text String value that you want the text to say
     * @param isLong boolean variable for if you want the toast to be long or short
     */
    void createToast(String text, boolean isLong) {
        cancelToast();
        int length = Toast.LENGTH_SHORT;
        if (isLong) {
            length = Toast.LENGTH_LONG;
        }
        mLastToast = Toast.makeText(mActivity, text, length);
        mLastToast.show();
    }

    boolean cancelToast() {
        if (mLastToast!= null) {
            mLastToast.cancel();
            return true;
        }
        return false;
    }

    public void getScreenDimensions() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mHeight = displaymetrics.heightPixels;
        mWidth = displaymetrics.widthPixels;
    }

    BoardObjectManager getBoardObjectManager() {
        return mBoardObjectManager;
    }
}

