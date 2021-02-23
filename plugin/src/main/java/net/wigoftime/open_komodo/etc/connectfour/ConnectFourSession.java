package net.wigoftime.open_komodo.etc.connectfour;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.etc.CurrencyClass;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.etc.connectfour.ConnectFourGUI.PlayerType;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class ConnectFourSession {
	public static Map<CustomPlayer, ConnectFourSession> activeSessions = new LinkedHashMap<CustomPlayer, ConnectFourSession>();
	private static Map<CustomPlayer, CustomPlayer> requests = new HashMap<CustomPlayer, CustomPlayer>();
	
	public enum boardPlayerEnum {PLAYER1, PLAYER2};
	private CustomPlayer[] playingPlayers = new CustomPlayer[2];
	private ConnectFourGUI[] activeGUIs = new ConnectFourGUI[2];
	
	public CustomPlayer currentPlayer;
	
	private final Board board;
	boolean isWaitingForAnimation = false;
	
	public ConnectFourSession(CustomPlayer player, CustomPlayer player2) {
		board = new Board();
		
		activeSessions.put(player, this);
		activeSessions.put(player2, this);
		
		playingPlayers[0] = player;
		playingPlayers[1] = player2;
		
		activeGUIs[0] = new ConnectFourGUI(player, player2, this, boardPlayerEnum.PLAYER1);
		activeGUIs[1] = new ConnectFourGUI(player2, player, this, boardPlayerEnum.PLAYER2);
		
		activeGUIs[0].open();
		activeGUIs[1].open();
		
		currentPlayer = player;
	}
	
	public void dropToken(CustomPlayer player, byte tokenX) {
		if (isWaitingForAnimation) return;
		
		DropTokenReturnObject results = board.dropToken(tokenX, currentPlayer == playingPlayers[0] ? Board.PLAYER_PIECE.PIECE_PLAYER1 : Board.PLAYER_PIECE.PIECE_PLAYER2);
		
		if (results == null) return;
		
		isWaitingForAnimation = true;
		
		BukkitRunnable runnable = getAnimationRunnable(tokenX, results, player);
		runnable.runTaskTimer(Main.getPlugin(), 0, 10);
		return;
	}
	
	private void win(DropTokenReturnObject results) {
		if (currentPlayer == playingPlayers[0]) {
			activeGUIs[0].displayWinPath(results.slotPositions);
			activeGUIs[1].displayWinPath(results.slotPositions);
			
			activeGUIs[0].refreshOptions(PlayerType.PLAYER, currentPlayer);
			activeGUIs[1].refreshOptions(PlayerType.OPPONENT, currentPlayer);
		} else {
			activeGUIs[0].displayWinPath(results.slotPositions);
			activeGUIs[1].displayWinPath(results.slotPositions);
			
			activeGUIs[0].refreshOptions(PlayerType.OPPONENT, currentPlayer);
			activeGUIs[1].refreshOptions(PlayerType.PLAYER, currentPlayer);
		}
		
		activeSessions.remove(playingPlayers[0]);
		activeSessions.remove(playingPlayers[1]);
		
		// Give winner XP and Points
		
		currentPlayer.setXP(currentPlayer.getXP() + 0.05);
		CurrencyClass.genPay(currentPlayer, 5, Currency.POINTS);
		
		// Give lost player XP and Points
		
		CustomPlayer loserPlayer;
		if (currentPlayer == playingPlayers[0]) loserPlayer = playingPlayers[1];
		else loserPlayer = playingPlayers[0];
		
		loserPlayer.setXP(loserPlayer.getXP() + 0.01);
		CurrencyClass.genPay(loserPlayer, 2, Currency.POINTS);
		
		currentPlayer.getPlayer().sendMessage(String.format("%s» %sYou won, and gained an additional 5 points!", ChatColor.GOLD, ChatColor.GRAY));
		loserPlayer.getPlayer().sendMessage(String.format("%s» %s%s won, but gained a smaller additional points of 2", ChatColor.GOLD, ChatColor.GRAY, currentPlayer.getPlayer().getDisplayName()));
		currentPlayer = null;
	}
	
	public void cancelMatch(CustomPlayer causer) {
		for (CustomPlayer playerIndex: playingPlayers) {
			if (causer == playerIndex) causer.getPlayer().sendMessage(String.format("%s» %sMatch cancelled", ChatColor.GOLD, ChatColor.GRAY));
			else if (causer != null) playerIndex.getPlayer().sendMessage(String.format("%s» %s%s cancelled match", ChatColor.GOLD, ChatColor.GRAY, causer.getPlayer().getDisplayName()));
				
			activeSessions.remove(playerIndex);
		}
		
		PrintConsole.test("About to close");
		close();
	}
	
	private void close() {
		activeGUIs[0].onlyClose();
		activeGUIs[1].onlyClose();
	}
	
	public static void requestToPlay(CustomPlayer requester, CustomPlayer recipient) {
		if (activeSessions.containsKey(recipient)) {
			requester.getPlayer().sendMessage(String.format("%sPlayer is already in a match", ChatColor.DARK_RED));
			return;
		}
		
		ComponentBuilder builder = new ComponentBuilder();
		builder.append("» ").color(ChatColor.GOLD).append(String.format("Incoming Connect Four match request from %s! Click here to accept!", requester.getPlayer().getDisplayName())).color(ChatColor.GRAY).event(new ClickEvent(Action.RUN_COMMAND, "/connect4 accept"));
		
		recipient.getPlayer().spigot().sendMessage(builder.create());
		requester.getPlayer().sendMessage(String.format("%s» %sRequested Connect Four match to %s", ChatColor.GOLD, ChatColor.GRAY, recipient.getPlayer().getDisplayName()));
		requests.put(recipient, requester);
	}
	
	public static void acceptRequest(CustomPlayer player) {
		CustomPlayer player2 = requests.get(player);
		
		if (player2 == null) {
			player.getPlayer().sendMessage(String.format("%sYou have no pending Connect4 requests", ChatColor.DARK_RED));
			return;
		}
		
		requests.remove(player);
		if (activeSessions.containsKey(player2)) {
			player.getPlayer().sendMessage(String.format("%sPlayer is already in a match", ChatColor.DARK_RED));
			return;
		}
		
		new ConnectFourSession(player, player2);
	}
	
	private static byte positionToSlotPosition(byte x, byte y) {
		if (y == 0) return (byte) (54 - (9 - x));
		else return (byte) (54 - (9 * y - x) - 9);
	}
	
	private BukkitRunnable getAnimationRunnable(byte tokenX, DropTokenReturnObject results, CustomPlayer player) {
		return new BukkitRunnable() {
			byte positionY = (byte) board.board[0].length;
			
			@Override
			public void run() {
				positionY--;
				
				if (positionY >= results.topY) {
					PrintConsole.test("1 pos: "+ positionY);
					byte newSlotID = positionToSlotPosition(tokenX, positionY);
					activeGUIs[0].dropToken(newSlotID, player == playingPlayers[0] ? ConnectFourGUI.PlayerType.PLAYER : ConnectFourGUI.PlayerType.OPPONENT);
					activeGUIs[1].dropToken(newSlotID, player == playingPlayers[0] ? ConnectFourGUI.PlayerType.OPPONENT : ConnectFourGUI.PlayerType.PLAYER);
				}
				
				if (positionY + 1 < (byte) board.board[0].length && positionY >= results.topY) {
					byte oldSlotID = positionToSlotPosition(tokenX, (byte) (positionY+1));
					activeGUIs[0].dropToken(oldSlotID, null);
					activeGUIs[1].dropToken(oldSlotID, null);
				}
				
				if (positionY >= results.topY) return;
				
				PrintConsole.test("2 pos: "+ positionY);
				
				if (results.status == CheckStatus.WIN) {
					win(results);
					isWaitingForAnimation = false;
					cancel();
					return;
				}
				
				// Refresh the player's option buttons
				
				if (player == playingPlayers[0]) {
					activeGUIs[0].refreshOptions(PlayerType.OPPONENT, null);
					activeGUIs[1].refreshOptions(PlayerType.PLAYER, null);
					
					currentPlayer = playingPlayers[1];
				}
				else {
					activeGUIs[0].refreshOptions(PlayerType.PLAYER, null);
					activeGUIs[1].refreshOptions(PlayerType.OPPONENT, null);
					
					currentPlayer = playingPlayers[0];
				}
				
				isWaitingForAnimation = false;
				cancel();
				
				return;
			}
		};
	}
}
