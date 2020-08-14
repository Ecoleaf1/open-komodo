package net.wigoftime.open_komodo.gui;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.wigoftime.open_komodo.config.WorldInventoryConfig;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.sql.SQLManager;

public class BagGui extends CustomGUI {
	private final int bagID;
	private final boolean isCreated;
	private ItemStack[] items;

	public BagGui(CustomPlayer openerCustomPlayer, int bagID) {
		super(openerCustomPlayer, null, Bukkit.createInventory(null, 27, "Bag"));
		this.bagID = bagID;
		isCreated = create();
	}
	
	public void open(InventoryClickEvent clickEvent) {
		if (!isCreated) { displayCreationError(); return; }
		
		PrintConsole.test("came thro open");
		
		this.open();
	}
	
	public void closed() {
		super.closed();
		items = gui.getContents();
		if (!isCreated) return;
		save();
	}
	
	public void clicked(InventoryClickEvent clickEvent) {}
	
	private void displayCreationError() {
		opener.getPlayer().sendMessage(String.format("%sERROR: Cannot be opened.", ChatColor.DARK_RED));
	}
	
	private boolean create() {
		List<ItemStack> items = getItems();
		if (items.size() == 0) return false;
		
		byte index = 0;
		for (ItemStack item : items) gui.setItem(index++, item);
		return true;
	}
	
	private void save() {
		if (SQLManager.isEnabled()) SQLManager.setBagInventory(opener.getUniqueId(), opener.getPlayer().getWorld().getName(), bagID, Arrays.asList(items));
		else WorldInventoryConfig.setInventory(opener.getPlayer(), opener.getPlayer().getWorld(), items);
	}
	
	private List<ItemStack> getItems() {
		if (SQLManager.isEnabled()) return SQLManager.getBagInventory(opener.getUniqueId(), opener.getPlayer().getWorld().getName(), bagID);
		else return Arrays.asList(WorldInventoryConfig.getInventory(opener.getPlayer(), bagID));
	}
}
