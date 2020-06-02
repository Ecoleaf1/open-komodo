package net.wigoftime.open_komodo.etc;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

abstract public class PlayerList 
{
	public static void add(Player player)
	{
		String header = ChatColor.translateAlternateColorCodes('&', "&e&l+———&8&6———— &b&lOpen &a&lKomodo &8&6———————+");
		player.setPlayerListHeader(header);
		
		String footer = ChatColor.translateAlternateColorCodes('&', "&b&lOffical Website:\n&ewww.wigoftime.net");
		player.setPlayerListFooter(footer);
	}
}
