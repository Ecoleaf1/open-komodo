package net.wigoftime.open_komodo.actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.Main;

abstract public class Rules 
{
	private static final String[] rules = {"No 18+ stuff", "No swearing (no discrimination and words behind anything inappropriate).","No bypassing the word filter to use forbidden words.", "Have fun!", "No harassment", "Please use common sense"};
	
	/*
	private static final File rulesFolder = new File(Main.config.getAbsolutePath()+"/rulesAccepted");
	private static Set<UUID> accepted = new HashSet<UUID>(Bukkit.getServer().getMaxPlayers());
	*/
	
	public static void display(CommandSender player)
	{
		// Get stringbuilder to display rules
		StringBuilder sb = new StringBuilder();
		sb.append(ChatColor.translateAlternateColorCodes('&', "&b&lRules:\n"));
		
		// Look through each rules on server
		for (int i = 0; i < rules.length; i++)
		{
			if (i + 1 < rules.length)
				sb.append(ChatColor.DARK_AQUA + "    " + (i+1) + ". " + rules[i]+"\n");
			else
				sb.append(ChatColor.DARK_AQUA + "    " + (i+1) + ". " + rules[i]);
		}
		
		
		// Display rules to player
		player.sendMessage(sb.toString());
	}
}
