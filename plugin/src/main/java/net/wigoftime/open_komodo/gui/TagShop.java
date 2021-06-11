package net.wigoftime.open_komodo.gui;

import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.ItemType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TagShop extends CustomGUI {
	private static final ItemStack nextItem = new ItemStack(Material.ARROW);
	private static final ItemStack backItem = new ItemStack(Material.ARROW);
	private static final ItemStack border = new ItemStack(Material.WHITE_STAINED_GLASS_PANE,1);;
	private static final int pageSlots = 35;
	private int page = 1;
	
	
	public static void setup() {
		{
			ItemMeta itemMeta = nextItem.getItemMeta();
			itemMeta.setDisplayName("Next");
			
			nextItem.setItemMeta(itemMeta);
		}
		
		{
			ItemMeta itemMeta = backItem.getItemMeta();
			itemMeta.setDisplayName("Back");
			
			backItem.setItemMeta(itemMeta);
		}
		{
			ItemMeta itemMeta = border.getItemMeta();
			itemMeta.setDisplayName(" ");
			
			border.setItemMeta(itemMeta);
		}
	}
	
	public TagShop(CustomPlayer player) {
		super(player, null, Bukkit.createInventory(null, 54,"Tag Shop"));
		
		// Adding Border Panels
		gui.setItem(44, border);
		gui.setItem(43, border);
		gui.setItem(42, border);
		gui.setItem(41, border);
		gui.setItem(40, border);
		gui.setItem(39, border);
		gui.setItem(38, border);
		gui.setItem(37, border);
		gui.setItem(36, border);
		
		gotoPage((byte)1);
	}
	
	private void gotoPage(byte page) {
		this.page = page;
		
		List<CustomItem> showTags;
		List<CustomItem> ownedTags = opener.getItems(ItemType.TAG);
		List<CustomItem> shopTags = CustomItem.getCustomItem(ItemType.TAG);
		
		// Get amount of tags that exist
		int amount = shopTags.size();
		
		// Set size for array with the amount variable
		showTags = new ArrayList<CustomItem>(amount);
		
		int shopIndex = 0;
		for (CustomItem cs : shopTags) {	
			if (shopIndex < (page - 1) * pageSlots) {
				shopIndex++;
				return;
			}
			shopIndex++;
				
			if (cs.getPointPrice() < 0)
				continue;
			
			boolean alreadyOwned = false;
			for (CustomItem cs2 : ownedTags)
				if (cs2.getItem().getItemMeta().getCustomModelData() == cs.getItem().getItemMeta().getCustomModelData()) {
					alreadyOwned = true;
					break;
				}
			
			if (alreadyOwned)
				continue;
			
			showTags.add(cs);
		}
		
		//boolean hasNext = showTags.size() > (page * pageSlots) ? true : false;
		boolean hasNext = showTags.size() > (page * pageSlots) ? true : false;
		if (hasNext)
			gui.setItem(53, nextItem);
		else
			gui.setItem(53, new ItemStack(Material.AIR));
		
		// If can go back a previous page
		if (page > 1) {
			
			
			gui.setItem(45, backItem);
		} else
			gui.setItem(45, new ItemStack(Material.AIR));
		
		int slotIndex = 0;
		
		for (CustomItem ci : showTags) {
			if (slotIndex > pageSlots)
				break;
			
			// Get Prices
			int p;
			
			// Make the item description
			List<String> lore = new ArrayList<String>();
			if (ci.getPointPrice() > -1 && ci.getCoinPrice() > -1)
				lore.add(String.format("Cost: %d %s or %d %s", ci.getPointPrice(), "Points", ci.getCoinPrice(), "Coins"));
			else
			{
				if (ci.getPointPrice() > -1)
					p = ci.getPointPrice();
				else
					p = ci.getCoinPrice();
				
				lore.add(String.format("Cost: %d %s", p, ci.getPointPrice() > -1 ? "Points" : "Coins"));
			}
			
			// Info about Item
			ItemStack is = ci.getItem();
			ItemMeta im = is.getItemMeta();
			
			// Save the item description
			im.setLore(lore);
			
			// Save changes to Item Meta
			is.setItemMeta(im);
			
			gui.setItem(slotIndex++, is);
		}
	}

	@Override
	public void clicked(@NotNull InventoryClickEvent clickEvent) {
		PrintConsole.test("clicked");
		
		clickEvent.setCancelled(true);
		ItemStack clickedItem = clickEvent.getCurrentItem();
		
		if (clickedItem.equals(nextItem)) {
			gotoPage((byte) ++page);
			return;
		}
		
		if (clickedItem.equals(backItem)) {
			gotoPage((byte) --page);
			return;
		}
		
		if (clickEvent.getSlot() > pageSlots)
			return;
			
		CustomItem itemTag = CustomItem.getCustomItem(clickedItem.getItemMeta().getCustomModelData());
		
		Currency currency;
		if (itemTag.getPointPrice() > -1)
			currency = Currency.POINTS;
		else
			currency = Currency.COINS;
		
		CustomGUI gui = new BuyConfirm(opener, itemTag, currency, itemTag.getPermission());
		gui.open();
	}
}
