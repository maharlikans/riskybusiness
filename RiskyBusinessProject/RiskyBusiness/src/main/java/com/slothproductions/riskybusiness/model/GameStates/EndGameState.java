package com.slothproductions.riskybusiness.model.GameStates;

import com.slothproductions.riskybusiness.model.GameLoop;

/**
 * Created by Kyle Maharlika on 4/4/2014.
 */
public class EndGameState implements GameState {

    GameLoop mGameLoop;

    public EndGameState (GameLoop gameLoop) {
        mGameLoop = gameLoop;
    }

    @Override
    public void init() {
        // show the necessary view elements on the screen
    }

    @Override
    public void startTurn() {
        // DO NOTHING
    }

    @Override
    public void rollDice() {
        // DO NOTHING
    }

    @Override
    public void buildRoad() {
        // do nothing
    }

    @Override
    public void buildSettlement() {
        // do nothing
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
        // DO NOTHING
    }

    @Override
    public void moveSoldier() {
        // DO NOTHING
    }

    @Override
    public void attackWithSoldier() {
        // DO NOTHING
    }

    @Override
    public void endTurn() {
        // DO NOTHING
    }
}
