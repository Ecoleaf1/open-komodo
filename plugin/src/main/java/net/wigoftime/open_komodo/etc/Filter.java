package net.wigoftime.open_komodo.etc;

import net.wigoftime.open_komodo.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public abstract class Filter 
{
	private static final String slowDownMsg = ChatColor.translateAlternateColorCodes('&', "&c&lHEY!&r&7 Sorry, but please slow down.");
	private static final String spamMsg = ChatColor.translateAlternateColorCodes('&', "&c&lHEY!&r&7 Sorry, but please stop spamming.");
	private static final String bannedWordMsg = ChatColor.translateAlternateColorCodes('&', "&c&lHEY!&r&7 Sorry, but that word isn't allowed on here.");
	
	public static final File fileDict = new File(Main.dataFolderPath+"/Whitelist-Dict.txt");
	
	private static @NotNull Map<UUID, Long> delay = new HashMap<UUID, Long>();
	private static @NotNull Set<String> whitelistWords = new HashSet<String>();
	
	public static void setup() {	
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
			e.printStackTrace();
		}
		
		final String[] extraPrefixes = {"(","!","[","@","\""};
		
		for (String extraPrefix: extraPrefixes)
			prefixes.add(extraPrefix);
		
		final String[] normalSuffixes = {"s","es","d", "ed"};
		final String[] extraSuffixes = {"?", "!", ".", ",", ";", ":", ")", "!)", ")!", "?)", ")?", ".)", ")." , ",)", ")," , ";)", ");", ":)", "):"};
		
		for (String suffix : normalSuffixes)
			suffixes.add(suffix);
		
		for (String extraSuffix : extraSuffixes)
			suffixes.add(extraSuffix);
		
		for (String normalSuffix : normalSuffixes) {
			for (String extraSuffix : extraSuffixes)
				suffixes.add(normalSuffix + extraSuffix);
		}
	}
	
	private static String @NotNull [] bannedWords = {"fuck" ,"f@ck", "f*ck", "cunt", "pussy", "pu$$y", "dick", "d!ck", "bastard", "tit", "shit", "piss", "bitch", "b!tch", "asshole", "arsehole", "penis", "vagina", "sex", "S@x", "s*x", "wh*re", "whore", "nigger", "nigga", "fag", "cum", "circumcise", "circumcision", "cock", "boob", "breast", "boner", "rape", "r*pe", "stripper", "slut"};
	
	private static @NotNull Set<String> prefixes = new HashSet<String>();
	private static @NotNull Set<String> suffixes = new HashSet<String>();
	
	public static boolean checkMessage(@NotNull Player player, String text) {
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
	
	public static boolean isSpamming(@NotNull Player player, String text)
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
		text = text.toLowerCase();
		
		// Split text into arguments
		String[] args = text.split(" ");
		
		// Approved word count, counts how much approved words in a row.
		int approvedStreak = 0;
		
		// StringBuilder for unapproved letters
		StringBuilder textBuilder = new StringBuilder();
		
		// Loop through each letter
		for (String s : args) {	
			
			// If a word in the WhiteList Dictionary
			if (isWhitelistWord(s)) {
				approvedStreak++;
				continue;
			}
			
			// If previous two words or more are approved, 
			// delete previous unapproved characters
			if (approvedStreak > 1) {
				textBuilder.delete(0, textBuilder.length());
				approvedStreak = 0;
			}
			
			// Get each letter in unapproved word
			for (int i = 0; i < s.length(); i++) {
				textBuilder.append(s.charAt(i));
			}
			
			// Loop through each forbidden word
			for (String word : bannedWords)
			{
				// String builder that limits multiple characters.
				StringBuilder sb = new StringBuilder();
				
				// Index for spotting swearing
				int swearIndex = 0;
				
				// Check for multiple characters together, if so, check if it has some in a forbidden word.
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
					System.out.println(sb.toString() + " | SWEARFILTER");
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean isWhitelistWord(@NotNull String word) {
		if (whitelistWords.contains(word.toLowerCase()))
			return true;
		
		if (isWhitelistWordWithAffix(word)) 
			return true;
		
		return false;
	}
	
	private static boolean isWhitelistWordWithAffix(@NotNull String word) {
		for (String suffix : suffixes) {
			if (!word.endsWith(suffix))
				continue;
			
			StringBuilder removeSuffixStringBuilder = new StringBuilder();
			for (int index = 0; index < word.length() - suffix.length(); index++)
				removeSuffixStringBuilder.append(word.charAt(index));
			
			if (removeSuffixStringBuilder.length() < 1) continue;
			
			if (whitelistWords.contains(removeSuffixStringBuilder.toString().toLowerCase()))
				return true;
			
			for (String prefix : prefixes) {
				if (!word.startsWith(prefix))
					continue;
				
				int cutOutPrefixIndex = prefix.length();
				
				PrintConsole.test("Maths: " + removeSuffixStringBuilder.length() + " - " + cutOutPrefixIndex + " = " + (removeSuffixStringBuilder.length() - cutOutPrefixIndex));
				char[] cutOutPrefixCharArray = new char[removeSuffixStringBuilder.length() - cutOutPrefixIndex];
				
				word.getChars(cutOutPrefixIndex, removeSuffixStringBuilder.length(), cutOutPrefixCharArray, 0);
				
				PrintConsole.test("Test Prefix " + String.copyValueOf(cutOutPrefixCharArray));
				
				if (whitelistWords.contains(String.copyValueOf(cutOutPrefixCharArray).toLowerCase()))
					return true;
			}
		}
		
		return false;
	}
}
