package net.wigoftime.open_komodo.chat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.Filter;

public class NormalMessageJson 
{
	
	// Distance Range by blocks set in Config.
	private static final int distanceR = Main.getDistanceRange();
	
	public static void sendMessage(Player sender, String message) 
	{		
		Location senderLocation = sender.getLocation();
		String format = MessageFormat.json(sender, message);
		
		// If DistanceChat is on (If the number is higher than 0)
		if (distanceR > 0) 
		{
			for(Player pl: Bukkit.getOnlinePlayers()) 
			{
				// If player isnt in same world, skip player
				if (pl.getWorld() != sender.getWorld())
					continue;
				
				// If Distance between the player and the sender is bigger than Distance Radius, skip the player.
				if (pl.getLocation().distance(senderLocation) > distanceR) 
					continue;
				
					/* 
					 *	Make a Runnable that will delay to sync it to the main thread.
					 *	In the runnable makes the server console use a command to send a message
					 *	to make it into RAW format.
					 */
					
					Runnable runnable = new Runnable() 
					{	
						public void run() 
						{
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + pl.getName() + " "+format);
						}
					};
					
					/*
					 *	Then delay the task to make it into the main thread to run the Runnable.
					 */
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), runnable);
			}
			
		// If Disabled
		}
		else 
		{
			for(Player pl: Bukkit.getOnlinePlayers()) 
			{
				/* 
				 *	Make a Runnable that will delay to sync it to the main thread.
				 *	In the runnable makes the server console use a command to send a message
				 *	to make it into RAW format.
				 */
				
				Runnable runnable = new Runnable() 
				{
					
					public void run() 
					{
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + pl.getName() + " "+format);
					}
				};
				
				/*
				*	Then delay the task to make it into the main thread to run the Runnable.
				*/
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), runnable);
			}
		}
		
		// Messages the server terminal/console as well.
		Bukkit.getLogger().info(String.format("%s: %s", sender.getName(), message));
	}
	
	public static void shout(Player sender, String message)
	{	
		String format = MessageFormat.json(sender, message);
		
		for (Player p : Bukkit.getOnlinePlayers())
		{
			BukkitRunnable runnable = new BukkitRunnable() 
			{
				
				public void run() 
				{
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + p.getName() + " "+format);
				}
			};
			
			runnable.runTaskLater(Main.getPlugin(), 2);
		}
	}
}
