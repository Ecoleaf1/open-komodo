package net.wigoftime.open_komodo.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.PrintConsole;
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
			new Rank(id, XPPrice, pSalery, rank, prefix, permissions, worldPermissionMap);
		}
	}
	/*
	// Get World Permissions
	public static List<Permission> getPermissions(String rankName, World world)
	{
		YamlConfiguration config = YamlConfiguration.loadConfiguration(rankConfigFile);
		
		ConfigurationSection s1 = config.getConfigurationSection(rankName);
		
		if (s1 == null)
			return null;
		
		ConfigurationSection s2 = s1.getConfigurationSection("Worlds");
		
		if (s2 == null)
			return null;
		
		ConfigurationSection s3 = s2.getConfigurationSection(world.getName());
		
		if (s3 == null)
			return null;
		
		List<String> list = s3.getStringList("Permissions");
		List<Permission> pList = new LinkedList<Permission>();
		
		for (String s : list)
			pList.add(new Permission(s));
		
		return pList;
	}
	
	 public static boolean containsRank(String rank) 
	 {
		
		if (YamlConfiguration.loadConfiguration(rankConfigFile).isConfigurationSection(rank))
			return true;
		else
			return false;
		
	}
	
	// A function to check true or false if one have the permission due to rank permissions
	public static boolean checkPermission(String rank,String permission) {
		YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(rankConfigFile);
		
		if (configYaml.getConfigurationSection(rank) == null)
			return false;
		
		for (String s : configYaml.getConfigurationSection(rank).getStringList("Permissions")) {
			if (s.equalsIgnoreCase(permission)) {
				return true;
			}
		}
		
		// If went through loop and didn't have it, return false
		return false;
		
	}
	
	public static List<String> rankPermissions(String rank) {
		
		// Open Config
		YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(rankConfigFile);
		
		// Check if there is a rank
		if (!yamlConfig.isConfigurationSection(rank))
			return null;
		
		ConfigurationSection section = yamlConfig.getConfigurationSection(rank);
		
		List<String> list = section.getStringList("Permissions");
		
		return list;
		
	}
	
	public static String getRankPrefix(String rank) 
	{	
		if (!containsRank(rank))
				return "";
		
		YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(rankConfigFile);
		
		if (yamlConfig.getConfigurationSection(rank).contains("Prefix"))
			return yamlConfig.getConfigurationSection(rank).getString("Prefix");
		else
			return "";
			
	}
	
	public static int getID(String rank)
	{
		if (!containsRank(rank))
			return -1;
	
	YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(rankConfigFile);
	
	if (yamlConfig.getConfigurationSection(rank).contains("ID"))
		return yamlConfig.getConfigurationSection(rank).getInt("ID");
	else
		return -1;
	}
	
	public static double getXpPrice(String rank)
	{
		if (!containsRank(rank))
			return -1;
		
		YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(rankConfigFile);
		ConfigurationSection section = yamlConfig.getConfigurationSection(rank);
		
		if (section.contains("XP Price"))
			return section.getDouble("XP Price");
		else
			return -1;
	} */
	
	public static File getFile()
	{
		return rankConfigFile;
	}
	 
}
