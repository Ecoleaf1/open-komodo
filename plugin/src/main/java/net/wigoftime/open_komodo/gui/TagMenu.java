package net.wigoftime.open_komodo.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.ItemType;

public class TagMenu extends CustomGUI {
	private static final String tagFormat = "Tag: $W";
	public static final String title = "Tags";
	
	private static final ItemStack border = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
	private static final ItemStack backButton = new ItemStack(Material.ARROW);
	private static final ItemStack resetButton = new ItemStack(Material.WHITE_WOOL);
	private static final ItemStack nextButton = new ItemStack(Material.ARROW);
	private static final ItemStack tagShopButton = new ItemStack(Material.STICK);
	
	private static final int slotLimit = 35;
	public byte page = 1;
	
	public static void setup() {
		// Setup Reset Button
		{
			// Name Reset Button
			ItemMeta meta = resetButton.getItemMeta();
			meta.setDisplayName("Reset");
			
			// Store page number in the reset button for switching pages
			meta.setCustomModelData(1);
			resetButton.setItemMeta(meta);
		}
		
		// Setup Border Item
		{
		// Set the border name to nothing
		ItemMeta meta = border.getItemMeta();
		meta.setDisplayName(" ");
		
		// Save Changes to border
		border.setItemMeta(meta);
		}
		
		// Setup backButton
		{
			ItemMeta meta = backButton.getItemMeta();
			
			// Set display name
			meta.setDisplayName("Next");
			
			// Save Changes
			backButton.setItemMeta(meta);
		}
		
		// Setup nextButton
		{
			ItemMeta meta = nextButton.getItemMeta();
			
			// Set display name 
			meta.setDisplayName("Back");
			
			// Save Changes
			nextButton.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = tagShopButton.getItemMeta();
			
			meta.setDisplayName(getTagShopButtonDisplayName());
			tagShopButton.setItemMeta(meta);
		}
	}
	
	public TagMenu(CustomPlayer player) {
		super(player, null, Bukkit.createInventory(null, 54, title));
		
		// Add reset button
		gui.setItem(49, resetButton);
		
		// Ad Tagshop button
		
		gui.setItem(48, tagShopButton);
		
		// Add Border
		gui.setItem(44, border);
		gui.setItem(43, border);
		gui.setItem(42, border);
		gui.setItem(41, border);
		gui.setItem(40, border);
		gui.setItem(39, border);
		gui.setItem(38, border);
		gui.setItem(37, border);
		gui.setItem(36, border);
		
		// Adding limit how many slots can be used
		int slotLimit = 35;
		
		// Get all the tags that the player owns
		List<CustomItem> list = player.getItems(ItemType.TAG);
		
		// Variable to see if there is next page
		boolean isNext;
		
		// get Total amount that player owns
		int totalAmount;
		totalAmount = list.size();
		
		// get index of the items for the current page.
		int pageIndex;
		pageIndex = slotLimit * 0;
		
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
		for (CustomItem item : list) {
			// pageAmount is higher than the itemIndex, keep skipping until the loop catches up
			if ((pageIndex) > itemIndex) {
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
		if (totalAmount >= 35)
			isNext = true;
		else
			isNext = false;
		
		// If there is a next page
		if (isNext)
			// Add Next Button
			gui.setItem(53, nextButton);
		
		player.getPlayer().openInventory(gui);
	}
	
	@Override
	public void clicked(InventoryClickEvent clickEvent) {
		ItemStack clickedItem = clickEvent.getCurrentItem();
		
		clickEvent.setCancelled(true);
		
		if (clickedItem.equals(nextButton)) {
			toPage(true);	
			return;
		} else if (clickedItem.equals(backButton)) {
			toPage(false);
			return;
		} else if (clickedItem.equals(resetButton)) {
			opener.setTagDisplay("");
			return;
		} else if (clickEvent.getCurrentItem().equals(tagShopButton)) {
			TagShop gui = new TagShop(opener);
			gui.open();
		}
		
		if (clickEvent.getSlot() > slotLimit)
			return;
		
		//CustomItem item = CustomItem.getCustomItem(clickedItem.getItemMeta().getCustomModelData());
		opener.setTagDisplay(clickedItem.getItemMeta().getDisplayName());
		opener.getPlayer().closeInventory();
		
	}
	
	private void toPage(boolean isForward) {
		if (isForward)
			page++;
		else
			page--;
		
		// Get all the tags that the player owns
				List<CustomItem> list = opener.getItems(ItemType.TAG);
				
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
				for (CustomItem item : list) {
					// pageAmount is higher than the itemIndex, keep skipping until the loop catches up
					if ((pageIndex) > itemIndex) {
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
				if (isPrevious) {
					// Add back button
					gui.setItem(46, backButton);
				} else
					gui.setItem(46, new ItemStack(Material.AIR));
				
				// If there is a next page
				if (isNext) {
					// Add Next Button
					gui.setItem(53, nextButton);
				}
				else
					gui.setItem(53, new ItemStack(Material.AIR));
	}
	
	public static String getTagShopButtonDisplayName() {
		return ChatColor.WHITE + "" + ChatColor.BOLD + "Tag Shop";
	}
	
}
