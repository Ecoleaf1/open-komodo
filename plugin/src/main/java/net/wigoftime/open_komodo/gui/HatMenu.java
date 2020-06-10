package net.wigoftime.open_komodo.gui;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.wigoftime.open_komodo.config.ItemConfig;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.ItemType;

abstract public class HatMenu 
{

	public static final String title = "Hat-menu";
	
	private static final String unlockedString = ChatColor.translateAlternateColorCodes('&', "&2&lUnlocked");
	
	private static final int pageCap = 27;
	public static void open(CustomPlayer player, byte page) 
	{	
		if (player.isBuilding())
		{
			player.getPlayer().sendMessage(CustomPlayer.buildingError);
			return;
		}
		
		Inventory gui = Bukkit.getServer().createInventory(null, 36, title);
		
		ItemStack resetButton;

		// Create Reset Hat Button
		resetButton = new ItemStack(Material.WHITE_WOOL);
		ItemMeta meta = resetButton.getItemMeta();
		
		// Set name display to "Reset Hat"
		meta.setDisplayName("Reset Hat");
		meta.setCustomModelData((int) page);
		
		// Save Changes
		resetButton.setItemMeta(meta);
		
		gui.setItem(31, resetButton);
		
		List<CustomItem> hatList = CustomItem.getItems(ItemType.HAT);
		
		// List of Player's owned hats
		List<CustomItem> owned = PlayerConfig.getItem(player.getPlayer(), ItemType.HAT);
		
		int index = 0;
		
		int total = (page - 1) * pageCap;
		
		for (CustomItem item : hatList)
		{
			if (index < total)
			{
				index++;
				continue;
			}
			
			if (index >= (page * pageCap))
			{
				break;
			}
			
			int id = item.getItem().getItemMeta().getCustomModelData();
			
			// if player already has it.
			// Checks by having looping through player's owned hats
			boolean unlocked = false;
			for (CustomItem cs : owned)
			{
				// Get more info
				ItemMeta im = cs.getItem().getItemMeta(); 
				int id2 = im.getCustomModelData();
				
				// Check if player owns it
				if (id2 == id)
				{
				unlocked = true;
				break;
				}
			}
			
			// CustomItem in ItemStack format
			ItemStack is = new ItemStack(item.getItem());
			
			// Get info about item
			ItemMeta im = is.getItemMeta();
			
			// Create Item description
			List<String> lore = new LinkedList<String>();
			if (unlocked)
				lore.add(unlockedString);
			else
			{
				// What Currency is being used
				Currency currency;
				
				// The total price for the item
				int price;
				
				// Put price on description, if not buyable skip
				if (item.getPointPrice() > -1)
				{
					currency = Currency.POINTS;
					price = item.getPointPrice();
				}
				else if (item.getCoinPrice() > -1)
				{
					currency = Currency.COINS;
					price = item.getCoinPrice();
				}
				else
				{
					// Set currency to none since item is not buyable in any currency
					currency = null;
					price = -1;
				}
				
				List<String> desc = item.getDescription();
				if (desc != null)
					for (String s : desc)
					{
						s = ChatColor.translateAlternateColorCodes('&', s);
						lore.add(s);
					}
				
				// Add Price to 2nd line in description (If item is buyable
				if (currency != null)
				{
					String priceDesc = String.format("Cost: %d %s", price, currency == Currency.POINTS ? "Points" : "Coins");
					priceDesc = ChatColor.translateAlternateColorCodes('&', "&r&e"+priceDesc);
					lore.add(priceDesc);
				}
			}
			
			// Save Description
			im.setLore(lore);
			
			// Save Changes
			is.setItemMeta(im);
			
			// Add the hat
			gui.addItem(is);
			
			index++;
		}
		
		// The options
		
		// Create Back Button
		ItemStack is1 = new ItemStack(Material.ARROW);
		ItemMeta im1 = is1.getItemMeta();
		
		// Set name display to "Back"
		im1.setDisplayName("Back");
		
		// Save Changes
		is1.setItemMeta(im1);
		
		// Create Next button
		ItemStack is3 = new ItemStack(Material.ARROW);
		ItemMeta im3 = is3.getItemMeta();
		
		// Set name display to "Next"
		im3.setDisplayName("Next");
		
		// Save Changes
		is3.setItemMeta(im3);
		
		// Place options on gui
		gui.setItem(30, is1);
		gui.setItem(32, is3);
		
		player.getPlayer().openInventory(gui);
	}
	
	public static void clicked(CustomPlayer player, ItemStack item, Inventory inventory)
	{
		Material material = item.getType();
		
		// If clicked on a hat
		if (material == Material.INK_SAC)
		{
			// Get information about item
			ItemMeta im = item.getItemMeta();
			
			// If ID don't exist
			if (!im.hasCustomModelData())
				return;
			
			// Get ID
			int id = im.getCustomModelData();
			
			// Check if Player owns hat
			List<CustomItem> owned = PlayerConfig.getItem(player.getPlayer(), ItemType.HAT);
			for (CustomItem ci : owned)
			{
				// Get info about item
				ItemMeta im2 = ci.getItem().getItemMeta();
				
				// Get ID
				int id2 = im2.getCustomModelData();
				
				// If matches, player has that hat
				if (id2 == id)
				{
					// Get Player Equipment
					EntityEquipment equipment = player.getPlayer().getEquipment();
					
					// Put hat on player
					equipment.setHelmet(item);
					
					player.getPlayer().closeInventory();
					return;
				}
		}
		
		// Get CustomItem from ID
		CustomItem cs = CustomItem.getCustomItem(id);
		
		// What currency is being used
		// If neither used, stop code; not a buyable item
		Currency currency;
		if (cs.getPointPrice() > -1)
			currency = Currency.POINTS;
		else if (cs.getCoinPrice() > -1)
			currency = Currency.COINS;
		else
			return;
		
		// Give Player a buy prompt
		BuyConfirm.create(player.getPlayer(), cs, currency);
		
		return;
		}
		
		if (material == Material.ARROW)
		{
			ItemMeta meta = item.getItemMeta();
			if (meta.getDisplayName().equalsIgnoreCase("next"))
			{
				byte page = (byte) inventory.getItem(31).getItemMeta().getCustomModelData();
				page++;
				
				if (inventory.getItem(pageCap - 1) != null)
					open(player, page);
			}
			else
			{
				byte page = (byte) inventory.getItem(31).getItemMeta().getCustomModelData();
				
				if (page > 1)
				{
					page--;
					open(player, page);
				}
			}
			
			return;
		}
		
		// Get info about item
		ItemMeta im = item.getItemMeta();
		String name = im.getDisplayName();
		
		// If reset button is pressed
		if (name.equals("Reset Hat"))
		{
			// Variable of Player's equipment
			EntityEquipment equipment = player.getPlayer().getEquipment();
			
			// Remove player's helmet (hat)
			equipment.setHelmet(null);
			
			// Close player's inventory
			player.getPlayer().closeInventory();
			
			return;
		}
	}
	
}
