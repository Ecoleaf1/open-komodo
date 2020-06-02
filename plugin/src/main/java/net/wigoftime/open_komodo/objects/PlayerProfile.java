package net.wigoftime.open_komodo.objects;

import java.io.File;
import java.util.HashMap;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.config.PlayerConfig;

public class PlayerProfile {

	private static final String playerConfigFolderURL = Main.dataFolderPath+"/Players/";
	
	private Player player;
	private String rank;
	
	// Hashtags to link them with the Player Profile
	private static HashMap<Player, PlayerProfile> hashMap = new HashMap<Player, PlayerProfile>();
	
	public PlayerProfile (Player player, byte rank) {
		
		{
		
			File config = new File(playerConfigFolderURL+player.getUniqueId()+".yml");
			
			if (!config.exists())
				PlayerConfig.create(player);
		
			YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(config);
		
			this.rank = configYaml.getString("Rank");
			
		}
		
		this.player = player;
		
		// Add it to the list
		hashMap.put(player, this);
	}
	
	public String getRank() {
		return rank;
	}
	
	public Player getPlayer() {
		return player;
	}
	
//	The functions that is about the list
	
	public static PlayerProfile getPlayerProfile(Player player) {
		if (hashMap.containsKey(player))
			return hashMap.get(player);
		else
			return null;
	}
	
	// use this instead of doing: new PlayerProfile. Instead use: PlayerProfile.createProfile(uuid);
	public static void createProfile(Player player) {
		
		if (hashMap.containsKey(player))
			return;
		
		new PlayerProfile(player, (byte) 0);
		
	}
	
	public static void deleteProfile(Player player) {

		if (hashMap.containsKey(player)) {
			hashMap.remove(player);
		}
		
	}
	
	public static int amountOfProfiles() {
		return hashMap.size();
	}
	
}
