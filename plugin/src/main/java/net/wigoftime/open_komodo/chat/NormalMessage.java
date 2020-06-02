package net.wigoftime.open_komodo.chat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.Main;

abstract public class NormalMessage
{
	// Distance Range by blocks (From x,y,z)
	private static final int distanceR = Main.getDistanceRange();
	
	public static void sendMessage(Player sender, String message) 
	{
		String messageJSON = MessageFormat.format(message, sender, message);
		
		// If DistanceChat is on (If the number is higher than 0)
		if (distanceR > 0) 
			for(Player pl: Bukkit.getOnlinePlayers()) 
			{
				
				if (pl.getLocation().distance(sender.getLocation()) < distanceR)
					pl.sendMessage(messageJSON);
			}
		// If Disabled
		else
			for(Player pl: Bukkit.getOnlinePlayers()) 
			{
				pl.sendMessage(messageJSON);
			}
			
		
		
		
	}
	
}
