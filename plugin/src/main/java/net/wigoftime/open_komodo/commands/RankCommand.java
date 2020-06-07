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
			
			if (args[0].equalsIgnoreCase("help"))
			{
				sender.sendMessage(ChatColor.AQUA + "Rank help" + ChatColor.DARK_AQUA + ":\nRank up by roleplaying and chatting in public chat. The more often you are active in chat, the quicker you rank up. Ranking up takes time, having no one of checking when you will rank up.");
				return true;
			}
			
			if (args[0].equalsIgnoreCase("reload"))
			{
				if (!sender.hasPermission(Permissions.rankReloadPerm))
				{
					sender.sendMessage(ChatColor.DARK_RED + "You don't have permission.");
					return false;
				}
				
				Rank.reload();
				sender.sendMessage(ChatColor.DARK_AQUA+ "Ranks reloaded!");
				
				return true;
			}
			
			return false;
		}
}
