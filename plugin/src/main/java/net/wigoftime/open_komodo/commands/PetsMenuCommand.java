package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.gui.PetMenu;
import net.wigoftime.open_komodo.gui.PetsGui;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PetsMenuCommand extends Command
{

	public PetsMenuCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
                           @NotNull List<String> aliases) {
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		// Get sender in CustomPlayer format
		CustomPlayer player = CustomPlayer.get(((Player) sender).getUniqueId());

		PetMenu gui = new PetMenu(player);
		gui.open();
		return true;
	}

}
