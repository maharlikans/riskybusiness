package com.slothproductions.riskybusiness.model;

import com.slothproductions.riskybusiness.model.GameStates.BackwardBeginningGameState;
import com.slothproductions.riskybusiness.model.GameStates.EndGameState;
import com.slothproductions.riskybusiness.model.GameStates.ForwardBeginningGameState;
import com.slothproductions.riskybusiness.model.GameStates.GameState;
import com.slothproductions.riskybusiness.model.GameStates.NormalGameState;
import com.slothproductions.riskybusiness.view.BoardScreen;


import java.util.Queue;

/**
 * Created by Kyle Maharlika on 4/4/2014.
 * Status: in progress... still need to construct object correctly
 *         encapsulate the class
 *
 */
public class GameLoop {
    BoardScreen mBoardScreen;

    // all game state objects will be held in this class
    GameState mCurrentGameState;
    ForwardBeginningGameState mForwardBeginningGameState;
    BackwardBeginningGameState mBackwardBeginningGameState;
    NormalGameState mNormalGameState;
    EndGameState mEndGameState;

    public GameLoop(BoardScreen boardScreen) {
        mBoardScreen = boardScreen;

        mForwardBeginningGameState = new ForwardBeginningGameState(this);
        mBackwardBeginningGameState = new BackwardBeginningGameState(this);
        mNormalGameState = new NormalGameState(this);
        mEndGameState = new EndGameState(this);

        setCurrentGameState(mForwardBeginningGameState);
    }

    // ALL BELOW TODO
    public void rollDice(/* some arguments*/) {
        mCurrentGameState.rollDice(/*some arguments*/);
    }

    public void build(/*some arguments*/) {
        mCurrentGameState.build(/*some arguments*/);
    }

    public void trade(/*some arguments*/) {
        mCurrentGameState.trade(/*some arguments*/);
    }

    public void moveSoldier(/*some arguments*/) {
        mCurrentGameState.moveSoldier(/*some arguments*/);
    }

    public void attackWithSoldier(/*some arguments*/) {
        mCurrentGameState.attackWithSoldier(/*some arguments*/);
    }

    public void endTurn(/*some arguments*/) {
        mCurrentGameState.endTurn(/*some arguments*/);
    }

    // all getters and setters

    public GameState getCurrentGameState() {
        return mCurrentGameState;
    }

    public void setCurrentGameState(GameState currentGameState) {
        mCurrentGameState = currentGameState;
        mCurrentGameState.init();   // init call will set up UI correctly
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
}
