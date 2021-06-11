package net.wigoftime.open_komodo.config;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.ItemType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public abstract class ItemConfig 
{
	// Get the Items configuration file
	private static final File config = new File(Main.dataFolderPath+"/Items.yml");
	
	public static void setup()
	{
		
		// Open the configuration file
		YamlConfiguration yc = YamlConfiguration.loadConfiguration(config);
		
		if (yc.contains("Props"))
		{
			// Get section "Props"
			ConfigurationSection cs1 = yc.getConfigurationSection("Props");
			
			// Loop for each value inside Props
			for (String cs1ss2: cs1.getKeys(false))
			{
				// Get Item Section
				ConfigurationSection c1s2 = cs1.getConfigurationSection(cs1ss2);
				
				// Set Itemtype
				ItemType type = ItemType.PROP;
				
				// Get Info about item
				String name = c1s2.getString("Display Name");
				
				int id = c1s2.getInt("id");
				
				int pc = c1s2.getInt("Price (Points)");
				if (c1s2.contains("Price (Points)"))
					pc = c1s2.getInt("Price (Points)");
				else
					pc = -1;
				
				List<String> description;
				if (c1s2.contains("Description"))
					description = c1s2.getStringList("Description");
				else
					description = null;
				
				boolean equipable;
				if (c1s2.contains("Equipable"))
					equipable = c1s2.getBoolean("Equipable");
				else
					equipable = false;
				
				// Check if has permission or not
				// Also create item
				if (c1s2.contains("Permission"))
				{
					Permission obtainPermission;
					obtainPermission = new Permission(c1s2.getString("Permission"));
					
					// Create item
					CustomItem.create(id, name, description, pc, equipable, obtainPermission, type);
				}
				else
				{
					// Create Item
					CustomItem.create(id, name, description, pc, equipable, null, type);
				}
			}
		}
		
		if (yc.contains("Hats"))
		{
			// Get section "Hats"
			ConfigurationSection c2s1 = yc.getConfigurationSection("Hats");
			
			// Loop through value inside Tags
			for (String c2ss1: c2s1.getKeys(false))
			{
				// Get Item Section
				ConfigurationSection c2s2 = c2s1.getConfigurationSection(c2ss1);
				
				// Set Itemtype
				ItemType type = ItemType.HAT;
				
				// Get Info about item
				String name = c2s2.getString("Display Name");
				
				int id = c2s2.getInt("id");
				
				int pc;
				if (c2s2.contains("Price (Points)"))
					pc = c2s2.getInt("Price (Points)");
				else
					pc = -1;
				
				// Get description
				List<String> description;
				if (c2s2.contains("Description"))
					description = c2s2.getStringList("Description");
				else
					description = null;
				
				boolean equipable;
				if (c2s2.contains("Equipable"))
					equipable = c2s2.getBoolean("Equipable");
				else
					equipable = false;
				
				if (c2s2.contains("Permission"))
				{
					Permission obtainPermission;
					obtainPermission = new Permission(c2s2.getString("Permission"));
					
					CustomItem.create(id, name, description, pc, equipable, obtainPermission, type);
				}
				else
				{
					// Create Item
					CustomItem.create(id, name, description, pc, equipable, null, type);
				}
			}
		}
		
		if (yc.contains("Tags")) {
			// Get section "Tags"
			ConfigurationSection c3s1 = yc.getConfigurationSection("Tags");
			
			// Loop through value inside Tags
			for (String c3ss1: c3s1.getKeys(false))
			{
				// Get Item Section
				ConfigurationSection c2s2 = c3s1.getConfigurationSection(c3ss1);
				
				// Set Itemtype
				ItemType type = ItemType.TAG;
				
				// Get Info about item
				String name = c2s2.getString("Display");
				int id = c2s2.getInt("id");
				int pc;
				if (c2s2.contains("Price (Points)"))
					pc = c2s2.getInt("Price (Points)");
				else
					pc = -1;
				
				// Get description
				List<String> description;
				if (c2s2.contains("Description"))
					description = c2s2.getStringList("Description");
				else
					description = null;
				
				boolean equipable;
				if (c2s2.contains("Equipable"))
					equipable = c2s2.getBoolean("Equipable");
				else
					equipable = false;
				
				// Check if has permission or not
				// Also create item
				if (c2s2.contains("Permission"))
				{
					Permission obtainPermission;
					obtainPermission = new Permission(c2s2.getString("Permission"));
					
					// Create item
					CustomItem.create(id, name, description, pc, equipable, obtainPermission, type);
				}
				else
				{
					// Create Item
					CustomItem.create(id, name, description, pc, equipable, null, type);
				}
			}
		}
		
		if (yc.contains("Phones")) {
			// Get section "Tags"
			ConfigurationSection c4s1 = yc.getConfigurationSection("Phones");
			
			// Loop through value inside Tags
			for (String c4ss1: c4s1.getKeys(false)) {
				// Get Item Section
				ConfigurationSection c2s2 = c4s1.getConfigurationSection(c4ss1);
				
				// Set Itemtype
				ItemType type = ItemType.PHONE;
				
				// Get Info about item
				String name = c2s2.getString("Display Name");
				int id = c2s2.getInt("id");
				int pc;
				if (c2s2.contains("Price (Points)"))
					pc = c2s2.getInt("Price (Points)");
				else
					pc = -1;
				
				// Get description
				List<String> description;
				if (c2s2.contains("Description"))
					description = c2s2.getStringList("Description");
				else
					description = null;
				
				boolean equipable;
				if (c2s2.contains("Equipable"))
					equipable = c2s2.getBoolean("Equipable");
				else
					equipable = false;
				
				// Check if has permission or not
				// Also create item
				if (c2s2.contains("Permission"))
				{
					Permission obtainPermission;
					obtainPermission = new Permission(c2s2.getString("Permission"));
					
					// Create item
					CustomItem.create(id, name, description, pc, equipable, obtainPermission, type);
				}
				else
				{
					// Create Item
					CustomItem.create(id, name, description, pc, equipable, null, type);
				}
			}
		}
		
		if (yc.contains("Furniture")) {
			// Get section "Furniture"
			ConfigurationSection c5s1 = yc.getConfigurationSection("Furniture");
			
			// Loop through value inside Tags
			for (String c5ss1: c5s1.getKeys(false)) {
				// Get Item Section
				ConfigurationSection c2s2 = c5s1.getConfigurationSection(c5ss1);
				
				String name = c2s2.getString("Name");
				int id = c2s2.getInt("ID");
				
				// Create Item
				CustomItem.create(id, name, null, 0, false, null, ItemType.FURNITURE);
			}
		}
	}
		
	
	public static @NotNull File getFile()
	{
		return config;
	}
}
