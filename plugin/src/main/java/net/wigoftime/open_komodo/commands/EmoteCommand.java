package net.wigoftime.open_komodo.commands;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.chat.Emote;
import net.wigoftime.open_komodo.etc.Permissions;

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
		if (args.length < 1)
		{	
			Iterator<String> iterator = Emote.nameSortMap.keySet().iterator();
			
			StringBuilder sb = new StringBuilder();
			sb.append(ChatColor.AQUA + "" + ChatColor.BOLD + "Emotes" + ChatColor.DARK_AQUA + ": ");
			
			while (iterator.hasNext())
			{
				sb.append(iterator.next());
				
				if (iterator.hasNext())
					sb.append(", ");
			}
			
			player.sendMessage(sb.toString());
			return true;
		}
		
		if (args[0].equalsIgnoreCase("reload"))
		{
			if (!sender.hasPermission(Permissions.emoteReloadPerm))
			{
				sender.sendMessage(ChatColor.DARK_RED + "You don't have permission.");
				return false;
			}
			
			Emote.reload();
			sender.sendMessage(ChatColor.DARK_AQUA+ "Emotes reloaded!");
			return true;
		}
		
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
