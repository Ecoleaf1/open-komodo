package net.wigoftime.open_komodo.etc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.objects.Rank;

abstract public class Permissions 
{
	private static HashMap<UUID, PermissionAttachment> permMap = new HashMap<UUID, PermissionAttachment>();
	
	public static final Permission flyPermission = new Permission("openkomodo.abilities.flight");
	public static final String notPermFlyingError = ChatColor.DARK_RED + "Flying is permitted to MVP and above";
	
	public static final Permission colorNickPerm = new Permission("openkomodo.abilities.colornick");
	
	public static final Permission placePerm = new Permission("openkomodo.build.place");
	private static final String placePermError = ChatColor.translateAlternateColorCodes('&', "&c&lHEY!&r&7 Sorry, but you cannot place blocks here.");
	
	public static final Permission breakPerm = new Permission("openkomodo.build.break");
	private static final String breakPermError = ChatColor.translateAlternateColorCodes('&', "&c&lHEY!&r&7 Sorry, but you cannot break blocks here.");
	
	public static final Permission changePerm = new Permission("openkomodo.build.change");
	private static final String changeError = ChatColor.translateAlternateColorCodes('&', "&c&lHEY!&r&7 Sorry, but you cannot change that here.");
	
	public static final Permission hurtPerm = new Permission("openkomodo.build.hurt");
	private static final String hurtError = ChatColor.translateAlternateColorCodes('&', "&c&lHEY!&r&7 Sorry, but you cannot damage that here.");
	
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
	
	public static final Permission mutePerm = new Permission("openkomodo.mod.mute");
	public static final Permission banPerm = new Permission("openkomodo.mod.ban");
	public static final Permission kickPerm = new Permission("openkomodo.mod.kick");
	
	public static final Permission abuseMonitorPerm = new Permission("openkomodo.manager.monitorabuse");
	
	public static final Permission petAccess = new Permission("openkomodo.pets.access");
	
	//public static final Permission BuilderBuildPerm = new Permission("fp.worlds.builderworld.build");
	/*
	public static final Permission builderBreakPerm = new Permission("fp.worlds.builderworld.break");
	public static final String builderBreak = ChatColor.translateAlternateColorCodes('&', "&c&lHEY!&r&7 Sorry, but you cannot break blocks here.");
	public static final Permission builderPlacePerm = new Permission("fp.worlds.builderworld.place");
	public static final String builderPlace = ChatColor.translateAlternateColorCodes('&', "&c&lHEY!&r&7 Sorry, but you cannot place blocks here."); */
	
	
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
	
	
	public static void setUp(Player player) 
	{
		// Get and load Player's configuration
		File file = PlayerConfig.getPlayerConfig(player);
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		// Get Player's Rank
		String rankStr = config.getConfigurationSection("General").getString("Rank");
		Rank rank = Rank.getRank(rankStr);
		
		// Get list of permissions inherited by Player's Rank
		List<Permission> rankPermissions;
		if (rank == null)
			rankPermissions = new ArrayList<Permission>();
		else
			rankPermissions = rank.getPermissions();
		
		// Get permissions inherited by Player
		List<String> playerPermissions = PlayerConfig.getPermissions(player);
		
		// Get world permissions inherited by Player
		List<Permission> worldPermissions = PlayerConfig.getPermission(player, player.getWorld());
		
		// Get list of world permissions inherited by Player's Rank
		List<Permission> worldPermRank;
		if (rank == null)
			worldPermRank = new ArrayList<Permission>();
		else
			worldPermRank = rank.getWorldPermissions(player.getWorld());
		
		PermissionAttachment attachment;
		attachment = permMap.get(player.getUniqueId());
		
		if (attachment != null)
			attachment.remove();
		
		// If op, turn it to false to sync with permissions
		if (player.isOp())
			player.setOp(false);
		
		// Get Player's permission attachment
		attachment = player.addAttachment(Main.getPlugin());
		permMap.put(player.getUniqueId(), attachment);
		
		// Add Rank Permissions to the Attachment
		if (rankPermissions != null)
			for (Permission p : rankPermissions)
				if (p.getName().equals("*"))
					player.setOp(true);
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
					 
					 PlayerConfig.setHomeLimit(player.getUniqueId(), Integer.parseInt(sb.toString()));
				}
				else
					attachment.setPermission(p, true);
		
		// Add Permissions to the Attachment
		for (String s : playerPermissions)
			attachment.setPermission(new Permission(s), true);
		
		for (Permission p : worldPermissions)
			attachment.setPermission(p, true);
		
		if (worldPermRank == null)
			return;
		
		for (Permission p : worldPermRank)
			attachment.setPermission(p, true);
	}
}
