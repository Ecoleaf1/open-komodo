package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.chat.Emote;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public class EmoteCommand extends Command
{

	public EmoteCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
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
		
		// Get player
		Player player = (Player) sender;
		
		CustomPlayer playerCustom = CustomPlayer.get(player.getUniqueId());
		if (playerCustom == null) return false;
		
		if (playerCustom.isInTutorial())
		if (!playerCustom.getTutorial().validState(Emote.class)) return true;
		
		// If player didnt enter in emote
		if (args.length < 1)
		{	
			Iterator<String> iterator = Emote.nameSortMap.keySet().iterator();
			
			StringBuilder sb = new StringBuilder();
			sb.append(String.format("%s» %sEmotes%s: ", ChatColor.GOLD, ChatColor.AQUA, ChatColor.DARK_AQUA));
			
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
			if (!sender.hasPermission(Permissions.emoteReloadPerm)) {
				sender.sendMessage(String.format("%s» %sYou don't have permission to run this", ChatColor.GOLD, ChatColor.DARK_RED));
				return false;
			}
			
			Emote.reload();
			sender.sendMessage(String.format("%s» %sEmotes reloaded!", ChatColor.GOLD, ChatColor.DARK_GREEN));
			return true;
		}
		
		// If emote entered by player is invaild
		if (Emote.getByName(args[0].toLowerCase()) == null) 
		{
			player.sendMessage(String.format("%s» %sNot an emote", ChatColor.GOLD, ChatColor.GRAY));
			return false;
		}
		
		// If there is another argument to target player
		if (args.length > 1) 
		{
			// Get targetted player
			Player directPlayer = Bukkit.getPlayer(args[1]);
			
			// If player exists, send
			if (directPlayer != null) {
				CustomPlayer directCustom = CustomPlayer.get(directPlayer.getUniqueId());
				if (directCustom != null) Emote.send(args[0].toLowerCase(), playerCustom, directCustom);
			}
		}
		else
			// If a solo emote
			Emote.send(args[0], playerCustom, null);
		
		return true;
	}
	
}
