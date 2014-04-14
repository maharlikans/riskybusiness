package com.slothproductions.riskybusiness.model.GameStates;

/**
 * Created by Kyle Maharlika on 4/4/2014.
 */
public interface GameState {

    // TODO: make the parameters reflect what the GameState actually needs
    // to successfully update the models
    void init();
    void startTurn();
    void rollDice();
    void buildRoad();
    void buildSettlement();
    void buildCity();
    void buildMilitaryUnit();
    void trade();
    void moveSoldier();
    void attackWithSoldier();
    void endTurn();
}
