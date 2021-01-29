package net.wigoftime.open_komodo.etc;

import java.awt.Color;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.wigoftime.open_komodo.objects.CustomPlayer;

abstract public class NickName 
{
	// Error message for not having colornick
	private static final String errorNotPermittedColor = ChatColor.DARK_RED + "Only VIP and up can color their nicknames.";
	private static final String errorNotPermittedHexColor = ChatColor.DARK_RED + "Only MVP+ and up can have hex colors on their nicknames.";
	
	
	public static void changeNick(Player player, String name) 
	{
		
		if (name == null) 
		{
			player.setCustomName(player.getName());
			return;
		}
		
		if (!Filter.checkMessage(player, name))
			return;
		
		if (!player.hasPermission(Permissions.moreColorNickPerm))
		if (name.contains("#")) {
			player.sendMessage(errorNotPermittedHexColor);
			return;
		} else if (!player.hasPermission(Permissions.colorNickPerm))
			if (name.contains("&")) {
			player.sendMessage(errorNotPermittedColor);
			return;
			}
		
		CustomPlayer playerCustom = CustomPlayer.get(player.getUniqueId());
		BaseComponent[] customName;
		if (player.hasPermission(Permissions.moreColorNickPerm)) customName = translateRGBColorCodes('#', '&', name);
		else if (player.hasPermission(Permissions.colorNickPerm)) customName = translateRGBColorCodes('\u0000', '&', name);
		else customName = translateRGBColorCodes('\u0000', '\u0000', name);
		
		if (customName != null)
		playerCustom.setCustomName(customName);
		
		ComponentBuilder builder = new ComponentBuilder();
		builder.append(new TextComponent("You have changed your name to "));
		builder.color(ChatColor.DARK_AQUA);
		
		builder.append(playerCustom.getCustomName());
		
		player.spigot().sendMessage(builder.create());
	}
	
	private static BaseComponent[] translateRGBColorCodes(char colorChar, char bukkitColorChar, String textToTranslate) {
		StringBuilder hexStringBuilder = new StringBuilder();
		boolean awaitingBukkitColorFormat = false;
		
		ComponentBuilder builder = new ComponentBuilder();
		TextComponent text = new TextComponent();
		ChatColor indexColor = ChatColor.DARK_GRAY;
		boolean endOfHexCode = false;
		for (char charIndex : textToTranslate.toCharArray()) {
			if (hexStringBuilder.length() > 0) {
				if (hexStringBuilder.toString().length() > 6) {
					text.setColor(indexColor);
					builder.append(text);
					text = new TextComponent();
					
					text.setBold(false);
					text.setItalic(false);
					text.setStrikethrough(false);
					text.setObfuscated(false);
					ChatColor nextColor = ChatColor.of(hexStringBuilder.toString());
					indexColor = nextColor == null ? ChatColor.DARK_GRAY : nextColor;
					hexStringBuilder = new StringBuilder();
					endOfHexCode = true;
				}
				
				if (!endOfHexCode) {
					hexStringBuilder.append(charIndex);
					continue;
				}
			}
			endOfHexCode = false;
			
			if (charIndex == colorChar) {
				hexStringBuilder.append(colorChar);
				continue;
			}
			
			if (awaitingBukkitColorFormat) {
				builder.append(text);
				text = new TextComponent();
				switch (charIndex) {
				case 'l':
					text.setBold(true);
					break;
				case 'o':
					text.setItalic(true);
					break;
				case 'm':
					text.setStrikethrough(true);
					break;
				case 'k':
					text.setObfuscated(true);
					break;
				case 'r':
					text.setBold(false);
					text.setItalic(false);
					text.setStrikethrough(false);
					text.setObfuscated(false);
					indexColor = ChatColor.DARK_GRAY;
					text.setColor(indexColor);
					break;
					
				default:
					indexColor = ChatColor.getByChar(charIndex);
					text.setColor(indexColor);
				}
				awaitingBukkitColorFormat = false;
				continue;
			}
			
			if (charIndex == bukkitColorChar) {
				awaitingBukkitColorFormat = true;
				continue;
			}
			
			text.addExtra(charIndex +"");
		}
		
		if (text.toPlainText().length() > 0) {
			text.setColor(indexColor);
			builder.append(text);
		}
		
		BaseComponent[] components = builder.create();
		if (components.length == 0)
			return null;
		
		// Check if player has empty name
		boolean isEmptyName = true;
		for (BaseComponent componentIndex : components) {
			if (componentIndex.toPlainText().length() > 0) {
				PrintConsole.test(componentIndex.toPlainText());
				isEmptyName = false;
				break;
			}
		}
		
		if (isEmptyName)
			return null;
		
		return components;
	}
}
