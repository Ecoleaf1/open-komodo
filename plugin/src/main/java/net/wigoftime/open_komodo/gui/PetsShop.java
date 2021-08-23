package net.wigoftime.open_komodo.gui;

import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.etc.PetsManager;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.Pet;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PetsShop extends CustomGUI {
	public static final String title = "Pet Shop";

	public PetsShop(CustomPlayer customPlayer) {
		super(customPlayer, Permissions.petAccess, Bukkit.createInventory(null, 45, title));
		gui.setContents(getListofPetIcons());
		return;
	}
	
	private ItemStack @NotNull [] getListofPetIcons() {
		Set<Pet> petsSet = Pet.getPets();
		ItemStack[] guiContent = new ItemStack[petsSet.size()];
		
		// Loop through each pet to display on gui
		int index = 0;
		for (Pet pet : petsSet)
		{
			if (opener.hasPet(pet.getID())) continue;

			ItemStack petItemStack = new ItemStack(pet.getItem());
			ItemMeta petItemMeta = petItemStack.getItemMeta();
			
			// Set Display Name on item
			petItemMeta.setDisplayName(pet.getDisplayName());
			
			// Get Prices
			int pointPrice = pet.getPrice(Currency.POINTS);
			int coinPrice = pet.getPrice(Currency.COINS);
			
			// Display the pet's description
			List<String> lore = new ArrayList<String>();
			
			if (pointPrice > -1 && coinPrice > -1)
				lore.add(0, String.format("Price: %d points or %d coins", pointPrice, coinPrice));
			else if (pointPrice > -1)
				lore.add(0, String.format("Price: %d points", pointPrice));
			else if (coinPrice > -1)
				lore.add(0, String.format("Price: %d coins", coinPrice));
			
			// Set Description
			petItemMeta.setLore(lore);
			
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
		
		int petID = clickedItem.getItemMeta().getCustomModelData();
		Pet pet = Pet.getPet(petID);

		BuyConfirm gui = new BuyConfirm(opener, pet, Currency.POINTS);
		gui.open();
	}
}
