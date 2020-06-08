package net.wigoftime.open_komodo.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.PrintConsole;

public abstract class TagConfig 
{
	
	private static final File config = new File(Main.dataFolderPath+"/Tags.yml");
	
	public static String getTag(UUID uuid) 
	{
		File playerFile = PlayerConfig.getPlayerConfig(uuid);
		YamlConfiguration playerYaml = YamlConfiguration.loadConfiguration(playerFile);
		
		String tag = playerYaml.getConfigurationSection("General").getString("Tag");
		
		return tag;
	}
	
	public static void checkTag(Player player) 
	{
		
		File playerFile = PlayerConfig.getPlayerConfig(player);
		YamlConfiguration playerYaml = YamlConfiguration.loadConfiguration(playerFile);
		YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(config);
		
		String tag = playerYaml.getConfigurationSection("General").getString("Tag");
		
		if (tag.equals(""))
			return;
		
		
		for (String s : configYaml.getStringList("Tags"))
			if (s.equalsIgnoreCase(tag))
				return;
		
		// If no tag is found then..
		
		playerYaml.getConfigurationSection("General").set("Tag", "");
		PrintConsole.print(player.getUniqueId() + " Doesn't have valid tag, set to none.");
		
		try 
		{
			playerYaml.save(playerFile);
		} catch (IOException e) 
		{
			PrintConsole.print("ERROR: Can't save the " + player.getUniqueId() + "'s invaild tag to vaild tag.");
		}
	}
	
	public static void create() 
	{
		
		if (config.exists())
			return;
		
		try {
			config.createNewFile();
		} catch (IOException e) {
			PrintConsole.print("Could not create Tags.yml");
		}
		
		FileWriter fw;
		
		try {
			fw = new FileWriter(config);
			
			fw.write("Tags:\n");
			fw.write("  Freshmen:\n" + 
					"    Name: Freshmen\n" + 
					"    Display: Freshmen\n" + 
					"    Points Price: 880\n");
			
			fw.close();
		} 
		catch (IOException e) 
		{
			PrintConsole.print("Could not write to Tags.yml");
		}
		
	}
	
	public static File get() 
	{
		return config;
	}
	
	
}
