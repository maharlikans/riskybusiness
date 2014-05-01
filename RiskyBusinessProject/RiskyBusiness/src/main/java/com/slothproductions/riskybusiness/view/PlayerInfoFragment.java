package com.slothproductions.riskybusiness.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.View.R;
import com.slothproductions.riskybusiness.model.Board;
import com.slothproductions.riskybusiness.model.DiceRoll;
import com.slothproductions.riskybusiness.model.GameLoop;
import com.slothproductions.riskybusiness.model.Player;
import com.slothproductions.riskybusiness.model.Resource;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class PlayerInfoFragment extends Fragment {

    private final static String TAG = "Player Info Overlay";

    private Board mBoardData;               //Model Board class used for the backend
    private BoardScreen mBoardScreen;       //BoardScreen that manages this fragment
    private android.app.Activity mActivity; //Activity that can be used instead of calling getActivity() every time
    private GameLoop mGameLoop;

    private ArrayList<TextView> mCurrentPlayerResources;
    private ArrayList<TextView> mVictoryPoints;
    private ArrayList<TextView> mTotalNumberResources;

    private ArrayList<ImageView> mPlayerSquares;
    private ArrayList<LinearLayout> mPlayerInfo;
    private ArrayList<LinearLayout> mPlayerName;
    private ArrayList<Player> mPlayersArrayList;

    private DiceRoll mDice;
    private ImageView mFirstDice;
    private ImageView mSecondDice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_player_info, container, false);
        Log.d(TAG, "View inflated");

        //initialize variables based on the activity of superclass
        mActivity = getActivity();
        mBoardScreen = (BoardScreen)mActivity;
        mBoardData = mBoardScreen.getBoard();

        mPlayersArrayList = (ArrayList<Player>)mBoardData.getPlayers();

        initializeResources(v); //detailed info for current player
        initializeVictoryPoints(v);
        initializeNumberResources(v); //total number available resources for each player
        initializePlayerSquares(v);
        initializePlayerInfo(v);
        initializePlayerName(v);
        initializeDice(v);

        colorBoxes();
        //TODO: Only show player info boxes for the number of players that there are
        return v;
    }

    public void updatePlayerValues() {
        if (mGameLoop == null) {
            mGameLoop = mBoardScreen.getGameLoop();
        }
        //first updates all resources on the bottom row (the current players resources)
        Map<Resource, Integer> resources = mGameLoop.getCurrentGameState().getCurrentPlayer().getResources();
        Iterator it = resources.entrySet().iterator();
        for (int i = 0; i < mCurrentPlayerResources.size(); i++) {
            Map.Entry resource = (Map.Entry)it.next();
            mCurrentPlayerResources.get(i).setText(Integer.toString((Integer)resource.getValue()));
        }
        //now update the total number of resources for all the players
        for (int i = 0; i < mPlayersArrayList.size(); i++) {
            mTotalNumberResources.get(i).setText(Integer.toString(mPlayersArrayList.get(i).getTotalNumberResources()));
        }
        //now update victory points
        for (int i = 0; i < mPlayersArrayList.size(); i++) {
            mVictoryPoints.get(i).setText(Integer.toString(mPlayersArrayList.get(i).getPoints()));
        }
    }

    public void colorBoxes() {
        for (int i = 0; i < mPlayersArrayList.size(); i++) {
            mPlayerSquares.get(i).setVisibility(View.VISIBLE);
            mPlayerSquares.get(i).setColorFilter(mPlayersArrayList.get(i).getColor());
            mPlayerInfo.get(i).setVisibility(View.VISIBLE);
        }
    }

    public int showRollDialog() {
        mDice.roll();
        int dice1 = mDice.getFirstDice();
        int dice2 = mDice.getSecondDice();

        Log.d("TAG", "dice 1 rolled " + dice1);
        Log.d("TAG", "dice 2 rolled " + dice2);

        if (mFirstDice.getVisibility() == View.INVISIBLE) {
            mFirstDice.setVisibility(View.VISIBLE);
            mSecondDice.setVisibility(View.VISIBLE);
        }

        Player p = mGameLoop.getCurrentGameState().getCurrentPlayer();
        int playerInfoId = 0;
        for (int i = 0; i < mPlayersArrayList.size(); i++) {
            if (p == mPlayersArrayList.get(i)) {
                playerInfoId = mPlayerSquares.get(i).getId();
            }
        }

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_END, playerInfoId);
        lp.addRule(RelativeLayout.ALIGN_BOTTOM, playerInfoId);
        mSecondDice.setLayoutParams(lp);

        switch(dice1){
            case 1:
                mFirstDice.setImageResource(getResources().getIdentifier("dice1", "drawable", mActivity.getPackageName()));
                break;
            case 2:
                mFirstDice.setImageResource(getResources().getIdentifier("dice2", "drawable", mActivity.getPackageName()));
                break;
            case 3:
                mFirstDice.setImageResource(getResources().getIdentifier("dice3", "drawable", mActivity.getPackageName()));
                break;
            case 4:
                mFirstDice.setImageResource(getResources().getIdentifier("dice4", "drawable", mActivity.getPackageName()));
                break;
            case 5:
                mFirstDice.setImageResource(getResources().getIdentifier("dice5", "drawable", mActivity.getPackageName()));
                break;
            case 6:
                mFirstDice.setImageResource(getResources().getIdentifier("dice6", "drawable", mActivity.getPackageName()));
                break;
        }

        switch (dice2) {
            case 1:
                mSecondDice.setImageResource(getResources().getIdentifier("dice1", "drawable", mActivity.getPackageName()));
                break;
            case 2:
                mSecondDice.setImageResource(getResources().getIdentifier("dice2", "drawable", mActivity.getPackageName()));
                break;
            case 3:
                mSecondDice.setImageResource(getResources().getIdentifier("dice3", "drawable", mActivity.getPackageName()));
                break;
            case 4:
                mSecondDice.setImageResource(getResources().getIdentifier("dice4", "drawable", mActivity.getPackageName()));
                break;
            case 5:
                mSecondDice.setImageResource(getResources().getIdentifier("dice5", "drawable", mActivity.getPackageName()));
                break;
            case 6:
                mSecondDice.setImageResource(getResources().getIdentifier("dice6", "drawable", mActivity.getPackageName()));
                break;
        }

        mSecondDice.invalidate();
        mFirstDice.invalidate();

        return mDice.getResults();
    }

    public void initializePlayerName(View v){
        switch(mPlayersArrayList.size()) {
            case 1:
                ((TextView) v.findViewById(R.id.player_1_name)).setText(mPlayersArrayList.get(0).getName());
                break;
            case 2:
                ((TextView) v.findViewById(R.id.player_1_name)).setText(mPlayersArrayList.get(0).getName());
                ((TextView) v.findViewById(R.id.player_2_name)).setText(mPlayersArrayList.get(1).getName());
                break;
            case 3:
                ((TextView) v.findViewById(R.id.player_1_name)).setText(mPlayersArrayList.get(0).getName());
                ((TextView) v.findViewById(R.id.player_2_name)).setText(mPlayersArrayList.get(1).getName());
                ((TextView) v.findViewById(R.id.player_3_name)).setText(mPlayersArrayList.get(2).getName());
                break;
            case 4:
                ((TextView) v.findViewById(R.id.player_1_name)).setText(mPlayersArrayList.get(0).getName());
                ((TextView) v.findViewById(R.id.player_2_name)).setText(mPlayersArrayList.get(1).getName());
                ((TextView) v.findViewById(R.id.player_3_name)).setText(mPlayersArrayList.get(2).getName());
                ((TextView) v.findViewById(R.id.player_4_name)).setText(mPlayersArrayList.get(3).getName());
        }
        }


    public void initializeDice(View v) {
        mDice = new DiceRoll();
        mFirstDice = (ImageView)v.findViewById(R.id.dice_1);
        mSecondDice = (ImageView)v.findViewById(R.id.dice_2);
    }


    public void initializePlayerInfo(View v) {
        mPlayerInfo = new ArrayList<LinearLayout>();
        mPlayerInfo.add((LinearLayout)v.findViewById(R.id.player_1_info));
        mPlayerInfo.add((LinearLayout)v.findViewById(R.id.player_2_info));
        mPlayerInfo.add((LinearLayout)v.findViewById(R.id.player_3_info));
        mPlayerInfo.add((LinearLayout)v.findViewById(R.id.player_4_info));
    }

    public void initializePlayerSquares(View v) {
        mPlayerSquares = new ArrayList<ImageView>();
        mPlayerSquares.add((ImageView)v.findViewById(R.id.player_1_square));
        mPlayerSquares.add((ImageView)v.findViewById(R.id.player_2_square));
        mPlayerSquares.add((ImageView)v.findViewById(R.id.player_3_square));
        mPlayerSquares.add((ImageView)v.findViewById(R.id.player_4_square));
    }

    public void initializeResources(View v) {
        mCurrentPlayerResources = new ArrayList<TextView>();
        mCurrentPlayerResources.add((TextView) v.findViewById(R.id.num_gold));
        mCurrentPlayerResources.add((TextView) v.findViewById(R.id.num_brick));
        mCurrentPlayerResources.add((TextView) v.findViewById(R.id.num_ore));
        mCurrentPlayerResources.add((TextView) v.findViewById(R.id.num_grain));
        mCurrentPlayerResources.add((TextView) v.findViewById(R.id.num_wool));
        mCurrentPlayerResources.add((TextView) v.findViewById(R.id.num_wood));
    }

    public void initializeVictoryPoints(View v) {
        mVictoryPoints = new ArrayList<TextView>();
        mVictoryPoints.add((TextView)v.findViewById(R.id.player_1_vp));
        mVictoryPoints.add((TextView)v.findViewById(R.id.player_2_vp));
        mVictoryPoints.add((TextView)v.findViewById(R.id.player_3_vp));
        mVictoryPoints.add((TextView)v.findViewById(R.id.player_4_vp));
    }

    public void initializeNumberResources(View v) {
        mTotalNumberResources = new ArrayList<TextView>();
        mTotalNumberResources.add((TextView) v.findViewById(R.id.player_1_num_resources));
        mTotalNumberResources.add((TextView) v.findViewById(R.id.player_2_num_resources));
        mTotalNumberResources.add((TextView) v.findViewById(R.id.player_3_num_resources));
        mTotalNumberResources.add((TextView) v.findViewById(R.id.player_4_num_resources));
    }
}
