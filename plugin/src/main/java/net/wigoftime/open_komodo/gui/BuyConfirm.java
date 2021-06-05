package net.wigoftime.open_komodo.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;

import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.etc.CurrencyClass;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.Pet;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;

public class BuyConfirm extends CustomGUI
{
	public static final String title = ChatColor.translateAlternateColorCodes('&', "Confirmation");
	
	private final Object pendingItem;
	private final ItemStack cancelButton;
	private final ItemStack confirmButton;
	
	// Create inventory for CustomItem
	public BuyConfirm(CustomPlayer customPlayer,CustomItem pendingCustomItem, Currency currencyType, Permission requiredPermissoin) 
	{
		super(customPlayer, requiredPermissoin, Bukkit.getServer().createInventory(null, 27, title));
		pendingItem = pendingCustomItem;
		
		confirmButton = new ItemStack(Material.LIME_WOOL,1); {
		// Change display name
		ItemMeta itemMeta = confirmButton.getItemMeta();
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lConfirmation"));
		confirmButton.setItemMeta(itemMeta);
		}
		
		// Create side to cancel.=
		cancelButton = new ItemStack(Material.RED_WOOL,1); {
		
		// Change the display name
		ItemMeta denyMeta = cancelButton.getItemMeta();
		denyMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lCancel"));
		cancelButton.setItemMeta(denyMeta);
		}
		
		// Display buttons
		gui.setContents(createButtons((byte) gui.getSize()));
		
		// Display item thats being bought
		gui.setItem(12, pendingCustomItem.getItem());
		
		// Set the Currency Item on the 14th slot
		gui.setItem(14, createCurrencyIcon(pendingCustomItem, currencyType));
		
		return;
	}
	
	// Create inventory for Pets
	public BuyConfirm(CustomPlayer customPlayer,Pet pendingPet, Currency currencyType) 
	{
		super(customPlayer, pendingPet.getPermission(), Bukkit.getServer().createInventory(null, 27, title));
		pendingItem = pendingPet;
		
		confirmButton = new ItemStack(Material.LIME_WOOL,1); {
		// Change display name
		ItemMeta itemMeta = confirmButton.getItemMeta();
		itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lConfirmation"));
		confirmButton.setItemMeta(itemMeta);
		}
		
		// Create side to cancel.=
		cancelButton = new ItemStack(Material.RED_WOOL,1); {
		
		// Change the display name
		ItemMeta denyMeta = cancelButton.getItemMeta();
		denyMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lCancel"));
		cancelButton.setItemMeta(denyMeta);
		}
		
		// Display buttons
		gui.setContents(createButtons((byte) gui.getSize()));
		
		// Display item thats being bought
		gui.setItem(12, pendingPet.getItem());
		
		// Set the Currency Item on the 14th slot
		gui.setItem(14, createCurrencyIcon(pendingPet, currencyType));
		
		return;
	}
	
	public void clickedBuy() {
		// Find the player that is using this gui
		HumanEntity humanEntity = gui.getViewers().get(0);
		CustomPlayer customPlayer = CustomPlayer.get(humanEntity.getUniqueId());
		
		// select the pending item
		ItemStack pendingItem = gui.getItem(12);
		
		// Get ID
		int customItemID = pendingItem.getItemMeta().getCustomModelData();
		
		// Get CustomItem
		CustomItem pendingCustomItem = CustomItem.getCustomItem(customItemID);
		
		// 
		if (pendingItem.getType() == Pet.material)
			buyPet(customPlayer, pendingItem);
		else 
			buyCustomItem(customPlayer, pendingCustomItem);
		
		// Close inventory
		customPlayer.getPlayer().closeInventory();
	}
	
