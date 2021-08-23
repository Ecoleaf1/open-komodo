package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.gui.PetMenu;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PetCommand extends Command {

	public PetCommand() {
		super("pet");
	}

	@Override
	public boolean execute(CommandSender sender,String command,String[] args) {
		
		if (!(sender instanceof Player))
			return false;
		
		CustomPlayer player = CustomPlayer.get(((Player) sender).getUniqueId());

		PetMenu gui = new PetMenu(player);
		gui.open();
		return false;
	}

}
