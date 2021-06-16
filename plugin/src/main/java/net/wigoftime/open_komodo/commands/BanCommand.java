package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.etc.systems.ModerationSystem;
import net.wigoftime.open_komodo.objects.ModerationResults;
import net.wigoftime.open_komodo.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public class BanCommand extends Command
{
	private final String playerNotFound = ChatColor.translateAlternateColorCodes('&', "Player not found");

	public BanCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
                      @NotNull List<String> aliases) {
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(@NotNull CommandSender sender, String command, String @NotNull [] commandArguments)
	{
		if (commandArguments.length < 1) {
			sender.sendMessage(this.usageMessage);
			return false;
		}
		
		OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(commandArguments[0]);
		
		if (!isCommandValid(sender, targetPlayer, commandArguments))
			return false;
		
		processCommand(sender, commandArguments, sender, targetPlayer);
		return true;
	}
	
	private boolean isCommandValid(@NotNull CommandSender sender, @Nullable OfflinePlayer targetPlayer, String @NotNull [] commandArguments) {
		if (!sender.hasPermission(Permissions.banPerm)) {
			sender.sendMessage(ChatColor.DARK_RED + "You are not permitted to use that command.");
			return false;
		}
		
		if (commandArguments.length < 2) {
			sender.sendMessage(this.usageMessage);
			return false;
		}
		
		if (targetPlayer == null) {
			sender.sendMessage(playerNotFound);
			return false;
		}
		
		List<Permission> permsissions; 
		if (SQLManager.isEnabled()) permsissions = SQLManager.getGlobalPermissions(targetPlayer.getUniqueId());
		else permsissions = PlayerConfig.getGlobalPermissions(targetPlayer.getUniqueId());
		
		if (ModerationSystem.isAffectingMod(sender, permsissions, targetPlayer))
			return false;
		
		return true;
	}
	
	private void processCommand(@NotNull CommandSender sender, String @NotNull [] commandArguments, CommandSender banner, @NotNull OfflinePlayer playerTarget) {
		StringBuilder reasonStringBuilder;
		if (commandArguments.length > 2) {
			reasonStringBuilder = new StringBuilder();
			for (int i = 2; i < commandArguments.length; i++) {
				reasonStringBuilder.append(commandArguments[i]+" ");
			}
		} else
			reasonStringBuilder = null;
		
		// Convert input time string from user to Time
		Instant instant = ModerationSystem.calculateTime(commandArguments[1]);
		
		// Create reference variables to referense on potentially different threads
		final Instant refInstant = instant;
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
			public void run() {
				ModerationResults banResult;
				
				if (reasonStringBuilder == null) ModerationSystem.ban(playerTarget, Date.from(refInstant), null);
				else ModerationSystem.ban(playerTarget, Date.from(refInstant), reasonStringBuilder.toString());
			}
		});
	}
	
	private static void sendBanResultMessage(@NotNull CommandSender banner, @NotNull ModerationResults result) {

		banner.sendMessage(String.format("%s» %s%s has been banned for reason %s. Ban date %s",
				ChatColor.GOLD, ChatColor.DARK_RED,
				result.affectedPlayer.getName(), result.reason == null ? "[No Reason Specified]" : result.reason,  result.date.toString()));
	}
}
