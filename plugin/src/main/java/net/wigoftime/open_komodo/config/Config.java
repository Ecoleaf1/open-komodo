package net.wigoftime.open_komodo.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.PrintConsole;

abstract public class Config 
{

	private static final String dataFolderPath = Main.dataFolderPath;
	private static final File configFile = new File(dataFolderPath+"/config.yml");
	
	public static void createConfig()
	{
		// If config exists, don't generate
		if (configFile.exists())
			return;
		
		// Get stream of bytes in default config
		InputStream is = Main.class.getClassLoader().getResourceAsStream("resources/default_configs/config.yml");
		// Get reader to read the bytes
		InputStreamReader isReader = new InputStreamReader(is);
		// Get reader to read bytes to characters
		BufferedReader reader = new BufferedReader(isReader);
		
		// Try write a new config file
		try
		{
			
			FileWriter fw = new FileWriter(configFile);
			BufferedWriter writer = new BufferedWriter(fw);
			
			while (reader.ready())
			{
				writer.write(reader.read());
			}
			
			writer.close();
		}
		catch (IOException e)
		{
			PrintConsole.print("Could not generation config.yml, IOExeception occured. Here's Stacktrace:");
			e.printStackTrace();
		}
	}
	
	/*
	public static void createConfig() {
		
		
		if (configYMLFile.exists())
			return;
			
			// Create the config yml file (But empty.)
			FileCreation.createFile(configYMLFile);
			
			YamlConfiguration configYAML = new YamlConfiguration();
			
			// Variables to create in the config.yml file
			
			configYAML.createSection("Normal Message");
				
				configYAML.getConfigurationSection("Normal Message").set("Format", "[\"\",{\"text\":\"$G\",\"color\":\"gray\"},{\"text\":\"$S\",\"color\":\"dark_gray\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/msg $N \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Username: $N\"}]}}},{\"text\":\": \",\"color\":\"gray\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/msg $N \"}},{\"text\":\"$M\",\"color\":\"gray\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/msg $N \"}}]");
				configYAML.getConfigurationSection("Normal Message").set("Tag Format", "[\"\",{\"text\":\"$W\",\"color\":\"gray\"},{\"text\":\" | \",\"color\":\"dark_gray\"},{\"text\":\"$G\",\"color\":\"gray\"},{\"text\":\"$S\",\"color\":\"dark_gray\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/msg $N \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Username: $N\"}]}}},{\"text\":\": \",\"color\":\"gray\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/msg $N \"}},{\"text\":\"$M\",\"color\":\"gray\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/msg $N \"}}]");
				configYAML.getConfigurationSection("Normal Message").set("Chat Distance", 50);
				
			configYAML.createSection("Private Message");
				
				configYAML.getConfigurationSection("Private Message").set("Format (Sent)", "&7you &e-> &7$D &7:&f $M");
				configYAML.getConfigurationSection("Private Message").set("Format (Received)", "&7$N &e-> &7you &7:&f $M");
				
			configYAML.createSection("Global Settings");
				
				configYAML.getConfigurationSection("Global Settings").createSection("Spawn");
					configYAML.getConfigurationSection("Global Settings").getConfigurationSection("Spawn").set("World", "foreverplay");
					configYAML.getConfigurationSection("Global Settings").getConfigurationSection("Spawn").set("Location", new Vector(-880.5, 17, 1576.5));
				
				configYAML.getConfigurationSection("Global Settings").set("Join Message", "");
				configYAML.getConfigurationSection("Global Settings").set("Leave Message", "");
				configYAML.getConfigurationSection("Global Settings").set("Allow Item Drops", false);
				configYAML.getConfigurationSection("Global Settings").set("Explosion Enabled", false);
				configYAML.getConfigurationSection("Global Settings").set("Leaves Decay", false);
				configYAML.getConfigurationSection("Global Settings").set("Ice Melts", false);
				configYAML.getConfigurationSection("Global Settings").set("Fall Damage", false);
				
			// Try save the file, if can't, print an error message in the console
			try {
				configYAML.save(configYMLFile);
			} catch (IOException e) {
				PrintConsole.print("Error: Config.yml Couldn't be saved.");
			}
			
		final World defaultWorld = Bukkit.getWorlds().get(0);
		defaultWorld.setGameRuleValue("mobGriefing", "false");
		defaultWorld.setGameRuleValue("doWeatherCycle", "false");
		defaultWorld.setGameRuleValue("doMobSpawning", "false");
		
		final Difficulty d = defaultWorld.getDifficulty();
		defaultWorld.setDifficulty(Difficulty.PEACEFUL);
		defaultWorld.setDifficulty(d);
	} */
	
	public static File getConfigFile() {
		return configFile;
	}

}
