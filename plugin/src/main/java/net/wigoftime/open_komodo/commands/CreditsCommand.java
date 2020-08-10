package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CreditsCommand extends Command {

	public CreditsCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
			@NotNull List<String> aliases) {
		super(name, description, usageMessage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		sender.sendMessage(String.format("%s» %sServer icon: SleepyLoser (Discord: SleepyLoser#6078)", ChatColor.GOLD, ChatColor.GRAY));
		sender.sendMessage("");
		sender.sendMessage(String.format("%s» %sProps: Jadesweetgamer (Discord: Jade/Yui#1428), Jazzycow11 (Discord: Aramis#2706)", ChatColor.GOLD, ChatColor.GRAY));
		sender.sendMessage("");
		sender.sendMessage(String.format("%s» %sMap creator: Eivisxp", ChatColor.GOLD, ChatColor.GRAY));
		sender.sendMessage("");
		sender.sendMessage(String.format("%s» %sProgrammer: Ecoleaf (Discord: Ecoleaf#9713)", ChatColor.GOLD, ChatColor.GRAY));
		sender.sendMessage(String.format("%s» %sAnd you, The community even just checking by helps! Thank you!", ChatColor.GOLD, ChatColor.GRAY));
		return true;
	}
	
}
