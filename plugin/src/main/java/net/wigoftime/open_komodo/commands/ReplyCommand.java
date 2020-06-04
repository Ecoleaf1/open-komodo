package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.chat.PrivateMessage;

public class ReplyCommand extends Command
{

	public ReplyCommand(String name, String description, String usageMessage,
			List<String> aliases) 
	{
		super(name, description, usageMessage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) 
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.DARK_RED + "Sorry, but this is a player only command.");
			return false;
		}
		
		if (args.length < 1)
		{
			sender.sendMessage(ChatColor.DARK_RED + this.usageMessage);
			return false;
		}
		
		Player player = (Player) sender;
		
		StringBuilder message = new StringBuilder();
		for (String s : args)
		{
			message.append(s +" ");
		}
		
		PrivateMessage.reply(player, message.toString());
		return true;
	}

}
