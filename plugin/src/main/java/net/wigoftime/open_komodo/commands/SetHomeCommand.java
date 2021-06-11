package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.etc.homesystem.HomeSystem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SetHomeCommand extends Command
{
	private static final String typeInName = ChatColor.translateAlternateColorCodes('&', "&4Enter a name for your home.");

	public SetHomeCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
                          @NotNull List<String> aliases)
	{
		super(name, description, usageMessage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String command, String @NotNull [] args)
	{
		if (!(sender instanceof Player))
			return false;
		
		CustomPlayer playerCustomPlayer = CustomPlayer.get(((Player) sender).getUniqueId());
		
		// If there are subcommands
		if (args.length > 0)
			// Create home
			HomeSystem.createHome(playerCustomPlayer, args[0]);
		// Else send in message to enter a subcommand
		else
			playerCustomPlayer.getPlayer().sendMessage(typeInName);
		
		return true;
	}

}
