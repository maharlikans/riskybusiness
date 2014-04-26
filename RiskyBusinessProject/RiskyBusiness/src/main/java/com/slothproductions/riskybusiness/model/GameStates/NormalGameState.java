package com.slothproductions.riskybusiness.model.GameStates;

import android.util.Log;
import android.view.Menu;
import android.widget.PopupMenu;

import com.slothproductions.riskybusiness.model.Board;
import com.slothproductions.riskybusiness.model.Edge;
import com.slothproductions.riskybusiness.model.GameAction;
import com.slothproductions.riskybusiness.model.GameLoop;
import com.slothproductions.riskybusiness.model.Player;
import com.slothproductions.riskybusiness.model.Vertex;
import com.slothproductions.riskybusiness.view.BoardButtonsFragment;
import com.slothproductions.riskybusiness.view.BoardScreen;

import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by Kyle Maharlika on 4/4/2014.
 */
public class NormalGameState implements GameState {

    Queue<Player> mPlayerQueue;
    GameLoop mGameLoop;
    Player mCurrentPlayer;
    BoardScreen mBoardScreen;
    Board mBoard;

    public NormalGameState (GameLoop gameLoop) {
        mGameLoop = gameLoop;
        mPlayerQueue = mGameLoop.getPlayerQueue();
        mCurrentPlayer = mGameLoop.getCurrentPlayer();
        mBoardScreen = mGameLoop.getBoardScreen();
        mBoard = mBoardScreen.getBoard();
    }

    @Override
    public void init() {
        // TODO: show all the buttons that need to be shown, i.e. all of them
        // waiting until joseph is finished with his part
    }

    @Override
    public void startTurn() {
        // takes the result of the dice roll and results in collection
        // of resources on the main board. This method will consult the models,
        // and then find out which people need to collect resources. Then
        // the method will update the view accordingly after the action is performed in
        // the model by some manipulation by either this class or Ricardo's class (most
        // likely will be Ricardo's class

        // basic structure:
        // take result of dice roll and pass it into a game action "collect resources"
        diceRoll();
    }

    @Override
    public void diceRoll() {
        int rollResult = ((BoardButtonsFragment)mBoardScreen.getButtonsFragment())
                .showRollDialog();
        mBoard.beginTurn(rollResult);
        // TODO update the resources on the game board's view
    }

    @Override
    public boolean buildRoad(Edge edge) {
        // TODO package the BUILD_ROAD GameAction and effect it
        return false;
    }

    @Override
    public boolean buildSettlement(Vertex vertex) {
        // TODO package the BUILD_SETTLEMENT GameAction and effect it
        return false;
    }

    @Override
    public void buildCity(Vertex vertex) {
        // TODO package the BUILD_CITY GameAction and effect it
    }

    @Override
    public void buildMilitaryUnit(Vertex vertex) {
        // TODO package the BUILD_MILITARY_UNIT and effect it
    }

    @Override
    public void trade() {
        // TODO: change the arguments and change this to call the effect change
        // on a player class
    }

    @Override
    public void attackWithSoldier(Vertex vertex) {
        // TODO: change the arguments and change this to call the effect change
    }

    @Override
    public void endTurn() {
        // TODO: change the current player by updating the queue
        // change the view to result in showing the new player's resources
    }



    @Override
    public void moveSoldier(Vertex vertexFrom, Vertex vertexTo) {

    }

    @Override
    public Player getCurrentPlayer() {
        return mCurrentPlayer;
    }

    // This method, if given an edge, will edit the popup menu passed to it so it can be displayed
    // correctly with the proper options in the menu
    public void getActions(PopupMenu popupMenu, Edge edge) {
        ArrayList<GameAction> gameActionArrayListUnfiltered =  mCurrentPlayer.getActions(edge, false);
        ArrayList<GameAction> gameActionArrayListFiltered =  mCurrentPlayer.getActions(edge, true);

        Menu menu = popupMenu.getMenu();

        // then enable only the menu items which are available in the list
        for (GameAction ga : gameActionArrayListUnfiltered) {
            menu.add(mGameLoop.mActionToMenuItemIdMap.get(ga));
            menu.findItem(mGameLoop.mActionToMenuItemIdMap.get(ga)).setEnabled(false);
        }

        for (GameAction ga : gameActionArrayListFiltered) {
            menu.findItem(mGameLoop.mActionToMenuItemIdMap.get(ga)).setEnabled(true);
        }

        Log.d("TAG", "found Edge Actions");
    }

    // This method, if given a vertex, will edit the popup menu passed to it so it can be displayed
    // correctly with the proper options in the menu
    @Override
    public void getActions(PopupMenu popupMenu, Vertex vertex) {
        ArrayList<GameAction> gameActionArrayListUnfiltered =  mCurrentPlayer.getActions(vertex, false);
        ArrayList<GameAction> gameActionArrayListFiltered =  mCurrentPlayer.getActions(vertex, true);

        Menu menu = popupMenu.getMenu();

        // then enable only the menu items which are available in the list
        for (GameAction ga : gameActionArrayListUnfiltered) {
            menu.add(mGameLoop.mActionToMenuItemIdMap.get(ga));
            menu.findItem(mGameLoop.mActionToMenuItemIdMap.get(ga)).setEnabled(false);
        }

        for (GameAction ga : gameActionArrayListFiltered) {
            menu.findItem(mGameLoop.mActionToMenuItemIdMap.get(ga)).setEnabled(true);
        }
        Log.d("TAG", "found Vertex Actions");
    }
}
