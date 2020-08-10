package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.objects.CustomPlayer;

public class HomeCommand extends Command
{
	private static final String enterNameMsg = String.format("%s» %sPlease enter a name for your home.", ChatColor.GOLD, ChatColor.GRAY);

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
		
		CustomPlayer playerCustomPlayer = CustomPlayer.get(((Player) sender).getUniqueId());
		
		// If there isn't enough arguments
		if (args.length < 1) {
			// Tell player to enter arguments
			playerCustomPlayer.getPlayer().sendMessage(enterNameMsg);
			return false;
		}
		
		if (args[0].equalsIgnoreCase("help")) {
			sender.sendMessage(String.format("%s» %sHome Help:\nHomes are a way to save your location. Different ranks can have different home limit.\\nTo create a home, type in: /sethome (Name)\nTo delete a home, type in /delhome (Name)\nTo go to a home, type in /home (name)\nDisplay your current homes: /homes", 
					ChatColor.GOLD, ChatColor.GRAY));
			return true;
		}
		
		// Teleport home
		playerCustomPlayer.teleportHome(args[0]);
		return true;
	}

}
