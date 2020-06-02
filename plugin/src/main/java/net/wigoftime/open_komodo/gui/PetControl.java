package net.wigoftime.open_komodo.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

abstract public class PetControl 
{
	public static final String title = "Pet Control";
	public static final String cancelButton = ChatColor.translateAlternateColorCodes('&', "&lRemove");
	public static final int cancelID = 1;
	
	public static final String changeNameButton = ChatColor.translateAlternateColorCodes('&', "&lSet Name");
	public static final int changeNameID = 2;
	
	public static final String mountNameButton = ChatColor.translateAlternateColorCodes('&', "&lMount");
	public static final int mountID = 3;
	
	public static void create(Player player)
	{
		Inventory gui = Bukkit.createInventory(null, 9, title);
		
		ItemStack remove = new ItemStack(Material.BARRIER);
		ItemMeta removeMeta = remove.getItemMeta();
		
		removeMeta.setDisplayName(cancelButton);
		removeMeta.setCustomModelData(cancelID);
		
		remove.setItemMeta(removeMeta);
		
		ItemStack changeName = new ItemStack(Material.NAME_TAG);
		ItemMeta cnMeta = changeName.getItemMeta();
		
		cnMeta.setDisplayName(changeNameButton);
		cnMeta.setCustomModelData(changeNameID);
		
		changeName.setItemMeta(cnMeta);
		
		ItemStack mountButton = new ItemStack(Material.SADDLE);
		ItemMeta mb = mountButton.getItemMeta();
		
		mb.setDisplayName(mountNameButton);
		mb.setCustomModelData(mountID);
		
		mountButton.setItemMeta(mb);
		
		gui.addItem(remove);
		gui.addItem(changeName);
		gui.addItem(mountButton);
		
		player.openInventory(gui);
	}
}
