package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class ModTeleportCommand extends Command {

	public ModTeleportCommand(String name, String description, String usageMessage, List<String> aliases) {
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
		
		senderPlayer.teleport(targetPlayer);
		senderPlayer.sendMessage(String.format("%s» %sTeleported to %s%s", ChatColor.GOLD, ChatColor.GRAY, ChatColor.GOLD, senderPlayer.getDisplayName()));
		return true;
	}

}
