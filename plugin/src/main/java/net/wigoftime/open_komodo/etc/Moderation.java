package net.wigoftime.open_komodo.etc;

import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.ModerationResults;
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
			return false;
		
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
	
	public static ModerationResults ban(UUID uuid, Date date, String reason) 
	{
		if (SQLManager.isEnabled()) {
			if (!SQLManager.containsModerationPlayer(uuid)) {
				SQLManager.createModerationPlayer(uuid);
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
			return new ModerationResults(null, date, reason);
		
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
		
		return new ModerationResults(player, date, reason);
	}
	
	public static void mute(OfflinePlayer targetPlayer, Date date, String reason) 
	{
		if (SQLManager.isEnabled()) {
			if (!SQLManager.containsModerationPlayer(targetPlayer.getUniqueId())) {
				SQLManager.createModerationPlayer(targetPlayer.getUniqueId());
			}
		} else
			if (!PlayerConfig.contains(targetPlayer.getUniqueId()))
				PlayerConfig.createPlayerConfig(targetPlayer.getUniqueId());
				
		Player onlineTarget = targetPlayer.getPlayer();
		if (onlineTarget == null) {
			if (reason == null) setMuteReason(targetPlayer.getUniqueId(), "");
			else setMuteReason(targetPlayer.getUniqueId(), reason);
			
			setMuteDate(targetPlayer.getUniqueId(), date);
			return;
		}
		
		CustomPlayer targetCustomPlayer = CustomPlayer.get(onlineTarget.getUniqueId());
		
		targetCustomPlayer.setMuteDate(date);
		
		if (reason == null) targetCustomPlayer.setMuteReason(reason);
		else targetCustomPlayer.setMuteReason("");
	}
	
	private static void setMuteDate(UUID uuid, Date date) 
	{
		if (SQLManager.isEnabled())
		SQLManager.setMuteDate(uuid, date);
		else
		PlayerConfig.setMuteDate(uuid, date);
	}
	
	private static void setMuteReason(UUID uuid, String reason) {
		if (SQLManager.isEnabled())
		SQLManager.setMuteReason(uuid, reason);
		else
		PlayerConfig.setMuteReason(uuid, reason);
	}
	
	private static void setBanDate(UUID uuid, Date date) 
	{
		if (SQLManager.isEnabled())
		SQLManager.setBanDate(uuid, date);
		else
		PlayerConfig.setBanDate(uuid, date);
	}
	
	public static Date getBanDate(UUID uuid) 
	{
		if (SQLManager.isEnabled())
		return SQLManager.getBanDate(uuid);
		else
		return PlayerConfig.getBanDate(uuid);
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
			String dateString = String.format("%04d/%02d/%02d %02d:%02d %s", 
					muteCal.get(Calendar.YEAR), 
					muteCal.get(Calendar.MONTH) + 1, 
					muteCal.get(Calendar.DAY_OF_MONTH), 
					muteCal.get(Calendar.HOUR),
					muteCal.get(Calendar.MINUTE),
					muteCal.get(Calendar.HOUR_OF_DAY) > 11 ? "PM" : "AM");
			
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
			String dateString = String.format("%04d/%02d/%02d %02d:%02d %s", 
					muteCal.get(Calendar.YEAR), 
					muteCal.get(Calendar.MONTH) + 1, 
					muteCal.get(Calendar.DAY_OF_MONTH), 
					muteCal.get(Calendar.HOUR),
					muteCal.get(Calendar.MINUTE),
					muteCal.get(Calendar.HOUR_OF_DAY) > 11 ? "PM" : "AM");
			
			// Insert date in message
			message = message.replace("$~", dateString);
			// Insert reason in message
			message = message.replace("$1~", playerCustomPlayer.getMuteReason());
			
			// Send message to player
			playerCustomPlayer.getPlayer().sendMessage(message);
		}
		
		PrintConsole.test("MuteCal: "+muteCal.toInstant().toString());
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
	
	private static enum TimeType {NONE, MINUTES, HOURS, DAYS, WEEKS, MONTHS, YEARS};
	public static Instant calculateTime(String timeString) {
		Instant instant = Instant.now();
		TimeType timeType = TimeType.NONE;
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < timeString.length(); i++) {
			char charIndex = timeString.charAt(i);
			
			PrintConsole.test("-1");
			
			// If character is a number
			if (charIndex == '0' || charIndex == '1' || charIndex == '2' || charIndex == '3' || charIndex == '4' || charIndex == '5' || charIndex == '6' || charIndex == '7' || charIndex == '8' || charIndex == '9')
			{
				sb.append(charIndex);
			}
			
			if (charIndex == 'n')
				timeType = TimeType.MINUTES;
			else if (charIndex == 'h')
				timeType = TimeType.HOURS;
			else if (charIndex == 'd')
				timeType = TimeType.DAYS;
			else if (charIndex == 'w')
				timeType = TimeType.WEEKS;
			else if (charIndex == 'm')
				timeType = TimeType.MONTHS;
			else if (charIndex == 'y')
				timeType = TimeType.YEARS;
			
			PrintConsole.test("0");
			if (i + 1 >= timeString.length()) {
				Integer amountInputed = Integer.parseInt(sb.toString());
				
				switch (timeType) {
				case MINUTES:
					instant = instant.plus(Duration.ofMinutes(amountInputed));
					break;
				case HOURS:
					instant = instant.plus(Duration.ofHours(amountInputed));
					break;
				case DAYS:
					instant = instant.plus(Duration.ofDays(amountInputed));
					break;
				case WEEKS:
					amountInputed = amountInputed * 7;
					instant = instant.plus(Duration.ofDays(amountInputed));
					break;
				case MONTHS:
					amountInputed = amountInputed * 29;
					instant = instant.plus(Duration.ofDays(amountInputed));
					break;
				case YEARS:
					amountInputed = amountInputed * 365;
					instant = instant.plus(Duration.ofDays(amountInputed));
					break;
				default:
					break;
					
				}
				
				break;
			}
		}
		
		return instant;
	}
	
	public static boolean isAffectingMod(CommandSender sender, List<Permission> permsissions, OfflinePlayer targetPlayer) {
		for (Permission permission : permsissions) 
		if (permission.getName().equals(Permissions.abuseMonitorPerm.getName())) {
			for (Player p : Bukkit.getOnlinePlayers())
			if (p.hasPermission(Permissions.abuseMonitorPerm))
				p.sendMessage(String.format("%s Abuse Detection: %s tried to ban another mod (or higher) %s", ChatColor.DARK_RED, sender.getName(), targetPlayer.getName()));
				
				return true;
			}
		
		return false;
	}
}
