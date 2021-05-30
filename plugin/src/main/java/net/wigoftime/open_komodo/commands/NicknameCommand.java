package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.etc.systems.NicknameSystem;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class NicknameCommand extends Command
{

	public NicknameCommand(String name, String description, String usageMessage,
			List<String> aliases) 
	{
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) 
	{
		if (!(sender instanceof Player))
			return false;
		
		// Get player
		CustomPlayer player = CustomPlayer.get(((Player) sender).getUniqueId());
		
		if (args.length < 1)
			NicknameSystem.changeNick(player, null);
		else
			NicknameSystem.changeNick(player, args[0]);
		
		return true;
	}
	
}
