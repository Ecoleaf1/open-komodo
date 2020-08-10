package net.wigoftime.open_komodo.sql;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import net.wigoftime.open_komodo.config.Config;
import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.Home;
import net.wigoftime.open_komodo.objects.Pet;
import net.wigoftime.open_komodo.objects.SQLInfo;

abstract public class SQLManager {
	public static void setup() {
		SQLInfo sqlInfo = Config.getSQLInfo();
		
        if (!sqlInfo.enabled) {
        	sqlConnection = null;
        	return;
        }
        
        try {
        	if (sqlInfo.sslEnabled)
        		sqlConnection = DriverManager.getConnection("jdbc:"+sqlInfo.url+"?useSSL=true&requireSSL=true", sqlInfo.user, sqlInfo.password);
        	else
        		sqlConnection = DriverManager.getConnection("jdbc:"+sqlInfo.url, sqlInfo.user, sqlInfo.password);
        } catch (SQLException exception) {
        	exception.printStackTrace();
        	sqlConnection = null;
        	return;
        }
        
        createMainTable();
        setUpWorlds(Bukkit.getWorlds());
        setupBagInventories();
	}
	
	private static Connection sqlConnection;
	
	private static void setupBagInventories()
	{
		for (World world : Bukkit.getWorlds())
			createBagInventoryTable(world.getName());
	}
	
	public static void createMainTable() {
		// Create Main table in SQL Database
		Statement statement;
		try {
			statement = sqlConnection.createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS `OpenKomodo.Main` ( "
					+ "`UUID` BINARY(16) NOT NULL, "
					+ "`Active Tag` VARCHAR(70) DEFAULT '', "
					+ "`Joined Date` DATE NULL DEFAULT NULL , "
					+ "`Mute Date` DATETIME NULL DEFAULT NULL , "
					+ "`Mute Reason` VARCHAR(150) NOT NULL DEFAULT '' , "
					+ "`Ban Date` DATETIME NULL DEFAULT NULL , "
					+ "`Ban Reason` VARCHAR(100) NOT NULL DEFAULT '' , "
					+ "`Rank ID` TINYINT UNSIGNED DEFAULT 0 , "
					+ "`XP` DECIMAL(10,6) UNSIGNED NOT NULL DEFAULT '0.0' , "
					+ "`Permissions` LONGTEXT NOT NULL , "
					+ "`TIP (USD)` INT UNSIGNED DEFAULT 0 , "
					+ "`Points` INT UNSIGNED DEFAULT 720 , "
					+ "`Coins` INT UNSIGNED DEFAULT 0 , "
					+ "`Items` LONGTEXT NOT NULL, "
					+ "`Pets` MEDIUMTEXT NOT NULL, "
					+ "`Homes` LONGTEXT NOT NULL, "
					+ "`Home Limit` INT UNSIGNED DEFAULT 1)");
			
			statement.close();
		} catch (SQLException exception) {
			exception.printStackTrace();
			return;
		}
	}
	
	public static void createBagInventoryTable(String worldName) {
		// Create Bag inventory table in SQL Database
		Statement statement;
		try {
			statement = sqlConnection.createStatement();
			statement.execute(String.format("CREATE TABLE IF NOT EXISTS `OpenKomodo.Bag_Inventory.%s` ( "
					+ "`UUID` BINARY(16) NOT NULL, "
					+ "`ID` INT UNSIGNED, "
					+ "`Inventory` TEXT, UNIQUE (`ID`))", worldName));
			
			statement.close();
			
		} catch (SQLException exception) {
			exception.printStackTrace();
			return;
		}
	}
	
