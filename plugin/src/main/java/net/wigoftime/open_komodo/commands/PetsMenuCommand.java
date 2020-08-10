package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.gui.PetsGui;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class PetsMenuCommand extends Command
{

	public PetsMenuCommand(String name, String description, String usageMessage,
			List<String> aliases) {
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		// Get sender in CustomPlayer format
		CustomPlayer player = CustomPlayer.get(((Player) sender).getUniqueId());
		
		PetsGui gui = new PetsGui(player);
		gui.open();
		return true;
	}

}
