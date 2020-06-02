package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.Main;

public class SpawnCommand extends Command
{

	public SpawnCommand(String name, String description, String usageMessage,
			List<String> aliases) 
	{
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) 
	{
		// Don't continue if isn't player
		if (!(sender instanceof Player))
			return false;
		
		// Get player
		Player player = (Player) sender;
		
		// Teleport to fp
		player.teleport(Main.getSpawnLocation());
		
		// Set gamemode to survival
		player.setGameMode(GameMode.SURVIVAL);
		
		return true;
	}
	
	
	
}
