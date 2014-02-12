public class Board {
	protected Set<Hexagon> hexgagons;
	protected ArrayList<Player> players;
	protected HashMap<DiceRoll,Resource> rolls_resources;
	protected HashMap<Player,Resource> players_resources;
	protected Calendar start_timestamp;
	protected Iterator<Player> current_player;

}
