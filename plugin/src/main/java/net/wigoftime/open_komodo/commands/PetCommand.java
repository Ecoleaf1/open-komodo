package net.wigoftime.open_komodo.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.gui.PetsGui;

public class PetCommand extends Command
{

	public PetCommand() {
		super("pet");
	}

	@Override
	public boolean execute(CommandSender sender,String command,String[] args) {
		
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		PetsGui.create(player);
		return false;
	}

}
