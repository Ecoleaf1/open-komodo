package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.chat.MessageFormat;
import net.wigoftime.open_komodo.etc.TpSystem;
import net.wigoftime.open_komodo.objects.TpRequest.tpType;

public class TpaHereCommand extends Command
{

	public TpaHereCommand(String name, String description, String usageMessage,
			List<String> aliases) 
	{
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) 
	{
		if (!(sender instanceof Player))
			return false;
		
		if (args.length < 1)
		{
			sender.sendMessage(ChatColor.DARK_RED + this.usageMessage);
			return false;
		}
		
		Player player = (Player) sender;
		
		Player target = Bukkit.getPlayer(args[0]);
		
		if (target == null)
		{
			String message = MessageFormat.format(TpSystem.errorCantFindPerson, player.getDisplayName(), args[0], null);
			
			player.sendMessage(message);
			return false;
		}
		
		TpSystem.request(player,target, tpType.TPAHERE);
		return true;
	}

}
