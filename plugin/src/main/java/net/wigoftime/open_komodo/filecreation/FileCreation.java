package net.wigoftime.open_komodo.filecreation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.config.Config;
import net.wigoftime.open_komodo.config.ConfigType;
import net.wigoftime.open_komodo.config.EmoteConfig;
import net.wigoftime.open_komodo.config.ItemConfig;
import net.wigoftime.open_komodo.config.PetConfig;
import net.wigoftime.open_komodo.config.RankConfig;
import net.wigoftime.open_komodo.etc.Filter;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.gui.PropShop;

abstract public class FileCreation 
{
	public static final File config = Config.getConfigFile();
	public static final File emoteConfig = EmoteConfig.getFile();
	public static final File itemConfig = ItemConfig.getFile();
	public static final File rankConfig = RankConfig.getFile();
	public static final File petConfig = PetConfig.getConfig();
	//public static final File playerConfigFolder = PlayerConfig.getConfigFolder();
	public static final File whitelistDict = Filter.fileDict;
	//public static final File playerConfig = PlayerConfig.getCon();

	public static void create(ConfigType type)
	{
		
		// Create File Variable
		File file;
		
		// Get contents in default configuration in bytes
		InputStream is;
		
		switch (type) {
		case EMOTE:
			is = Main.class.getClassLoader().getResourceAsStream("default_configs/Emotes.yml");
			file = emoteConfig;
			break;
		case ITEM:
			is = Main.class.getClassLoader().getResourceAsStream("default_configs/Items.yml");
			file = itemConfig;
			break;
		case NORMAL:
			is = Main.class.getClassLoader().getResourceAsStream("default_configs/config.yml");
			file = config;
			break;
		case PET:
			is = Main.class.getClassLoader().getResourceAsStream("default_configs/Pets.yml");
			file = petConfig;
			break;
		case PROPSHOP_DEFAULT:
			is = Main.class.getClassLoader().getResourceAsStream("default_gui/propshop/default.yml");
			
			File directory = new File(Main.dataFolderPath+"/gui/propshop");
			if (!directory.exists())
				directory.mkdirs();
			
			file = PropShop.shopPage;
			break;
		case RANK:
			is = Main.class.getClassLoader().getResourceAsStream("default_configs/Ranks.yml");
			file = rankConfig;
			break;
		case WHITELIST_DICT:
			is = Main.class.getClassLoader().getResourceAsStream("default_configs/Whitelist-Dict.txt");
			file = whitelistDict;
			break;
		default:
			PrintConsole.print("No type was specified to create file.");
			return;
		}
		/*
		if (type == ConfigType.NORMAL)
		{
			is = Main.class.getClassLoader().getResourceAsStream("default_configs/config.yml");
			file = config;
		}
		else if (type == ConfigType.EMOTE)
		{
			is = Main.class.getClassLoader().getResourceAsStream("default_configs/Emotes.yml");
			file = emoteConfig;
		}
		else if (type == ConfigType.ITEM)
		{
			is = Main.class.getClassLoader().getResourceAsStream("default_configs/Items.yml");
			file = itemConfig;
		}
		else if (type == ConfigType.RANK)
		{
			is = Main.class.getClassLoader().getResourceAsStream("default_configs/Ranks.yml");
			file = rankConfig;
		}
		else if (type == ConfigType.PET)
		{
			is = Main.class.getClassLoader().getResourceAsStream("default_configs/Pets.yml");
			file = petConfig;
		}
		else if (type == ConfigType.WHITELIST_DICT)
		{
			is = Main.class.getClassLoader().getResourceAsStream("default_configs/Whitelist-Dict.txt");
			file = whitelistDict;
		}
		else if (type == ConfigType.PROPSHOP_DEFAULT)
		{
			is = Main.class.getClassLoader().getResourceAsStream("default_gui/propshop/default.yml");
			
			File directory = new File(Main.dataFolderPath+"/gui/propshop");
			if (!directory.exists())
				directory.mkdirs();
			
			file = PropShop.shopPage;
		}
		else
		{
			PrintConsole.print("No type was specified to create file.");
			return;
		}*/
		
		// If config exists, don't generate
		if (file.exists())
			return;
		
		// Get reader to read the bytes
		InputStreamReader isReader = new InputStreamReader(is);
		// Get reader to read bytes to characters
		BufferedReader reader = new BufferedReader(isReader);
		
		// Try write a new config file
		try {
			// Create a file writer to be able to write
			FileWriter fw = new FileWriter(file);
			// Write in file writer to buffer
			BufferedWriter writer = new BufferedWriter(fw);
			
			// Write next character if there is another character
			while (reader.ready())
			{
				writer.write(reader.read());
			}
			
			// Close Writers and readers
			writer.close();
			fw.close();
			is.close();
		}
		catch (IOException e)
		{
			PrintConsole.print("Could not generation config.yml, IOExeception occured. Here's Stacktrace:");
			e.printStackTrace();
		}
	}
	
	/*public static void createFile(File f) {
		
		if (f.exists()) return;

		try {
			f.createNewFile();
		} catch (IOException e) {
			PrintConsole.print("Error: Couldn't create " + f.getName());
		}
		
	}*/
	
	
}
