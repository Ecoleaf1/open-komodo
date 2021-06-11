package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.chat.MessageFormat;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.TpRequest.tpType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TpaHereCommand extends Command
{

	public TpaHereCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
                          @NotNull List<String> aliases)
	{
		super(name, description, usageMessage, aliases);
	}
	
	@Override
	public boolean execute(CommandSender sender, String commandLabel, String @NotNull [] args)
	{
		if (!(sender instanceof Player))
			return false;
		
		if (args.length < 1)
		{
			 final String msgUsage = String.format("%s» %sUsage: %s", ChatColor.GOLD, ChatColor.GRAY, this.usageMessage);
			sender.sendMessage(msgUsage);
			return false;
		}
		
		CustomPlayer player = CustomPlayer.get(((Player) sender).getUniqueId());
		
		Player target = Bukkit.getPlayer(args[0]);
		
		if (target == null)
		{
			String message = MessageFormat.format(TpaCommand.errorCantFindPerson, player.getPlayer().getDisplayName(), args[0], null);
			player.getPlayer().sendMessage(message);
			return false;
		}
		
		
		CustomPlayer targetCustomPlayer = CustomPlayer.get(target.getUniqueId());
		targetCustomPlayer.tpaRequest(player, tpType.TPAHERE);
		return true;
	}

}
