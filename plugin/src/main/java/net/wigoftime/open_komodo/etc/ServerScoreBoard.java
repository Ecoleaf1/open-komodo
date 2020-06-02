package net.wigoftime.open_komodo.etc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import net.md_5.bungee.api.ChatColor;
import net.wigoftime.open_komodo.chat.MessageFormat;

abstract public class ServerScoreBoard 
{
	
	//private static Scoreboard board;
	private static final String[] text = 
	{
		ChatColor.translateAlternateColorCodes('&', "&e————————————"),
		"",
		ChatColor.translateAlternateColorCodes('&', "   &7Coins: &e$C"),
		ChatColor.translateAlternateColorCodes('&', "   &7Points: &9$T"),
		" ",
		ChatColor.translateAlternateColorCodes('&', "   &7Rank: $G"),
		ChatColor.translateAlternateColorCodes('&', "   &7Role: $W"),
		ChatColor.translateAlternateColorCodes('&', "   &7Name: $N"),
		ChatColor.translateAlternateColorCodes('&', "&6&lINFO:"),
		ChatColor.translateAlternateColorCodes('&', "&e———————————— ")
	};
	
	public static void add(Player player)
	{
		// Create  new Scoreboard
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		
		// Register object to write on sidebar
		Objective obj = board.registerNewObjective("Sidebar", "", 
			ChatColor.translateAlternateColorCodes('&', "&e&lWoT &e&l• &b&lOpen &a&lKomodo")	
			);
		
		// Set object to display on the sidebar
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		// Loop through each text to add on the sidebar
		for (int i = 0; i < text.length; i++)
		{
			Score score = obj.getScore(MessageFormat.format(player, text[i]));
			score.setScore(i);
		}
		
		player.setScoreboard(board);
	}
	
}
