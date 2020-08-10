package net.wigoftime.open_komodo.etc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public abstract class Status_Bar 
{
	private static final String[] msg = 
		{
				ChatColor.translateAlternateColorCodes('&', "&e&lWelcome to &b&lOpen &a&lKomodo&b&l!"),
				ChatColor.translateAlternateColorCodes('&', "&e&lEnjoy your stay!")
		};
	
	private static BossBar bar;
	private static byte barIndex = 0;
	
	public static void startLoop()
	{
		bar = Bukkit.createBossBar(msg[0], BarColor.PURPLE, BarStyle.SEGMENTED_20, BarFlag.DARKEN_SKY);
		bar.setProgress(0);
		
		Runnable runnable = new Runnable() {
			public void run() {
				if ((msg.length - 1) <= barIndex)
					barIndex = 0;
				else
					barIndex++;
				
					bar.setTitle(msg[barIndex]);
			}
		};
		
		Bukkit.getScheduler().runTaskTimer(Main.getPlugin(), runnable, 20, 100);
	}
	
	public static BossBar getBar() 
	{
		return bar;
	}
	
	public static void addPlayer(CustomPlayer customPlayer)
	{
		for (Player pl : bar.getPlayers())
			if (pl == customPlayer.getPlayer())
				return;
		
		bar.addPlayer(customPlayer.getPlayer());
	}
}
