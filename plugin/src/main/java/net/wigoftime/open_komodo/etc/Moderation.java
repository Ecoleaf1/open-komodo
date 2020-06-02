package net.wigoftime.open_komodo.etc;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.config.PlayerConfig;

abstract public class Moderation 
{
	public static final String banNoReason = ChatColor.translateAlternateColorCodes('&', "&cYou have been banned until: $~");
	public static final String banReason = ChatColor.translateAlternateColorCodes('&', "&cYou have been banned.\nReason: $1~\nDue date: $~");
	
	public static final String mutedNoReason = ChatColor.translateAlternateColorCodes('&', "&cYou have been muted until: $~");
	public static final String mutedReason = ChatColor.translateAlternateColorCodes('&', "&cYou have been muted for: $1~\nYou will be unmuted at: $~");
	
	public static boolean isMuted(Player player)
	{
		// Get date
		Date muteDate = PlayerConfig.getMuteDate(player);
		
		//	If Player is stated clearly not muted.
		if (muteDate == null) 
			return false;
		else 
		{
			//	Get the current Calendar with the current date.
			Calendar cal = Calendar.getInstance();
			
			//	If Player's mute date has passed.
			if (cal.toInstant().isAfter(muteDate.toInstant()))
			{
				
				//	Try write the Mute Date to none to avoid more checking in the future.
				try
				{
					File file = PlayerConfig.getPlayerConfig(player);
					YamlConfiguration playerYaml = YamlConfiguration.loadConfiguration(file);
					playerYaml.getConfigurationSection("Moderation").set("Mute", "");
					playerYaml.save(file);
				}
				catch (IOException exception)
				{
					PrintConsole.print(String.format("ERROR: %s's Mute Date Cannot be Removed.", player.getName()));
				}
				
				return false;
			}
			
			// If the code made it this fair, then the player is muted.
			sendMuteMessage(player);
			return true;
		}
	}
	
	public static boolean isBanned(UUID uuid)
	{
		// Get ban date
		Instant date = PlayerConfig.getBanInstant(uuid);
		
		// If date don't exist, not banned
		if (date == null)
			return false;
		
		// Get today's calendar
		Instant instant = Instant.now();
		
		if (instant.isBefore(date))
			return true;
		else
		{
			PlayerConfig.setBanDate(uuid, null, null);
			return false;
		}
	}
	
	public static void ban(OfflinePlayer player, Instant instant, String reason)
	{
		Player onlinePlayer;
		if (player.isOnline())
			onlinePlayer = (Player) player;
		else
			onlinePlayer = null;
		
		PlayerConfig.setBanDate(player.getUniqueId(), instant, reason);
		
		String kickMsg;
		if (reason == null)
			kickMsg = banNoReason;
		else
		{
			kickMsg = banReason;
			kickMsg = kickMsg.replace("$1~", reason);
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(instant.getEpochSecond());
		// Get a string of text of how long they are muted for
		String dateString = String.format("%04d/%d/%02d %d:%d %s", 
				cal.get(Calendar.YEAR), 
				cal.get(Calendar.MONTH), 
				cal.get(Calendar.DAY_OF_MONTH), 
				cal.get(Calendar.HOUR) > 12 ? cal.get(Calendar.MONTH) - 2 : cal.get(Calendar.MONTH), 
				cal.get(Calendar.MINUTE),
				cal.get(Calendar.HOUR) > 12 ? "PM" : "AM");
			
		kickMsg = kickMsg.replace("$1~", dateString);
		
		if (onlinePlayer != null)
			onlinePlayer.kickPlayer(kickMsg);
	}
	
	private static void sendMuteMessage(Player player)
	{
		// Get reason and the replied back message
		String reason = PlayerConfig.getMuteReason(player);
		String message;
		
		// Get Date
		Date date = PlayerConfig.getMuteDate(player);
		
		// Get calendar and set it to the mute calendar
		Calendar muteCal = Calendar.getInstance();
		muteCal.setTime(date);
		
		// If there is no reason
		if (reason == null)
		{
			// Get format
			message = mutedNoReason;
			
			// Get a string of text of how long they are muted for
			String dateString = String.format("%04d/%d/%02d %d:%d %s", 
					muteCal.get(Calendar.YEAR), 
					muteCal.get(Calendar.MONTH), 
					muteCal.get(Calendar.DAY_OF_MONTH), 
					muteCal.get(Calendar.HOUR) > 12 ? muteCal.get(Calendar.MONTH) - 2 : muteCal.get(Calendar.MONTH), 
					muteCal.get(Calendar.MINUTE),
					muteCal.get(Calendar.HOUR) > 12 ? "PM" : "AM");
			
			// Insert date in message
			message = message.replace("$~", dateString);
			
			// Send message to player
			player.sendMessage(message);
		}
		else
		{
			// Get format
			message = mutedReason;
			
			// Get a string of text of how long they are muted for
			String dateString = String.format("%04d/%d/%02d %d:%d %s", 
					muteCal.get(Calendar.YEAR), 
					muteCal.get(Calendar.MONTH), 
					muteCal.get(Calendar.DAY_OF_MONTH), 
					muteCal.get(Calendar.HOUR) > 12 ? muteCal.get(Calendar.MONTH) - 2 : muteCal.get(Calendar.MONTH), 
					muteCal.get(Calendar.MINUTE),
					muteCal.get(Calendar.HOUR) > 12 ? "PM" : "AM");
			
			// Insert date in message
			message = message.replace("$~", dateString);
			// Insert reason in message
			message = message.replace("$1~", PlayerConfig.getMuteReason(player));
			
			// Send message to player
			player.sendMessage(message);
		}
	}
	
	public static void isBanned()
	{
		
	}
}
