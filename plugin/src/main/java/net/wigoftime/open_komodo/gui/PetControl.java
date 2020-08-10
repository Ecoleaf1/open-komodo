package net.wigoftime.open_komodo.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.wigoftime.open_komodo.etc.PetsManager;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class PetControl extends CustomGUI {
	public static final String title = "Pet Control";
	
	private static final ItemStack removeButton = createRemoveButton();
	private static final ItemStack changeNameButton = createChangeNameButton();
	private static final ItemStack mountButton = createMountButton();
	
	public PetControl(CustomPlayer customPlayer) {
		super(customPlayer, null, Bukkit.createInventory(null, 9, title));
		
		gui.setContents(setButtons(gui.getSize()));
	}
	
	public void clicked(InventoryClickEvent clickEvent) {
		clickEvent.setCancelled(true);
		ItemStack clickedItem = clickEvent.getCurrentItem();
		
		// Clicked on Remove Button
		if (clickedItem.equals(removeButton))
			PetsManager.removePet(opener.getPlayer());
		
		// Clicked on Change Name Button
		else if (clickedItem.equals(changeNameButton))
			PetsManager.setAwaitingNameInput(opener.getPlayer());
		
		// Clicked on Remove Button
		else if (clickedItem.equals(mountButton))
			PetsManager.mount(opener.getPlayer());
		
		opener.getPlayer().closeInventory();
	}
	
	private static ItemStack[] setButtons(int size) {
		ItemStack[] content = new ItemStack[size];
		
		content[0] = removeButton;
		content[1] = changeNameButton;
		content[2] = mountButton;
		
		return content;
	}
	
	private static ItemStack createRemoveButton() {
		ItemStack removeButtonItemStack = new ItemStack(Material.BARRIER);
		ItemMeta removeButtonItemMeta = removeButtonItemStack.getItemMeta();
		
		// Set button's display name
		removeButtonItemMeta.setDisplayName(ChatColor.BOLD + "Remove");
		
		// Set Button ID
		removeButtonItemMeta.setCustomModelData(1);
		
		removeButtonItemStack.setItemMeta(removeButtonItemMeta);
		return removeButtonItemStack;
	}
	
	private static ItemStack createChangeNameButton() {
		ItemStack changeNameButtonItemStack = new ItemStack(Material.NAME_TAG);
		ItemMeta changeNameButtonItemMeta = changeNameButtonItemStack.getItemMeta();
		
		// Set button's display name
		changeNameButtonItemMeta.setDisplayName(ChatColor.BOLD + "Set Name");
		
		// Set Button ID
		changeNameButtonItemMeta.setCustomModelData(2);
		
		changeNameButtonItemStack.setItemMeta(changeNameButtonItemMeta);
		return changeNameButtonItemStack;
	}
	
	private static ItemStack createMountButton() {
		ItemStack mountButtonItemStack = new ItemStack(Material.SADDLE);
		ItemMeta mountButtonItemMeta = mountButtonItemStack.getItemMeta();
		
		// Set button's display name
		mountButtonItemMeta.setDisplayName(ChatColor.BOLD + "Mount");
		
		// Set Button ID
		mountButtonItemMeta.setCustomModelData(3);
		
		mountButtonItemStack.setItemMeta(mountButtonItemMeta);
		return mountButtonItemStack;
	}
}
