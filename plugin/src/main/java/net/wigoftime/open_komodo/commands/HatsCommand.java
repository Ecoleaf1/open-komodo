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
		
		HatMenu gui;
		
		if (args.length > 0) {
			if (!HatMenu.isValid(args[0]))
				return true;
			
			gui = new HatMenu(player, args[0]);
		} else {
			gui = new HatMenu(player, "default");	
		}
		
		// Open HatMenu
		gui.open();
		return false;
	}

}
