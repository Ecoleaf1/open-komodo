package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DelHomeCommand extends Command
{
	private static final String nameMsg = ChatColor.translateAlternateColorCodes('&', "&6» &7Enter your home name that you wish to delete.");

	public DelHomeCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
                          @NotNull List<String> aliases) {
		super(name, description, usageMessage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String command, String @NotNull [] args)
	{
		if (!(sender instanceof Player))
			return false;
		
		// Get player in CustomPlayer format
		CustomPlayer playerCustomPlayer = CustomPlayer.get(((Player) sender).getUniqueId());
		
		// If there aren't any subcommands
		if (args.length < 1) 
		{
			// Tell player to enter a subcommand
			playerCustomPlayer.getPlayer().sendMessage(nameMsg);
			return false;
		}
		
		// Delete home
		
		playerCustomPlayer.deleteHome(args[0]);
		return false;
	}}
