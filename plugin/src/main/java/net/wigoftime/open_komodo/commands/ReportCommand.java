package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.actions.BugReporter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReportCommand extends Command 
{

	public ReportCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
                         @NotNull List<String> aliases) {
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) 
	{
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		BugReporter.open(player);
		return true;
	}

}
