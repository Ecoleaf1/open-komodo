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
		
		// If there isn't enough arguments
		if (args.length < 1) 
		{
			// Tell player to enter arguments
			player.sendMessage(enterNameMsg);
			return false;
		}
		
		if (args[0].equalsIgnoreCase("help"))
		{
			sender.sendMessage(ChatColor.AQUA +"Home Help:\n" + ChatColor.DARK_AQUA+ "Homes are a way to save your location. Different ranks can have different home limit.\nTo create a home, type in: /sethome (Name)\nTo delete a home, type in /delhome (Name)\nTo go to a home, type in /home (name)\nDisplay your current homes: /homes");
			return true;
		}
		
		// Teleport home
		HomeSystem.teleportHome(player, args[0]);
		return true;
	}

}
