package net.wigoftime.open_komodo.chat;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.wigoftime.open_komodo.config.EmoteConfig;
import net.wigoftime.open_komodo.etc.NickName;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class Emote {
	
	private static final String errorOnlySolo = "&cSorry, but this is a solo only emote.";
	private static final String errorOnlyDirect = "&cSorry, but you have to target others for this emote.";
	
	private final String name;
	private final String command;
	
	private final String msgSolo;
	private final String msgOthers;
	
	public static HashMap<String, Emote> nameSortMap = new HashMap<String, Emote>();
	private static HashMap<String, Emote> commandSortMap = new HashMap<String, Emote>();
	
	public Emote(String name,String command,String msgSolo, String msgOthers) 
	{
		this.name = name;
		this.command = command;
		
		this.msgOthers = msgOthers;
		this.msgSolo = msgSolo;
		
		if (name != null) nameSortMap.put(name.toLowerCase(), this);
		if (command != null) commandSortMap.put(command.toLowerCase(), this);
	}
	
	public String getOtherMsg() { return msgOthers; }
	public String getSoloMsg() { return msgSolo; }
	
	// Static functions
	
	public static void send(String emote, CustomPlayer sender, CustomPlayer directPlayer) 
	{
		Emote emoteObj;
		
		if (emote.startsWith("/"))
			emoteObj = commandSortMap.get(emote.toLowerCase());
		else
			emoteObj = nameSortMap.get(emote.toLowerCase());
		
		if (emoteObj == null)
			return;
		
		BaseComponent[] message;
		
		if (directPlayer == null) {
			
			if (emoteObj.getSoloMsg() == null) {
				sender.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', errorOnlyDirect));
				return;
			}
			
			message = getFormattedMessage(emoteObj.getSoloMsg(), false, sender, null);
		} else {
			
			if (emoteObj.getOtherMsg() == null) {
				sender.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', errorOnlySolo));
				return;
			}
			
			message = getFormattedMessage(emoteObj.msgOthers, true, sender, directPlayer);
		}
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			player.spigot().sendMessage(message);
		}
		
		// Play a sound to direct Player to notify about emote
		if (directPlayer != null)
		directPlayer.getPlayer().playSound(directPlayer.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_YES, SoundCategory.VOICE, 1, 1);
	}
		
	/*
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
	}*/
	
	private static BaseComponent[] getFormattedMessage(String unformattedText, boolean isOtherMsg, CustomPlayer sender, CustomPlayer directPlayer) {
		ComponentBuilder messageBuilder = new ComponentBuilder();
		String previousChatColor = "";
		String[] splitMessageString = unformattedText.split(" ");
		
		BaseComponent[] colorFormatted = NickName.translateRGBColorCodes('#', '&', unformattedText);
		
		if (isOtherMsg)
			for (BaseComponent index : colorFormatted) {
				TextComponent beforeFormat = new TextComponent();
				beforeFormat.copyFormatting(index);
				
				if (index.toPlainText().contains("$S"))
					if (sender.getCustomName() == null) messageBuilder.append(beforeFormat).append(sender.getPlayer().getDisplayName());
					else messageBuilder.append(beforeFormat).append(sender.getCustomName());
				else if (index.toPlainText().contains("$N"))
					 messageBuilder.append(beforeFormat).append(sender.getPlayer().getDisplayName());
				else if (index.toPlainText().contains("$R"))
					if (directPlayer.getCustomName() == null)  messageBuilder.append(beforeFormat).append(directPlayer.getPlayer().getDisplayName());
					else  messageBuilder.append(beforeFormat).append(directPlayer.getCustomName());
				else if (index.toPlainText().contains("$D"))
					 messageBuilder.append(beforeFormat).append(directPlayer.getPlayer().getDisplayName()); 
				else
					messageBuilder.append(index);
			}
		else
			for (BaseComponent index : colorFormatted) {
				TextComponent beforeFormat = new TextComponent();
				beforeFormat.copyFormatting(index);
				
				if (index.toPlainText().contains("$S"))
					if (sender.getCustomName() == null) messageBuilder.append(beforeFormat).append(sender.getPlayer().getDisplayName());
					else messageBuilder.append(beforeFormat).append(sender.getCustomName());
				else if (index.toPlainText().contains("$N"))
					 messageBuilder.append(beforeFormat).append(sender.getPlayer().getDisplayName());
				else
					messageBuilder.append(index);
			}
		
		return messageBuilder.create();
	}
	
	public static Emote getByName(String name) {
		return nameSortMap.get(name.toLowerCase());
	}
	
	public static Emote getByCommand(String command) {
		return commandSortMap.get(command.toLowerCase());
	}
	
	public static void setup() 
	{
		
		YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(EmoteConfig.getFile());
		ConfigurationSection section = configYaml.getConfigurationSection("Emotes");
				
		for (String key : section.getKeys(false)) {
			String name = section.getConfigurationSection(key).getString("name");
			
			String command = section.getConfigurationSection(key).getString("command");
			String selfMsg = section.getConfigurationSection(key).getString("self");
			String othersMsg = section.getConfigurationSection(key).getString("others");
			
			new Emote(name,command,selfMsg,othersMsg);
		}
		
		CommandMap map;
		
		try 
		{
			Field commandMapField;
			commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			commandMapField.setAccessible(true);
			map = (CommandMap) commandMapField.get(Bukkit.getServer());
		} 
		catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		for(Entry<String, Emote> e : commandSortMap.entrySet())
		{
			String emotestr = e.getKey();
			Emote emote = e.getValue();
			
			// Register new command
			map.register("emote", new Command(emote.command) 
			{
				public boolean execute(CommandSender commandSender, String command, String[] args) 
				{
					if (!(commandSender instanceof Player))
					{
						commandSender.sendMessage("You need to be a player to use this command.");
						return false;
					}
					
					CustomPlayer senderCustom = CustomPlayer.get(((Player) commandSender).getUniqueId());
					
					if (senderCustom == null) return false;
					
					if (args.length > 0)
					{
						if (emote.msgOthers == null)
						{
							commandSender.sendMessage(emote.msgSolo);
							return false;
						}
						
						CustomPlayer directCustom;
						Player direct = Bukkit.getServer().getPlayer(args[0]);
						if (direct == null) directCustom = null;
						else directCustom = CustomPlayer.get(direct.getUniqueId());
						
						if (directCustom == null) return false;
						
						Emote.send(emotestr, senderCustom, directCustom);
						return true;
					}
					
					if (args.length == 0)
					{
						if (emote.msgSolo == null)
						{
							senderCustom.getPlayer().sendMessage(emote.msgOthers);
							return false;
						}
						
						Emote.send(emotestr, senderCustom, null);
						return true;
					}
					
					return false;
				}
			});
		}
	}
	
	public static void reload()
	{
		nameSortMap.clear();
		commandSortMap.clear();
		
		setup();
	}
	
}
