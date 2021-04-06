package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.objects.CustomPlayer;

public class TutorialCommand extends Command {

	public TutorialCommand(String name, String description, String usageMessage, List<String> aliases) {
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
