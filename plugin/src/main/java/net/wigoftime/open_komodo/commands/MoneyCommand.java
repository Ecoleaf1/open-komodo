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
	
	private final String moneyInfo = String.format("%s» %sMoney Information: \nSimply earn more points when being on the server. Coins are used to obtain special features. They can be obtained starting at MVP and above, earning coins around every 40 minutes.", 
			ChatColor.GOLD, ChatColor.GRAY);
	
	@Override
	public boolean execute(CommandSender sender, String command, String[] args) {
		if (args.length < 1) return false;
		
		if (args[0].equalsIgnoreCase("help")) {
		sender.sendMessage(moneyInfo);
		return true;
		}
		
		return false;
	}

}
