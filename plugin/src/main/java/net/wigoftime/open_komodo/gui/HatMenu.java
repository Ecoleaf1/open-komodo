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

import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.ItemType;

abstract public class HatMenu 
{

	public static final String title = "Hat-menu";
	
	private static final String unlockedString = ChatColor.translateAlternateColorCodes('&', "&2&lUnlocked");
	
	public static void open(Player player) 
	{	
		Inventory gui = Bukkit.getServer().createInventory(null, 36, title);
		
		// The CustomItems in the shop (In order by IDs)
		int idList[] = 
			{
				28, 29, 30, 31, 32, 33, 34, 36, 37, 38, 39, 40, 42, 43, 44, 45, 46
			};
		
		// List of Player's owned hats
		List<CustomItem> owned = PlayerConfig.getItem(player, ItemType.HAT);
		
		for (int id : idList)
		{
			// Get Item from ID
			CustomItem ct = CustomItem.getCustomItem(id);
			
			// if player already has it.
			// Checks by having looping through player's owned hats
			boolean unlocked = false;
			for (CustomItem cs : owned)
			{
				// Get more info
				ItemMeta im = cs.getNBTItem().getItem().getItemMeta(); 
				int id2 = im.getCustomModelData();
				
				// Check if player owns it
				if (id2 == id)
				{
				unlocked = true;
				break;
				}
			}
			
			// CustomItem in ItemStack format
			ItemStack is = ct.getNBTItem().getItem();
			
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
				if (ct.getPointPrice() > -1)
				{
					currency = Currency.POINTS;
					price = ct.getPointPrice();
				}
				else if (ct.getCoinPrice() > -1)
				{
					currency = Currency.COINS;
					price = ct.getCoinPrice();
				}
				else
				{
					// Set currency to none since item is not buyable in any currency
					currency = null;
					price = -1;
				}
				
				List<String> desc = ct.getDescription();
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
		}
		
		
		/*
		// Add page 1 hats on gui
		
		gui.addItem(CustomItem.getCustomItem(169).getNBTItem().getItem());	// Brown bear ears
		gui.addItem(CustomItem.getCustomItem(171).getNBTItem().getItem());	// Bowler Hat
		gui.addItem(CustomItem.getCustomItem(172).getNBTItem().getItem());	// Bunny Ears
		gui.addItem(CustomItem.getCustomItem(173).getNBTItem().getItem());	// Advent wreath
		gui.addItem(CustomItem.getCustomItem(174).getNBTItem().getItem());	// Blue cap
		gui.addItem(CustomItem.getCustomItem(175).getNBTItem().getItem());	// Brown cap
		gui.addItem(CustomItem.getCustomItem(176).getNBTItem().getItem());	// Golden cap
		gui.addItem(CustomItem.getCustomItem(223).getNBTItem().getItem());	// Gray cap
		gui.addItem(CustomItem.getCustomItem(177).getNBTItem().getItem());	// Brown cat ears
		gui.addItem(CustomItem.getCustomItem(178).getNBTItem().getItem());	// Orange cat ears
		gui.addItem(CustomItem.getCustomItem(179).getNBTItem().getItem());	// White cat ears
		gui.addItem(CustomItem.getCustomItem(180).getNBTItem().getItem());	// Chicken
		gui.addItem(CustomItem.getCustomItem(202).getNBTItem().getItem());	// Santa's hat
		gui.addItem(CustomItem.getCustomItem(181).getNBTItem().getItem());	// Cowboy Hat
		gui.addItem(CustomItem.getCustomItem(182).getNBTItem().getItem());	// Crown King
		gui.addItem(CustomItem.getCustomItem(183).getNBTItem().getItem());	// Crown Prince
		gui.addItem(CustomItem.getCustomItem(184).getNBTItem().getItem());	// Crown Princess
		gui.addItem(CustomItem.getCustomItem(185).getNBTItem().getItem());	// Crown Queen
		gui.addItem(CustomItem.getCustomItem(186).getNBTItem().getItem());	// Elf
		gui.addItem(CustomItem.getCustomItem(187).getNBTItem().getItem());	// Fedora
		gui.addItem(CustomItem.getCustomItem(188).getNBTItem().getItem());	// Fez
		gui.addItem(CustomItem.getCustomItem(189).getNBTItem().getItem());	// Firefighter helmet
		gui.addItem(CustomItem.getCustomItem(190).getNBTItem().getItem());	// Flower bow
		gui.addItem(CustomItem.getCustomItem(218).getNBTItem().getItem());	// FlowerCrown
		gui.addItem(CustomItem.getCustomItem(191).getNBTItem().getItem());	// Foreverplay Hat
		gui.addItem(CustomItem.getCustomItem(192).getNBTItem().getItem());	// Fried Egg
		gui.addItem(CustomItem.getCustomItem(193).getNBTItem().getItem());	// Golf Hat */
		
		// The options
		
		// Create Back Button
		ItemStack is1 = new ItemStack(Material.ARROW);
		ItemMeta im1 = is1.getItemMeta();
		
		// Set name display to "Back"
		im1.setDisplayName("Back");
		
		// Save Changes
		is1.setItemMeta(im1);
		
		// Create Reset Hat Button
		ItemStack is2 = new ItemStack(Material.WHITE_WOOL);
		ItemMeta im2 = is2.getItemMeta();
		
		// Set name display to "Reset Hat"
		im2.setDisplayName("Reset Hat");
		
		// Save Changes
		is2.setItemMeta(im2);
		
		// Create Next button
		ItemStack is3 = new ItemStack(Material.ARROW);
		ItemMeta im3 = is3.getItemMeta();
		
		// Set name display to "Next"
		im3.setDisplayName("Next");
		
		// Save Changes
		is3.setItemMeta(im3);
		
		// Place options on gui
		gui.setItem(30, is1);
		gui.setItem(31, is2);
		gui.setItem(32, is3);
		
		player.openInventory(gui);
		/*
		gui.addItem(CustomItem.getCustomItem(194).getNBTItem().getItem());
		gui.addItem(CustomItem.getCustomItem(195).getNBTItem().getItem());
		gui.addItem(CustomItem.getCustomItem(196).getNBTItem().getItem());
		gui.addItem(CustomItem.getCustomItem(197).getNBTItem().getItem());
		gui.addItem(CustomItem.getCustomItem(198).getNBTItem().getItem());
		gui.addItem(CustomItem.getCustomItem(199).getNBTItem().getItem());
		gui.addItem(CustomItem.getCustomItem(200).getNBTItem().getItem()); */
		
		/*
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
		
		putItem(gui,Material.ARROW,1,"Back", 30);
		putItem(gui,Material.PAPER,1, "Reset Hat", 31);
		putItem(gui,Material.ARROW,1,"Next", 32);
		
		*/
	}
	
	public static void clicked(Player player, ItemStack item)
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
		List<CustomItem> owned = PlayerConfig.getItem(player, ItemType.HAT);
		for (CustomItem ci : owned)
		{
			// Get info about item
			ItemMeta im2 = ci.getNBTItem().getItem().getItemMeta();
			
			// Get ID
			int id2 = im2.getCustomModelData();
			
			// If matches, player has that hat
			if (id2 == id)
			{
				// Get Player Equipment
				EntityEquipment equipment = player.getEquipment();
				
				// Put hat on player
				equipment.setHelmet(item);
				
				player.closeInventory();
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
		BuyConfirm.create(player, cs, currency);
		
		return;
		}
		
		// Get info about item
		ItemMeta im = item.getItemMeta();
		String name = im.getDisplayName();
		
		// If reset button is pressed
		if (name.equals("Reset Hat"))
		{
			// Variable of Player's equipment
			EntityEquipment equipment = player.getEquipment();
			
			// Remove player's helmet (hat)
			equipment.setHelmet(null);
			
			// Close player's inventory
			player.closeInventory();
			
			return;
		}
	}
	
}
