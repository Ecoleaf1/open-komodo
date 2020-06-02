package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.chat.MessageFormat;
import net.wigoftime.open_komodo.etc.TpSystem;

public class TpaCommand extends Command
{

	public TpaCommand(String name, String description, String usageMessage,
			List<String> aliases) 
	{
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) 
	{
		if (!(sender instanceof Player))
			return false;
		
		// Get player in Player format
		Player player = (Player) sender;
		
		if (args.length > 0) 
		{
			Player target = Bukkit.getPlayer(args[0]);
			
			if (target != null)
				TpSystem.requestTpa(player, target);
			else 
			{
				String message = MessageFormat.format(TpSystem.errorCantFindPerson, player.getDisplayName(), args[0], null);
				player.sendMessage(message);
			}
				
		}
		else
			player.sendMessage(TpSystem.commandDesc);
		
		return true;
	}

}
