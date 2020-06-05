package net.wigoftime.open_komodo.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.etc.CurrencyClass;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.Pet;
import net.wigoftime.open_komodo.objects.Tag;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

abstract public class BuyConfirm 
{
	
	public static final String title = ChatColor.translateAlternateColorCodes('&', "Confirmation");
	
	private static final Material confirmMaterial = Material.LIME_WOOL;
	private static final Material cancelMaterial = Material.RED_WOOL;
	
	/*
	public static void create(Player player, Tag tag, Currency currency)
	{
		Inventory gui = Bukkit.getServer().createInventory(null, 27, "Confirmation");
		
		// Create side to cancel.=
		ItemStack denyItem = new ItemStack(cancelMaterial,1); 
		
		// Change the display name
		ItemMeta denyMeta = denyItem.getItemMeta();
		denyMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lCancel"));
		denyItem.setItemMeta(denyMeta);
		
		// Create side to confirm
		ItemStack confirmItem = new ItemStack(confirmMaterial,1); 
		
		// Change display name
		ItemMeta itemMeta = confirmItem.getItemMeta();
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lConfirmation"));
		confirmItem.setItemMeta(itemMeta);
		
		// Place the cancel side items to the left side
		gui.setItem(0,denyItem);
		gui.setItem(1,denyItem);
		gui.setItem(9, denyItem);
		gui.setItem(10, denyItem);
		gui.setItem(18, denyItem);
		gui.setItem(19, denyItem);
		
		// Place the confirm side items to the right side
		gui.setItem(7, confirmItem);
		gui.setItem(8, confirmItem);
		gui.setItem(16, confirmItem);
		gui.setItem(17, confirmItem);
		gui.setItem(25, confirmItem);
		gui.setItem(26, confirmItem);
		
		// Name of Currency
		String cn = currency == Currency.POINTS ?
					"Points" : "Coins";
		
		// Cost amount of item
		int amount;
		amount = currency == Currency.POINTS ? 
				 tag.getPointPrice() : tag.getCoinPrice();
		
		// Create the display item
		ItemStack is = new ItemStack(Material.NAME_TAG);
		
		// Set display name on display item
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(tag.getDisplay());
		
		// Save changes
		is.setItemMeta(im);
		
		// Display the display item on the 12th slot
		gui.setItem(12, is);
		
		// Currency Item
		ItemStack ci;
		
		// Get the currency type
		if (currency == Currency.POINTS)
			ci = new ItemStack(Material.IRON_INGOT);
		else
			ci = new ItemStack(Material.GOLD_INGOT);
		
		// Show price
		ItemMeta cim = ci.getItemMeta();
		cim.setDisplayName(String.format("Cost: %d %s", amount, cn));
		
		// Save changes
		ci.setItemMeta(cim);
		
		// Set the Currency Item on the 14th slot
		gui.setItem(14, ci);
		
		player.openInventory(gui);
	} */
	
	public static void create(Player player,Pet pet, Currency currency) 
	{
		Inventory gui = Bukkit.getServer().createInventory(null, 27, "Confirmation");
		
		// Create side to cancel.=
		ItemStack denyItem = new ItemStack(cancelMaterial,1); 
		
		// Change the display name
		ItemMeta denyMeta = denyItem.getItemMeta();
		denyMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lCancel"));
		denyItem.setItemMeta(denyMeta);
		
		// Create side to confirm
		ItemStack confirmItem = new ItemStack(confirmMaterial,1); 
		
		// Change display name
		ItemMeta itemMeta = confirmItem.getItemMeta();
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lConfirmation"));
		confirmItem.setItemMeta(itemMeta);
		
		// Place the cancel side items to the left side
		gui.setItem(0,denyItem);
		gui.setItem(1,denyItem);
		gui.setItem(9, denyItem);
		gui.setItem(10, denyItem);
		gui.setItem(18, denyItem);
		gui.setItem(19, denyItem);
		
		// Place the confirm side items to the right side
		gui.setItem(7, confirmItem);
		gui.setItem(8, confirmItem);
		gui.setItem(16, confirmItem);
		gui.setItem(17, confirmItem);
		gui.setItem(25, confirmItem);
		gui.setItem(26, confirmItem);
		
		// Name of Currency
		String cn = currency == Currency.POINTS ?
					"Points" : "Coins";
		
		// Cost amount of item
		int amount;
		amount = currency == Currency.POINTS ? 
				 pet.getPrice(Currency.POINTS) : pet.getPrice(Currency.COINS);
		
		// Display the item on the 12th slot.
		ItemStack is = new ItemStack(Material.GHAST_SPAWN_EGG);
		
		ItemMeta meta = is.getItemMeta();
		
		// Put ID
		meta.setCustomModelData(pet.getID());
		
		// Set Display Name on item
		meta.setDisplayName(pet.getDisplayName());
		
		// Save Changes
		is.setItemMeta(meta);
		
		gui.setItem(12, is);
		
		// Currency Item
		ItemStack ci;
		
		// Get the currency type
		if (currency == Currency.POINTS)
			ci = new ItemStack(Material.IRON_INGOT);
		else
			ci = new ItemStack(Material.GOLD_INGOT);
		
		// Show price
		ItemMeta im = ci.getItemMeta();
		im.setDisplayName(String.format("Cost: %d %s", amount, cn));
		
		// Save changes
		ci.setItemMeta(im);
		
		// Set the Currency Item on the 14th slot
		gui.setItem(14, ci);
		
		player.openInventory(gui);
	}
	
