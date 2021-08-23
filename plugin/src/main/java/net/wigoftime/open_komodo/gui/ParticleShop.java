package net.wigoftime.open_komodo.gui;

import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
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

public class ParticleShop extends CustomGUI {
	private static final CustomItem particleKey = CustomItem.getCustomItem(80);
	private static final CustomItem styleKey = CustomItem.getCustomItem(81);

	public ParticleShop(@NotNull CustomPlayer openerCustomPlayer) {
		super(openerCustomPlayer, Permissions.particleAccess, Bukkit.createInventory(null,27, "Particle Shop"));

		gui.setItem(11, particleKey.getItem());
		gui.setItem(15, styleKey.getItem());
	}

	
	public void clicked(InventoryClickEvent clickEvent) {
		clickEvent.setCancelled(true);

		if (clickEvent.getCurrentItem().equals(particleKey.getItem())) {
			CustomGUI buyConfirm;
			if (particleKey.getCoinPrice() < 0)
				buyConfirm = new BuyConfirm(opener, particleKey, Currency.POINTS, particleKey.getPermission());
			else
				buyConfirm = new BuyConfirm(opener, particleKey, Currency.COINS, particleKey.getPermission());
			buyConfirm.open();
			return;
		} else if (clickEvent.getCurrentItem().equals(styleKey.getItem())) {
			CustomGUI buyConfirm;
			if (styleKey.getCoinPrice() < 0)
				buyConfirm = new BuyConfirm(opener, styleKey, Currency.POINTS, styleKey.getPermission());
			else
				buyConfirm = new BuyConfirm(opener, styleKey, Currency.COINS, styleKey.getPermission());
			buyConfirm.open();
		}
	}
}
