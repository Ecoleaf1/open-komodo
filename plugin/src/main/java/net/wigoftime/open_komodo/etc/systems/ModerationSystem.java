package net.wigoftime.open_komodo.etc.systems;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.ModHistorySingle;
import net.wigoftime.open_komodo.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class ModerationSystem
{
	public static final String banNoReason = ChatColor.translateAlternateColorCodes('&', "&cYou have been banned until: $~");
	public static final String banReason = ChatColor.translateAlternateColorCodes('&', "&cYou have been banned.\nReason: $1~\nDue date: $~");
	
	public static final String mutedNoReason = ChatColor.translateAlternateColorCodes('&', "&cYou have been muted until: $~");
	public static final String mutedReason = ChatColor.translateAlternateColorCodes('&', "&cYou have been muted for: $1~\nYou will be unmuted at: $~");
	
	public static void ban(@Nullable OfflinePlayer causer, @NotNull OfflinePlayer target, @NotNull Date date, @Nullable String reason) {
		if (SQLManager.isEnabled()) {
			if (!SQLManager.containsModerationPlayer(target.getUniqueId())) {
				SQLManager.createModerationPlayer(target.getUniqueId());
			}
		} else if (!PlayerConfig.contains(target.getUniqueId()))
			PlayerConfig.createPlayerConfig(target.getUniqueId());
				
		setBanDate(target.getUniqueId(), date);
		
		if (reason == null)
		setBanReason(target.getUniqueId(), "");
		else setBanReason(target.getUniqueId(), reason);

		addModHistory(target.getUniqueId(), new ModHistorySingle(Instant.now().getEpochSecond(), causer.getUniqueId(), "Ban", reason));

		Bukkit.getScheduler().runTask(Main.getPlugin(), new Runnable() {
			public void run() {
				if (reason == null && target.isOnline()) target.getPlayer().kickPlayer(String.format("You have been banned.\nDate: %s", date.toString()));
				else if (target.isOnline()) target.getPlayer().kickPlayer(String.format("You have been banned\nReason: %s\n.\nDate: %s", reason, date.toString()));

				if (causer.isOnline()) causer.getPlayer().sendMessage(getCauserResultsBan(target, reason, date));
			}
		});
	}
	
	public static void mute(@Nullable OfflinePlayer causer, @NotNull OfflinePlayer targetPlayer, @NotNull Date date, @Nullable String reason)
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
			addModHistory(targetPlayer.getUniqueId(), new ModHistorySingle(Instant.now().getEpochSecond(),causer.getUniqueId(), "Mute", reason));

			if (causer.isOnline()) causer.getPlayer().sendMessage(getCauserResultsMute(targetPlayer, reason, date));
			return;
		}
		
		CustomPlayer targetCustomPlayer = CustomPlayer.get(onlineTarget.getUniqueId());
		if (targetCustomPlayer != null) targetCustomPlayer.setMuteDate(date);

		addModHistory(targetPlayer.getUniqueId(), new ModHistorySingle(Instant.now().getEpochSecond(), causer == null ? null : causer.getUniqueId(), "Mute", reason));
		
		if (reason != null) targetCustomPlayer.setMuteReason(reason);
		else targetCustomPlayer.setMuteReason("");

		if (causer.isOnline()) causer.getPlayer().sendMessage(getCauserResultsMute(targetPlayer, reason, date));
	}

	private static void setMuteDate(UUID uuid, @NotNull Date date) {
		if (SQLManager.isEnabled())
		SQLManager.setMuteDate(uuid, date);
		else PlayerConfig.setMuteDate(uuid, date);
	}
	
	private static void setMuteReason(UUID uuid, String reason) {
		if (SQLManager.isEnabled())
		SQLManager.setMuteReason(uuid, reason);
		else PlayerConfig.setMuteReason(uuid, reason);
	}
	
	private static void setBanDate(UUID uuid, @NotNull Date date) {
		if (SQLManager.isEnabled())
		SQLManager.setBanDate(uuid, date);
		else PlayerConfig.setBanDate(uuid, date);
	}
	
	public static Date getBanDate(UUID uuid) 
	{
		if (SQLManager.isEnabled()) return SQLManager.getBanDate(uuid);
		else return PlayerConfig.getBanDate(uuid);
	}
	
	private static void setBanReason(UUID uuid, String reason) {
		if (SQLManager.isEnabled())
		SQLManager.setBanReason(uuid, reason);
		else PlayerConfig.setBanReason(uuid, reason);
	}
	
	public static @Nullable String getBanReason(UUID uuid) {
		String reason;
		if (SQLManager.isEnabled()) reason = SQLManager.getBanReason(uuid);
		else reason = PlayerConfig.getBanReason(uuid);
		
		if (reason == "") reason = null;
		
		return reason;
	}
	
	private static void sendMuteMessage(@NotNull CustomPlayer playerCustomPlayer) {
		// Get reason and the replied back message
		String reason = playerCustomPlayer.getMuteReason();
		String message;
		
		// Get Date
		Date date = playerCustomPlayer.getMuteDate();
		
		// Get calendar and set it to the mute calendar
		Calendar muteCal = Calendar.getInstance();
		muteCal.setTime(date);

		// Get format
		message = reason == null ? mutedNoReason : mutedReason;

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

		if (reason != null)
		message = message.replace("$1~", playerCustomPlayer.getMuteReason());

		// Send message to player
		playerCustomPlayer.getPlayer().sendMessage(message);
		PrintConsole.test("MuteCal: "+muteCal.toInstant().toString());
	}
	
	
	public static boolean isBanned(UUID uuid) {
		Date banDate;
		if (SQLManager.isEnabled())
		banDate = SQLManager.getBanDate(uuid);
		else {
			if (PlayerConfig.contains(uuid))
			banDate = PlayerConfig.getBanDate(uuid);
			else
			return false;
		}
		
		if (banDate.after(Date.from(Instant.now()))) return true;
		else return false;
	}
	
	private static enum TimeType {NONE, MINUTES, HOURS, DAYS, WEEKS, MONTHS, YEARS};
	public static Instant calculateTime(@NotNull String timeString) {
		Instant instant = Instant.now();
		TimeType timeType = TimeType.NONE;
		
		StringBuilder sb = new StringBuilder();
		for (int timeStringIndex = 0; timeStringIndex < timeString.length(); timeStringIndex++) {
			char charIndex = timeString.charAt(timeStringIndex);
			
			PrintConsole.test("-1");
			
			// If character is a number
			if (charIndex == '0' || charIndex == '1' || charIndex == '2' || charIndex == '3' || charIndex == '4'
				|| charIndex == '5' || charIndex == '6' || charIndex == '7' || charIndex == '8' || charIndex == '9')
			sb.append(charIndex);
			
			if (charIndex == 'n') timeType = TimeType.MINUTES;
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
		}
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

		return instant;
	}
	
	public static boolean isAffectingMod(@NotNull CommandSender sender, List<Permission> permsissions, @NotNull OfflinePlayer targetPlayer) {
		Permissions.getPermissions(targetPlayer.getUniqueId(), null);
		if (!Permissions.isMod(targetPlayer.getUniqueId())) return false;

		for (Player p : Bukkit.getOnlinePlayers())
			if (p.hasPermission(Permissions.abuseMonitorPerm))
			p.sendMessage(String.format("%s Abuse Detection: %s tried to mute/ban another mod (or higher) %s", ChatColor.DARK_RED, sender.getName(), targetPlayer.getName()));

		return true;
	}

	private static void addModHistory(UUID uuid, ModHistorySingle historySingle) {
		SQLManager.addModHistory(uuid, historySingle);
	}

	// Non-static
	private final CustomPlayer playerCustom;
	public @Nullable Date muteDate;
	public String muteReason;

	public ModerationSystem(CustomPlayer playerCustom) {
		this.playerCustom = playerCustom;
	}

	public String getMuteReason() {
		return muteReason;
	}

	public void setMuteDate(@Nullable Date date) {
		if (date == null) {
			muteDate = new Date(0);
		}

		Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
			public void run() {
				if (SQLManager.isEnabled())
					SQLManager.setMuteDate(playerCustom.getUniqueId(), date);
				else
					PlayerConfig.setMuteDate(playerCustom.getUniqueId(), date);
			}
		});

		muteDate = date;

	}

	public void setMuteReason(String reason) {

		Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
			public void run() {
				if (SQLManager.isEnabled())
					SQLManager.setMuteReason(playerCustom.getUniqueId(), reason);
				else
					PlayerConfig.setMuteReason(playerCustom.getUniqueId(), reason);
			}
		});

		muteReason = reason;
	}

	public boolean isMuted()
	{
		//	If Player is stated clearly not muted.
		if (muteDate == null)
			return false;
		else {
			//	Get the current Calendar with the current date.
			Calendar cal = Calendar.getInstance();

			//	If Player's mute date has passed.
			if (muteDate.before(cal.getTime())) return false;

			// If the code made it this fair, then the player is muted.
			sendMuteMessage(playerCustom);
			return true;
		}
	}
	private enum modType {KICK, MUTE, BAN};

	private static String getCauserResultsBan(@NotNull OfflinePlayer player, @Nullable String reason, @NotNull Date date) {
		if (reason == null) return String.format("%s» %s%s Has been banned until %s", ChatColor.GOLD, ChatColor.DARK_RED, player.getName(), date.toString());
		else return String.format("%s» %s%s Has been banned for %s until %s", ChatColor.GOLD, ChatColor.DARK_RED, player.getName(), reason, date.toString());
	}
	public static String getCauserResultsKick(@NotNull OfflinePlayer player, @Nullable String reason) {
		if (reason == null) return String.format("%s» %s%s Has been kicked", ChatColor.GOLD, ChatColor.DARK_RED, player.getName());
		else return String.format("%s» %s%s Has been kicked for %s ", ChatColor.GOLD, ChatColor.DARK_RED, player.getName(), reason);
	}
	private static String getCauserResultsMute(@NotNull OfflinePlayer player, @Nullable String reason, @NotNull Date date) {
		if (reason == null) return String.format("%s» %s%s Has been muted until %s", ChatColor.GOLD, ChatColor.DARK_RED, player.getName(), date.toString());
		else return String.format("%s» %s%s Has been mute for %s until %s", ChatColor.GOLD, ChatColor.DARK_RED, player.getName(), reason, date.toString());
	}
}
