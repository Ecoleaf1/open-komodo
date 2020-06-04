package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MoneyCommand extends Command 
{

	public MoneyCommand(String name, String description, String usageMessage,
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
		sender.sendMessage(ChatColor.AQUA + "Money information" + ChatColor.DARK_AQUA + ":\nSimply earn more points when being on the server. Coins are unobtainable as of right now"
				+ "\nRank up to get a higher salery");
		return true;
		}
		
		return false;
	}

}
