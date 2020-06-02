package net.wigoftime.open_komodo.etc;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.chat.MessageFormat;

abstract public class TpSystem {

	private static HashMap<Player, Player> tpaMap = new HashMap<Player, Player>();
	
	public static final String commandDesc = ChatColor.translateAlternateColorCodes('&', "&c/tpa (username)");
	
	public static final String errorCantFindPerson = ChatColor.translateAlternateColorCodes('&', "&cCan't find $D");
	private static final String alreadySent = ChatColor.translateAlternateColorCodes('&', "&cYou already sent a tpa request.");
	private static final String noRequests = ChatColor.translateAlternateColorCodes('&', "&7You don't have any requests");
	
	private static final String receivedRequest = ChatColor.translateAlternateColorCodes('&', "&7$N requests to teleport to you!\n&6/tpaccept&7 to accept\n&6/tpadeny&7 to deny");
	private static final String sentRequest = ChatColor.translateAlternateColorCodes('&', "&7You have sent the request to $D");
	
	private static final String acceptedRequester = ChatColor.translateAlternateColorCodes('&', "&7$N has accepted your request.");
	private static final String acceptedTarget = ChatColor.translateAlternateColorCodes('&', "&7You accepted $D request.");
	
	private static final String deniedRequest = ChatColor.translateAlternateColorCodes('&', "&7You denied the request.");
	
	public static void requestTpa(Player requester, Player target) {
		
		if (tpaMap.containsKey(target))
			if (tpaMap.get(target) == requester) {
				
				String message = MessageFormat.format(alreadySent, requester, target, null);
				
				requester.sendMessage(message);
				return;
			}
		
		tpaMap.put(target, requester);
		
		String requesterMessage = MessageFormat.format(sentRequest, requester, target, null);
		requester.sendMessage(requesterMessage);
		
		String targetMessage = MessageFormat.format(receivedRequest, requester, target, null);
		target.sendMessage(targetMessage);
		
	}
	
	public static void denyTpa(Player target) {
		if (!tpaMap.containsKey(target)) {
			target.sendMessage(noRequests);
			return;
		}
		
		Player requester = tpaMap.get(target);
		
		String message = MessageFormat.format(deniedRequest, target, requester, null);
		target.sendMessage(deniedRequest);
		tpaMap.remove(target);
		return;
	}
	
	public static void acceptTpa(Player target) {
		
		if (!tpaMap.containsKey(target)) {
			target.sendMessage(noRequests);
			return;
		}
		
		Player requester = tpaMap.get(target);

		String requesterMessage = MessageFormat.format(acceptedRequester, target, requester, null);
		String targetMessage = MessageFormat.format(acceptedTarget, target, requester, null);
		
		requester.sendMessage(requesterMessage);
		target.sendMessage(targetMessage);
		
		requester.teleport(target.getLocation());
		
		tpaMap.remove(target);
	}
	
}
