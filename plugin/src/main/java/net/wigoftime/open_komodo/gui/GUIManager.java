package net.wigoftime.open_komodo.gui;

import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.InventoryManagement;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.ItemType;

abstract public class GUIManager {
	
	public static void invItemClicked(InventoryClickEvent clickEvent) {
		// If couldn't get item, return
		if (clickEvent.getCurrentItem() == null) {
			if (clickEvent.getCursor() != null)
				return;
			
			clickEvent.setCancelled(true);
			return;
		}
		
		// Player's current GUI Title
		String guiTitle = clickEvent.getView().getTitle();
		
		// Clicked ItemStack
		ItemStack clickedItemStack = clickEvent.getCurrentItem();
		
		// Get clicker
		CustomPlayer clickerCustomPlayer = CustomPlayer.get(clickEvent.getWhoClicked().getUniqueId());
		
		// Cancel if player in customPlayer format doesn't exist
		if (clickerCustomPlayer == null) {
			clickEvent.setCancelled(true);
			return;
		}
		
		if (clickerCustomPlayer.hasActiveGui()) {
			CustomGUI gui = clickerCustomPlayer.getActiveGui();
			
			if (gui instanceof CustomGUI)
				gui.clicked(clickEvent);
			
			return;
		}
		
		if (clickEvent.getClickedInventory().equals(PhoneSwitcher.phoneSwitcherGui)) {
			clickEvent.setCancelled(true);
			
			PhoneSwitcher.clicked(clickerCustomPlayer, clickedItemStack);
			return;
		}
		
		// Checks if clicked on an unmovable item
		if (clickedItemStack.getType() == Material.INK_SAC) {	
			unmovableItem(clickedItemStack, clickEvent);
			return;
		}
		
		// Clicked on bag
		if (clickedItemStack.getType() == Material.STICK) {
			clickedBag(clickerCustomPlayer, clickedItemStack, clickEvent);
			return;
		}
	}
	
	private static void clickedBag(CustomPlayer clickerCustomPlayer, ItemStack clickedItemStack ,InventoryClickEvent clickEvent) {
		if (isSameBag(clickerCustomPlayer.getUniqueId(), clickedItemStack))
			clickEvent.setCancelled(true);
		
		return;
	}
	
	private static boolean isSameBag(UUID clickerUUID, ItemStack clickedItemStack) {
		if (!InventoryManagement.currentOpen.containsKey(clickerUUID))
			return false;
		
		int clickedBagID = clickedItemStack.getItemMeta().getCustomModelData();
		int currentlyOpenedBagID = InventoryManagement.currentOpen.get(clickerUUID);
		
		return clickedBagID == currentlyOpenedBagID ? true : false;
	}
	
	private static void unmovableItem(ItemStack clickedItemStack, InventoryClickEvent clickEvent) {
		if (!clickedItemStack.getItemMeta().hasCustomModelData()) return;
		
		int clickedItemID = clickedItemStack.getItemMeta().getCustomModelData();
		CustomItem item = CustomItem.getCustomItem(clickedItemID);
		
		// If Player clicked on the helmet equipment slot.
		if (clickEvent.getSlot() == 39) {
			if (item.getType() == ItemType.HAT) clickEvent.setCancelled(true);
			return;
		}
		
		if (item.getType() == ItemType.PHONE) clickEvent.setCancelled(true);
	}
	
	public static void inventoryClosed(InventoryCloseEvent closeEvent) {	
		CustomPlayer customPlayer = CustomPlayer.get(closeEvent.getPlayer().getUniqueId());
		if (customPlayer == null)
			return;
		
		if (customPlayer.getActiveGui() != null) {
			customPlayer.getActiveGui().closed();
			customPlayer.setActiveGui(null);
		}
		
		customPlayer.setAfk(false);
		Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
			public void run() {
				InventoryManagement.saveInventory(customPlayer, customPlayer.getPlayer().getWorld());
			}
		});
	}
	
}
