package net.wigoftime.open_komodo.etc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import alexh.Fluent.LinkedHashMap;
import net.md_5.bungee.api.ChatColor;
import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.chat.MessageFormat;
import net.wigoftime.open_komodo.objects.CustomPlayer;

abstract public class ServerScoreBoard 
{
	//private static Scoreboard board;
	private static final String[] text = 
	{
		ChatColor.translateAlternateColorCodes('&', "&7   rp.wigoftime.net "),
		ChatColor.translateAlternateColorCodes('&', "&e&m━━━━━━━━━━━━━━━━━━"),
		"",
		ChatColor.translateAlternateColorCodes('&', "   &7Coins: &e$C"),
		ChatColor.translateAlternateColorCodes('&', "   &7Points: &9$T"),
		" ",
		ChatColor.translateAlternateColorCodes('&', "   &7Rank: $G"),
		ChatColor.translateAlternateColorCodes('&', "   &7Role: $W"),
		ChatColor.translateAlternateColorCodes('&', "   &7Name: $N"),
		ChatColor.translateAlternateColorCodes('&', "&6&lINFO:"),
		ChatColor.translateAlternateColorCodes('&', "&r&e&m━━━━━━━━━━━━━━━━━━━")
	};
	
	public static void add(CustomPlayer customPlayer)
	{	
		Scoreboard scoreboard = customPlayer.getPlayer().getScoreboard();
		
		for (Objective objectiveIndex : scoreboard.getObjectives())
			objectiveIndex.unregister();
		
		// Sidebar top to bottom
		Objective sidebarObjective = scoreboard.registerNewObjective("Sidebar", "", 
			ChatColor.translateAlternateColorCodes('&', "&e&lWoT &e&l• &b&lOpen &a&lKomodo")	
			);
		
		sidebarObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		for (int i = 0; i < text.length; i++) {
			Score score = sidebarObjective.getScore(MessageFormat.format(customPlayer, text[i]));
			score.setScore(i);
		}
		
		Team team = getTeam(scoreboard, customPlayer);
		
		team.addEntry(customPlayer.getUniqueId().toString());
		customPlayer.getPlayer().setScoreboard(scoreboard);
		
		
		syncToOthers(customPlayer);
	}
	
	private static void syncToOthers(CustomPlayer playerJoined) {
		for (CustomPlayer playerIndex : CustomPlayer.getOnlinePlayers()) {
			if (playerIndex == playerJoined) continue;
			
			Scoreboard currentScoreboard = playerIndex.getPlayer().getScoreboard();
			Team team = getTeam(currentScoreboard, playerJoined);
			team.addEntry(playerJoined.getPlayer().getDisplayName());
			
			playerIndex.getPlayer().setScoreboard(currentScoreboard);
		}
	}
	
	public static void sync(CustomPlayer playerJoined) {
		Scoreboard currentScoreboard = playerJoined.getPlayer().getScoreboard();
		
		for (CustomPlayer playerIndex : CustomPlayer.getOnlinePlayers()) {
			if (playerIndex == playerJoined) continue;
			Team team = getTeam(currentScoreboard, playerIndex);
			team.addEntry(playerIndex.getPlayer().getDisplayName());
			
		}
		
		playerJoined.getPlayer().setScoreboard(currentScoreboard);
	}
	
	private static Team getTeam(Scoreboard playerScoreBoard, CustomPlayer toSyncPlayer) {
		Team team = playerScoreBoard.getTeam(toSyncPlayer.getRank().getName());
		
		if (team == null) team = playerScoreBoard.registerNewTeam(toSyncPlayer.getRank().getName());
		
		team.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
		
		if (toSyncPlayer.getRank() != null) team.setPrefix(toSyncPlayer.getRank().getPrefix());
		return team;
	}
}
