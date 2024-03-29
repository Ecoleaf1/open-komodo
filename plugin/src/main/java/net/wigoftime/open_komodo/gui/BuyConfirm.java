package net.wigoftime.open_komodo.gui;

import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.etc.CurrencyClass;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.Pet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

public class BuyConfirm extends CustomGUI
{
	public static final String title = ChatColor.translateAlternateColorCodes('&', "Confirmation");
	
	private final @NotNull Object pendingItem;
	private final @NotNull ItemStack cancelButton;
	private final @NotNull ItemStack confirmButton;
	private final @NotNull Currency currencyType;
	
	// Create inventory for CustomItem
	public BuyConfirm(CustomPlayer customPlayer, @NotNull CustomItem pendingCustomItem, Currency currencyType, Permission requiredPermissoin)
	{
		super(customPlayer, requiredPermissoin, Bukkit.getServer().createInventory(null, 27, title));
		pendingItem = pendingCustomItem;
		this.currencyType = currencyType;
		
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
	public BuyConfirm(CustomPlayer customPlayer, @NotNull Pet pendingPet, Currency currencyType)
	{
		super(customPlayer, pendingPet.getPermission(), Bukkit.getServer().createInventory(null, 27, title));
		pendingItem = pendingPet;
		this.currencyType = currencyType;
		
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
			buyPet(customPlayer, currencyType, pendingItem);
		else 
			buyCustomItem(customPlayer, currencyType,pendingCustomItem);
		
		// Close inventory
		customPlayer.getPlayer().closeInventory();
	}
	
	private static void buyPet(@NotNull CustomPlayer playerCustomPlayer, @NotNull Currency currencyType, @NotNull ItemStack pendingItem)
	{
			// Get info about item
			ItemMeta pendingItemMeta = pendingItem.getItemMeta();
			
			// Get pet from ID on item
			Pet pet = Pet.getPet(pendingItemMeta.getCustomModelData());
			
			// Get the price
			int price = (currencyType == Currency.POINTS) ? 
						pet.getPrice(Currency.POINTS) : 
							pet.getPrice(Currency.COINS);
			
			// Buy gui
			CurrencyClass.buy(playerCustomPlayer, price, currencyType, pet);
			
			return;
	}
	
	private static void buyCustomItem(@NotNull CustomPlayer customPlayer, @NotNull Currency currencyType, @NotNull CustomItem pendingCustomItem)
	{
			
			// Buy CustomItem
			CurrencyClass.buy(customPlayer, currencyType == Currency.POINTS ? 
					pendingCustomItem.getPointPrice() : pendingCustomItem.getCoinPrice(), currencyType, pendingCustomItem);
	}
	
	// Create currency icon for CustomItems
	private static ItemStack createCurrencyIcon(@NotNull CustomItem customItem, Currency currencyType)
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
	private static ItemStack createCurrencyIcon(@NotNull Pet pet, Currency currencyType)
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
	
	private ItemStack @NotNull [] createButtons(byte size)
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

	public void clicked(@NotNull InventoryClickEvent clickEvent) {
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
