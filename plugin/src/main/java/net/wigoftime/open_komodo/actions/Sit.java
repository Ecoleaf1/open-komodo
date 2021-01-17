package net.wigoftime.open_komodo.actions;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import net.md_5.bungee.api.ChatColor;
import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.PrintConsole;

abstract public class Sit 
{
	private static final String notStanding = ChatColor.translateAlternateColorCodes('&', "&4Please stand on a block to sit down");

	public static void sit(Player player)
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
