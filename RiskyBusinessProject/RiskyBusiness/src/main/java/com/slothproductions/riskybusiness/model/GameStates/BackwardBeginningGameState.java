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
    }

    @Override
    public void rollDice() {
        // DOES NOTHING, potentially throw exception
    }

    @Override
    public void build() {
        // TODO not sure yet
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
    }
}
