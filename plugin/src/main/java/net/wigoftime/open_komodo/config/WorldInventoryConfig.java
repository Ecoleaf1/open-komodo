package net.wigoftime.open_komodo.config;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.InventoryManagement;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class WorldInventoryConfig 
{
	private static final File worldInventoryFolder = new File(Main.dataFolderPath+"/Inventories/Worlds");
	private static final File bagInventoryFolder = new File(Main.dataFolderPath+"/Inventories/Bags");
	
	
	public static ItemStack @NotNull [] getInventory(@NotNull Player player, @NotNull World world)
	{
		if (!worldInventoryFolder.exists())
			worldInventoryFolder.mkdir();
		
		File worldFolder = new File(worldInventoryFolder.getAbsolutePath()+"/"+world.getName());
		
		if (!worldFolder.exists())
			worldFolder.mkdir();
		
		File file = new File(worldFolder.getAbsolutePath()+"/"+player.getUniqueId());
		
		if (!file.exists())
		{
			CustomPlayer customPlayer = CustomPlayer.get(player.getUniqueId());
			InventoryManagement.saveInventory(customPlayer, world);
		}
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		List<ItemStack> list = (List<ItemStack>) config.getList("Inventory");
		
		return list.toArray(new ItemStack[list.size()]);
	}
	
	
	public static void setInventory(@NotNull Player player, @NotNull World world, ItemStack[] inventory)
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
	
	public static short getInventoryIndex(@NotNull Player player)
	{
		if (!bagInventoryFolder.exists())
			bagInventoryFolder.mkdir();
		
		File worldFolder = new File(bagInventoryFolder.getAbsolutePath()+"/"+player.getWorld().getName());
		
		if (!worldFolder.exists())
			worldFolder.mkdir();
		
		File playerFolder = new File(worldFolder.getAbsolutePath()+"/"+player.getUniqueId());
			
		if (!playerFolder.exists())
			playerFolder.mkdir();
		
		
		String[] list = playerFolder.list();
	//	Integer.parseInt(list[list.length-1].replace(".yml", ""));
		
		short index;
		if (list.length < 1)
			index = 0;
		else
		{
			
			short max = 0;
			for (String s : list)
			{
				char[] numChar = new char[s.length()-4];
				s.getChars(0, (s.length()-4), numChar, 0);
				
				StringBuilder sb = new StringBuilder();
				for (char c : numChar)
				{
					sb.append(c);
				}
				
				short number = Short.parseShort(sb.toString());
				
				if (number > max)
					max = number;
			}
			
			index = max;
			/*
			char[] numbers = new char[list[0].length()-4];
			PrintConsole.test("Numbers length: " + numbers.length);
			
			list[0].getChars(0, (list[0].length()-4), numbers, 0);
			
			StringBuilder sb = new StringBuilder();
			for (char c : numbers)
			{
				sb.append(c);
				PrintConsole.test(c+"");
			}
			
			index = Integer.parseInt(sb.toString());*/
		}
		
		PrintConsole.test("index: "+index);
		PrintConsole.test("List Length WorldInventory: "+list.length);
		return index;
	}
	
	
	public static int createBagInventory(@NotNull Player player)
	{
		int bagID = getInventoryIndex(player) + 1;
		
		setInventory(player, bagID, new ItemStack[0]);
		
		return bagID;
	}
	
	public static void setInventory(@NotNull Player player, int bagID, ItemStack[] inventory)
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
	
	public static ItemStack @Nullable [] getInventory(@NotNull Player player, int bagID)
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
