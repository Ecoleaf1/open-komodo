package net.wigoftime.open_komodo.config;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.objects.SQLInfo;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

abstract public class Config 
{
	private static final String dataFolderPath = Main.dataFolderPath;
	private static final File configFile = new File(dataFolderPath+"/config.yml");
	
	public static @Nullable String getMessageFormat() {
		File configFile = getConfigFile();
		
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
		ConfigurationSection normalMessageSection = yamlConfiguration.getConfigurationSection("Normal Message");
		
		return normalMessageSection.getString("Format");
	}
	
	public static @Nullable String getTagMessageFormat() {
		File configFile = getConfigFile();
		
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
		ConfigurationSection normalMessageSection = yamlConfiguration.getConfigurationSection("Normal Message");
		
		return normalMessageSection.getString("Tag Format");
	}
	
	public static int getChatDistance() {
		File configFile = getConfigFile();
		
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
		ConfigurationSection normalMessageSection = yamlConfiguration.getConfigurationSection("Normal Message");
		
		return normalMessageSection.getInt("Chat Distance");
	}
	
	public static @Nullable String getPrivateSentMessageFormat() {
		File configFile = getConfigFile();
		
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
		ConfigurationSection privateMessageSection = yamlConfiguration.getConfigurationSection("Private Message");
		
		return privateMessageSection.getString("Format (Sent)");
	}
	
	public static @Nullable String getPrivateReceivedMessageFormat() {
		File configFile = getConfigFile();
		
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
		ConfigurationSection privateMessageSection = yamlConfiguration.getConfigurationSection("Private Message");
		
		return privateMessageSection.getString("Format (Received)");
	}
	
	public static @Nullable String getResourcePackURL() {
		File configFile = getConfigFile();
		
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
		ConfigurationSection globalSection = yamlConfiguration.getConfigurationSection("Global Settings");
		
		return globalSection.getString("Resource Pack");
	}
	
	public static @Nullable Location getSpawnLocation() {
		File configFile = getConfigFile();
		
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
		ConfigurationSection normalMessageSection = yamlConfiguration.getConfigurationSection("Global Settings");
		
		return (Location) normalMessageSection.get("Spawn Location");
	}
	
	public static @Nullable Location getTutorialLocation() {
		File configFile = getConfigFile();
		
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
		ConfigurationSection normalMessageSection = yamlConfiguration.getConfigurationSection("Global Settings");
		
		return (Location) normalMessageSection.get("Tutorial Location");
	}
	
	public static boolean isPropShopEnabled() {
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
		ConfigurationSection normalMessageSection = yamlConfiguration.getConfigurationSection("Global Settings");
		
		return normalMessageSection.getBoolean("Enable Propshop");
	}
	
	public static boolean isPetShopEnabled() {
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
		ConfigurationSection normalMessageSection = yamlConfiguration.getConfigurationSection("Global Settings");
		
		return normalMessageSection.getBoolean("Enable Propshop");
	}
	
	public static boolean isHatshopEnabled() {
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
		ConfigurationSection normalMessageSection = yamlConfiguration.getConfigurationSection("Global Settings");
		
		return normalMessageSection.getBoolean("Enable Hatshop");
	}
	
	public static boolean isTagshopEnabled() {
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
		ConfigurationSection normalMessageSection = yamlConfiguration.getConfigurationSection("Global Settings");
		
		return normalMessageSection.getBoolean("Enable Tagshop");
	}
	
	public static boolean isBinEnabled() {
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
		ConfigurationSection normalMessageSection = yamlConfiguration.getConfigurationSection("Global Settings");
		
		return normalMessageSection.getBoolean("Enable Bin");
	}
	
	public static boolean isWarpsEnabled() {
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
		ConfigurationSection normalMessageSection = yamlConfiguration.getConfigurationSection("Global Settings");
		
		return normalMessageSection.getBoolean("Enable Warps");
	}
	
	public static @Nullable Vector getBorderPosition1() {
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
		ConfigurationSection normalMessageSection = yamlConfiguration.getConfigurationSection("Global Settings");
		
		return normalMessageSection.getVector("Border Position 1");
	}
	
	public static @Nullable Vector getBorderPosition2() {
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
		ConfigurationSection normalMessageSection = yamlConfiguration.getConfigurationSection("Global Settings");
		
		return normalMessageSection.getVector("Border Position 2");
	}
	
	public static @NotNull String getWebsiteDescription() {
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
		ConfigurationSection normalMessageSection = yamlConfiguration.getConfigurationSection("Global Settings");
		
		return ChatColor.translateAlternateColorCodes('&', normalMessageSection.getString("Website"));
	}
	
	public static @NotNull String getDiscordDescription() {
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
		ConfigurationSection normalMessageSection = yamlConfiguration.getConfigurationSection("Global Settings");
		
		return ChatColor.translateAlternateColorCodes('&', normalMessageSection.getString("Discord"));
	}

	public static @NotNull String getVotingDescription() {
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
		ConfigurationSection normalMessageSection = yamlConfiguration.getConfigurationSection("Global Settings");

		return ChatColor.translateAlternateColorCodes('&', normalMessageSection.getString("Voting"));
	}

	public static @NotNull String getStoreDescription() {
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
		ConfigurationSection normalMessageSection = yamlConfiguration.getConfigurationSection("Global Settings");

		return ChatColor.translateAlternateColorCodes('&', normalMessageSection.getString("Store"));
	}

	public static @NotNull List<String> getRules() {
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
		ConfigurationSection normalMessageSection = yamlConfiguration.getConfigurationSection("Global Settings");

		List<String> unformattedRules = normalMessageSection.getStringList("Rules");
		List<String> formattedRules = new ArrayList<String>(unformattedRules.size());

		for (String ruleIndex : unformattedRules)
			formattedRules.add(ChatColor.translateAlternateColorCodes('&', ruleIndex));

		return formattedRules;
	}
	
	public static @NotNull SQLInfo getSQLInfo() {
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
		ConfigurationSection section = yamlConfiguration.getConfigurationSection("SQL");
		
		boolean enabled = section.getBoolean("Enabled");
		String type = section.getString("Type");
		String host = section.getString("Host");
		String database = section.getString("Database");
		String user = section.getString("User");
		String password = section.getString("Password");
		boolean sslEnabled = section.getBoolean("EnableSSL");
		
		return new SQLInfo(enabled, type, database, host, user, password, sslEnabled);
	}
	
	public static @NotNull File getConfigFile() {
		return configFile;
	}

}
