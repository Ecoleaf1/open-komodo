package net.wigoftime.open_komodo.gui;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FurnitureMenu extends CustomItemMenuType1 {

	public FurnitureMenu(CustomPlayer openerCustomPlayer, Permission requiredPermission, String fileName) {
		super(openerCustomPlayer, Permissions.accessFurnitureMenu, getFurniture(fileName), "Furniture Menu", 54, (byte)0);
	}

	
	public void clicked(@NotNull InventoryClickEvent clickEvent) {
		super.clicked(clickEvent);
		
		ItemStack clickedItem = clickEvent.getCurrentItem();
		
		if (clickedItem.getType() != Material.INK_SAC) return;
		if (clickEvent.getClickedInventory() != clickEvent.getInventory()) return;
		
		clickEvent.getWhoClicked().getInventory().addItem(clickedItem);
	}
	
	private static @NotNull List<ItemStack> getFurniture(String fileName) {
		File menuFile = new File(String.format("%s/gui/furniture-menu/%s.yml",Main.dataFolderPath,fileName));
		
		YamlConfiguration menuConfig = YamlConfiguration.loadConfiguration(menuFile);
		List<Integer> ids = menuConfig.getIntegerList("ID");
		
		List<ItemStack> items = new ArrayList<ItemStack>(ids.size());
		for (int idIndex : ids)
			items.add(CustomItem.getCustomItem(idIndex).getItem());
		
		return items;
	}
	
	public static boolean isValidFurnitureMenu(String fileName) {
		File shopPage = new File(String.format("%s/gui/furniture-menu/%s.yml",Main.dataFolderPath,fileName));
		return shopPage.exists();
	}
}
