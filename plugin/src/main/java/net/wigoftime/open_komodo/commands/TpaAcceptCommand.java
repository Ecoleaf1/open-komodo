package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TpaAcceptCommand extends Command
{

	public TpaAcceptCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
                            @NotNull List<String> aliases)
	{
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) 
	{
		if (!(sender instanceof Player))
			return false;
		
		// Get sender in CustomPlayer format
		CustomPlayer player = CustomPlayer.get(((Player) sender).getUniqueId());
		
		player.tpaAccept();
		return true;
	}
	
}
