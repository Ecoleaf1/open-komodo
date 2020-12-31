package net.wigoftime.open_komodo.etc;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

abstract public class NickName 
{
	// Error message for not having colornick
	private static final String errorNotPermittedColor = ChatColor.DARK_RED + "Only VIP and up can color their nicknames.";
	
	
	public static void changeNick(Player player, String name) 
	{
		
		if (name == null) 
		{
			player.setCustomName(player.getName());
			return;
		}
		
		if (!Filter.checkMessage(player, name))
			return;
		
		if (player.hasPermission(Permissions.colorNickPerm)) 
		{
			player.setCustomName(ChatColor.DARK_GRAY + ChatColor.translateAlternateColorCodes('&', name));
			player.sendMessage(ChatColor.DARK_AQUA + String.format("You have changed your name to %s", player.getCustomName()));
			return;
		}
		
		// If not permitted to do colornicking
		if (!name.contains("&")) 
		{
			player.setCustomName(ChatColor.DARK_GRAY + name);
			player.sendMessage(ChatColor.DARK_AQUA + String.format("You have changed your name to %s", player.getCustomName()));
		}
		else
			player.sendMessage(errorNotPermittedColor);
	}
	
}
