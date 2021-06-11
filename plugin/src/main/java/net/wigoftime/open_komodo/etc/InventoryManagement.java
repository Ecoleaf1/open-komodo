package net.wigoftime.open_komodo.etc;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.config.WorldInventoryConfig;
import net.wigoftime.open_komodo.gui.BagGui;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

abstract public class InventoryManagement 
{
	public static @NotNull Map<UUID, Integer> currentOpen = new HashMap<UUID, Integer>();
	
	public static void saveBagInventory(@NotNull Player player, String worldName, @NotNull List<ItemStack> inventory) {
		
		if (SQLManager.isEnabled())
		SQLManager.setBagInventory(player.getUniqueId(), currentOpen.get(player.getUniqueId()), inventory);
		else
		WorldInventoryConfig.setInventory(player, currentOpen.get(player.getUniqueId()), inventory.toArray(new ItemStack[inventory.size()]));
	}
	// Save inventory under a world uuid.
	public static void saveInventory(@NotNull CustomPlayer player, @NotNull World world) {
		PrintConsole.test("Saving inventory..");
		
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
		
		PrintConsole.test("Saved Inventory");
	}
	
	// Load a world inventory
	public static void loadInventory(@NotNull CustomPlayer player, @NotNull World world) {
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
	
	public static void openBagInventory(@NotNull CustomPlayer player, int bagID) {
		Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
			public void run() {
				// Get all items
				List<ItemStack> items;
				if (SQLManager.isEnabled())
					items = SQLManager.getBagInventory(player.getUniqueId(), bagID);
				else
					items = Arrays.asList(WorldInventoryConfig.getInventory(player.getPlayer(), bagID));
				
				if (items == null) {
					PrintConsole.test("Doesn't exist.");
					items = new ArrayList<ItemStack>(0);
				}
				
				Runnable mainThreadRunnable = new Runnable() {
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
