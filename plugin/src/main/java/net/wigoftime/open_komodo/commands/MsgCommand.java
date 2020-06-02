package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.chat.PrivateMessage;

public class MsgCommand extends Command 
{

	public MsgCommand(String name, String description, String usageMessage,
			List<String> aliases) 
	{
		super(name, description, usageMessage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) 
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage("Must be player to send a message");
			return false;
		}
		
		Player player = (Player) sender;
		
		PrivateMessage.sendMessage(player, args);
		return false;
	}

}
