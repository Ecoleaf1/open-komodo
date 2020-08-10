package net.wigoftime.open_komodo.config;

import java.io.File;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;

import net.minecraft.server.v1_14_R1.EntityTypes;
import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.TypeToEnum;
import net.wigoftime.open_komodo.objects.Pet;

public abstract class PetConfig 
{
	private static final File file = new File(Main.dataFolderPath+"/Pets.yml");
	public static void setup()
	{
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		ConfigurationSection section = config.getConfigurationSection("Pets");
		
		// Loop for each pet type
		for (String s : section.getKeys(false))
		{
			ConfigurationSection s2 = section.getConfigurationSection(s);
			
			// Get all Strings from config
			String displayName = s2.getString("Display Name");
			String mob = s2.getString("Mob");
			int id = s2.getInt("id");
			
			// Convert Displayname into colours
			displayName = ChatColor.translateAlternateColorCodes('&', displayName);
			
			// Get type from mob string
			EntityTypes<?> type = TypeToEnum.convertToEntityTypes(mob);
			
			int pPrice;
			if (s2.contains("Price (Points)"))
				pPrice = s2.getInt("Price (Points)");
			else
				pPrice = -1;
			
			int cPrice;
			if (s2.contains("Price (Coins)"))
				cPrice = s2.getInt("Price (Coins)");
			else
				cPrice = -1;
			
			// Get description of pet
			List<String> desc;
			if (s2.contains("Description"))
				desc = s2.getStringList("Description");
			else
				desc = null;
			
			// Get permission of pet
			Permission permission;
			if (s2.contains("Permission"))
				permission = new Permission(s2.getString("Permission"));
			else
				permission = null;
			
			// Create new pet
			new Pet(displayName,desc, permission, type, id, pPrice, cPrice);
		}
	}
	
	public static File getConfig()
	{
		return file;
	}
}
