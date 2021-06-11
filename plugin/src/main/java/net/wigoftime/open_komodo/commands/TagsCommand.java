package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.gui.TagMenu;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TagsCommand extends Command{

	public TagsCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
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
