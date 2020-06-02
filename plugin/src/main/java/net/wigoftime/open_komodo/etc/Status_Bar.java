package net.wigoftime.open_komodo.etc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.objects.Rank;

public abstract class Status_Bar 
{
	private static final String[] msg = 
		{
				ChatColor.translateAlternateColorCodes('&', "&e&lWelcome to &b&lOpen &a&lKomodo&b&l!"),
				ChatColor.translateAlternateColorCodes('&', "&e&lEnjoy your stay!")
		};
	
	private static BossBar bar;

	public static void startLoop()
	{
		bar = Bukkit.createBossBar(msg[0], BarColor.PURPLE, BarStyle.SEGMENTED_20, BarFlag.DARKEN_SKY);
		bar.setProgress(0);
		
		Thread thread = new Thread(new Runnable() 
		{
			public void run() 
			{
				while (true)
				{
					for (String s : msg)
					{
						bar.setTitle(s);
						
						try 
						{
							Thread.sleep(5000);
						} catch (InterruptedException e) 
						{
							e.printStackTrace();
						}
					
					}
				}
			}
		});
		
		thread.start();
	}
	
	public static BossBar getBar() 
	{
		return bar;
	}
	
	public static void addPlayer(Player player)
	{
		for (Player pl : bar.getPlayers())
			if (pl == player)
				return;
		
		bar.addPlayer(player);
		
		String rankStr = PlayerConfig.getRank(player);
		Rank rank = Rank.getRank(rankStr);
		
		if (rank == null)
			return;
		
		String rankPrefix = rank.getPrefix();
		
		if (rankPrefix == null)
			return;
		
		rankPrefix = ChatColor.translateAlternateColorCodes('&', rankPrefix);
		player.setPlayerListName(rankPrefix + ChatColor.translateAlternateColorCodes('&', "&r| ") + player.getDisplayName());
	}
}
