package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.gui.PropShop;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class PropShopCommand extends Command 
{

	public PropShopCommand(String name, String description, String usageMessage,
			List<String> aliases) 
	{
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) 
	{
		if (!(sender instanceof Player))
			return false;
		
		// Get player in CustomPlayer
		CustomPlayer player = CustomPlayer.get(((Player) sender).getUniqueId());
		
		// Open Prop Shop
		PropShop.open(player);
		return true;
	}
	
}
