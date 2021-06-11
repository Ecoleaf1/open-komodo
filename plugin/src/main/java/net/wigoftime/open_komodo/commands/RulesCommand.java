package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.actions.Rules;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RulesCommand extends Command
{

	public RulesCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
                        @NotNull List<String> aliases)
	{
		super(name, description, usageMessage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(@NotNull CommandSender sender, String command, String[] args)
	{
		Rules.display(sender);
		return true;
	}

}
