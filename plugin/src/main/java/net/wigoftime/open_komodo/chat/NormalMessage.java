package net.wigoftime.open_komodo.chat;

import github.scarsz.discordsrv.DiscordSRV;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.*;

public class NormalMessage 
{
	
	// Distance Range by blocks set in Config.
	private static final int distanceR = Main.getDistanceRange();
	
	public static void sendMessage(@NotNull CustomPlayer senderCustomPlayer, String message) {
		if (senderCustomPlayer.isInTutorial())
		if (!senderCustomPlayer.getTutorial().validState(NormalMessage.class)) return;
		
		Location senderLocation = senderCustomPlayer.getPlayer().getLocation();
		
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
		
		{
		ComponentBuilder componentHoverDescBuilder = new ComponentBuilder(String.format("Username: %s", senderCustomPlayer.getPlayer().getDisplayName()));

		synchronized (senderCustomPlayer.getPartners()) {
			if (senderCustomPlayer.getPartners().size() > 0) {
				if (senderCustomPlayer.getPartners().size() > 1) {
					componentHoverDescBuilder.append("\nMarried: ");

					Iterator<OfflinePlayer> partnerIterator = senderCustomPlayer.getPartners().iterator();
					while (partnerIterator.hasNext()) {
						componentHoverDescBuilder.append(String.format("%s%s", partnerIterator.next().getName(), partnerIterator.hasNext() ? ", " : ""));
					}
				} else {
					componentHoverDescBuilder.append(
							String.format("\nMarried: %s", senderCustomPlayer.getMarriageSystem().getPartners().get(0).getName()));
				}
			}

		}

		Date joinDate = senderCustomPlayer.getJoinDate();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		componentHoverDescBuilder.append(String.format("\nJoined Date: %s", joinDate == null ? "??" : simpleDateFormat.format(joinDate)));

		if (senderCustomPlayer.discordUser != null)
			componentHoverDescBuilder.append(String.format("\nDiscord: %s", senderCustomPlayer.discordUser.getAsTag()));
		if (senderCustomPlayer.getSettings().isDisplayTipEnabled())
			componentHoverDescBuilder.append(String.format("\nTipped: $%.1f", senderCustomPlayer.getDonated()));
		BaseComponent[] hoverDescription = componentHoverDescBuilder.create();
		BaseComponent[] name = senderCustomPlayer.getCustomName();
		if (name == null) {
			name = new BaseComponent[] {new TextComponent(senderCustomPlayer.getPlayer().getDisplayName())};
			name[0].setColor(ChatColor.DARK_GRAY);
			name[0].setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverDescription));
			name[0].setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/msg %s ", senderCustomPlayer.getPlayer().getDisplayName())));
			componentList.add(name[0]);
		} else {
			ComponentBuilder nameBuilder = new ComponentBuilder();
			TextComponent defaultColor = new TextComponent();
			defaultColor.setColor(ChatColor.DARK_GRAY);
			nameBuilder.append(defaultColor);
			
			for (BaseComponent componentIndex : name) {
			componentIndex.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverDescription));
			componentIndex.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/msg %s ", senderCustomPlayer.getPlayer().getDisplayName())));
			
			nameBuilder.append(componentIndex);
			}
			
			componentList.addAll(Arrays.asList(nameBuilder.create()));
		}
		}
		
		TextComponent messageComponent = new TextComponent(String.format("%s: ", ChatColor.WHITE));
		messageComponent.setColor(ChatColor.WHITE);
		componentList.add(messageComponent);
		TextComponent message2Component = new TextComponent(String.format("%s", message));
		message2Component.setColor(ChatColor.GRAY);
		componentList.add(message2Component);
		
		BaseComponent[] componentBaseMessage = componentList.toArray(new BaseComponent[componentList.size()]);
		
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
	
	public static void sendToDiscord(@NotNull CustomPlayer playerCustomPlayer, String message) {
		if (Main.getDiscordSRV() == null)
			return;

		((DiscordSRV) Main.getDiscordSRV()).processChatMessage(
				playerCustomPlayer.getPlayer(),
				message,
				"moderation",
				false);

		if (!playerCustomPlayer.getSettings().isDiscordChatEnabled())
			return;
		
		// Send message to Discord
		((DiscordSRV) Main.getDiscordSRV()).processChatMessage(
				playerCustomPlayer.getPlayer(),
				message,
				"minecraft-chat",
				false);
	}
	
	private static void sendMonitorMessage(@NotNull CustomPlayer messenger, String message, @NotNull CustomPlayer monitorPlayer) {
		monitorPlayer.getPlayer().sendMessage(String.format("%s[Public] %s%s%s: %s%s", 
				ChatColor.YELLOW, ChatColor.DARK_GRAY, messenger.getPlayer().getDisplayName(), 
				ChatColor.RESET, ChatColor.GRAY, message));
		
		return;
	}
}
