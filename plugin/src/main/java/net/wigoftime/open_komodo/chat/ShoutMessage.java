package net.wigoftime.open_komodo.chat;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
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

public class ShoutMessage {
	public static void shout(CustomPlayer sender, String message) {	
		if (sender.isInTutorial())
		if (!sender.getTutorial().validState(ShoutMessage.class)) return;
		
		CustomPlayer senderCustomPlayer = CustomPlayer.get(sender.getUniqueId());
		
		// Get message in JSON template
		
		BaseComponent[] messageInFormat = MessageFormat.getChatFormat(true, senderCustomPlayer, message);
		
		if (senderCustomPlayer.isInTutorial()) {
			senderCustomPlayer.getPlayer().spigot().sendMessage(messageInFormat);
			senderCustomPlayer.getTutorial().trigger(ShoutMessage.class);
			return;
		}
		
		for (Player p : Bukkit.getOnlinePlayers())
			p.spigot().sendMessage(messageInFormat);
		
		// Messages the server terminal/console as well.
		Bukkit.getLogger().info(String.format("[shout] %s: %s", sender.getPlayer(), message));
		
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
}
