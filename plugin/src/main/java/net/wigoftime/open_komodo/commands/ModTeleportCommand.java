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
	
		Player player = (Player) sender;
		
		if (!player.getPlayer().hasPermission(Permissions.modteleport))
			return false;
		
		if (commandArguments.length > 0) {
			player.sendMessage(String.format("%s» %sUsage: %s", ChatColor.GOLD, ChatColor.GRAY, this.usageMessage));
			return false;
		}
		
		Player targetPlayer = Bukkit.getPlayer(commandArguments[0]);
		
		if (targetPlayer == null)
			return false;
		
		player.teleport(targetPlayer);
		player.sendMessage(String.format("%s» %sTeleported to %s%s", ChatColor.GOLD, ChatColor.GRAY, ChatColor.GOLD, player.getDisplayName()));
		return true;
	}

}
