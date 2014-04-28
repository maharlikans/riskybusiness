package com.slothproductions.riskybusiness.model;

import android.graphics.Color;
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
    public HashMap<Enum, Integer> mActionToMenuItemIdMap;
    public HashMap<Enum, Integer> mActionToMenuItemStringMap;

    // all game state objects will be held in this class
    GameState mCurrentGameState;

    public GameLoop(BoardScreen boardScreen) {
        mBoardScreen = boardScreen;

        // Board will continue to be initialized in the BoardScreenMainFragment
        // not the best, but whatever dude
//        mBoard = ((BoardScreenMainFragment)boardScreen.getScreenFragment()).getBoardData();

        int[] colors = {Color.argb(150,20,120,200), Color.argb(150,10,140,30), Color.argb(150,170,15,15), Color.argb(150,100,100,100)};

        // populate the player queue correctly
        List<Player> playersList = mBoardScreen.getBoard().getPlayers();
        mPlayerQueue = new LinkedList<Player>();
        for (int i = 0; i < playersList.size(); i++) {
//            Log.d("tag", "This player is: " + p.toString());
            Player p = playersList.get(i);
            p.setColor(colors[i]);
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
        mActionToMenuItemIdMap.put(GameAction.REPAIR_CITY, R.id.repaircity);
        mActionToMenuItemIdMap.put(GameAction.REPAIR_SETTLEMENT, R.id.repairsettlement);

        mActionToMenuItemStringMap = new HashMap<Enum, Integer>();
        mActionToMenuItemIdMap.put(GameAction.BUILD_ROAD, R.string.build_road);
        mActionToMenuItemIdMap.put(GameAction.BUILD_SETTLEMENT, R.string.build_settlement);
        mActionToMenuItemIdMap.put(GameAction.BUILD_MILITARY_UNIT, R.string.train_soldier);
        mActionToMenuItemIdMap.put(GameAction.BUILD_CITY, R.string.upgrade_city);
        mActionToMenuItemIdMap.put(GameAction.MOVE_MILITARY_UNIT, R.string.move_soldier);
        mActionToMenuItemIdMap.put(GameAction.REPAIR_CITY, R.string.repair_city);
        mActionToMenuItemIdMap.put(GameAction.REPAIR_SETTLEMENT, R.string.repair_settlement);
    }

    // ALL BELOW TODO
    public void startGame() {
        // only reason I use this instead of just changing the game state is because the game state
        // must not only be changed, init() must also be called on the game state, which is built
        // into the setCurrentGameState function.
        ForwardBeginningGameState forwardBeginningGameState =
                new ForwardBeginningGameState(this);
        setCurrentGameState(forwardBeginningGameState);
    }

    public void startTurn() {
        mCurrentGameState.startTurn();
    }

    public boolean buildRoad(Edge edge) {
        return mCurrentGameState.buildRoad(edge);
    }

    public boolean buildSettlement(Vertex vertex) {
        return mCurrentGameState.buildSettlement(vertex);
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

    public Queue<Player> getPlayerQueue() {
        return mPlayerQueue;
    }

    public BoardScreen getBoardScreen() {
        return mBoardScreen;
    }

//    public Board getBoard() {
//        return mBoard;
//    }

    public void getActions(PopupMenu popupMenu, Edge edge) {
        mCurrentGameState.getActions(popupMenu, edge);
    }

    public void getActions(PopupMenu popupMenu, Vertex vertex) {
        mCurrentGameState.getActions(popupMenu, vertex);
    }

    public Player getCurrentPlayer() {
        return mCurrentPlayer;
    }
}
