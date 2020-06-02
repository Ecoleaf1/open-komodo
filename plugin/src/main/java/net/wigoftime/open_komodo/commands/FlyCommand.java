package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.etc.Permissions;

public class FlyCommand extends Command
{
	
	private static final String flyingOff = ChatColor.GRAY + "Flying off.";
	private static final String flyingOn = ChatColor.GRAY + "Flying on.";
	
	public FlyCommand(String name, String description, String usageMessage,
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
		
		// If player has flight permissions
		if (!player.hasPermission(Permissions.flyPermission))
		{
			player.sendMessage(Permissions.notPermFlyingError);
			return false;
		}
			
		if (player.getAllowFlight()) 
		{
			player.setAllowFlight(false);
			player.setFlying(false);
			player.sendMessage(flyingOff);
			
			return false;
		}
	
		player.setAllowFlight(true);
		player.setFlying(true);
		player.sendMessage(flyingOn);
		
		return true;
	}
	

}
