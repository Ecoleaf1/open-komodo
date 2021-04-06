package net.wigoftime.open_komodo.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.Home;
import net.wigoftime.open_komodo.objects.Pet;

public abstract class PlayerConfig
{
	private static final String folderPath = Main.dataFolderPath+"/Players";
	
	private static boolean isBeingWritten = false;
	
	private static LinkedList<Runnable> quoteList = new LinkedList<Runnable>();
	private synchronized static void writeQuote(Runnable runnable) {
		quoteList.add(runnable);
			
		if (isBeingWritten)
			return;
			
		isBeingWritten = true;
	
		Runnable runAllQuotes = new Runnable() {
			public void run() {
				Iterator<Runnable> iterator = quoteList.iterator();
				
				int count = 0;
				while (iterator.hasNext()) {
					Runnable task = iterator.next();
					task.run();
					
					iterator.remove();
				}
				
				isBeingWritten = false;
			}
		};
		
		Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getPlugin(), runAllQuotes, 1);
	}
	
	public static void setupFolder() {
		File folder = new File(folderPath);
		if (folder.exists())
			return;
		
		folder.mkdir();
	}
	
	private static File getConfigFile(UUID uuid) {
		File file = new File(String.format("%s/%s.yml", folderPath, uuid.toString()));
		
		return file;
	}
	
	public static boolean contains(UUID uuid) {
		if (!getConfigFile(uuid).exists()) return false;
		return true;
	}
	
	public static void createPlayerConfig(UUID uuid) {
		// Try write a new config file
		try {
			InputStream is = Main.class.getClassLoader().getResourceAsStream("default_configs/PlayerConfig.yml");
			
			// Get reader to read the bytes
			InputStreamReader isReader = new InputStreamReader(is);
			// Get reader to read bytes to characters
			BufferedReader reader = new BufferedReader(isReader);
			
			// Get Player config file
			File file = PlayerConfig.getConfigFile(uuid);
			
			if (!file.exists())
				file.createNewFile();
			
			// Create a file writer to be able to write
			FileWriter fileWriter = new FileWriter(file);
			// Write in file writer to buffer
			BufferedWriter buffWriter = new BufferedWriter(fileWriter);
			
			StringBuilder configStringBuilder = new StringBuilder();
			
			// Write next character if there is another character
			while (reader.ready()) {
				configStringBuilder.append(reader.readLine() + "\n");
			}
			
			System.out.println(configStringBuilder.toString());
			
			buffWriter.append(String.format(configStringBuilder.toString(), Instant.now().toString(), Main.defaultPoints, Main.defaultCoins, Instant.ofEpochMilli(0).toString(), Instant.ofEpochMilli(0).toString()));
			buffWriter.close();
			fileWriter.close();
			reader.close();
			isReader.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	// Active Rank functions
	
	public static String getActiveTag(UUID uuid) {
		YamlConfiguration playerYamlConfig;
		playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
		
		ConfigurationSection generalSection;
		generalSection = playerYamlConfig.getConfigurationSection("General");
		
		String activeTag = generalSection.getString("Active Tag");
		return activeTag;
	}
	
	public static void setActiveTag(UUID uuid, String activeTag) {
		
		Runnable task = new Runnable() {
			public void run() {
				YamlConfiguration playerYamlConfig;
				playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
				
				ConfigurationSection generalSection;
				generalSection = playerYamlConfig.getConfigurationSection("General");
				
				generalSection.set("Active Tag", activeTag);
				
				try {
					playerYamlConfig.save(getConfigFile(uuid));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		writeQuote(task);
	}
	
	// Rank ID functions
	
	public static int getRankID(UUID uuid) {
		YamlConfiguration playerYamlConfig;
		playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
		
		ConfigurationSection generalSection;
		generalSection = playerYamlConfig.getConfigurationSection("General");
		
		int rankID = generalSection.getInt("Rank ID");
		return rankID;
	}
	
	public static void setRankID(UUID uuid, int rankID) {
		
		Runnable task = new Runnable() {
			public void run() {
				YamlConfiguration playerYamlConfig;
				playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
				
				ConfigurationSection generalSection;
				generalSection = playerYamlConfig.getConfigurationSection("General");
				
				generalSection.set("Rank ID", rankID);
				
				try {
					playerYamlConfig.save(getConfigFile(uuid));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		writeQuote(task);
		return;
	}
	
	// Tip Functions
	
	public static float getTip(UUID uuid) {
		YamlConfiguration playerYamlConfig;
		playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
		
		ConfigurationSection generalSection;
		generalSection = playerYamlConfig.getConfigurationSection("General");
		
		float tip = (float) generalSection.getDouble("Tip");
		return tip;
	}
	
	public static void setTip(UUID uuid, float tip) {
		
		Runnable task = new Runnable() {
			public void run() {
				YamlConfiguration playerYamlConfig;
				playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
				
				ConfigurationSection generalSection;
				generalSection = playerYamlConfig.getConfigurationSection("General");
				
				generalSection.set("Tip", tip);
				
				try {
					playerYamlConfig.save(getConfigFile(uuid));
				} catch (IOException e) {
					e.printStackTrace();
					
					Player player = Bukkit.getPlayer(uuid);
					if (player == null)
						return;
					
					player.sendMessage(String.format("%sUhh, something went wrong. Try contacting support. Error: IOException#SetTip, Time: %s", ChatColor.DARK_RED, Instant.now().toString()));
				}
			}
		};
		
		writeQuote(task);
	}
	
	// XP Functions
	
	public static double getXP(UUID uuid) {
		YamlConfiguration playerYamlConfig;
		playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
		
		ConfigurationSection generalSection;
		generalSection = playerYamlConfig.getConfigurationSection("General");
		
		double xp = generalSection.getDouble("XP");
		return xp;
	}
	
	public static void setXP(UUID uuid, double xp) {
		
		Runnable task = new Runnable() {
			public void run() {
				YamlConfiguration playerYamlConfig;
				playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
				
				ConfigurationSection generalSection;
				generalSection = playerYamlConfig.getConfigurationSection("General");
				generalSection.set("XP", xp);
				
				try {
					playerYamlConfig.save(getConfigFile(uuid));
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
		};
		
		writeQuote(task);
	}
	
	// Join Date Functions
	
	public static Date getJoinDate(UUID uuid) {
		YamlConfiguration playerYamlConfig;
		playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
		
		ConfigurationSection generalSection;
		generalSection = playerYamlConfig.getConfigurationSection("General");
		
		if (!playerYamlConfig.isConfigurationSection("General"))
		//if (o == null)
			System.out.println("something wrong.");
		
		Date joinDate = (Date) generalSection.get("Date Joined");
		return joinDate;
	}
	
	public static void setJoinDate(UUID uuid, Date date) {
		
		Runnable task = new Runnable() {
			public void run() {
				YamlConfiguration playerYamlConfig;
				playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
				
				ConfigurationSection generalSection;
				generalSection = playerYamlConfig.getConfigurationSection("General");
				
				generalSection.set("Date Joined", date);
				
				try {
					playerYamlConfig.save(getConfigFile(uuid));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		writeQuote(task);
	}
	
	// Currency functions
	
	public static int getCurrency(UUID uuid, Currency currencyType) {
		YamlConfiguration playerYamlConfig;
		playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
		
		ConfigurationSection currencySection;
		currencySection = playerYamlConfig.getConfigurationSection("Currency");
		
		switch (currencyType) {
		case POINTS:
			return currencySection.getInt("Points");
		case COINS:
			return currencySection.getInt("Coins");
		}
		
		return 0;
	}
	
	public static void setCurrency(UUID uuid, Currency currencyType, int amount) {
		
		Runnable task = new Runnable() {
			public void run() {
				File file = getConfigFile(uuid);
				
				YamlConfiguration playerYamlConfig;
				playerYamlConfig = YamlConfiguration.loadConfiguration(file);
				
				ConfigurationSection currencySection;
				currencySection = playerYamlConfig.getConfigurationSection("Currency");
				
				switch (currencyType) {
				case POINTS:
					currencySection.set("Points", amount);
					break;
				case COINS:
					currencySection.set("Coins", amount);
					break;
				}
				
				try {
					playerYamlConfig.save(file);
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		};
		
		writeQuote(task);
	}
	
	// Mute Date functions
	
	public static Date getMuteDate(UUID uuid) {
		YamlConfiguration playerYamlConfig;
		playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
		
		ConfigurationSection moderationSection;
		moderationSection = playerYamlConfig.getConfigurationSection("Moderation");
		
		Date muteDate = (Date) moderationSection.get("Mute Date");
		return muteDate;
	}
	
	public static void setMuteDate(UUID uuid, Date date) {
		Runnable task = new Runnable() {
			public void run() {
				YamlConfiguration playerYamlConfig;
				playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
				
				ConfigurationSection moderationSection;
				moderationSection = playerYamlConfig.getConfigurationSection("Moderation");
				
				moderationSection.set("Mute Date", date);
				
				try {
					playerYamlConfig.save(getConfigFile(uuid));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		writeQuote(task);
	}
	
	// Mute Reason functions
	
	public static String getMuteReason(UUID uuid) {
		YamlConfiguration playerYamlConfig;
		playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
		
		ConfigurationSection moderationSection;
		moderationSection = playerYamlConfig.getConfigurationSection("Moderation");
		
		String muteReason = moderationSection.getString("Mute Reason");
		return muteReason;
	}
	
	public static void setMuteReason(UUID uuid, String muteReason) {
		Runnable task = new Runnable() {
			public void run() {
				YamlConfiguration playerYamlConfig;
				playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
				
				ConfigurationSection generalSection;
				generalSection = playerYamlConfig.getConfigurationSection("Moderation");
				
				generalSection.set("Mute Reason", muteReason);
				
				try {
					playerYamlConfig.save(getConfigFile(uuid));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		writeQuote(task);
	}
	
	// Ban Date functions
	
	public static Date getBanDate(UUID uuid) {
		YamlConfiguration playerYamlConfig;
		playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
		
		ConfigurationSection generalSection;
		generalSection = playerYamlConfig.getConfigurationSection("Moderation");
		
		Date banDate = (Date) generalSection.get("Ban Date");
		return banDate;
	}
	
	public static void setBanDate(UUID uuid, Date date) {
		Runnable task = new Runnable() {
			public void run() {
				YamlConfiguration playerYamlConfig;
				playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
				
				ConfigurationSection generalSection;
				generalSection = playerYamlConfig.getConfigurationSection("Moderation");
				
				generalSection.set("Ban Date", date);
				
				try {
					playerYamlConfig.save(getConfigFile(uuid));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		writeQuote(task);
	}
	
	// Ban Reason functions
	
	public static String getBanReason(UUID uuid) {
		YamlConfiguration playerYamlConfig;
		playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
		
		ConfigurationSection generalSection;
		generalSection = playerYamlConfig.getConfigurationSection("Moderation");
		
		String banReason = generalSection.getString("Ban Reason");
		return banReason;
	}
	
	public static void setBanReason(UUID uuid, String banReason) {
		Runnable task = new Runnable() {
			public void run() {
				YamlConfiguration playerYamlConfig;
				playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
				
				ConfigurationSection moderationSection;
				moderationSection = playerYamlConfig.getConfigurationSection("Moderation");
				
				moderationSection.set("Ban Reason", banReason);
				
				try {
					playerYamlConfig.save(getConfigFile(uuid));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		writeQuote(task);
	}
	
	// Owned item list functions
	
	public static List<CustomItem> getItems(UUID uuid) {
		YamlConfiguration playerYamlConfig;
		playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
		
		ConfigurationSection inventorySection;
		inventorySection = playerYamlConfig.getConfigurationSection("Custom Inventory");
		
		List<Integer> itemIDList = inventorySection.getIntegerList("Items");
		List<CustomItem> itemList = new ArrayList<CustomItem>(itemIDList.size());
		
		for (int id : itemIDList)
			itemList.add(CustomItem.getCustomItem(id));
		
		return itemList;
	}
	
	public static void setItems(UUID uuid, List<CustomItem> items) {
		Runnable task = new Runnable() {
			public void run() {
				File file = getConfigFile(uuid);
				
				List<Integer> idList = new ArrayList<Integer>(items.size());
				for (CustomItem item : items)
					idList.add(item.getID());
				
				YamlConfiguration playerYamlConfig;
				playerYamlConfig = YamlConfiguration.loadConfiguration(file);
				
				ConfigurationSection inventorySection;
				inventorySection = playerYamlConfig.getConfigurationSection("Custom Inventory");
				
				inventorySection.set("Items", idList);
				
				try {
					playerYamlConfig.save(file);
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		};
		
		writeQuote(task);
	}
	
	// List of owned pets functions
	
	public static List<Pet> getPets(UUID uuid) {
		YamlConfiguration playerYamlConfig;
		playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
		
		ConfigurationSection inventorySection;
		inventorySection = playerYamlConfig.getConfigurationSection("Custom Inventory");
		
		List<Integer> petIDList = inventorySection.getIntegerList("Pets");
		List<Pet> petList = new ArrayList<Pet>(petIDList.size());
		
		for (int id : petIDList)
			petList.add(Pet.getPet(id));
		
		return petList;
	}
	
	public static void setPets(UUID uuid, List<Pet> pets) {
		Runnable task = new Runnable() {
			public void run() {
				List<Integer> idList = new ArrayList<Integer>(pets.size());
				for (Pet pet : pets)
					idList.add(pet.getID());
				
				YamlConfiguration playerYamlConfig;
				playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
				
				ConfigurationSection inventorySection;
				inventorySection = playerYamlConfig.getConfigurationSection("Custom Inventory");
				
				inventorySection.set("Pets", idList);
				
				try {
					playerYamlConfig.save(getConfigFile(uuid));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		writeQuote(task);
	}
	
	// List of homes functions
	
	public static List<Home> getHomes(UUID uuid) {
		YamlConfiguration playerYamlConfig;
		playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
		
		ConfigurationSection homeSection = playerYamlConfig.getConfigurationSection("Homes");
		
		LinkedList<Home> homeLinkedList = new LinkedList<Home>();
		for (String homeName : homeSection.getKeys(false))
			homeLinkedList.add(new Home(homeName, 
					homeSection.
					getConfigurationSection(homeName)
					.getLocation("Location")));
		
		ArrayList<Home> homeList = new ArrayList<Home>(homeLinkedList.size());
		homeList.addAll(homeLinkedList);
		
		return homeList;
	}
	
	public static void setHomes(UUID uuid, List<Home> homes) {
		Runnable task = new Runnable() {
			public void run() {
				YamlConfiguration playerYamlConfig;
				playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
				
				ConfigurationSection homesSection;
				homesSection = playerYamlConfig.getConfigurationSection("Homes");
				
				for (String homeName : homesSection.getKeys(false)) {
					if (!homesSection.isConfigurationSection(homeName))
						continue;
					
					homesSection.set(homeName, null);
				}
				
				for (Home home : homes) {
					homesSection.createSection(home.name);
					
					ConfigurationSection homeSection = homesSection.getConfigurationSection(home.name);
					homeSection.set("Location", home.location);
				}
				
				try {
					playerYamlConfig.save(getConfigFile(uuid));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		writeQuote(task);
	}
	
	// Permission functions
	
	public static List<Permission> getGlobalPermissions(UUID uuid) {
		YamlConfiguration playerYamlConfig;
		playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
		
		List<String> permissionNodes = playerYamlConfig.getStringList("Global Permissions");
		
		List<Permission> permissions = new ArrayList<Permission>(permissionNodes.size());
		for (String permissionNode : permissionNodes)
			permissions.add(new Permission(permissionNode));
		
		return permissions;
	}
	
	public static void setGlobalPermissions(UUID uuid, List<Permission> permissions) {
		final List<String> permissionNodes = new ArrayList<String>(permissions.size());
		for (Permission permission : permissions)
			permissionNodes.add(permission.getName());
		
		Runnable task = new Runnable() {
			public void run() {
				File file = getConfigFile(uuid);
				YamlConfiguration playerYamlConfig;
				playerYamlConfig = YamlConfiguration.loadConfiguration(file);
				
				playerYamlConfig.set("Global Permissions", permissionNodes);
				
				try {
					playerYamlConfig.save(file);
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		};
		
		writeQuote(task);
	}
	
	public static List<Permission> getWorldPermissions(UUID uuid, String worldName) {
		YamlConfiguration playerYamlConfig;
		playerYamlConfig = YamlConfiguration.loadConfiguration(getConfigFile(uuid));
		
		ConfigurationSection worldsSection;
		worldsSection = playerYamlConfig.getConfigurationSection("Worlds");
		
		if (!worldsSection.contains(worldName))
			createWorld(playerYamlConfig, uuid, worldName);
		
		ConfigurationSection worldSection = worldsSection.getConfigurationSection(worldName);
		
		List<String> permissionNodes = worldSection.getStringList("World Permissions");
		
		List<Permission> permissions = new ArrayList<Permission>(permissionNodes.size());
		for (String permissionNode : permissionNodes)
			permissions.add(new Permission(permissionNode));
		
		return permissions;
	}
	
	private static void createWorld(YamlConfiguration config, UUID uuid, String worldName) {
		ConfigurationSection worldsSection;
		worldsSection = config.getConfigurationSection("Worlds");
		
		ConfigurationSection worldSection = worldsSection.createSection(worldName);
		
		worldSection.set("World Permissions", new ArrayList<String>());
	}
	
	public static void setWorldPermissions(UUID uuid, String worldName, List<Permission> permissions) {
		final List<String> permissionNodes = new ArrayList<String>(permissions.size());
		for (Permission permission : permissions)
			permissionNodes.add(permission.getName());
		
		Runnable task = new Runnable() {
			public void run() {
				File file = getConfigFile(uuid);
				
				YamlConfiguration playerYamlConfig;
				playerYamlConfig = YamlConfiguration.loadConfiguration(file);
				
				ConfigurationSection worldsSection;
				worldsSection = playerYamlConfig.getConfigurationSection("Worlds");
				
				if (!worldsSection.contains(worldName))
					createWorld(playerYamlConfig, uuid, worldName);
				
				ConfigurationSection worldSection = worldsSection.getConfigurationSection(worldName);
				
				worldSection.set("World Permissions", permissionNodes);
				
				try {
					playerYamlConfig.save(file);
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		};
		
		writeQuote(task);
	}
}