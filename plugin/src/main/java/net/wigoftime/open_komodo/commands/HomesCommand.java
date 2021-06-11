package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.etc.homesystem.HomeSystem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.Home;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.ListIterator;

public class HomesCommand extends Command 
{

	public HomesCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
                        @NotNull List<String> aliases)
	{
		super(name, description, usageMessage, aliases);
		
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) 
	{
		if (!(sender instanceof Player))
			return false;
		
		// Get Player in CustomPlayer format
		CustomPlayer playerCustomPlayer = CustomPlayer.get(((Player) sender).getUniqueId());
		
		// Get a list of home names
		List<Home> homes = playerCustomPlayer.getHomes();
		
		// If there are no homes
		if (homes.size() < 1) 
		{
			// Send message that player has no homes
			playerCustomPlayer.getPlayer().sendMessage(HomeSystem.noHomes);
			return false;
		}
		
		// Create a new Stringbuilder
		StringBuilder sb = new StringBuilder();
		
		// Start adding onto the stringbuilder the title of homes
		sb.append(String.format("%s» %sHomes (Limit: %d):\n", ChatColor.GOLD, ChatColor.GRAY, playerCustomPlayer.getHomeLimit()));
		
		// loop through List of home names
		for (ListIterator<Home> iter = homes.listIterator(); iter.hasNext();) 
		{
			// Get Name of home
			Home home = iter.next();
			
			// If there are more homes after this name, add a comma
			if (iter.hasNext())
				sb.append(home.name + ", ");
			// Else don't do a comma
			else
				sb.append(home.name);
		}
		
		// Send message of their list of homes
		playerCustomPlayer.getPlayer().sendMessage(sb.toString());
		return true;
	}

}
