package net.wigoftime.open_komodo.chat;

import java.text.SimpleDateFormat;
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
		Location senderLocation = senderCustomPlayer.getPlayer().getLocation();
		
		// Get message in JSON template
		//String format = MessageFormat.json(senderCustomPlayer, message);
		
		// Convert custom component
		List<BaseComponent> componentList = new LinkedList<BaseComponent>();
		
		if (!senderCustomPlayer.getTagDisplay().equals("")) {
			TextComponent componentTag = new TextComponent(String.format("%s%s %s| ", ChatColor.GRAY, senderCustomPlayer.getTagDisplay(), ChatColor.DARK_GRAY));
			componentList.add(componentTag);
		}
		
		if (senderCustomPlayer.getRank() != null) {
			TextComponent componentRank = new TextComponent(String.format("%s", ChatColor.translateAlternateColorCodes('&', senderCustomPlayer.getRank().getPrefix())));
			componentList.add(componentRank);
		}
		else
		{
			TextComponent componentRank = new TextComponent(String.format(ChatColor.DARK_GRAY + ""));
			componentList.add(componentRank);
		}
		
		//TextComponent name;
		BaseComponent[] name = senderCustomPlayer.getCustomName();
		//name = new TextComponent(String.format("%s%s", ChatColor.DARK_GRAY + senderCustomPlayer.getPlayer().getCustomName() == null ? senderCustomPlayer.getPlayer().getDisplayName() : senderCustomPlayer.getPlayer().getCustomName())); 
		{
		ComponentBuilder componentHoverDescBuilder = new ComponentBuilder(String.format("Username: %s\n", senderCustomPlayer.getPlayer().getDisplayName()));
		
		Date joinDate = senderCustomPlayer.getJoinDate();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		componentHoverDescBuilder.append(String.format("Joined Date: %s", joinDate == null ? "??" : simpleDateFormat.format(joinDate)));
		if (senderCustomPlayer.getSettings().isDisplayTipEnabled())
		componentHoverDescBuilder.append(String.format("\nTipped: %d$", senderCustomPlayer.getDonated()));
		
		BaseComponent[] hoverDescription = componentHoverDescBuilder.create();
		for (BaseComponent componentIndex : name) {
			componentIndex.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverDescription));
			componentIndex.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/msg %s ", senderCustomPlayer.getPlayer().getDisplayName())));
			
			componentIndex.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,hoverDescription));
			componentList.add(componentIndex);
		}
		}
		
		TextComponent messageComponent = new TextComponent(String.format("%s: ", ChatColor.WHITE));
		messageComponent.setColor(ChatColor.WHITE);
		componentList.add(messageComponent);
		TextComponent message2Component = new TextComponent(String.format("%s", message));
		message2Component.setColor(ChatColor.GRAY);
		componentList.add(message2Component);
		
		BaseComponent[] componentBaseMessage = componentList.toArray(new BaseComponent[componentList.size()]);
		
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
		sendToDiscord(senderCustomPlayer.getPlayer(), message);
	}
	
	public static void shout(Player sender, String message) {	
		CustomPlayer senderCustomPlayer = CustomPlayer.get(sender.getUniqueId());
		
		// Get message in JSON template
		//String format = MessageFormat.json(senderCustomPlayer, message);
		
		List<BaseComponent> componentList = new LinkedList<BaseComponent>();
		
		if (!senderCustomPlayer.getTagDisplay().equals("")) {
			TextComponent componentTag = new TextComponent(String.format("%s%s %s| ", ChatColor.GRAY, senderCustomPlayer.getTagDisplay(), ChatColor.DARK_GRAY));
			componentList.add(componentTag);
		}
		
		if (senderCustomPlayer.getRank() != null) {
			TextComponent componentRank = new TextComponent(String.format("%s", ChatColor.translateAlternateColorCodes('&', senderCustomPlayer.getRank().getPrefix())));
			componentList.add(componentRank);
		} else {
			TextComponent componentRank = new TextComponent(String.format(ChatColor.DARK_GRAY + ""));
			componentList.add(componentRank);
		}
		
		BaseComponent[] name = senderCustomPlayer.getCustomName(); {
		ComponentBuilder componentHoverDescBuilder = new ComponentBuilder(String.format("Username: %s\n", sender.getDisplayName()));
		
		Date joinDate = senderCustomPlayer.getJoinDate();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		componentHoverDescBuilder.append(String.format("Joined Date: %s", joinDate == null ? "??" : simpleDateFormat.format(joinDate)));
		if (senderCustomPlayer.getSettings().isDisplayTipEnabled())
		componentHoverDescBuilder.append(String.format("\nTipped: %d$", senderCustomPlayer.getDonated()));
		
		BaseComponent[] hoverDescription = componentHoverDescBuilder.create();
		
		for (BaseComponent componentIndex : name) {
			componentIndex.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverDescription));
			componentIndex.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/msg %s ", senderCustomPlayer.getPlayer().getDisplayName())));
			
			componentIndex.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,hoverDescription));
			componentList.add(componentIndex);
		}
		}
		
		TextComponent messageComponent = new TextComponent(": "+ChatColor.GRAY+message);
		componentList.add(messageComponent);
		
		for (Player p : Bukkit.getOnlinePlayers())
			p.spigot().sendMessage(componentList.toArray(new BaseComponent[componentList.size()]));
		
		// Messages the server terminal/console as well.
		Bukkit.getLogger().info(String.format("[shout] %s: %s", sender.getName(), message));
		
		// Send message to discord (If enabled)
		sendToDiscord(senderCustomPlayer.getPlayer(), message);
	}
	
	private static void sendToDiscord(Player player, String message) {
		if (Main.getDiscordSRV() == null)
			return;
		
		// Send message to Discord
		((DiscordSRV) Main.getDiscordSRV()).processChatMessage(player, message, null, false);
	}
	
	private static void sendMonitorMessage(CustomPlayer messenger, String message, CustomPlayer monitorPlayer) {
		monitorPlayer.getPlayer().sendMessage(String.format("%s[Public] %s%s%s: %s%s", 
				ChatColor.YELLOW, ChatColor.DARK_GRAY, messenger.getPlayer().getDisplayName(), 
				ChatColor.RESET, ChatColor.GRAY, message));
		
		return;
	}
}
