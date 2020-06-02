package net.wigoftime.open_komodo.config;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.FriendProfile;
import net.wigoftime.open_komodo.objects.ItemType;
import net.wigoftime.open_komodo.objects.Pet;
import net.wigoftime.open_komodo.objects.Rank;

public abstract class PlayerConfig 
{
	private static final File playerConfigFolder = new File(Main.dataFolderPath+"/Players/");
	
	// Use the Australia/Sydney TimeZone
	private static final ZoneId zoneID = Main.zoneID;
	// For example when a first player joins, create the config
	public static void create(OfflinePlayer player) 
	{
		File f = new File(playerConfigFolder.getPath()+"/"+player.getUniqueId()+".yml");
		
		// If File Exists, don't run anymore
		if (f.exists())
			return;
		
		// Try create config
		try 
		{
			f.createNewFile();
		} 
		catch (IOException e) 
		{
			PrintConsole.print("Error: Couldn't create " + f.getName());
			return;
		}
		
			// Set all the Player Config Variables
		{
			YamlConfiguration yamlConfig = new YamlConfiguration();

			// Variables to the Player Config
			yamlConfig.createSection("General");
			{
			ConfigurationSection section = yamlConfig.getConfigurationSection("General");
				section.set("Name", player.getName());
				section.set("Rank", "Default");
				section.set("Tag", "");
				section.set("XP", 0.0);
				
				ZonedDateTime date = ZonedDateTime.now(zoneID);
				
				// Convert to the U.S Date Format
				String dateFormat = String.format("%02d/%02d/%02d", date.getMonthValue(), date.getDayOfMonth(), date.getYear());
				
				if (player.isOnline())
					section.set("DateJoined",dateFormat);
				else
					section.set("DateJoined","");
			}
			
			yamlConfig.createSection("Currency");
			{
				ConfigurationSection section = yamlConfig.getConfigurationSection("Currency");
				
				section.set("Points", 720);
				section.set("Coins", 0);
			}
			
			yamlConfig.createSection("Moderation"); {
				ConfigurationSection section = yamlConfig.getConfigurationSection("Moderation");
				
				section.set("Mute", "");
				section.set("Mute Reason", "");
				section.set("Ban", "");
				section.set("Ban Reason", "");
			}

			ConfigurationSection section = yamlConfig.createSection("Items");
			section.set("Hats", new LinkedList<Integer>());
			section.set("Props", new LinkedList<Integer>());
			
			yamlConfig.set("Home Limit", 1);
			yamlConfig.createSection("Homes");
			
			ConfigurationSection wSection = yamlConfig.createSection("Worlds");
			for (World w : Bukkit.getWorlds())
			{
				String name = w.getName();
				
				ConfigurationSection worldSection = wSection.createSection(name);
				
				// create list for permissions only in that world.
				worldSection.set("Permissions", new LinkedList<String>());
			}
			
			// Adding the default permissions and setting up the list.
			String[] permissionArray = new String[] {""};
			yamlConfig.set("Permissions", permissionArray);
			
			// Try Save
			try 
			{
				yamlConfig.save(f);
			}
			catch (IOException e) 
			{
				PrintConsole.print("Error: Couldn't save "+player.getUniqueId()+".yml");
			}
		}
		
	}
	
	public static boolean firstJoined(Player player)
	{
		File file = PlayerConfig.getPlayerConfig(player);
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		if (config.contains("General"))
			if (config.getConfigurationSection("General").contains("DateJoined"))
				return false;
			else
				return true;
		return true;
	}
	
	// Create the player config file.
	public static void createFolder() 
	{
		
		if (playerConfigFolder.exists())
			return;
		
		playerConfigFolder.mkdir();
		
	}
	
	// Check if player has permission, because of player not rank.
	public static boolean checkPermission(Player player, String permission) 
	{
		YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(new File(playerConfigFolder.getPath()+"/"+player.getUniqueId()+".yml"));
		
		if (configYaml.getConfigurationSection("Permissions") == null)
			return false;
		
		for (String s : configYaml.getStringList("Permissions")) 
		{
			if (s.equalsIgnoreCase(permission))
				return true;
		}
		
		// If went through loop and didn't have it, return false
		return false;
		
	}
	
