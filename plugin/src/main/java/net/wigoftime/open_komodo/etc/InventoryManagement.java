package net.wigoftime.open_komodo.etc;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.wigoftime.open_komodo.config.WorldInventoryConfig;

abstract public class InventoryManagement 
{
	public static Map<UUID, Integer> currentOpen = new HashMap<UUID, Integer>();
	
	
	// Save inventory under a world uuid.
	public static void saveInventory(Player player,  World world)
	{
		// Get Player's Items
		Inventory inventory = player.getInventory();
		
		PrintConsole.test("1");
		if (currentOpen.containsKey(player.getUniqueId()))
		{
			PrintConsole.test("2");
			int bagID = currentOpen.get(player.getUniqueId());
			
			ItemStack[] items = player.getOpenInventory().getTopInventory().getContents();
			WorldInventoryConfig.setInventory(player, bagID, items);
			currentOpen.remove(player.getUniqueId());
		}
		
		// Get items from Player's inventory
		ItemStack[] items = inventory.getContents();
		
		// Save inventory
		WorldInventoryConfig.setInventory(player, world, items);
	}
	
	// Load a world inventory
	public static void loadInventory(Player player, World world)
	{	
		// Get Player's Items
		Inventory inventory = player.getInventory();
		
		// Clear current inventory for next inventory
		inventory.clear();
		
		// Get all items
		ItemStack[] items = WorldInventoryConfig.getInventory(player, world);
		
		// Load inventory on player
		inventory.setContents(items);
	}
	
	public static void openBagInventory(Player player, int bagID)
	{	
		
		// Get all items
		ItemStack[] items = WorldInventoryConfig.getInventory(player, bagID);
		
		if (items == null)
		{
			PrintConsole.test("Doesn't exist.");
			WorldInventoryConfig.setInventory(player, bagID, new ItemStack[0]);
			items = WorldInventoryConfig.getInventory(player, bagID);
		}
		
		Inventory gui = Bukkit.createInventory(null, 27, "Backpack");
		
		for (int i = 0; i < gui.getSize(); i++)
		{
			if (i >= items.length)
				break;
			
			gui.setItem(i, items[i]);
		}
		
		InventoryManagement.currentOpen.put(player.getUniqueId(), bagID);
		player.openInventory(gui);
	}
}
