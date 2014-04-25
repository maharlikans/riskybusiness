package com.slothproductions.riskybusiness.model.GameStates;

import com.slothproductions.riskybusiness.model.Board;
import com.slothproductions.riskybusiness.model.Edge;
import com.slothproductions.riskybusiness.model.GameLoop;
import com.slothproductions.riskybusiness.model.Player;
import com.slothproductions.riskybusiness.model.Vertex;
import com.slothproductions.riskybusiness.view.BoardButtonsFragment;
import com.slothproductions.riskybusiness.view.BoardScreen;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

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
    public void buildRoad(Edge edge) {
        // TODO package the BUILD_ROAD GameAction and effect it
    }

    @Override
    public void buildSettlement(Vertex vertex) {
        // TODO package the BUILD_SETTLEMENT GameAction and effect it
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
}
