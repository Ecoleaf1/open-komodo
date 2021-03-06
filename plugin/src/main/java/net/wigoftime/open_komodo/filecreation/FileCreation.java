package net.wigoftime.open_komodo.filecreation;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.config.*;
import net.wigoftime.open_komodo.etc.Filter;
import net.wigoftime.open_komodo.etc.PrintConsole;
import org.jetbrains.annotations.NotNull;

import java.io.*;

abstract public class FileCreation 
{
	public static final File config = Config.getConfigFile();
	public static final File emoteConfig = EmoteConfig.getFile();
	public static final File itemConfig = ItemConfig.getFile();
	public static final File rankConfig = RankConfig.getFile();
	public static final File petConfig = PetConfig.getConfig();
	public static final File whitelistDict = Filter.fileDict;

	public static void create(@NotNull ConfigType type)
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
			
			String s;
			
			File directoryOfPropShop = new File(Main.dataFolderPath+"/gui/propshop");
			if (!directoryOfPropShop.exists())
				directoryOfPropShop.mkdirs();
			
			file = new File(Main.dataFolderPath+"/gui/propshop/default.yml");
			break;
		case HATSHOP_DEFAULT:
			is = Main.class.getClassLoader().getResourceAsStream("default_gui/hatshop/default.yml");
			
			File directoryOfHatMenu = new File(Main.dataFolderPath+"/gui/hatshop");
			if (!directoryOfHatMenu.exists())
				directoryOfHatMenu.mkdirs();
			
			file = new File(Main.dataFolderPath+"/gui/hatshop/default.yml");
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
	
}
