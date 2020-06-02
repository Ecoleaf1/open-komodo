package net.wigoftime.open_komodo.etc;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.chat.MessageFormat;
import net.wigoftime.open_komodo.config.PlayerConfig;

abstract public class FriendSystem {

	private static final String requestMessage = ChatColor.translateAlternateColorCodes('&', "$D wants to friend you! Type \"/friend accept "+"$n\" to accept!");
	private static final String friendAdded = ChatColor.translateAlternateColorCodes('&', "&a&lYou accepted $N’s friend request");
	private static final String nowFriends = ChatColor.translateAlternateColorCodes('&', "“&a&l$D accepted your friend request!");
	private static final String errorFriendAdded = ChatColor.translateAlternateColorCodes('&', "&e&lYou’re already friends with $D!");
	
	private static final String requested = ChatColor.translateAlternateColorCodes('&', "&b&lYou received a friend request from $N!");
	private static final String requestedSent = ChatColor.translateAlternateColorCodes('&', "“&b&lYou successfully sent your friend request to $D");
	
	private static final String removedSucess = ChatColor.translateAlternateColorCodes('&', "You removed $D as your friend.");
	
	private static final String friendOnlineMsg = ChatColor.translateAlternateColorCodes('&', "&e&l$S joined the game!");
	private static final String friendOfflineMsg = ChatColor.translateAlternateColorCodes('&', "&e&l$S left the game!");
	
	private static HashMap<Player, Player> requests = new HashMap<Player, Player>();
	
	public static void requestPlayer(Player requester, Player target) {
		requests.put(target, requester);
		
		String text = requestMessage;
		text = MessageFormat.format(text, requester, target,null);
		
		target.sendMessage(text);
	}
	
	public static void removeAsFriends(Player sender, OfflinePlayer target) {
		File senderConfig = PlayerConfig.getPlayerConfig(sender);
		YamlConfiguration senderYaml = YamlConfiguration.loadConfiguration(senderConfig);
		
		for (String s : senderYaml.getStringList("Friends")) {
			if (UUID.fromString(s).equals(target.getUniqueId())) {
				File targetConfig = PlayerConfig.getPlayerConfig(target);
				YamlConfiguration targetYaml = YamlConfiguration.loadConfiguration(targetConfig);
				
				List<String> senderFriends = senderYaml.getStringList("Friends");
				List<String> targetFriends = targetYaml.getStringList("Friends");
				
				senderFriends.remove(target.getUniqueId().toString());
				targetFriends.remove(sender.getUniqueId().toString());

				senderYaml.set("Friends", senderFriends);
				targetYaml.set("Friends", targetFriends);

				try {
					senderYaml.save(senderConfig);
					targetYaml.save(targetConfig);
				} catch (IOException e) {
					PrintConsole.print(String.format("ERROR: %s (%s) couldn't remove %s (%s) as friends.", sender.getUniqueId(),sender.getDisplayName(),target.getUniqueId(),target.getName()));
				}
				
				String message = MessageFormat.format(removedSucess, sender, target, null);
				sender.sendMessage(message);
				
				return;
			}
		}
		
		sender.sendMessage(String.format("%s isn't in your friends list.", target.getName()));
		
	}
	
	public static void addPlayer (Player sender, Player target) {
		
		File senderConfig = PlayerConfig.getPlayerConfig(sender);
		YamlConfiguration senderYaml = YamlConfiguration.loadConfiguration(senderConfig);
		
		for (String s : senderYaml.getStringList("Friends")) {
			UUID uuid = UUID.fromString(s);
			
			if (uuid.equals(target.getUniqueId())) {
				String message = errorFriendAdded;
				message = MessageFormat.format(message, sender, target, null);
				sender.sendMessage(message);
				return;
			}
		}
		
		if (requests.containsKey(target))
			requests.replace(target, sender);
		else
			requests.put(target, sender);
		{
		String message = requested;
		message = MessageFormat.format(message, sender, target,null);
		target.sendMessage(message);
		}
		
		{
		String message = requestedSent;
		message = MessageFormat.format(message, sender, target,null);
		sender.sendMessage(message);
		
		}
		
	}
	
	public static void acceptPlayer(Player target) {
		Player requester = requests.get(target);
		
		File targetConfig = PlayerConfig.getPlayerConfig(target);
		File requesterConfig = PlayerConfig.getPlayerConfig(requester);
		
		YamlConfiguration targetYaml = YamlConfiguration.loadConfiguration(targetConfig);
		YamlConfiguration requesterYaml = YamlConfiguration.loadConfiguration(requesterConfig);
		
		List<String> targetFriends = targetYaml.getStringList("Friends");
		targetFriends.add(requester.getUniqueId().toString());
		targetYaml.set("Friends", targetFriends);
		
		List<String> requesterFriends = requesterYaml.getStringList("Friends");
		requesterFriends.add(target.getUniqueId().toString());
		requesterYaml.set("Friends", requesterFriends);
		
		// Try save the updated friends
		
		try {
			targetYaml.save(targetConfig);
			requesterYaml.save(requesterConfig);
		} catch (IOException e) {
			PrintConsole.print(String.format("ERROR: %s (%s) and %s (%s) couldn't add each other as friends properly", target.getUniqueId(),target.getDisplayName(),requester.getUniqueId(),requester.getDisplayName()));
		}
		
		requests.remove(target);
		
		String targetMessage = MessageFormat.format(friendAdded, target,requester, null);
		target.sendMessage(targetMessage);
		
		String requesterMessage = MessageFormat.format(nowFriends, requester, requester,null);
		requester.sendMessage(requesterMessage);
		
		
	}
	
	public static Player getRequester(Player target) {
		if (requests.containsKey(target))
			return requests.get(target);
		else
			return null;
		
	}
	
	public static String getFriendOnlineMsg() {
		return friendOnlineMsg;
	}
	
	public static String getFriendOfflineMsg() {
		return friendOfflineMsg;
	}
	
}
