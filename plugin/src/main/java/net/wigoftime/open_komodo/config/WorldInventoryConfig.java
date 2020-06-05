package net.wigoftime.open_komodo.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.InventoryManagement;
import net.wigoftime.open_komodo.etc.PrintConsole;

public abstract class WorldInventoryConfig 
{
	private static final File worldInventoryFolder = new File(Main.dataFolderPath+"/Inventories/Worlds");
	private static final File bagInventoryFolder = new File(Main.dataFolderPath+"/Inventories/Bags");
	
	public static ItemStack[] getInventory(Player player, World world)
	{
		if (!worldInventoryFolder.exists())
			worldInventoryFolder.mkdir();
		
		File worldFolder = new File(worldInventoryFolder.getAbsolutePath()+"/"+world.getName());
		
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
		if (!worldInventoryFolder.exists())
			worldInventoryFolder.mkdir();
		
		File worldFolder = new File(worldInventoryFolder.getAbsolutePath()+"/"+world.getName());
		
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
	
	public static int getInventoryIndex(Player player)
	{
		if (!bagInventoryFolder.exists())
			bagInventoryFolder.mkdir();
		
		File playerFolder = new File(bagInventoryFolder.getAbsolutePath()+"/"+player.getWorld().getName()+"/"+player.getUniqueId());
		
		if (!playerFolder.exists())
			playerFolder.mkdir();
		
		
		String[] list = playerFolder.list();
		
		int index;
		if (list.length < 1)
			index = 1;
		else
			index = Integer.parseInt(list[list.length-1].replace(".yml", ""));
		
		return index;
	}
	
	public static void createBagInventory(Player player)
	{
		setInventory(player, getInventoryIndex(player), new ItemStack[0]);
	}
	
	public static void setInventory(Player player, int bagID, ItemStack[] inventory)
	{
		if (!bagInventoryFolder.exists())
			bagInventoryFolder.mkdir();
		
		File playerFolder = new File(bagInventoryFolder.getAbsolutePath()+"/"+player.getWorld().getName()+"/"+player.getUniqueId());
		
		if (!playerFolder.exists())
			playerFolder.mkdir();
		
		File file = new File(playerFolder.getAbsolutePath()+"/"+bagID+".yml");
		
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
	
	public static ItemStack[] getInventory(Player player, int bagID)
	{
		if (!bagInventoryFolder.exists())
			bagInventoryFolder.mkdir();
		
		File playerFolder = new File(bagInventoryFolder.getAbsolutePath()+"/"+player.getWorld().getName()+"/"+player.getUniqueId());
		
		if (!playerFolder.exists())
			playerFolder.mkdir();
		
		File file = new File(playerFolder.getAbsolutePath()+"/"+bagID+".yml");
		
		if (!file.exists())
			return null;
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		List<ItemStack> list = (List<ItemStack>) config.getList("Inventory");
		
		return list.toArray(new ItemStack[list.size()]);
	}
	
	/*
	public static void save(Player player)
	{
		if (currentOpen.containsKey(player.getUniqueId()))
		{
			int bagID = currentOpen.get(player.getUniqueId());
			
			ItemStack[] items = player.getOpenInventory().getTopInventory().getContents();
			setInventory(player, bagID, items);
		}
		
		InventoryManagement.saveInventory(player, player.getWorld());
	}*/
}
