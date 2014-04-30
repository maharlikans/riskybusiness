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
import com.slothproductions.riskybusiness.view.PlayerInfoFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * Created by Kyle Maharlika on 4/4/2014.
 */
public class NormalGameState implements GameState {

    Queue<Player> mPlayerQueue;
    GameLoop mGameLoop;
    Player mCurrentPlayer;
    BoardScreen mBoardScreen;
    BoardButtonsFragment mBoardButtonsFragment;
    Board mBoard;
    PlayerInfoFragment mPlayerInfo;

    public NormalGameState (GameLoop gameLoop) {
        mGameLoop = gameLoop;
        mPlayerQueue = mGameLoop.getPlayerQueue();
        mCurrentPlayer = mGameLoop.getCurrentPlayer();
        mBoardScreen = mGameLoop.getBoardScreen();
        mBoard = mBoardScreen.getBoard();
        mPlayerInfo = (PlayerInfoFragment)mBoardScreen.getPlayerInfoFragment();
    }

    @Override
    public void init() {
        mBoardButtonsFragment = (BoardButtonsFragment)mBoardScreen.getButtonsFragment();
        View v = mBoardButtonsFragment.getView();
        ImageView tradeButton = (ImageView)v.findViewById(R.id.tradeButton);
        tradeButton.setVisibility(View.VISIBLE);
        ImageView endTurnButton = (ImageView)v.findViewById(R.id.endTurnButton);
        endTurnButton.setVisibility(View.VISIBLE);

        mBoardButtonsFragment.createToast("Now in NormalGameState", false);
        startTurn();
    }

    @Override
    public void startTurn() {
        diceRoll();
    }

    @Override
    public void diceRoll() {
        int rollResult = ((BoardButtonsFragment)mBoardScreen.getButtonsFragment())
                .showRollDialog();
        Log.d("TAG", "The dice roll result is " + rollResult);
        mBoard.beginTurn(rollResult);
        mPlayerInfo.updatePlayerValues();
    }

    @Override
    public boolean buildRoad(Edge edge) {
        GameAction gameAction = GameAction.BUILD_ROAD;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("edge", edge);
        return mCurrentPlayer.effect(gameAction, map);
    }

    @Override
    public boolean buildSettlement(Vertex vertex) {
        GameAction gameAction = GameAction.BUILD_SETTLEMENT;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("vertex", vertex);
        return mCurrentPlayer.effect(gameAction, map);
    }

    @Override
    public boolean buildCity(Vertex vertex) {
        GameAction gameAction = GameAction.BUILD_CITY;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("vertex", vertex);
        return mCurrentPlayer.effect(gameAction, map);
    }

    @Override
    public boolean buildMilitaryUnit(Vertex vertex) {
        GameAction gameAction = GameAction.BUILD_MILITARY_UNIT;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("vertex", vertex);
        return mCurrentPlayer.effect(gameAction, map);
    }

    @Override
    public boolean moveMilitaryUnit(Vertex vertexFrom, Vertex vertexTo) {
        GameAction gameAction = GameAction.MOVE_MILITARY_UNIT;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("vertex_from", vertexFrom);
        map.put("vertex_to", vertexTo);
        return mCurrentPlayer.effect(gameAction, map);
    }

    @Override
    public boolean repairSettlement(Vertex vertex) {
        GameAction gameAction = GameAction.REPAIR_SETTLEMENT;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("vertex", vertex);
        return mCurrentPlayer.effect(gameAction, map);
    }

    @Override
    public boolean repairCity(Vertex vertex) {
        GameAction gameAction = GameAction.REPAIR_CITY;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("vertex", vertex);
        return mCurrentPlayer.effect(gameAction, map);
    }

    @Override
    public boolean attack(Vertex vertex) {
        GameAction gameAction = GameAction.ATTACK;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("vertex", vertex);
        return mCurrentPlayer.effect(gameAction, map);
    }

    @Override
    public void trade() {
        // TODO: change the arguments and change this to call the effect change
        // on a player class
    }

    @Override
    public void endTurn() {
        mPlayerQueue.offer(mCurrentPlayer);
        mCurrentPlayer = mPlayerQueue.poll();
        mBoardButtonsFragment.createToast(
                "Now " + mCurrentPlayer.getName() + "'s turn",
                false);
        startTurn();
        mPlayerInfo.updatePlayerValues();
    }

    @Override
    public Player getCurrentPlayer() {
        return mCurrentPlayer;
    }

    // This method, if given an edge, will edit the popup menu passed to it so it can be displayed
    // correctly with the proper options in the menu
    public void getActions(PopupMenu popupMenu, Edge edge) {
        ArrayList<GameAction> gameActionArrayListUnfiltered =
                mCurrentPlayer.getActions(edge, false);
        ArrayList<GameAction> gameActionArrayListFiltered =
                mCurrentPlayer.getActions(edge, true);

        for (GameAction ga : gameActionArrayListUnfiltered) {
            Log.d("TAG", "One available game action is " + ga.toString());
        }

        Menu menu = popupMenu.getMenu();

        // then enable only the menu items which are available in the list
        for (GameAction ga : gameActionArrayListUnfiltered) {
            menu.add(0, mGameLoop.mActionToMenuItemIdMap.get(ga), 0,
                    mGameLoop.mActionToMenuItemStringMap.get(ga));
            menu.findItem(mGameLoop.mActionToMenuItemIdMap.get(ga)).setEnabled(false);
        }

        for (GameAction ga : gameActionArrayListFiltered) {
            menu.findItem(mGameLoop.mActionToMenuItemIdMap.get(ga)).setEnabled(true);
        }

        Log.d("TAG", "found Edge Actions");
    }

    @Override
    public void getActions(PopupMenu popupMenu, Vertex vertex) {
        ArrayList<GameAction> gameActionArrayListUnfiltered =
                mCurrentPlayer.getActions(vertex, false);
        ArrayList<GameAction> gameActionArrayListFiltered =
                mCurrentPlayer.getActions(vertex, true);

        for (GameAction ga : gameActionArrayListUnfiltered) {
            Log.d("TAG", "One available game action is " + ga.toString());
        }

        Menu menu = popupMenu.getMenu();

        // then enable only the menu items which are available in the list
        for (GameAction ga : gameActionArrayListUnfiltered) {
            menu.add(0, mGameLoop.mActionToMenuItemIdMap.get(ga), 0,
                    mGameLoop.mActionToMenuItemStringMap.get(ga));
            menu.findItem(mGameLoop.mActionToMenuItemIdMap.get(ga)).setEnabled(false);
        }

        for (GameAction ga : gameActionArrayListFiltered) {
            menu.findItem(mGameLoop.mActionToMenuItemIdMap.get(ga)).setEnabled(true);
        }
        Log.d("TAG", "found Vertex Actions");
    }
}
