package com.slothproductions.riskybusiness.model;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.View.R;
import com.slothproductions.riskybusiness.model.GameStates.BackwardBeginningGameState;
import com.slothproductions.riskybusiness.model.GameStates.EndGameState;
import com.slothproductions.riskybusiness.model.GameStates.ForwardBeginningGameState;
import com.slothproductions.riskybusiness.model.GameStates.GameState;
import com.slothproductions.riskybusiness.model.GameStates.NormalGameState;
import com.slothproductions.riskybusiness.view.BoardScreen;
import com.slothproductions.riskybusiness.view.BoardScreenMainFragment;


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Kyle Maharlika on 4/4/2014.
 * Status: in progress... still need to construct object correctly
 *         encapsulate the class
 */
public class GameLoop {
    private final String TAG = "Game Loop";
    BoardScreen mBoardScreen;
    Board mBoard;

    Queue<Player> mPlayerQueue;
    Player mCurrentPlayer;
    HashMap<Enum, Integer> mActionToMenuItemIdMap;

    // all game state objects will be held in this class
    GameState mCurrentGameState;
    ForwardBeginningGameState mForwardBeginningGameState;
    BackwardBeginningGameState mBackwardBeginningGameState;
    NormalGameState mNormalGameState;
    EndGameState mEndGameState;

    public GameLoop(BoardScreen boardScreen) {
        mBoardScreen = boardScreen;

        // Board will continue to be initialized in the BoardScreenMainFragment
        // not the best, but whatever dude
//        mBoard = ((BoardScreenMainFragment)boardScreen.getScreenFragment()).getBoardData();

        // populate the player queue correctly
        List<Player> playersList = mBoardScreen.getBoard().getPlayers();
        mPlayerQueue = new LinkedList<Player>();
        for (Player p : playersList) {
//            Log.d("tag", "This player is: " + p.toString());
            mPlayerQueue.offer(p);
        }

        // set the current player
        mCurrentPlayer = mPlayerQueue.poll();

        mActionToMenuItemIdMap = new HashMap<Enum, Integer>();
        mActionToMenuItemIdMap.put(GameAction.BUILD_ROAD, R.id.road);
        mActionToMenuItemIdMap.put(GameAction.BUILD_SETTLEMENT, R.id.settlement);
        mActionToMenuItemIdMap.put(GameAction.BUILD_MILITARY_UNIT, R.id.soldier);
        mActionToMenuItemIdMap.put(GameAction.BUILD_CITY, R.id.city);
        mActionToMenuItemIdMap.put(GameAction.MOVE_MILITARY_UNIT, R.id.move);

        mForwardBeginningGameState = new ForwardBeginningGameState(this);
//        mBackwardBeginningGameState = new BackwardBeginningGameState(this);
//        mNormalGameState = new NormalGameState(this);
//        mEndGameState = new EndGameState(this);

        // only reason I use this instead of just changing the game state is because the game state
        // must not only be changed, init() must also be called on the game state, which is built
        // into the setCurrentGameState function.

        //setCurrentGameState(mForwardBeginningGameState);
    }

    // ALL BELOW TODO
    public void startTurn() {
        mCurrentGameState.startTurn();
    }

    public void buildRoad(Edge edge) {
        mCurrentGameState.buildRoad(edge);
    }

    public void buildSettlement(Vertex vertex) {
        mCurrentGameState.buildSettlement(vertex);
    }

    public void buildCity(Vertex vertex) {
        mCurrentGameState.buildCity(vertex);
    }

    public void buildMilitaryUnit(Vertex vertex) {
        mCurrentGameState.buildMilitaryUnit(vertex);
    }

    public void trade(/*some arguments*/) {
        mCurrentGameState.trade(/*some arguments*/);
    }

    public void moveSoldier(Vertex vertexFrom, Vertex vertexTo) {
        mCurrentGameState.moveSoldier(vertexFrom, vertexTo);
    }

    public void attackWithSoldier(Vertex vertex) {
        mCurrentGameState.attackWithSoldier(vertex);
    }

    public void endTurn(/*some arguments*/) {
        mCurrentGameState.endTurn(/*some arguments*/);
    }

    // all getters and setters

    public GameState getCurrentGameState() {
        return mCurrentGameState;
    }

    // on setting the current game state, must also call init because the state must hide and
    // show specific buttons on the screen depending on what they'd like to do
    public void setCurrentGameState(GameState currentGameState) {
        mCurrentGameState = currentGameState;
        mCurrentGameState.init();
    }

    public ForwardBeginningGameState getForwardBeginningGameState() {
        return mForwardBeginningGameState;
    }

    public BackwardBeginningGameState getBackwardBeginningGameState() {
        return mBackwardBeginningGameState;
    }

    public NormalGameState getNormalGameState() {
        return mNormalGameState;
    }

    public EndGameState getEndGameState() {
        return mEndGameState;
    }

    public Queue<Player> getPlayerQueue() {
        return mPlayerQueue;
    }

    public BoardScreen getBoardScreen() {
        return mBoardScreen;
    }

//    public Board getBoard() {
//        return mBoard;
//    }

    // This method, if given an edge, will edit the popup menu passed to it so it can be displayed
    // correctly with the proper options in the menu
    public void getActions(PopupMenu popupMenu, Edge edge) {
        ArrayList<GameAction> gameActionArrayListUnfiltered =  mCurrentPlayer.getActions(edge, false);
        ArrayList<GameAction> gameActionArrayListFiltered =  mCurrentPlayer.getActions(edge, true);

        Menu menu = popupMenu.getMenu();

        // then enable only the menu items which are available in the list
        for (GameAction ga : gameActionArrayListUnfiltered) {
            menu.add(mActionToMenuItemIdMap.get(ga));
            menu.findItem(mActionToMenuItemIdMap.get(ga)).setEnabled(false);
        }

        for (GameAction ga : gameActionArrayListFiltered) {
            menu.findItem(mActionToMenuItemIdMap.get(ga)).setEnabled(true);
        }

        Log.d(TAG, "found Edge Actions");
    }


    // This method, if given a vertex, will edit the popup menu passed to it so it can be displayed
    // correctly with the proper options in the menu
    public void getActions(PopupMenu popupMenu, Vertex vertex) {
        ArrayList<GameAction> gameActionArrayListUnfiltered =  mCurrentPlayer.getActions(vertex, false);
        ArrayList<GameAction> gameActionArrayListFiltered =  mCurrentPlayer.getActions(vertex, true);

        Menu menu = popupMenu.getMenu();

        // then enable only the menu items which are available in the list
        for (GameAction ga : gameActionArrayListUnfiltered) {
            menu.add(mActionToMenuItemIdMap.get(ga));
            menu.findItem(mActionToMenuItemIdMap.get(ga)).setEnabled(false);
        }

        for (GameAction ga : gameActionArrayListFiltered) {
            menu.findItem(mActionToMenuItemIdMap.get(ga)).setEnabled(true);
        }
        Log.d(TAG, "found Vertex Actions");
    }

    public Player getCurrentPlayer() {
        return mCurrentPlayer;
    }
}
