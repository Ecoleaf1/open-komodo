package net.wigoftime.open_komodo.etc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.Rank;
import net.wigoftime.open_komodo.sql.SQLManager;

abstract public class Permissions 
{
	private static HashMap<UUID, PermissionAttachment> permMap = new HashMap<UUID, PermissionAttachment>();
	
	public static final Permission flyPermission = new Permission("openkomodo.abilities.flight");
	public static final String notPermFlyingError = ChatColor.DARK_RED + "Flying is permitted to MVP and above";
	
	public static final Permission colorNickPerm = new Permission("openkomodo.abilities.colornick");
	public static final Permission moreColorNickPerm = new Permission("openkomodo.abilities.morecolornick");
	
	public static final Permission placePerm = new Permission("openkomodo.build.place");
	private static final String placePermError = ChatColor.translateAlternateColorCodes('&', "&c&lHEY!&r&7 Sorry, but you cannot place blocks here.");
	
	public static final Permission breakPerm = new Permission("openkomodo.build.break");
	private static final String breakPermError = ChatColor.translateAlternateColorCodes('&', "&c&lHEY!&r&7 Sorry, but you cannot break blocks here.");
	
	public static final Permission changePerm = new Permission("openkomodo.build.change");
	private static final String changeError = ChatColor.translateAlternateColorCodes('&', "&c&lHEY!&r&7 Sorry, but you cannot change that here.");
	
	public static final Permission hurtPerm = new Permission("openkomodo.build.hurt");
	private static final String hurtError = ChatColor.translateAlternateColorCodes('&', "&c&lHEY!&r&7 Sorry, but you cannot damage that here.");
	
	public static final Permission buildModePerm = new Permission("openkomodo.build.buildmode");
	
	public static final Permission dropPerm = new Permission("openkomodo.build.drop");
	private static final String dropError = ChatColor.translateAlternateColorCodes('&', "&c&lHEY!&r&7 Sorry, but you cannot drop that here.");
	
	public static final String useError = ChatColor.translateAlternateColorCodes('&', "&c&lHEY!&r&7 Sorry, but you cannot use that.");
	
	public static final Permission ignoreSwearPerm = new Permission("openkomodo.chat.ignoreswear");
	public static final Permission ignoreSpamPerm = new Permission("openkomodo.chat.ignorespam");
	
	public static final Permission unmutePerm = new Permission("openkomodo.chat.unmute");
	public static final String notPermUnmute = ChatColor.translateAlternateColorCodes('&', "You are not permitted to unmute.");
	
	public static final Permission tpBuilderWorld = new Permission("openkomodo.teleport.builderworld");
	public static final String builderWorldNotPerm = ChatColor.translateAlternateColorCodes('&', "&c&lHEY!&r&7 Sorry, but you are not permitted to go to builder's world.");
	
	public static final Permission promotePerm = new Permission("openkomodo.admin.promote");
	public static final String promotePermError = ChatColor.translateAlternateColorCodes('&', "&c&lHEY!&r&7 Sorry, but you are not permitted to do that.");
	
	public static final Permission genPayPerm = new Permission("openkomodo.admin.genpay");
	public static final Permission genTipPerm = new Permission("openkomodo.console.gentip");
	public static final Permission emoteReloadPerm = new Permission("openkomodo.admin.emote.reload");
	public static final Permission rankReloadPerm = new Permission("openkomodo.admin.rank.reload");
	
	public static final Permission modteleport = new Permission("openkomodo.mod.teleport");
	public static final Permission mutePerm = new Permission("openkomodo.mod.mute");
	public static final Permission banPerm = new Permission("openkomodo.mod.ban");
	public static final Permission kickPerm = new Permission("openkomodo.mod.kick");
	public static final Permission invis = new Permission("openkomodo.mod.invisible");
	public static final Permission seeOtherInvis = new Permission("openkomodo.mod.seeotherinvisible");
	public static final Permission chatmonitor = new Permission("openkomodo.mod.chatmonitor");
	
	public static final Permission abuseMonitorPerm = new Permission("openkomodo.manager.monitorabuse");
	
	public static final Permission petAccess = new Permission("openkomodo.pets.access");
	public static final Permission particleAccess = new Permission("openkomodo.particles.access");
	
	public static String getPlaceError() 
	{
		return placePermError;
	}
	
	public static String getBreakError() 
	{
		return breakPermError;
	}
	
