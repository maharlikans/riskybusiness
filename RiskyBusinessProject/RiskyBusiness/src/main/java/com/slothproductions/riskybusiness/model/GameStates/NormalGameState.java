package com.slothproductions.riskybusiness.model.GameStates;

import com.slothproductions.riskybusiness.model.GameLoop;
import com.slothproductions.riskybusiness.model.Player;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by Kyle Maharlika on 4/4/2014.
 */
public class NormalGameState implements GameState {

    Queue<Player> mPlayerQueue;
    GameLoop mGameLoop;

    public NormalGameState (GameLoop gameLoop) {
        mGameLoop = gameLoop;
        mPlayerQueue = new LinkedList<Player>();
        // TODO: populate the queue with players somehow
    }

    @Override
    public void init() {
        // TODO: show all the buttons that need to be shown, i.e. all of them
        // waiting until joseph is finished with his part
    }

    @Override
    public void rollDice() {
        // takes the result of the dice roll and results in collection
        // of resources on the main board. This method will consult the models,
        // and then find out which people need to collect resources. Then
        // the method will update the view accordingly after the action is performed in
        // the model by some manipulation by either this class or Ricardo's class (most
        // likely will be Ricardo's class

        // basic structure:
        // take result of dice roll and pass it into a game action "collect resources"
    }

    @Override
    public void build() {
        // TODO: change the arguments and change this to call the effect change
        // on a player class
    }

    @Override
    public void trade() {
        // TODO: change the arguments and change this to call the effect change
        // on a player class
    }

    @Override
    public void moveSoldier() {
        // TODO: change the arguments and change this to call the effect change
        // on a player class
    }

    @Override
    public void attackWithSoldier() {
        // TODO: change the arguments and change this to call the effect change
        // on a player class
    }

    @Override
    public void endTurn() {
        // TODO: change the current player by updating the queue
        // change the view to result in showing the new player's resources
    }
}
