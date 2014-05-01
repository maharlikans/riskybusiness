package com.slothproductions.riskybusiness.model.GameStates;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.View.R;
import com.slothproductions.riskybusiness.model.Board;
import com.slothproductions.riskybusiness.model.Edge;
import com.slothproductions.riskybusiness.model.GameLoop;
import com.slothproductions.riskybusiness.model.Player;
import com.slothproductions.riskybusiness.model.Vertex;
import com.slothproductions.riskybusiness.view.BoardButtonsFragment;
import com.slothproductions.riskybusiness.view.BoardScreen;
import com.slothproductions.riskybusiness.view.PlayerInfoFragment;

import java.util.Queue;
import java.util.Stack;

/**
 * Created by Kyle Maharlika on 4/4/2014.
 */
public class BackwardBeginningGameState implements GameState {

    Stack<Player> mPlayerStack;
    Queue<Player> mPlayerQueue;
    Player mCurrentPlayer;
    GameLoop mGameLoop;
    BoardScreen mBoardScreen;
    BoardButtonsFragment mBoardButtonsFragment;
    Board mBoard;
    PlayerInfoFragment mPlayerInfo;
    boolean mPlayerBuiltRoad;
    boolean mPlayerBuiltSettlement;

    public BackwardBeginningGameState (GameLoop gameLoop, Stack<Player> playerStack) {
        mGameLoop = gameLoop;
        mPlayerStack = playerStack;
        mPlayerQueue = gameLoop.getPlayerQueue();
        mCurrentPlayer = mPlayerStack.pop();
        mBoardScreen = mGameLoop.getBoardScreen();
        mBoard = mBoardScreen.getBoard();
        mPlayerInfo = (PlayerInfoFragment)mBoardScreen.getPlayerInfoFragment();
        mPlayerBuiltRoad = false;
        mPlayerBuiltSettlement = false;
    }

    @Override
    public void init() {
        // in the forward beginning game state, we don't need to see the tradeButton nor
        // the endTurnButton, so hide them in the init method
        mBoardButtonsFragment = (BoardButtonsFragment)mBoardScreen.getButtonsFragment();
        View v = mBoardButtonsFragment.getView();
        ImageView tradeButton = (ImageView)v.findViewById(R.id.tradeButton);
        tradeButton.setVisibility(View.GONE);
        ImageView endTurnButton = (ImageView)v.findViewById(R.id.endTurnButton);
        endTurnButton.setVisibility(View.GONE);

        mBoardButtonsFragment.createToast("Now in BackwardBeginningGameState", false);
    }

    @Override
    public void startTurn() {
        // DO NOTHING
    }

    @Override
    public void diceRoll() {
        // DO NOTHING
    }

    @Override
    public boolean buildRoad(Edge edge) {
        mCurrentPlayer.buildInitial(edge, 2);
        mPlayerBuiltRoad = true;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                endTurn();
            }
        }, 200);

        return true;
    }

    @Override
    public boolean buildSettlement(Vertex vertex) {
        mCurrentPlayer.buildInitial(vertex, 2);
        mPlayerBuiltSettlement = true;
        return true;
    }

    @Override
    public boolean buildCity(Vertex vertex) {
        // DO NOTHING
        return false;
    }

    @Override
    public boolean buildMilitaryUnit(Vertex vertex) {
        // DO NOTHING
        return false;
    }

    @Override
    public void trade() {
        // DO NOTHING
    }

    @Override
    public boolean moveMilitaryUnit(Vertex vertexFrom, Vertex vertexTo) {
        // DO NOTHING
        return false;
    }

    @Override
    public boolean attack(Vertex vertexFrom, Vertex vertexTo, Integer amount) {
        // DO NOTHING
        return false;
    }

    @Override
    public boolean repairCity(Vertex vertex) {
        // DO NOTHING
        return false;
    }

    @Override
    public boolean repairSettlement(Vertex vertex) {
        // DO NOTHING
        return false;
    }

    @Override
    public void endTurn() {
        mPlayerQueue.offer(mCurrentPlayer);
        if (mPlayerStack.isEmpty()) {
            NormalGameState normalGameState = new NormalGameState(mGameLoop);
            mGameLoop.setCurrentGameState(normalGameState);
        } else {
            mCurrentPlayer = mPlayerStack.pop();
            // TODO BoardScreenMainFragment updateResourceView() method
            // get the resources from the player class and pass it with the method
            mPlayerBuiltSettlement = false;
            mPlayerBuiltRoad = false;
            mBoardButtonsFragment.createToast(
                    "Now " + mCurrentPlayer.getName() + "'s turn",
                    false);
            mPlayerInfo.updatePlayerValues();
        }
    }

    @Override
    public Player getCurrentPlayer() {
        return mCurrentPlayer;
    }

    // This method, if given an edge, will edit the popup menu passed to it so it can be displayed
    // correctly with the proper options in the menu
    @Override
    public void getActions(PopupMenu popupMenu, Edge edge) {
        Menu menu = popupMenu.getMenu();

        menu.add(0, R.id.road, 0, R.string.build_road);
        if (mPlayerBuiltRoad || !mPlayerBuiltSettlement || !mCurrentPlayer.canBuildInitial(edge, 2)) {
            menu.findItem(R.id.road).setEnabled(false);
        }

        Log.d("TAG", "found Edge Actions");
    }

    // This method, if given a vertex, will edit the popup menu passed to it so it can be displayed
    // correctly with the proper options in the menu
    @Override
    public void getActions(PopupMenu popupMenu, Vertex vertex) {
        Menu menu = popupMenu.getMenu();

        menu.add(0, R.id.settlement, 0, R.string.build_settlement);

        if (mPlayerBuiltSettlement || !mCurrentPlayer.canBuildInitial(vertex, 2)) {
            menu.findItem(R.id.settlement).setEnabled(false);
        }
        Log.d("TAG", "found Vertex Actions");
    }
}
