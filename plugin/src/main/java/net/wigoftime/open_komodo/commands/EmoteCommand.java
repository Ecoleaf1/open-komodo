package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.chat.Emote;

public class EmoteCommand extends Command
{

	public EmoteCommand(String name, String description, String usageMessage,
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
		
		// Get player
		Player player = (Player) sender;
		
		// If player didnt enter in emote
		if (args.length < 0)
			return false;
		
		// If emote entered by player is invaild
		if (Emote.getByName(args[0].toLowerCase()) == null) 
		{
			player.sendMessage("Not an emote.");
			return false;
		}
		
		// If there is another argument to target player
		if (args.length > 1) 
		{
			// Get targetted player
			Player directPlayer = Bukkit.getPlayer(args[1]);
			
			// If player exists, send
			if (directPlayer != null)
				Emote.send(args[0].toLowerCase(), player, directPlayer);
		}
		else
			// If a solo emote
			Emote.send(args[0], player, null);
		
		return true;
	}
	
}
