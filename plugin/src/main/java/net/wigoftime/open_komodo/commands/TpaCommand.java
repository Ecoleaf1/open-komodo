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
import java.util.UUID;

public class TpaCommand extends Command
{
	public static final String errorCantFindPerson = String.format("%s» %sCan't find $D", net.md_5.bungee.api.ChatColor.YELLOW, net.md_5.bungee.api.ChatColor.GRAY);

	public TpaCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
                      @NotNull List<String> aliases)
	{
		super(name, description, usageMessage, aliases);
	}
	
	private static final String msgUsage = String.format("%s» %sUsage: /tpa (Player)", ChatColor.GOLD, ChatColor.GRAY);
	
	@Override
	public boolean execute(CommandSender sender, String command, String @NotNull [] args)
	{
		if (!(sender instanceof Player))
			return false;
		
		// Get player in CustomPlayer format
		CustomPlayer player = CustomPlayer.get(((Player) sender).getUniqueId());
		
		if (args.length > 0) 
		{
			Player targetPlayer = Bukkit.getPlayer(args[0]);
			
			if (targetPlayer != null) {
				UUID uuid = Bukkit.getPlayer(args[0]).getUniqueId();
				CustomPlayer targetCustomPlayer = CustomPlayer.get(uuid);
				
				targetCustomPlayer.tpaRequest(player, tpType.TPA);
				
			} else 
			{
				String message = MessageFormat.format(errorCantFindPerson, player.getPlayer().getDisplayName(), args[0], null);
				player.getPlayer().sendMessage(message);
			}
				
		}
		else
			player.getPlayer().sendMessage(msgUsage);
		
		return true;
	}

}
