package net.wigoftime.open_komodo.commands;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.etc.InventoryManagement;
import net.wigoftime.open_komodo.etc.Permissions;

public class BuildModeCommand extends Command
{
	public static Set<UUID> buildMode = new HashSet<UUID>();

	public BuildModeCommand(String name, String description, String usageMessage,
			List<String> aliases) {
		
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) 
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.DARK_RED + "Only a player can use this command");
			return false;
		}
		
		if (!sender.hasPermission(Permissions.buildModePerm))
		{
			sender.sendMessage(ChatColor.DARK_RED + "You don't have permission to go to build mode.");
			return false;
		}
		
		Player player = (Player) sender;
		
		toggleBuild(player);
		return true;
	}
	
	public static void toggleBuild(Player player)
	{
		if (buildMode.contains(player.getUniqueId()))
		{
			buildMode.remove(player.getUniqueId());
			player.setGameMode(GameMode.SURVIVAL);
			InventoryManagement.loadInventory(player, player.getWorld());
		}
		else
		{
			InventoryManagement.saveInventory(player, player.getWorld());
			player.getInventory().clear();
			
			player.setGameMode(GameMode.CREATIVE);
			buildMode.add(player.getUniqueId());
		}
		
		return;
	}

}
