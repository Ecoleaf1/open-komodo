package net.wigoftime.open_komodo.etc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.config.Config;

public abstract class Filter 
{
	private static final String slowDownMsg = ChatColor.translateAlternateColorCodes('&', "&c&lHEY!&r&7 Sorry, but please slow down.");
	private static final String spamMsg = ChatColor.translateAlternateColorCodes('&', "&c&lHEY!&r&7 Sorry, but please stop spamming.");
	private static final String bannedWordMsg = ChatColor.translateAlternateColorCodes('&', "&c&lHEY!&r&7 Sorry, but that word isn't allowed on here.");
	
	public static final File fileDict = new File(Main.dataFolderPath+"/Whitelist-Dict.txt");
	
	private static Map<UUID, Long> delay = new HashMap<UUID, Long>();
	private static Set<String> whitelistWords = new HashSet<String>();
	
	public static void setup()
	{
		FileReader reader;
		try
		{
			reader = new FileReader(fileDict);
			BufferedReader buff = new BufferedReader(reader);
			
			while(buff.ready())
			{
				String line = buff.readLine();
				
				
				
				whitelistWords.add(line);
			}
			
			buff.close();
			reader.close();
		}
		catch (IOException e)
		{
			
		}
	}
	
	private static String[] bannedWords = {"fuck", "f*ck", "cunt", "pussy", "pu$$y", "dick", "d!ck", "bastard", "tit", "shit", "piss", "bitch", "b!tch", "asshole", "arsehole", "penis", "vagina", "sex", "S@x", "s*x", "wh*re", "whore", "nigger", "nigga", "fag", "cum", "circumcise", "circumcision", "cock", "boob", "breast"};
	private static String[] suffixes = {"s","es","d"};
	
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
		
		// Check if the player needs to slow down
		if (delay.containsKey(uuid))
			if (delay.get(uuid) > cal.getTimeInMillis())
			{
				player.sendMessage(slowDownMsg);
				return true;
			}
		
		// Add 2 seconds on.
		cal.add(Calendar.SECOND, 2);
		delay.put(uuid, cal.getTimeInMillis());
		return false;
	}
	
	
	public static boolean isSwore(String text)
	{
		// Split text into arguments
		String[] args = text.split(" ");
		
		// Approved word count, counts how much approved words in a row.
		int approvedStreak = 0;
		
		// Get all unapproved characters
		StringBuilder textBuilder = new StringBuilder();
		for (String s : args)
		{	
			if (whitelistWords.contains(s.toLowerCase()))
			{
				approvedStreak++;
				//approved.put(approved.size(), true);
				continue;
			}
			
			boolean approvedSuffix = false;
			for (String ss : suffixes)
			{
				if (!s.endsWith(ss))
					continue;
				
				int ii = s.length() - ss.length();
				
				char[] test = new char[ii];
				s.getChars(0, ii, test, 0);
				StringBuilder wordSuffixRemoval = new StringBuilder();
				
				for (char c : test)
				{
					wordSuffixRemoval.append(c);
				}
				
				if (whitelistWords.contains(wordSuffixRemoval.toString().toLowerCase()))
				{
					approvedStreak++;
					
					// Tick approved so it will stop
					approvedSuffix = true;
					break;
				}
				
				approvedSuffix = false;
			}
			
			
			
			// If approved word with suffix, stop.
			if (approvedSuffix)
				continue;
			
			if (approvedStreak > 1)
			{
				textBuilder.delete(0, textBuilder.length());
				approvedStreak = 0;
			}
			
			// Get each letter in unapproved word
			for (int i = 0; i < s.length(); i++)
			{
				textBuilder.append(s.charAt(i));
			}
			
			// Loop through each forbidden word
			for (String word : bannedWords)
			{
				// String builder that limits multiple characters.
				StringBuilder sb = new StringBuilder();
				
				// Index for spotting swearing
				int swearIndex = 0;
				
				// Check for multiple characters together, if so, check if it has some in sforbidden word.
				for (int i = 0; i < textBuilder.length(); i++)
				{
					if (swearIndex < word.length())
						if (word.charAt(swearIndex) == textBuilder.charAt(i))
						{
							swearIndex++;
							sb.append(textBuilder.charAt(i));
							continue;
						}
					
					if (i+1 < textBuilder.length())
						if (textBuilder.charAt(i) == textBuilder.charAt(i+1))
						{
							continue;
						}
					sb.append(textBuilder.charAt(i));
				}
				
				StringBuilder wordRegex = new StringBuilder();
				for (char c : word.toCharArray())
				{
					if (c == '*')
					{
						wordRegex.append("\\"+c+".{0,3}");
						continue;
					}
					
					wordRegex.append(c+".{0,3}");
				}
				
				
				Pattern pattern = Pattern.compile(wordRegex.toString(), Pattern.CASE_INSENSITIVE);
				
				if (pattern.matcher(sb.toString()).find())
				{
					return true;
				}
			}
		}
		return false;
	}
}
