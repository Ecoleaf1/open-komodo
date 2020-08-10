package net.wigoftime.open_komodo.actions;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

abstract public class Rules 
{
	private static final String[] rules = {"No 18+ stuff", "No swearing (no discrimination and words behind anything inappropriate).","No bypassing the word filter to use forbidden words.", "Have fun!", "No harassment", "Please use common sense"};
	
	public static void display(CommandSender player)
	{
		// Get stringbuilder to display rules
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s» %sRules%s:\n", ChatColor.GOLD, ChatColor.GRAY, ChatColor.DARK_GRAY));
		
		// Look through each rules on server
		for (int i = 0; i < rules.length; i++)
			sb.append(String.format("    %s» %s%s\n", ChatColor.GOLD, ChatColor.GRAY, rules[i]));
		
		
		// Display rules to player
		player.sendMessage(sb.toString());
	}
}
