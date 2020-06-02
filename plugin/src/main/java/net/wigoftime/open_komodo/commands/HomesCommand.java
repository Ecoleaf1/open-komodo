package net.wigoftime.open_komodo.commands;

import java.util.List;
import java.util.ListIterator;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.etc.HomeSystem;

public class HomesCommand extends Command 
{

	public HomesCommand(String name, String description, String usageMessage,
			List<String> aliases) 
	{
		super(name, description, usageMessage, aliases);
		
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) 
	{
		if (!(sender instanceof Player))
			return false;
		
		// Get Player
		Player player = (Player) sender;
		
		// Get a list of home names
		List<String> homes = HomeSystem.getHomes(player);
		
		// If there are no homes
		if (homes.size() < 1) 
		{
			// Send message that player has no homes
			player.sendMessage(HomeSystem.noHomes);
			return false;
		}
		
		// Create a new Stringbuilder
		StringBuilder sb = new StringBuilder();
		
		// Start adding onto the stringbuilder the title of homes
		sb.append("Homes: ");
		
		// loop through List of home names
		for (ListIterator<String> iter = homes.listIterator(); iter.hasNext();) 
		{
			// Get Name of home
			String name = iter.next();
			
			// If there are more homes after this name, add a comma
			if (iter.hasNext())
				sb.append(name + ", ");
			// Else don't do a comma
			else
				sb.append(name);
		} 
		
		// Send message of their list of homes
		player.sendMessage(sb.toString());
		return true;
	}

}
