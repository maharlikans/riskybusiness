package com.slothproductions.riskybusiness.model.GameStates;

import android.widget.PopupMenu;

import com.slothproductions.riskybusiness.model.Edge;
import com.slothproductions.riskybusiness.model.GameLoop;
import com.slothproductions.riskybusiness.model.Player;
import com.slothproductions.riskybusiness.model.Vertex;

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

    }

    @Override
    public void startTurn() {

    }

    @Override
    public void diceRoll() {

    }

    @Override
    public boolean buildRoad(Edge edge) {
        return false;
    }

    @Override
    public boolean buildSettlement(Vertex vertex) {
        return false;
    }

    @Override
    public boolean buildCity(Vertex vertex) {
        return false;
    }

    @Override
    public boolean buildMilitaryUnit(Vertex vertex) {
        return false;
    }

    @Override
    public void trade() {

    }

    @Override
    public boolean moveMilitaryUnit(Vertex vertexFrom, Vertex vertexTo) {
        return false;
    }

    @Override
    public boolean attack(Vertex vertexFrom, Vertex vertexTo, Integer amount) {
        return false;
    }

    @Override
    public boolean repairCity(Vertex vertex) {
        return false;
    }

    @Override
    public boolean repairSettlement(Vertex vertex) {
        return false;
    }

    @Override
    public void endTurn() {

    }

    @Override
    public void getActions(PopupMenu popupMenu, Edge edge) {

    }

    @Override
    public void getActions(PopupMenu popupMenu, Vertex vertex) {

    }

    @Override
    public Player getCurrentPlayer() {
        return null;
    }
}
