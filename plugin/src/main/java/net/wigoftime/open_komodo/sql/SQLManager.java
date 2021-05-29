package net.wigoftime.open_komodo.sql;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
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
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.*;
import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.objects.*;
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

import net.md_5.bungee.api.chat.BaseComponent;
import net.wigoftime.open_komodo.config.Config;
import net.wigoftime.open_komodo.custommobs.CustomPet;
import net.wigoftime.open_komodo.sql.SQLCard.SQLCardType;
import net.wigoftime.open_komodo.sql.SQLCode.SQLCodeType;

abstract public class SQLManager {
	private static final int delayAmount = 5000;
	private static SQLInfo sqlInfo = Config.getSQLInfo();
	
	public static void setup() {
		createMainTable();
		setUpWorlds(Bukkit.getWorlds());
		createModerationTable();
	}
	
	public static void setUpWorlds(List<World> worlds) {
		for (World world : worlds) {
			createWorldTable(world.getName());
		}
	}
	
	private static void createWorldTable(String worldName) {
		new SQLCard(SQLCodeType.CREATE_WORLD_TABLE, SQLCardType.SET, Arrays.asList(worldName)).execute();
	}
	
	public static List<CustomItem> getItems(UUID uuid) {
		JSONArray array;
		
		JSONParser parser = new JSONParser();
		try {
			array = (JSONArray) parser.parse((String) new SQLCard(SQLCodeType.GET_ITEMS, SQLCardType.GET, Arrays.asList(uuid.toString().replaceAll("-", ""))).execute().get(0));
		} catch (ParseException e) {
			e.printStackTrace();
			return new ArrayList<CustomItem>(0);
		}
		List<CustomItem> list = new ArrayList<CustomItem>(array.size());
		
		Iterator arrayIterator = array.iterator();
		while (arrayIterator.hasNext()) list.add(CustomItem.getCustomItem(((Long) arrayIterator.next()).intValue()));
		return list;
	}
	
	public static void setItems(CustomPlayer player) {
		List<CustomItem> items = player.getItems();
		JSONArray array = new JSONArray();
		for (CustomItem item : items) array.add(item.getID());
		new SQLCard(SQLCodeType.SET_ITEMS, SQLCardType.SET, Arrays.asList(array.toJSONString(), player.getUniqueId().toString().replaceAll("-", ""))).execute();
	}
	
	private static void setupBagInventories() {
		for (World world : Bukkit.getWorlds())
			createBagInventoryTable(world.getName());
	}
	
	private static void createMainTable() {
		new SQLCard(SQLCodeType.CREATE_MAIN_TABLE, SQLCardType.SET, Arrays.asList()).execute();
	}
	
	private static void createModerationTable() {
		new SQLCard(SQLCodeType.CREATE_MODERATION_TABLE, SQLCardType.SET, Arrays.asList()).execute();
	}
	
	private static void createBagInventoryTable(String worldName) {
		new SQLCard(SQLCodeType.CREATE_BAG_INVENTORY_TABLE, SQLCardType.SET, Arrays.asList(worldName)).execute();
	}
	
	public static boolean containsPlayer(UUID uuid) {
		return new SQLCard(SQLCodeType.CONTAINS_PLAYER, SQLCardType.GET, Arrays.asList(uuid.toString().replaceAll("-",""))).execute().size() < 1 ? false : true;
	}
	
	public static boolean containsModerationPlayer(UUID uuid) {
		SQLCard card = new SQLCard(SQLCodeType.CONTAINS_MODERATION_PLAYER, SQLCardType.GET, Arrays.asList(uuid.toString().replaceAll("-","")));
		
		// If Player can't be found in world database
		if (card.execute().isEmpty())
			return false;
		
		return true;
	}
	
	public static boolean containsWorldPlayer(UUID uuid, String worldName) {
		SQLCard card = new SQLCard(SQLCodeType.CONTAINS_WORLD_PLAYER, SQLCardType.GET, Arrays.asList(worldName, uuid.toString().replaceAll("-","")));
		
		// If Player can't be found in world database
		if (card.execute().isEmpty())
			return false;
		
		return true;
	}
	
