package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.etc.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FlyCommand extends Command
{
	
	private static final String flyingOff = String.format("%s» %sFlying disabled", ChatColor.GOLD, ChatColor.GRAY);
	private static final String flyingOn = String.format("%s» %sFlying enabled", ChatColor.GOLD, ChatColor.GRAY);
	
	public FlyCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
                      @NotNull List<String> aliases)
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
