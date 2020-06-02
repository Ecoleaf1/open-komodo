package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.etc.FriendSystem;
import net.wigoftime.open_komodo.gui.FriendsList;

public class FriendsCommand extends Command 
{

	public FriendsCommand(String name, String description, String usageMessage,
			List<String> aliases) 
	{
		super(name, description, usageMessage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) 
	{
		if (!(sender instanceof Player))
			return false;
		
		// Get sender in player format
		Player player = (Player) sender;
		
		// If no arguments, display the friends list
		if (args.length < 1) {
			FriendsList.open(player, (byte) 0);
			return false;
		}
		
		if (args[1].equalsIgnoreCase("add")) {
			if (args.length > 1) {
				Player target = Bukkit.getPlayer(args[1]);
				
				if (target != null)
					FriendSystem.addPlayer(player,target);
				else
					player.sendMessage("Type in the correct username");
				
			} else
				player.sendMessage("/friend add (username)");
			
			return true;
		}
				
		if (args[1].equalsIgnoreCase("remove"))
			if (args.length > 1) {
				OfflinePlayer friend = Bukkit.getOfflinePlayer(args[1]);
				
				if (friend != null)
					FriendSystem.removeAsFriends(player, friend);
				else
					player.sendMessage("Enter a correct username.");
				
				return true;
			}
		
		if (args[1].equalsIgnoreCase("accept")) {
			if (args.length > 1)
				if (args[1].equalsIgnoreCase(FriendSystem.getRequester(player).getDisplayName()))
					FriendSystem.acceptPlayer(player);
				else
					player.sendMessage("Type in the correct username");
			
			return true;
		}
				
			
		
		return true;
	}

}
