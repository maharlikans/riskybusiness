package com.slothproductions.riskybusiness.model.GameStates;

import android.widget.PopupMenu;

import com.slothproductions.riskybusiness.model.Edge;
import com.slothproductions.riskybusiness.model.Player;
import com.slothproductions.riskybusiness.model.Vertex;

/**
 * Created by Kyle Maharlika on 4/4/2014.
 */
public interface GameState {
    void init();
    void startTurn();
    void diceRoll();
    boolean buildRoad(Edge edge);
    boolean buildSettlement(Vertex vertex);
    boolean buildCity(Vertex vertex);
    boolean buildMilitaryUnit(Vertex vertex);
    void trade(); // TODO change the parameters for trade
    boolean moveMilitaryUnit(Vertex vertexFrom, Vertex vertexTo);
    boolean attack(Vertex vertex);
    boolean repairCity(Vertex vertex);
    boolean repairSettlement(Vertex vertex);
    void endTurn();
    void getActions(PopupMenu popupMenu, Edge edge);
    void getActions(PopupMenu popupMenu, Vertex vertex);
    Player getCurrentPlayer();
}
