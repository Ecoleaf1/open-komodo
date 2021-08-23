package net.wigoftime.open_komodo.gui;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.ItemType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HatMenu extends CustomItemMenuType1 {
	private final String fileName;
	private final boolean isInStore;
	
	public HatMenu(@NotNull CustomPlayer customPlayer, String fileName, boolean isInStore) {
		super(customPlayer, null, getItems(customPlayer, fileName), "Hat Menu", 36);
		this.fileName = fileName;
		this.isInStore = isInStore;
		
		gui.setItem(31, resetButton);
	}

	@Override
	public void clicked(@NotNull InventoryClickEvent clickEvent) {
		super.clicked(clickEvent);
		
		switch (clickEvent.getCurrentItem().getType()) {
		case INK_SAC:
			clickedHat(opener, clickEvent.getCurrentItem(), isInStore);
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
	
	private static @NotNull List<ItemStack> getItems(@NotNull CustomPlayer opener, String fileName) {
		File shopPage = new File(String.format("%s/gui/hatshop/%s.yml",Main.dataFolderPath,fileName));
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(shopPage);
		List<Integer> ids = config.getIntegerList("ID");

		List<ItemStack> items = new ArrayList<ItemStack>(ids.size());
		for (int idIndex : ids)
			items.add(getHatItemStack(opener, CustomItem.getCustomItem(idIndex)));
		
		return items;
	}
	
	
	private static @NotNull ItemStack getHatItemStack(@NotNull CustomPlayer customPlayer, @NotNull CustomItem customItem){
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
	
	private static void clickedHat(@NotNull CustomPlayer clicker, @NotNull ItemStack clickedItem, boolean isInStore) {
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

		// If not in store
		if (!isInStore) {
			clicker.getPlayer().sendMessage(ChatColor.DARK_RED+"Sorry, but you must go to the mall to buy locked hats");
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
