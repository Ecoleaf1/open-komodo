package net.wigoftime.open_komodo.etc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.config.WorldInventoryConfig;
import net.wigoftime.open_komodo.gui.BagGui;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.sql.SQLManager;

abstract public class InventoryManagement 
{
	public static Map<UUID, Integer> currentOpen = new HashMap<UUID, Integer>();
	
	public static void saveBagInventory(Player player, String worldName, List<ItemStack> inventory)
	{
		
		if (SQLManager.isEnabled())
		SQLManager.setBagInventory(player.getUniqueId(), worldName, currentOpen.get(player.getUniqueId()), inventory);
		else
		WorldInventoryConfig.setInventory(player, currentOpen.get(player.getUniqueId()), inventory.toArray(new ItemStack[inventory.size()]));
	}
	// Save inventory under a world uuid.
	public static void saveInventory(CustomPlayer player,  World world)
	{
		if (player.isBuilding())
			return;
		
		// Get Player's Items
		Inventory inventory = player.getPlayer().getInventory();
		
		// Get items from Player's inventory
		ItemStack[] items = inventory.getContents();
		
		// Save inventory
		if (SQLManager.isEnabled())
			SQLManager.setInventory(player.getUniqueId(), world.getName(), items);
		else
			WorldInventoryConfig.setInventory(player.getPlayer(), world, items);
		//WorldInventoryConfig.setInventory(player.getPlayer(), world, items);
	}
	
	// Load a world inventory
	public static void loadInventory(CustomPlayer player, World world)
	{
		if (player.isBuilding())
			return;
		
		// Get Player's Items
		Inventory inventory = player.getPlayer().getInventory();
		
		// Clear current inventory for next inventory
		inventory.clear();
		
		// Get all items
		ItemStack[] items;
		if (SQLManager.isEnabled())
			items = SQLManager.getInventory(player.getUniqueId(), world.getName());
		else
			items = WorldInventoryConfig.getInventory(player.getPlayer(), world);
		
		// Load inventory on player
		if (items != null)
			inventory.setContents(items);
	}
	
	public static void openBagInventory(CustomPlayer player, int bagID) {	
		Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
			public void run() {
				// Get all items
				List<ItemStack> items;
				if (SQLManager.isEnabled())
					items = SQLManager.getBagInventory(player.getUniqueId(), player.getPlayer().getWorld().getName(), bagID);
				else
					items = Arrays.asList(WorldInventoryConfig.getInventory(player.getPlayer(), bagID));
				
				if (items == null)
				{
					PrintConsole.test("Doesn't exist.");
					//SQLManager.setBagInventory(player.getUniqueId(), player.getPlayer().getWorld().getName(), bagID, new ArrayList<ItemStack>(0));
					//SQLManager.getBagInventory(player.getUniqueId(), bagID, new ItemStack[0]);
					items = new ArrayList<ItemStack>(0);
					//items = WorldInventoryConfig.getInventory(player, bagID);
				}
				
				final List<ItemStack> itemsFinal = items;
				Runnable mainThreadRunnable = new Runnable() {
					/*
					public void run() {
						Inventory gui = Bukkit.createInventory(null, 27, "Backpack");
						
						byte index = 0;
						for (ItemStack item : itemsFinal)
							gui.setItem(index++, item);
						
						InventoryManagement.currentOpen.put(player.getUniqueId(), bagID);
						player.openInventory(gui);
					}*/

					@Override
					public void run() {
						BagGui gui = new BagGui(player, bagID);
						gui.open();
					}
				};
				
				Bukkit.getScheduler().runTask(Main.getPlugin(), mainThreadRunnable);
			}
		});
	}
}
