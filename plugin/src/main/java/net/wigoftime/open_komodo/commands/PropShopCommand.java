package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.gui.PropShop;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PropShopCommand extends Command 
{

	public PropShopCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
                           @NotNull List<String> aliases)
	{
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String command, String @NotNull [] args)
	{
		if (!(sender instanceof Player))
			return true;
		
		// Get player in CustomPlayer
		CustomPlayer player = CustomPlayer.get(((Player) sender).getUniqueId());
		
		PropShop gui;
		
		if (args.length > 0) {
			if (!PropShop.isValidPropShop(args[0]))
				return true;
			
			gui = new PropShop(player, args[0]);
		} else {
			gui = new PropShop(player, "default");	
		}
		
		// Open Prop Shop
		gui.open();
		return true;
	}
	
}
