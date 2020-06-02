package net.wigoftime.open_komodo.chat;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.config.EmoteConfig;
import net.wigoftime.open_komodo.etc.PrintConsole;

public class Emote {
	
	private static final String errorOnlySolo = "&cSorry, but this is a solo only emote.";
	private static final String errorOnlyDirect = "&cSorry, but you have to target others for this emote.";
	
	private final String name;
	private final String command;
	
	private final String msgSolo;
	private final String msgOthers;
	
	private static HashMap<String, Emote> nameSortMap = new HashMap<String, Emote>();
	private static HashMap<String, Emote> commandSortMap = new HashMap<String, Emote>();
	
	public Emote(String name,String command,String msgSolo, String msgOthers) 
	{
		this.name = name;
		this.command = command;
		
		this.msgOthers = msgOthers;
		this.msgSolo = msgSolo;
		
		if (name != null) {
			name = name.toLowerCase();
			nameSortMap.put(name, this);
		}
		
		if (command != null) {
			command = command.toLowerCase();
			commandSortMap.put(command, this);
		}
	}
	
	public String getOtherMsg() 
	{
		return msgOthers;
	}
	
	public String getSoloMsg() 
	{
		return msgSolo;
	}
	
	// Static functions
	
	public static void send(String emote, Player sender, Player directPlayer) 
	{
		String message;
		Emote emoteObj;
		
		if (emote.startsWith("/"))
			emoteObj = commandSortMap.get(emote.toLowerCase());
		else
			emoteObj = nameSortMap.get(emote.toLowerCase());
		
		if (emoteObj == null)
			return;
		
		if (directPlayer == null) 
		{
			
			if (emoteObj.getSoloMsg() == null) 
			{
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', errorOnlyDirect));
				return;
			}
				
			message = emoteObj.getSoloMsg();
			
			message = ChatColor.translateAlternateColorCodes('&', message);
			
			if (sender.getCustomName() != null)
				message = message.replace("$S", sender.getCustomName());
			else
				message = message.replace("$S", sender.getDisplayName());
			
			message = message.replace("$N", sender.getDisplayName());
		} 
		else 
		{
			
			if (emoteObj.getOtherMsg() == null) 
			{
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', errorOnlySolo));
				return;
			}
			
			message = emoteObj.getOtherMsg();
			
			message = ChatColor.translateAlternateColorCodes('&', message);
			
			if (sender.getCustomName() != null)
				message = message.replace("$S", sender.getCustomName());
			else
				message = message.replace("$S", sender.getDisplayName());
			
			message = message.replace("$N", sender.getDisplayName());
			
			if (directPlayer.getCustomName() != null)
				message = message.replace("$R", directPlayer.getCustomName());
			else
				message = message.replace("$R", directPlayer.getDisplayName());
			
			message = message.replace("$D", directPlayer.getDisplayName());
		}
		
		for(Player p : Bukkit.getOnlinePlayers()) 
		{
			p.sendMessage(message);
		}
		
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
				
				@Override
				public boolean execute(CommandSender sender, String command, String[] args) 
				{
					if (!(sender instanceof Player))
					{
						sender.sendMessage("You need to be a player to use this command.");
						return false;
					}
					
					PrintConsole.test("Sending emote.");
					if (args.length > 0)
					{
						if (emote.msgOthers == null)
						{
							sender.sendMessage(emote.msgSolo);
							return false;
						}
						
						Player direct = Bukkit.getServer().getPlayer(args[0]);
						Emote.send(emotestr, (Player) sender, direct);
						return true;
					}
					
					if (args.length == 0)
					{
						if (emote.msgSolo == null)
						{
							sender.sendMessage(emote.msgOthers);
							return false;
						}
						
						Emote.send(emotestr, (Player) sender, null);
						return true;
					}
					
					return false;
				}
			});
		}
	}
	
}
