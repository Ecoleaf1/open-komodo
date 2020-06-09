package net.wigoftime.open_komodo.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.logging.log4j.core.config.yaml.YamlConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.chat.MessageFormat;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.objects.TpRequest.tpType;

public class CustomPlayer
{
	private final Player player;
	private final UUID uuid;
	
	private Rank rank;
	
	private boolean buildMode;
	
	private TpRequest tpRequest;
	private boolean allowTpaRequests;
	
	private static Map<UUID, CustomPlayer> mapOfPlayers = new HashMap<UUID, CustomPlayer>();
	
	public CustomPlayer(Player player, Rank rank)
	{
		this.player = player;
		uuid = player.getUniqueId();
		this.rank = rank;
		buildMode = false;
		allowTpaRequests = true;
		
		mapOfPlayers.put(uuid, this);
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public UUID getUniqueId()
	{
		return uuid;
	}
	
	public Rank getRank()
	{
		return rank;
	}
	
	public void setRank(Rank rank)
	{
		PlayerConfig.setRank(this.player.getUniqueId(), rank.getName());
		this.rank = rank;
	}
	
	public static final String buildingError = ChatColor.DARK_RED + "Sorry, but you must disable build mode in order to open this.";
	public boolean isBuilding()
	{
		return buildMode;
	}
	
	public void setBuilding(boolean isBuilding)
	{
		buildMode = isBuilding;
	}
	
	public void reload()
	{
		String rankName = PlayerConfig.getRank(player);
		
		rank = Rank.getRank(rankName);
	}
	
	public static CustomPlayer create(Player player)
	{
		String rankName = PlayerConfig.getRank(player);
		
		return new CustomPlayer(player, Rank.getRank(rankName));
	}
	
	public static CustomPlayer get(UUID uuid)
	{
		return mapOfPlayers.get(uuid);
	}
	
	public static List<CustomPlayer> getOnlinePlayers()
	{
		List<CustomPlayer> list = new ArrayList<CustomPlayer>(mapOfPlayers.size());
		for (Entry<UUID, CustomPlayer> e : mapOfPlayers.entrySet())
		{
			list.add(e.getValue());
		}
		
		return list;
	}
	
	private static final String alreadySent = ChatColor.translateAlternateColorCodes('&', "&cYou already sent a request.");
	private static final String noRequests = ChatColor.translateAlternateColorCodes('&', "&7You don't have any requests");
	
	private static final String receivedRequestTpa = ChatColor.translateAlternateColorCodes('&', "&7$N requests to teleport to you!\n&6/tpaccept&7 to accept\n&6/tpadeny&7 to deny");
	private static final String receivedRequestTpaHere = ChatColor.translateAlternateColorCodes('&', "&7$N requests to teleport to them!\n&6/tpaccept&7 to accept\n&6/tpadeny&7 to deny");
	
	private static final String sentRequest = ChatColor.translateAlternateColorCodes('&', "&7You have sent the request to $D");
	
	private static final String acceptedRequester = ChatColor.translateAlternateColorCodes('&', "&7$N has accepted your request.");
	private static final String acceptedTarget = ChatColor.translateAlternateColorCodes('&', "&7You accepted $D request.");
	
	private static final String deniedRequest = ChatColor.translateAlternateColorCodes('&', "&7You denied the request.");
	
	public static final String errorCantFindPerson = ChatColor.translateAlternateColorCodes('&', "&cCan't find $D");
	public static final String tpaOff = ChatColor.GRAY + "Sorry, but they have disabled tpa requests,";
	
	public void tpaRequest(CustomPlayer requester, tpType type)
	{
		if (!allowTpaRequests)
		{
			requester.getPlayer().sendMessage(tpaOff);
			return;
		}
		
		tpRequest = new TpRequest(this.getPlayer(), requester.getPlayer(), type);
		
		String requesterMessage = MessageFormat.format(sentRequest, requester.getPlayer(), this.getPlayer(), null);
		requester.getPlayer().sendMessage(requesterMessage);
		
		String targetMessage = MessageFormat.format(type == tpType.TPA ? receivedRequestTpa : receivedRequestTpaHere, requester.getPlayer(), this.getPlayer(), null);
		this.getPlayer().sendMessage(targetMessage);
	}
	
	public void tpaAccept()
	{
		if (tpRequest == null) {
			this.getPlayer().sendMessage(noRequests);
			return;
		}
		
		if (tpRequest.getType() == tpType.TPA)
			tpRequest.getRequester().teleport(tpRequest.getTarget());
		else if (tpRequest.getType() == tpType.TPAHERE)
			tpRequest.getTarget().teleport(tpRequest.getRequester());
		
		
		String requesterMessage = MessageFormat.format(acceptedRequester, this.getPlayer(), tpRequest.getRequester(), null);
		String targetMessage = MessageFormat.format(acceptedTarget, this.getPlayer(), tpRequest.getRequester(), null);
		
		tpRequest.getRequester().sendMessage(requesterMessage);
		tpRequest.getTarget().sendMessage(targetMessage);
	}
	
	public void tpaDeny()
	{
		if (tpRequest == null) {
			this.getPlayer().sendMessage(noRequests);
			return;
		}
		
		tpRequest = null;
		this.getPlayer().sendMessage(deniedRequest);
		return;
	}
	
	public void prepareDestroy()
	{
		mapOfPlayers.remove(this.getUniqueId());
	}
	
	public boolean isTpaRequestAllowed()
	{
		return allowTpaRequests;
	}
	
	public void setTpaRequestAllowed(boolean isAllowed)
	{
		allowTpaRequests = isAllowed;
	}
}
