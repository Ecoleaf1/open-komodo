package net.wigoftime.open_komodo.commands;

import java.util.List;

import net.wigoftime.open_komodo.etc.systems.ModerationSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.etc.Permissions;

public class KickCommand extends Command 
{
	public KickCommand(String name, String description, String usageMessage,
		 List<String> aliases) {
		super(name, description, usageMessage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) 
	{
		if (!sender.hasPermission(Permissions.kickPerm))
		{
			sender.sendMessage(ChatColor.DARK_RED + "You are not permitted to use that command.");
			return false;
		}
		
		if (args.length < 1)
		{
			sender.sendMessage(ChatColor.GOLD + "" + this.getUsage());
			return false;
		}
		
		Player target = Bukkit.getPlayer(args[0]);
		
		if (target == null) {
			sender.sendMessage(ChatColor.DARK_RED+"Player not found");
			return false;
		}
		
		if (args.length > 1) {
			StringBuilder sb = new StringBuilder();
			
			for (int i = 0; i < args.length; i++) {
				if (i == 0)
					continue;
				
				sb.append(args[i] + " ");
			}
			
			target.kickPlayer(ChatColor.DARK_RED + "You have been kicked for: "+ sb.toString());
			ModerationSystem.sendCauserResultsKick(target, sender, sb.toString());
		}
		else {
			target.kickPlayer("You have been kicked.");
			ModerationSystem.sendCauserResultsKick(target, sender, null);
		}
		
		return true;
	}

}
