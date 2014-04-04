package com.slothproductions.riskybusiness.model.GameStates;

import com.slothproductions.riskybusiness.model.GameLoop;
import com.slothproductions.riskybusiness.model.Player;

import java.util.Stack;

/**
 * Created by Kyle Maharlika on 4/4/2014.
 */
public class ForwardBeginningGameState implements GameState{
    Stack<Player> mPlayerStack;
    Player mCurrentPlayer;
    GameLoop mGameLoop;

    public ForwardBeginningGameState (GameLoop gameLoop) {
        mGameLoop = gameLoop;
        mCurrentPlayer = /*pop first player in the queue*/;
        mPlayerStack = new Stack<Player>();

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
        // DOES NOTHING
        // TODO: potentially throw exception
    }

    @Override
    public void build() {
        // TODO: change the arguments and change this to call the effect change
        // on a player class
    }

    @Override
    public void trade() {
        // TODO: not sure yet
    }

    @Override
    public void moveSoldier() {
        // TODO: not sure yet
    }

    @Override
    public void attackWithSoldier() {
        // TODO: not sure yet
    }

    @Override
    public void endTurn() {
        // put the current player in the stack
        // check if all the players have gone (i.e. the Queue is empty)
        // if so,
        //     transition to the next state and pass it the stack from this state
        // if not,
        //     mCurrentPlayer = /*pop from the player queue*/;
        //     display their resources on the game board
    }
}
