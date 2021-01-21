package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.wigoftime.open_komodo.etc.Permissions;

public class ModTeleportHereCommand extends Command {

	public ModTeleportHereCommand(String name, String description, String usageMessage, List<String> aliases) {
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] commandArguments) {
		if (!(sender instanceof Player))
			return false;
	
		Player senderPlayer = (Player) sender;
		
		if (!senderPlayer.getPlayer().hasPermission(Permissions.modteleport))
			return false;
		
		if (commandArguments.length < 1) {
			senderPlayer.sendMessage(String.format("%s» %sUsage: %s", ChatColor.GOLD, ChatColor.GRAY, this.usageMessage));
			return false;
		}
		
		Player targetPlayer = Bukkit.getPlayer(commandArguments[0]);
		
		if (targetPlayer == null)
			return false;
		
		targetPlayer.teleport(senderPlayer);
		senderPlayer.sendMessage(String.format("%s» %sTeleported %s%s%s to your location", 
				ChatColor.GOLD, ChatColor.GRAY, ChatColor.GOLD, senderPlayer.getDisplayName(),
				ChatColor.GRAY));
		
		targetPlayer.sendMessage(String.format("%s» %sA mod or higher has teleported you", 
				ChatColor.GOLD, ChatColor.GRAY));
		return true;
	}


}
