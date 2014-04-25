package com.slothproductions.riskybusiness.model.GameStates;

import android.view.View;
import android.widget.ImageView;

import com.View.R;
import com.slothproductions.riskybusiness.model.Board;
import com.slothproductions.riskybusiness.model.Edge;
import com.slothproductions.riskybusiness.model.GameAction;
import com.slothproductions.riskybusiness.model.GameLoop;
import com.slothproductions.riskybusiness.model.Player;
import com.slothproductions.riskybusiness.model.Vertex;
import com.slothproductions.riskybusiness.view.BoardScreen;

import java.lang.Override;
import java.util.HashMap;
import java.util.Map;
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
    Board mBoard;

    public ForwardBeginningGameState (GameLoop gameLoop) {
        mGameLoop = gameLoop;
        mPlayerQueue = mGameLoop.getPlayerQueue();
        mPlayerStack = new Stack<Player>();
        mCurrentPlayer = mGameLoop.getCurrentPlayer();
        mBoardScreen = mGameLoop.getBoardScreen();
        mBoard = mBoardScreen.getBoard();
    }

    @Override
    public void init() {
        // in the forward beginning game state, we don't need to see the tradeButton nor
        // the endTurnButton, so hide them in the init method
        ImageView tradeButton = (ImageView)mBoardScreen.findViewById(R.id.tradeButton);
        tradeButton.setVisibility(View.GONE);
        ImageView endTurnButton = (ImageView)mBoardScreen.findViewById(R.id.endTurnButton);
        endTurnButton.setVisibility(View.GONE);
    }

    @Override
    public void startTurn() {
        // TODO force buildSettlement
        // force buildRoad
        // force endTurn
    }

    @Override
    public void diceRoll() {
        // DO NOTHING
    }

    @Override
    public void buildRoad(Edge edge) {
        GameAction ga = GameAction.BUILD_ROAD;
        Map<String, Object> hm =  new HashMap<String, Object>();
        hm.put("edge", (Object)edge);
        mCurrentPlayer.effect(ga, hm);
    }

    @Override
    public void buildSettlement(Vertex vertex) {
        GameAction ga = GameAction.BUILD_SETTLEMENT;
        Map<String, Object> hm =  new HashMap<String, Object>();
        hm.put("vertex", (Object)vertex);
        mCurrentPlayer.effect(ga, hm);
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
    public void trade() {
        // DO NOTHING
    }

    @Override
    public void moveSoldier(Vertex vertexFrom, Vertex vertexTo) {
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

    @Override
    public Player getCurrentPlayer() {
        return mCurrentPlayer;
    }
}
