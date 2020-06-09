package net.wigoftime.open_komodo.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.wigoftime.open_komodo.objects.CustomPlayer;

public abstract class BinGUI 
{
	public static final String title = ChatColor.translateAlternateColorCodes('&', "&7&lBin");
	
	public static void open(CustomPlayer player)
	{
		if (player.isBuilding())
		{
			player.getPlayer().sendMessage(CustomPlayer.buildingError);
			return;
		}
		
		Inventory gui = Bukkit.createInventory(null, 27,title);
		
		player.getPlayer().openInventory(gui);
	}
}
