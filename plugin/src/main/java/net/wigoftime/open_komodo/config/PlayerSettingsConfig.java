package net.wigoftime.open_komodo.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.Settings;

abstract public class PlayerSettingsConfig {
	private static final File settingsFolder = new File(Main.dataFolderPath+"/PlayerSettings");
	
	public static void setup() {
		if (!settingsFolder.exists())
			settingsFolder.mkdir();
	}
	
	private static File getFile(UUID uuid) {
		return new File(String.format("%s/%s.yml", settingsFolder.getAbsolutePath(), uuid.toString()));
	}
		
	public static boolean contains(UUID uuid) {
		if (!getFile(uuid).exists()) return false;
		return true;
	}
	
	public static void create(UUID uuid) {
		try {
			InputStream is = Main.class.getClassLoader().getResourceAsStream("default_configs/PlayerSettings.yml");
			
			// Get reader to read the bytes
			InputStreamReader isReader = new InputStreamReader(is);
			// Get reader to read bytes to characters
			BufferedReader reader = new BufferedReader(isReader);
			
			// Get Player config file
			File file = getFile(uuid);
			
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
			
			buffWriter.append(configStringBuilder.toString());
			buffWriter.close();
			fileWriter.close();
			reader.close();
			isReader.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public static Settings getSettings(UUID uuid) {
		File file = getFile(uuid);
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		boolean beenWritten = false;
		
		String activeTagDisplay;
		if (config.contains("Current Tag"))
			activeTagDisplay = config.getString("Current Tag");
		else {
			config.set("Current Tag", "");
			activeTagDisplay = "";
			beenWritten = true;
		}
		
		CustomItem currentPhone;
		if (config.contains("Current Phone ID")) {
			int id = config.getInt("Current Phone ID");
			
			CustomItem item = CustomItem.getCustomItem(id);
			if (item == null)
				item = CustomItem.getCustomItem(id);
			
			currentPhone = item;
		} else {
			config.set("Current Phone ID", 1);
			currentPhone = CustomItem.getCustomItem(1);
			beenWritten = true;
		}
		
		boolean displayTip;
		if (config.contains("Display Tip"))
			displayTip = config.getBoolean("Display Tip");
		else {
			config.set("Display Tip", true);
			displayTip = true;
			beenWritten = true;
		}
		
		boolean masterSounds;
		if (config.contains("Master Sounds"))
			masterSounds = config.getBoolean("Master Sounds");
		else {
			config.set("Master Sounds", true);
			masterSounds = true;
			beenWritten = true;
		}
		
		boolean tpaEnabled;
		if (config.contains("Tpa Enabled"))
			tpaEnabled = config.getBoolean("Tpa Enabled");
		else {
			config.set("Tpa Enabled", true);
			tpaEnabled = true;
			beenWritten = true;
		}
		
		boolean playerParticlesEnabled;
		if (config.contains("Player Particles Enabled"))
			playerParticlesEnabled = config.getBoolean("Player Particles Enabled");
		else {
			config.set("Player Particles Enabled", true);
			playerParticlesEnabled = true;
			beenWritten = true;
		}
		
		boolean isDiscordChatEnabled;
		if (config.contains("Discord Chat"))
			isDiscordChatEnabled = config.getBoolean("Discord Chat");
		else {
			config.set("Discord Chat", true);
			isDiscordChatEnabled = true;
			beenWritten = true;
		}
		
		if (beenWritten)
			try {
				config.save(file);
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		
		return new Settings(uuid, currentPhone, activeTagDisplay, displayTip, masterSounds, tpaEnabled, playerParticlesEnabled, isDiscordChatEnabled);
	}
	
	public static void setSettings(Settings settings, UUID uuid) {
		File file = new File(String.format("%s/%s.yml", settingsFolder.getAbsolutePath(), uuid.toString()));
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		config.set("Current Tag",settings.getTagDisplay());
		config.set("Current Phone ID",settings.getPhone().getID());
		config.set("Display Tip",settings.isDisplayTipEnabled());
		config.set("Master Sounds",settings.isMasterSoundsOn());
		config.set("Tpa Enabled",settings.isTpaEnabled());
		config.set("Player Particles Enabled",settings.isPlayerParticlesEnabled());
		config.set("Discord Chat",settings.isDiscordChatEnabled());
		
		try {
			config.save(file);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}