	public static int createBagInventory(UUID uuid, String worldName) {
		List<Object> sqlElements = new SQLCard(SQLCodeType.GET_LATEST_BAGID, SQLCardType.GET, Arrays.asList(worldName, uuid.toString().replaceAll("-", ""))).execute();
		int id;
		
		if (sqlElements.isEmpty())
			id = 1;
		else {
		id = (int) (long) sqlElements.get(0);
		id++;
		}
		
		new SQLCard(SQLCodeType.CREATE_BAG_INVENTORY, SQLCardType.SET, Arrays.asList(worldName, uuid.toString().replaceAll("-", ""), id)).execute();
		return id;
	}
	
	public static List<ItemStack> getBagInventory(UUID uuid, String worldName, int id) {
		String serializedInventory = (String) new SQLCard(SQLCodeType.GET_BAG_INVENTORY, SQLCardType.GET, Arrays.asList(worldName, uuid.toString().replaceAll("-", ""), id)).execute().get(0);
		
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(serializedInventory));
		BukkitObjectInputStream objectInputStream = null;
		try {
			objectInputStream = new BukkitObjectInputStream(byteInputStream);
			return (List<ItemStack>) objectInputStream.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			Bukkit.getPlayer(uuid).sendMessage(ChatColor.DARK_RED + "Error: ClassNotFoundException. Cannot open bag");
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (objectInputStream != null) try { objectInputStream.close(); } catch (IOException e) {e.printStackTrace();}
			if (byteInputStream != null) try { byteInputStream.close(); } catch (IOException e) {e.printStackTrace();}
		}
	}
	
	public static void setBagInventory(UUID uuid, String worldName, int id, List<ItemStack> inventory) {
		ByteArrayOutputStream byteInputStream = new ByteArrayOutputStream();
		BukkitObjectOutputStream objectInputStream = null;
		try {
			objectInputStream = new BukkitObjectOutputStream(byteInputStream);
			objectInputStream.writeObject(inventory);
			
			new SQLCard(SQLCodeType.SET_BAG_INVENTORY, SQLCardType.SET, Arrays.asList(worldName, Base64Coder.encodeLines(byteInputStream.toByteArray()), uuid.toString().replaceAll("-", ""), id)).execute();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (objectInputStream != null) try { objectInputStream.close(); } catch (IOException e) {e.printStackTrace();}
			if (byteInputStream != null) try { byteInputStream.close(); } catch (IOException e) {e.printStackTrace();}
		}
	}
	
	public static void createPlayer(UUID uuid) {
		LocalDate date = LocalDate.now();
		new SQLCard(SQLCodeType.CREATE_PLAYER, SQLCardType.SET, Arrays.asList(uuid.toString().replaceAll("-", ""), String.format("%04d/%02d/%02d", date.getYear(), date.getMonth().getValue(), date.getDayOfMonth()))).execute();
	}
	
	public static void createModerationPlayer(UUID uuid) {
		new SQLCard(SQLCodeType.CREATE_MODERATION_PLAYER, SQLCardType.SET, Arrays.asList(uuid.toString().replaceAll("-", ""))).execute();
	}
	
	public static void createWorldPlayer(UUID uuid, String worldName) {
		new SQLCard(SQLCodeType.CREATE_WORLD_PLAYER, SQLCardType.SET, Arrays.asList(worldName, uuid.toString().replaceAll("-", ""))).execute();
	}
	
	public static Date getBanDate(UUID uuid) {
		return Date.from(((Timestamp) new SQLCard(SQLCodeType.GET_BANDATE, SQLCardType.GET, Arrays.asList(uuid.toString().replaceAll("-", ""))).execute().get(0)).toInstant());
	}
	
	public static void setBanDate(UUID uuid, Date date) {
		new SQLCard(SQLCodeType.SET_BANDATE, SQLCardType.SET, Arrays.asList(Timestamp.from(date.toInstant()).toString() ,uuid.toString().replaceAll("-", ""))).execute();
	}
	
	public static String getBanReason(UUID uuid) {
		return (String) new SQLCard(SQLCodeType.GET_BANREASON, SQLCardType.GET, Arrays.asList(uuid.toString().replaceAll("-", ""))).execute().get(0);
	}
	
	public static void setBanReason(UUID uuid, String reason) {
		new SQLCard(SQLCodeType.SET_BANREASON, SQLCardType.SET, Arrays.asList(reason, uuid.toString().replaceAll("-", ""))).execute();
	}
	
	public static void setNickName(UUID uuid, String nickname) {
		new SQLCard(SQLCodeType.SET_NICKNAME, SQLCardType.SET, Arrays.asList(nickname, uuid.toString().replaceAll("-", ""))).execute();
	}
	
	public static BaseComponent[] getNickName(Player player) {
		final String nicknameRaw = (String) new SQLCard(SQLCodeType.GET_NICKNAME, SQLCardType.GET, Arrays.asList(player.getUniqueId().toString().replaceAll("-", ""))).execute().get(0);
		if (nicknameRaw.length() == 0) return null;
		BaseComponent[] nickname;
		nickname = NickName.translateRGBColorCodes('#', '&', nicknameRaw);
		
		return nickname;
	}

	public static List<MailWrapper> getMail(UUID receiver) {
		List<Object> objects = new SQLCard(SQLCodeType.GET_MAIL, SQLCardType.GET, Arrays.asList(receiver.toString().replaceAll("-", ""))).execute();

		int objectIndex = 0;
		LinkedList<MailWrapper> mail = new LinkedList<MailWrapper>();

		int index = 0;
		while (objectIndex < objects.size() && index <= MailSystem.mailMax) {
			ByteBuffer uuidBuffer = ByteBuffer.wrap((byte[]) objects.get(objectIndex+1));
			long high = uuidBuffer.getLong();
			long low = uuidBuffer.getLong();

			UUID senderUUID = new UUID(high, low);

			mail.add(new MailWrapper(senderUUID, ((java.sql.Timestamp) objects.get(objectIndex + 2)).toLocalDateTime(), (String) objects.get(objectIndex + 3)));
			objectIndex = objectIndex + 4;
			index++;
		}
		return mail;
	}

	public static void clearMail(UUID playerUUID) {
		new SQLCard(SQLCodeType.CLEAR_MAIL, SQLCardType.SET, Arrays.asList(playerUUID.toString().replaceAll("-", ""))).execute();
	}

	public static int countMail(UUID playerUUID) {
		return (int) (long) new SQLCard(SQLCodeType.COUNT_MAIL, SQLCardType.GET, Arrays.asList(playerUUID.toString().replaceAll("-",""))).execute().get(0);
	}

	public static void sendMail(UUID recipientUUID, UUID senderUUID, String message) {
		new SQLCard(SQLCodeType.SEND_MAIL, SQLCardType.SET, Arrays.asList(recipientUUID.toString().replaceAll("-",""), senderUUID.toString().replaceAll("-", "") , Timestamp.from(Calendar.getInstance().toInstant()),message)).execute();
	}

	public static Date getMuteDate(UUID uuid) {
		return Date.from(((Timestamp) new SQLCard(SQLCodeType.GET_MUTEDATE, SQLCardType.GET, Arrays.asList(uuid.toString().replaceAll("-", ""))).execute().get(0)).toInstant());
	}
	
	public static void setMuteDate(UUID uuid, Date date) {
		new SQLCard(SQLCodeType.SET_MUTEDATE, SQLCardType.SET, Arrays.asList(Timestamp.from(date.toInstant()).toString(),uuid.toString().replaceAll("-", ""))).execute();
	}
	
	public static String getMuteReason(UUID uuid) {
		return (String) new SQLCard(SQLCodeType.GET_MUTEREASON, SQLCardType.GET, Arrays.asList(uuid.toString().replaceAll("-", ""))).execute().get(0);
	}
	
	public static void setMuteReason(UUID uuid, String reason) {
		new SQLCard(SQLCodeType.SET_MUTEREASON, SQLCardType.SET, Arrays.asList(reason, uuid.toString().replaceAll("-", ""))).execute();
	}
	
	public static Date getJoinDate(UUID uuid) {
		return ((Date) new SQLCard(SQLCodeType.GET_JOINDATE, SQLCardType.GET, Arrays.asList(uuid.toString().replaceAll("-", ""))).execute().get(0));
	}
	
	public static int getCurrency(UUID uuid, Currency currency) {
		if (currency == Currency.POINTS)
		return (int) (long) new SQLCard(SQLCodeType.GET_POINTS, SQLCardType.GET, Arrays.asList(uuid.toString().replaceAll("-", ""))).execute().get(0);
		else if (currency == Currency.COINS)
		return (int) (long) new SQLCard(SQLCodeType.GET_COINS, SQLCardType.GET, Arrays.asList(uuid.toString().replaceAll("-", ""))).execute().get(0);
		else
			return 0;
	}
	
	public static void setCurrency(UUID uuid, int amount, Currency currency) {
		if (currency == Currency.POINTS)
		new SQLCard(SQLCodeType.SET_POINTS, SQLCardType.SET, Arrays.asList(amount, uuid.toString().replaceAll("-", ""))).execute();
		else if (currency == Currency.COINS)
		new SQLCard(SQLCodeType.SET_COINS, SQLCardType.SET, Arrays.asList(amount, uuid.toString().replaceAll("-", ""))).execute();
	}
	
	public static void setCurrency(UUID uuid, Currency currency, int amount) {
		if (currency == Currency.POINTS)
		new SQLCard(SQLCodeType.SET_POINTS, SQLCardType.SET, Arrays.asList(amount, uuid.toString().replaceAll("-", ""))).execute().get(0);
		else if (currency == Currency.COINS)
		new SQLCard(SQLCodeType.GET_COINS, SQLCardType.SET, Arrays.asList(amount, uuid.toString().replaceAll("-", ""))).execute().get(0);
	}
	
	public static List<Permission> getGlobalPermissions(UUID uuid) {
		SQLCard card = new SQLCard(SQLCodeType.GET_GLOBAL_PERMISSIONS, SQLCardType.GET, Arrays.asList(uuid.toString().replaceAll("-", "")));
		if (card.execute().size() < 1)
			return new ArrayList<Permission>(0);
		
		String serialized = (String) card.execute().get(0);

		// Deserialize permissions to a list of permissions
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(serialized));
		BukkitObjectInputStream objectInputStream = null;
		
		try {
			objectInputStream = new BukkitObjectInputStream(byteInputStream);
			List<String> permissionsInString = (List<String>) objectInputStream.readObject();
			List<Permission> permissions = new ArrayList<Permission>(permissionsInString.size());
			
			Iterator<String> iterator = permissionsInString.iterator();
			while (iterator.hasNext())
				permissions.add(new Permission(iterator.next()));
			
			return permissions;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			setGlobalPermissions(uuid, new LinkedList<Permission>());
			return new LinkedList<Permission>();
		} finally {
			if (objectInputStream != null) try { objectInputStream.close(); } catch (IOException e) { e.printStackTrace(); }
			if (byteInputStream != null) try { byteInputStream.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}	
	
	public static void setGlobalPermissions(UUID uuid, List<Permission> permissions) {
		// Serialize permissions to a list of permissions
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		BukkitObjectOutputStream objectOutputStream = null;
		
		// Convert List of permissions to a List of String
		List<String> permissionNodes = new ArrayList<String>(permissions.size());
		Iterator<Permission> iterator = permissions.iterator();
		while (iterator.hasNext()) {
			permissionNodes.add(iterator.next().getName());
		}
		
		// Serialize list of permissions
		try {
			objectOutputStream = new BukkitObjectOutputStream(byteOutputStream);
			objectOutputStream.writeObject(permissionNodes);
			
			// Save serialized list of permissions in Database
			new SQLCard(SQLCodeType.SET_GLOBAL_PERMISSIONS, SQLCardType.SET, Arrays.asList(Base64Coder.encodeLines(byteOutputStream.toByteArray()), uuid.toString().replaceAll("-", ""))).execute();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (objectOutputStream != null) try { objectOutputStream.close(); } catch (IOException e) { e.printStackTrace(); }
			if (byteOutputStream != null) try { byteOutputStream.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}	
	
	public static List<Permission> getWorldPermission(UUID uuid, String worldName) {
		// Get world permissions from database
		SQLCard card = new SQLCard(SQLCodeType.GET_WORLD_PERMISSIONS, SQLCardType.GET, Arrays.asList(worldName, uuid.toString().replaceAll("-", "")));
		
		// Return new empty permissions if database entry doesn't exist
		if (card.execute().size() < 1)
			return new ArrayList<Permission>(0);
		
		String serialized = (String) card.execute().get(0);
		
		// Deserialize to a list of permissions
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(serialized));
		BukkitObjectInputStream objectInputStream = null;
		try {
			objectInputStream = new BukkitObjectInputStream(byteInputStream);
			List<String> permissionsInString = (List<String>) objectInputStream.readObject();
			
			// Convert list of String to list of Permissions
			List<Permission> permissions = new ArrayList<Permission>(permissionsInString.size());
			Iterator<String> iterator = permissionsInString.iterator();
			while (iterator.hasNext())
				permissions.add(new Permission(iterator.next()));
			
			return permissions;
		}catch (IOException e) {
			
			setWorldPermission(uuid, new LinkedList<Permission>(), worldName);
			return new ArrayList<Permission>(0);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (objectInputStream != null) try { objectInputStream.close(); } catch (IOException e) { e.printStackTrace(); }
			if (byteInputStream != null) try { byteInputStream.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}	
	
	public static void setWorldPermission(UUID uuid, List<Permission> permissions, String worldName) {
		// Convert list of permissions to a list of string
		List<String> permissionNodes = new ArrayList<String>(permissions.size());
		
		Iterator<Permission> iterator = permissions.iterator();
		while (iterator.hasNext())
			permissionNodes.add(iterator.next().getName());
		
		// Serialized world permissions
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		BukkitObjectOutputStream objectOutputStream = null;
		try {
			objectOutputStream = new BukkitObjectOutputStream(byteOutputStream);
			objectOutputStream.writeObject(permissionNodes);
			
			// Save serialized list to database
			new SQLCard(SQLCodeType.SET_WORLD_PERMISSIONS, SQLCardType.SET, Arrays.asList(worldName, Base64Coder.encodeLines(byteOutputStream.toByteArray()), uuid.toString().replaceAll("-", ""))).execute();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
			if (objectOutputStream != null) try { objectOutputStream.close(); } catch (IOException e) { e.printStackTrace(); }
			if (byteOutputStream != null) try { byteOutputStream.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}	
	
	public static List<Home> getHomes(UUID uuid) {
		// Get serialized list from database
		String serialized = (String) new SQLCard(SQLCodeType.GET_HOMES, SQLCardType.GET, Arrays.asList(uuid.toString().replaceAll("-", ""))).execute().get(0);
		
		// Deserialize list
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(serialized));
		BukkitObjectInputStream objectInputStream = null;
		try {
			objectInputStream = new BukkitObjectInputStream(byteInputStream);
			List<Home> homes = (List<Home>) objectInputStream.readObject();
			
			return homes;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return new ArrayList<Home>(0);
		} finally {
			if (objectInputStream != null) try { objectInputStream.close(); } catch (IOException e) { e.printStackTrace(); }
			if (byteInputStream != null) try { byteInputStream.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	public static void setHomes(UUID uuid, List<Home> homes) {
		// Serialized home list
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		BukkitObjectOutputStream objectOutputStream = null;
		try {
			objectOutputStream = new BukkitObjectOutputStream(byteOutputStream);
			objectOutputStream.writeObject(homes);
			
			// Save Serialized home list to database
			new SQLCard(SQLCodeType.SET_HOMES, SQLCardType.SET, Arrays.asList(Base64Coder.encodeLines(byteOutputStream.toByteArray()), uuid.toString().replaceAll("-", ""))).execute();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (objectOutputStream != null) try { objectOutputStream.close(); } catch (IOException e) { e.printStackTrace(); }
			if (byteOutputStream != null) try { byteOutputStream.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	public static ItemStack[] getInventory(UUID uuid, String worldName) {
		// Get Seralized Inventory from Database
		String serialized = (String) new SQLCard(SQLCodeType.GET_INVENTORY, SQLCardType.GET, Arrays.asList(worldName, uuid.toString().replaceAll("-", ""))).execute().get(0);
		
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(serialized));
		BukkitObjectInputStream objectInputStream = null;
		
		// Deseralize inventory
		try {
			objectInputStream = new BukkitObjectInputStream(byteInputStream);
			ItemStack[] inventory = (ItemStack[]) objectInputStream.readObject();
			
			return inventory;
		} 
		// If error, print out error and give player a new, empty inventory
		catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return new ItemStack[27];
		} finally {
			if (objectInputStream != null) try { objectInputStream.close(); } catch (IOException e) { e.printStackTrace(); }
			if (byteInputStream != null) try { byteInputStream.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	public static void setInventory(UUID uuid, String worldName, ItemStack[] inventory) {
		// Seralize inventory
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		BukkitObjectOutputStream outputStream = null;
		try {
			// Serialize inventory
			outputStream = new BukkitObjectOutputStream(byteOutputStream);
			outputStream.writeObject(inventory);
			
			// Save inventory in the SQL database
			new SQLCard(SQLCodeType.SET_INVENTORY, SQLCardType.SET, Arrays.asList(worldName, Base64Coder.encodeLines(byteOutputStream.toByteArray()), uuid.toString().replaceAll("-", ""))).execute();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) try { outputStream.close(); } catch (IOException e) { e.printStackTrace(); }
			if (byteOutputStream != null) try { byteOutputStream.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	public static int getRankID(UUID uuid) {
		return (int) new SQLCard(SQLCodeType.GET_RANKID, SQLCardType.GET, Arrays.asList(uuid.toString().replaceAll("-", ""))).execute().get(0);
	}
	
	public static void setRankID(UUID uuid, int rankID) {
		new SQLCard(SQLCodeType.SET_RANKID, SQLCardType.SET, Arrays.asList(rankID, uuid.toString().replaceAll("-", ""))).execute();
	}
	
	public static float getTip(UUID uuid) {
		return ((BigDecimal) new SQLCard(SQLCodeType.GET_TIP, SQLCardType.GET, Arrays.asList(uuid.toString().replaceAll("-", ""))).execute().get(0)).floatValue();
	}
	
	public static void setTip(UUID uuid, float amount) {
		new SQLCard(SQLCodeType.SET_TIP, SQLCardType.SET, Arrays.asList(amount , uuid.toString().replaceAll("-", ""))).execute();
	}
	
	public static double getXP(UUID uuid) {
		return ((BigDecimal) new SQLCard(SQLCodeType.GET_XP, SQLCardType.GET, Arrays.asList(uuid.toString().replaceAll("-", ""))).execute().get(0)).doubleValue();
	}
	
	public static void setXP(UUID uuid, double amount) {
		new SQLCard(SQLCodeType.SET_XP, SQLCardType.SET, Arrays.asList(amount, uuid.toString().replaceAll("-", ""))).execute();
	}
	
	public static List<Pet> getPets(UUID uuid) {
		// Get serialized list of pet IDs
		String serialized = (String) new SQLCard(SQLCodeType.GET_PETS, SQLCardType.GET, Arrays.asList(uuid.toString().replaceAll("-", ""))).execute().get(0);
		
		// Deserialized list of pet IDs
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(Base64Coder.decodeLines(serialized));
		BukkitObjectInputStream objectInputStream = null;
		try {
			objectInputStream = new BukkitObjectInputStream(byteInputStream);
			List<Integer> idList = (List<Integer>) objectInputStream.readObject();
			
			// Convert list of IDs to list of Pets
			List<Pet> pets = new ArrayList<Pet>(idList.size());
			for (int id : idList)
				pets.add(Pet.getPet(id));
			
			return pets;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return new ArrayList<Pet>(0);
		} finally {
			if (objectInputStream != null) try { objectInputStream.close(); } catch (IOException e) { e.printStackTrace(); }
			if (byteInputStream != null) try { byteInputStream.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	public static void setPets(CustomPlayer player) {
		List<Pet> pets = player.getPets();
		
		// Convert list of pets to list of pet IDs
		List<Integer> idList = new ArrayList<Integer>(pets.size());
		for (Pet pet : pets)
			idList.add(pet.getID());
		
		// Serialize list of pet ID
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		BukkitObjectOutputStream objectOutputStream = null;
		try {
			objectOutputStream = new BukkitObjectOutputStream(byteOutputStream);
			objectOutputStream.writeObject(idList);
			
			// Save list of pet IDs (serialized) to database
			new SQLCard(SQLCodeType.SET_PETS, SQLCardType.SET, Arrays.asList(Base64Coder.encodeLines(byteOutputStream.toByteArray()), player.getUniqueId().toString().replaceAll("-", ""))).execute();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (objectOutputStream != null) try { objectOutputStream.close(); } catch (IOException e) { e.printStackTrace(); }
			if (byteOutputStream != null) try { byteOutputStream.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	public static boolean isEnabled() {
		return sqlInfo.enabled;
	}
	
	public static void disconnectSQL() {
		SQLCard.disconnect();
	}
}

class Items{
	 public List<Integer> list;
	 //getter and setter
	}