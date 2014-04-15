package com.slothproductions.riskybusiness.model.GameStates;

import com.View.R;
import com.slothproductions.riskybusiness.model.Edge;
import com.slothproductions.riskybusiness.model.GameLoop;
import com.slothproductions.riskybusiness.model.Player;
import com.slothproductions.riskybusiness.model.Vertex;
import com.slothproductions.riskybusiness.view.BoardScreen;

import java.util.Queue;
import java.util.Stack;

/**
 * Created by Kyle Maharlika on 4/4/2014.
 */
public class ForwardBeginningGameState implements GameState{
    Stack<Player> mPlayerStack;
    Queue<Player> mPlayerQueue;
    Player mCurrentPlayer;
    GameLoop mGameLoop;
    BoardScreen mBoardScreen;

    public ForwardBeginningGameState (GameLoop gameLoop) {
        mGameLoop = gameLoop;
        mPlayerQueue = mGameLoop.getPlayerQueue();
        mPlayerStack = new Stack<Player>();
        mCurrentPlayer = mPlayerQueue.poll();
        mBoardScreen = mGameLoop.getBoardScreen();

        // force this player to start the turn
        startTurn();
    }

    @Override
    public void init() {
        // TODO merge the master branch into this one
    }

    @Override
    public void startTurn(int rollResult) {
        // TODO force buildSettlement
        // force buildRoad
        // force endTurn
    }

    @Override
    public void startTurn() {
        // DOES NOTHING
        // TODO: potentially throw exception
    }

    @Override
    public void buildRoad(Edge edge) {
        // TODO package the BUILD_ROAD GameAction here
    }

    @Override
    public void buildSettlement(Vertex vertex) {
        // TODO package the BUILD_SETTLEMENT GameAction here
    }

    @Override
    public void buildCity(Vertex vertex) {
        // DO NOTHING
    }

    @Override
    public void buildMilitaryUnit(Vertex vertex) {
        // DO NOTHING
    }

    @Override
    public void trade(Player other) {
        // DO NOTHING
    }

    @Override
    public void moveSoldier(Vertex vertexFrom, Edge edgeAcross) {
        // DO NOTHING
    }

    @Override
    public void attackWithSoldier(Vertex vertex) {
        // DO NOTHING
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
