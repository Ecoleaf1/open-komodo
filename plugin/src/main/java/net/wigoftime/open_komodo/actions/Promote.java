package net.wigoftime.open_komodo.actions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.wigoftime.open_komodo.chat.MessageFormat;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.etc.ServerScoreBoard;
import net.wigoftime.open_komodo.objects.Rank;

abstract public class Promote 
{
	private static final String promoted = ChatColor.translateAlternateColorCodes('&', "&e&lYou've been promoted to $G!");
	private static final String senderPromote = ChatColor.translateAlternateColorCodes('&', "&e&lSucessfully promoted $D!");
	private static final String senderPermission = ChatColor.translateAlternateColorCodes('&', "&e&lSucessfully added permission on $D!");
	
	public static boolean promoteRank(Player player, String rankName)
	{
		// Check if rank exists
		if (Rank.getRank(rankName) == null)
			return false;
		
		PlayerConfig.setRank(player, rankName);
		return true;
	}
	
	// When command for adding Player to rank
	public static void commandPromoteRank(CommandSender promoter, String targetName, String rank)
	{
		// Check if rank exists
		if (Rank.getRank(rank) == null && !rank.equalsIgnoreCase("default"))
		{
			promoter.sendMessage(ChatColor.DARK_RED + "ERROR: Unknown Rank!");
			return;
		}
		
		// Get player by name
		Player target = Bukkit.getPlayer(targetName);
		
		// Check if player is online/exists
		if (target == null)
		{
			promoter.sendMessage(ChatColor.DARK_RED + "ERROR: Unknown Player!");
			return;
		}
		
		// Setup rank and then their permissions
		PlayerConfig.setRank(target, rank);
		Permissions.setUp(target);
		
		// Message to target that they been promoted
		String msg = MessageFormat.format(target, promoted);
		target.sendMessage(msg);
		
		// Message to promoter that player been promoted
		String msg2 = MessageFormat.format(senderPromote, promoter, target, null);
		promoter.sendMessage(msg2);
	}
	
	// When command for adding Player permission
	public static void commandPromoteRank(Player promoter, String targetName, Permission permission, boolean addMode)
	{
		// Get player by name
		Player target = Bukkit.getPlayer(targetName);
		
		// Check if player is online/exists
		if (target == null)
		{
			promoter.sendMessage(ChatColor.DARK_RED + "ERROR: Unknown Player!");
			return;
		}
		
		// Set Player Permission
		PlayerConfig.setPermission(target, null, permission, addMode);
		Permissions.setUp(target);
		
		// Send to promoter that the command worked
		String msg2 = MessageFormat.format(senderPermission, promoter, null);
		promoter.sendMessage(msg2);
		
		// Refresh scoreboard
		ServerScoreBoard.add(target);
	}
	
	// When command for adding Player permission
	public static void commandPromoteRank(CommandSender promoter, String targetName, World world, Permission permission, boolean addMode)
	{
		// Get player by name
		Player target = Bukkit.getPlayer(targetName);
		
		// Check if player is online/exists
		if (target == null)
		{
			promoter.sendMessage(ChatColor.DARK_RED + "ERROR: Unknown Player!");
			return;
		}
		
		// Set Player Permission
		if (world == null)
			PlayerConfig.setPermission(target, null, permission, addMode);
		else
			PlayerConfig.setPermission(target, world, permission, addMode);
		Permissions.setUp(target);
		
		// Send to promoter that the command worked
		String msg2 = MessageFormat.format(senderPermission, promoter, target, null);
		promoter.sendMessage(msg2);
		
		// Refresh scoreboard
		ServerScoreBoard.add(target);
	}
}