	public static String getChangeError() 
	{
		return changeError;
	}
	
	public static String getHurtError() 
	{
		return hurtError;
	}
	
	public static String getDropError() 
	{
		return dropError;
	}
	
	public static void addPermission(CustomPlayer customPlayer, Permission permission)
	{
		PermissionAttachment attachment = permMap.get(customPlayer.getUniqueId());
		attachment.setPermission(permission, true);
		
		permMap.replace(customPlayer.getUniqueId(), attachment);
		customPlayer.getPlayer().recalculatePermissions();
	}
	
	public static void removePermission(CustomPlayer customPlayer, Permission permission)
	{
		PermissionAttachment attachment = permMap.get(customPlayer.getUniqueId());
		attachment.unsetPermission(permission);
		
		permMap.replace(customPlayer.getUniqueId(), attachment);
		customPlayer.getPlayer().recalculatePermissions();
	}
	
	public static void setUp(CustomPlayer customPlayer) 
	{
		PrintConsole.test("Setup Permissions");
		// Get and load Player's configuration
		//File file = PlayerConfig.getPlayerConfig(customPlayer.getPlayer());
		//YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		// Get Player's Rank
		Rank rank = customPlayer.getRank();
		
		// Get list of permissions inherited by Player's Rank
		List<Permission> rankPermissions;
		if (rank == null) {
			rankPermissions = new ArrayList<Permission>();
		} else
			rankPermissions = rank.getPermissions();
		
		// Permissions inherited by Player
		List<Permission> playerPermissions;
		// World permissions inherited by Player
		List<Permission> worldPermissions;
		
		if (SQLManager.isEnabled())
		{
			// Get player's global permissions
			playerPermissions = SQLManager.getGlobalPermissions(customPlayer.getUniqueId());
			
			// Get player's world permissions
			worldPermissions = SQLManager.getWorldPermission(customPlayer.getUniqueId(), customPlayer.getPlayer().getWorld().getName());
		}
		else
		{
			// Get player's global permissions
			playerPermissions = PlayerConfig.getGlobalPermissions(customPlayer.getUniqueId());
			
			// Get player's world permissions
			worldPermissions = PlayerConfig.getWorldPermissions(customPlayer.getUniqueId(), customPlayer.getPlayer().getWorld().getName());
		}
		
		// Get list of world permissions inherited by Player's Rank
		List<Permission> worldPermRank;
		if (rank == null)
			worldPermRank = new ArrayList<Permission>();
		else
			worldPermRank = rank.getWorldPermissions(customPlayer.getPlayer().getWorld());
		
		PermissionAttachment attachment;
		attachment = permMap.get(customPlayer.getPlayer().getUniqueId());
		
		if (attachment != null)
			attachment.remove();
		
		// If op, turn it to false to sync with permissions
		if (customPlayer.getPlayer().isOp())
			Bukkit.getScheduler().runTask(Main.getPlugin(), new Runnable() {
				public void run() {
					customPlayer.getPlayer().setOp(false);
				}
			});
		
		// Get Player's permission attachment
		attachment = customPlayer.getPlayer().addAttachment(Main.getPlugin());
		permMap.put(customPlayer.getPlayer().getUniqueId(), attachment);
		
		// Add Rank Permissions to the Attachment
		if (rankPermissions != null)
			for (Permission p : rankPermissions)
				if (p.getName().equals("*"))
					Bukkit.getScheduler().runTask(Main.getPlugin(), new Runnable() {
						public void run() {
							customPlayer.getPlayer().setOp(true);
						}
					});
				else if (p.getName().startsWith("openkomodo.home.limit"))
				{
					String string = p.getName();
					
					char[] chars = new char[string.length() - 22];
					 string.getChars(22, string.length(), chars, 0);
					
					 StringBuilder sb = new StringBuilder();
					 for (char c : chars)
					 {
						 sb.append(c);
					 }
					 
					 customPlayer.setHomeLimit(Integer.parseInt(sb.toString()));
				}
				else
					attachment.setPermission(p, true);
		
		// Add Permissions to the Attachment
		for (Permission p : playerPermissions)
			attachment.setPermission(p, true);
		
		for (Permission p : worldPermissions)
			attachment.setPermission(p, true);
		
		if (worldPermRank == null)
			return;
		
		for (Permission p : worldPermRank)
			attachment.setPermission(p, true);
	}
}
