package net.wigoftime.open_komodo.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.wigoftime.open_komodo.objects.CustomItem;

public abstract class BuyOptionsGUI {

	private static final String coinsLabel = ChatColor.translateAlternateColorCodes('&', "&c&lCoins");
	private static final String pointsLabel = ChatColor.translateAlternateColorCodes('&', "&b&lPoints");
	
	private static ItemStack coinStack;
	private static ItemStack pointsStack;
	
	public static void create(Player player, CustomItem customItem) {
		
		Inventory gui = Bukkit.createInventory(null, 27);
			gui.setItem(10, coinStack);
			gui.setItem(16, pointsStack);
		
			gui.setItem(13, customItem.getNBTItem().getItem());
			
		player.openInventory(gui);
	}
	
	public static final void setup() {
		coinStack = new ItemStack(Material.GOLD_NUGGET); {
			ItemMeta meta = coinStack.getItemMeta();
			meta.setDisplayName(coinsLabel);
			coinStack.setItemMeta(meta);
		}
		
		pointsStack = new ItemStack(Material.IRON_INGOT); {
			ItemMeta meta = coinStack.getItemMeta();
			meta.setDisplayName(pointsLabel);
			pointsStack.setItemMeta(meta);
		}
	}
	
}
