package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.gui.HatMenu;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HatsCommand extends Command 
{

	public HatsCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
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
		
		CustomPlayer player = CustomPlayer.get(((Player) sender).getUniqueId());
		
		HatMenu gui;
		
		if (args.length > 0) {
			if (!HatMenu.isValid(args[0]))
				return true;
			
			gui = new HatMenu(player, args[0], false);
		} else {
			gui = new HatMenu(player, "default", false);
		}
		
		// Open HatMenu
		gui.open();
		return false;
	}

}