	public static List<Permission> getPermission(Player player, World world)
	{
		// Get Player's Configuration File
		String path;
		path = String.format("%s/%s.yml", playerConfigFolder.getAbsolutePath(), player.getUniqueId());
		File file = new File(path);
		
		// Load Configuration
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		// Go to section "Worlds"
		ConfigurationSection s1 = config.getConfigurationSection("Worlds");
		
		
		// Under Worlds, go into the world's section
		ConfigurationSection s2 = s1.getConfigurationSection(world.getName().toString());
		
		// If world doesn't exist in config (New world)
		if (s2 == null)
		{
			// Create world propteries in config
			createWorld(player, world);
			
			// Load that create world again
			List<Permission> permissions = getPermission(player,world);
			return permissions;
		}
		
		// Get List of permissions.
		List<Permission> permissions = new LinkedList<Permission>();
		
		for (String s : s2.getStringList("Permissions"))
			permissions.add(new Permission(s));
			
		// Return the permissions
		return permissions;
	}
	
	private static void createWorld(Player player, World world)
	{
		// Get and open config
		File f = new File(playerConfigFolder.getPath()+"/"+player.getUniqueId()+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
		
		// Go to section "Worlds"
		ConfigurationSection s1 = config.getConfigurationSection("Worlds");
		
		ConfigurationSection worldSection = s1.createSection(world.getName());
		
		// create list for permissions only in that world.
		worldSection.set("Permissions", new LinkedList<String>());
	
		try
		{
			config.save(f);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static File getConfigFolder() 
	{
		return playerConfigFolder;
	}
	
	public static boolean addItem(Player player, CustomItem cs)
	{
		// Get Player's Configuration File
		String path;
		path = String.format("%s/%s.yml", playerConfigFolder.getAbsolutePath(), player.getUniqueId());
		File file = new File(path);
		
		// Open player's Configuration  file
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		// Go into "Items"
		ConfigurationSection section;
		section = config.getConfigurationSection("Items");
		
		// Create a list that holds IDs
		List<Integer> list;
		
		// Grab the type of the item that's going to be added
		ItemType type = cs.getType();
		
		// Select what list in the configuration
		if (type == ItemType.TAG)
			list = section.getIntegerList("Tags");
		else if (type == ItemType.HAT)
			list = section.getIntegerList("Hats");
		else
			return false;
		
		// Get info about item
		ItemStack is = cs.getNBTItem().getItem();
		ItemMeta im = is.getItemMeta();
		
		// Get the ID of the item
		int id = im.getCustomModelData();
		
		// Add it to the list.
		list.add(id);
		
		// Save the list in the configuration
		if (type == ItemType.TAG)
			section.set("Tags", list);
		else if (type == ItemType.HAT)
			section.set("Hats", list);
		else
			return false;
		
		// Try save the player's configuration file.
		try 
		{
			config.save(file);
			return true;
		}
		catch (IOException e)
		{
			String msg;
			msg = String.format("ERROR: Couldn't save item #%d in %d's config", id, player.getDisplayName());
			
			return false;
		}
	}
	
	public static List<CustomItem> getItem(Player player, ItemType type)
	{
		// Get Player's Configuration File
		String path;
		path = String.format("%s/%s.yml", playerConfigFolder.getAbsolutePath(), player.getUniqueId());
		File file = new File(path);
		
		// Open player's Configuration  file
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		// Go into "Items"
		ConfigurationSection section;
		section = config.getConfigurationSection("Items");
		
		// Create a list that holds IDs
		List<Integer> list;
		
		// Select what list in the configuration
		if (type == ItemType.TAG)
			list = section.getIntegerList("Tags");
		else if (type == ItemType.HAT)
			list = section.getIntegerList("Hats");
		else
			return null;
		
		// Create another lists to put CustomItems
		List<CustomItem> list2 = new ArrayList<CustomItem>(list.size());
		
		// Loop through every ID that player owns
		for (int id : list) 
		{
			// Get CustomItem from ID
			CustomItem cs = CustomItem.getCustomItem(id);
			
			// If CustomItem doesn't exist, go to next ID
			if (cs == null)
				 continue;
			
			// Add CustomItem to the CustomItem list.
			list2.add(cs);
		}
		
		return list2;
	}
	
	public static void changeCurrentTag(Player player, String display) 
	{
		File playerFile = getPlayerConfig(player);
		YamlConfiguration playerYaml = YamlConfiguration.loadConfiguration(playerFile);
		
		playerYaml.getConfigurationSection("General").set("Tag", display);
		
		try 
		{
			playerYaml.save(playerFile);
		} catch (IOException e) 
		{
			PrintConsole.print("ERROR: Couldn't switch "+player.getUniqueId()+"'s tag: Couldn't save.");
		}
	}
	
	public static String getRank(Player player) 
	{
		File config = getPlayerConfig(player);
		YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(config);
		
		return configYaml.getConfigurationSection("General").getString("Rank");
	}
	
	public static int getHomeLimit(UUID uuid) 
	{
		File config = getPlayerConfig(uuid);
		YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(config);
		
		return configYaml.getInt("Home Limit");
	}
	
	public static void setHomeLimit(UUID uuid, int amount) 
	{
		File config = getPlayerConfig(uuid);
		YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(config);
		
		configYaml.set("Home Limit", amount);
		
		// Try save
		try
		{
			configYaml.save(config);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void setRank(Player player, String rank) 
	{
		// Get config file and load config
		File file = getPlayerConfig(player);
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		// Set player's Rank
		config.getConfigurationSection("General").set("Rank", rank);
		
		// Try save changes
		try
		{
			config.save(file);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static List<String> getPermissions(OfflinePlayer target) 
	{
		File config = getPlayerConfig(target);
		YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(config);
		
		return configYaml.getStringList("Permissions");
	}
	
	public static void setPermission(Player player, Permission permission, boolean isAdding)
	{
		// Get and open Player's Configuration
		File file = getPlayerConfig(player);
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		// Get list of permissions
		List<String> list = config.getStringList("Permissions");
		
		// If adding, add to list, if removing, remove to list
		if (isAdding)
			list.add(permission.getName());
		else
			list.remove(permission.getName());
		
		// Overwrite with updated permission list
		config.set("Permissions", list);
		
		// Try save configuration
		try
		{
			config.save(file);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static String getRankPrefix(Player player) 
	{
		YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(getPlayerConfig(player));
		
		String userRankStr = configYaml.getConfigurationSection("General").getString("Rank");
		Rank rank = Rank.getRank(userRankStr);
		
		if (rank == null)
			return "";
		
		String rankPrefix = rank.getPrefix();
		rankPrefix = ChatColor.translateAlternateColorCodes('&', rankPrefix);
		
		return rankPrefix;
	}
	
	public static List<Player> getOnlinePlayers(Player player) 
	{
		List<Player> onlineFriends = new ArrayList<Player>();
		YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(getPlayerConfig(player));
		
		List<String> uuidList = configYaml.getStringList("Friends");
		
		for (String s : uuidList) 
		{
			UUID uuid = UUID.fromString(s);
			Player friend = Bukkit.getPlayer(uuid);
			
			// If friend is online
			if (friend != null) {
				onlineFriends.add(friend);
			}
				
		}
		
		return onlineFriends;
	}
	
	public static int getCurrency(Player player, Currency currency)
	{
		File f = getPlayerConfig(player);
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
		ConfigurationSection section = config.getConfigurationSection("Currency");
		
		if (currency == Currency.POINTS)
			return section.getInt("Points");
		
		if (currency == Currency.COINS)
			return section.getInt("Coins");
		
		return 0;
	}
	
	public static void setCurrency(Player player, int amount, Currency currency)
	{
		// Get Player Configuration
		File f = getPlayerConfig(player);
		
		// Load the Player Config and read the Currency Section
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
		ConfigurationSection section = config.getConfigurationSection("Currency");
		
		if (currency == Currency.POINTS)
			section.set("Points", amount);
		
		if (currency == Currency.COINS)
			section.set("Coins", amount);
		
		// Try Save
		try 
		{
			config.save(f);
		}
		catch (IOException e)
		{
			PrintConsole.print(String.format("Couldn't set %s's amount (%d) using %d. Here's the StackTraces:", player.getDisplayName(),amount, currency.toString()));
			e.printStackTrace();
		}
		return;
	}
	
	public static Instant getBanInstant(UUID uuid)
	{
		File file = PlayerConfig.getPlayerConfig(uuid);
		
		if (file == null)
			return null;
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		ConfigurationSection section = config.getConfigurationSection("Moderation");
		String dateStr = section.getString("Ban");
		
		// If no ban date, return null
		if (dateStr.equals(""))
			return null;
		
		Instant instant = Instant.parse(dateStr);
		
		return instant;
	}
	
	public static void setJoinDate(Player player)
	{
		// Get Date
		ZonedDateTime date = ZonedDateTime.now(zoneID);
		
		// Convert to the U.S Date Format
		String dateFormat = String.format("%02d/%02d/%02d", date.getMonthValue(), date.getDayOfMonth(), date.getYear());
		
		// Get config file
		File file = PlayerConfig.getPlayerConfig(player.getUniqueId());
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		ConfigurationSection section = config.getConfigurationSection("General");
		section.set("JoinDate", dateFormat);
	}
	
	public static void setBanDate(UUID uuid, Instant instant, String reason)
	{
		// Get config file
		File file = PlayerConfig.getPlayerConfig(uuid);
		
		// If file is null, create one
		if (file == null)
		{
			OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
			
			if (player == null)
				return;
			
			PlayerConfig.create(player);
			file = PlayerConfig.getPlayerConfig(uuid);
		}
		
		// Load config
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		// Get section
		ConfigurationSection section = config.getConfigurationSection("Moderation");
		
		// Set ban date
		if (instant == null)
			section.set("Ban", "");
		else
			section.set("Ban", instant.toString());
		
		// Set Ban Reason
		if (reason == null)
			section.set("Ban Reason", "");
		else
			section.set("Ban Reason", reason);
		
		// Try save changes
		try
		{
			config.save(file);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return;
	}
	
	public static String getBanReason(UUID uuid)
	{
		// Get config file
		File file = PlayerConfig.getPlayerConfig(uuid);
		
		// Load config
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		// Get section
		ConfigurationSection section = config.getConfigurationSection("Moderation");
		
		// Get Ban Reason
		String reason = section.getString("Ban Reason");
		
		// If no reason, return null
		if (reason.equals(""))
			return null;
		
		return reason;
	}
	
	public static void addPet(Player player, Pet pet)
	{
		File f = new File(playerConfigFolder.getPath()+"/"+player.getUniqueId()+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
		
		List<Integer> s1 = config.getIntegerList("Pets");
		s1.add(pet.getID());
		
		// Set list back to save
		config.set("Pets", s1);
		
		try
		{
			config.save(f);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static List<Pet> getPets(Player player)
	{
		File f = new File(playerConfigFolder.getPath()+"/"+player.getUniqueId()+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
		
		List<Integer> ids = config.getIntegerList("Pets");
		List<Pet> foundList = new LinkedList<Pet>();
		for (int i : ids)
		{
			Pet pet = Pet.getPet(i);
			
			if (pet == null)
				continue;
			
			foundList.add(pet);
		}
		
		return foundList;
	}
	
	public static boolean containsPet(Player player, Pet pet)
	{
		File f = new File(playerConfigFolder.getPath()+"/"+player.getUniqueId()+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
		
		List<Integer> ids = config.getIntegerList("Pets");
		for (int i : ids)
		{
			Pet petLoop = Pet.getPet(i);
			
			if (petLoop == pet)
				return true;
		}
		
		return false;
	}
	
	public static File getPlayerConfig(Player player) 
	{
		File f = new File(playerConfigFolder.getPath()+"/"+player.getUniqueId()+".yml");
		if (f.exists())
			return f;
		else
			return null;
	}
	
	public static File getPlayerConfig(UUID uuid) 
	{
		File f = new File(playerConfigFolder.getPath()+"/"+uuid.toString()+".yml");
		if (f.exists())
			return f;
		else
			return null;
	}
	
	public static File getPlayerConfig(OfflinePlayer player) 
	{
		File f = new File(playerConfigFolder.getPath()+"/"+player.getUniqueId()+".yml");
		if (f.exists())
			return f;
		else
			return null;
	}
	
	public static List<FriendProfile> getFriends(Player player) 
	{
		YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(getPlayerConfig(player));
		List<FriendProfile> friends = new ArrayList<FriendProfile>();
		
		for (String s : configYaml.getStringList("Friends")) {
			UUID uuid = UUID.fromString(s);
			
			// If player is online
			if (Bukkit.getPlayer(uuid) != null) {
				Player friend = Bukkit.getPlayer(uuid);
				
				String friendName = friend.getName();
				boolean isOnline = true;
				
				FriendProfile friendProfile = new FriendProfile(friendName, uuid, isOnline);
				friends.add(friendProfile);
				
				continue;
			}
			
			// If player is offline
			if (Bukkit.getOfflinePlayer(uuid) != null) 
			{
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
				
				String friendName = offlinePlayer.getName();
				boolean isOnline = false;
				
				FriendProfile friendProfile = new FriendProfile(friendName, uuid, isOnline);
				friends.add(friendProfile);
				
				continue;
			}
		}
		
		return friends;
	}
	
	public static double getXP(Player player)
	{
		File f = getPlayerConfig(player);
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
		
		ConfigurationSection section = config.getConfigurationSection("General");
		
		return section.getDouble("XP");
	}
	
	public static void setXP(Player player, double xp)
	{
		// Get config and go into config
		File f = getPlayerConfig(player);
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
		
		// Get section "General" in config file
		ConfigurationSection section = config.getConfigurationSection("General");
		
		// Set XP
		section.set("XP", xp);
		
		// Try save the config
		try
		{
			config.save(f);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static String getMuteReason(Player player)
	{
		File file = PlayerConfig.getPlayerConfig(player.getUniqueId());
		YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(file);
		
		String reason = yamlConfig.getConfigurationSection("Moderation").getString("Mute Reason");
		
		return reason;
	}
	
	public static void setMuteReason(Player player, String reason)
	{
		// Get config file and load config file
		File file = PlayerConfig.getPlayerConfig(player.getUniqueId());
		YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(file);
		
		// Set mute reason
		yamlConfig.getConfigurationSection("Moderation").set("Mute Reason", reason);
		
		// Try save
		try
		{
			yamlConfig.save(file);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return;
	}
	
	public static Date getMuteDate(Player player)
	{
		// Get file and load config file
		File file = PlayerConfig.getPlayerConfig(player.getUniqueId());
		YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(file);
		
		// Get the mute date in string
		String dateString = yamlConfig.getConfigurationSection("Moderation").getString("Mute");
		
		// If dateString doesn't exist
		if (dateString.equals(""))
			return null;
		
		// Get instant date from mute date
		Instant instant = Instant.parse(dateString);
		
		// Create date from Instant
		Date date = new Date(instant.toEpochMilli());
		
		// Return the date
		return date;
	}
	
	public static void setMuteDate(Player player, Instant instant)
	{
		// Get file and load config file
		File file = PlayerConfig.getPlayerConfig(player.getUniqueId());
		YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(file);
		
		// Set the mute date
		yamlConfig.getConfigurationSection("Moderation").set("Mute", instant.toString());
		
		// Try save changes
		try
		{
			yamlConfig.save(file);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		
		return;
	}
	
}
