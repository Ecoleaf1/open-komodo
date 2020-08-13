package net.wigoftime.open_komodo.etc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatAnnouncements implements Runnable {
	private final Random random = new Random();
	private static final String header = String.format("         %s%s━━━━━━━━━━━━━━━━━%s%s|%s%s[ %s%sO%s%sK %s%s]%s%s|%s%s━━━━━━━━━━━━━━━━━", 
			ChatColor.YELLOW, ChatColor.STRIKETHROUGH, ChatColor.DARK_AQUA
			,ChatColor.MAGIC, ChatColor.YELLOW, ChatColor.BOLD, ChatColor.YELLOW, ChatColor.BOLD, ChatColor.AQUA, ChatColor.BOLD, ChatColor.YELLOW,
			ChatColor.BOLD, ChatColor.AQUA, ChatColor.MAGIC, ChatColor.YELLOW, ChatColor.STRIKETHROUGH);
	private static final String footer = String.format("                          %s%s━━━━━━━━━━━━━━━", ChatColor.YELLOW, ChatColor.STRIKETHROUGH);
	
	private byte lastNumber;
	@Override
	public void run() {
		byte randomNumber = (byte) random.nextInt(7);
		if (randomNumber == lastNumber)
			while (randomNumber == lastNumber)
				randomNumber = (byte) random.nextInt(7);
		
		lastNumber = randomNumber;
		List<String> lines;
		
		switch (randomNumber) {
		case 0:
			lines = order1();
			break;
		case 1:
			lines = order2();
			break;
		case 2:
			lines = order3();
			break;
		case 3:
			lines = order4();
			break;
		case 4:
			lines = order5();
			break;
		case 5:
			lines = order6();
			break;
		case 6:
			lines = order7();
			break;
		default:
			lines = Arrays.asList(String.format("%sWarning: Randomizer is out of range, ChatAnnouncements", ChatColor.YELLOW));
		}
		
		broadcast(lines);
	}
	
	private List<String> order1() {
		List<String> lines = new ArrayList<String>(5);
		
		lines.add(header);
		lines.add("");
		lines.add(String.format("%sOpen Komodo is still in beta! Report bugs and glitches!", ChatColor.DARK_AQUA));
		lines.add("");
		lines.add(footer);
		
		return lines;
	}
	
	private List<String> order2() {
		List<String> lines = new ArrayList<String>(5);
		
		lines.add(header);
		lines.add("");
		lines.add(String.format("%sDid you know that we have a discord? Click on the discord icon on your phone to get the link! we would love to hear your ideas!", ChatColor.DARK_AQUA, ChatColor.BOLD, ChatColor.DARK_AQUA));
		lines.add("");
		lines.add(footer);
		
		return lines;
	}
	
	private List<String> order3() {
		List<String> lines = new ArrayList<String>(6);
		
		lines.add(header);
		lines.add("");
		lines.add(String.format("%sRank up by roleplay & exploring! Doing so will give you xp to level up. Be patient though, it takes a bit to get a rank! For more information, type in /rank help", ChatColor.DARK_AQUA));
		lines.add("");
		lines.add(footer);
		
		return lines;
	}
	
	private List<String> order4() {
		List<String> lines = new ArrayList<String>(6);
		
		lines.add(header);
		lines.add("");
		lines.add(String.format("%sThanks for chilling on our server! Your support is greatly appreciated", ChatColor.DARK_AQUA));
		lines.add("");
		lines.add(footer);
		
		return lines;
	}
	
	private List<String> order5() {
		List<String> lines = new ArrayList<String>(6);
		
		lines.add(header);
		lines.add("");
		lines.add(String.format("%sMVPs can start collecting coins around every 40 minutes, which is used to buy particle boxes!", ChatColor.DARK_AQUA));
		lines.add("");
		lines.add(footer);
		
		return lines;
	}
	
	private List<String> order6() {
		List<String> lines = new ArrayList<String>(6);
		
		lines.add(header);
		lines.add("");
		lines.add(String.format("%sUse common sense and follow the rules (/rules)!", ChatColor.DARK_AQUA));
		lines.add("");
		lines.add(footer);
		
		return lines;
	}
	
	private List<String> order7() {
		List<String> lines = new ArrayList<String>(6);
		
		lines.add(header);
		lines.add("");
		lines.add(String.format("%sYou can change your display name by using the command /nick!", ChatColor.DARK_AQUA));
		lines.add("");
		lines.add(footer);
		
		return lines;
	}
	
	private void broadcast(List<String> lines) {
		for (Player player : Bukkit.getOnlinePlayers())
			for (String line : lines)
				player.sendMessage(line);
	}
}
