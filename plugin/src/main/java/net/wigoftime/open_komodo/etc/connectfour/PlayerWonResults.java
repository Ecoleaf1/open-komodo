package net.wigoftime.open_komodo.etc.connectfour;

import java.util.List;

public class PlayerWonResults {
	public final boolean hasWon;
	public final List<Integer> slotPositions;
	
	public PlayerWonResults(boolean hasWon, List<Integer> slotPositions) {
		this.hasWon = hasWon;
		this.slotPositions = slotPositions;
	}
}
