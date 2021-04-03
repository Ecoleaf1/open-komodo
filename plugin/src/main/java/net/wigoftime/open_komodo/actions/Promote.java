package net.wigoftime.open_komodo.actions;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.wigoftime.open_komodo.chat.MessageFormat;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.etc.ServerScoreBoard;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.Rank;
import net.wigoftime.open_komodo.sql.SQLManager;

abstract public class Promote 
{
	private static final String promoted = String.format(" \n                      %s%s| %sOpen Komodo %s|%s\n       A staff member promoted you to %%s\n ", 
			ChatColor.YELLOW, ChatColor.MAGIC, ChatColor.YELLOW, ChatColor.MAGIC, ChatColor.DARK_AQUA);
	private static final String senderPromote = ChatColor.translateAlternateColorCodes('&', "&e&lSucessfully promoted $D!");
	private static final String senderPermission = ChatColor.translateAlternateColorCodes('&', "&e&lSucessfully added permission on $D!");
	private static final String senderRemovePermission = ChatColor.translateAlternateColorCodes('&', "&e&lSucessfully removed permission on $D!");
	
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
		} else
			CustomPlayer.setRankOffline(target.getUniqueId(), Rank.getRank(rank).getID());
		
		// Message to promoter that player been promoted
		String msg2 = MessageFormat.format(senderPromote, promoter.getName(), target.getName(), null);
		promoter.sendMessage(msg2);
	}
	
	// When command for adding Player permission
	public static void commandPromotePermission(CommandSender promoter, String targetName, World world, Permission permission, boolean addMode) {
		Player targetPlayer = Bukkit.getPlayer(targetName);
		
		if (targetPlayer != null) {
			promoteOnline(promoter, targetPlayer, world, permission, addMode);
			return;
		}
		
		promoteOffline(promoter, addMode, targetName, permission, world);
	}
	
	private static void promoteOnline(CommandSender promoter, Player targetPlayer, World targetWorld, Permission targetPermission, boolean addMode) {
		CustomPlayer targetCustomPlayer = CustomPlayer.get(targetPlayer.getUniqueId());
		
		// Set Player Permission
		targetCustomPlayer.setPermission(targetPermission, targetWorld, addMode);
		
		// Refresh scoreboard
		ServerScoreBoard.add(targetCustomPlayer);
		
		String promotedMsgFormatted = MessageFormat.format(senderPermission, promoter, targetPlayer, null);
		promoter.sendMessage(promotedMsgFormatted);
		return;
	}
	
	private static void promoteOffline(CommandSender promoter, boolean addMode, String targetName, Permission targetPermission, World targetWorld) {
		OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(targetName);
				
		if (offlineTarget == null) {
			promoter.sendMessage(ChatColor.DARK_RED + "ERROR: Unknown Player!");
			return;
		}
		
		List<Permission> permissions = getPermissions(offlineTarget.getUniqueId(), targetWorld);
		
		if (permissions == null) {
			promoter.sendMessage(ChatColor.DARK_RED + "ERROR: Unknown player in database! Have they joined before?");
			return;
		}
		
		// add Permission
		if (addMode) permissions.add(targetPermission);
		else {
			boolean permissionFound = false;
			for (Permission p : permissions) {
				if (p.getName().equalsIgnoreCase(targetPermission.getName())) {
					permissions.remove(p);
					permissionFound = true;
					break;
				}
			}
			
			if (!permissionFound) {
			promoter.sendMessage(String.format("%sWarning: %s did not have the permission %s", ChatColor.GOLD, offlineTarget.getName(), targetPermission.getName()));
			return;
			}
		}
		
		setPermissions(offlineTarget.getUniqueId(), permissions, targetWorld);
		
		String promotedMsgFormatted;
		if (addMode) promotedMsgFormatted = MessageFormat.format(senderPermission, promoter.getName(), offlineTarget.getName(), null);
		else promotedMsgFormatted = MessageFormat.format(senderRemovePermission, promoter.getName(), offlineTarget.getName(), null);
		
		promoter.sendMessage(promotedMsgFormatted);
	}
	
	private static List<Permission> getPermissions(UUID uuid, World world) {
		if (SQLManager.isEnabled())
		if (world == null) return SQLManager.getGlobalPermissions(uuid);
		else return SQLManager.getWorldPermission(uuid, world.getName());
		
		// If SQL not enabled
		else
		if (world == null) return PlayerConfig.getGlobalPermissions(uuid);
		else return PlayerConfig.getWorldPermissions(uuid, world.getName());
	}
	
	private static void setPermissions(UUID uuid, List<Permission> permissions, World world) {
		if (SQLManager.isEnabled())
		if (world == null) SQLManager.setGlobalPermissions(uuid, permissions);
		else SQLManager.setWorldPermission(uuid, permissions, world.getName());
		
		// If SQL not enabled
		else
		if (world == null) PlayerConfig.setGlobalPermissions(uuid, permissions);
		else PlayerConfig.setWorldPermissions(uuid, world.getName(), permissions);
	}
}
