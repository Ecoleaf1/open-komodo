package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.objects.Rank;

public class RankCommand extends Command
{
	public RankCommand(String name, String description, String usageMessage,
			 List<String> aliases) 
		{
			super(name, description, usageMessage, aliases);
		}

		@Override
		public boolean execute(CommandSender sender, String command, String[] args) 
		{
			if (args.length < 1)
				return false;
			
			if (args[0].equalsIgnoreCase("help")) {
				sender.sendMessage(String.format("%s» %sRank Help:\n%s» %sEarn XP to get a new rank by exploring the map and talking! To view your progress, type in /xp", 
						ChatColor.GOLD, ChatColor.AQUA, ChatColor.GOLD, ChatColor.DARK_AQUA));
				return true;
			}
			
			if (args[0].equalsIgnoreCase("reload"))
			{
				if (!sender.hasPermission(Permissions.rankReloadPerm))
				{
					sender.sendMessage(String.format("%s» %sYou are not permitted to use that command", ChatColor.GOLD, ChatColor.DARK_RED));
					return true;
				}
				
				Rank.reload();
				sender.sendMessage(String.format("%s» %sRanks reloaded!", ChatColor.GOLD, ChatColor.DARK_GREEN));
				return true;
			}
			
			return true;
		}
}
