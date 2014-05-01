package com.slothproductions.riskybusiness.model;

import android.graphics.Color;
import android.widget.PopupMenu;

import com.View.R;
import com.slothproductions.riskybusiness.model.GameStates.ForwardBeginningGameState;
import com.slothproductions.riskybusiness.model.GameStates.GameState;
import com.slothproductions.riskybusiness.view.BoardScreen;


import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Kyle Maharlika on 4/4/2014.
 */
public class GameLoop {
    private final String TAG = "Game Loop";
    BoardScreen mBoardScreen;
    Board mBoard;

    Queue<Player> mPlayerQueue;
    Player mCurrentPlayer;
    public EnumMap<GameAction, Integer> mActionToMenuItemIdMap;
    public EnumMap<GameAction, Integer> mActionToMenuItemStringMap;

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

        mActionToMenuItemIdMap = new EnumMap<GameAction, Integer>(GameAction.class);
        mActionToMenuItemIdMap.put(GameAction.BUILD_ROAD, R.id.road);
        mActionToMenuItemIdMap.put(GameAction.BUILD_SETTLEMENT, R.id.settlement);
        mActionToMenuItemIdMap.put(GameAction.BUILD_MILITARY_UNIT, R.id.soldier);
        mActionToMenuItemIdMap.put(GameAction.BUILD_CITY, R.id.city);
        mActionToMenuItemIdMap.put(GameAction.MOVE_MILITARY_UNIT, R.id.move);
        mActionToMenuItemIdMap.put(GameAction.REPAIR_CITY, R.id.repaircity);
        mActionToMenuItemIdMap.put(GameAction.REPAIR_SETTLEMENT, R.id.repairsettlement);
        mActionToMenuItemIdMap.put(GameAction.ATTACK, R.id.attack);

        /*Log.d("TAG", "The String ID for the BUILD_CITY item is: " + R.string.upgrade_city);*/

        mActionToMenuItemStringMap = new EnumMap<GameAction, Integer>(GameAction.class);
        mActionToMenuItemStringMap.put(GameAction.BUILD_ROAD, R.string.build_road);
        mActionToMenuItemStringMap.put(GameAction.BUILD_SETTLEMENT, R.string.build_settlement);
        mActionToMenuItemStringMap.put(GameAction.BUILD_MILITARY_UNIT, R.string.train_soldier);
        mActionToMenuItemStringMap.put(GameAction.BUILD_CITY, R.string.upgrade_city);
        mActionToMenuItemStringMap.put(GameAction.MOVE_MILITARY_UNIT, R.string.move_soldier);
        mActionToMenuItemStringMap.put(GameAction.REPAIR_CITY, R.string.repair_city);
        mActionToMenuItemStringMap.put(GameAction.REPAIR_SETTLEMENT, R.string.repair_settlement);
        mActionToMenuItemStringMap.put(GameAction.ATTACK, R.string.attack);

       /* Log.d("TAG", "The ID for BUILDCITY is " +
                mActionToMenuItemIdMap.get(GameAction.BUILD_CITY));
        Log.d("TAG", "mActionToMenuItemStringMap contains the key "
                        + GameAction.BUILD_CITY.toString() + ": "
                + mActionToMenuItemStringMap.containsKey(GameAction.BUILD_CITY));
        Log.d("TAG", "The String ID for BUILDCITY is " +
                mActionToMenuItemStringMap.get(GameAction.BUILD_CITY));

        for (GameAction ga : mActionToMenuItemStringMap.keySet()) {
            Log.d("TAG", "A key of mActionToMenuItemStringMap: " + ga.toString());
        }*/
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

    public boolean buildCity(Vertex vertex) {
        return mCurrentGameState.buildCity(vertex);
    }

    public boolean buildMilitaryUnit(Vertex vertex) {
        return mCurrentGameState.buildMilitaryUnit(vertex);
    }

    public void trade(/*some arguments*/) {
        mCurrentGameState.trade(/*some arguments*/);
    }

    public boolean moveSoldier(Vertex vertexFrom, Vertex vertexTo) {
        return mCurrentGameState.moveMilitaryUnit(vertexFrom, vertexTo);
    }

    public boolean attack(Vertex vertexFrom, Vertex vertexTo, Integer amount) {
        return mCurrentGameState.attack(vertexFrom, vertexTo, amount);
    }

    public void endTurn(/*some arguments*/) {
        mCurrentGameState.endTurn(/*some arguments*/);
    }

    /* GETTERS AND SETTERS */

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
