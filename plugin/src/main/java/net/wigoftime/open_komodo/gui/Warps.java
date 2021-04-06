package net.wigoftime.open_komodo.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class Warps extends CustomGUI
{
	public static final String title = ChatColor.RED + "Warps";
	
	private static enum warps {AIRPORT, HIGH_SCHOOL, TOWN_HALL, BEACH, CRUISE_SHIP};
	
	// All Warp Icons
	private static ItemStack airportItemStackIcon;
	private static ItemStack highschoolItemStackIcon;
	private static ItemStack townHallItemStackIcon;
	private static ItemStack beachItemStackIcon;
	private static ItemStack cruiseShipItemStackIcon;
	
	public Warps(CustomPlayer opener) {
		super(opener, null, Bukkit.createInventory(null, 27, title));
		//Inventory gui = Bukkit.createInventory(null, 27,title);
		gui.setContents(getIcons(gui.getSize()));
		
		return;
	}
	
	private static ItemStack[] getIcons(int size) {
		ItemStack[] itemStacks = new ItemStack[size];
		
		itemStacks[10] = highschoolItemStackIcon;
		itemStacks[11] = townHallItemStackIcon;
		itemStacks[12] = beachItemStackIcon;
		itemStacks[13] = cruiseShipItemStackIcon;
		itemStacks[14] = airportItemStackIcon;
		
		return itemStacks;
	}
	
	public void clicked(InventoryClickEvent clickEvent) {
		clickEvent.setCancelled(true);
		final ItemStack clickedItemStack = clickEvent.getCurrentItem();
		
		if (!clickedItemStack.getItemMeta().hasCustomModelData())
			return;
			
		int clickedItemID = clickedItemStack.getItemMeta().getCustomModelData();
		
		// If clicked on HighSchool Warp Icon
		if (clickedItemID == highschoolItemStackIcon.getItemMeta().getCustomModelData()) {
			teleport(opener.getPlayer(), warps.HIGH_SCHOOL);
			opener.getPlayer().closeInventory();
			return;
		}
		
		// If clicked on TownHall Warp Icon
		if (clickedItemID == townHallItemStackIcon.getItemMeta().getCustomModelData()) {
			teleport(opener.getPlayer(), warps.TOWN_HALL);
			opener.getPlayer().closeInventory();
			return;
		}
		
		// If clicked on Beach Warp Icon
		if (clickedItemID == beachItemStackIcon.getItemMeta().getCustomModelData()) {
			teleport(opener.getPlayer(), warps.BEACH);
			opener.getPlayer().closeInventory();
			return;
		}
		
		// If clicked on CruiseShip Warp Icon
		if (clickedItemID == cruiseShipItemStackIcon.getItemMeta().getCustomModelData()) {
			teleport(opener.getPlayer(), warps.CRUISE_SHIP);
			opener.getPlayer().closeInventory();
			return;
		}
		
		// If clicked on Airport Warp Icon
		if (clickedItemID == airportItemStackIcon.getItemMeta().getCustomModelData()) {
			teleport(opener.getPlayer(), warps.AIRPORT);
			opener.getPlayer().closeInventory();
			return;
		}
		
	}
	
	public static void teleport(Player player, warps place) {
		switch(place) {
		case HIGH_SCHOOL:
			player.teleport(new Location(Main.world, 377, 68, -220,(float)-22, (float) 0.6));
			return;
		case TOWN_HALL: 
			player.teleport(new Location(Main.world, -257, 62, -237,(float)123.5, (float) 4.1));
			return;
		case BEACH:
			player.teleport(new Location(Main.world, -93, 62, -386,(float)179, (float) 2.2));
			return;
		case CRUISE_SHIP:
			player.teleport(new Location(Main.world, 108, 68, -382,(float)179, (float) 2.2));
			return;
		
		case AIRPORT:
			player.teleport(new Location(Main.world, -943, 69, 94));
			return;
		}
	}
	
	public static void setup() {
		airportItemStackIcon = new ItemStack(Material.INK_SAC); {
			ItemMeta itemMeta = airportItemStackIcon.getItemMeta();
			itemMeta.setDisplayName(ChatColor.BOLD + "Airport");
			itemMeta.setCustomModelData(-8);
			
			airportItemStackIcon.setItemMeta(itemMeta);
		}
		
		highschoolItemStackIcon = new ItemStack(Material.INK_SAC); {
			ItemMeta itemMeta = highschoolItemStackIcon.getItemMeta();
			itemMeta.setDisplayName(ChatColor.BOLD + "Komodo High School");
			itemMeta.setCustomModelData(-4);
			
			highschoolItemStackIcon.setItemMeta(itemMeta);
		}
		
		townHallItemStackIcon = new ItemStack(Material.INK_SAC); {
			ItemMeta itemMeta = townHallItemStackIcon.getItemMeta();
			itemMeta.setDisplayName(ChatColor.BOLD + "Town Hall");
			itemMeta.setCustomModelData(-5);
			
			townHallItemStackIcon.setItemMeta(itemMeta);
		}
		
		beachItemStackIcon = new ItemStack(Material.INK_SAC); {
			ItemMeta itemMeta = beachItemStackIcon.getItemMeta();
			itemMeta.setDisplayName(ChatColor.BOLD + "Beach");
			itemMeta.setCustomModelData(-6);
			
			beachItemStackIcon.setItemMeta(itemMeta);
		}
		
		cruiseShipItemStackIcon = new ItemStack(Material.INK_SAC); {
			ItemMeta itemMeta = cruiseShipItemStackIcon.getItemMeta();
			itemMeta.setDisplayName(ChatColor.BOLD + "Cruise Ship");
			itemMeta.setCustomModelData(-7);
			
			cruiseShipItemStackIcon.setItemMeta(itemMeta);
		}
		
	}
	
}
