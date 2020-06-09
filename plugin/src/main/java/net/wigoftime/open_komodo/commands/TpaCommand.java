package net.wigoftime.open_komodo.commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.chat.MessageFormat;
import net.wigoftime.open_komodo.etc.TpSystem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.TpRequest.tpType;

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
		
		// Get player in CustomPlayer format
		CustomPlayer player = CustomPlayer.get(((Player) sender).getUniqueId());
		
		if (args.length > 0) 
		{
			UUID uuid = Bukkit.getPlayer(args[0]).getUniqueId();
			CustomPlayer target = CustomPlayer.get(uuid);
			
			if (target != null)
				target.tpaRequest(player, tpType.TPA);
			else 
			{
				String message = MessageFormat.format(CustomPlayer.errorCantFindPerson, player.getPlayer().getDisplayName(), args[0], null);
				player.getPlayer().sendMessage(message);
			}
				
		}
		else
			player.getPlayer().sendMessage(ChatColor.DARK_RED + this.usageMessage);
		
		return true;
	}

}