	public static int createBagInventory(UUID uuid, String worldName) {
		// Create Bag inventory in SQL Database
		Statement statement;
		try {
			statement = sqlConnection.createStatement();
			
			ResultSet result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Bag_Inventory.%s` WHERE `UUID` = UNHEX('%s') ORDER BY `ID` DESC LIMIT 1", worldName, uuid.toString().replaceAll("-", "")));
																	// SELECT * FROM `OpenKomodo.Bag_Inventory.komodo_island` WHERE `UUID` = UNHEX('3a401966852e4dd5b9e7d976f6cc7685') AND `ID` = 1 ORDER BY `ID` DESC LIMIT 1;
			int id;
			if (result.next())
				id = result.getInt("ID") + 1;
			else
				id = 1;
			
			statement.execute(String.format("INSERT INTO `OpenKomodo.Bag_Inventory.%s` ( "
					+ "`UUID` , "
					+ "`Inventory` , "
					+ "`ID`) VALUES (" 
					+ "UNHEX('%s'), '', %d)", worldName, uuid.toString().replaceAll("-", ""), id));
			
			result.close();
			statement.close();
			return id;
		} catch (SQLException exception) {
			exception.printStackTrace();
			return 0;
		}
	}
	
	public static void setBagInventory(UUID uuid, String worldName, int ID, List<ItemStack> inventory) {
		try {
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		BukkitObjectOutputStream objectOutputStream = new BukkitObjectOutputStream(byteOutputStream);
		
		objectOutputStream.writeObject(inventory);
		objectOutputStream.close();
		
		Statement statement;
		statement = sqlConnection.createStatement();
		statement.execute(String.format("UPDATE `OpenKomodo.Bag_Inventory.%s` SET `Inventory` = \"%s\" WHERE `UUID` = UNHEX('%s') AND `ID` = %d", 
				worldName,
				Base64Coder.encodeLines(byteOutputStream.toByteArray()),
				uuid.toString().replaceAll("-", ""), ID 
				));
		
		byteOutputStream.close();
		statement.close();
		} catch (SQLException | IOException exception) {
			exception.printStackTrace();
			return;
		}
	}
	
	public static List<ItemStack> getBagInventory(UUID uuid, String worldName, int ID) 
	{
		try {
			Statement statement = sqlConnection.createStatement();
			ResultSet result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Bag_Inventory.%s` WHERE `UUID` = UNHEX('%s') AND `ID` = %d", 
					worldName, uuid.toString().replaceAll("-", ""), ID));
			
			if (!result.next())
				return null;
			
			if (result.getString("Inventory") == "")
			{
				result.close();
				statement.close();
				return new ArrayList<ItemStack>(0);
			}
			
