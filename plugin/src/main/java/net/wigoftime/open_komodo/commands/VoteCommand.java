package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class VoteCommand extends Command {

	public VoteCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
			@NotNull List<String> aliases) {
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
		sender.sendMessage(String.format("%s» %sNot setup yet", ChatColor.GOLD, ChatColor.GRAY));
		return false;
	}

}
