package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.objects.CustomPlayer;

public class TpaToggleCommand extends Command 
{

	public TpaToggleCommand(String name, String description, String usageMessage,
			List<String> aliases) 
	{
		super(name, description, usageMessage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) 
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.DARK_RED + "Sorry, only players can use this command.");
			return false;
		}
		
		CustomPlayer player = CustomPlayer.get(((Player) sender).getUniqueId());
		
		if (player.isTpaRequestAllowed())
			player.setTpaRequestAllowed(false);
		else
			player.setTpaRequestAllowed(true);
		
		sender.sendMessage(ChatColor.GRAY + "You have toggled your tpa requests");
		return true;
	}

}