	public static void create(Player player,CustomItem item, Currency currency) 
	{
		// Check if player has permission for the item
		if (item.getPermission() != null)
			if (!player.hasPermission(item.getPermission()))
			{
				player.sendMessage(Permissions.useError);
				
				player.closeInventory();
				return;
			}
		
		Inventory gui = Bukkit.getServer().createInventory(null, 27, "Confirmation");
		
		// Create side to cancel.=
		ItemStack denyItem = new ItemStack(cancelMaterial,1); 
		
		// Change the display name
		ItemMeta denyMeta = denyItem.getItemMeta();
		denyMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lCancel"));
		denyItem.setItemMeta(denyMeta);
		
		// Create side to confirm
		ItemStack confirmItem = new ItemStack(confirmMaterial,1); 
		
		// Change display name
		ItemMeta itemMeta = confirmItem.getItemMeta();
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lConfirmation"));
		confirmItem.setItemMeta(itemMeta);
		
		// Place the cancel side items to the left side
		gui.setItem(0,denyItem);
		gui.setItem(1,denyItem);
		gui.setItem(9, denyItem);
		gui.setItem(10, denyItem);
		gui.setItem(18, denyItem);
		gui.setItem(19, denyItem);
		
		// Place the confirm side items to the right side
		gui.setItem(7, confirmItem);
		gui.setItem(8, confirmItem);
		gui.setItem(16, confirmItem);
		gui.setItem(17, confirmItem);
		gui.setItem(25, confirmItem);
		gui.setItem(26, confirmItem);
		
		// Name of Currency
		String cn = currency == Currency.POINTS ?
					"Points" : "Coins";
		
		// Cost amount of item
		int amount;
		amount = currency == Currency.POINTS ? 
				 item.getPointPrice() : item.getCoinPrice();
		
		// Display the item on the 12th slot.
		ItemStack is = item.getNBTItem().getItem();
		gui.setItem(12, is);
		
		// Currency Item
		ItemStack ci;
		
		// Get the currency type
		if (currency == Currency.POINTS)
			ci = new ItemStack(Material.IRON_INGOT);
		else
			ci = new ItemStack(Material.GOLD_INGOT);
		
		// Show price
		ItemMeta im = ci.getItemMeta();
		im.setDisplayName(String.format("Cost: %d %s", amount, cn));
		
		// Save changes
		ci.setItemMeta(im);
		
		// Set the Currency Item on the 14th slot
		gui.setItem(14, ci);
		
		player.openInventory(gui);
	}
	
