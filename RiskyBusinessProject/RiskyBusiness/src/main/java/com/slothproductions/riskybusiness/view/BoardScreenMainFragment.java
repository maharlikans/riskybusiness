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

    //Buttons
    private Button mBtnOptions;
    private Button mBtnEndTurn;
    private Button mBtnBuild;
    private Button mBtnTrade;

    //Controllers
    private OnTouchListener GameBoardController;
    private OnClickListener optionsController;
    private OnClickListener endTurnController;
    private OnClickListener buildController;
    private OnClickListener tradeController;

    //private ArrayList<Button> mButtons; may add this variable later in order to better manage buttons
    //private ArrayList<OnClickListener> mControllers; may add this variable later in order to better manage controllers

    //Last toast variable is kept track of in order to cancel last toast upon creating new one
    private Toast mLastToast;

    //Screen Dimensions
    private int mHeight;
    private int mWidth;

    private int dice1;
    private int dice2;

    private DiceRoll viceDice = new DiceRoll();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initializing non view elements
        mBoardData = new Board(new String[]{"Player1", "Player2", "Player3", "Player4"});

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

        //maps the buttons and layouts in the view to the class variables
        initializeViewElements(v);

        //creates the controller to manage the board actions (pan/zoom, object placement, etc)
        setGameBoardController();

        //Creates all of the simpleOnClickListeners for the buttons
        createControllers();

        //Adds the listeners to the appropriate buttons
        initializeControllers();

        //Adds the resource Icons to the board
        addResourcesToBoard();

        //Creates the view tree observer necessary for adding the visual elements after the hexparent is inflated
        setDelayedInitializations();

        //Initializes the game board object manager
        mBoardObjectManager = new BoardObjectManager(mBoardData, mHexParent, mActivity);

        return v;
    }

    void initializeViewElements(View v) {
        mBtnTrade = (Button)v.findViewById(R.id.tradeButton);
        mBtnBuild = (Button)v.findViewById(R.id.buildButton);
        mBtnEndTurn = (Button)v.findViewById(R.id.endTurnButton);
        mBtnOptions = (Button)v.findViewById(R.id.optionsButton);

        mHexParent = (ZoomableLayout)v.findViewById(R.id.hexParent);
    }

    void createControllers() {
        optionsController = new OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog();
            }
        };

        tradeController = new OnClickListener(){
            @Override
            public void onClick(View v){
                mBoardScreen.onTradeButtonPressed();
            }
        };

        endTurnController = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBtnEndTurn.getText().equals("End Turn")) {
                    showEndTurnDialog();
                } else if (mBtnEndTurn.getText().equals("Roll Dice")) {
                    showRollDialog();
                }
            }
        };

        buildController = new OnClickListener(){
            @Override
            public void onClick(View v){
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(mActivity, mBtnBuild);

                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        mBoardObjectManager.setCurrentBuildItem(item);
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        };
    }

    void initializeControllers() {
        mHexParent.setOnTouchListener(GameBoardController);
        mBtnOptions.setOnClickListener(optionsController);
        mBtnEndTurn.setOnClickListener(endTurnController);
        mBtnBuild.setOnClickListener(buildController);
        mBtnTrade.setOnClickListener(tradeController);
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
                mBoardObjectManager.onSingleTapConfirmed(e);
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
            ImageView iv = (ImageView)mHexParent.getChildAt(i);
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
            ImageView iv = (ImageView)mHexParent.getChildAt(i);
            TextView tv = new TextView(mActivity);
            tv.setId((int)System.currentTimeMillis());

            int x = iv.getLeft();
            int y = iv.getTop();

            y += iv.getDrawable().getIntrinsicHeight()/2;

            ImageView back = new ImageView(mActivity);
            back.setId((int)System.currentTimeMillis());
            back.setImageResource(getResources().getIdentifier("textback", "drawable", mActivity.getPackageName()));

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

    public void showOptionsDialog() {
        AlertDialog.Builder alertOptionsDialog = new AlertDialog.Builder(mActivity);

        alertOptionsDialog.setTitle("Options");
        alertOptionsDialog.setCancelable(false);

        alertOptionsDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(mActivity.getApplicationContext(), OptionsScreen.class);
                startActivity(i);
            }
        });

        alertOptionsDialog.setNeutralButton("How to Play", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(mActivity.getApplicationContext(), GameRules.class);
                startActivity(i);
            }
        });

        alertOptionsDialog.setNegativeButton("Save and Return to Game", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (mLastToast!= null) {
                    mLastToast.cancel();
                }
                mLastToast = Toast.makeText(mActivity, "Game Saved!",
                        Toast.LENGTH_SHORT);
                mLastToast.show();

            }
        });

        alertOptionsDialog.show();
    }

    public void showEndTurnDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);

        alertDialog.setTitle("End Turn?");
        alertDialog.setMessage("Do you want to end your turn?");

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (mLastToast!= null) {
                    mLastToast.cancel();
                }
                mLastToast = Toast.makeText(mActivity, "Turn ended", Toast.LENGTH_SHORT);
                mLastToast.show();
                mBtnEndTurn.setText("Roll Dice");

            }
        });
        alertDialog.setNegativeButton("No",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                if (mLastToast!= null) {
                    mLastToast.cancel();
                }
                mLastToast = Toast.makeText(mActivity, "Turn not ended", Toast.LENGTH_SHORT);
                mLastToast.show();

            }
        });

        alertDialog.show();
    }

    public void showRollDialog() {
        viceDice.roll();
        dice1 = viceDice.getFirstDice();
        dice2 = viceDice.getSecondDice();

        ImageView outputdice1 = new ImageView(mActivity);
        outputdice1.setId((int)System.currentTimeMillis());



        LayoutParams lpDice1 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lpDice1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        lpDice1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

       switch(dice1){
           case 1:
               outputdice1.setImageResource(getResources().getIdentifier("dice1", "drawable", mActivity.getPackageName()));
               mHexParent.removeView(outputdice1);
               mHexParent.addView(outputdice1, lpDice1);
               break;
           case 2:
               outputdice1.setImageResource(getResources().getIdentifier("dice2", "drawable", mActivity.getPackageName()));
               mHexParent.removeView(outputdice1);
               mHexParent.addView(outputdice1, lpDice1);
           case 3:
               outputdice1.setImageResource(getResources().getIdentifier("dice3", "drawable", mActivity.getPackageName()));
               mHexParent.removeView(outputdice1);
               mHexParent.addView(outputdice1, lpDice1);        //Crashes here?
               break;
           case 4:
               outputdice1.setImageResource(getResources().getIdentifier("dice4", "drawable", mActivity.getPackageName()));
               mHexParent.removeView(outputdice1);
               mHexParent.addView(outputdice1, lpDice1);
               break;
           case 5:
               outputdice1.setImageResource(getResources().getIdentifier("dice5", "drawable", mActivity.getPackageName()));
               mHexParent.removeView(outputdice1);
               mHexParent.addView(outputdice1, lpDice1);
               break;
           case 6:
               outputdice1.setImageResource(getResources().getIdentifier("dice6", "drawable", mActivity.getPackageName()));
               mHexParent.removeView(outputdice1);
               mHexParent.addView(outputdice1, lpDice1);
               break;
       }

        ImageView outputdice2 = new ImageView(mActivity);
        outputdice2.setId((int)System.currentTimeMillis()+1);

        LayoutParams lpDice2 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lpDice2.addRule(RelativeLayout.RIGHT_OF, outputdice1.getId());
        lpDice2.addRule(RelativeLayout.ALIGN_BOTTOM, outputdice1.getId());


        switch (dice2) {
          case 1:
              outputdice2.setImageResource(getResources().getIdentifier("dice1", "drawable", mActivity.getPackageName()));
              mHexParent.removeView(outputdice2);
              mHexParent.addView(outputdice2, lpDice2);
              break;
          case 2:
              outputdice2.setImageResource(getResources().getIdentifier("dice2", "drawable", mActivity.getPackageName()));
              mHexParent.removeView(outputdice2);
              mHexParent.addView(outputdice2, lpDice2);
              break;
          case 3:
              outputdice2.setImageResource(getResources().getIdentifier("dice3", "drawable", mActivity.getPackageName()));
              mHexParent.removeView(outputdice2);
              mHexParent.addView(outputdice2, lpDice2);
              break;
          case 4:
              outputdice2.setImageResource(getResources().getIdentifier("dice4", "drawable", mActivity.getPackageName()));
              mHexParent.removeView(outputdice2);
              mHexParent.addView(outputdice2, lpDice2);
              break;
          case 5:
              outputdice2.setImageResource(getResources().getIdentifier("dice5", "drawable", mActivity.getPackageName()));
              mHexParent.removeView(outputdice2);
              mHexParent.addView(outputdice2, lpDice2);
              break;
          case 6:
              outputdice2.setImageResource(getResources().getIdentifier("dice6", "drawable", mActivity.getPackageName()));
              mHexParent.removeView(outputdice2);
              mHexParent.addView(outputdice2, lpDice2);
              break;

      }

        mBtnEndTurn.setText("End Turn");


    }

    public void getScreenDimensions() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mHeight = displaymetrics.heightPixels;
        mWidth = displaymetrics.widthPixels;
    }
}

