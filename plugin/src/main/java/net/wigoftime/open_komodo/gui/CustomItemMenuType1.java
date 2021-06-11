package net.wigoftime.open_komodo.gui;

import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomItemMenuType1 extends CustomGUI {

	protected final int pageCap;
	private byte page = 1;
	private final byte maxPage;
	
	private final List<ItemStack> displayableItems;
	
	public CustomItemMenuType1(CustomPlayer customPlayer, Permission requiredPermission, List<ItemStack> listOfItems, @NotNull String title, int guiSize) {
		super(customPlayer, requiredPermission, Bukkit.createInventory(null, guiSize, title)); //36
		displayableItems = listOfItems;
		pageCap = guiSize - 9;
		maxPage = (byte) Math.ceil((double)displayableItems.size() / pageCap);
		
		refresh();
		return;
	}
	
	public CustomItemMenuType1(CustomPlayer customPlayer, Permission requiredPermission, List<ItemStack> listOfItems, @NotNull String title, int guiSize, byte roleplayModeOnly) {
		super(customPlayer, requiredPermission, Bukkit.createInventory(null, guiSize, title), roleplayModeOnly); //36
		displayableItems = listOfItems;
		pageCap = guiSize - 9;
		maxPage = (byte) Math.ceil((double)displayableItems.size() / pageCap);
		
		refresh();
		return;
	}
	
	public void refresh() {
		gui.clear();
		gui.setContents(createButtons(page, (byte) gui.getSize()));
		
		byte index = 0;
		byte guiIndex = 0;
		for (ItemStack currentItemStack : getPageItems()) {
			if (index >= pageCap) break;
			
			gui.setItem(guiIndex++,currentItemStack);
			index++;
		}
		
	}
	
	private ItemStack @NotNull [] createButtons(byte page, byte sizeOfGui)
	{
		ItemStack[] contents = new ItemStack[sizeOfGui];
		contents[pageCap + 3] = getPreviousPageButton();
		contents[pageCap + 5] = getNextPageButton();
		
		return contents;
	}
	
	private static @NotNull ItemStack getPreviousPageButton()
	{
		// Create Back Button
		ItemStack previousPageItemStack = new ItemStack(Material.ARROW);
		ItemMeta previousPageItemMeta = previousPageItemStack.getItemMeta();
		
		previousPageItemMeta.setDisplayName("Back");
		previousPageItemMeta.setCustomModelData(0);
		
		// Save Changes
		previousPageItemStack.setItemMeta(previousPageItemMeta);
		
		return previousPageItemStack;
	}
	
	private static @NotNull ItemStack getNextPageButton()
	{
		// Create Next Button
		ItemStack nextPageItemStack = new ItemStack(Material.ARROW);
		ItemMeta nextPageItemMeta = nextPageItemStack.getItemMeta();
		
		nextPageItemMeta.setDisplayName("Next");
		nextPageItemMeta.setCustomModelData(1);
		
		// Save Changes
		nextPageItemStack.setItemMeta(nextPageItemMeta);
		
		return nextPageItemStack;
	}
	
	public void clicked(@NotNull InventoryClickEvent clickEvent) {
		clickEvent.setCancelled(true);
		
		if (clickEvent.getCurrentItem().getType() == Material.ARROW) {
			ItemStack clickedArrow =  clickEvent.getCurrentItem();
			if (!clickedArrow.getItemMeta().hasCustomModelData()) return;
			
			int modelData = clickedArrow.getItemMeta().getCustomModelData();
			if (modelData == 0) {
				if (page <= 1) return;
				--page;
				refresh();
			} else {
				if (page >= maxPage) return;
				
				++page;
				refresh();
			}
		}
	}
	
	public @NotNull List<ItemStack> getPageItems() {
		List<ItemStack> itemsForDisplay = new ArrayList<ItemStack>(pageCap);
		
		// Index of hatList
		int idIndex = 0;
		
		// Minimum index to reach before adding items to list
		// This is the first index of the list for selected page.
		int hatListMinimumIndex = (page - 1) * pageCap;
		
		for (ItemStack itemIndex : displayableItems) {
			// If hasn't reached hatlist of current page, skip
			if (idIndex < hatListMinimumIndex) {
				idIndex++;
				continue;
			}
			
			// Stop if index has reached the end of the page.
			if (idIndex >= (page * pageCap)) break;
			
			// Add the hat
			itemsForDisplay.add(itemIndex);
			
			idIndex++;
		}
		
		return itemsForDisplay;
	}
}
