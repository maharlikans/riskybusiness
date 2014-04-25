package com.slothproductions.riskybusiness.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.View.R;
import com.slothproductions.riskybusiness.model.Board;
import com.slothproductions.riskybusiness.model.GameLoop;

import java.util.Random;

public class BoardScreen extends FragmentActivity {

    private Board mBoardData;
    private GameLoop mGameLoop;

    private Fragment mBoardScreenFragment;
    private Fragment mTradeScreenFragment;
    private Fragment mBoardButtonsFragment;
    private Fragment mPlayerInfoFragment;

    private FragmentManager mFragmentManager;


    int[] music = {R.raw.song1, R.raw.song2, R.raw.song3, R.raw.song4, R.raw.song5};
    int nextSong;
    MediaPlayer song;
    OnCompletionListener mListener = new OnCompletionListener(){
        @Override
        public void onCompletion(MediaPlayer song) {
            song.release();
            startNextSong();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_screen);

        nextSong = 0;
        startNextSong();

        String[] Players = new String [getIntent().getIntExtra(GameSetupScreen.NUM_PLAYERS_CHOSEN, 0)];
        String[] defaultPlayers = getResources().getStringArray(R.array.player_names);
        for (int i = 0; i < Players.length; i++) {
                Log.d("TAG", "This player is: " + defaultPlayers[i]);
                Players[i] = defaultPlayers[i];
        }

        mBoardData = new Board(Players);
        mGameLoop = new GameLoop(this);

        mFragmentManager = getSupportFragmentManager();

        initializeDefaultFragments();
    }

    void startNextSong(){
        Random r = new Random();
        int random = r.nextInt(5 - 0) + 0;
       // if(nextSong > 4) nextSong = 0;
        //if(nextSong < music.length){
            song = MediaPlayer.create(BoardScreen.this, music[random]);
            song.setOnCompletionListener(mListener);
            song.start();
        //}
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
    protected  void onResume() {
        super.onResume();
        song.start();
        //start song back again.
    }

    @Override
    protected void onPause() {
        super.onPause();
        song.pause();
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

    // this function is used to fetch the arguemnts from the intent
    // which started this activity, which is from the GameSetupScreen
    public Bundle fetchAndPlaceArguments() {
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        int numPlayersChosen = intent.getIntExtra(GameSetupScreen.NUM_PLAYERS_CHOSEN, 0);
        String[] playerTypes = intent.getStringArrayExtra(GameSetupScreen.PLAYER_TYPES);
        int numVictoryPoints = intent.getIntExtra(GameSetupScreen.NUM_VICTORY_POINTS, 0);
        boolean variableBoard = intent.getBooleanExtra(GameSetupScreen.VARIABLE_BOARD, false);
        boolean attacksOn = intent.getBooleanExtra(GameSetupScreen.ATTACKS, false);
        int[] colors = intent.getIntArrayExtra(GameSetupScreen.COLORS);
        Log.d("TAG", "Number of players is: " + numPlayersChosen);
        for (int i = 0; i < playerTypes.length; i++) {
            Log.d("TAG", "Player " + i + " is of type " + playerTypes[i]);
        }
        Log.d("TAG", "The number of victory points is: " + numVictoryPoints);
        Log.d("TAG", "Board generation is variable: " + variableBoard);
        Log.d("TAG", "Attacks are on: " + attacksOn);
        bundle.putInt(GameSetupScreen.NUM_PLAYERS_CHOSEN,
                intent.getIntExtra(GameSetupScreen.NUM_PLAYERS_CHOSEN, 0));
        bundle.putStringArray(GameSetupScreen.PLAYER_TYPES,
                intent.getStringArrayExtra(GameSetupScreen.PLAYER_TYPES));
        bundle.putInt(GameSetupScreen.NUM_VICTORY_POINTS,
                intent.getIntExtra(GameSetupScreen.NUM_VICTORY_POINTS, 0));
        bundle.putBoolean(GameSetupScreen.VARIABLE_BOARD,
                intent.getBooleanExtra(GameSetupScreen.VARIABLE_BOARD, false));
        bundle.putBoolean(GameSetupScreen.ATTACKS,
                intent.getBooleanExtra(GameSetupScreen.ATTACKS, false));
        bundle.putIntArray(GameSetupScreen.COLORS,
                intent.getIntArrayExtra(GameSetupScreen.COLORS));
        return bundle;
    }

    public Fragment getButtonsFragment() {
        return mBoardButtonsFragment;
    }

    public Board getBoard() {
        return mBoardData;
    }

    public GameLoop getGameLoop() {
        return mGameLoop;
    }

    protected void initializeDefaultFragments() {
        mBoardScreenFragment = mFragmentManager.findFragmentById(R.id.hexParent);
        mBoardButtonsFragment = mFragmentManager.findFragmentById(R.id.BoardButtons);
        mPlayerInfoFragment = mFragmentManager.findFragmentById(R.id.PlayerInfo);

        if (mBoardScreenFragment == null) {
            Bundle args = fetchAndPlaceArguments();

            mBoardScreenFragment = new BoardScreenMainFragment();
            mBoardScreenFragment.setArguments(args);
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
