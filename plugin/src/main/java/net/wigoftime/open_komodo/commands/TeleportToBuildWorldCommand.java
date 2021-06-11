package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.world.BuilderWorld;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TeleportToBuildWorldCommand extends Command
{

	public TeleportToBuildWorldCommand(@NotNull String name, @NotNull String description,
                                       @NotNull String usageMessage, @NotNull List<String> aliases)
	{
		super(name, description, usageMessage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) 
	{
		if (!(sender instanceof Player))
			return false;
		
		// Get player in Player format
		Player player = (Player) sender;
		
		// If player is permitted to go to builderworld
		if (player.hasPermission(Permissions.tpBuilderWorld))
			BuilderWorld.joinWorld(player);
		else
			player.sendMessage(Permissions.builderWorldNotPerm);
		
		return true;
	}

}
