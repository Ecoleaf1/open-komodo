package net.wigoftime.open_komodo.gui;

import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.etc.PetsManager;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.Pet;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PetMenu extends CustomGUI {
	public static final String title = "Pet Menu";

	public PetMenu(CustomPlayer customPlayer) {
		super(customPlayer, Permissions.petAccess, Bukkit.createInventory(null, 45, title));
		gui.setContents(getListofPetIcons());
		return;
	}
	
	private ItemStack @NotNull [] getListofPetIcons() {
		List<Pet> petsSet = opener.getPets();
		ItemStack[] guiContent = new ItemStack[petsSet.size()];
		
		// Loop through each pet to display on gui
		int index = 0;
		for (Pet pet : petsSet)
		{
			ItemStack petItemStack = new ItemStack(pet.getItem());
			ItemMeta petItemMeta = petItemStack.getItemMeta();
			
			// Set Display Name on item
			petItemMeta.setDisplayName(pet.getDisplayName());
			
			// Assigning ID to to track
			petItemMeta.setCustomModelData(pet.getID());
			
			// Save Changes
			petItemStack.setItemMeta(petItemMeta);
			
			// Add Item
			guiContent[index++] = petItemStack;
		}
		
		return guiContent;
	}

	@Override
	public void clicked(@NotNull InventoryClickEvent clickEvent) {
		clickEvent.setCancelled(true);
		ItemStack clickedItem = clickEvent.getCurrentItem();

		if (clickedItem.getType() != Material.GHAST_SPAWN_EGG) return;
		if (!clickedItem.hasItemMeta()) return;
		if (!clickedItem.getItemMeta().hasCustomModelData()) return;

		int petID = clickedItem.getItemMeta().getCustomModelData();
		Pet pet = Pet.getPet(petID);


		PetsManager.create(opener.getPlayer(), pet);
		opener.getPlayer().closeInventory();
		return;
	}
}
