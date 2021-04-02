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
	
	
	public static void changeNick(CustomPlayer playerCustom, String name) 
	{
		
		if (name == null) 
		{
			playerCustom.setCustomName(null, null);
			return;
		}
		
		if (name.length() > 50) {
			playerCustom.getPlayer().sendMessage(String.format("%s%sHEY! %sSorry, but please reduce your letters", 
					ChatColor.RED, ChatColor.BOLD, ChatColor.DARK_RED));
			return;
		}
		
		if (!Filter.checkMessage(playerCustom.getPlayer(), name))
			return;
		
		if (!playerCustom.getPlayer().hasPermission(Permissions.moreColorNickPerm))
		if (name.contains("#")) {
			playerCustom.getPlayer().sendMessage(errorNotPermittedHexColor);
			return;
		} else if (!playerCustom.getPlayer().hasPermission(Permissions.colorNickPerm))
			if (name.contains("&")) {
				playerCustom.getPlayer().sendMessage(errorNotPermittedColor);
			return;
			}
		
		BaseComponent[] customName;
		
		if (playerCustom.getPlayer().hasPermission(Permissions.moreColorNickPerm)) customName = translateRGBColorCodes('#', '&', name);
		else if (playerCustom.getPlayer().hasPermission(Permissions.colorNickPerm)) customName = translateRGBColorCodes(' ', '&', name);
		else customName = translateRGBColorCodes(' ', ' ', name);
		
		if (customName == null) playerCustom.setCustomName(null, null);
		else playerCustom.setCustomName(customName, name);
		
		ComponentBuilder builder = new ComponentBuilder();
		builder.append(new TextComponent("You have changed your name to "));
		builder.color(ChatColor.DARK_AQUA);
		
		if (customName == null) builder.append(playerCustom.getPlayer().getDisplayName());
		else builder.append(customName);
		
		playerCustom.getPlayer().spigot().sendMessage(builder.create());
	}
	
	public static BaseComponent[] translateRGBColorCodes(char colorChar, char bukkitColorChar, String textToTranslate) {
			
		StringBuilder hexStringBuilder = new StringBuilder();
		boolean awaitingBukkitColorFormat = false;
		
		ComponentBuilder builder = new ComponentBuilder();
		TextComponent text = new TextComponent();
		ChatColor indexColor = null;
		boolean endOfHexCode = false;
		for (char charIndex : textToTranslate.toCharArray()) {
			PrintConsole.test("char:" + charIndex);
			
			if (hexStringBuilder.toString().length() > 0) {
				if (hexStringBuilder.toString().length() > 6) {
					
					if (indexColor != null) text.setColor(indexColor);
					builder.append(text);
					text = new TextComponent();
					
					text.setBold(false);
					text.setItalic(false);
					text.setStrikethrough(false);
					text.setObfuscated(false);
					ChatColor nextColor = ChatColor.of(hexStringBuilder.toString());
					
					if (nextColor != null) indexColor = nextColor;
					//indexColor = nextColor == null ? ChatColor.DARK_GRAY : nextColor;
					hexStringBuilder = new StringBuilder();
					endOfHexCode = true;
				}
				
				if (!endOfHexCode) {
					hexStringBuilder.append(charIndex);
					continue;
				}
			}
			endOfHexCode = false;
			
			if (charIndex == colorChar && colorChar != ' ') {
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
					indexColor = ChatColor.RESET;
					text.setColor(indexColor);
					break;
					
				default:
					indexColor = ChatColor.getByChar(charIndex);
					text.setColor(indexColor);
				}
				awaitingBukkitColorFormat = false;
				continue;
			}
			
			if (charIndex == bukkitColorChar && bukkitColorChar != ' ') {
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
