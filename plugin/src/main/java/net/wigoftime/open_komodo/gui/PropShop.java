package net.wigoftime.open_komodo.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.wigoftime.open_komodo.config.WorldInventoryConfig;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;

abstract public class PropShop 
{
	
	public static final String title =  ChatColor.translateAlternateColorCodes('&', "&6&lProps");
	
	public static void open(CustomPlayer player)
	{
		if (player.isBuilding())
		{
			player.getPlayer().sendMessage(CustomPlayer.buildingError);
			return;
		}
		int slot = 0;
		Inventory gui = Bukkit.getServer().createInventory(null, 45, title);
		
		gui.setItem(slot++,CustomItem.getCustomItem(2).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(3).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(4).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(5).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(6).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(7).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(8).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(9).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(10).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(11).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(12).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(13).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(14).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(15).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(16).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(17).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(18).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(19).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(20).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(21).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(22).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(23).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(24).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(25).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(26).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(27).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(35).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(41).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(55).getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(57).getItem());
		
		gui.setItem(slot++, CustomItem.getCustomItem(999).getItem());
		
		player.getPlayer().openInventory(gui);
	}
}
