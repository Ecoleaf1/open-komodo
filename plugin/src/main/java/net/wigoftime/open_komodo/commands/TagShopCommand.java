package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.gui.TagShop;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class TagShopCommand extends Command
{

	public TagShopCommand(String name, String description, String usageMessage,
			List<String> aliases) 
	{
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) 
	{
		if (!(sender instanceof Player))
			return false;
		
		// Get sender in player format
		CustomPlayer player = CustomPlayer.get(((Player) sender).getUniqueId());
		
		TagShop gui = new TagShop(player);
		gui.open();
		
		return false;
	}

}
