package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class DisplayTagsCommand extends Command
{

	public DisplayTagsCommand(String name, String description, String usageMessage,
			List<String> aliases) 
	{
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender arg0, String arg1, String[] arg2) 
	{
		return true;
	}

}
