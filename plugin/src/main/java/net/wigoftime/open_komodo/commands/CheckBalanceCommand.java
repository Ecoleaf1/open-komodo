package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.etc.CurrencyClass;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CheckBalanceCommand extends Command
{

	public CheckBalanceCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
                               @NotNull List<String> aliases)
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
