package net.wigoftime.open_komodo.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import net.md_5.bungee.api.ChatColor;
import net.wigoftime.open_komodo.etc.PrintConsole;


public class CustomItem 
{
	
	private static HashMap<Integer, CustomItem> items = new HashMap<Integer, CustomItem>();
	
	private final int id;
	private final List<String> description;
	private final boolean equipable;
	
	private final int pointPrice;
	private final int coinPrice;
	
	private final ItemStack item;
	private final ItemType type;
	
	private final Permission obtainPermission;
	
	public CustomItem(ItemStack item, int id, String name, List<String> description, boolean equipable, int pointPrice,Permission permission, ItemType type)
	{
		// Set ID
		this.id = id;
		
		// Set the CustomModelData to the id
		ItemMeta meta = item.getItemMeta();
		meta.setCustomModelData(id);
		
		if (name != null)
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		
		if (description != null)
			meta.setLore(description);
		
		item.setItemMeta(meta);
		
		this.equipable = equipable;
		
		// Assign all the CustomItem Variables
		this.description = description;
		
		this.pointPrice = pointPrice;
		this.coinPrice = -1;
		this.item = item;
		this.type = type;
		this.obtainPermission = permission;
		
		
		// Put it in a Hashmap, finding it with the ID
		items.put(id, this);
	}
	
	public static void create(ItemStack item, int id, int pointPrice, boolean equipable, Permission permission, ItemType type)
	{
		new CustomItem(item, id, null, null, equipable, pointPrice, permission, type);
	}
	
	public static void create(int id, int pointPrice,Permission permission, ItemType type)
	{
		ItemStack item = new ItemStack(Material.INK_SAC);
		new CustomItem(item, id, null, null, false, pointPrice, permission, type);
	}
	
	public static void create(int id, String name, List<String> description, int pointPrice, boolean equipable, Permission permission, ItemType type)
	{
		ItemStack item;
		
		if (type == ItemType.PROP || type == ItemType.HAT || type == ItemType.PHONE) {
			item = new ItemStack(Material.INK_SAC);
			ItemMeta meta = item.getItemMeta();
			if (name != null) {
				meta.setDisplayName(name);
			}
			
			meta.setCustomModelData(id);
			
			item.setItemMeta(meta);
		}  else if (type == ItemType.FURNITURE) {
			item = new ItemStack(Material.INK_SAC);
			ItemMeta meta = item.getItemMeta();
			if (name != null) {
				meta.setDisplayName(name);
			}
			
			item.setItemMeta(meta);
		}
		else if (type == ItemType.TAG)
			item = new ItemStack(Material.NAME_TAG);
		else
		{
			PrintConsole.print(String.format(ChatColor.RED+"ERROR: Item #%d couldn't be created, due to an ItemType not being specified.", id));
			return;
		}
		
		new CustomItem(item, id, name, description, equipable, pointPrice, permission, type);
	}
	
	public static List<CustomItem> getCustomItem(ItemType type)
	{
		// Create List that matches ItemType
		LinkedList<CustomItem> list = new LinkedList<CustomItem>();
		
		// Add item to list if has the requested ItemType
		for (Map.Entry<Integer, CustomItem> entry : items.entrySet())
		{
			CustomItem cs = entry.getValue();
			
			if (cs.getType() != type)
				continue;
			
			list.add(cs);
		}
		
		return list;
	}
	
	public static CustomItem getCustomItem(int id)
	{
		CustomItem cs = items.get(id);
		
		if (cs == null)
			return items.get(-99);
		
		return cs;
	}
	
	public static List<CustomItem> getItems()
	{
		List<CustomItem> itemList = new ArrayList<CustomItem>(items.size());
		for (Entry<Integer, CustomItem> e : items.entrySet())
		{
			itemList.add(e.getValue());
		}
		
		return itemList;
	}
	
	public static List<CustomItem> getItems(ItemType type)
	{
		List<CustomItem> itemList = new ArrayList<CustomItem>(items.size());
		for (Entry<Integer, CustomItem> e : items.entrySet())
		{
			if (e.getValue().getType() == type)
				itemList.add((CustomItem) e.getValue());
		}
		
		return itemList;
	}
	
	public int getID() {
		return id;
	}
	
	public List<String> getDescription()
	{
		return description;
	}
	
	public ItemStack getItem()
	{
		return item;
	}
	
	public int getPointPrice()
	{
		return pointPrice;
	}
	
	public int getCoinPrice()
	{
		return coinPrice;
	}
	
	public ItemType getType()
	{
		return type;
	}
	
	public boolean isEquipable()
	{
		return equipable;
	}
	
	public Permission getPermission()
	{
		return obtainPermission;
	}
}
