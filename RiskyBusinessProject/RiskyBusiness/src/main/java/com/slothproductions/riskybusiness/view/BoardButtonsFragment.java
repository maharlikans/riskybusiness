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
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.View.R;
import com.slothproductions.riskybusiness.model.Board;
import com.slothproductions.riskybusiness.model.Coordinate;
import com.slothproductions.riskybusiness.model.DiceRoll;
import com.slothproductions.riskybusiness.model.Edge;
import com.slothproductions.riskybusiness.model.GameLoop;
import com.slothproductions.riskybusiness.model.MilitaryUnit;
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
                  showEndTurnDialog();
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
                mGameLoop.endTurn();
            }
        });

        alertDialog.setNegativeButton("No",new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                createToast("Turn not ended", false);
            }
        });

        alertDialog.show();
    }

    //Popup for use with corner objects
    public void showActionsMenu(final Coordinate c, Vertex v) {
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
    public void showActionsMenu(final Coordinate c, Edge e) {
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

    public void setMilitaryNumberPicker(final Coordinate c, Vertex v, final Boolean isMoving) {
        int maxNumberMilitary;
        MilitaryUnit military = v.getImmutable().getMilitary();
        if (isMoving) {
            maxNumberMilitary = Math.min(military.getHealth(), military.getHaveNotMoved()+military.getHaveBonusMoved());
        }
        else {
            maxNumberMilitary = Math.min(5, military.getHaveNotMoved());
        }

        if (maxNumberMilitary == 1) {
            if (isMoving) {
                mBoardObjectManager.setNumberMoving(c, 1);
            }
            else {
                mBoardObjectManager.setNumberAttacking(c, 1);
            }
            return;
        }

        //an anchor for the popup menu to be placed on. It needs this for whatever reason..
        ImageView anchor = new ImageView(mActivity);
        anchor.setId((int)System.currentTimeMillis());
        anchor.setImageResource(mActivity.getResources().getIdentifier("anchor", "drawable", mActivity.getPackageName()));
        anchor.setX(c.getUnMappedX());
        anchor.setY(c.getUnMappedY());

        mButtonsParent.addView(anchor);

        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(mActivity, anchor);
        popup.getMenuInflater().inflate(R.menu.numberpicker, popup.getMenu());

        Menu menu = popup.getMenu();
        for (int i = 1; i <= maxNumberMilitary; i++) {
            menu.add(Integer.toString(i));
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (isMoving) {
                    mBoardObjectManager.setNumberMoving(c, Integer.parseInt(item.getTitle().toString()));
                }
                else {
                    mBoardObjectManager.setNumberAttacking(c, Integer.parseInt(item.getTitle().toString()));
                }
                return true;
            }
        });
        popup.show();
    }

    public void createToast(String text, boolean isLong) {
        cancelToast();
        int length = Toast.LENGTH_SHORT;
        if (isLong) {
            length = Toast.LENGTH_LONG;
        }
        mLastToast = Toast.makeText(mActivity, text, length);
        mLastToast.show();
    }

    public boolean cancelToast() {
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
        mGameLoop = mBoardScreen.getGameLoop();
        mGameLoop.startGame();
    }
}
