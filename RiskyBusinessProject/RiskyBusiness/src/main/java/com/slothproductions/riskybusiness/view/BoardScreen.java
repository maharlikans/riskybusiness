package com.slothproductions.riskybusiness.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.View.R;
import com.slothproductions.riskybusiness.model.Board;

public class BoardScreen extends FragmentActivity {

    private Board mBoardData;

    private Fragment mBoardScreenFragment;
    private Fragment mTradeScreenFragment;
    private Fragment mBoardButtonsFragment;
    private Fragment mPlayerInfoFragment;

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_screen);

        String[] Players = new String [getIntent().getIntExtra("numplayerschosen", 0)];
        String[] defaultPlayers = getResources().getStringArray(R.array.player_names);
        for (int i = 0; i < Players.length; i++) {
                Players[i] = defaultPlayers[i];
        }

        mBoardData = new Board(Players);

        mFragmentManager = getSupportFragmentManager();

        initializeDefaultFragments();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.board_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
    if (id == R.id.action_settings) {
        return true;
    }
    return super.onOptionsItemSelected(item);
}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.d("KEYPRESSED", "Back Button was pressed");
            if (mTradeScreenFragment==null) {
                showExitDialog();
            }
            else {
                onCancelTradeButtonPressed();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showExitDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Exiting Game");
        alertDialog.setMessage("All unsaved progess will be lost. Are you sure you want to exit the game?");

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //return to screen
            }
        });

        alertDialog.show();
    }

    @Override
    protected void onStart() {
        Log.d("ONSTART", "On Start Called");
        super.onStart();
        Log.d("ONSTART", "On Start Finished");
    }

    // public method specifically for the BoardScreenMainFragment to call
    // when the user presses the trade button on the view
    public void onTradeButtonPressed() {
        mTradeScreenFragment = new TradeScreenFragment();
        mFragmentManager.beginTransaction()
                .add(R.id.BoardContainer, mTradeScreenFragment)
                .commit();
    }

    // public method implemented specifically for the TradeFragment to call
    // when the user presses the cancel trade button on the view
    public void onCancelTradeButtonPressed() {
        mFragmentManager.beginTransaction()
                .remove(mTradeScreenFragment)
                .commit();
        mTradeScreenFragment = null;
    }

    public Fragment getScreenFragment() {
        return mBoardScreenFragment;
    }

    public Fragment getButtonsFragment() {
        return mBoardButtonsFragment;
    }

    public Board getBoard() {
        return mBoardData;
    }

    protected void initializeDefaultFragments() {
        mBoardScreenFragment = mFragmentManager.findFragmentById(R.id.hexParent);
        mBoardButtonsFragment = mFragmentManager.findFragmentById(R.id.BoardButtons);
        mPlayerInfoFragment = mFragmentManager.findFragmentById(R.id.PlayerInfo);

        if (mBoardScreenFragment == null) {
            mBoardScreenFragment = new BoardScreenMainFragment();
            mFragmentManager.beginTransaction()
                    .add(R.id.BoardContainer, mBoardScreenFragment)
                    .commit();
        }

        if (mBoardButtonsFragment == null) {
            mBoardButtonsFragment = new BoardButtonsFragment();
            mFragmentManager.beginTransaction()
                    .add(R.id.BoardContainer, mBoardButtonsFragment)
                    .commit();
        }

        if (mPlayerInfoFragment == null) {
            mPlayerInfoFragment = new PlayerInfoFragment();
            mFragmentManager.beginTransaction()
                    .add(R.id.BoardContainer, mPlayerInfoFragment)
                    .commit();
        }
    }
}
