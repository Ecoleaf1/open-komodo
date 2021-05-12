package net.wigoftime.open_komodo.chat;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import github.scarsz.discordsrv.DiscordSRV;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class NormalMessage 
{
	
	// Distance Range by blocks set in Config.
	private static final int distanceR = Main.getDistanceRange();
	
	public static void sendMessage(CustomPlayer senderCustomPlayer, String message) {	
		if (senderCustomPlayer.isInTutorial())
		if (!senderCustomPlayer.getTutorial().validState(NormalMessage.class)) return;
		
		Location senderLocation = senderCustomPlayer.getPlayer().getLocation();
		
		BaseComponent[] componentBaseMessage = MessageFormat.getChatFormat(false, senderCustomPlayer, message);
		
		if (senderCustomPlayer.isInTutorial()) {
			senderCustomPlayer.getPlayer().spigot().sendMessage(componentBaseMessage);
			senderCustomPlayer.getTutorial().trigger(NormalMessage.class);
			return;
		}
		
		// If DistanceChat is on (If the number is higher than 0)
		if (distanceR > 0)
			for(CustomPlayer pl: CustomPlayer.getOnlinePlayers()) {
				
				if (pl.isMonitoring() && senderCustomPlayer != pl) {
					sendMonitorMessage(senderCustomPlayer, message, pl);
					continue;
				}
				
				// If player isnt in same world, skip player
				if (pl.getPlayer().getWorld() != senderCustomPlayer.getPlayer().getWorld())
					continue;
				
				
				// If Distance between the player and the sender is bigger than Distance Radius, skip the player.
				if (pl.getPlayer().getLocation().distance(senderLocation) > distanceR)
					continue;
				
				pl.getPlayer().spigot().sendMessage(componentBaseMessage);
			}
		// If Disabled
		else 
			for(Player pl: Bukkit.getOnlinePlayers()) 
				pl.spigot().sendMessage(componentBaseMessage);
		
		// Messages the server terminal/console as well.
		Bukkit.getLogger().info(String.format("%s: %s", senderCustomPlayer.getPlayer().getName(), message));
		
		// Send message to discord (If enabled)
		sendToDiscord(senderCustomPlayer, message);
	}
	
	public static void sendToDiscord(CustomPlayer playerCustomPlayer, String message) {
		if (Main.getDiscordSRV() == null)
			return;
		
		if (!playerCustomPlayer.getSettings().isDiscordChatEnabled())
			return;
		
		// Send message to Discord
		((DiscordSRV) Main.getDiscordSRV()).processChatMessage(playerCustomPlayer.getPlayer(), message, null, false);
	}
	
	private static void sendMonitorMessage(CustomPlayer messenger, String message, CustomPlayer monitorPlayer) {
		monitorPlayer.getPlayer().sendMessage(String.format("%s[Public] %s%s%s: %s%s", 
				ChatColor.YELLOW, ChatColor.DARK_GRAY, messenger.getPlayer().getDisplayName(), 
				ChatColor.RESET, ChatColor.GRAY, message));
		
		return;
	}
}
