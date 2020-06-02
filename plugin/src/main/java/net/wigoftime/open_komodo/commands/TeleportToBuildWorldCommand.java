package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.world.BuilderWorld;

public class TeleportToBuildWorldCommand extends Command
{

	public TeleportToBuildWorldCommand(String name, String description,
			String usageMessage, List<String> aliases) 
	{
		super(name, description, usageMessage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) 
	{
		if (!(sender instanceof Player))
			return false;
		
		// Get player in Player format
		Player player = (Player) sender;
		
		// If player is permitted to go to builderworld
		if (player.hasPermission(Permissions.tpBuilderWorld))
			BuilderWorld.joinWorld(player);
		else
			player.sendMessage(Permissions.builderWorldNotPerm);
		
		return true;
	}

}
