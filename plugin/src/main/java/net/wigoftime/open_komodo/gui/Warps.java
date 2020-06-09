package net.wigoftime.open_komodo.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class Warps 
{
	
	private static enum warps {AIRPORT, HIGH_SCHOOL, TOWN_HALL, BEACH, CRUISE_SHIP};

	public static final String guiName = ChatColor.translateAlternateColorCodes('&', "&cWarps");
	
	private static ItemStack airport;
	private static String airportName = ChatColor.translateAlternateColorCodes('&', "&lAirport");
	
	private static ItemStack hs;
	private static String hsName = ChatColor.translateAlternateColorCodes('&', "&lKomodo High School");
	
	private static ItemStack th;
	private static String thName = ChatColor.translateAlternateColorCodes('&', "&r&lTown Hall");
	
	private static ItemStack beach;
	private static String beachName = ChatColor.translateAlternateColorCodes('&', "&r&lBeach");
	
	private static ItemStack cruiseShip;
	private static String cruiseShipName = ChatColor.translateAlternateColorCodes('&', "&r&lCruise Ship");
	
	public static void open(CustomPlayer player) 
	{
		if (player.isBuilding())
		{
			player.getPlayer().sendMessage(CustomPlayer.buildingError);
			return;
		}
		
		Inventory gui = Bukkit.createInventory(null, 27,guiName);
		
		gui.setItem(10, hs);
		gui.setItem(11, th);
		gui.setItem(12, beach);
		gui.setItem(13, cruiseShip);
		gui.setItem(14, airport);
		
		player.getPlayer().openInventory(gui);
	}
	
	public static void clicked(Player player, ItemStack item) 
	{
		
		if (item.getType() == hs.getType() && item.getItemMeta().getDisplayName().equals(hs.getItemMeta().getDisplayName())) {
			teleport(player, warps.HIGH_SCHOOL);
			player.closeInventory();
			return;
		}
		
		if (item.getType() == th.getType() && item.getItemMeta().getDisplayName().equals(th.getItemMeta().getDisplayName())) {
			teleport(player, warps.TOWN_HALL);
			player.closeInventory();
			return;
		}
		
		if (item.getType() == beach.getType() && item.getItemMeta().getDisplayName().equals(beach.getItemMeta().getDisplayName())) {
			teleport(player, warps.BEACH);
			player.closeInventory();
			return;
		}
		
		if (item.getType() == cruiseShip.getType() && item.getItemMeta().getDisplayName().equals(cruiseShip.getItemMeta().getDisplayName())) 
		{
			teleport(player, warps.CRUISE_SHIP);
			player.closeInventory();
			return;
		}
		
		if (item.getType() == airport.getType() && item.getItemMeta().getDisplayName().equals(airport.getItemMeta().getDisplayName())) {
			teleport(player, warps.AIRPORT);
			player.closeInventory();
			return;
		}
		
	}
	
	public static void teleport(Player player, warps place) 
	{
		
		if (place == warps.HIGH_SCHOOL) 
		{
			player.teleport(new Location(Main.world, 302, 68, -160,(float)137, (float) -1.1));
			return;
		}
		
		if (place == warps.TOWN_HALL) 
		{
			player.teleport(new Location(Main.world, -257, 62, -237,(float)123.5, (float) 4.1));
			return;
		}
		
		if (place == warps.BEACH) 
		{
			player.teleport(new Location(Main.world, -93, 62, -386,(float)179, (float) 2.2));
			return;
		}
		
		if (place == warps.CRUISE_SHIP) 
		{
			player.teleport(new Location(Main.world, 108, 68, -382,(float)179, (float) 2.2));
			return;
		}
		
		if (place == warps.AIRPORT) {
			player.teleport(new Location(Main.world, -943, 69, 94));
			return;
		}
		
	}
	
	public static void setup() 
	{
		airport = new ItemStack(Material.INK_SAC); 
		{
			ItemMeta meta = airport.getItemMeta();
			meta.setDisplayName(airportName);
			meta.setCustomModelData(-7);
			
			airport.setItemMeta(meta);
		}
		
		hs = new ItemStack(Material.INK_SAC); 
		{
			ItemMeta meta = hs.getItemMeta();
			meta.setDisplayName(hsName);
			meta.setCustomModelData(-4);
			
			hs.setItemMeta(meta);
		}
		
		th = new ItemStack(Material.INK_SAC); 
		{
			ItemMeta meta = th.getItemMeta();
			meta.setDisplayName(thName);
			meta.setCustomModelData(-5);
			
			th.setItemMeta(meta);
		}
		
		beach = new ItemStack(Material.INK_SAC); 
		{
			ItemMeta meta = beach.getItemMeta();
			meta.setDisplayName(beachName);
			meta.setCustomModelData(-6);
			
			beach.setItemMeta(meta);
		}
		
		cruiseShip = new ItemStack(Material.INK_SAC); 
		{
			ItemMeta meta = cruiseShip.getItemMeta();
			meta.setDisplayName(cruiseShipName);
			meta.setCustomModelData(-7);
			
			cruiseShip.setItemMeta(meta);
		}
		
	}
	
}
