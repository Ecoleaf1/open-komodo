package net.wigoftime.open_komodo.etc;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.chat.MessageFormat;
import net.wigoftime.open_komodo.objects.TpRequest;
import net.wigoftime.open_komodo.objects.TpRequest.tpType;

abstract public class TpSystem {

	private static HashMap<Player, TpRequest> tpaMap = new HashMap<Player, TpRequest>();
	
	public static final String errorCantFindPerson = ChatColor.translateAlternateColorCodes('&', "&cCan't find $D");
	private static final String alreadySent = ChatColor.translateAlternateColorCodes('&', "&cYou already sent a request.");
	private static final String noRequests = ChatColor.translateAlternateColorCodes('&', "&7You don't have any requests");
	
	private static final String receivedRequestTpa = ChatColor.translateAlternateColorCodes('&', "&7$N requests to teleport to you!\n&6/tpaccept&7 to accept\n&6/tpadeny&7 to deny");
	private static final String receivedRequestTpaHere = ChatColor.translateAlternateColorCodes('&', "&7$N requests to teleport to them!\n&6/tpaccept&7 to accept\n&6/tpadeny&7 to deny");
	
	private static final String sentRequest = ChatColor.translateAlternateColorCodes('&', "&7You have sent the request to $D");
	
	private static final String acceptedRequester = ChatColor.translateAlternateColorCodes('&', "&7$N has accepted your request.");
	private static final String acceptedTarget = ChatColor.translateAlternateColorCodes('&', "&7You accepted $D request.");
	
	private static final String deniedRequest = ChatColor.translateAlternateColorCodes('&', "&7You denied the request.");
	
	public static void request(Player requester, Player target, tpType type) {
		
		if (tpaMap.containsKey(target))
			if (tpaMap.get(target) == requester) {
				
				String message = MessageFormat.format(alreadySent, requester, target, null);
				
				requester.sendMessage(message);
				return;
			}
		
		tpaMap.put(target, new TpRequest(target, requester, type));
		
		String requesterMessage = MessageFormat.format(sentRequest, requester, target, null);
		requester.sendMessage(requesterMessage);
		
		String targetMessage = MessageFormat.format(type == tpType.TPA ? receivedRequestTpa : receivedRequestTpaHere, requester, target, null);
		target.sendMessage(targetMessage);
		
	}
	
	public static void acceptTpa(Player target) {
		
		if (!tpaMap.containsKey(target)) {
			target.sendMessage(noRequests);
			return;
		}
		
		TpRequest request = tpaMap.get(target);
		tpType type = request.getType();
		
		if (type == tpType.TPA)
			request.getRequester().teleport(request.getTarget());
		
		if (type == tpType.TPAHERE)
			request.getRequester().teleport(request.getTarget());
		
		
		String requesterMessage = MessageFormat.format(acceptedRequester, target, request.getRequester(), null);
		String targetMessage = MessageFormat.format(acceptedTarget, target, request.getRequester(), null);
		
		request.getRequester().sendMessage(requesterMessage);
		request.getTarget().sendMessage(targetMessage);
		
		/*requester.teleport(target.getLocation());*/
		
		tpaMap.remove(target);
	}
	public static void denyTpa(Player target) {
		if (!tpaMap.containsKey(target)) {
			target.sendMessage(noRequests);
			return;
		}
		
		target.sendMessage(deniedRequest);
		tpaMap.remove(target);
		return;
	}
	
	public static void playerLeft(Player target)
	{
		tpaMap.remove(target);
	}
	
}
