package net.wigoftime.open_komodo.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public abstract class BinGUI 
{
	public static final String title = ChatColor.translateAlternateColorCodes('&', "&7&lBin");
	
	public static void open(Player player)
	{
		Inventory gui = Bukkit.createInventory(null, 27,title);
		
		player.openInventory(gui);
	}
}
