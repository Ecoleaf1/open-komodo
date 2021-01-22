package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.gui.TagMenu;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class TagsCommand extends Command{

	public TagsCommand(String name, String description, String usageMessage, List<String> aliases) {
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		CustomPlayer senderCustomPlayer = CustomPlayer.get(((Player) sender).getUniqueId());
		new TagMenu(senderCustomPlayer);
		return true;
	}

}
