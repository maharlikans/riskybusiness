package com.slothproductions.riskybusiness.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.View.R;
import com.slothproductions.riskybusiness.model.Board;
import com.slothproductions.riskybusiness.model.Coordinate;
import com.slothproductions.riskybusiness.model.DiceRoll;
import com.slothproductions.riskybusiness.model.Edge;
import com.slothproductions.riskybusiness.model.GameLoop;
import com.slothproductions.riskybusiness.model.Vertex;

public class BoardButtonsFragment extends Fragment {

    private final static String TAG = "Board Buttons Fragment";

    //Data and Managing Objects
    private Board mBoardData;           //Model Board class used for the backend
    private GameLoop mGameLoop;
    private BoardScreen mBoardScreen;   //BoardScreen that manages this fragment
    private Activity mActivity;         //Activity that can be used instead of calling getActivity() every time
    private BoardObjectManager mBoardObjectManager; //BoardObjectManager is used to keep track of all the view elements specific to the playable game board

    //Layouts
    private RelativeLayout mButtonsParent;
    private ZoomableLayout mHexParent; //This is only used for the Board Object Manager

    //Buttons
    private ImageView mBtnSettings;
    private ImageView mBtnEndTurn;
    private ImageView mBtnTrade;

    //Controllers
    private View.OnClickListener settingsController;
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
        mBoardData = mBoardScreen.getBoard();

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
        mBtnTrade = (ImageView)v.findViewById(R.id.tradeButton);
        mBtnEndTurn = (ImageView)v.findViewById(R.id.endTurnButton);
        mBtnSettings = (ImageView)v.findViewById(R.id.settingsButton);
        mButtonsParent = (RelativeLayout)v.findViewById(R.id.BoardButtons);
        mBoardObjectManager = ((BoardScreenMainFragment)mBoardScreen.getScreenFragment()).getBoardObjectManager();
    }

    void createControllers() {
        settingsController = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mActivity.getApplicationContext(), SettingsScreen.class);
                startActivity(i);
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
//                if (mBtnEndTurn.getText().equals("End Turn")) {
                  showEndTurnDialog();
//                } else if (mBtnEndTurn.getText().equals("Roll Dice")) {
                  showRollDialog();
//                }
            }
        };
    }

    void initializeControllers() {
        mBtnSettings.setOnClickListener(settingsController);
        mBtnEndTurn.setOnClickListener(endTurnController);
        mBtnTrade.setOnClickListener(tradeController);
    }

    //new stuff for settings btn

    public boolean onCreateSettingsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.settings_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_options) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showEndTurnDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);

        alertDialog.setTitle("End Turn?");
        alertDialog.setMessage("Do you want to end your turn?");

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                createToast("Turn Ended", false);
//                mBtnEndTurn.setText("Roll Dice");
            }
        });

        alertDialog.setNegativeButton("No",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                createToast("Turn not ended", false);
            }
        });

        alertDialog.show();
    }

    //TODO: Move the dice to the location of the player that is currently going.
    public int showRollDialog() {
        viceDice.roll();
        dice1 = viceDice.getFirstDice();
        dice2 = viceDice.getSecondDice();

        ImageView outputdice1 = new ImageView(mActivity);
        outputdice1.setId((int) System.currentTimeMillis());

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

        return viceDice.getResults();
    }

    //Popup for use with corner objects
    public void showPopUp(final Coordinate c, Vertex v) {
        //an anchor for the popup menu to be placed on.
        ImageView anchor = new ImageView(mActivity);
        anchor.setId((int)System.currentTimeMillis());
        anchor.setImageResource(mActivity.getResources().getIdentifier("anchor", "drawable", mActivity.getPackageName()));
        anchor.setX(c.getUnMappedX());
        anchor.setY(c.getUnMappedY());

        mButtonsParent.addView(anchor);

        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(mActivity, anchor);

        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
        popup.getMenu().removeItem(R.id.road);
        popup.getMenu().removeGroup(R.id.corneritems);

        // pass the popup to be adjusted
        mBoardScreen.getGameLoop().getActions(popup, v);

        if (popup.getMenu().size() == 0) {
            return;
        }

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
            mBoardObjectManager.callActionFromMenuSelection(item, c);
            return true;
            }
        });

        popup.show();//showing popup menu
    }

    //Popup for use with roads
    public void showPopUp(final Coordinate c, Edge e) {
        //an anchor for the popup menu to be placed on. It needs this for whatever reason..
        ImageView anchor = new ImageView(mActivity);
        anchor.setId((int)System.currentTimeMillis());
        anchor.setImageResource(mActivity.getResources().getIdentifier("anchor", "drawable", mActivity.getPackageName()));
        anchor.setX(c.getUnMappedX());
        anchor.setY(c.getUnMappedY());

        mButtonsParent.addView(anchor);

        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(mActivity, anchor);

        popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());
        popup.getMenu().removeGroup(R.id.corneritems);
        popup.getMenu().removeItem(R.id.road);

        // Pass the popup menu to be adjusted
        mBoardScreen.getGameLoop().getActions(popup, e);

        if (popup.getMenu().size() == 0) {
            return;
        }

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBoardScreen.initializeGameLoop();
        mBoardScreen.getGameLoop().startGame();
    }
}
