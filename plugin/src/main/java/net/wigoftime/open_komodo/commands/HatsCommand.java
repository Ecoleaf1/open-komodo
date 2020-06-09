package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.gui.HatMenu;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class HatsCommand extends Command 
{

	public HatsCommand(String name, String description, String usageMessage,
			List<String> aliases) 
	{
		super(name, description, usageMessage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) 
	{
		if (!(sender instanceof Player))
			return false;
		
		CustomPlayer player = CustomPlayer.get(((Player) sender).getUniqueId());
		
		// Give player the hats gui
		HatMenu.open(player);
		return false;
	}

}
