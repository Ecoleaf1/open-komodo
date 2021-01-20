package net.wigoftime.open_komodo.commands;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.etc.Moderation;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.sql.SQLManager;

public class BanCommand extends Command
{
	private final String playerNotFound = ChatColor.translateAlternateColorCodes('&', "Player not found");

	public BanCommand(String name, String description, String usageMessage,
			List<String> aliases) 
	{
		super(name, description, usageMessage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] commandArguments) 
	{
		if (commandArguments.length < 1)
			return false;
		
		OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(commandArguments[0]);
		
		if (!isCommandValid(sender, targetPlayer, commandArguments))
			return false;
		
		processCommand(commandArguments, targetPlayer);
		return true;
	}
	
	private boolean isCommandValid(CommandSender sender, OfflinePlayer targetPlayer, String[] commandArguments) {
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
		
		if (Moderation.isAffectingMod(sender, permsissions, targetPlayer))
			return false;
		
		return true;
	}
	
	private void processCommand(String[] commandArguments, OfflinePlayer playerTarget) {
		StringBuilder reasonStringBuilder;
		if (commandArguments.length > 2) {
			reasonStringBuilder = new StringBuilder();
			for (int i = 2; i < commandArguments.length; i++) {
				reasonStringBuilder.append(commandArguments[i]+" ");
			}
		} else
			reasonStringBuilder = null;
		
		// Convert input time string from user to Time
		Instant instant = Moderation.calculateTime(commandArguments[1]);
		
		// Create reference variables to referense on potentially different threads
		final UUID refUUID = playerTarget.getUniqueId();
		final Instant refInstant = instant;
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
			public void run() {
				if (reasonStringBuilder == null) Moderation.ban(refUUID, Date.from(refInstant), null);
				else Moderation.ban(refUUID, Date.from(refInstant), reasonStringBuilder.toString());
			}
		});
	}
}
