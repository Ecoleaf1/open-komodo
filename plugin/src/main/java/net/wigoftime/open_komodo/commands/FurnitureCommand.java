package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.gui.CustomGUI;
import net.wigoftime.open_komodo.gui.FurnitureMenu;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FurnitureCommand extends Command {

	public FurnitureCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		Player player = (Player) sender;
		
		CustomGUI gui = new FurnitureMenu(CustomPlayer.get(player.getUniqueId()), null, "default");
		gui.open();
		return true;
	}
}
