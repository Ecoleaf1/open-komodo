package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import net.wigoftime.open_komodo.actions.Promote;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.sql.SQLManager;

public class PromoteCommand extends Command {
	public PromoteCommand(String name, String description, String usageMessage,
			List<String> aliases) {
		super(name, description, usageMessage, aliases);
	}
	
	private static final String usage = String.format("%s» %sUsage: /promote {Rank/Player/list-perms}", 
			ChatColor.GOLD, ChatColor.GRAY);
	private static final String usageRankMsg = String.format("%s» %sUsage: /promote {Rank} {Player Name} {Rank}", 
			ChatColor.GOLD, ChatColor.GRAY);

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) {
		// If player doesn't have promote permission, return
		if (!sender.hasPermission(Permissions.promotePerm)) {
			sender.sendMessage(Permissions.promotePermError);
			return false;
		}
		
		// Skip if no sub-commands
		if (args.length < 1) {
			sender.sendMessage(usage);
			return false;
		}
		
		// First sub-command will be selector, example "rank"
		String selector = args[0];
		
		// If player wants to promote player's rank
		if (selector.equalsIgnoreCase("rank")) {
			// skip if less than 3 sub-commands
			if (args.length < 3) {
				sender.sendMessage(usageRankMsg);
				return false;
			}
			
			String targetName = args[1];
			String rankName = args[2];
			
			Promote.commandPromoteRank(sender, targetName, rankName);
			return true;
		}
		
		// If Managing Player Local Permissions
		if (selector.equalsIgnoreCase("player")) {
			// If has less than 4 sub commands
			if (args.length < 4) {
				sender.sendMessage(String.format("%s» %sUsage: /promote player {Add/Remove} {Player} {World Name} {Permission}", ChatColor.GOLD, ChatColor.GRAY));
				return false;
			}
			
			// Sub-command to put either "add" or "remove
			String modifer = args[1];
			
			String targetName = args[2];
			
			Permission permission;
			World world;
			
			if (args.length > 4)
			{
				world = Bukkit.getWorld(args[3]);
				
				if (world == null)
				{
					sender.sendMessage(String.format("%s» %sCould not find world", ChatColor.GOLD, ChatColor.DARK_RED));
					return false;
				}
				
				permission = new Permission(args[4]);
			}
			else
			{
				permission = new Permission(args[3]);
				world = null;
			}
			
			// If player is adding permission
			boolean addMode;
			
			if (modifer.equalsIgnoreCase("add"))
				addMode = true;
			else if (modifer.equalsIgnoreCase("remove"))
				addMode = false;
			else {
				sender.sendMessage(String.format("%s» %sUsage: /promote player {Add/Remove} {Player} {World Name} {Permission}", ChatColor.GOLD, ChatColor.GRAY));
				return false;
			}
			
			Promote.commandPromotePermission(sender, targetName, world, permission, addMode);
			return false;
		}
		
		if (selector.equalsIgnoreCase("list-perms")) {
			if (args.length < 2) {
				sender.sendMessage(String.format("%s» %sUsage: /promote list-perms {Player} [World Name]", ChatColor.GOLD, ChatColor.GRAY));
				return  false;
			}
			
			OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
			if (player == null) {
				sender.sendMessage(String.format("%sError: Player not found", ChatColor.DARK_RED));
				return false;
			}
			
			List<Permission> permissions;
			
			if (args.length < 3) {
				
				if (SQLManager.isEnabled()) permissions = SQLManager.getGlobalPermissions(player.getUniqueId());
				else permissions = PlayerConfig.getGlobalPermissions(player.getUniqueId());
				
				if (permissions == null)
					return false;
				
				sender.sendMessage(String.format("%s» %s%s's%s Global Permissions:", ChatColor.GOLD, ChatColor.GOLD, player.getName(), ChatColor.GRAY));
			} else {
				if (SQLManager.isEnabled())
					permissions = SQLManager.getWorldPermission(player.getUniqueId(), args[2]);
				else permissions = PlayerConfig.getWorldPermissions(player.getUniqueId(), args[2]);
				
				if (permissions == null) {
					sender.sendMessage(String.format("%sError: Permissions not found, does the world exist?", ChatColor.DARK_RED));
					return false;
				}
				
				sender.sendMessage(String.format("%s» %s%s's%s world (%s%s%s) Permissions:", 
						ChatColor.GOLD, ChatColor.GOLD, player.getName(), ChatColor.GRAY,
						ChatColor.GOLD, ChatColor.GRAY ,args[2]));
			}
			
			for (Permission permissionIndex: permissions)
				sender.sendMessage(String.format("%s- %s", ChatColor.GRAY , permissionIndex.getName()));
		}
		return false;
	}

}
