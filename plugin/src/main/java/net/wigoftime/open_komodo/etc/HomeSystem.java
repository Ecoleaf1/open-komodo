package net.wigoftime.open_komodo.etc;

import java.io.File;

import org.bukkit.ChatColor;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.Home;

abstract public class HomeSystem {
	private static final File playerConfigFolder = new File(Main.dataFolderPath+"/Players/");
	
	public static final String noHomes = ChatColor.translateAlternateColorCodes('&', "&4You have no homes!");
	public static final String invaildHouse = ChatColor.translateAlternateColorCodes('&', "&cYou don't have a house by that name!");
	private static final String aboveLimit = ChatColor.translateAlternateColorCodes('&', "&cSorry, but you are above your home limit!");
	private static final String homeCreated = ChatColor.translateAlternateColorCodes('&', "&b&lHome Created!");
	
	private static final String homeDeleted = ChatColor.translateAlternateColorCodes('&', "&cHome deleted.");
	
	public static void createHome(CustomPlayer playerCustomPlayer, String name) {
		Home home = new Home(name, playerCustomPlayer.getPlayer().getLocation());
		
		playerCustomPlayer.addHome(home);
	}
	
	/*
	public static void createHouse(Player player, String name) {
		File playerConfig = new File(playerConfigFolder.getPath()+"/"+player.getUniqueId()+".yml");
		YamlConfiguration playerYaml = YamlConfiguration.loadConfiguration(playerConfig);
		ConfigurationSection section = playerYaml.getConfigurationSection("Homes");
		
		int limit = playerYaml.getInt("Home Limit");
		
		int count = 0;
		for (String s : section.getKeys(false)) {
			
			if (s.equalsIgnoreCase(name))
				return;
			
			count++;
		}
		
		if (count >= limit) {
			player.sendMessage(aboveLimit);
			return;
		}
		
		ConfigurationSection newHomeSection = section.createSection(name);
		newHomeSection.set("Location", player.getLocation());
		
		try {
			playerYaml.save(playerConfig);
		} catch (IOException e) {
			player.sendMessage("ERROR: Something went wrong with saving your new home, contact an admin or the owner about this issue.");
			PrintConsole.print("ERROR: Something went wrong with saving new home of" + player.getDisplayName()+" ("+player.getUniqueId()+")");
			return;
		}
		
		player.sendMessage(homeCreated);
	}
	
	public static void teleportHome(Player player, String home) {
		File playerConfig = new File(playerConfigFolder.getPath()+"/"+player.getUniqueId()+".yml");
		YamlConfiguration playerYaml = YamlConfiguration.loadConfiguration(playerConfig);
		ConfigurationSection section = playerYaml.getConfigurationSection("Homes");
		
		for (String s : section.getKeys(false)) {
			if (section.getConfigurationSection(s).getName().equalsIgnoreCase(home)) {
				ConfigurationSection homeSection = section.getConfigurationSection(s);
				
				Location location = (Location) homeSection.get("Location");
				player.teleport(location);
				
				return;
			}
		}
		
		player.sendMessage(invaildHouse);
		return;
		
	}
	
	public static void deleteHome(Player player, String home) {
		File playerConfig = new File(playerConfigFolder.getPath()+"/"+player.getUniqueId()+".yml");
		YamlConfiguration playerYaml = YamlConfiguration.loadConfiguration(playerConfig);
		ConfigurationSection section = playerYaml.getConfigurationSection("Homes");
		
		for (String s : section.getKeys(false)) {
			if (section.getConfigurationSection(s).getName().equalsIgnoreCase(home)) {
				
				section.set(s, null);
				
				try {
					playerYaml.save(playerConfig);
					player.sendMessage(homeDeleted);
				} catch (IOException e) {
					player.sendMessage("ERROR: Something went wrong with deleting your home, contact an admin or the owner about this issue.");
					PrintConsole.print("ERROR: Something went wrong with deleting a home of" + player.getDisplayName()+" ("+player.getUniqueId()+")");
					return;
				}
				
				return;
			}
		}
	}
	
	public static ArrayList<String> getHomes(Player player) {
		File playerConfig = new File(playerConfigFolder.getPath()+"/"+player.getUniqueId()+".yml");
		YamlConfiguration playerYaml = YamlConfiguration.loadConfiguration(playerConfig);
		ConfigurationSection section = playerYaml.getConfigurationSection("Homes");
		
		ArrayList<String> homes = new ArrayList<String>();
		
		for (String s : section.getKeys(false)) {
			homes.add(s);
		}
		
		return homes;
	}*/
	
}
