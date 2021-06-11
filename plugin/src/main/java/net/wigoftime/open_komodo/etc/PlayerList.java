package net.wigoftime.open_komodo.etc;

import net.md_5.bungee.api.ChatColor;
import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.jetbrains.annotations.NotNull;

abstract public class PlayerList 
{
	private static final String header = String.format("%s%s╋%s%s━━━━━━━━%s%s━━━━━━━━━%s %s %s%s ━━━━━━━━━━━━━━━━━╋", 
			ChatColor.YELLOW, ChatColor.STRIKETHROUGH, ChatColor.YELLOW, ChatColor.STRIKETHROUGH, ChatColor.GOLD, ChatColor.STRIKETHROUGH,
			ChatColor.YELLOW, Main.nameColoured, ChatColor.GOLD, ChatColor.STRIKETHROUGH);
	private static final String footer = String.format("%s%sOffical Website:\n%shttps://www.wigoftime.net", ChatColor.AQUA, ChatColor.BOLD, ChatColor.YELLOW);
	
	public static void add(@NotNull CustomPlayer player)
	{
		//String header = ChatColor.translateAlternateColorCodes('&', "&e&l+———&8&6———— &b&lOpen &a&lKomodo &8&6———————+");
		player.getPlayer().setPlayerListHeader(header);
		
		player.getPlayer().setPlayerListFooter(footer);
		
		if (player.getRank() == null)
			player.getPlayer().setPlayerListName(String.format("%s%s", ChatColor.GRAY, player.getPlayer().getDisplayName()));
		else
		player.getPlayer().setPlayerListName(String.format("%s%s", player.getRank().getPrefix(), player.getPlayer().getDisplayName()));
	}
}
