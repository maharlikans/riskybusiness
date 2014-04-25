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
        // show the necessary view elements on the screen
    }

    @Override
    public void startTurn() {
        // DO NOTHING
    }

    @Override
    public void buildRoad(Edge edge) {
        // do nothing
    }

    @Override
    public void buildSettlement(Vertex vertex) {
        // do nothing
    }

    @Override
    public void buildCity(Vertex vertex) {
        // do nothing
    }

    @Override
    public void buildMilitaryUnit(Vertex vertex) {
        // do nothing
    }

    @Override
    public void trade() {
        // DO NOTHING
    }

    @Override
    public void attackWithSoldier(Vertex vertex) {
        // DO NOTHING
    }

    @Override
    public void endTurn() {
        // DO NOTHING
    }

    @Override
    public void diceRoll() {

    }

    @Override
    public void moveSoldier(Vertex vertexFrom, Vertex vertexTo) {

    }

    @Override
    public Player getCurrentPlayer() {
        return null;
    }

    @Override
    public void getActions(PopupMenu popupMenu, Edge edge) {
        // DO NOTHING
    }

    @Override
    public void getActions(PopupMenu popupMenu, Vertex vertex) {
        // DO NOTHING
    }
}
