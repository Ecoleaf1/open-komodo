package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.chat.PrivateMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MsgCommand extends Command {

	public MsgCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
                      @NotNull List<String> aliases) {
		super(name, description, usageMessage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String command, String @NotNull [] args)  {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Must be player to send a message");
			return false;
		}
		
		if (args.length < 1) {
			sender.sendMessage(String.format("%s» %sUsage: %s", ChatColor.GOLD, ChatColor.GRAY, this.usageMessage));
			return false;
		}
		
		Player player = (Player) sender;
		Player target = Bukkit.getPlayer(args[0]);
		
		StringBuilder message = new StringBuilder();
		for (int i = 1; i < args.length; i++)
			message.append(args[i] + " ");
		
		PrivateMessage.sendMessage(player, target, message.toString());
		return false;
	}

}
