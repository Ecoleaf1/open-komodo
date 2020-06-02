package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

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
				sender.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Rank up by roleplaying! The more you roleplay publically, the more likely you will rank up!");
				return true;
			}
			
			return false;
		}
}
