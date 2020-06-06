package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.etc.HomeSystem;

public class DelHomeCommand extends Command
{
	private static final String nameMsg = ChatColor.translateAlternateColorCodes('&', "&4Enter your home name that you wish to delete.");
	private static final String homeDeleted = ChatColor.translateAlternateColorCodes('&', "&7Home deleted!");

	public DelHomeCommand(String name, String description, String usageMessage,
			List<String> aliases) {
		super(name, description, usageMessage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) 
	{
		if (!(sender instanceof Player))
			return false;
		
		// Get player
		Player player = (Player) sender;
		
		// If there aren't any subcommands
		if (args.length < 1) 
		{
			// Tell player to enter a subcommand
			player.sendMessage(nameMsg);
			return false;
		}
		
		// Delete home
		HomeSystem.deleteHome(player, args[0]);
		player.sendMessage(homeDeleted);
		return false;
	}}