	public static void buy(Inventory gui)
	{
		// Find the player that is using this gui
		HumanEntity he = gui.getViewers().get(0);
		String username = he.getName();
		Player player = Bukkit.getPlayer(username);
		
		// select the pending item
		ItemStack cis = gui.getItem(12);
		
		// If it is Spawn egg, it is a pet item.
		if (cis.getType() == Material.GHAST_SPAWN_EGG)
		{
			// Get info about item
			ItemMeta meta = cis.getItemMeta();
			
			// Get pet from ID on item
			Pet pet = Pet.getPet(meta.getCustomModelData());
			
			// Currency that is being used
			Currency currency;
			
			// Identify what currency is being used.
			if (cis.getType() == Material.GOLD_INGOT)
				currency = Currency.COINS;
			else
				currency = Currency.POINTS;
			
			// Get the price
			int price = (currency == Currency.POINTS) ? 
						pet.getPrice(Currency.POINTS) : pet.getPrice(Currency.COINS);
			
			CurrencyClass.buy(player, price, currency, pet);
			player.closeInventory();
			return;
		}
		
		// Create an NBTItem to get custom model data.
		NBTItem nbti = new NBTItem(cis);
		int id = nbti.getInteger("CustomModelData");
		
		// About Item
		CustomItem cs = CustomItem.getCustomItem(id);
		
		// Currency that is being used
		Currency currency;
		
		// Identify what currency is being used.
		if (cis.getType() == Material.GOLD_INGOT)
			currency = Currency.COINS;
		else
			currency = Currency.POINTS;
		
		// Get the price
		int price = (currency == Currency.POINTS) ? 
					cs.getPointPrice() : cs.getCoinPrice();
		
		// Buy item
		CurrencyClass.buy(player, price, currency, cs);
		
		player.closeInventory();
	}
	
	/*
	public static void confirm(Player player) 
	{
		Inventory gui = player.getOpenInventory().getTopInventory();
		
		ItemStack itemStack = gui.getItem(12);
		ItemStack currencyItem = gui.getItem(14);
		
		String currency;
		
		if (currencyItem.getType() == Material.GOLD_INGOT) {
			currency = "Coins";
		} else
			currency = "Points";
		
		if (itemStack.getType() == Material.NAME_TAG) 
		{
		Tag tag = Tag.findByDisplay(itemStack.getItemMeta().getDisplayName());
		//buy(player, tag, currency);
		}
		InventoryView view = (InventoryView) gui;
		System.out.println(view.getTitle());
		
		player.closeInventory();
	}*/
	
	/*
	public static void confirm(Player player, Inventory gui, Currency currency, byte type) 
	{
		
		
		// Get the prop that is in the slot 23.
		Damageable damage = (Damageable) gui.getItem(12).getItemMeta();
		
		//CustomItem prop = CustomItem.getItem(gui.getItem(12).getType(), damage.getDamage());
		
		// The Buying item
		
		ItemStack is = gui.getItem(12);
		NBTItem nbtItem = new NBTItem(is);
		int id = nbtItem.getShort("CustomModelData");
		CustomItem cs = CustomItem.getCustomItem(id);
		
		int amount = currency == Currency.POINTS ?
				cs.getPointPrice() : cs.getCoinPrice();
		
			if (CurrencyClass.buy(player, amount, currency)) 
			{
				player.getInventory().addItem(is);
			}
	} */
			/*
		if (currency.equalsIgnoreCase("Coins"))
			if (Currency.buyWithCoins(player,cs.getCoinPrice())) 
			{
				if (type == 1)
					player.getInventory().addItem(is);
				
				if (type == 2) 
				{ */
					/*
					File config = PlayerConfig.getPlayerConfig(player);
					YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(config);
					
					List<String> items = configYaml.getStringList("Items");
					items.add(prop.getType() + " " + prop.getDamage());
					
					configYaml.set("Items", items);
					try {
						configYaml.save(config);
					} catch (IOException e) {
						PrintConsole.print("ERROR: Couldn't put it into " + player.getDisplayName() + "'s items in their config.");
					} *)/
				}
			}
		
		player.closeInventory();
	}
	
	private static void buy(Player player, Tag tag, String currency) {
		
		if (currency.equalsIgnoreCase("Points"))
			if (Currency.buyWithPoints(player, tag.getPointsPrice()) == true) {
				File config = PlayerConfig.getPlayerConfig(player);
				YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(config);
				
				List<String> tags = configYaml.getStringList("Tags");
				tags.add(tag.getDisplay());
				configYaml.set("Tags", tags);
				
				try {
					configYaml.save(config);
				} catch (IOException e) {
					PrintConsole.print("ERROR: Couldn't put it into " + player.getDisplayName() + "'s tags in their config.");
				}
				
			}
		
	} */
	
}
