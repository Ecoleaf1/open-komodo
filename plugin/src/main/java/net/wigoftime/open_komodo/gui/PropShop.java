package net.wigoftime.open_komodo.gui;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;

abstract public class PropShop 
{
	public static File shopPage = new File(Main.dataFolderPath+"/gui/propshop/default.yml");
	public static final String title =  ChatColor.translateAlternateColorCodes('&', "&6&lProps");
	public static List<Integer> idList;
	public static void setup()
	{
		YamlConfiguration config = YamlConfiguration.loadConfiguration(shopPage);
		idList = config.getIntegerList("ID");
	}
	
	public static void open(CustomPlayer player)
	{
		if (player.isBuilding())
		{
			player.getPlayer().sendMessage(CustomPlayer.buildingError);
			return;
		}
		int slot = 0;
		Inventory gui = Bukkit.getServer().createInventory(null, 45, title);
		
		Iterator<Integer> iterator = idList.listIterator();
		
		while (iterator.hasNext())
		{
			gui.setItem(slot++, CustomItem.getCustomItem(iterator.next()).getItem());
		}
		
		player.getPlayer().openInventory(gui);
	}
}
