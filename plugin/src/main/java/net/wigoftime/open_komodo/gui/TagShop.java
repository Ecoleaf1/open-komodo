package net.wigoftime.open_komodo.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.config.TagConfig;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.ItemType;

public class TagShop {
	
	private static final int pageSlots = 35;
	
	public static void open(Player player) 
	{
		
		File plConfig = PlayerConfig.getPlayerConfig(player);
		
		YamlConfiguration plYaml = YamlConfiguration.loadConfiguration(plConfig);
		File tagConfig = TagConfig.get();
		YamlConfiguration tagYaml = YamlConfiguration.loadConfiguration(tagConfig);
		ConfigurationSection section = tagYaml.getConfigurationSection("Tags");
		
		
		
		int page;
		ItemStack idItem = player.getOpenInventory().getItem(44);
		if (idItem != null)
			if (idItem.getItemMeta().hasCustomModelData())
				page = idItem.getItemMeta().getCustomModelData();
			else
				page = 1;
		else
			page = 1;
		
		int tagIndex = pageSlots * (page - 1);
		
		Inventory gui = Bukkit.createInventory(null, 54,"Tag Shop");
		ItemStack itemStack = new ItemStack(Material.NAME_TAG);
		
		List<CustomItem> showTags;
		List<CustomItem> ownedTags = PlayerConfig.getItem(player, ItemType.TAG);
		
		List<CustomItem> shopTags = CustomItem.getCustomItem(ItemType.TAG);
		
		// Get amount of tags that exist
		int amount = shopTags.size();
		
		// Set size for array with the amount variable
		showTags = new ArrayList<CustomItem>(amount);
		
		
		for (CustomItem cs : shopTags)
		{	
			if (cs.getPointPrice() < 0)
				continue;
			
			boolean alreadyOwned = false;
			for (CustomItem cs2 : ownedTags)
			{
				if (cs2.getItem().getItemMeta().getCustomModelData() == cs.getItem().getItemMeta().getCustomModelData())
				{
					alreadyOwned = true;
					break;
				}
			}
			
			if (alreadyOwned)
				continue;
			
			showTags.add(cs);
		}
		
		boolean hasNext = showTags.size() > (page * pageSlots) ? true : false;
		if (hasNext) 
		{
			ItemStack nextItem = new ItemStack(Material.ARROW); 
			{
				ItemMeta itemMeta = nextItem.getItemMeta();
				itemMeta.setDisplayName("Next");
				
				nextItem.setItemMeta(itemMeta);
			}
			
			gui.setItem(53, nextItem);
		}
		
		// If can go back a previous page
		if (page > 1) {
			ItemStack backItem = new ItemStack(Material.ARROW); 
			{
				ItemMeta itemMeta = backItem.getItemMeta();
				itemMeta.setDisplayName("Back");
				
				backItem.setItemMeta(itemMeta);
			}
			
			gui.setItem(45, backItem);
		}
		
		int slotIndex = 0;
		for (CustomItem ci : showTags)
		{
			// Get Prices
			int p;
			
			// Make the item description
			List<String> lore = new ArrayList<String>();
			if (ci.getPointPrice() > -1 && ci.getCoinPrice() > -1)
				lore.add(String.format("Cost: %d %s or %d %s", ci.getPointPrice(), "Points", ci.getCoinPrice(), "Coins"));
			else
			{
				if (ci.getPointPrice() > -1)
					p = ci.getPointPrice();
				else
					p = ci.getCoinPrice();
				
				lore.add(String.format("Cost: %d %s", p, ci.getPointPrice() > -1 ? "Points" : "Coins"));
			}
			
			// Info about Item
			ItemStack is = ci.getItem();
			ItemMeta im = is.getItemMeta();
			
			// Save the item description
			im.setLore(lore);
			
			// Save changes to Item Meta
			is.setItemMeta(im);
			
			gui.setItem(slotIndex++, is);
		}
		
		ItemStack border = new ItemStack(Material.WHITE_STAINED_GLASS_PANE,1); {
			ItemMeta itemMeta = border.getItemMeta();
			itemMeta.setDisplayName(" ");
			
			border.setItemMeta(itemMeta);
		}
		
		// Adding Border Panels
		gui.setItem(44, border);
		gui.setItem(43, border);
		gui.setItem(42, border);
		gui.setItem(41, border);
		gui.setItem(40, border);
		gui.setItem(39, border);
		gui.setItem(38, border);
		gui.setItem(37, border);
		gui.setItem(36, border);
		
		player.openInventory(gui);
	}
	
}
