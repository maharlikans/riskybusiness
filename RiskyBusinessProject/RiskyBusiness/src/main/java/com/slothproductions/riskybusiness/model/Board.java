package com.slothproductions.riskybusiness.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Board {
	protected Set<Hexagon> hexagons;
	protected ArrayList<Player> players;
	protected HashMap<DiceRoll,Resource> rolls_resources;
	protected HashMap<Player,Resource> players_resources;
	protected Calendar start_timestamp;
	protected Iterator<Player> current_player;

}
