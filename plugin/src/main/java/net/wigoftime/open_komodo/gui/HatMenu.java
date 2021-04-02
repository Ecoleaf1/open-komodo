package net.wigoftime.open_komodo.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.ItemType;

public class HatMenu extends CustomItemMenuType1 {
	private final String fileName;
	
	public HatMenu(CustomPlayer customPlayer, String fileName) {
		super(customPlayer, null, getItems(customPlayer, fileName), "Hat-Menu", 36);
		this.fileName = fileName;
		
		gui.setItem(31, resetButton);
	}

	@Override
	public void clicked(InventoryClickEvent clickEvent) {
		super.clicked(clickEvent);
		
		switch (clickEvent.getCurrentItem().getType()) {
		case INK_SAC:
			clickedHat(opener, clickEvent.getCurrentItem());
			break;
		case WHITE_WOOL:
			if (!clickEvent.getCurrentItem().getItemMeta().hasCustomModelData()) break;
			if (clickEvent.getCurrentItem().getItemMeta().getCustomModelData() != 2) return;
			
			CustomPlayer playerCustom = CustomPlayer.get(clickEvent.getWhoClicked().getUniqueId());
			playerCustom.setHat(null);
			clickEvent.getWhoClicked().closeInventory();
			break;
		}
	}
	
	private static List<ItemStack> getItems(CustomPlayer opener, String fileName) {
		File shopPage = new File(String.format("%s/gui/hatshop/%s.yml",Main.dataFolderPath,fileName));
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(shopPage);
		List<Integer> ids = config.getIntegerList("ID");
		
		List<ItemStack> items = new ArrayList<ItemStack>(ids.size());
		for (int idIndex : ids)
			items.add(getHatItemStack(opener, CustomItem.getCustomItem(idIndex)));
		
		return items;
	}
	
	
	private static ItemStack getHatItemStack(CustomPlayer customPlayer, CustomItem customItem){
		ItemStack itemStack = new ItemStack(customItem.getItem());
		
		// Get info about item
		ItemMeta itemMeta = itemStack.getItemMeta();
		
		// Get ID
		int id = itemMeta.getCustomModelData();
		
		// Copy the current CustomItem description to a new list
		// to prevent the original description being modified
		List<String> itemStackDescription = new ArrayList<String>();
		for (String line : customItem.getDescription())
			itemStackDescription.add(
					ChatColor.translateAlternateColorCodes('&', line));
		
		// If player owns hat
		if (customPlayer.hasItem(id, ItemType.HAT))
			itemStackDescription.add(String.format
					("%s%s%s", ChatColor.GREEN, ChatColor.BOLD, "Unlocked"));
		
		// If player does not own hat 
		else
		{
			Currency currencyType;
			
			// Get currency type
			if (customItem.getPointPrice() > -1)
				currencyType = Currency.POINTS;
			else if (customItem.getCoinPrice() > -1)
				currencyType = Currency.COINS;
			else
				currencyType = null;
			
			// If item buyable, display price
			if (currencyType != null)
			{
				int price;
				price = currencyType == Currency.POINTS ? customItem.getPointPrice() : customItem.getCoinPrice();
				String priceDesc = String.format("Cost: %d %s", price, currencyType == Currency.POINTS ? "Points" : "Coins");
				itemStackDescription.add(0, priceDesc);
			}
		}
		
		// Save Description
		itemMeta.setLore(itemStackDescription);
		
		// Save Changes
		itemStack.setItemMeta(itemMeta);
		
		return itemStack;
	}
	
	public static boolean isValid(String fileName) {
		File shopPage = new File(String.format("%s/gui/hatshop/%s.yml",Main.dataFolderPath,fileName));
		return shopPage.exists();
	}
	
	private static void clickedHat(CustomPlayer clicker, ItemStack clickedItem) {
		// Get information about item
		ItemMeta clickedItemMeta = clickedItem.getItemMeta();
		
		// If ID don't exist
		if (!clickedItemMeta.hasCustomModelData())
			return;
		
		// Get ID
		int clickedItemID = clickedItemMeta.getCustomModelData();
		
		if (clicker.hasItem(clickedItemID, ItemType.HAT))
		{	
			// Put hat on player
			clicker.setHat(clickedItem);
			
			clicker.getPlayer().closeInventory();
			return;
		}
		
		// Get CustomItem from ID
		CustomItem clickedCustomItem = CustomItem.getCustomItem(clickedItemID);
		
		// What currency is being used
		// If neither used, stop code; not a buyable item
		Currency currency;
		if (clickedCustomItem.getPointPrice() > -1)
			currency = Currency.POINTS;
		else if (clickedCustomItem.getCoinPrice() > -1)
			currency = Currency.COINS;
		else
			return;
		
		// Give Player a buy prompt
		BuyConfirm gui = new BuyConfirm(clicker, clickedCustomItem, currency, clickedCustomItem.getPermission());
		gui.open();
		
		return;
	}
	
	public void refresh(List<ItemStack> listOfItems) {
		super.refresh();
		gui.setItem(31, resetButton);
	}
	
	private final static ItemStack resetButton = new ItemStack(Material.WHITE_WOOL); {
		// Create Reset Hat Button
		ItemStack resetButtonItemStack = new ItemStack(Material.WHITE_WOOL);
		ItemMeta resetItemButtonItemMeta = resetButtonItemStack.getItemMeta();
		
		// Set name display to "Reset Hat"
		resetItemButtonItemMeta.setDisplayName("Reset Hat");
		resetItemButtonItemMeta.setCustomModelData(2);
		
		resetButton.setItemMeta(resetItemButtonItemMeta);
	}
}
