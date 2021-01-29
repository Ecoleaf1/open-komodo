package net.wigoftime.open_komodo.etc;

import java.util.HashMap;
import java.util.LinkedList;
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
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		// Register object to write on sidebar
		Objective obj = scoreboard.registerNewObjective("Sidebar", "", 
			ChatColor.translateAlternateColorCodes('&', "&e&lWoT &e&l• &b&lOpen &a&lKomodo")	
			);
		
		// Set object to display on the sidebar
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		// Loop through each text to add on the sidebar
		for (int i = 0; i < text.length; i++) {
			Score score = obj.getScore(MessageFormat.format(customPlayer, text[i]));
			score.setScore(i);
		}
		
		Team team = scoreboard.registerNewTeam("team");
		team.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
		team.addEntry(customPlayer.getUniqueId().toString());
		customPlayer.getPlayer().setScoreboard(scoreboard);
	}
	
	private static Scoreboard globalBoard;
	
	private static HashMap<UUID, Integer> countIDMap = new HashMap<UUID, Integer>();
	private static int count = 0;
	
	public static void createBoard() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			public void run() {
				globalBoard = Bukkit.getScoreboardManager().getNewScoreboard();
			}
		});
	}
	/*
	public static void addExtraPlayer(Player player) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				int id;
				if (countIDMap.containsKey(player.getUniqueId()))
					id = countIDMap.get(player.getUniqueId());
				else {
					id = count++;
					countIDMap.put(player.getUniqueId(), id);
				}
					
				Team team = globalBoard.registerNewTeam(id + "");
				team.addEntry(player.getUniqueId().toString());
				player.setScoreboard(globalBoard);
				
				turnOnDefaultSettings(player);
			}
		});
	}
	
	public static void setPrefix(Player player, String prefix) {
		Team team = globalBoard.getTeam(countIDMap.get(player.getUniqueId()) + "");
		team.setPrefix(prefix);
	}
	
	public static void turnOnDefaultSettings(Player player) {
		String stringTest = countIDMap.get(player.getUniqueId()) + "";
		PrintConsole.test("turnondefault: "+ stringTest);
		Team team = globalBoard.getTeam(stringTest);
		team.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
	}*/
}
