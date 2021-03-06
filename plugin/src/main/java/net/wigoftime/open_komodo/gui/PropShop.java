package net.wigoftime.open_komodo.gui;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class PropShop extends CustomGUI
{
	public static final String title =  ChatColor.translateAlternateColorCodes('&', "&6&lProps");
	private List<Integer> idList;
	
	private void setup(String fileName)
	{
		File shopPage = new File(String.format("%s/gui/propshop/%s.yml",Main.dataFolderPath,fileName));
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(shopPage);
		idList = config.getIntegerList("ID");
	}
	
	public PropShop(CustomPlayer player, String fileName) {
		super(player, null, Bukkit.getServer().createInventory(null, 45, title));
		setup(fileName);
		
		int slot = 0;
		Iterator<Integer> iterator = idList.listIterator();
		while (iterator.hasNext())
		{
			gui.setItem(slot++, CustomItem.getCustomItem(iterator.next()).getItem());
		}
		
		return;
	}
	
	public void clicked(@NotNull InventoryClickEvent clickEvent) {
		clickEvent.setCancelled(true);
		ItemStack clickedItemStack = clickEvent.getCurrentItem();
		
		// Get clicked ID
		int clickedItemID;
		if (clickedItemStack.hasItemMeta())
			clickedItemID = clickedItemStack.getItemMeta().getCustomModelData();
		else
			clickedItemID = 0;
		
		if (clickedItemID < 1)
			return;
		
		// Get Item from ID
		CustomItem clickedCustomItem = CustomItem.getCustomItem(clickedItemID);
		
		// If couldn't find Item by ID, stop
		if (clickedCustomItem == null)
			return;
		
		// If not for sale, stop
		if (clickedCustomItem.getPointPrice() < 0 && clickedCustomItem.getCoinPrice() < 0)
			return;
		
		// Send player into BuyConfirm GUI to confirm payments
		BuyConfirm gui;
		if (clickedCustomItem.getCoinPrice() < 0)
			gui = new BuyConfirm(opener, clickedCustomItem, Currency.POINTS, clickedCustomItem.getPermission());
		else
			gui = new BuyConfirm(opener, clickedCustomItem, Currency.COINS, clickedCustomItem.getPermission());
		
		gui.open();
	}
	
	public static boolean isValidPropShop(String fileName) {
		File shopPage = new File(String.format("%s/gui/propshop/%s.yml",Main.dataFolderPath,fileName));
		return shopPage.exists();
	}
}
