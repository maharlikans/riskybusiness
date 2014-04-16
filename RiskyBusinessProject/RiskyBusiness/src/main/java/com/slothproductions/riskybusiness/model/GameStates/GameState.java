package com.slothproductions.riskybusiness.model.GameStates;

import com.slothproductions.riskybusiness.model.Edge;
import com.slothproductions.riskybusiness.model.Player;
import com.slothproductions.riskybusiness.model.Vertex;
import com.sun.javafx.geom.Edge;

/**
 * Created by Kyle Maharlika on 4/4/2014.
 */
public interface GameState {
    void init();
    void startTurn(int rollResult);
    void getValidMoves(Edge edge);
    void getValidMoves(Vertex v);
    void buildRoad(Edge edge);
    void buildSettlement(Vertex vertex);
    void buildCity(Vertex vertex);
    void buildMilitaryUnit(Vertex vertex);
    void trade(Player other); // TODO change the parameters for trade
    void moveSoldier(Vertex vertexFrom, Edge edgeAcross);
    void attackWithSoldier(Vertex vertex);
    void endTurn();
}
