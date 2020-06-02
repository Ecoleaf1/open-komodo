package net.wigoftime.open_komodo.etc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.connorlinfoot.bountifulapi.BountifulAPI;

import net.md_5.bungee.api.ChatColor;
import net.wigoftime.open_komodo.Main;

public abstract class ActionBar 
{

	private static final String msg[] = 
	{
		ChatColor.translateAlternateColorCodes('&', "&e&k|&r&e&lTIP&e&k|&r &eWondering how you get ranks? Do &l/rank help!"),
		ChatColor.translateAlternateColorCodes('&', "&e&k|&r&e&lTIP&e&k|&r &eYou earn points by simply being online!"),
		//ChatColor.translateAlternateColorCodes('&', "&e&k|&r&e&lTIP&e&k|&r &eTrade with your friends! &l/trade request <name>"),
		ChatColor.translateAlternateColorCodes('&', "&e&k|&r&e&lTIP&e&k|&r &eWant a fancy Roleplay Tag in chat? Do: &l/tags"),
		ChatColor.translateAlternateColorCodes('&', "&e&k|&r&e&lTIP&e&k|&r &eWant to transfer money to a friend? Do: &l/pay <name> <amount> <currency>"),
		ChatColor.translateAlternateColorCodes('&', "&e&k|&r&e&lTIP&e&k|&r &eWant a fancy hat? Do &l/hats"),
		ChatColor.translateAlternateColorCodes('&', "&e&k|&r&e&lTIP&e&k|&r &eWondering how you get &2&lPoints&r&e or &6&lCoins&r&e? Do &l/money help!"),
		//ChatColor.translateAlternateColorCodes('&', "&e&k|&r&e&lTIP&e&k|&r &eAccess your friend list via your Phone!"),
		ChatColor.translateAlternateColorCodes('&', "&e&k|&r&e&lTIP&e&k|&r &eUse &l!&r&e to shout something to the whole server!"),
	};
	
	public static void startLoop()
	{
		Thread thread = new Thread(new Runnable() 
		{
			public void run() 
			{
				while (true)
				{
					for (String s : msg)
					{
						for (Player p : Bukkit.getServer().getOnlinePlayers())
							Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() 
							{
								public void run()
								{
									//ActionBarAPI.sendActionBar(p, s);
									BountifulAPI.sendActionBar(p, s, 120);
								}
							});
						
						try 
						{
							Thread.sleep(12000);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		});
		
		thread.start();
	}
	
}
