package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.chat.PrivateMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReplyCommand extends Command
{

	public ReplyCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
                        @NotNull List<String> aliases)
	{
		super(name, description, usageMessage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String @NotNull [] args)
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.DARK_RED + "Sorry, but this is a player only command.");
			return false;
		}
		
		if (args.length < 1) {
			sender.sendMessage(String.format("%s» %s%s", ChatColor.GOLD, ChatColor.DARK_RED, this.usageMessage));
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
