package net.wigoftime.open_komodo.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.gui.PetsGui;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class PetCommand extends Command {

	public PetCommand() {
		super("pet");
	}

	@Override
	public boolean execute(CommandSender sender,String command,String[] args) {
		
		if (!(sender instanceof Player))
			return false;
		
		CustomPlayer player = CustomPlayer.get(((Player) sender).getUniqueId());
		
		PetsGui gui = new PetsGui(player);
		gui.open();
		return false;
	}

}
