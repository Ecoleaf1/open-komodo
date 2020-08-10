package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.etc.CurrencyClass;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class CheckBalanceCommand extends Command
{

	public CheckBalanceCommand(String name, String description, String usageMessage,
			List<String> aliases) 
	{
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) 
	{
		// Don't Continue if isn't player
		if (!(sender instanceof Player))
			return false;
		
		// Get player in CustomPlayer format
		CustomPlayer customPlayer = CustomPlayer.get(((Player) sender).getUniqueId());
		
		CurrencyClass.displayBalance(customPlayer);
		return true;
	}

}
