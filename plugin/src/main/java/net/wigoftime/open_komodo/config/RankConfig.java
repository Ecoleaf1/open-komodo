package net.wigoftime.open_komodo.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.objects.Rank;

public abstract class RankConfig 
{

	private static final File rankConfigFile = new File(Main.dataFolderPath+"/Ranks.yml");
	
	public static void setup()
	{
		YamlConfiguration config = YamlConfiguration.loadConfiguration(rankConfigFile);
		
		for (String rank : config.getKeys(false))
		{
			ConfigurationSection section = config.getConfigurationSection(rank);
			
			// Get ID
			int id = section.getInt("ID");
			
			// Get XP Price
			double XPPrice;
			if (section.contains("XP Price"))
				XPPrice = section.getDouble("XP Price");
			else
				XPPrice = -1;
			
			// Get Points Salery
			int pSalery;
			if (section.contains("Salery (Points)"))
				pSalery = section.getInt("Salery (Points)");
			else
				pSalery = 0;
			
			// Get Coins Salery
			int cSalery;
			if (section.contains("Salery (Coins)"))
				cSalery = section.getInt("Salery (Coins)");
			else
				cSalery = 0;
			
			// Get Prefix
			String prefix = section.getString("Prefix");
			
			// Get List of Permissions
			List<String> permissionStr = section.getStringList("Permissions");
			List<Permission> permissions = new ArrayList<Permission>(permissionStr.size());
			for (String s : permissionStr)
			{
				permissions.add(new Permission(s));
			}
			
			//Get World Permissions
			HashMap<String, List<Permission>> worldPermissionMap = new HashMap<String, List<Permission>>();
			
			ConfigurationSection worldsSection = section.getConfigurationSection("Worlds");
			
			// Loop through each world permission
			if (section.contains("Worlds"))
				for (String s : worldsSection.getKeys(false))
				{
					ConfigurationSection section2 = worldsSection.getConfigurationSection(s);
					
						// Create new list for the world's permission and add it in
					List<Permission> worldPermissions = new LinkedList<Permission>();
					for (String ss : section2.getStringList("Permissions"))
							worldPermissions.add(new Permission(ss));
					
					// Add it in the rank's worldpermission map
					worldPermissionMap.put(rank, permissions);
				}
			
			// Create rank
			new Rank(id, XPPrice, pSalery, cSalery, rank, prefix, permissions, worldPermissionMap);
		}
	}
	
	public static File getFile()
	{
		return rankConfigFile;
	}
	 
}
