package net.wigoftime.open_komodo.gui;

/*
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import minecraftuuid.uuid3a401966852e4dd5b9e7d976f6cc7685.fprewritten.config.PlayerConfig;
import minecraftuuid.uuid3a401966852e4dd5b9e7d976f6cc7685.fprewritten.etc.PrintConsole;
import minecraftuuid.uuid3a401966852e4dd5b9e7d976f6cc7685.fprewritten.objects.CustomItem;
import net.md_5.bungee.api.ChatColor;
*/

abstract public class LegacyHatMenu {
	/*
	private static final String hatEquipMessage = "&lHat Equipped";
	private static final String hatDontHaveMsg = "&c&lSorry, but you don't have this hat.";
	
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
	
	private static void putItem(Player player,Inventory inv, Material material, int amount, short durability) {
		
		CustomItem customItem = CustomItem.getItem(material, durability);
		
		if(customItem == null) {
			PrintConsole.print(material + " " + durability + ": Is not found.");
			return;
		}
		
		ItemStack item = new ItemStack(material,amount, durability);  {
			boolean owned;
			
			ItemMeta itemMeta = item.getItemMeta();
			
			String name = customItem.getName();
			itemMeta.setDisplayName(name);
			
			File config = PlayerConfig.getPlayerConfig(player);
			if (config != null) {
			YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(config);
			
			owned = configYaml.getStringList("Items").contains(material+" "+durability);
			
			List<String> lore = new ArrayList<String>();
			
			if (owned) {
				lore.add("Unlocked");
			} else {
				lore.add("Locked");
				
				final int cost = customItem.getCoinCost();
				if (cost > -1)
					lore.add(String.format("Cost: %d coins", customItem.getCoinCost()));
			}
			
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);
			}
		}
		
		inv.addItem(item);
	}
	
	private static boolean hasItem(YamlConfiguration configYaml, String item) {
		return configYaml.getStringList("Items").contains(item);
	}
	
	protected static void open(Player player) {
		
		Inventory gui = Bukkit.getServer().createInventory(null, 36, "Hat-Menu");
		
		YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(PlayerConfig.getPlayerConfig(player));
		System.out.println(hasItem(configYaml, "STONE_HOE"));
		
		putItem(player, gui,Material.STONE_HOE,1, (short) 40); // Brown bear ears
		putItem(player, gui,Material.STONE_HOE,1, (short) 12); // Bowler Hat
		putItem(player,gui,Material.STONE_HOE,1, (short) 11); // Bunny Ears
		putItem(player,gui,Material.STONE_HOE,1, (short) 31); // Advent wreath
		putItem(player,gui,Material.STONE_HOE,1, (short) 6); // Blue cap
		putItem(player,gui,Material.STONE_HOE,1, (short) 4); // Brown cap
		putItem(player,gui,Material.STONE_HOE,1, (short) 7); // Golden cap
		putItem(player,gui,Material.STONE_HOE,1, (short) 8); // Gray cap
		putItem(player,gui,Material.STONE_HOE,1, (short) 10); // Brown cat ears
		
		putItem(player,gui,Material.STONE_HOE,1, (short) 41); // Orange cat ears
		putItem(player,gui,Material.STONE_HOE,1, (short) 9); // White cat ears
		putItem(player,gui,Material.STONE_HOE,1, (short) 37); // Chicken
		putItem(player,gui,Material.STONE_HOE,1, (short) 34); // Santa's hat
		putItem(player,gui,Material.STONE_HOE,1, (short) 13); // Cowboy Hat
		putItem(player,gui,Material.STONE_HOE,1, (short) 14); // Crown King
		putItem(player,gui,Material.STONE_HOE,1, (short) 17); // Crown Prince
		putItem(player,gui,Material.STONE_HOE,1, (short) 16); // Crown Princess
		putItem(player,gui,Material.STONE_HOE,1, (short) 15); // Crown Queen
		
		putItem(player,gui,Material.STONE_HOE,1, (short) 32); // Elf
		putItem(player,gui,Material.STONE_HOE,1, (short) 1); // Fedora
		putItem(player,gui,Material.STONE_HOE,1, (short) 2); // Fez
		putItem(player,gui,Material.STONE_HOE,1, (short) 18); // Firefighter helmet
		putItem(player,gui,Material.STONE_HOE,1, (short) 19); // Flower bow
		putItem(player,gui,Material.STONE_HOE,1, (short) 44); // FlowerCrown
		putItem(player,gui,Material.STONE_HOE,1, (short) 3); // Foreverplay Hat
		putItem(player,gui,Material.STONE_HOE,1, (short) 38); // Fried Egg
		putItem(player,gui,Material.STONE_HOE,1, (short) 5); // Golf Hat
		
		// The options
		
		putItem(gui,Material.ARROW,1,"Back", 30);
		putItem(gui,Material.PAPER,1, "Reset Hat", 31);
		putItem(gui,Material.ARROW,1,"Next", 32);
		
		player.openInventory(gui);
	}
	
	protected static void hatSelected(Player player, ItemStack item) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(PlayerConfig.getPlayerConfig(player));
		
		if (config.getStringList("Items").contains(String.format("%s %d", item.getType(),item.getDurability()))) {
			player.getInventory().setHelmet(item);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', hatEquipMessage));
		} else {
			CustomItem customItem = CustomItem.getItem(item.getType(), item.getDurability());
			
			if (customItem.getCoinCost() > -1) {
				BuyConfirm.create(player,customItem,"Coins");
				
				return;
			} else
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', hatDontHaveMsg));
		}
		
	player.closeInventory();	
	}
	
	protected static Inventory createPage2() {
		Inventory gui = Bukkit.getServer().createInventory(null, 36);
		
		return gui;
	} */
	
}
