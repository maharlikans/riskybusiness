package com.slothproductions.riskybusiness.model.GameStates;

import com.slothproductions.riskybusiness.model.Edge;
import com.slothproductions.riskybusiness.model.Player;
import com.slothproductions.riskybusiness.model.Vertex;

/**
 * Created by Kyle Maharlika on 4/4/2014.
 */
public interface GameState {
    void init();
    void startTurn();
    void diceRoll(int rollResult);
    void buildRoad(Edge edge);
    void buildSettlement(Vertex vertex);
    void buildCity(Vertex vertex);
    void buildMilitaryUnit(Vertex vertex);
    void trade(); // TODO change the parameters for trade
    void moveSoldier(Vertex vertexFrom, Vertex vertexTo);
    void attackWithSoldier(Vertex vertex);
    void endTurn();
    Player getCurrentPlayer();
}
