package net.wigoftime.open_komodo.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.ItemType;

public class FurnitureMenu extends CustomItemMenuType1 {

	public FurnitureMenu(CustomPlayer openerCustomPlayer, Permission requiredPermission, String fileName) {
		super(openerCustomPlayer, Permissions.accessFurnitureMenu, getFurniture(fileName), "Furniture Menu", 54, (byte)0);
	}

	
	public void clicked(InventoryClickEvent clickEvent) {
		super.clicked(clickEvent);
		
		ItemStack clickedItem = clickEvent.getCurrentItem();
		
		if (clickedItem.getType() != Material.INK_SAC) return;
		if (clickEvent.getClickedInventory() != clickEvent.getInventory()) return;
		
		clickEvent.getWhoClicked().getInventory().addItem(clickedItem);
	}
	
	private static List<ItemStack> getFurniture(String fileName) {
		File menuFile = new File(String.format("%s/gui/furniture-menu/%s.yml",Main.dataFolderPath,fileName));
		
		YamlConfiguration menuConfig = YamlConfiguration.loadConfiguration(menuFile);
		List<Integer> ids = menuConfig.getIntegerList("ID");
		
		List<ItemStack> items = new ArrayList<ItemStack>(ids.size());
		for (int idIndex : ids)
			items.add(CustomItem.getCustomItem(idIndex).getItem());
		
		return items;
	}
}
