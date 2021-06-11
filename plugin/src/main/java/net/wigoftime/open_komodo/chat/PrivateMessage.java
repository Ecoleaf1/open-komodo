package net.wigoftime.open_komodo.chat;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.Filter;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

abstract public class PrivateMessage 
{
	private static final Map<UUID, UUID> lastReply = new HashMap<UUID, UUID>();
	
	private static final String errorNotFound = String.format("%s» %sPlayer not found",
			ChatColor.GOLD, ChatColor.GRAY);
	public static void sendMessage(@NotNull CommandSender sender, @Nullable Player receiver, String message)
	{
		
		// If the player typed in an online correct username
		if (receiver == null) {
			sender.sendMessage(errorNotFound);
			return;
		}
		
		// If spammed or swore
		if (sender instanceof Player)
		if (!Filter.checkMessage((Player) sender, message))
			return;
		
		CustomPlayer senderCustomPlayer;
		if (sender instanceof Player)
			senderCustomPlayer = CustomPlayer.get(((Player) sender).getUniqueId());
		else
			senderCustomPlayer = null;
		
		CustomPlayer receiverCustomPlayer = CustomPlayer.get(receiver.getUniqueId());
		
		String sentFormat = Main.getPMSentFormat();
		String receivedFormat = Main.getPMReceivedFormat();
		
		String sentMessage = ChatColor.translateAlternateColorCodes('&', sentFormat);
		sentMessage = MessageFormat.format(sentMessage, senderCustomPlayer,receiverCustomPlayer, message);
		
		String receivedMessage = ChatColor.translateAlternateColorCodes('&', receivedFormat);
		receivedMessage = MessageFormat.format(receivedMessage, senderCustomPlayer, receiverCustomPlayer, message);
		
		receiver.sendMessage(receivedMessage);
		if (receiverCustomPlayer.getSettings().isMasterSoundsOn())
			receiver.playSound(receiver.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);
		
		sender.sendMessage(sentMessage);
		
		if (sender instanceof Player) {
			Player player = (Player) sender;
			
			lastReply.put(player.getUniqueId(), receiver.getUniqueId());
			lastReply.put(receiver.getUniqueId(), player.getUniqueId());
		}
		
		sendAllMonitoringPlayers(senderCustomPlayer, sentMessage, receiverCustomPlayer);
	}
	
	public static void playerLeft(UUID uuid)
	{
		lastReply.remove(uuid);
	}
	
	public static void reply(@NotNull Player sender, String message)
	{
		Player target = Bukkit.getPlayer(lastReply.get(sender.getUniqueId()));
		
		if (target == null)
		{
			sender.sendMessage(ChatColor.DARK_RED + "No one to to reply to.");
			return;
		}
		
		sendMessage(sender, target, message);
	}
	
	private static void sendAllMonitoringPlayers(@NotNull CustomPlayer messenger, String message, @NotNull CustomPlayer receiver) {
		for (CustomPlayer playerIndex : CustomPlayer.getOnlinePlayers())
			if (playerIndex.isMonitoring())
			if (messenger != playerIndex && receiver != playerIndex)
				sendMonitorMessage(messenger, receiver, message, receiver);
	}
	
	private static void sendMonitorMessage(@NotNull CustomPlayer messenger, @NotNull CustomPlayer receiver, String message, @NotNull CustomPlayer monitorPlayer) {
		monitorPlayer.getPlayer().sendMessage(String.format("%s[Private] %s%s (to %s)%s: %s%s", 
				ChatColor.YELLOW, ChatColor.DARK_GRAY, messenger.getPlayer().getDisplayName(), receiver.getPlayer().getDisplayName(),
				ChatColor.RESET, ChatColor.GRAY, message));
		
		return;
	}
}
