package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.etc.systems.NicknameSystem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NicknameCommand extends Command
{

	public NicknameCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
                           @NotNull List<String> aliases)
	{
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String command, String @NotNull [] args)
	{
		if (!(sender instanceof Player))
			return false;
		
		// Get player
		CustomPlayer player = CustomPlayer.get(((Player) sender).getUniqueId());
		
		if (args.length < 1)
			NicknameSystem.changeNick(player, null);
		else
			NicknameSystem.changeNick(player, args[0]);
		
		return true;
	}
	
}
