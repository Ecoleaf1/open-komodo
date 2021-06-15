package net.wigoftime.open_komodo.etc;

import net.md_5.bungee.api.ChatColor;
import net.wigoftime.open_komodo.chat.MessageFormat;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.scoreboard.*;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
	
	public static void add(@NotNull CustomPlayer customPlayer)
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
		
		Team team = customPlayer.isAfk() ? getAFKTeam(scoreboard, customPlayer) : getTeam(scoreboard, customPlayer);
		
		team.addEntry(customPlayer.getUniqueId().toString());
		customPlayer.getPlayer().setScoreboard(scoreboard);
		
		
		syncToOthers(customPlayer);
	}
	
	private static void syncToOthers(@NotNull CustomPlayer playerJoined) {
		for (CustomPlayer playerIndex : CustomPlayer.getOnlinePlayers()) {
			if (playerIndex == playerJoined) continue;
			
			Scoreboard currentScoreboard = playerIndex.getPlayer().getScoreboard();
			Team team = playerJoined.isAfk() ? getAFKTeam(currentScoreboard, playerJoined) : getTeam(currentScoreboard, playerJoined);
			team.addEntry(playerJoined.getPlayer().getDisplayName());
			
			playerIndex.getPlayer().setScoreboard(currentScoreboard);
		}
	}
	
	public static void sync(@NotNull CustomPlayer playerJoined) {
		Scoreboard currentScoreboard = playerJoined.getPlayer().getScoreboard();
		
		for (CustomPlayer playerIndex : CustomPlayer.getOnlinePlayers()) {
			if (playerIndex == playerJoined) continue;
			Team team = playerIndex.isAfk() ? getAFKTeam(currentScoreboard, playerIndex) : getTeam(currentScoreboard, playerIndex);
			team.addEntry(playerIndex.getPlayer().getDisplayName());
		}

		playerJoined.getPlayer().setScoreboard(currentScoreboard);
	}
	
	private static @Nullable Team getTeam(@NotNull Scoreboard playerScoreBoard, @NotNull CustomPlayer toSyncPlayer) {
		Team team = playerScoreBoard.getTeam(toSyncPlayer.getRank().getName());
		
		if (team == null) team = playerScoreBoard.registerNewTeam(toSyncPlayer.getRank().getName());
		
		team.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
		
		if (toSyncPlayer.getRank() != null) team.setPrefix(toSyncPlayer.getRank().getPrefix());
		return team;
	}

	private static @Nullable Team getAFKTeam(@NotNull Scoreboard playerScoreBoard, @NotNull CustomPlayer toSyncPlayer) {
		Team team = playerScoreBoard.getTeam(".AFKTEAM");

		if (team == null) team = playerScoreBoard.registerNewTeam(".AFKTEAM");

		team.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);

		team.setPrefix(ChatColor.GOLD + "" + ChatColor.BOLD + "AFK " + ChatColor.GRAY);
		return team;
	}
}
