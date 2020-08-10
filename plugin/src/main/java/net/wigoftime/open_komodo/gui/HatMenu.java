package net.wigoftime.open_komodo.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.ItemType;

public class HatMenu extends CustomGUI {
	public static final String title = "Hat-menu";
	private static final int pageCap = 27;
	private static byte page = 1;
	public HatMenu(CustomPlayer customPlayer) {
		super(customPlayer, null, Bukkit.createInventory(null, 36, title));
		
		gui.setContents(createButtons(page, (byte) gui.getSize()));
		
		for (ItemStack currentItemStack : listHats(customPlayer, page, (byte) gui.getSize()))
		{
			gui.addItem(currentItemStack);
		}
		
		return;
	}
	
	public void goToPage(byte page) {
		ItemStack[] guicontent = new ItemStack[gui.getSize()];
		
		guicontent = createButtons(page, (byte) gui.getSize());
		byte counter = 0;
		for (ItemStack currentItemStack : listHats(opener, page, (byte) gui.getSize()))
			guicontent[counter++] = currentItemStack;
		
		gui.setContents(guicontent);
	}
	
	private static List<ItemStack> listHats(CustomPlayer customPlayer, byte page, byte sizeOfGui) {
		List<ItemStack> contentList = new ArrayList<ItemStack>(pageCap);
		
		// List of hats on the server
		List<CustomItem> hatList = CustomItem.getItems(ItemType.HAT);
		
		// Index of hatList
		int hatListIndex = 0;
		
		// Minimum index of hatList to reach before placing down hats.
		// This is the hatlist start index for selected page.
		int hatListMinimumIndex = (page - 1) * pageCap;
		
		for (CustomItem currentCustomItem : hatList)
		{
			// If hasn't reached hatlist of current page, skip
			if (hatListIndex < hatListMinimumIndex)
			{
				hatListIndex++;
				continue;
			}
			
			// Stop if hatList index has reached the end of the page.
			if (hatListIndex >= (page * pageCap))
				break;
			
			// Add the hat
			contentList.add(getHatItemStack(customPlayer, currentCustomItem));
			
			// Increase index
			hatListIndex++;
		}
		
		return contentList;
	}
	
	private static ItemStack getHatItemStack(CustomPlayer customPlayer, CustomItem customItem)
	{
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
	
	private static ItemStack[] createButtons(byte page, byte sizeOfGui)
	{
		// Setup an array of content to setup & send
		ItemStack[] contents = new ItemStack[sizeOfGui];
		
		// Place buttons
		contents[31] = getResetButton(page);
		contents[30] = getPreviousPageButton();
		contents[32] = getNextPageButton();
		
		return contents;
	}
	
	private static ItemStack getResetButton(byte page)
	{
		// Create Reset Hat Button
		ItemStack resetButtonItemStack = new ItemStack(Material.WHITE_WOOL);
		ItemMeta resetItemButtonItemMeta = resetButtonItemStack.getItemMeta();
		
		// Set name display to "Reset Hat"
		resetItemButtonItemMeta.setDisplayName("Reset Hat");
		resetItemButtonItemMeta.setCustomModelData((int) page);
		
		// Save Changes
		resetButtonItemStack.setItemMeta(resetItemButtonItemMeta);
		
		return resetButtonItemStack;
	}
	
	private static ItemStack getPreviousPageButton()
	{
		// Create Back Button
		ItemStack previousPageItemStack = new ItemStack(Material.ARROW);
		ItemMeta previousPageItemMeta = previousPageItemStack.getItemMeta();
		
		// Set name display to "Back"
		previousPageItemMeta.setDisplayName("Back");
		
		// Save Changes
		previousPageItemStack.setItemMeta(previousPageItemMeta);
		
		return previousPageItemStack;
	}
	
	private static ItemStack getNextPageButton()
	{
		// Create Next Button
		ItemStack nextPageItemStack = new ItemStack(Material.ARROW);
		ItemMeta nextPageItemMeta = nextPageItemStack.getItemMeta();
		
		// Set name display to "Back"
		nextPageItemMeta.setDisplayName("Next");
		
		// Save Changes
		nextPageItemStack.setItemMeta(nextPageItemMeta);
		
		return nextPageItemStack;
	}
	
	public void clicked(InventoryClickEvent clickEvent) {
		clickEvent.setCancelled(true);
		ItemStack clickedItemStack = clickEvent.getCurrentItem();
		
		String clickedItemName = clickedItemStack.getItemMeta().getDisplayName();
		
		// If clicked on a hat
		if (clickedItemStack.getType() == Material.INK_SAC)
		{
			clickedHat(opener, clickedItemStack);
			return;
		}
		
		if (clickedItemStack.getType() == Material.ARROW)
		{
			// If clicked button was next-page button
			if (clickedItemName.equalsIgnoreCase("next"))
			{
				// Get next page number
				byte page = (byte) clickEvent.getClickedInventory().getItem(31).getItemMeta().getCustomModelData();
				page++;
				
				// Checks if another page exists
				if (clickEvent.getClickedInventory().getItem(pageCap - 1) != null)
					goToPage(page);
			}
			// If clicked button was previous-page button
			else
			{
				// Get current page number
				byte page = (byte) clickEvent.getClickedInventory().getItem(31).getItemMeta().getCustomModelData();
				
				// Stop if on page 1
				if (page <= 1)
					return;
				
				// Go back a page
				page--;
				goToPage(page);
			}
			
			return;
		}
		
		// If reset button is pressed
		if (clickedItemName.equals("Reset Hat"))
		{
			// Variable of Player's equipment
			EntityEquipment equipment = opener.getPlayer().getEquipment();
			
			// Remove player's helmet (hat)
			equipment.setHelmet(null);
			
			// Close player's inventory
			opener.getPlayer().closeInventory();
			
			return;
		}
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
			// Get Player Equipment
			EntityEquipment equipment = clicker.getPlayer().getEquipment();
			
			// Put hat on player
			equipment.setHelmet(clickedItem);
			
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
	
}
