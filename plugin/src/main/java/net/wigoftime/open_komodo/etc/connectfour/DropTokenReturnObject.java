package net.wigoftime.open_komodo.etc.connectfour;

import java.util.List;

public class DropTokenReturnObject {
	public final CheckStatus status;
	public final byte topY;
	public final List<Integer> slotPositions;
	
	public DropTokenReturnObject(CheckStatus status, byte topY, List<Integer> slotPositions) {
		this.status = status;
		this.topY = topY;
		this.slotPositions = slotPositions;
	}
}
