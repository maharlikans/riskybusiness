package com.slothproductions.riskybusiness.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.View.R;
import com.slothproductions.riskybusiness.model.Board;
import com.slothproductions.riskybusiness.model.Coordinate;
import com.slothproductions.riskybusiness.model.DiceRoll;

public class BoardButtonsFragment extends Fragment {

    private final static String TAG = "Board Buttons Fragment";

    //Data and Managing Objects
    private Board mBoardData;           //Model Board class used for the backend
    private BoardScreen mBoardScreen;   //BoardScreen that manages this fragment
    private Activity mActivity;         //Activity that can be used instead of calling getActivity() every time
    private BoardObjectManager mBoardObjectManager; //BoardObjectManager is used to keep track of all the view elements specific to the playable game board

    //Layouts
    private RelativeLayout mButtonsParent;
    private ZoomableLayout mHexParent; //This is only used for the Board Object Manager

    //Buttons
    private Button mBtnOptions;
    private Button mBtnEndTurn;
    private Button mBtnTrade;

    //Controllers
    private View.OnClickListener optionsController;
    private View.OnClickListener endTurnController;
    private View.OnClickListener tradeController;

    //Last toast variable is kept track of in order to cancel last toast upon creating new one
    private Toast mLastToast;

    private int dice1;
    private int dice2;

    private DiceRoll viceDice;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_board_buttons, parent, false);
        Log.d(TAG, "View inflated");

        //initialize variables based on the activity of superclass
        mActivity = getActivity();
        mBoardScreen = (BoardScreen)mActivity;

        //maps the buttons and layouts in the view to the class variables
        initializeViewElements(v);

        //Creates all of the simpleOnClickListeners for the buttons
        createControllers();

        //Adds the listeners to the appropriate buttons
        initializeControllers();

        viceDice = new DiceRoll();

        return v;
    }

    void initializeViewElements(View v) {
        mBtnTrade = (Button)v.findViewById(R.id.tradeButton);
        mBtnEndTurn = (Button)v.findViewById(R.id.endTurnButton);
        mBtnOptions = (Button)v.findViewById(R.id.optionsButton);

        mButtonsParent = (RelativeLayout)v.findViewById(R.id.BoardButtons);
        mBoardObjectManager = ((BoardScreenMainFragment)mBoardScreen.getScreenFragment()).getBoardObjectManager();
    }

    void createControllers() {
        optionsController = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog();
            }
        };

        tradeController = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mBoardScreen.onTradeButtonPressed();
            }
        };

        endTurnController = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBtnEndTurn.getText().equals("End Turn")) {
                    showEndTurnDialog();
                } else if (mBtnEndTurn.getText().equals("Roll Dice")) {
                    showRollDialog();
                }
            }
        };
    }

    void initializeControllers() {
        mBtnOptions.setOnClickListener(optionsController);
        mBtnEndTurn.setOnClickListener(endTurnController);
        mBtnTrade.setOnClickListener(tradeController);
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
                createToast("Game Saved!", false);
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
                createToast("Turn Ended", false);
                mBtnEndTurn.setText("Roll Dice");

            }
        });

        alertDialog.setNegativeButton("No",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                createToast("Turn not ended", false);
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



        RelativeLayout.LayoutParams lpDice1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpDice1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        lpDice1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

        switch(dice1){
            case 1:
                outputdice1.setImageResource(getResources().getIdentifier("dice1", "drawable", mActivity.getPackageName()));
                mButtonsParent.removeView(outputdice1);
                mButtonsParent.addView(outputdice1, lpDice1);
                break;
            case 2:
                outputdice1.setImageResource(getResources().getIdentifier("dice2", "drawable", mActivity.getPackageName()));
                mButtonsParent.removeView(outputdice1);
                mButtonsParent.addView(outputdice1, lpDice1);
            case 3:
                outputdice1.setImageResource(getResources().getIdentifier("dice3", "drawable", mActivity.getPackageName()));
                mButtonsParent.removeView(outputdice1);
                mButtonsParent.addView(outputdice1, lpDice1);        //Crashes here?
                break;
            case 4:
                outputdice1.setImageResource(getResources().getIdentifier("dice4", "drawable", mActivity.getPackageName()));
                mButtonsParent.removeView(outputdice1);
                mButtonsParent.addView(outputdice1, lpDice1);
                break;
            case 5:
                outputdice1.setImageResource(getResources().getIdentifier("dice5", "drawable", mActivity.getPackageName()));
                mButtonsParent.removeView(outputdice1);
                mButtonsParent.addView(outputdice1, lpDice1);
                break;
            case 6:
                outputdice1.setImageResource(getResources().getIdentifier("dice6", "drawable", mActivity.getPackageName()));
                mButtonsParent.removeView(outputdice1);
                mButtonsParent.addView(outputdice1, lpDice1);
                break;
        }

        ImageView outputdice2 = new ImageView(mActivity);
        outputdice2.setId((int)System.currentTimeMillis()+1);

        RelativeLayout.LayoutParams lpDice2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpDice2.addRule(RelativeLayout.RIGHT_OF, outputdice1.getId());
        lpDice2.addRule(RelativeLayout.ALIGN_BOTTOM, outputdice1.getId());


        switch (dice2) {
            case 1:
                outputdice2.setImageResource(getResources().getIdentifier("dice1", "drawable", mActivity.getPackageName()));
                mButtonsParent.removeView(outputdice2);
                mButtonsParent.addView(outputdice2, lpDice2);
                break;
            case 2:
                outputdice2.setImageResource(getResources().getIdentifier("dice2", "drawable", mActivity.getPackageName()));
                mButtonsParent.removeView(outputdice2);
                mButtonsParent.addView(outputdice2, lpDice2);
                break;
            case 3:
                outputdice2.setImageResource(getResources().getIdentifier("dice3", "drawable", mActivity.getPackageName()));
                mButtonsParent.removeView(outputdice2);
                mButtonsParent.addView(outputdice2, lpDice2);
                break;
            case 4:
                outputdice2.setImageResource(getResources().getIdentifier("dice4", "drawable", mActivity.getPackageName()));
                mButtonsParent.removeView(outputdice2);
                mButtonsParent.addView(outputdice2, lpDice2);
                break;
            case 5:
                outputdice2.setImageResource(getResources().getIdentifier("dice5", "drawable", mActivity.getPackageName()));
                mButtonsParent.removeView(outputdice2);
                mButtonsParent.addView(outputdice2, lpDice2);
                break;
            case 6:
                outputdice2.setImageResource(getResources().getIdentifier("dice6", "drawable", mActivity.getPackageName()));
                mButtonsParent.removeView(outputdice2);
                mButtonsParent.addView(outputdice2, lpDice2);
                break;

        }

        mBtnEndTurn.setText("End Turn");


    }

    public void showPopUp(final Coordinate c) {
        ImageView anchor = new ImageView(mActivity);
        anchor.setId((int)System.currentTimeMillis());
        anchor.setImageResource(mActivity.getResources().getIdentifier("anchor", "drawable", mActivity.getPackageName()));
        anchor.setX(c.getUnMappedX());
        anchor.setY(c.getUnMappedY());

        mButtonsParent.addView(anchor);

        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(mActivity, anchor);

        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                mBoardObjectManager.callActionFromMenuSelection(item, c);
                return true;
            }
        });

        popup.show();//showing popup menu
    }

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
}
