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
        mCurrentPlayer = mGameLoop.getPlayerQueue().poll();
        mPlayerStack = new Stack<Player>();

        // TODO: know some sort of ordering to the players
        // via a queue?

        // force this player to start the turn
        startTurn();
    }

    @Override
    public void init() {
        // hide the necessary buttons here and perform setup
        // on the view elements
        // TODO as Joseph fixes this part of things
    }

    @Override
    public void startTurn() {
        // TODO force buildSettlement
        // force buildRoad
        // force endTurn
    }

    @Override
    public void rollDice() {
        // DOES NOTHING
        // TODO: potentially throw exception
    }

    @Override
    public void buildRoad() {
        // TODO package the BUILD_ROAD GameAction here
    }

    @Override
    public void buildSettlement() {
        // TODO package the BUILD_SETTLEMENT GameAction here
    }

    @Override
    public void buildCity() {
        // DO NOTHING
    }

    @Override
    public void buildMilitaryUnit() {
        // DO NOTHING
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
        //     using setPlayerStack
        // if not,
        //     mCurrentPlayer = /*pop from the player queue*/;
        //     display their resources on the game board
        //     force turn to begin
    }
}
