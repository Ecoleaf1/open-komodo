package net.wigoftime.open_komodo.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.objects.FriendProfile;

public class FriendsList {

	public static final String title = ChatColor.translateAlternateColorCodes('&', "&lFriends List");
	
	private static final ItemStack friendItem = new ItemStack(Material.PLAYER_HEAD);
	private static final String onlineStatus = ChatColor.translateAlternateColorCodes('&', "&2&lOnline");
	private static final String offlineStatus = ChatColor.translateAlternateColorCodes('&', "&4&lOffline");
	
	public static final String nextButtonLabel = ChatColor.translateAlternateColorCodes('&', "&lNext");
	public static final Material nextButtonMaterial = Material.ARROW;
	
	public static final String backButtonLabel = ChatColor.translateAlternateColorCodes('&', "&lBack");
	public static final Material backButtonMaterial = Material.ARROW;
	
	private static final int pageSlots = 35;
	private static HashMap<Player, Integer> pages = new HashMap<Player, Integer>();
	
	public static void open(Player player, byte pageType) {
		Inventory gui = Bukkit.createInventory(null, 54,title);
		
		if (!pages.containsKey(player))
			pages.put(player, 1);
		
			if (pageType == 0)
				pages.replace(player, 1);
		
			if (pageType == 1)
				pages.replace(player, pages.get(player)+1);
			
			if (pageType == 2)
				pages.replace(player, pages.get(player)-1);
		
		int page = pages.get(player);
		int friendsIndex = pageSlots * (page - 1);
		
		List<FriendProfile> friends = PlayerConfig.getFriends(player);
		
		boolean hasNext = friends.size() > (page * pageSlots) ? true : false;
		boolean hasPast = page > 1 ? true : false;
		
		int slotIndex = 0;
		for (int i = friendsIndex;i<friends.size(); i++) {
			if (slotIndex <= pageSlots) {
				ItemStack item = friendItem;
				SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
				
				itemMeta.setDisplayName(friends.get(i).getUsername());
				itemMeta.setOwningPlayer(Bukkit.getOfflinePlayer(friends.get(i).getUUID()));
				
				List<String> lore = new ArrayList<String>();
				lore.add(friends.get(i).isOnline() ? onlineStatus : offlineStatus);
				itemMeta.setLore(lore);
				
				item.setItemMeta(itemMeta);
				
				gui.setItem(slotIndex, item);
			} else {
				break;
			}
				
			slotIndex = slotIndex + 1;
		}
		
		if (hasNext) {
			ItemStack item = new ItemStack(nextButtonMaterial);
			ItemMeta itemMeta = item.getItemMeta();
			
			itemMeta.setDisplayName(nextButtonLabel);
			
			item.setItemMeta(itemMeta);
			
			gui.setItem(53, item);
		}
		
		if (hasPast) {
			ItemStack item = new ItemStack(backButtonMaterial);
			ItemMeta itemMeta = item.getItemMeta();
			
			itemMeta.setDisplayName(backButtonLabel);
			item.setItemMeta(itemMeta);
			
			
			gui.setItem(45, item);
		}
		
		player.openInventory(gui);
	}
	
}
