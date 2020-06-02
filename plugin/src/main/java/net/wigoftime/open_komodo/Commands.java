package net.wigoftime.open_komodo;

import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import net.md_5.bungee.api.ChatColor;
import net.wigoftime.open_komodo.chat.Emote;

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
		
		/*
		if (args[0].equalsIgnoreCase("/npcspawn"))
			if (args.length > 1) 
				if (args.length > 2)
					NPCSpawner.spawn(e.getPlayer().getLocation(), args[1], args[2]);
				else
					NPCSpawner.spawn(e.getPlayer().getLocation(), args[1], null); */
		
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
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lHelp:&e&l\nFor info about ranks, type in /rank help\nFor info about points, type in /money help\n"
					+ "/msg (Player) (message) to message someone!\n/tpa (player) to send a teleport request!"));
			e.setCancelled(true);
			return;
		}
	}
	
}
