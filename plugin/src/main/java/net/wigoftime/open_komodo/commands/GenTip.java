package net.wigoftime.open_komodo.commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class GenTip extends Command {
	
	public GenTip(String name, String description, String usageMessage,
			List<String> aliases) {
		super(name, description, usageMessage, aliases);
	}
	
	public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
		if (!sender.hasPermission(Permissions.genTipPerm))
			return false;
		
		if (args.length < 2) {
			sender.sendMessage(String.format("%s» %sNeed more arguments. /gentip (uuid) (tip)", ChatColor.GOLD, ChatColor.DARK_RED));
			return true;
		}
		
		UUID uuid = UUID.fromString(args[0]);
		float tip = Float.parseFloat(args[1]);
		
		CustomPlayer.setDonated(uuid, tip);
		
		return true;
	}

}
