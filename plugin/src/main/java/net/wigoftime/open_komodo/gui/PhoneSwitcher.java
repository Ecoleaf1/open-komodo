package net.wigoftime.open_komodo.gui;

import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.ItemType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PhoneSwitcher extends CustomGUI {
	public PhoneSwitcher(@NotNull CustomPlayer openerCustomPlayer) {
		super(openerCustomPlayer, new Permission("openkomodo.abilities.phoneswitching"), Bukkit.createInventory(null, 9, "Phones"));
		create();
	}

	@Override
	public void clicked(InventoryClickEvent clickEvent) {
		clickEvent.setCancelled(true);
		ItemStack clickedItem = clickEvent.getCurrentItem();
		if (clickedItem.getType() != Material.INK_SAC)
			return;

		CustomItem customItem = CustomItem.getCustomItem(clickedItem.getItemMeta().getCustomModelData());
		if (customItem == null)
			return;

		if (opener.hasItem(customItem.getID(), ItemType.PHONE)) {
			opener.setActivePhone(customItem);
			return;
		}

		BuyConfirm gui;
		if (customItem.getPointPrice() > -1)
			gui = new BuyConfirm(opener, customItem, Currency.POINTS, customItem.getPermission());
		else if (customItem.getCoinPrice() > -1)
			gui = new BuyConfirm(opener, customItem, Currency.COINS, customItem.getPermission());
		else
			return;

		gui.open();
	}

	private void create() {
		List<CustomItem> phones = CustomItem.getCustomItem(ItemType.PHONE);

		Iterator<CustomItem> iterator = phones.iterator();
		for (int index = 0; index < gui.getSize(); index++)
			if (iterator.hasNext()) {
				CustomItem itemCustomItem = iterator.next();
				ItemStack phoneItemStack = new ItemStack(itemCustomItem.getItem());

				List<String> lore = new ArrayList<String>(1);

				if (itemCustomItem.getPointPrice() > 0 && itemCustomItem.getCoinPrice() > 0)
					lore.add(String.format("%sPrice: %d Points or %d Coins", ChatColor.DARK_AQUA, itemCustomItem.getPointPrice(), itemCustomItem.getCoinPrice()));
				else if (itemCustomItem.getPointPrice() > -1)
					lore.add(String.format("%sPrice: %d Points", ChatColor.DARK_AQUA, itemCustomItem.getPointPrice()));
				else if (itemCustomItem.getCoinPrice() > -1)
					lore.add(String.format("%sPrice: %d Coins", ChatColor.DARK_AQUA, itemCustomItem.getCoinPrice()));

				ItemMeta meta = phoneItemStack.getItemMeta();
				meta.setLore(lore);
				phoneItemStack.setItemMeta(meta);

				gui.setItem(index, phoneItemStack);
			}
			else
				break;
	}
}
