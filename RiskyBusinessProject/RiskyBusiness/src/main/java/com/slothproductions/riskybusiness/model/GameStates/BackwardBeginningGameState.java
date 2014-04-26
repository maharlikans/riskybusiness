package com.slothproductions.riskybusiness.model.GameStates;

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
    boolean mPlayerBuiltRoad;
    boolean mPlayerBuiltSettlement;

    public BackwardBeginningGameState (GameLoop gameLoop, Stack<Player> playerStack) {
        mGameLoop = gameLoop;
        mPlayerStack = playerStack;
        mPlayerQueue = gameLoop.getPlayerQueue();
        mCurrentPlayer = mPlayerStack.pop();
        mBoardScreen = mGameLoop.getBoardScreen();
        mBoard = mBoardScreen.getBoard();
        mPlayerBuiltRoad = false;
        mPlayerBuiltSettlement = false;
        // TODO: know some sort of ordering to the players
        // via a queue?
        startTurn();
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
        if (mPlayerBuiltRoad) {
            if (mCurrentPlayer.canBuildInitial(edge, 1)) { // 1 means the first
                mCurrentPlayer.buildInitial(edge, 1);
                endTurn();
                return true;
            } else {
                mBoardButtonsFragment.createToast("You can't build a road here.", false);
                return false;
            }
        } else {
            mBoardButtonsFragment.createToast("Please build a road first.", false);
            return false;
        }
    }

    @Override
    public boolean buildSettlement(Vertex vertex) {
        if (!mPlayerBuiltSettlement) {
            if (mCurrentPlayer.canBuildInitial(vertex, 1)) {
                mCurrentPlayer.buildInitial(vertex, 1);
                return true;
            } else {
                mBoardButtonsFragment.createToast("You can't build a settlement here.", false);
                return false;
            }
        } else {
            mBoardButtonsFragment.createToast(
                    "You already built a settlement, you can't build another",
                    false);
            return false;
        }
    }

    @Override
    public void buildCity(Vertex vertex) {
        // do nothing
    }

    @Override
    public void buildMilitaryUnit(Vertex vertex) {
        // do nothing
    }

    @Override
    public void trade() {
        // TODO not sure yet
    }

    @Override
    public void moveSoldier(Vertex vertexFrom, Vertex vertexTo) {

    }

    @Override
    public void attackWithSoldier(Vertex vertex) {
        // TODO not sure yet
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
                    "Now player " + mCurrentPlayer.getName() + "'s turn",
                    false);
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

        menu.add(R.id.settlement);

        Log.d("TAG", "found Edge Actions");
    }

    // This method, if given a vertex, will edit the popup menu passed to it so it can be displayed
    // correctly with the proper options in the menu
    @Override
    public void getActions(PopupMenu popupMenu, Vertex vertex) {
        Menu menu = popupMenu.getMenu();

        menu.add(R.id.road);
        Log.d("TAG", "found Vertex Actions");
    }
}
