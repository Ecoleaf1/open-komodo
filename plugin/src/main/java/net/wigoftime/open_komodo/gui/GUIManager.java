package net.wigoftime.open_komodo.gui;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.InventoryManagement;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.etc.dialog.DialogManager;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.ItemType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

abstract public class GUIManager {
	
	public static void invItemClicked(@NotNull InventoryClickEvent clickEvent) {
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
		
		if (clickEvent.getClickedInventory().equals(_PhoneSwitcher.phoneSwitcherGui)) {
			clickEvent.setCancelled(true);
			
			_PhoneSwitcher.clicked(clickerCustomPlayer, clickedItemStack);
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
		
		if (!clickerCustomPlayer.isBuilding())
		if (clickEvent.getSlotType() == SlotType.CRAFTING || clickEvent.getSlotType() == SlotType.RESULT) {
			clickEvent.setCancelled(true); return;
		}
	}
	
	private static void clickedBag(@NotNull CustomPlayer clickerCustomPlayer, @NotNull ItemStack clickedItemStack , @NotNull InventoryClickEvent clickEvent) {
		if (isSameBag(clickerCustomPlayer.getUniqueId(), clickedItemStack))
			clickEvent.setCancelled(true);
		
		return;
	}
	
	private static boolean isSameBag(UUID clickerUUID, @NotNull ItemStack clickedItemStack) {
		if (!InventoryManagement.currentOpen.containsKey(clickerUUID))
			return false;
		
		int clickedBagID = clickedItemStack.getItemMeta().getCustomModelData();
		int currentlyOpenedBagID = InventoryManagement.currentOpen.get(clickerUUID);
		
		return clickedBagID == currentlyOpenedBagID ? true : false;
	}
	
	private static void unmovableItem(@NotNull ItemStack clickedItemStack, @NotNull InventoryClickEvent clickEvent) {
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
	
	 public static void inventoryClosed(@NotNull InventoryCloseEvent closeEvent) {
		 PrintConsole.test("1");
		CustomPlayer customPlayer = CustomPlayer.get(closeEvent.getPlayer().getUniqueId());
		if (customPlayer == null)
			return;
		
		PrintConsole.test("2");
		
		if (customPlayer.getActiveGui() != null) {
			customPlayer.getActiveGui().closed();
			customPlayer.setActiveGui(null);
		}

		 DialogManager.leaving(customPlayer.getPlayer());
		customPlayer.setAfk(false);
		Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
			public void run() {
				InventoryManagement.saveInventory(customPlayer, customPlayer.getPlayer().getWorld());
			}
		});
	}
	
}
