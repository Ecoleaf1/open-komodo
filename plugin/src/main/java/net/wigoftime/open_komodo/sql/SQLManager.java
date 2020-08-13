package net.wigoftime.open_komodo.sql;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
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
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.Home;
import net.wigoftime.open_komodo.objects.Pet;
import net.wigoftime.open_komodo.objects.SQLInfo;

abstract public class SQLManager {
	private static final int delayAmount = 5000;
	
	private static BasicDataSource ds = new BasicDataSource();
	private static SQLInfo sqlInfo = Config.getSQLInfo();
	public static void setup() {
		
		ds.setUsername(sqlInfo.user);
		ds.setPassword(sqlInfo.password);
		ds.setUrl("jdbc:"+sqlInfo.url);
		ds.setMinIdle(5);
		ds.setMaxIdle(10);
        
        createMainTable();
        setUpWorlds(Bukkit.getWorlds());
        setupBagInventories();
	}
	
	//private static Connection sqlConnection;
	
	private static void setupBagInventories()
	{
		for (World world : Bukkit.getWorlds())
			createBagInventoryTable(world.getName());
	}
	
	public static void createMainTable() {
		Connection connection = null;
		PreparedStatement statement = null;
		
		while (true)
		try {
			connection = ds.getConnection();
			
			statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `OpenKomodo.Main` ( "
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
			PrintConsole.test("boop?");
			statement.execute();
			PrintConsole.test("boop");
			break;
		} catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
		} finally {
			if (connection != null)
			try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			
			if (statement != null)
				try { statement.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
		//}
	}
	
	public static void createBagInventoryTable(String worldName) {
		Connection connection = null;
		
		// Create Bag inventory table in SQL Database
		Statement statement = null;
		
		while (true)
		try {
			connection = ds.getConnection();
			
			statement = connection.createStatement();
			statement.execute(String.format("CREATE TABLE IF NOT EXISTS `OpenKomodo.Bag_Inventory.%s` ( "
					+ "`UUID` BINARY(16) NOT NULL, "
					+ "`ID` INT UNSIGNED, "
					+ "`Inventory` TEXT, UNIQUE (`ID`))", worldName));
			
			statement.close();
			break;
		} catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
		} finally {
			if (connection != null)
			try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
			try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
	}
	
	public static int createBagInventory(UUID uuid, String worldName) {
		Connection connection = null;
		
		// Create Bag inventory in SQL Database
		Statement statement = null;
		ResultSet result = null;
		
		while (true)
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
			result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Bag_Inventory.%s` WHERE `UUID` = UNHEX('%s') ORDER BY `ID` DESC LIMIT 1", worldName, uuid.toString().replaceAll("-", "")));
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
			return id;
		} catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
		} finally {
			if (connection != null)
			try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
			try { statement.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (result != null)
			try { result.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
	}
	
	public static void setBagInventory(UUID uuid, String worldName, int ID, List<ItemStack> inventory) {
		Connection connection = null;
		Statement statement = null;
		while (true)
		try {
			connection = ds.getConnection();
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream objectOutputStream = new BukkitObjectOutputStream(byteOutputStream);
			
			objectOutputStream.writeObject(inventory);
			objectOutputStream.close();
			statement = connection.createStatement();
			statement.execute(String.format("UPDATE `OpenKomodo.Bag_Inventory.%s` SET `Inventory` = \"%s\" WHERE `UUID` = UNHEX('%s') AND `ID` = %d", 
					worldName,
					Base64Coder.encodeLines(byteOutputStream.toByteArray()),
					uuid.toString().replaceAll("-", ""), ID 
					));
			
			byteOutputStream.close();
			statement.close();
			break;
		} catch (SQLException | IOException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
		} finally {
			if (connection != null)
			try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
			try { statement.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
	}
	
	public static List<ItemStack> getBagInventory(UUID uuid, String worldName, int ID) 
	{
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		while(true)
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
			result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Bag_Inventory.%s` WHERE `UUID` = UNHEX('%s') AND `ID` = %d", 
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
			
			return inventory;
		} catch (SQLException | IOException | ClassNotFoundException exception)
		{
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
		} finally {
			if (connection != null)
			try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
			try { statement.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (result != null)
			try { result.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
			
	}
	
	public static void setUpWorlds(List<World> worlds) {
		for (World world : worlds) {
			createWorldTable(world.getName());
		}
	}
	
	private static void createWorldTable(String worldName) {
		Connection connection = null;
		
		// Create World table in SQL Database
		Statement statement = null;
		while (true)
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
			
			final String statementString = String.format(
					""
					+ "CREATE TABLE IF NOT EXISTS `OpenKomodo.Worlds.%s` ( "
					+ "`UUID` BINARY(16) NOT NULL PRIMARY KEY, "
					+ "`inventory` TEXT DEFAULT NULL, "
					+ "`Permissions` TEXT)", worldName);
			
			statement.execute(statementString);
			break;
		} catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
		} finally {
			if (connection != null)
			try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
			try { statement.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
	}
	
	public static void saveItems(String jsonString, UUID playerUUID) {
		Connection connection = null;
		Statement statement = null;
		
		while(true)
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
			statement.execute(String.format("UPDATE `OpenKomodo.Main` SET `Items` = '%s\' WHERE `UUID` = UNHEX(\"%s\")", jsonString, playerUUID.toString().replaceAll("-", "")));
			break;
		} catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
		} finally {
			if (connection != null)
			try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
			try { statement.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
	}
	
	public static List<CustomItem> getItems(UUID playerUUID) {
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		while(true)
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
			result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main` WHERE UUID = UNHEX(\"%s\")", playerUUID.toString().replaceAll("-", "")));
			
			result.next();
			
			String itemsString = result.getString("Items");
			
			Object parser;
			try {
				parser = new JSONParser().parse(itemsString);
			} catch (ParseException e) {
				e.printStackTrace();
				parser = null;
			}
			List<Long> idList;
			if (parser == null)
				idList = new ArrayList<Long>(0);
			else
				idList = (JSONArray) parser;
			
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
			return ownedItems;
		}
		catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
		} finally {
			if (connection != null)
			try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
			try { statement.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (result != null)
			try { result.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
		
		//return null;
	}
	
	public static String getActiveTag(UUID uuid) {
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		while (true)
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
			result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main` WHERE UUID = UNHEX(\"%s\")", uuid.toString().replaceAll("-", "")));
			
			result.next();
			
			String tagDisplay = result.getString("Active Tag");
			
			return tagDisplay;
		} catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
		} finally {
			if (connection != null)
			try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
			try { statement.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (result != null)
			try { result.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
	}
	
	public static void setTag(UUID uuid, String tag) {
		Connection connection = null;
		Statement statement = null;
		
		while(true)
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
			statement.execute(String.format(""
					+ "UPDATE `OpenKomodo.Main` " +
					"SET `Active Tag` = \"%s\" " +
					"WHERE `UUID` = UNHEX('%s');", tag, uuid.toString().replaceAll("-", "")));
			break;
		} catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
		} finally {
			if (connection != null)
			try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
			try { statement.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
	}
	
	public static Date getJoinDate(UUID uuid) {
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		while(true)
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
			result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main`"));
			
			result.next();
			
			Date joinedDate = result.getDate("Joined Date");
			return joinedDate;
		} catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
		} finally {
			if (connection != null)
			try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
			try { statement.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (result != null)
			try { result.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
	}
	
	public static boolean containsPlayer(UUID uuid) {
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		while(true)
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
			result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main` WHERE `UUID` = UNHEX('%s')", uuid.toString().replaceAll("-", "")));
			
			boolean exists;
			
			if (result.next())
				exists = true;
			else
				exists = false;
			
			return exists;
		} catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
		} finally {
			if (connection != null)
			try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
			try { statement.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (result != null)
			try { result.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
	}
	
	public static void setXP(UUID uuid, double xp) {
		Connection connection = null;
		Statement statement = null;
		
		while(true)
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
			statement.execute(String.format(""
					+ "UPDATE `OpenKomodo.Main` " +
					"SET `XP` = %s" +
					"WHERE `UUID` = UNHEX('%s');", xp, uuid.toString().replaceAll("-", "")));
			
			break;
		} catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
		} finally {
			if (connection != null)
			try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
			try { statement.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
	}
	
	public static double getXP(UUID uuid) {
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		while (true)
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
			result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main`"));
			
			result.next();
			
			double xp = result.getDouble("XP");
			
			return xp;
		} catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
		} finally {
			if (connection != null)
			try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
			try { statement.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (result != null)
			try { result.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
	}
	
	public static void createPlayer (UUID uuid) {
		Connection connection = null;
		Statement statement = null;
		
		while (true)
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
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
			
			break;
		} catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
		} finally {
			if (connection != null)
			try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
			try { statement.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
	}
	
	public static boolean containsWorldPlayer(UUID uuid, String worldName) {
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		while (true)
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
			result = statement.executeQuery(String.format("SELECT `UUID` FROM `OpenKomodo.Worlds.%s` WHERE `UUID` = UNHEX('%s')", worldName, uuid.toString().replaceAll("-", "")));
			
			boolean exists;
			if (result.next())
				exists = true;
			else
				exists = false;
			
			return exists;
		} catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
		} finally {
			if (connection != null)
			try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
			try { statement.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (result != null)
			try { result.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
	}
	
	public static void createWorldPlayer (UUID uuid, String worldName) {
		Connection connection = null;
		Statement statement = null;
		
		while (true)
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
			statement.execute(String.format("INSERT INTO `OpenKomodo.Worlds.%s` ("
					+ "`UUID`, "
					+ "`Inventory`, "
					+ "`Permissions`) VALUES "
					+ "(UNHEX(\"%s\"), '', '')", 
					worldName, uuid.toString().replaceAll("-", "")));
			
			break;
		} catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
		} finally {
			if (connection != null)
			try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
			try { statement.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
	}
	
	public static void setGlobalPermissions(UUID uuid, List<Permission> permissions) {
		Connection connection = null;
		Statement statement = null;
		
		while (true)
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
			connection = ds.getConnection();
			statement = connection.createStatement();
			statement.execute(String.format("UPDATE `OpenKomodo.Main` " +
					"SET `Permissions` = \"%s\"" +
					"WHERE `UUID` = UNHEX('%s');", Base64Coder.encodeLines(byteOutputStream.toByteArray()), uuid.toString().replaceAll("-", "")));
			
			// Close output Stream and SQL statement
			byteOutputStream.close();
			break;
		} catch (SQLException | IOException exception) {
			exception.printStackTrace();
			
			if (exception instanceof IOException) {
				Player player = Bukkit.getPlayer(uuid);
				
				if (player != null)
					player.sendMessage(String.format("%sERROR: IOException, global permissions may be lost", ChatColor.DARK_RED));
				break;
			}
			
			try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
		} finally {
			if (connection != null)
			try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
			try { statement.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
	}
	
	public static List<Permission> getGlobalPermissions(UUID uuid) {
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		while (true)
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
			
			// Retrieve world permissions
			result = statement.executeQuery(String.format("SELECT `UUID`, `Permissions` FROM `OpenKomodo.Main` " +
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
			
			return permissions;
			} catch (SQLException | IOException | ClassNotFoundException exeception) {
				exeception.printStackTrace();
				try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
			} finally {
				if (connection != null)
				try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
				if (statement != null)
				try { statement.close(); } catch(SQLException e) { e.printStackTrace(); }
				if (result != null)
				try { result.close(); } catch(SQLException e) { e.printStackTrace(); }
			}
	}
	
	public static void setWorldPermission(UUID uuid, List<Permission> permissions, String worldName) {
		Connection connection = null;
		Statement statement = null;
		
		while (true)
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
			connection = ds.getConnection();
			statement = connection.createStatement();
			statement.execute(String.format("UPDATE `OpenKomodo.Worlds.%s` " +
					"SET `Permissions` = \"%s\"" +
					"WHERE `UUID` = UNHEX('%s');", worldName, Base64Coder.encodeLines(byteOutputStream.toByteArray()), uuid.toString().replaceAll("-", "")));
			
			// Close output Stream and SQL statement
			byteOutputStream.close();
			break;
			} catch (SQLException | IOException exeception) {
				exeception.printStackTrace();
				try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
			} finally {
				if (connection != null)
				try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
				if (statement != null)
				try { statement.close(); } catch(SQLException e) { e.printStackTrace(); }
			}
	}
	
	public static List<Permission> getWorldPermission(UUID uuid, String worldName) {
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		while (true)
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
			
			// Retrieve world permissions
			result = statement.executeQuery(String.format("SELECT `UUID`, `Permissions` FROM `OpenKomodo.Worlds.%s` " +
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
			
			return permissions;
			} catch (SQLException | IOException | ClassNotFoundException exeception) {
				exeception.printStackTrace();
				
				if (exeception instanceof IOException || exeception instanceof ClassNotFoundException)
					return new ArrayList<Permission>(0);
				
				try {Thread.sleep(delayAmount); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace();}
	        } finally {
				if (connection != null)
	        	try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
				if (statement != null)
		        try {statement.close(); } catch(SQLException e) { e.printStackTrace(); }
				if (result != null)
			        try {result.close(); } catch(SQLException e) { e.printStackTrace(); }
	        }
	}
	
	public static void setInventory(UUID uuid, String worldName, ItemStack[] inventory) {
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		while (true)
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
		
			try {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				BukkitObjectOutputStream objectOutput = new BukkitObjectOutputStream(outputStream);
				
				objectOutput.writeObject(inventory);
				objectOutput.close();
				
				result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Worlds.%s`", worldName));
				
				if (result.next())
					statement.execute(String.format(""
							+ "UPDATE `OpenKomodo.Worlds.%s` " +
							"SET `Inventory` = '%s'" +
							"WHERE `UUID` = UNHEX('%s');", worldName, Base64Coder.encodeLines(outputStream.toByteArray()), uuid.toString().replaceAll("-", "")));
				else
				{
					createWorldPlayer(uuid, worldName);
					setInventory(uuid, worldName, inventory);
				}
				
				break;
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		
		} catch (SQLException exeception) {
			exeception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace();}
        } finally {
			if (connection != null)
        	try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
	        try {statement.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (result != null)
		        try {result.close(); } catch(SQLException e) { e.printStackTrace(); }
        }
	}
	
	public static ItemStack[] getInventory(UUID uuid, String worldName) {
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		while (true)
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
			
			result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Worlds.%s` " +
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
				
				Player player = Bukkit.getPlayer(uuid);
				if (player != null)
					player.sendMessage(String.format("%sError: BukkitObjectInputStream Error for getting inventory, potential inventory loss", ChatColor.DARK_RED));
				
				return new ItemStack[0];
			}
			
			ItemStack[] inventory;
			try {
				inventory = (ItemStack[]) objectInput.readObject();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
				
				Player player = Bukkit.getPlayer(uuid);
				if (player != null)
					player.sendMessage(String.format("%sError: BukkitObjectInputStream.readObject() Error for getting inventory, potential inventory loss", ChatColor.DARK_RED));
				
				return new ItemStack[0];
			}
			
			return inventory;
		} catch (SQLException exeception) {
			exeception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace();}
        } finally {
			if (connection != null)
        	try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
	        try {statement.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (result != null)
		        try {result.close(); } catch(SQLException e) { e.printStackTrace(); }
        }
	}
	
	public static void setTip(UUID uuid, int amount) {
		Connection connection = null;
		Statement statement = null;
		
		while (true)
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
			statement.execute(String.format("UPDATE `OpenKomodo.Main` SET `TIP (USD)` = %d WHERE `UUID` = UNHEX('%s')", amount, uuid.toString().replaceAll("-", "")));
			break;
		} catch (SQLException exception) {
			exception.printStackTrace();
			
			Player player = Bukkit.getPlayer(uuid);
			if (player != null)
				player.sendMessage(String.format("%sUhh, something went wrong. Try contacting support. Error: SQLException#SetTip Time: %s", ChatColor.DARK_RED, Instant.now().toString()));
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace();}
        } finally {
			if (connection != null)
        	try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
	        try {statement.close(); } catch(SQLException e) { e.printStackTrace(); }
        }
	}
	
	public static int getTip(UUID uuid) {
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		while (true)
        try {
        	connection = ds.getConnection();
			// Get Tip in SQL Database
			statement = connection.createStatement();
			result = statement.executeQuery(String.format("SELECT `UUID`, `TIP (USD)` FROM `OpenKomodo.Main` " + 
					"WHERE UUID=UNHEX('%s');", uuid.toString().replaceAll("-", "")));
			
			// If can't find player, put 0
			if (!result.next())
				return 0;
			
			// Get money donated
			int usdMoney = result.getInt("TIP (USD)");
			
			// Return with donation money
			return usdMoney;
        } catch (SQLException exception) {
        	exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace();}
        } finally {
			if (connection != null)
        	try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
	        try {statement.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (result != null)
		        try {result.close(); } catch(SQLException e) { e.printStackTrace(); }
        }
	}
	
	public static int getRankID(UUID uuid) {
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		while (true)
        try {
        	connection = ds.getConnection();
        	
			// Get Player in SQL Database
			statement = connection.createStatement();
			result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main` " + 
					"WHERE UUID=UNHEX('%s');", uuid.toString().replaceAll("-", "")));
			
			result.next();
			
			// Get RANK ID
			int rankID = result.getInt("Rank ID");
			
			// Return with rank ID
			return rankID;
        } catch (SQLException exception) {
        	exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace();}
        } finally {
			if (connection != null)
        	try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
	        try {statement.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (result != null)
		        try {result.close(); } catch(SQLException e) { e.printStackTrace(); }
        }
	}
	
	public static void setRankID(UUID uuid, int rankID) {
		Connection connection = null;
		Statement statement = null;
		
		while (true)
        try {
			// Set rank in SQL
        	connection = ds.getConnection();
			statement = connection.createStatement();
			statement.execute(String.format("UPDATE `OpenKomodo.Main` " + 
					"SET `Rank ID` = %d " +
					"WHERE `UUID` = UNHEX(\"%s\");",  rankID, uuid.toString().replaceAll("-", "")));
			break;
        } catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace();}
        } finally {
			if (connection != null)
        	try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
	        try {statement.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
	}
	
	public static List<Home> getHomes(UUID uuid) {
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		while (true)
        try {
			// Get Homes in SQL Database
        	connection = ds.getConnection();
			statement = connection.createStatement();
			result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main` " +
					"WHERE `UUID` =UNHEX('%s');", uuid.toString().replaceAll("-", "")));
			
			result.next();
			
			if (result.getString("Homes") == "")
				return new ArrayList<Home>(0);
			
			ByteArrayInputStream byteInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(result.getString("Homes")));
			BukkitObjectInputStream objectInputStream = new BukkitObjectInputStream(byteInputStream);
			
			List<Home> homes = (List<Home>) objectInputStream.readObject();
			return homes;
        }
        catch (IOException | SQLException | ClassNotFoundException exception) {
        	exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace();}
        } finally {
			if (connection != null)
        	try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
	        try {statement.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (result != null)
		    try {result.close(); } catch(SQLException e) { e.printStackTrace(); }
        }
        
	}
	
	public static Date getMuteDate(UUID uuid) {
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		while (true)
		try {
			// Get Results in SQL Database
			connection = ds.getConnection();
			statement = connection.createStatement();
			result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main` " +
					"WHERE `UUID` =UNHEX('%s');", uuid.toString().replaceAll("-", "")));
			
			result.next();
			return result.getTimestamp("Mute Date");
		} catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace();}
        } finally {
			if (connection != null)
        	try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
	        try {statement.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (result != null)
		    try {result.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
	}
	
	public static String getMuteReason(UUID uuid) {
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		while (true)
		try {
			// Get Results in SQL Database
			connection = ds.getConnection();
			statement = connection.createStatement();
			result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main` " +
					"WHERE `UUID` =UNHEX('%s');", uuid.toString().replaceAll("-", "")));
			
			result.next();
			
			return result.getString("Mute Reason");
		} catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace();}
        } finally {
			if (connection != null)
        	try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
	        try {statement.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (result != null)
		    try {result.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
	}
	
	public static void setMuteDate(UUID uuid, Date date) {
		Connection connection = null;
		PreparedStatement statement = null;
		
		while (true)
		try {
			// Set date in database
			connection = ds.getConnection();
			
			if (date == null) {
				statement = connection.prepareStatement("UPDATE `OpenKomodo.Main` " +
						"SET `Mute Date` = '1990/01/01' " +
						"WHERE `UUID` = UNHEX('?')");
				
				statement.setString(1, uuid.toString().replaceAll("-", ""));
			}
			else {
				statement = connection.prepareStatement("UPDATE `OpenKomodo.Main` " +
						"SET `Mute Date` = ? " +
						"WHERE `UUID` = UNHEX(?)");
				
				statement.setString(2, uuid.toString().replaceAll("-", ""));
				statement.setTimestamp(1, new Timestamp(date.getTime()));
			}
			
			statement.execute();
			break;
		} catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace();}
        } finally {
			if (connection != null)
        	try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
	        try {statement.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
	}
	
	public static void setMuteReason(UUID uuid, String reason) {
		Connection connection = null;
		Statement statement = null;
		
		while (true)
		try {
			// Set mute reason in database
			connection = ds.getConnection();
			statement = connection.createStatement();
			
			statement.execute(String.format("UPDATE `OpenKomodo.Main` " +
					"SET `Mute Reason` = \"%s\" " +
					"WHERE `UUID` = UNHEX('%s');", reason, uuid.toString().replaceAll("-", "")));
			
			break;
		} catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace();}
        } finally {
			if (connection != null)
        	try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
	        try {statement.close(); } catch(SQLException e) { e.printStackTrace(); }
        }
	}
	
	public static void setBanDate(UUID uuid, Date date) {
		Connection connection = null;
		PreparedStatement statement = null;
		
		while (true)
		try {
			connection = ds.getConnection();
			
			if (date == null) {
				statement = connection.prepareStatement("UPDATE `OpenKomodo.Main` " +
						"SET `Ban Date` = '1990/01/01' " +
						"WHERE `UUID` = UNHEX('?')");
				
				statement.setString(1, uuid.toString().replaceAll("-", ""));
			}
			else {
				statement = connection.prepareStatement("UPDATE `OpenKomodo.Main` " +
						"SET `Ban Date` = ? " +
						"WHERE `UUID` = UNHEX(?)");
				
				statement.setString(2, uuid.toString().replaceAll("-", ""));
				statement.setTimestamp(1, new Timestamp(date.getTime()));
			}
			
			statement.execute();
			break;
		} catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace();}
        } finally {
			if (connection != null)
        	try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
	        try {statement.close(); } catch(SQLException e) { e.printStackTrace(); }
        }
	}
	
	public static Date getBanDate (UUID uuid) {
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		while (true)
		try {
			// Get Results in SQL Database
			connection = ds.getConnection();
			statement = connection.createStatement();
			result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main` " +
					"WHERE `UUID` =UNHEX('%s');", uuid.toString().replaceAll("-", "")));
			
			result.next();
			
			return result.getTimestamp("Ban Date");
		} catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace();}
        } finally {
			if (connection != null)
        	try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
	        try {statement.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (result != null)
		    try {result.close(); } catch(SQLException e) { e.printStackTrace(); }
        }
	}
	
	public static void setBanReason(UUID uuid, String reason) {
		Connection connection = null;
		Statement statement = null;
		
		while (true)
		try {
			// Set ban reason in database
			connection = ds.getConnection();
			statement = connection.createStatement();
			
			if (reason == null)
				statement.execute(String.format("UPDATE `OpenKomodo.Main` " +
						"SET `Ban Reason` = '' " +
						"WHERE `UUID` = UNHEX('%s');", uuid.toString().replaceAll("-", "")));
			else {
				statement.execute(String.format("UPDATE `OpenKomodo.Main` " +
						"SET `Ban Reason` = \"%s\" " +
						"WHERE `UUID` = UNHEX('%s');", reason, uuid.toString().replaceAll("-", "")));
			}
			
			break;
		} catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace();}
        } finally {
			if (connection != null)
        	try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
	        try {statement.close(); } catch(SQLException e) { e.printStackTrace(); }
        }
	}
	
	public static String getBanReason (UUID uuid) {
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		while (true)
		try {
			// Get Results in SQL Database
			connection = ds.getConnection();
			statement = connection.createStatement();
			result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main` " +
					"WHERE `UUID` =UNHEX('%s');", uuid.toString().replaceAll("-", "")));
			
			result.next();
			
			return result.getString("Ban Reason");
		} catch (SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace();}
        } finally {
			if (connection != null)
        	try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
	        try {statement.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (result != null)
		    try {result.close(); } catch(SQLException e) { e.printStackTrace(); }
        }
	}
	
	public static void setHomes(UUID uuid, List<Home> homes) {
		Connection connection = null;
		Statement statement = null;
		
		while (true)
		try {
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream outputStream = new BukkitObjectOutputStream(byteOutputStream);
			
			outputStream.writeObject(homes);
			outputStream.close();
			
			connection = ds.getConnection();
			statement = connection.createStatement();
			
			statement.execute(String.format("UPDATE `OpenKomodo.Main` " + 
					"SET `Homes` = '%s' " +
					"WHERE `UUID` = UNHEX('%s');",  Base64Coder.encodeLines(byteOutputStream.toByteArray()), uuid.toString().replaceAll("-", "")));
			
			break;
		} catch (IOException | SQLException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace();}
        } finally {
			if (connection != null)
        	try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
	        try {statement.close(); } catch(SQLException e) { e.printStackTrace(); }
        }
		
	}
	
	public static int getCurrency(UUID uuid, Currency type) {
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		while (true)
        try {
        	connection = ds.getConnection();
			// Get Player in SQL Database
			statement = connection.createStatement();
			result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main` " +
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
			
			return amount;
        } catch (SQLException e) {
			e.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace();}
        } finally {
			if (connection != null)
        	try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
	        try {statement.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (result != null)
		    try {result.close(); } catch(SQLException e) { e.printStackTrace(); }
        }
	}
	
	public static void setCurrency(UUID uuid, int amount, Currency type) {
		Connection connection = null;
		Statement statement = null;
		
		while (true)
        try {
			// Create statement to run SQL commands
        	connection = ds.getConnection();
			statement = connection.createStatement();
			
			
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
			break;
        } catch (SQLException e) {
			e.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException interruptedException) { interruptedException.printStackTrace();}
        } finally {
			if (connection != null)
        	try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
	        try {statement.close(); } catch(SQLException e) { e.printStackTrace(); }
        }
	}
	
	public static void setItems(CustomPlayer player) {
		List<CustomItem> items = player.getItems();
		
		JSONArray json = new JSONArray();
		for (CustomItem item : items)
			json.add(item.getID());
		
		Connection connection = null;
		Statement statement = null;
		while (true)
		try {
			// Create statement to run SQL commands
			connection = ds.getConnection();
			statement = connection.createStatement();
			
			statement.execute(String.format("UPDATE `OpenKomodo.Main` " +
					"SET `Items` = '%s' " +
					"WHERE `UUID` = UNHEX('%s');", json.toString(), player.getUniqueId().toString().replaceAll("-", "")));
			break;
			} catch (SQLException exception) {
				exception.printStackTrace();
				try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
			} finally {
				if (connection != null)
		        try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
				if (statement != null)
			    try {statement.close(); } catch(SQLException e) { e.printStackTrace(); }
			}
	}
	
	public static List<Pet> getPets(UUID uuid) {
		Connection connection = null;
		Statement statement = null;
		ResultSet result = null;
		
		while (true)
		try {
			connection = ds.getConnection();
			statement = connection.createStatement();
			result = statement.executeQuery(String.format("SELECT * FROM `OpenKomodo.Main` " +
					"WHERE `UUID` =UNHEX('%s');", uuid.toString().replaceAll("-", "")));
			
			result.next();
			
			if (result.getString("Pets") == "") {
				result.close();
				statement.close();
				connection.close();
				
				return new ArrayList<Pet>(0);
			}
			
			ByteArrayInputStream byteInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(result.getString("Pets")));
			ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);
			
			List<Integer> idList = (List<Integer>) objectInputStream.readObject();
			
			List<Pet> ownedPets = new ArrayList<Pet>(idList.size());
			for (int id : idList)
				ownedPets.add(Pet.getPet(id));
			
			return ownedPets;
		} catch (SQLException | ClassNotFoundException | IOException exception) {
			exception.printStackTrace();
			try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
		} finally {
			if (connection != null)
        	try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (statement != null)
	        try {statement.close(); } catch(SQLException e) { e.printStackTrace(); }
			if (result != null)
		    try { result.close(); } catch(SQLException e) { e.printStackTrace(); }
		}
	}
	
	public static void setPets(CustomPlayer customPlayer) {
			Connection connection = null;
			Statement statement = null;
			
			while (true)
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
				connection = ds.getConnection();
				statement = connection.createStatement();
				
				statement.execute(String.format("UPDATE `OpenKomodo.Main` " +
						"SET `Pets` = '%s' " +
						"WHERE `UUID` = UNHEX('%s');", base64, customPlayer.getUniqueId().toString().replaceAll("-", "")));
				break;
			} catch (SQLException | IOException exception) {
				exception.printStackTrace();
				try {Thread.sleep(delayAmount); } catch (InterruptedException e) { e.printStackTrace();}
			} finally {
				if (connection != null)
	        	try { connection.close(); } catch(SQLException e) { e.printStackTrace(); }
				if (statement != null)
		        try { statement.close(); } catch(SQLException e) { e.printStackTrace(); }
			}
		}
	
	public static void disconnectSQL() {
		//if (getConnection() == null)
			//return;
		
		try {
			//getConnection().close();
			ds.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean isEnabled() {
		return sqlInfo.enabled;
	}
}

class Items{
	 public List<Integer> list;
	 //getter and setter
	}