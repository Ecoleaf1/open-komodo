package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.etc.HomeSystem;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class SetHomeCommand extends Command
{
	private static final String typeInName = ChatColor.translateAlternateColorCodes('&', "&4Enter a name for your home.");

	public SetHomeCommand(String name, String description, String usageMessage,
			List<String> aliases) 
	{
		super(name, description, usageMessage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) 
	{
		if (!(sender instanceof Player))
			return false;
		
		CustomPlayer playerCustomPlayer = CustomPlayer.get(((Player) sender).getUniqueId());
		
		// If there are subcommands
		if (args.length > 0)
			// Create home
			HomeSystem.createHome(playerCustomPlayer, args[0]);
		// Else send in message to enter a subcommand
		else
			playerCustomPlayer.getPlayer().sendMessage(typeInName);
		
		return true;
	}

}
