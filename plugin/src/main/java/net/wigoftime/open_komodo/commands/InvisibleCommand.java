package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InvisibleCommand extends Command {

	public InvisibleCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!(sender instanceof Player))
				return false;
		
		CustomPlayer player = CustomPlayer.get(((Player) sender).getUniqueId());
		
		if (player == null)
			return false;
		
		if (!player.getPlayer().hasPermission(Permissions.invis))
			return false;
		
		player.setInvisible(player.isInvisible() == true ? false : true);
		return false;
	}

}