	private static void buyPet(CustomPlayer playerCustomPlayer, ItemStack pendingItem)
	{
			// Get info about item
			ItemMeta pendingItemMeta = pendingItem.getItemMeta();
			
			// Get pet from ID on item
			Pet pet = Pet.getPet(pendingItemMeta.getCustomModelData());
			
			// Currency that is being used
			Currency currencyType;
			
			// Identify what currency is being used.
			if (pendingItem.getType() == Material.GOLD_INGOT)
				currencyType = Currency.COINS;
			else
				currencyType = Currency.POINTS;
			
			// Get the price
			int price = (currencyType == Currency.POINTS) ? 
						pet.getPrice(Currency.POINTS) : 
							pet.getPrice(Currency.COINS);
			
			// Buy gui
			CurrencyClass.buy(playerCustomPlayer, price, currencyType, pet);
			
			return;
	}
	
	private static void buyCustomItem(CustomPlayer customPlayer, CustomItem pendingCustomItem)
	{		
			// Currency that is being used
			Currency currencyType;
			
			// Identify what currency is being used.
			if (pendingCustomItem.getItem().getType() == Material.GOLD_INGOT)
				currencyType = Currency.COINS;
			else
				currencyType = Currency.POINTS;
			
			// Buy CustomItem
			CurrencyClass.buy(customPlayer, currencyType == Currency.POINTS ? 
					pendingCustomItem.getPointPrice() : pendingCustomItem.getCoinPrice(), currencyType, pendingCustomItem);
	}
	
	// Create currency icon for CustomItems
	private static ItemStack createCurrencyIcon(CustomItem customItem, Currency currencyType)
	{
		ItemStack currencyIcon;
		
		// Get the currency type
		
		if (currencyType == Currency.POINTS)
			currencyIcon = new ItemStack(Material.IRON_INGOT);
		else
			currencyIcon = new ItemStack(Material.GOLD_INGOT);
		
		// Display the price
		
		ItemMeta meta = currencyIcon.getItemMeta();
		
		if (currencyType == Currency.POINTS)
			meta.setDisplayName(String.format("Cost: %d Points", customItem.getPointPrice()));
		else
			meta.setDisplayName(String.format("Cost: %d Coins", customItem.getCoinPrice()));
		
		// Set ItemMeta and return
		
		currencyIcon.setItemMeta(meta);
		
		return currencyIcon;
	}
	
	// Create currency icon for Pets
	private static ItemStack createCurrencyIcon(Pet pet, Currency currencyType)
	{
		ItemStack currencyIcon;
		
		// Get the currency type
		
		if (currencyType == Currency.POINTS)
			currencyIcon = new ItemStack(Material.IRON_INGOT);
		else
			currencyIcon = new ItemStack(Material.GOLD_INGOT);
		
		// Display the price
		
		ItemMeta meta = currencyIcon.getItemMeta();
		
		if (currencyType == Currency.POINTS)
			meta.setDisplayName(String.format("Cost: %d Points", pet.getPrice(currencyType)));
		else
			meta.setDisplayName(String.format("Cost: %d Coins", pet.getPrice(currencyType)));
		
		// Set ItemMeta and return
		
		currencyIcon.setItemMeta(meta);
		
		return currencyIcon;
	}
	
	private ItemStack[] createButtons(byte size)
	{
		
		ItemStack[] contents = new ItemStack[size];
		
		// Place down buttons at left and right side
		for (byte i = 1; (i * 1) < size; i++)
		{
			byte slot = (byte) (i * 9);
			
			if (slot > size)
				break;
			
			// Place confirmButton on right side
			contents[slot - 1] = confirmButton;
			contents[slot - 2] = confirmButton;
			
			// Place cancelButton the left side
			contents[slot - 8] = cancelButton;
			contents[slot - 9] = cancelButton;
		}
		
		return contents;
	}

	public void clicked(InventoryClickEvent clickEvent) {
		clickEvent.setCancelled(true);
		ItemStack clickedIcon = clickEvent.getCurrentItem();
		
		if (clickedIcon.equals(confirmButton)) {
			PrintConsole.test("Bought");
			opener.setActiveGui(null);
			clickedBuy();
			return;
		}
		
		if (clickedIcon.equals(cancelButton)) {
			opener.setActiveGui(null);
			opener.getPlayer().closeInventory();
		}
	}
	
}
