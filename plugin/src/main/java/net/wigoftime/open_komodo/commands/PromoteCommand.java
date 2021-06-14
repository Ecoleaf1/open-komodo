package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.Rank;
import net.wigoftime.open_komodo.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PromoteCommand extends Command {
	public PromoteCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
                          @NotNull List<String> aliases) {
		super(name, description, usageMessage, aliases);
	}
	
	private static final String usage = String.format("%s» %sUsage: /promote {Rank/Player/list-perms}", 
			ChatColor.GOLD, ChatColor.GRAY);
	private static final String usageRankMsg = String.format("%s» %sUsage: /promote {Rank} {Player Name} {Rank}", 
			ChatColor.GOLD, ChatColor.GRAY);

	@Override
	public boolean execute(@NotNull CommandSender sender, String command, String @NotNull [] args) {
		if (!sender.hasPermission(Permissions.promotePerm)) {
			sender.sendMessage(Permissions.promotePermError);
			return false;
		}

		if (args.length < 1) {
			sender.sendMessage(usage);
			return false;
		}

		if (args[0].equalsIgnoreCase("player")) playerSelector(sender, args);
		else if (args[0].equalsIgnoreCase("rank")) rankSelector(sender, args);
		else if (args[0].equalsIgnoreCase("list-perms") || args[0].equalsIgnoreCase("listperm")) listpermsSelector(sender, args);
		return true;
	}

	private void playerSelector(@NotNull CommandSender sender, String @NotNull [] args) {
		if (args.length < 4) {
			sender.sendMessage(String.format("%s» %sUsage: /promote player {Add/Remove} {Player} {World Name} {Permission}", ChatColor.GOLD, ChatColor.GRAY));
			return;
		}

		boolean isAdding;
		if (args[1].equalsIgnoreCase("add")) isAdding = true;
		else isAdding = false;

		OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[2]);

		if (args.length > 4) {
			World targetWorld = Bukkit.getWorld(args[3]);
			if (targetWorld == null) {
				sender.sendMessage(String.format("%s» %sCould not find world", ChatColor.GOLD, ChatColor.DARK_RED));
				return;
			}

			boolean addedResult;

			if (isAdding)
				addedResult = Permissions.addPermission(targetPlayer.getUniqueId(), new Permission(args[4]), targetWorld);
			else
				addedResult = Permissions.removePermission(targetPlayer.getUniqueId(), new Permission(args[4]), targetWorld);

		} else {
			boolean addedResult;
			if (isAdding)
				addedResult = Permissions.addPermission(targetPlayer.getUniqueId(), new Permission(args[3]), null);
			else
				addedResult = Permissions.removePermission(targetPlayer.getUniqueId(), new Permission(args[3]), null);
		}
	}

	private void rankSelector(@NotNull CommandSender sender, String @NotNull [] args) {
		if (args.length < 3) {
			sender.sendMessage(usageRankMsg);
			return;
		}

		OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
		CustomPlayer playerCustom = CustomPlayer.get(player.getUniqueId());

		Rank rank = Rank.getRank(args[2]);

		if (playerCustom == null) SQLManager.setRankID(player.getUniqueId(),rank.getID());
		else playerCustom.getRankSystem().setRank(rank);
	}

	private void listpermsSelector(@NotNull CommandSender sender, String @NotNull [] args) {
		if (args.length < 2) {
			sender.sendMessage(String.format("%s» %sUsage: /promote list-perms {Player} [World Name]", ChatColor.GOLD, ChatColor.GRAY));
			return;
		}

		OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[1]);
		if (targetPlayer == null) {
			sender.sendMessage(String.format("%sError: Player not found", ChatColor.DARK_RED));
			return;
		}

		List<Permission> permissions;
		if (args.length < 3) permissions = Permissions.getPermissions(targetPlayer.getUniqueId(), null);
		else permissions = Permissions.getPermissions(targetPlayer.getUniqueId(), Bukkit.getWorld(args[2]));

		if (args.length < 3) sender.sendMessage(String.format("%s» %s%s's%s Global Permissions:",
			ChatColor.GOLD, ChatColor.GOLD, targetPlayer.getName(), ChatColor.GRAY));
		else sender.sendMessage(String.format("%s» %s%s's%s world (%s%s%s) Permissions:",
			ChatColor.GOLD, ChatColor.GOLD, targetPlayer.getName(), ChatColor.GRAY,
			ChatColor.GOLD, ChatColor.GRAY ,args[2]));

		for (Permission permissionIndex: permissions)
		sender.sendMessage(String.format("%s- %s", ChatColor.GRAY , permissionIndex.getName()));
	}

}
