package net.wigoftime.open_komodo.etc;

import org.bukkit.Bukkit;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.objects.CustomPlayer;

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
		ChatColor.translateAlternateColorCodes('&', "&e&k|&r&e&lTIP&e&k|&r &eUse &l!&r&e to shout something to the whole server!"),
	};
	
	private static byte messageIndex = 0;
	
	public static void startLoop() {
		Runnable runnable = new Runnable() {
			public void run() {
				
				for (CustomPlayer playerIndex : CustomPlayer.getOnlinePlayers())
				if (!playerIndex.isBuilding()) playerIndex.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(msg[messageIndex]));
				else playerIndex.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(String.format("%sBuild Mode", ChatColor.YELLOW)));
			}
		};
		
		Runnable buildActionMenuLoop = new Runnable() {
			public void run() {
				
				for (CustomPlayer playerIndex: CustomPlayer.getBuildingPlayers())
				if (playerIndex.isBuilding()) playerIndex.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(String.format("%s%sBuild Mode", ChatColor.YELLOW, ChatColor.BOLD)));
			}
		};
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), runnable, 240, 240);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), buildActionMenuLoop, 40, 40);
	}
	
	private static void updateMsgIndex() {
		if (messageIndex + 1 > msg.length) messageIndex = 0;
		else messageIndex++;
	}
	
}
