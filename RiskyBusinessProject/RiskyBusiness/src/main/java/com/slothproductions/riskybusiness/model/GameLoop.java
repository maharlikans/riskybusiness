package com.slothproductions.riskybusiness.model;

import com.slothproductions.riskybusiness.model.GameStates.BackwardBeginningGameState;
import com.slothproductions.riskybusiness.model.GameStates.EndGameState;
import com.slothproductions.riskybusiness.model.GameStates.ForwardBeginningGameState;
import com.slothproductions.riskybusiness.model.GameStates.GameState;
import com.slothproductions.riskybusiness.model.GameStates.NormalGameState;
import com.slothproductions.riskybusiness.view.BoardScreen;
import com.slothproductions.riskybusiness.view.BoardScreenMainFragment;


import java.util.List;
import java.util.Queue;

/**
 * Created by Kyle Maharlika on 4/4/2014.
 * Status: in progress... still need to construct object correctly
 *         encapsulate the class
 *
 */
public class GameLoop {
    BoardScreen mBoardScreen;
    Board mBoard;

    Queue<Player> mPlayerQueue;

    // all game state objects will be held in this class
    GameState mCurrentGameState;
    ForwardBeginningGameState mForwardBeginningGameState;
    BackwardBeginningGameState mBackwardBeginningGameState;
    NormalGameState mNormalGameState;
    EndGameState mEndGameState;

    public GameLoop(BoardScreen boardScreen) {
        mBoardScreen = boardScreen;

        // Board will continue to be initialized in the BoardScreenMainFragment
        // not the best, but whatever dude
        mBoard = ((BoardScreenMainFragment)boardScreen.getScreenFragment()).getBoardData();

        mForwardBeginningGameState = new ForwardBeginningGameState(this);
        mBackwardBeginningGameState = new BackwardBeginningGameState(this);
        mNormalGameState = new NormalGameState(this);
        mEndGameState = new EndGameState(this);

        // populate the player queue correctly
        List<Player> playersList = mBoard.getPlayers();
        for (Player p : playersList) {
            mPlayerQueue.offer(p);
        }

        // only reason I use this instead of just changing the game state is because the game state
        // must not only be changed, init() must also be called on the game state, which is built
        // into the setCurrentGameState function.
        setCurrentGameState(mForwardBeginningGameState);
    }

    // ALL BELOW TODO
    public void rollDice(/* some arguments*/) {
        mCurrentGameState.startTurn(/*some arguments*/);
    }

    public void buildRoad(/* some arguments*/) {
        mCurrentGameState.buildRoad(/* some arguments */);
    }

    public void buildSettlement(/* some arguments*/) {
        mCurrentGameState.buildSettlement(/* some arguments*/);
    }

    public void buildCity(/* some arguments*/) {
        mCurrentGameState.buildCity(/* some arguments*/);
    }

    public void buildMilitaryUnit(/* some arguments*/) {
        mCurrentGameState.buildMilitaryUnit(/* some arguments*/);
    }

    public void trade(/*some arguments*/) {
        mCurrentGameState.trade(/*some arguments*/);
    }

    public void moveSoldier(/*some arguments*/) {
        mCurrentGameState.moveSoldier(,/*some arguments*/);
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

    // on setting the current game state, must also call init because the state must hide and
    // show specific buttons on the screen depending on what they'd like to do
    public void setCurrentGameState(GameState currentGameState) {
        mCurrentGameState = currentGameState;
        mCurrentGameState.init();
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

    public Queue<Player> getPlayerQueue() {
        return mPlayerQueue;
    }

    public BoardScreen getBoardScreen() {
        return mBoardScreen;
    }
}
