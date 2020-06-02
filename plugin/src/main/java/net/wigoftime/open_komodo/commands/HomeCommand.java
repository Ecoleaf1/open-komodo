package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.etc.HomeSystem;

public class HomeCommand extends Command
{
	private static final String enterNameMsg = ChatColor.translateAlternateColorCodes('&', "&4Enter a name of your home.");

	public HomeCommand(String name, String description, String usageMessage,
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
		
		Player player = (Player) sender;
		
		// If there isn't enough subcommands
		if (args.length == 0) 
		{
			// Tell player to enter subcommand
			player.sendMessage(enterNameMsg);
			return false;
		}
		
		// Teleport home
		HomeSystem.teleportHome(player, args[0]);
		return true;
	}

}
