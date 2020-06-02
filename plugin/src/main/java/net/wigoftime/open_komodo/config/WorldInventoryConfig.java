package net.wigoftime.open_komodo.config;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.InventoryManagement;
import net.wigoftime.open_komodo.etc.PrintConsole;

public abstract class WorldInventoryConfig 
{
	private static final File inventoryFolder = new File(Main.dataFolderPath+"/Inventories");
	
	public static ItemStack[] getInventory(Player player, World world)
	{
		if (!inventoryFolder.exists())
			inventoryFolder.mkdir();
		
		File worldFolder = new File(inventoryFolder.getAbsolutePath()+"/"+world.getName());
		
		if (!worldFolder.exists())
			worldFolder.mkdir();
		
		File file = new File(worldFolder.getAbsolutePath()+"/"+player.getUniqueId());
		
		if (!file.exists())
			InventoryManagement.saveInventory(player, world);
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		List<ItemStack> list = (List<ItemStack>) config.getList("Inventory");
		
		return list.toArray(new ItemStack[list.size()]);
	}
	
	public static void setInventory(Player player, World world, ItemStack[] inventory)
	{
		if (!inventoryFolder.exists())
			inventoryFolder.mkdir();
		
		File worldFolder = new File(inventoryFolder.getAbsolutePath()+"/"+world.getName());
		
		if (!worldFolder.exists())
			worldFolder.mkdir();
		
		File file = new File(worldFolder.getAbsolutePath()+"/"+player.getUniqueId());
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		PrintConsole.test("Generating..");
		config.set("Inventory", inventory);
		
		try
		{
			config.save(file);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return;
	}
}
