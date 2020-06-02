package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.actions.Sit;

public class SitCommand extends Command
{

	public SitCommand(String name, String description, String usageMessage,
			 List<String> aliases) 
	{
		super(name, description, usageMessage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) {
		if (!(sender instanceof Player))
		{
			return false;
		}
		
		Player player = (Player) sender;
		
		Sit.sit(player);
		return true;
	}

}
