package net.wigoftime.open_komodo.etc;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.sql.SQLManager;

abstract public class Moderation 
{
	public static final String banNoReason = ChatColor.translateAlternateColorCodes('&', "&cYou have been banned until: $~");
	public static final String banReason = ChatColor.translateAlternateColorCodes('&', "&cYou have been banned.\nReason: $1~\nDue date: $~");
	
	public static final String mutedNoReason = ChatColor.translateAlternateColorCodes('&', "&cYou have been muted until: $~");
	public static final String mutedReason = ChatColor.translateAlternateColorCodes('&', "&cYou have been muted for: $1~\nYou will be unmuted at: $~");
	
	public static boolean isMuted(CustomPlayer playerCustomPlayer)
	{
		// Get date
		Date muteDate = playerCustomPlayer.getMuteDate();
		
		//	If Player is stated clearly not muted.
		if (muteDate == null) 
		{ 
			PrintConsole.test("mute date is null");
			return false;
		}
		else 
		{
			//	Get the current Calendar with the current date.
			Calendar cal = Calendar.getInstance();
			
			//	If Player's mute date has passed.
			if (muteDate.before(cal.getTime())) return false;
			
			// If the code made it this fair, then the player is muted.
			sendMuteMessage(playerCustomPlayer);
			return true;
		}
	}
	
	public static void ban(UUID uuid, Date date, String reason) 
	{
		if (SQLManager.isEnabled()) {
			if (!SQLManager.containsPlayer(uuid)) {
				SQLManager.createPlayer(uuid);
			}
		} else
			if (!PlayerConfig.contains(uuid))
				PlayerConfig.createPlayerConfig(uuid);
				
		
		setBanDate(uuid, date);
		
		if (reason == null)
			setBanReason(uuid, "");
		else
			setBanReason(uuid, reason);
		
		Player player = Bukkit.getPlayer(uuid);
		
		if (player == null)
			return;
		
		if (reason == null)
			Bukkit.getScheduler().runTask(Main.getPlugin(), new Runnable() {
				public void run() {
					player.kickPlayer(String.format("You have been banned.\nDate: %s", date.toString()));
				}
			});
			
		else
			Bukkit.getScheduler().runTask(Main.getPlugin(), new Runnable() {
				public void run() {
			player.kickPlayer(String.format("You have been banned\nReason: %s\n.\nDate: %s", reason, date.toString()));
				}
			});
	}
	
	private static void setBanDate(UUID uuid, Date date) 
	{
		if (SQLManager.isEnabled())
		SQLManager.setBanDate(uuid, date);
		else
		PlayerConfig.setBanDate(uuid, date);
	}
	
	private static void setBanReason(UUID uuid, String reason) 
	{
		if (SQLManager.isEnabled())
		SQLManager.setBanReason(uuid, reason);
		else
		PlayerConfig.setBanReason(uuid, reason);
	}
	
	public static String getBanReason(UUID uuid) 
	{
		
		String reason;
		if (SQLManager.isEnabled())
		reason = SQLManager.getBanReason(uuid);
		else
		reason = PlayerConfig.getBanReason(uuid);
		
		if (reason == "")
			reason = null;
		
		return reason;
	}
	
	private static void sendMuteMessage(CustomPlayer playerCustomPlayer)
	{
		// Get reason and the replied back message
		String reason = playerCustomPlayer.getMuteReason();
		String message;
		
		// Get Date
		Date date = playerCustomPlayer.getMuteDate();
		
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
			playerCustomPlayer.getPlayer().sendMessage(message);
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
			message = message.replace("$1~", playerCustomPlayer.getMuteReason());
			
			// Send message to player
			playerCustomPlayer.getPlayer().sendMessage(message);
		}
	}
	
	
	public static boolean isBanned(UUID uuid)
	{
		Date banDate;
		if (SQLManager.isEnabled())
		banDate = SQLManager.getBanDate(uuid);
		else {
			if (PlayerConfig.contains(uuid))
			banDate = PlayerConfig.getBanDate(uuid);
			else
			return false;
		}
		
		if (banDate.after(Date.from(Instant.now())))
			return true;
		else
			return false;
	}
}
