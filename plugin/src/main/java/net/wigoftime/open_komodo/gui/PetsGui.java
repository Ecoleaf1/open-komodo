package net.wigoftime.open_komodo.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.objects.Pet;

abstract public class PetsGui 
{
	public static final String title = "Pet Menu";
	
	public static void create(Player player)
	{
		if (!player.hasPermission(Permissions.petAccess))
			return;
		
		Inventory inv = Bukkit.createInventory(null, 45, title);
		
		Set<Pet> pets = Pet.getPets();
		
		// Loop through each pet to display on gui
		for (Pet p : pets)
		{
			PrintConsole.test("P NAME" + p.getDisplayName());
			
			ItemStack item = new ItemStack(Material.GHAST_SPAWN_EGG);
			ItemMeta meta = item.getItemMeta();
			
			// Set Display Name on item
			meta.setDisplayName(p.getDisplayName());
			
			// Get Prices
			int pPrice = p.getPrice(Currency.POINTS);
			int cPrice = p.getPrice(Currency.COINS);
			
			List<String> lore = new ArrayList<String>();
			
			if (pPrice > -1 && cPrice > -1)
				lore.add(0, String.format("Price: %d points or %d coins", pPrice, cPrice));
			else if (pPrice > -1)
				lore.add(0, String.format("Price: %d points", pPrice));
			else if (cPrice > -1)
				lore.add(0, String.format("Price: %d coins", cPrice));
			
			// Set Lore
			meta.setLore(lore);
			
			// Assigning IDs to items to track
			meta.setCustomModelData(p.getID());
			
			// Save Changes
			item.setItemMeta(meta);
			
			// Add Item
			inv.addItem(item);
		}
		
		player.openInventory(inv);
	}
}
