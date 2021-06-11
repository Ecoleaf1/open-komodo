package net.wigoftime.open_komodo.etc.connectfour;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Board {
	public PLAYER_PIECE[] @NotNull [] board = new PLAYER_PIECE[9][5];
	
	public enum PLAYER_PIECE {PIECE_PLAYER1, PIECE_PLAYER2};
	public @Nullable DropTokenReturnObject dropToken(byte x, PLAYER_PIECE tokenMaterial) {
		byte top = 0;
		
		for (byte index = 0; index < 5 && board[x][index] != null; index++) {
			top++;
		}
		
		if (top > 4) return null;
		
		board[x][top] = tokenMaterial;
		
		PlayerWonResults results = getWonResults(x, top, tokenMaterial);
		
		if (results.hasWon) return new DropTokenReturnObject(CheckStatus.WIN, top, slotPositions);
		return new DropTokenReturnObject(CheckStatus.NEXT_TURN, top, null);
	}
	
	private @NotNull List<Integer> slotPositions = new ArrayList<Integer>(0);
	private @Nullable PlayerWonResults getWonResults(byte x, byte y, PLAYER_PIECE tokenItem) {
		if (checkTop(x, y, tokenItem)) return new PlayerWonResults(true, slotPositions);
		if (checkRight(x, y, tokenItem)) return new PlayerWonResults(true, slotPositions);
		if (checkVerticalLeft(x, y, tokenItem)) return new PlayerWonResults(true, slotPositions);
		if (checkVerticalRight(x, y, tokenItem)) return new PlayerWonResults(true, slotPositions);
		return new PlayerWonResults(false, null);
	}
	
	private boolean checkRight(byte x, byte y, PLAYER_PIECE tokenItem) {
		// Get start position of the pattern (Right to left)
		int startOfPattern = x;
		for (int indexX = x; indexX < 9; indexX++) {
			if (board[indexX][y] == null) break;
			if (board[indexX][y] != tokenItem) break;
			startOfPattern = indexX;
		}
		
		List<Integer> slotPositions2 = new ArrayList<Integer>(4);
		byte count = 0;
		for (int indexX = startOfPattern; indexX > -1; indexX--) {
			if (board[indexX][y] == null) break;
			if (board[indexX][y] != tokenItem) break;
			
			final int slotPosition;
			if (y == 0) slotPosition = 54 - (9 - indexX);
			else slotPosition = 54 - (9 * y - indexX) - 9;
			slotPositions2.add(slotPosition);
			
			count++;
		}
		
		if (count > 3) {
			slotPositions = slotPositions2;
			return true;
		}
		
		return false;
	}
	
	private boolean checkVerticalRight(byte x, byte y, PLAYER_PIECE tokenItem) {
		// Get start position of the pattern (Vertical Right to vertical down left)
		byte startX = x;
		byte startY = y;
		while (startX < 8 && startY < 4) {
			if (board[startX+1][startY+1] == null) break;
			if (board[startX+1][startY+1] != tokenItem) break;
			
			startX++;
			startY++;
		}
		
		List<Integer> slotPositions2 = new ArrayList<Integer>(4);
		byte count = 0;
		
		byte indexX = startX;
		byte indexY = startY;
		
		while (indexX > -1 && indexY > -1 && count < 4) {
			if (board[indexX][indexY] == null) break;
			if (board[indexX][indexY] != tokenItem) break;
			
			count++;
			System.out.println("vertical right count: " + count);
			
			final int slotPosition;
			if (indexY == 0) slotPosition = 54 - (9 - indexX);
			else slotPosition = 54 - (9 * indexY - indexX) - 9;
			slotPositions2.add(slotPosition);
			
			indexX--;
			indexY--;
		}
		
		if (count > 3) {
			slotPositions = slotPositions2;
			return true;
		}
		
		return count > 3 ? true : false;
	}
	
	private boolean checkVerticalLeft(byte x, byte y, PLAYER_PIECE tokenItem) {
		// Get start position of the pattern (Vertical Left to Vertical down left)
		byte startX = x;
		byte startY = y;
		while (startX > 0 && startY < 4) {
			if (board[startX-1][startY+1] == null) break;
			if (board[startX-1][startY+1] != tokenItem) break;
			
			startX--;
			startY++;
		}
		
		List<Integer> slotPositions2 = new ArrayList<Integer>(4);
		byte count = 0;
		
		byte indexX = startX;
		byte indexY = startY;
		
		while (indexX < 9 && indexY > -1 && count < 4) {
			if (board[indexX][indexY] == null) break;
			if (board[indexX][indexY] != tokenItem) break;
			
			count++;
			System.out.println("vertical left count: " + count);
			
			final int slotPosition;
			if (indexY == 0) slotPosition = 54 - (9 - indexX);
			else slotPosition = 54 - (9 * indexY - indexX) - 9;
			slotPositions2.add(slotPosition);
			
			indexX++;
			indexY--;
		}
		
		if (count > 3) {
			slotPositions = slotPositions2;
			return true;
		}
		
		return count > 3 ? true : false;
	}
	
	private boolean checkTop(byte x, byte y, PLAYER_PIECE tokenItem) {
		List<Integer> slotPositions2 = new ArrayList<Integer>(4);
		byte count = 0;
		
		// Get start position of the pattern (Get start of top)
		byte indexY = y;
		while (indexY > -1 && count < 4) {
			if (board[x][indexY] == null) break;
			if (board[x][indexY] != tokenItem) break;
			
			count++;
			System.out.println("vertical top to bottom count: " + count);
			
			final int slotPosition;
			if (indexY == 0) slotPosition = 54 - (9 - x);
			else slotPosition = 54 - (9 * indexY - x) - 9;
			slotPositions2.add(slotPosition);
			
			indexY--;
		}
		
		if (count > 3) {
			slotPositions = slotPositions2;
			return true;
		}
		
		return count > 3 ? true : false;
	}
}
