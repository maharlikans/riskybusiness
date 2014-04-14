package com.slothproductions.riskybusiness.model.GameStates;

import com.slothproductions.riskybusiness.model.GameLoop;
import com.slothproductions.riskybusiness.model.Player;

import java.util.Stack;

/**
 * Created by Kyle Maharlika on 4/4/2014.
 */
public class BackwardBeginningGameState implements GameState {

    Stack<Player> mPlayerStack;
    Player mCurrentPlayer;
    GameLoop mGameLoop;

    public BackwardBeginningGameState (GameLoop gameLoop) {
        mGameLoop = gameLoop;
        mPlayerStack = new Stack<Player>();
        mCurrentPlayer = /*first player pop off the stack*/;
        // TODO: know some sort of ordering to the players
        // via a queue?
    }

    @Override
    public void init() {
        // hide the necessary buttons here and perform setup
        // on the view elements
        // TODO as Joseph fixes this part of things
    }

    @Override
    public void startTurn() {
        // force buildSettlement
        // force collectResources
        // force endTurn
    }

    @Override
    public void rollDice() {
        // DOES NOTHING, potentially throw exception
    }

    @Override
    public void buildRoad() {
        // TODO package the BUILD_ROAD GameAction here
    }

    @Override
    public void buildSettlement() {
        // TODO package the BUILD_SETTLEMENT GameAction here
        // COLLECT RESOURCES HERE
    }

    @Override
    public void buildCity() {
        // do nothing
    }

    @Override
    public void buildMilitaryUnit() {
        // do nothing
    }

    @Override
    public void trade() {
        // TODO not sure yet
    }

    @Override
    public void moveSoldier() {
        // TODO not sure yet
    }

    @Override
    public void attackWithSoldier() {
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

    public void setPlayerStack(Stack<Player> playerStack) {
        mPlayerStack = playerStack;
    }
}
