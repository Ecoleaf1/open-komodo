package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TutorialCommand extends Command {

	public TutorialCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) return true;
		
		CustomPlayer playerCustom = CustomPlayer.get(((Player) sender).getUniqueId());
		if (playerCustom == null) return true;
		
		playerCustom.setTutorial(true);
		return false;
	}
	
}
