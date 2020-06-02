package net.wigoftime.open_komodo.chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.Moderation;
import net.wigoftime.open_komodo.etc.Filter;

abstract public class PrivateMessage 
{

	public static void sendMessage(CommandSender sender,String[] args) 
	{
		
		// Make sure that the player just didn't do /msg, with nothing else (arg[0] is the command).
		if (args.length>0) 
		{
			
			if (sender instanceof Player)
				if (Moderation.isMuted((Player) sender))
					return;
			
			Player receiver = Bukkit.getPlayer(args[0]);
			
			// If the player typed in an online correct username
			if (receiver == null)
			{
				sender.sendMessage(ChatColor.DARK_RED+"Player not found.");
				return;
			}
				
			StringBuilder sb = new StringBuilder();
			
			for (int i = 1; i < args.length;i++) 
			{
				if (i+1<args.length)
					sb.append(args[i] + " ");
				else
					sb.append(args[i]);
			}
			
			// If spammed or swore
			if (sender instanceof Player)
			if (!Filter.checkMessage((Player) sender, sb.toString()))
				return;
			
			String sentFormat = Main.getPMSentFormat();
			String receivedFormat = Main.getPMReceivedFormat();
			
			String sentMessage = ChatColor.translateAlternateColorCodes('&', sentFormat);
			
			sentMessage = MessageFormat.format(sentMessage, sender,receiver, sb.toString());
			
			String receivedMessage = ChatColor.translateAlternateColorCodes('&', receivedFormat);
			receivedMessage = MessageFormat.format(receivedMessage, sender,receiver, sb.toString());
			
			receiver.sendMessage(receivedMessage);
					
			sender.sendMessage(sentMessage);
				
			
		}
	}
	
}
