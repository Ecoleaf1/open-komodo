package net.wigoftime.open_komodo.actions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.wigoftime.open_komodo.chat.MessageFormat;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.etc.ServerScoreBoard;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.Rank;

abstract public class Promote 
{
	private static final String promoted = String.format(" \n                      %s%s| %sOpen Komodo %s|%s\n       A staff member promoted you to %%s\n ", 
			ChatColor.YELLOW, ChatColor.MAGIC, ChatColor.YELLOW, ChatColor.MAGIC, ChatColor.DARK_AQUA);
	private static final String senderPromote = ChatColor.translateAlternateColorCodes('&', "&e&lSucessfully promoted $D!");
	private static final String senderPermission = ChatColor.translateAlternateColorCodes('&', "&e&lSucessfully added permission on $D!");
	
	/*
	public static boolean promoteRank(OfflinePlayer player, String rankName)
	{
		Rank rank = Rank.getRank(rankName);
		
		// Check if rank doesn't exist
		if (rank == null)
			return false;
		
		CustomPlayer.setRankOffline(player.getUniqueId(), rank);
		return true;
	}*/
	
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
		OfflinePlayer target = Bukkit.getPlayer(targetName);
		
		// Check if player is online/exists
		if (target == null)
		{
			// Get offline player
			target = Bukkit.getOfflinePlayer(targetName);
			
			if (target == null)
			{
				promoter.sendMessage(ChatColor.DARK_RED + "ERROR: Unknown Player!");
				return;
			}
		}
		
		if (target.isOnline()) {
			CustomPlayer customTargetPlayer = CustomPlayer.get(target.getUniqueId());
			customTargetPlayer.setRank(Rank.getRank(rank));
				
			// Message to target that they been promoted
			String msg = String.format(promoted, customTargetPlayer.getRank() == null ? "" : customTargetPlayer.getRank().getPrefix());
			customTargetPlayer.getPlayer().sendMessage(msg);
			return;
		} else
			CustomPlayer.setRankOffline(target.getUniqueId(), Rank.getRank(rank).getID());
		
		// Message to promoter that player been promoted
		String msg2 = MessageFormat.format(senderPromote, promoter.getName(), target.getName(), null);
		promoter.sendMessage(msg2);
	}
	
	// When command for adding Player permission
	public static void commandPromoteRank(Player promoterPlayer, String targetName, Permission permission, boolean addMode)
	{
		// Get target (in Player format) by name
		Player targetPlayer = Bukkit.getPlayer(targetName);
		
		// Check if player is online/exists
		if (targetPlayer == null) {
			promoterPlayer.sendMessage(ChatColor.DARK_RED + "ERROR: Unknown Player!");
			return;
		}
		
		// get target Player in CustomPlayer format.
		CustomPlayer targetCustomPlayer = CustomPlayer.get(targetPlayer.getUniqueId());
		
		// get promoter Player in CustomPlayer format.
		CustomPlayer promoterCustomPlayer = CustomPlayer.get(promoterPlayer.getUniqueId());
		
		// Set Player Permission
		//PlayerConfig.setPermission(targetPlayer, null, permission, addMode);
		//Permissions.setUp(targetCustomPlayer);
		targetCustomPlayer.setPermission(permission, null, addMode);
		
		// Send to promoter that the command worked
		String msg2 = MessageFormat.format(senderPermission, promoterCustomPlayer, null);
		promoterPlayer.sendMessage(msg2);
		
		// Refresh scoreboard
		ServerScoreBoard.add(targetCustomPlayer);
	}
	
	// When command for adding Player permission
	public static void commandPromoteRank(CommandSender promoter, String targetName, World world, Permission permission, boolean addMode)
	{
		// Get player by name
		Player targetPlayer = Bukkit.getPlayer(targetName);
		
		// Check if player is online/exists
		if (targetPlayer == null) {
			promoter.sendMessage(ChatColor.DARK_RED + "ERROR: Unknown Player!");
			return;
		}
		
		CustomPlayer targetCustomPlayer = CustomPlayer.get(targetPlayer.getUniqueId());
		
		// Set Player Permission
		targetCustomPlayer.setPermission(permission, world, addMode);
		//PlayerConfig.setPermission(targetPlayer, world, permission, addMode);
		
		//Permissions.setUp(targetCustomPlayer);
		
		// Send to promoter that the command worked
		String msg2 = MessageFormat.format(senderPermission, promoter, targetPlayer, null);
		promoter.sendMessage(msg2);
		
		// Refresh scoreboard
		ServerScoreBoard.add(targetCustomPlayer);
	}
}
