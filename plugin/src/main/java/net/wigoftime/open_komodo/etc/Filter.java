package net.wigoftime.open_komodo.etc;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class Filter 
{
	private static final String slowDownMsg = ChatColor.translateAlternateColorCodes('&', "&c&lHEY!&r&7 Sorry, but please slow down.");
	private static final String spamMsg = ChatColor.translateAlternateColorCodes('&', "&c&lHEY!&r&7 Sorry, but please stop spamming.");
	private static final String bannedWordMsg = ChatColor.translateAlternateColorCodes('&', "&c&lHEY!&r&7 Sorry, but that word isn't allowed on here.");
	
	private static Map<UUID, Long> delay = new HashMap<UUID, Long>();
	
	private static String[] bannedWords = {"fuck", "cunt", "pussy", "dick", "dickhead", "bastard", "tits", "shit", "piss", "bitch", "asshole", "arsehole", "penis", "vagina", "sex", "whore", "nigger", "nigga", "fag" ,"cumming"};
	
	public static boolean checkMessage(Player player, String text)
	{
		if (!player.hasPermission(Permissions.ignoreSpamPerm))
			if (isSpamming(player, text))
				return false;
		
		if (!player.hasPermission(Permissions.ignoreSwearPerm))
			if (isSwore(text))
			{
				player.sendMessage(bannedWordMsg);
				return false;
			}
		
		return true;
	}
	
	public static boolean isSpamming(Player player, String text)
	{
		Calendar cal = Calendar.getInstance();
		UUID uuid = player.getUniqueId();
		
		if (delay.containsKey(uuid))
			if (delay.get(uuid) > cal.getTimeInMillis())
			{
				player.sendMessage(slowDownMsg);
				PrintConsole.test("time: " + cal.getTime().toString());
				return true;
			}
		
		char[] chars = text.toCharArray();
		
		for (int i = 0; i < chars.length; i++)
		{
			char c = chars[i];
			
			if (i + 3 > chars.length)
				break;
			
			if (chars[i+1] == c)
				if (chars[i+2] == c)
				{
					player.sendMessage(spamMsg);
					return true;
				}
				
		}
		
		cal.add(Calendar.SECOND, 2);
		delay.put(uuid, cal.getTimeInMillis());
		return false;
	}
	
	
	public static boolean isSwore(String text)
	{
		// List every single word
		for (String word : bannedWords)
		{
			// String builder for pattern
			StringBuilder sb = new StringBuilder();
			
			// Beginning of pattern
			sb.append("");
			// Loop through each character
			for (int i = 0; i < word.toCharArray().length; i++)
			{
				// Append letter to pattern
				sb.append(word.charAt(i)+".{0,3}");
			}
			sb.append("");
			
			if (Pattern.matches(sb.toString(), text))
			{
				return true;
			}
		}
		return false;
	}
}