			ByteArrayInputStream byteInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(result.getString("Inventory")));
			BukkitObjectInputStream objectInputStream = new BukkitObjectInputStream(byteInputStream);
			
			List<ItemStack> inventory = (List<ItemStack>) objectInputStream.readObject();
			
			objectInputStream.close();
			byteInputStream.close();
			result.close();
			statement.close();
			
			return inventory;
		} catch (SQLException | IOException | ClassNotFoundException exception)
		{
			exception.printStackTrace();
			return null;
		}
			
	}
	
	public static void setUpWorlds(List<World> worlds) {
		for (World world : worlds) {
			createWorldTable(world.getName());
		}
	}
	
	private static void createWorldTable(String worldName) {
		// Create World table in SQL Database
		Statement statement;
		try {
			statement = sqlConnection.createStatement();
			
			final String statementString = String.format(
					""
					+ "CREATE TABLE IF NOT EXISTS `OpenKomodo.Worlds.%s` ( "
					+ "`UUID` BINARY(16) NOT NULL PRIMARY KEY, "
					+ "`inventory` TEXT DEFAULT NULL, "
					+ "`Permissions` TEXT)", worldName);
			
			statement.execute(statementString);
			statement.close();
		} catch (SQLException exception) {
			exception.printStackTrace();
			return;
		}
	}
	
	public static void saveItems(String jsonString, UUID playerUUID) {
		Statement statement;
		
	try {
		statement = sqlConnection.createStatement();
		statement.execute(String.format("UPDATE `OpenKomodo.Main` SET `Items` = '%s\' WHERE `UUID` = UNHEX(\"%s\")", jsonString, playerUUID.toString().replaceAll("-", "")));
		
		statement.close();
	} catch (SQLException exception) {
		exception.printStackTrace();
		return;
	}
	}
	
	public static List<CustomItem> getItems(UUID playerUUID) {
		try {
			Statement statement = sqlConnection.createStatement();
			ResultSet result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main` WHERE UUID = UNHEX(\"%s\")", playerUUID.toString().replaceAll("-", "")));
			
			result.next();
			
			String itemsString = result.getString("Items");
			
			Object parser;
			try {
				parser = new JSONParser().parse(itemsString);
			} catch (ParseException e) {
				e.printStackTrace();
				result.close();
				statement.close();
				return null;
			}
			
			List<Long> idList = (JSONArray) parser;
			
			List<CustomItem> ownedItems = new ArrayList<CustomItem>(idList.size());
			
			boolean hasError = false;
			for (long id : idList) {
				CustomItem item = CustomItem.getCustomItem((int) id);
				if (item == null) {
					Player player = Bukkit.getPlayer(playerUUID);
					if (player != null)
						player.sendMessage(String.format("%sERROR: Your item (ID %d) doesn't appear to be a thing in items! \nPlease contact support with a picture of this error to recover this item. Meantime to prevent errors, this item will be deleted from your inventory",
								ChatColor.DARK_RED, id));
					hasError = true;
					continue;
				}
				
				ownedItems.add(CustomItem.getCustomItem((int) id));
			}
			
			if (hasError) {
				JSONArray json = new JSONArray();
				for (CustomItem item : ownedItems)
					json.add(item.getID());
				
				SQLManager.saveItems(json.toString(), playerUUID);
			}
			result.close();
			statement.close();
			return ownedItems;
		}
		catch (SQLException exception) {
			exception.printStackTrace();
		}
		
		return null;
	}
	
	public static String getActiveTag(UUID uuid) {
		try {
			Statement statement = sqlConnection.createStatement();
			ResultSet result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main` WHERE UUID = UNHEX(\"%s\")", uuid.toString().replaceAll("-", "")));
			
			result.next();
			
			String tagDisplay = result.getString("Active Tag");
			
			result.close();
			statement.close();
			return tagDisplay;
		} catch (SQLException exception) {
			exception.printStackTrace();
			return "";
		}
	}
	
	public static void setTag(UUID uuid, String tag) {
		// Set 
		Statement statement;
		try {
			statement = sqlConnection.createStatement();
			statement.execute(String.format(""
					+ "UPDATE `OpenKomodo.Main` " +
					"SET `Active Tag` = \"%s\" " +
					"WHERE `UUID` = UNHEX('%s');", tag, uuid.toString().replaceAll("-", "")));
			
			statement.close();
		}
		catch (SQLException exception)
		{
			exception.printStackTrace();
		}
	}
	
	public static Date getJoinDate(UUID uuid) {
		// Get XP
		Statement statement;
		try {
			statement = sqlConnection.createStatement();
			ResultSet result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main`"));
			
			result.next();
			
			Date joinedDate = result.getDate("Joined Date");
			
			result.close();
			statement.close();
			
			return joinedDate;
		}
		catch (SQLException exception)
		{
			exception.printStackTrace();
			return null;
		}
	}
	
	public static boolean containsPlayer(UUID uuid) {
		// Query Player
		Statement statement;
		try {
			statement = sqlConnection.createStatement();
			ResultSet result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main` WHERE `UUID` = UNHEX('%s')", uuid.toString().replaceAll("-", "")));
			
			boolean exists;
			
			if (result.next())
				exists = true;
			else
				exists = false;
			
			result.close();
			statement.close();
			
			return exists;
		}
		catch (SQLException exception)
		{
			exception.printStackTrace();
			return false;
		}
	}
	
	public static void setXP(UUID uuid, double xp) {
		// Set XP
		Statement statement;
		try {
			statement = sqlConnection.createStatement();
			statement.execute(String.format(""
					+ "UPDATE `OpenKomodo.Main` " +
					"SET `XP` = %s" +
					"WHERE `UUID` = UNHEX('%s');", xp, uuid.toString().replaceAll("-", "")));
			
			statement.close();
		}
		catch (SQLException exception)
		{
			exception.printStackTrace();
		}
	}
	
	public static double getXP(UUID uuid) {
		// Get XP
		Statement statement;
		try {
			statement = sqlConnection.createStatement();
			ResultSet result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main`"));
			
			result.next();
			
			double xp = result.getDouble("XP");
			
			result.close();
			statement.close();
			
			return xp;
		}
		catch (SQLException exception)
		{
			exception.printStackTrace();
			return 0.0;
		}
	}
	
	public static void createPlayer (UUID uuid) {
		// Create player in SQL Database
		Statement statement;
		try {
			statement = sqlConnection.createStatement();
			statement.execute(String.format("INSERT INTO `OpenKomodo.Main` ("
					+ "`UUID`, "
					+ "`Mute Date`, "
					+ "`Mute Reason`, "
					+ "`Ban Date`, "
					+ "`Ban Reason`, "
					+ "`Permissions`, "
					+ "`Items`, "
					+ "`Pets`, "
					+ "`Homes`, "
					+ "`Joined Date`) VALUES "
					+ "(UNHEX(\"%s\"), DATE('1990-01-01'), '', DATE('1990-01-01'), '', '', '[]', '', '', DATE(\"%s\"))", 
					uuid.toString().replaceAll("-", "") ,new SimpleDateFormat("yyyy-MM-dd").format(Date.from(Instant.now()))));
			
			statement.close();
			
		} catch (SQLException exception) {
			exception.printStackTrace();
			return;
		}
	}
	
	public static boolean containsWorldPlayer(UUID uuid, String worldName) {
		// Query Player
		Statement statement;
		try {
			statement = sqlConnection.createStatement();
			ResultSet result = statement.executeQuery(String.format("SELECT `UUID` FROM `OpenKomodo.Worlds.%s` WHERE `UUID` = UNHEX('%s')", worldName, uuid.toString().replaceAll("-", "")));
			
			boolean exists;
			
			if (result.next())
				exists = true;
			else
				exists = false;
			
			result.close();
			statement.close();
			
			return exists;
		}
		catch (SQLException exception)
		{
			exception.printStackTrace();
			return false;
		}
	}
	
	public static void createWorldPlayer (UUID uuid, String worldName) {
		// Create player in SQL Database
		Statement statement;
		try {
			statement = sqlConnection.createStatement();
			statement.execute(String.format("INSERT INTO `OpenKomodo.Worlds.%s` ("
					+ "`UUID`, "
					+ "`Inventory`, "
					+ "`Permissions`) VALUES "
					+ "(UNHEX(\"%s\"), '', '')", 
					worldName, uuid.toString().replaceAll("-", "")));
			
			statement.close();
			
		} catch (SQLException exception) {
			exception.printStackTrace();
			return;
		}
	}
	
	/*
	public static void createPlayer2 (CustomPlayer customPlayer) {
		// Create player in SQL Database
		Statement statement;
		try {
			statement = sqlConnection.createStatement();
			statement.execute(String.format("INSERT INTO `OpenKomodo.Main` ("
					+ "`UUID`, "
					+ "`Nickname`, "
					+ "`Joined Date`, "
					+ "`Mute Date`, "
					+ "`Ban Date`, "
					+ "`Rank ID`, "
					+ "`Permissions`, "
					+ "`TIP (USD)`, "
					+ "`Points`, "
					+ "`Coins`, "
					+ "`Items`, "
					+ "`Homes`) VALUES "
					+ "(UNHEX(\"%s\"), '', DATE(\"%s\"), NULL, NULL, 0, '', %d, 0, 0, '', '')", 
					customPlayer.getUniqueId().toString().replaceAll("-", ""), new SimpleDateFormat("yyyy-MM-dd").format(Date.from(Instant.now())), customPlayer.getDonated()));
			
			statement.close();
			
		} catch (SQLException exception) {
			exception.printStackTrace();
			return;
		}
	}*/
	
	public static void setGlobalPermissions(UUID uuid, List<Permission> permissions) {
		try {
			// Serialize the list of permissions
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream objectOutputStream = new BukkitObjectOutputStream(byteOutputStream);
			
			List<String> permissionInString = new ArrayList<String>(permissions.size());
			Iterator<Permission> iterator = permissions.iterator();
			while(iterator.hasNext())
				permissionInString.add(iterator.next().getName());
			
			objectOutputStream.writeObject(permissionInString);
			objectOutputStream.close();
			
			// Save the serialized list of permissions into base64 string into the SQL database
			Statement statement = sqlConnection.createStatement();
			statement.execute(String.format("UPDATE `OpenKomodo.Main` " +
					"SET `Permissions` = \"%s\"" +
					"WHERE `UUID` = UNHEX('%s');", Base64Coder.encodeLines(byteOutputStream.toByteArray()), uuid.toString().replaceAll("-", "")));
			
			// Close output Stream and SQL statement
			byteOutputStream.close();
			statement.close();
			} catch (SQLException | IOException exeception) {
				exeception.printStackTrace();
			}
	}
	
	
	public static List<Permission> getGlobalPermissions(UUID uuid) {
		try {
			Statement statement = sqlConnection.createStatement();
			
			// Retrieve world permissions
			ResultSet result = statement.executeQuery(String.format("SELECT `UUID`, `Permissions` FROM `OpenKomodo.Main` " +
					"WHERE `UUID` = UNHEX('%s');", uuid.toString().replaceAll("-", "")));
			
			// Get first result
			result.next();
			
			// Get the permissions String for the first row
			String permissionsString = result.getString("Permissions");
			
			// If empty, return empty list of permissions
			if (permissionsString == "")
				return new ArrayList<Permission>(0);
			
			// Deserialize permissions to a list of permissions
			ByteArrayInputStream byteInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(permissionsString));
			BukkitObjectInputStream objectInputStream = new BukkitObjectInputStream(byteInputStream);
			
			// Get the permissions String for the first row
			List<String> permissionsInString = (List<String>) objectInputStream.readObject();
			List<Permission> permissions = new ArrayList<Permission>(permissionsInString.size());
			
			Iterator<String> iterator = permissionsInString.iterator();
			while (iterator.hasNext())
				permissions.add(new Permission(iterator.next()));
			
			// Close input stream and SQL statement
			objectInputStream.close();
			byteInputStream.close();
			statement.close();
			
			return permissions;
			} catch (SQLException | IOException | ClassNotFoundException exeception) {
				exeception.printStackTrace();
				return new ArrayList<Permission>(0);
			}
	}
	
	public static void setWorldPermission(UUID uuid, List<Permission> permissions, String worldName) {
		try {
			// Serialize the list of permissions
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream objectOutputStream = new BukkitObjectOutputStream(byteOutputStream);
			
			List<String> permissionInString = new ArrayList<String>(permissions.size());
			Iterator<Permission> iterator = permissions.iterator();
			while(iterator.hasNext())
				permissionInString.add(iterator.next().getName());
			
			objectOutputStream.writeObject(permissionInString);
			objectOutputStream.close();
			
			// Save the serialized list of permissions into base64 string into the SQL database
			Statement statement = sqlConnection.createStatement();
			statement.execute(String.format("UPDATE `OpenKomodo.Worlds.%s` " +
					"SET `Permissions` = \"%s\"" +
					"WHERE `UUID` = UNHEX('%s');", worldName, Base64Coder.encodeLines(byteOutputStream.toByteArray()), uuid.toString().replaceAll("-", "")));
			
			// Close output Stream and SQL statement
			byteOutputStream.close();
			statement.close();
			} catch (SQLException | IOException exeception) {
				exeception.printStackTrace();
			}
	}
	
	public static List<Permission> getWorldPermission(UUID uuid, String worldName) {
		try {
			Statement statement = sqlConnection.createStatement();
			
			// Retrieve world permissions
			ResultSet result = statement.executeQuery(String.format("SELECT `UUID`, `Permissions` FROM `OpenKomodo.Worlds.%s` " +
					"WHERE `UUID` = UNHEX('%s');", worldName, uuid.toString().replaceAll("-", "")));
			
			// Get first result
			result.next();
			
			// Get the permissions String for the first row
			String permissionsString = result.getString("Permissions");
			
			// If empty, return list of permissions
			if (permissionsString == "")
				return new ArrayList<Permission>(0);
			
			// Deserialize permissions to a list of permissions
			ByteArrayInputStream byteInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(permissionsString));
			BukkitObjectInputStream objectInputStream = new BukkitObjectInputStream(byteInputStream);
			
			List<String> permissionsInString = (List<String>) objectInputStream.readObject();
			List<Permission> permissions = new ArrayList<Permission>(permissionsInString.size());
			
			Iterator<String> iterator = permissionsInString.iterator();
			while (iterator.hasNext())
				permissions.add(new Permission(iterator.next()));
			
			// Close input stream and SQL statement
			objectInputStream.close();
			byteInputStream.close();
			statement.close();
			
			return permissions;
			} catch (SQLException | IOException | ClassNotFoundException exeception) {
				exeception.printStackTrace();
				return new ArrayList<Permission>(0);
			}
	}
	
	public static void setInventory(UUID uuid, String worldName, ItemStack[] inventory) {
		try {
		Statement statement = sqlConnection.createStatement();
		
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream objectOutput = new BukkitObjectOutputStream(outputStream);
			
			objectOutput.writeObject(inventory);
			objectOutput.close();
			
			ResultSet result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Worlds.%s`", worldName));
			
			if (result.next())
				statement.execute(String.format(""
						+ "UPDATE `OpenKomodo.Worlds.%s` " +
						"SET `Inventory` = '%s'" +
						"WHERE `UUID` = UNHEX('%s');", worldName, Base64Coder.encodeLines(outputStream.toByteArray()), uuid.toString().replaceAll("-", "")));
			else
			{
				createWorldPlayer(uuid, worldName);
				setInventory(uuid, worldName, inventory);
				return;
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		
		} catch (SQLException exeception) {
			exeception.printStackTrace();
		}
	}
	
	public static ItemStack[] getInventory(UUID uuid, String worldName) {
		try {
			Statement statement = sqlConnection.createStatement();
			
			ResultSet result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Worlds.%s` " +
					"WHERE `UUID` = UNHEX('%s');", worldName, uuid.toString().replaceAll("-", "")));
			
			if (!result.next()) {
				ItemStack[] inventory = new ItemStack[45];
				
				setInventory(uuid, worldName, inventory);
				return new ItemStack[0];
			}
			
			String inventoryString = result.getString("Inventory");
			
			if (inventoryString == "")
				return new ItemStack[0];
			
			byte[] decode = Base64Coder.decodeLines(inventoryString);
			
			ByteArrayInputStream inputStream = new ByteArrayInputStream(decode);
			BukkitObjectInputStream objectInput;
			try {
				objectInput = new BukkitObjectInputStream(inputStream);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				
				return new ItemStack[0];
			}
			
			ItemStack[] inventory;
			try {
				inventory = (ItemStack[]) objectInput.readObject();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
			return inventory;
		} catch (SQLException exeception) {
			exeception.printStackTrace();
			return new ItemStack[0];
		}
	}
	
	public static void setTip(UUID uuid, int amount) {
		try {
		Statement statement = sqlConnection.createStatement();
		statement.execute(String.format("UPDATE `OpenKomodo.Main` SET `TIP (USD)` = %d WHERE `UUID` = UNHEX('%s')", amount, uuid.toString().replaceAll("-", "")));
		} catch (SQLException exception) {
			exception.printStackTrace();
			
			Player player = Bukkit.getPlayer(uuid);
			if (player == null)
				return;
			
			player.sendMessage(String.format("%sUhh, something went wrong. Try contacting support. Error: SQLException#SetTip Time: %s", ChatColor.DARK_RED, Instant.now().toString()));
		}
	}
	
	public static int getTip(UUID uuid) {
        try {
			// Get Tip in SQL Database
			Statement statement = sqlConnection.createStatement();
			ResultSet result = statement.executeQuery(String.format("SELECT `UUID`, `TIP (USD)` FROM `OpenKomodo.Main` " + 
					"WHERE UUID=UNHEX('%s');", uuid.toString().replaceAll("-", "")));
			
			// If can't find player, put 0
			if (!result.next()) {
				result.close();
				statement.close();
				return 0;
			}
			
			// Get money donated
			int usdMoney = result.getInt("TIP (USD)");
			
			// Close cause finished.
			result.close();
			statement.close();
			
			// Return with donation money
			return usdMoney;
        } catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static int getRankID(UUID uuid) {
        try {
			// Get Player in SQL Database
			Statement statement = sqlConnection.createStatement();
			ResultSet result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main` " + 
					"WHERE UUID=UNHEX('%s');", uuid.toString().replaceAll("-", "")));
			
			//if (!result.next())
			//	return 0;
			
			result.next();
			
			// Get RANK ID
			int rankID = result.getInt("Rank ID");
			
			// Close cause finished.
			result.close();
			statement.close();
			
			// Return with rank ID
			return rankID;
        } catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static void setRankID(UUID uuid, int rankID) {
        try {
			// Set rank in SQL
			Statement statement = sqlConnection.createStatement();
			statement.execute(String.format("UPDATE `OpenKomodo.Main` " + 
					"SET `Rank ID` = %d " +
					"WHERE `UUID` = UNHEX(\"%s\");",  rankID, uuid.toString().replaceAll("-", "")));
			
			// Close because finished
			statement.close();
			
			// Return with rank ID
			return;
        } catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public static List<Home> getHomes(UUID uuid) {
        try {
			// Get Homes in SQL Database
			Statement statement = sqlConnection.createStatement();
			ResultSet result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main` " +
					"WHERE `UUID` =UNHEX('%s');", uuid.toString().replaceAll("-", "")));
			
			result.next();
			
			if (result.getString("Homes") == "")
			{
				result.close();
				return new ArrayList<Home>(0);
			}
			
			ByteArrayInputStream byteInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(result.getString("Homes")));
			BukkitObjectInputStream objectInputStream = new BukkitObjectInputStream(byteInputStream);
			
			List<Home> homes = (List<Home>) objectInputStream.readObject();
			
			result.close();
			return homes;
        }
        catch (IOException | SQLException | ClassNotFoundException exception) {
        	exception.printStackTrace();
    		return new ArrayList<Home>(0);
        }
			
			/*
			Object parser;
			try {
				parser = new JSONParser().parse(result.getString("Homes"));
			} catch (ParseException e) {
				e.printStackTrace();
				result.close();
				statement.close();
				return new ArrayList<Home>(0);
			}
			
			return Home.jsonToHomes(((JSONObject) parser));
			} catch (SQLException exception) {
				exception.printStackTrace();
			}*/
        
	}
	
	public static Date getMuteDate(UUID uuid) {
		try {
			// Get Results in SQL Database
			Statement statement = sqlConnection.createStatement();
			ResultSet result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main` " +
					"WHERE `UUID` =UNHEX('%s');", uuid.toString().replaceAll("-", "")));
			
			result.next();
			
			return result.getDate("Mute Date");
			//return Date.from(Instant.ofEpochMilli(result.getDate("Mute Date").getTime()));
		} catch (SQLException exception) {
			exception.printStackTrace();
			return null;
		}
	}
	
	public static String getMuteReason(UUID uuid) {
		try {
			// Get Results in SQL Database
			Statement statement = sqlConnection.createStatement();
			ResultSet result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main` " +
					"WHERE `UUID` =UNHEX('%s');", uuid.toString().replaceAll("-", "")));
			
			result.next();
			
			return result.getString("Mute Reason");
		} catch (SQLException exception) {
			exception.printStackTrace();
			return null;
		}
	}
	
	public static void setMuteDate(UUID uuid, Date date) {
		try {
			// Set date in database
			Statement statement = sqlConnection.createStatement();
			
			if (date == null)
				statement.execute(String.format("UPDATE `OpenKomodo.Main` " +
						"SET `Mute Date` = '1990/01/01' " +
						"WHERE `UUID` = UNHEX('%s');", uuid.toString().replaceAll("-", "")));
			else {
				statement.execute(String.format("UPDATE `OpenKomodo.Main` " +
						"SET `Mute Date` = '%s' " +
						"WHERE `UUID` = UNHEX('%s');", new SimpleDateFormat("yyyy-MM-dd").format(date), uuid.toString().replaceAll("-", "")));
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}
	
	public static void setMuteReason(UUID uuid, String reason) {
		try {
			// Set mute reason in database
			Statement statement = sqlConnection.createStatement();
			
			statement.execute(String.format("UPDATE `OpenKomodo.Main` " +
					"SET `Mute Reason` = \"%s\" " +
					"WHERE `UUID` = UNHEX('%s');", reason, uuid.toString().replaceAll("-", "")));
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}
	
	public static void setBanDate(UUID uuid, Date date) {
		try {
			// Set date in database
			Statement statement = sqlConnection.createStatement();
			
			if (date == null)
				statement.execute(String.format("UPDATE `OpenKomodo.Main` " +
						"SET `Ban Date` = '1990/01/01' " +
						"WHERE `UUID` = UNHEX('%s');", uuid.toString().replaceAll("-", "")));
			else {
				statement.execute(String.format("UPDATE `OpenKomodo.Main` " +
						"SET `Ban Date` = '%s' " +
						"WHERE `UUID` = UNHEX('%s');", new SimpleDateFormat("yyyy-MM-dd").format(date), uuid.toString().replaceAll("-", "")));
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}
	
	public static Date getBanDate (UUID uuid) {
		try {
			// Get Results in SQL Database
			Statement statement = sqlConnection.createStatement();
			ResultSet result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main` " +
					"WHERE `UUID` =UNHEX('%s');", uuid.toString().replaceAll("-", "")));
			
			result.next();
			
			return result.getDate("Ban Date");
		} catch (SQLException exception) {
			exception.printStackTrace();
			return null;
		}
	}
	
	public static void setBanReason(UUID uuid, String reason) {
		try {
			// Set ban reason in database
			Statement statement = sqlConnection.createStatement();
			
			if (reason == null)
				statement.execute(String.format("UPDATE `OpenKomodo.Main` " +
						"SET `Ban Reason` = '' " +
						"WHERE `UUID` = UNHEX('%s');", uuid.toString().replaceAll("-", "")));
			else {
				statement.execute(String.format("UPDATE `OpenKomodo.Main` " +
						"SET `Ban Reason` = \"%s\" " +
						"WHERE `UUID` = UNHEX('%s');", reason, uuid.toString().replaceAll("-", "")));
			}
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}
	
	public static String getBanReason (UUID uuid) {
		try {
			// Get Results in SQL Database
			Statement statement = sqlConnection.createStatement();
			ResultSet result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main` " +
					"WHERE `UUID` =UNHEX('%s');", uuid.toString().replaceAll("-", "")));
			
			result.next();
			
			return result.getString("Ban Reason");
		} catch (SQLException exception) {
			exception.printStackTrace();
			return null;
		}
	}
	
	public static void setHomes(UUID uuid, List<Home> homes) {
		
		try {
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream outputStream = new BukkitObjectOutputStream(byteOutputStream);
			
			outputStream.writeObject(homes);
			outputStream.close();
			
			try {
				Statement statement = sqlConnection.createStatement();
				
				statement.execute(String.format("UPDATE `OpenKomodo.Main` " + 
						"SET `Homes` = '%s' " +
						"WHERE `UUID` = UNHEX('%s');",  Base64Coder.encodeLines(byteOutputStream.toByteArray()), uuid.toString().replaceAll("-", "")));
			} catch (SQLException exception) {
				exception.printStackTrace();
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public static int getCurrency(UUID uuid, Currency type) {
		
        try {
			// Get Player in SQL Database
			Statement statement = sqlConnection.createStatement();
			ResultSet result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main` " +
					"WHERE `UUID` =UNHEX('%s');", uuid.toString().replaceAll("-", "")));
			
			result.next();
			
			int amount;
			switch (type) {
			case POINTS:
				amount = result.getInt("Points");
				break;
			case COINS:
				amount = result.getInt("Coins");
				break;
			default:
				return 0;
			}
			
			// Close because finished
			statement.close();
			
			return amount;
        } catch (SQLException e) {
			e.printStackTrace();
        }
        
        return 0;
	}
	
	public static void setCurrency(UUID uuid, int amount, Currency type) {
		
        try {
			// Create statement to run SQL commands
			Statement statement = sqlConnection.createStatement();
			
			
			switch (type) {
			case POINTS:
				statement.execute(String.format("UPDATE `OpenKomodo.Main` " +
						"SET `Points`= %d " +
						"WHERE `UUID` = UNHEX(\"%s\");", amount, uuid.toString().replaceAll("-", "")));
				break;
			case COINS:
				statement.execute(String.format("UPDATE `OpenKomodo.Main` " +
						"SET `Coins` = %d " +
						"WHERE `UUID` = UNHEX(\"%s\");", amount, uuid.toString().replaceAll("-", "")));
				break;
			}
			
			// Close because finished
			statement.close();
			
        } catch (SQLException e) {
			e.printStackTrace();
        }
	}
	
	public static void setItems(CustomPlayer player) {
		List<CustomItem> items = player.getItems();
		
		JSONArray json = new JSONArray();
		for (CustomItem item : items)
			json.add(item.getID());
		/*
		switch (customItem.getType()) {
		case HAT:
			List<Long> listOfHats = ((List<Long>)json.get("Hats"));
			listOfHats.add((long) customItem.getID());
			
			json.put("Hats",  listOfHats);
			break;
		case TAG:
			List<Long> listOfTags = ((List<Long>)json.get("Tags"));
			listOfTags.add((long) customItem.getID());
			
			json.put("Tags", listOfTags);
			break;
		
		default:
			return;
		}*/
		
		try {
		// Create statement to run SQL commands
		Statement statement = sqlConnection.createStatement();
		
		statement.execute(String.format("UPDATE `OpenKomodo.Main` " +
				"SET `Items` = '%s' " +
				"WHERE `UUID` = UNHEX('%s');", json.toString(), player.getUniqueId().toString().replaceAll("-", "")));
		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}
	
	public static List<Pet> getPets(UUID uuid) {
		try {
		// Get Player in SQL Database
		Statement statement = sqlConnection.createStatement();
		ResultSet result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main` " +
				"WHERE `UUID` =UNHEX('%s');", uuid.toString().replaceAll("-", "")));
		
		result.next();
		
		if (result.getString("Pets") == "") {
			result.close();
			statement.close();
			
			return new ArrayList<Pet>(0);
		}
		
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(result.getString("Pets")));
		ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);
		
		List<Integer> idList = (List<Integer>) objectInputStream.readObject();
		
		List<Pet> ownedPets = new ArrayList<Pet>(idList.size());
		for (int id : idList)
			ownedPets.add(Pet.getPet(id));
		
		result.close();
		statement.close();
		return ownedPets;
		} catch (SQLException | ClassNotFoundException | IOException exception) {
			exception.printStackTrace();
			return null;
		}
	}
	
	public static void setPets(CustomPlayer customPlayer) {
			try {
			List<Pet> ownedPets = customPlayer.getPets();
			List<Integer> petIDs = new ArrayList<Integer>(ownedPets.size());
			
			for (Pet pet : ownedPets)
				petIDs.add(pet.getID());
			
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
			
			objectOutputStream.writeObject(petIDs);
			objectOutputStream.close();
			
			String base64 = Base64Coder.encodeLines(byteOutputStream.toByteArray());
			
			// Create statement to run SQL commands
			Statement statement = sqlConnection.createStatement();
			
			statement.execute(String.format("UPDATE `OpenKomodo.Main` " +
					"SET `Pets` = '%s' " +
					"WHERE `UUID` = UNHEX('%s');", base64, customPlayer.getUniqueId().toString().replaceAll("-", "")));
			} catch (SQLException | IOException exception) {
				exception.printStackTrace();
			}
		}
	
	public static void disconnectSQL() {
		if (sqlConnection == null)
			return;
		
		try {
			sqlConnection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean isEnabled() {
		return sqlConnection == null ? false : true;
	}
}

class Items{
	 public List<Integer> list;
	 //getter and setter
	}
