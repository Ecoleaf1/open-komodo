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

abstract public class PropShop 
{
	
	public static final String title =  ChatColor.translateAlternateColorCodes('&', "&6&lProps");
	
	public static void open(Player player)
	{
		int slot = 0;
		Inventory gui = Bukkit.getServer().createInventory(null, 45, title);
		
		gui.setItem(slot++,CustomItem.getCustomItem(2).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(3).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(4).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(5).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(6).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(7).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(8).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(9).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(10).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(11).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(12).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(13).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(14).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(15).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(16).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(17).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(18).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(19).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(20).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(21).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(22).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(23).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(24).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(25).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(26).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(27).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(35).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(41).getNBTItem().getItem());
		
		gui.setItem(slot++, CustomItem.getCustomItem(999).getNBTItem().getItem());
		
		/*
		gui.setItem(slot++,CustomItem.getCustomItem(31).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(32).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(9).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(4).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(12).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(7).getNBTItem().getItem()); // Handcuffs
		gui.setItem(slot++,CustomItem.getCustomItem(23).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(11).getNBTItem().getItem()); // Gold Staff
		gui.setItem(slot++,CustomItem.getCustomItem(22).getNBTItem().getItem()); // Saber
		gui.setItem(slot++,CustomItem.getCustomItem(24).getNBTItem().getItem()); // Silver Staff
		gui.setItem(slot++,CustomItem.getCustomItem(8).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(20).getNBTItem().getItem()); // Racquet
		gui.setItem(slot++,CustomItem.getCustomItem(30).getNBTItem().getItem()); // Walky Talky
		gui.setItem(slot++,CustomItem.getCustomItem(36).getNBTItem().getItem()); // avocado
		gui.setItem(slot++,CustomItem.getCustomItem(25).getNBTItem().getItem()); // School rucksack
		gui.setItem(slot++,CustomItem.getCustomItem(35).getNBTItem().getItem()); // White Wool Bag
		gui.setItem(slot++,CustomItem.getCustomItem(34).getNBTItem().getItem()); // Pink Wool Bag
		gui.setItem(slot++,CustomItem.getCustomItem(33).getNBTItem().getItem()); // Black White Bag
		gui.setItem(slot++,CustomItem.getCustomItem(15).getNBTItem().getItem()); // Plush Bear
		gui.setItem(slot++,CustomItem.getCustomItem(16).getNBTItem().getItem()); // Plush Rabbit
		gui.setItem(slot++,CustomItem.getCustomItem(3).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(38).getNBTItem().getItem()); // Sushi
		gui.setItem(slot++,CustomItem.getCustomItem(5).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(6).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(13).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(37).getNBTItem().getItem()); // Lunch Box
		gui.setItem(slot++,CustomItem.getCustomItem(17).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(18).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(26).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(27).getNBTItem().getItem());
		gui.setItem(slot++,CustomItem.getCustomItem(92).getNBTItem().getItem());*/
		
		player.openInventory(gui);
	}
	
	/*
	private static void putItem(Inventory inv, Material material, int amount, short durability,int slot) {
		ItemStack item = new ItemStack(material,amount, durability);
		ItemMeta itemMeta = item.getItemMeta();
		CustomItem customItem = CustomItem.getItem(item.getType(), item.getDurability());
		
		if (customItem == null) {
			PrintConsole.print(item.getType() + " " + item.getDurability() + ": Is not found.");
		} else {
			String name = customItem.getName();
			itemMeta.setDisplayName(name);
		
			inv.setItem(slot,item);
		}
	}
	
	private static void putItem(Inventory inv, Material material, int amount, String name,int slot) {
		ItemStack item = new ItemStack(material,amount);
		
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		item.setItemMeta(itemMeta);
		
			inv.setItem(slot,item);
	}
	
	private static void putItem(Inventory inv, Material material, int amount, short durability) {
		ItemStack item = new ItemStack(material,amount, durability); 
		ItemMeta itemMeta = item.getItemMeta();
		
		CustomItem customItem = CustomItem.getItem(item.getType(), item.getDurability());
		
		if(customItem == null) {
			PrintConsole.print(item.getType() + " " + item.getDurability() + ": Is not found.");
			return;
		}
		
		int pointsPrice = customItem.getPointCost();
		
		if (pointsPrice < 0)
			return;
		
		String name = customItem.getName();
		itemMeta.setDisplayName(name);
		
		if (pointsPrice > 0) {
			List<String> lore = new ArrayList<String>();
			lore.add(String.format("Cost: %d points", pointsPrice));
			
			itemMeta.setLore(lore);
		}
		
		item.setItemMeta(itemMeta);
		
		inv.addItem(item);
	}
	
	public static Inventory create() {
		Inventory gui = Bukkit.getServer().createInventory(null, 45, "Props");
		putItem(gui, Material.IRON_HOE, 1, (short)1);
		putItem(gui, Material.IRON_HOE, 1, (short)2);
		putItem(gui, Material.IRON_HOE, 1, (short)3);
		putItem(gui, Material.IRON_HOE, 1, (short)5);
		putItem(gui, Material.IRON_HOE, 1, (short)6);
		putItem(gui, Material.IRON_HOE, 1, (short)7);
		putItem(gui, Material.IRON_HOE, 1, (short)8);
		putItem(gui, Material.IRON_HOE, 1, (short)9);
		putItem(gui, Material.IRON_HOE, 1, (short)10);
		putItem(gui, Material.IRON_HOE, 1, (short)12);
		putItem(gui, Material.IRON_HOE, 1, (short)11);
		putItem(gui, Material.IRON_HOE, 1, (short)13);
		putItem(gui, Material.IRON_HOE, 1, (short)14);
		putItem(gui, Material.IRON_HOE, 1, (short)18);
		putItem(gui, Material.IRON_HOE, 1, (short)16);
		putItem(gui, Material.IRON_HOE, 1, (short)17);
		putItem(gui, Material.IRON_HOE, 1, (short)15);
		putItem(gui, Material.IRON_HOE, 1, (short)19);
		putItem(gui, Material.IRON_HOE, 1, (short)20);
		putItem(gui, Material.IRON_HOE, 1, (short)21);
		putItem(gui, Material.IRON_HOE, 1, (short)22);
		putItem(gui, Material.IRON_HOE, 1, (short)27);
		putItem(gui, Material.IRON_HOE, 1, (short)28);
		putItem(gui, Material.IRON_HOE, 1, (short)29);
		putItem(gui, Material.IRON_HOE, 1, (short)32);
		putItem(gui, Material.IRON_HOE, 1, (short)33);
		putItem(gui, Material.IRON_HOE, 1, (short)34);
		putItem(gui, Material.IRON_HOE, 1, (short)35);
		putItem(gui, Material.IRON_HOE, 1, (short)36);
		putItem(gui, Material.IRON_HOE, 1, (short)37);
		putItem(gui, Material.IRON_HOE, 1, (short)38);
		
		return gui;
	} */
	
}
