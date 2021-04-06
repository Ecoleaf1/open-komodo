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
		ComponentBuilder componentHoverDescBuilder = new ComponentBuilder(String.format("Username: %s\n", sender.getPlayer().getDisplayName()));
		
		Date joinDate = senderCustomPlayer.getJoinDate();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
		componentHoverDescBuilder.append(String.format("Joined Date: %s", joinDate == null ? "??" : simpleDateFormat.format(joinDate)));
		if (senderCustomPlayer.discordUser != null) componentHoverDescBuilder.append(String.format("\nDiscord: %s", senderCustomPlayer.discordUser.getAsTag()));
		if (senderCustomPlayer.getSettings().isDisplayTipEnabled())
		componentHoverDescBuilder.append(String.format("\nTipped: $%.1f", senderCustomPlayer.getDonated()));
		
		BaseComponent[] hoverDescription = componentHoverDescBuilder.create();
		
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
		
		if (message.contains("! ")) message = message.replaceFirst("! ", "");
		else message = message.replaceFirst("!", "");
		TextComponent messageComponent = new TextComponent(": "+ChatColor.GOLD + ChatColor.BOLD + "! " + ChatColor.GRAY + message);
		componentList.add(messageComponent);
		
		if (senderCustomPlayer.isInTutorial()) {
			senderCustomPlayer.getPlayer().spigot().sendMessage(componentList.toArray(new BaseComponent[componentList.size()]));
			senderCustomPlayer.getTutorial().trigger(ShoutMessage.class);
			return;
		}
		
		for (Player p : Bukkit.getOnlinePlayers())
			p.spigot().sendMessage(componentList.toArray(new BaseComponent[componentList.size()]));
		
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
