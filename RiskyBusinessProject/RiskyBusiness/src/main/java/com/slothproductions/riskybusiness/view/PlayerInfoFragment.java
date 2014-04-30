package com.slothproductions.riskybusiness.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.View.R;
import com.slothproductions.riskybusiness.model.Board;
import com.slothproductions.riskybusiness.model.GameLoop;
import com.slothproductions.riskybusiness.model.Player;
import com.slothproductions.riskybusiness.model.Resource;

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

    ArrayList<Player> mPlayersArrayList;

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
            mPlayerSquares.get(i).setColorFilter(mPlayersArrayList.get(i).getColor());
        }
    }

    public void initializePlayerSquares(View v) {
        mPlayerSquares = new ArrayList<ImageView>();
        mPlayerSquares.add((ImageView)v.findViewById(R.id.player_1_info));
        mPlayerSquares.add((ImageView)v.findViewById(R.id.player_2_info));
        mPlayerSquares.add((ImageView)v.findViewById(R.id.player_3_info));
        mPlayerSquares.add((ImageView)v.findViewById(R.id.player_4_info));
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
