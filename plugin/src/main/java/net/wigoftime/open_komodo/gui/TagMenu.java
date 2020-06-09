package net.wigoftime.open_komodo.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.ItemType;

abstract public class TagMenu {
	
	private static final String tagFormat = "Tag: $W";
	public static final String title = "Tags";
	
	public static final String shop = ChatColor.translateAlternateColorCodes('&', "&e&lTag Shop");
	
	public static void open(CustomPlayer player, boolean next)
	{
		if (player.isBuilding())
		{
			player.getPlayer().sendMessage(CustomPlayer.buildingError);
		}
		
		// Create Page number Variable
		int page;
		
		// What page the player is on
		if (player.getPlayer().getOpenInventory().getTitle().equals(title))
		{
			// Get Player Inventory
			Inventory gui = player.getPlayer().getOpenInventory().getTopInventory();
			
			// Get Info about Reset Button
			ItemStack is = gui.getItem(49);
			ItemMeta im = is.getItemMeta();
			
			// Get page number
			
			if (next)
				page = im.getCustomModelData() + 1;
			else
				page = im.getCustomModelData() - 1;
		}
		else
			page = 1;
		
		// Create Gui
		Inventory gui = Bukkit.createInventory(null, 54, title);
		
		// Create Reset Button
		ItemStack is;
		is = new ItemStack(Material.WHITE_WOOL);
		
		// Name Reset Button
		ItemMeta im = is.getItemMeta();
		im.setDisplayName("Reset");
		
		// Store page number in the reset button for switching pages
		im.setCustomModelData(page);
		
		// Create Border Item
		ItemStack is2 = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
		
		// Set the border name to nothing
		ItemMeta im2 = is.getItemMeta();
		im2.setDisplayName(" ");
		
		// Save Changes to border
		is2.setItemMeta(im2);
		
		// Save Changes
		is.setItemMeta(im);
		
		// Add reset button
		gui.setItem(49, is);
		
		// Ad Tagshop button
		ItemStack iss = new ItemStack(Material.STICK);
		ItemMeta ims = iss.getItemMeta();
		
		ims.setDisplayName(shop);
		iss.setItemMeta(ims);
		
		gui.setItem(48, iss);
		
		// Add Border
		gui.setItem(44, is2);
		gui.setItem(43, is2);
		gui.setItem(42, is2);
		gui.setItem(41, is2);
		gui.setItem(40, is2);
		gui.setItem(39, is2);
		gui.setItem(38, is2);
		gui.setItem(37, is2);
		gui.setItem(36, is2);
		
		// Adding limit how many slots can be used
		int slotLimit = 35;
		
		// Get all the tags that the player owns
		List<CustomItem> list = PlayerConfig.getItem(player.getPlayer(), ItemType.TAG);
		
		// Variable to see if there is previous page
		boolean isPrevious;
		
		// Variable to see if there is next page
		boolean isNext;
		
		// Checks if there is a previous page
		if (page > 1)
			isPrevious = true;
		else
			isPrevious = false;
		
		// get Total amount that player owns
		int totalAmount;
		totalAmount = list.size();
		
		// get index of the items for the current page.
		int pageIndex;
		pageIndex = slotLimit * (page - 1);
		
		// What slot number the gui needs to set the item in
		int slotIndex = 0;
		
		/* What the list is up to
		 * 
		 * This is used when player needs to go on next page,
		 * Where it can tell the item list to catch up until it is
		 * on the next page of the items.
		 */
		int itemIndex = 0;
		
		// Loop through tags and add them to gui
		for (CustomItem item : list)
		{
			// pageAmount is higher than the itemIndex, keep skipping until the loop catches up
			if ((pageIndex) > itemIndex)
			{
				itemIndex++;
				continue;
			}
			
			// Stop if loop reaches slot limit
			if (slotIndex > slotLimit)
				break;
			
			// Get it into the ItemStack format
			ItemStack is3 = item.getItem();
			
			gui.setItem(slotIndex++, is3);
		}
		
		// Checks if there is a next page
		if (totalAmount >= (35 * page))
			isNext = true;
		else
			isNext = false;
		
		// If there is a previous page
		if (isPrevious)
		{
			// Create back button
			ItemStack is3 = new ItemStack(Material.ARROW);
			ItemMeta im3 = is3.getItemMeta();
			
			// Set display name
			im3.setDisplayName("Back");
			
			// Save Changes
			is3.setItemMeta(im3);
			
			// Add back button
			gui.setItem(46, is3);
		}
		
		// If there is a next page
		if (isNext)
		{
			// Create next button
			ItemStack is3 = new ItemStack(Material.ARROW);
			ItemMeta im3 = is3.getItemMeta();
			
			// Set display name 
			im3.setDisplayName("Next");
			
			// Save Changes
			is3.setItemMeta(im3);
			
			// Add Next Button
			gui.setItem(53, is3);
		}
		else
			gui.setItem(53, new ItemStack(Material.AIR));
		
		player.getPlayer().openInventory(gui);
	}
	
}
