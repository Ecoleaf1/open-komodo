package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.ChatColor;
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
	
	private final String teleportedMsg = String.format("%s» %steleported to %s%sSpawn", 
			ChatColor.GOLD, ChatColor.GRAY, ChatColor.GOLD, ChatColor.BOLD);

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) {
		// Don't continue if isn't player
		if (!(sender instanceof Player))
			return false;
		
		// Get player
		Player player = (Player) sender;
		
		// Teleport to fp
		player.teleport(Main.getSpawnLocation());
		
		player.sendMessage(teleportedMsg);
		
		// Set gamemode to survival
		player.setGameMode(GameMode.SURVIVAL);
		
		return true;
	}
	
	
	
}
