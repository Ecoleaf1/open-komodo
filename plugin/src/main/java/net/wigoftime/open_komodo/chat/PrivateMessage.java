package net.wigoftime.open_komodo.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.Moderation;
import net.wigoftime.open_komodo.etc.Filter;

abstract public class PrivateMessage 
{
	private static final Map<UUID, UUID> lastReply = new HashMap<UUID, UUID>();
	
	public static void sendMessage(CommandSender sender, Player receiver, String message) 
	{
		
		// If the player typed in an online correct username
		if (receiver == null)
		{
			sender.sendMessage(ChatColor.DARK_RED+"Player not found.");
			return;
		}
		
		// If spammed or swore
		if (sender instanceof Player)
		if (!Filter.checkMessage((Player) sender, message))
			return;
		
		String sentFormat = Main.getPMSentFormat();
		String receivedFormat = Main.getPMReceivedFormat();
		
		String sentMessage = ChatColor.translateAlternateColorCodes('&', sentFormat);
		
		sentMessage = MessageFormat.format(sentMessage, sender,receiver, message);
		
		String receivedMessage = ChatColor.translateAlternateColorCodes('&', receivedFormat);
		receivedMessage = MessageFormat.format(receivedMessage, sender,receiver, message);
		
		receiver.sendMessage(receivedMessage);
		sender.sendMessage(sentMessage);
		
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			
			lastReply.put(player.getUniqueId(), receiver.getUniqueId());
			lastReply.put(receiver.getUniqueId(), player.getUniqueId());
		}
	}
	
	public static void playerLeft(UUID uuid)
	{
		lastReply.remove(uuid);
	}
	
	public static void reply(Player sender, String message)
	{
		Player target = Bukkit.getPlayer(lastReply.get(sender.getUniqueId()));
		
		if (target == null)
		{
			sender.sendMessage(ChatColor.DARK_RED + "No one to to reply to.");
			return;
		}
		
		sendMessage(sender, target, message);
	}
}
