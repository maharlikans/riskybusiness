package com.slothproductions.riskybusiness.model.GameStates;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.View.R;
import com.slothproductions.riskybusiness.model.Board;
import com.slothproductions.riskybusiness.model.Edge;
import com.slothproductions.riskybusiness.model.GameAction;
import com.slothproductions.riskybusiness.model.GameLoop;
import com.slothproductions.riskybusiness.model.Player;
import com.slothproductions.riskybusiness.model.Vertex;
import com.slothproductions.riskybusiness.view.BoardButtonsFragment;
import com.slothproductions.riskybusiness.view.BoardScreen;

import java.lang.Override;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by Kyle Maharlika on 4/4/2014.
 */
public class ForwardBeginningGameState implements GameState{
    Stack<Player> mPlayerStack;
    Queue<Player> mPlayerQueue;
    Player mCurrentPlayer;
    GameLoop mGameLoop;
    BoardScreen mBoardScreen;
    BoardButtonsFragment mBoardButtonsFragment;
    Board mBoard;
    HashMap<Player, Boolean> mPlayerBooleanHashMap;

    public ForwardBeginningGameState (GameLoop gameLoop) {
        mGameLoop = gameLoop;
        mPlayerQueue = mGameLoop.getPlayerQueue();
        mPlayerStack = new Stack<Player>();
        mCurrentPlayer = mGameLoop.getCurrentPlayer();
        mBoardScreen = mGameLoop.getBoardScreen();
        mBoard = mBoardScreen.getBoard();
    }

    @Override
    public void init() {
        // in the forward beginning game state, we don't need to see the tradeButton nor
        // the endTurnButton, so hide them in the init method
        mBoardButtonsFragment = (BoardButtonsFragment)mBoardScreen.getButtonsFragment();
        /*if (boardButtonsFragment == null)
            Log.d("TAG", "BoardButtonsFragment is null");*/
        View v = mBoardButtonsFragment.getView();
        /*if (v == null) {
            Log.d("TAG", "View for BoardButtonsFragment is not created");
        }*/
        ImageView tradeButton = (ImageView)v.findViewById(R.id.tradeButton);
        tradeButton.setVisibility(View.GONE);
        ImageView endTurnButton = (ImageView)v.findViewById(R.id.endTurnButton);
        endTurnButton.setVisibility(View.GONE);
    }

    @Override
    public void startTurn() {
    }

    @Override
    public void diceRoll() {
        // DO NOTHING
    }

    @Override
    public void buildRoad(Edge edge) {
        if (mCurrentPlayer.canBuildInitial(edge, 1)) { // 1 means the first
            mCurrentPlayer.buildInitial(edge, 1);
        } else {
            mBoardButtonsFragment.createToast("You can't build a road here.", false);
        }
    }

    @Override
    public void buildSettlement(Vertex vertex) {
        if (mCurrentPlayer.canBuildInitial(vertex, 1)) {
            mCurrentPlayer.buildInitial(vertex, 1);
        } else {
            mBoardButtonsFragment.createToast("You can't build a settlement here.", false);
        }
    }

    @Override
    public void buildCity(Vertex vertex) {
        // DO NOTHING
    }

    @Override
    public void buildMilitaryUnit(Vertex vertex) {
        // DO NOTHING
    }

    @Override
    public void trade() {
        // DO NOTHING
    }

    @Override
    public void moveSoldier(Vertex vertexFrom, Vertex vertexTo) {
        // DO NOTHING
    }

    @Override
    public void attackWithSoldier(Vertex vertex) {
        // DO NOTHING
    }

    @Override
    public void endTurn() {
        // put the current player in the stack
        // check if all the players have gone (i.e. the Queue is empty)
        // if so,
        //     transition to the next state and pass it the stack from this state
        //     using setPlayerStack
        // if not,
        //     mCurrentPlayer = /*pop from the player queue*/;
        //     display their resources on the game board
        //     force turn to begin
        mPlayerStack.push(mCurrentPlayer);
        if (mPlayerQueue.isEmpty()) {
            BackwardBeginningGameState backwardBeginningGameState =
                    new BackwardBeginningGameState(mGameLoop, mPlayerStack);
            mGameLoop.setCurrentGameState(backwardBeginningGameState);
        } else {
            mCurrentPlayer = mPlayerQueue.poll();
            // TODO BoardScreenMainFragment updateResourceView() method
            // get the resources from the player class and pass it with the method
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

        menu.add(0, R.id.road, 0, R.string.build_road);

        Log.d("TAG", "found Edge Actions");
    }

    // This method, if given a vertex, will edit the popup menu passed to it so it can be displayed
    // correctly with the proper options in the menu
    @Override
    public void getActions(PopupMenu popupMenu, Vertex vertex) {
        Menu menu = popupMenu.getMenu();

        menu.add(0, R.id.settlement, 0, R.string.build_settlement);
        Log.d("TAG", "found Vertex Actions");
    }
}
