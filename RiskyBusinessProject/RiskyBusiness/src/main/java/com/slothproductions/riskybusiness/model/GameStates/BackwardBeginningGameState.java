package com.slothproductions.riskybusiness.model.GameStates;

import android.util.Log;
import android.view.Menu;
import android.widget.PopupMenu;

import com.View.R;
import com.slothproductions.riskybusiness.model.Edge;
import com.slothproductions.riskybusiness.model.GameLoop;
import com.slothproductions.riskybusiness.model.Player;
import com.slothproductions.riskybusiness.model.Vertex;

import java.util.Stack;

/**
 * Created by Kyle Maharlika on 4/4/2014.
 */
public class BackwardBeginningGameState implements GameState {

    Stack<Player> mPlayerStack;
    Player mCurrentPlayer;
    GameLoop mGameLoop;

    public BackwardBeginningGameState (GameLoop gameLoop, Stack<Player> playerStack) {
        mGameLoop = gameLoop;
        mPlayerStack = playerStack;
        mCurrentPlayer = mPlayerStack.pop();
        // TODO: know some sort of ordering to the players
        // via a queue?
        startTurn();
    }

    @Override
    public void init() {
        // hide the necessary buttons here and perform setup
        // on the view elements
    }

    @Override
    public void startTurn() {
        // force buildSettlement
        // force collectResources
        // force endTurn
    }

    @Override
    public void diceRoll() {

    }

    @Override
    public boolean buildRoad(Edge edge) {
        // TODO package the BUILD_ROAD GameAction here
        return false;
    }

    @Override
    public boolean buildSettlement(Vertex vertex) {
        // TODO package the BUILD_SETTLEMENT GameAction here
        // COLLECT RESOURCES HERE
        return false;
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
        // put the current player in the queue
        // check if all the players have gone (i.e. the Stack is empty)
        // if so,
        //     transition to the NormalGameState and pass it the queue from this state
        // if not,
        //     mCurrentPlayer = /*pop from the player stack*/;
        //     display their resources on the game board
        //     force turn to begin
    }

    @Override
    public Player getCurrentPlayer() {
        return mCurrentPlayer;
    }

    public void setPlayerStack(Stack<Player> playerStack) {
        mPlayerStack = playerStack;
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
