package net.wigoftime.open_komodo.gui;

import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;

public class BinGUI extends CustomGUI{
	static final String title = String.format("%sBin", ChatColor.GRAY);
	
	public BinGUI(CustomPlayer customPlayer) {
		super(customPlayer, null, Bukkit.createInventory(null, 27,title));
	}

	public void clicked(InventoryClickEvent clickEvent) {
		return;
	}
}
