package net.wigoftime.open_komodo.actions;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

abstract public class Sit 
{
	private static final String notStanding = ChatColor.translateAlternateColorCodes('&', "&4Please stand on a block to sit down");

	public static void sit(@NotNull Player player)
	{
		// Get Player location
		Location loc = player.getLocation();
		loc.setY(loc.getY() - 1);
		
		// Get player's world
		World world = Bukkit.getWorld(player.getWorld().getUID());
		
		if (loc.getBlock().getType() == Material.AIR)
		{
			player.sendMessage(notStanding);
			return;
		}
		
		// Get location on the seat
		Location sitLocation = new Location(world, player.getLocation().getX(), player.getLocation().getY() - 0.5, player.getLocation().getZ());
		
		// Spawn arrow to be sat on
		Entity arrow = world.spawnEntity(sitLocation, EntityType.ARROW);
		arrow.setTicksLived(Integer.MAX_VALUE);
		// add player to seat
		arrow.addPassenger(player);
		
		//Packet packet = new PacketPlayOutBe
	}
}
