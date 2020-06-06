package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

import net.wigoftime.open_komodo.actions.Promote;
import net.wigoftime.open_komodo.etc.Permissions;

public class PromoteCommand extends Command 
{

	public PromoteCommand(String name, String description, String usageMessage,
			List<String> aliases) 
	{
		super(name, description, usageMessage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) 
	{
		
		// If player doesn't have promote permission, return
		if (!sender.hasPermission(Permissions.promotePerm))
		{
			sender.sendMessage(Permissions.promotePermError);
			return false;
		}
		
		// Skip if no sub-commands
		if (args.length < 1)
		{
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lUsage: /promote {Rank/Player}"));
			return false;
		}
		
		// First sub-command will be selector, example "rank"
		String selector = args[0];
		
		// If player wants to promote player's rank
		if (selector.equalsIgnoreCase("rank"))
		{
			// skip if less than 3 sub-commands
			if (args.length < 3)
			{
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lUsage: /promote {Rank} {Player Name} {Rank}"));
				return false;
			}
			
			String targetName = args[1];
			String rankName = args[2];
			
			Promote.commandPromoteRank(sender, targetName, rankName);
		}
		
		// If Managing Player Local Permissions
		if (selector.equalsIgnoreCase("player"))
		{
			// If has less than 4 sub commands
			if (args.length < 4)
			{
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lUsage: /promote {Player} {Add/Remove} {Player Name} {Permission}"));
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
					sender.sendMessage(ChatColor.DARK_RED + "Could not find world.");
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
			else
			{
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lUsage: /promote {Player} {Add/Remove} {Player Name} {Permission}"));
				return false;
			}
			
			Promote.commandPromoteRank(sender, targetName, world, permission, addMode);
		return false;
		}
		
		return true;
	}

}
