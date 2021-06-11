package net.wigoftime.open_komodo.etc.connectfour;

import net.md_5.bungee.api.ChatColor;
import net.wigoftime.open_komodo.etc.connectfour.ConnectFourSession.boardPlayerEnum;
import net.wigoftime.open_komodo.gui.CustomGUI;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConnectFourGUI extends CustomGUI {
	private final @NotNull ConnectFourSession activeSession;
	
	private final @NotNull ItemStack playerTokenItem;
	private final @NotNull ItemStack opponentTokenItem;
	
	private final @NotNull ItemStack playerOptionItem;
	private final @NotNull ItemStack opponentOptionItem;
	
	public ConnectFourGUI(@NotNull CustomPlayer openerCustomPlayer, @NotNull CustomPlayer opponent, @NotNull ConnectFourSession activeSession, @NotNull ConnectFourSession.boardPlayerEnum boardPlayerType) {
		super(openerCustomPlayer, null, Bukkit.createInventory(null, 54, "Connect Four"));
		this.activeSession = activeSession;
		
		if (boardPlayerType == boardPlayerEnum.PLAYER1) {
			// Setup token items
			playerTokenItem = new ItemStack(Material.RED_WOOL);
			ItemMeta playerTokenMeta = playerTokenItem.getItemMeta();
			playerTokenMeta.setDisplayName(String.format("%s%sYour piece Piece", ChatColor.RED, ChatColor.BOLD));
			playerTokenItem.setItemMeta(playerTokenMeta);
			
			opponentTokenItem = new ItemStack(Material.BLUE_WOOL);
			ItemMeta opponentTokenMeta = opponentTokenItem.getItemMeta();
			opponentTokenMeta.setDisplayName(String.format("%s%s%s's Piece", ChatColor.DARK_BLUE, ChatColor.BOLD, opponent.getPlayer().getDisplayName()));
			opponentTokenItem.setItemMeta(opponentTokenMeta);
			
			// Setup Player option buttons
			
			playerOptionItem = new ItemStack(Material.RED_STAINED_GLASS_PANE);
			ItemMeta playerOptionMeta = playerOptionItem.getItemMeta();
			playerOptionMeta.setDisplayName(String.format("%s%sYour turn", ChatColor.RED, ChatColor.BOLD));
			playerOptionItem.setItemMeta(playerOptionMeta);
			
			opponentOptionItem = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
			ItemMeta opponentOptionMeta = playerOptionItem.getItemMeta();
			opponentOptionMeta.setDisplayName(String.format("%s%s%s's turn", ChatColor.DARK_BLUE, ChatColor.BOLD, opponent.getPlayer().getDisplayName()));
			opponentOptionItem.setItemMeta(opponentOptionMeta);
			} else {
				// Setup token items
				playerTokenItem = new ItemStack(Material.BLUE_WOOL);
				ItemMeta playerTokenMeta = playerTokenItem.getItemMeta();
				playerTokenMeta.setDisplayName(String.format("%s%sYour piece Piece", ChatColor.DARK_BLUE, ChatColor.BOLD));
				playerTokenItem.setItemMeta(playerTokenMeta);
				
				opponentTokenItem = new ItemStack(Material.RED_WOOL);
				ItemMeta opponentTokenMeta = opponentTokenItem.getItemMeta();
				opponentTokenMeta.setDisplayName(String.format("%s%s%s's Piece", ChatColor.RED, ChatColor.BOLD, opponent.getPlayer().getDisplayName()));
				opponentTokenItem.setItemMeta(opponentTokenMeta);
				
				// Setup Player option buttons
				
				playerOptionItem = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
				ItemMeta playerOptionMeta = playerOptionItem.getItemMeta();
				playerOptionMeta.setDisplayName(String.format("%s%sYour turn", ChatColor.DARK_BLUE, ChatColor.BOLD));
				playerOptionItem.setItemMeta(playerOptionMeta);
				
				opponentOptionItem = new ItemStack(Material.RED_STAINED_GLASS_PANE);
				ItemMeta opponentOptionMeta = opponentOptionItem.getItemMeta();
				opponentOptionMeta.setDisplayName(String.format("%s%s%s's turn", ChatColor.RED, ChatColor.BOLD, opponent.getPlayer().getDisplayName()));
				opponentOptionItem.setItemMeta(opponentOptionMeta);
		}
		
		if (boardPlayerType == ConnectFourSession.boardPlayerEnum.PLAYER1) refreshOptions(PlayerType.PLAYER, null);
		else refreshOptions(PlayerType.OPPONENT, null);
	}
	
	public void clicked(@NotNull InventoryClickEvent clickEvent) {
		clickEvent.setCancelled(true);
		if (opener != activeSession.currentPlayer) return;
		
		byte tokenX = (byte) clickEvent.getCurrentItem().getItemMeta().getCustomModelData();
		activeSession.dropToken(opener, tokenX);
		
	}
	
	public enum PlayerType {PLAYER, OPPONENT};
	public void dropToken(@NotNull byte slotPosition, @Nullable PlayerType playerType) {
		if (playerType == null) {
			gui.setItem(slotPosition, new ItemStack(Material.AIR));
			return;
		}
		
		switch (playerType) {
		case PLAYER:
			gui.setItem(slotPosition, playerTokenItem);
			break;
		case OPPONENT:
			gui.setItem(slotPosition, opponentTokenItem);
			break;
		}
	}
	
	public void refreshOptions(@NotNull PlayerType playerType, @Nullable CustomPlayer winnerCustomPlayer) {
		ItemStack optionButton;
		
		if (winnerCustomPlayer != null) {
			optionButton = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
			ItemMeta meta = optionButton.getItemMeta();
			meta.setDisplayName(String.format("%s%s won!", ChatColor.YELLOW, winnerCustomPlayer.getPlayer().getDisplayName()));
			optionButton.setItemMeta(meta);
		} else if (playerType == PlayerType.PLAYER) {
			optionButton = playerOptionItem;
		} else {
			optionButton = opponentOptionItem;
		}
		
		for (int i = 0; i < 9; i++) {
			ItemStack tokenItem = optionButton;
			ItemMeta meta = tokenItem.getItemMeta();
			meta.setCustomModelData(i);
			tokenItem.setItemMeta(meta);
			
			gui.setItem(i, tokenItem);
			}
	}
	
	public void displayWinPath(@NotNull List<Integer> slotPositions) {
		for (int SlotIndex: slotPositions)
			gui.setItem(SlotIndex, new ItemStack(Material.WHITE_WOOL));
	}
	
	public void closed() {
		super.closed();
		
		// If match is over
		if (activeSession.currentPlayer == null) return;
		
		activeSession.cancelMatch(opener);
	}
	
	public void onlyClose() {
		super.closed();
		opener.getPlayer().closeInventory();
	}
}
