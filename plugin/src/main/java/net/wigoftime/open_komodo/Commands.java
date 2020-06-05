package net.wigoftime.open_komodo;

import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import net.wigoftime.open_komodo.chat.Emote;
import net.wigoftime.open_komodo.config.WorldInventoryConfig;

abstract public class Commands 
{
	
	public static void commands(PlayerCommandPreprocessEvent e) 
	{
		// Get Player
		Player player = e.getPlayer();
		
		
		// The command arguments, first string is the actual command
		String[] args = e.getMessage().split(" ");
		
		// Get the command
		String command = args[0];
		
		if (args[0].equalsIgnoreCase("/test2")) {
			Calendar cal = Calendar.getInstance();
			e.getPlayer().sendMessage(cal.getTime().toString());
			
			return;
		}
		
		if (command.equalsIgnoreCase("/memory"))
		{
			Long freeMemory = Runtime.getRuntime().freeMemory();
			Long totalMemory = Runtime.getRuntime().totalMemory();
			
			freeMemory = freeMemory / 1000;
			freeMemory = freeMemory / 1000;
			
			totalMemory = totalMemory / 1000;
			totalMemory = totalMemory / 1000;
			
			player.sendMessage(String.format("Free Memory: %d/%d", freeMemory, totalMemory));
		}
		
		if (command.equalsIgnoreCase("/help") || command.equalsIgnoreCase("/minecraft:help"))
		{
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bHelp:&3\nFor info about ranks, type in /rank help\nFor info about points, type in /money help\n"
					+ "/msg (Player) (message) to message someone!\n/tpa (player) to send a teleport request!\n/home help for setting homes!"));
			e.setCancelled(true);
			return;
		}
	}
	
}
