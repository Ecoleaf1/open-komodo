package net.wigoftime.open_komodo.sql;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.etc.systems.MailSystem;
import net.wigoftime.open_komodo.etc.systems.NicknameSystem;
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
import net.wigoftime.open_komodo.sql.SQLCode.SQLCodeType;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

abstract public class SQLManager {
	private static final int delayAmount = 5000;
	private static SQLInfo sqlInfo = Config.getSQLInfo();
	
	public static void setup() {
		createMainTable();
		setUpWorlds(Bukkit.getWorlds());
		createModerationTable();
		createMailTable();
		createBagInventoryTable();
		createMarriageTable();
	}
	
	public static void setUpWorlds(List<World> worlds) {
		for (World world : worlds) {
			createWorldTable(world.getName());
		}
	}
	
	private static void createWorldTable(String worldName) {
		new SQLCard(SQLCodeType.CREATE_WORLD_TABLE, SQLCard.SQLCardType.SET, Arrays.asList(worldName), Arrays.asList()).execute();
	}

	private static void createMailTable() {
		new SQLCard(SQLCodeType.CREATE_MAIL_TABLE, SQLCard.SQLCardType.SET, Arrays.asList(), Arrays.asList()).execute();
	}
	
	public static List<CustomItem> getItems(UUID uuid) {
		Blob playerBlob = uuidToBlob(uuid);
		JSONArray array;
		//Blob uuidBlob = uuidTo
		
		JSONParser parser = new JSONParser();
		try {
			array = (JSONArray) parser.parse((String) new SQLCard(SQLCodeType.GET_ITEMS, SQLCard.SQLCardType.GET,
					Arrays.asList(),
					Arrays.asList(playerBlob)).execute().get(0));
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
		Blob playerBlob = uuidToBlob(player.getUniqueId());
		List<CustomItem> items = player.getItems();
		JSONArray array = new JSONArray();
		for (CustomItem item : items) array.add(item.getID());
		new SQLCard(SQLCodeType.SET_ITEMS, SQLCard.SQLCardType.SET, Arrays.asList(),
				Arrays.asList(array.toJSONString(), playerBlob)).execute();
	}
	
	private static void createMainTable() {
		new SQLCard(SQLCodeType.CREATE_MAIN_TABLE, SQLCard.SQLCardType.SET,
				Arrays.asList(),
				Arrays.asList()).execute();
	}
	
	private static void createModerationTable() {
		new SQLCard(SQLCodeType.CREATE_MODERATION_TABLE, SQLCard.SQLCardType.SET,
				Arrays.asList(), Arrays.asList()).execute();
	}
	
	private static void createBagInventoryTable() {
		new SQLCard(SQLCodeType.CREATE_BAG_INVENTORY_TABLE, SQLCard.SQLCardType.SET, Arrays.asList(), Arrays.asList()).execute();
	}
	
	public static boolean containsPlayer(UUID uuid) {
		Blob playerBlob = uuidToBlob(uuid);

		return new SQLCard(SQLCodeType.CONTAINS_PLAYER, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(playerBlob)).execute().size() < 1 ? false : true;
	}
	
	public static boolean containsModerationPlayer(UUID uuid) {
		Blob playerBlob = uuidToBlob(uuid);

		SQLCard card = new SQLCard(SQLCodeType.CONTAINS_MODERATION_PLAYER, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(playerBlob));
		
		// If Player can't be found in world database
		if (card.execute().isEmpty())
			return false;
		
		return true;
	}
	
	public static boolean containsWorldPlayer(UUID uuid, String worldName) {
		Blob playerBlob = uuidToBlob(uuid);

		SQLCard card = new SQLCard(SQLCodeType.CONTAINS_WORLD_PLAYER, SQLCard.SQLCardType.GET, Arrays.asList(worldName),
				Arrays.asList(playerBlob));
		
		// If Player can't be found in world database
		if (card.execute().isEmpty())
			return false;
		
		return true;
	}
	
	public static int createBagInventory(UUID uuid) {
		Blob playerBlob = uuidToBlob(uuid);

		List<Object> sqlElements = new SQLCard(SQLCodeType.GET_LATEST_BAGID, SQLCard.SQLCardType.GET,
				Arrays.asList(), Arrays.asList(playerBlob)).execute();
		int id = (int) (long) sqlElements.get(0);

		new SQLCard(SQLCodeType.CREATE_BAG_INVENTORY, SQLCard.SQLCardType.SET, Arrays.asList(),
				Arrays.asList(playerBlob, id)).execute();
		return id;
	}
	
	public static List<ItemStack> getBagInventory(UUID uuid, int id) {
		Blob playerBlob = uuidToBlob(uuid);

		byte[] serializedBlob = (byte[]) new SQLCard(SQLCodeType.GET_BAG_INVENTORY, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(playerBlob, id)).execute().get(0);

		if (serializedBlob.length < 1) return new ArrayList<ItemStack>(27);

		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(serializedBlob);
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
	
	public static void setBagInventory(UUID uuid, int id, List<ItemStack> inventory) {
		ByteArrayOutputStream byteInputStream = new ByteArrayOutputStream();
		BukkitObjectOutputStream objectInputStream = null;
		try {
			objectInputStream = new BukkitObjectOutputStream(byteInputStream);
			objectInputStream.writeObject(inventory);

			Blob playerBlob = uuidToBlob(uuid);
			Blob inventoryBlob = new SerialBlob(byteInputStream.toByteArray());
			new SQLCard(SQLCodeType.SET_BAG_INVENTORY, SQLCard.SQLCardType.SET, Arrays.asList(),
					Arrays.asList((inventoryBlob), playerBlob, id)).execute();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SerialException throwables) {
			throwables.printStackTrace();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} finally {
			if (objectInputStream != null) try { objectInputStream.close(); } catch (IOException e) {e.printStackTrace();}
			if (byteInputStream != null) try { byteInputStream.close(); } catch (IOException e) {e.printStackTrace();}
		}
	}
	
	public static void createPlayer(UUID uuid) {
		Blob playerBlob = uuidToBlob(uuid);
		Blob permissionsBlob, petsBlob, homeBlob;

		ByteArrayOutputStream byteArrayOutputStream = null;
		ObjectOutputStream objectOutputStream = null ;
		try {
			{
				byteArrayOutputStream = new ByteArrayOutputStream();
				objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

				objectOutputStream.writeObject(new LinkedList<String>());

				permissionsBlob = new SerialBlob(byteArrayOutputStream.toByteArray());
			}
			{
				byteArrayOutputStream = new ByteArrayOutputStream();
				objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

				objectOutputStream.writeObject(new LinkedList<Integer>());

				petsBlob = new SerialBlob(byteArrayOutputStream.toByteArray());
			}
			{
				byteArrayOutputStream = new ByteArrayOutputStream();
				objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

				objectOutputStream.writeObject(new LinkedList<Home>());

				homeBlob = new SerialBlob(byteArrayOutputStream.toByteArray());
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			return;
		} finally {
			if (objectOutputStream != null) try { objectOutputStream.close(); } catch (IOException exception) { exception.printStackTrace(); };
			if (byteArrayOutputStream != null) try { byteArrayOutputStream.close(); } catch (IOException exception) { exception.printStackTrace(); };
		}


		LocalDateTime date = LocalDateTime.now();
		new SQLCard(SQLCodeType.CREATE_PLAYER, SQLCard.SQLCardType.SET,
				Arrays.asList(),
				Arrays.asList(playerBlob, permissionsBlob, petsBlob, homeBlob, date)).execute();

	}
	
	public static void createModerationPlayer(UUID uuid) {
		Blob playerBlob = uuidToBlob(uuid);
		new SQLCard(SQLCodeType.CREATE_MODERATION_PLAYER, SQLCard.SQLCardType.SET, Arrays.asList(),
				Arrays.asList(playerBlob)).execute();
	}
	
	public static void createWorldPlayer(UUID uuid, String worldName) {
		Blob playerBlob = uuidToBlob(uuid);

		new SQLCard(SQLCodeType.CREATE_WORLD_PLAYER, SQLCard.SQLCardType.SET,
				Arrays.asList(worldName),
				Arrays.asList(playerBlob, playerBlob)).execute();
	}
	
	public static Date getBanDate(UUID uuid) {
		Blob playerBlob = uuidToBlob(uuid);

		return Date.from(((Timestamp) new SQLCard(SQLCodeType.GET_BANDATE, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(playerBlob)).execute().get(0)).toInstant());

	}
	
	public static void setBanDate(UUID uuid, Date date) {
		Blob playerBlob = uuidToBlob(uuid);

		new SQLCard(SQLCodeType.SET_BANDATE, SQLCard.SQLCardType.SET,
				Arrays.asList(),
				Arrays.asList(Timestamp.from(date.toInstant()).toString(), playerBlob)).execute();
	}
	
	public static String getBanReason(UUID uuid) {
		Blob playerBlob = uuidToBlob(uuid);

		return (String) new SQLCard(SQLCodeType.GET_BANREASON, SQLCard.SQLCardType.GET, Arrays.asList(),
				Arrays.asList(playerBlob)).execute().get(0);
	}
	
	public static void setBanReason(UUID uuid, String reason) {
		Blob playerBlob = uuidToBlob(uuid);

		new SQLCard(SQLCodeType.SET_BANREASON, SQLCard.SQLCardType.SET,
				Arrays.asList(),
				Arrays.asList(reason, playerBlob)).execute();
	}
	
	public static void setNickName(UUID uuid, String nickname) {
		Blob playerBlob = uuidToBlob(uuid);

		new SQLCard(SQLCodeType.SET_NICKNAME, SQLCard.SQLCardType.SET,
				Arrays.asList(),
				Arrays.asList(nickname, playerBlob)).execute();
	}
	
	public static BaseComponent[] getNickName(Player player) {
		Blob playerBlob = uuidToBlob(player.getUniqueId());

		final String nicknameRaw = (String) new SQLCard(SQLCodeType.GET_NICKNAME, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(playerBlob)).execute().get(0);
		if (nicknameRaw.length() == 0) return null;
		BaseComponent[] nickname;
		nickname = NicknameSystem.translateRGBColorCodes('#', '&', nicknameRaw);
		
		return nickname;
	}

	public static List<MailWrapper> getMail(UUID receiver) {
		Blob receiverBlob = uuidToBlob(receiver);

		List<Object> objects = new SQLCard(SQLCodeType.GET_MAIL, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(receiverBlob)).execute();

		int objectIndex = 0;
		LinkedList<MailWrapper> mail = new LinkedList<MailWrapper>();

		int index = 0;
		while (objectIndex < objects.size() && index <= MailSystem.mailMax) {
			UUID senderUUID = bytesToUUID((byte[]) objects.get(objectIndex+1));

			mail.add(new MailWrapper(senderUUID, ((java.sql.Timestamp) objects.get(objectIndex + 2)).toLocalDateTime(), (String) objects.get(objectIndex)));
			objectIndex = objectIndex + 3;
			index++;
		}
		return mail;
	}

	public static void clearMail(UUID playerUUID) {
		Blob playerBlob = uuidToBlob(playerUUID);
		new SQLCard(SQLCodeType.CLEAR_MAIL, SQLCard.SQLCardType.SET,
				Arrays.asList(),
				Arrays.asList(playerBlob)).execute();
	}

	public static int countMail(UUID playerUUID) {
		Blob playerBlob = uuidToBlob(playerUUID);

		return (int) (long) new SQLCard(SQLCodeType.COUNT_MAIL, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(playerBlob)).execute().get(0);
	}

	public static void sendMail(UUID recipientUUID, UUID senderUUID, String message) {
		Blob recipientBlob = uuidToBlob(recipientUUID);
		Blob senderBlob = uuidToBlob(senderUUID);

		new SQLCard(SQLCodeType.SEND_MAIL, SQLCard.SQLCardType.SET,
				Arrays.asList(),
				Arrays.asList(recipientBlob, senderBlob, Timestamp.from(Instant.now()),message)).execute();
	}

	public static Date getMuteDate(UUID uuid) {
		Blob uuidBlob = uuidToBlob(uuid);

		return Date.from(((Timestamp) new SQLCard(SQLCodeType.GET_MUTEDATE, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(uuidBlob)).execute().get(0)).toInstant());
	}
	
	public static void setMuteDate(UUID uuid, Date date) {
		Blob uuidBlob = uuidToBlob(uuid);

		new SQLCard(SQLCodeType.SET_MUTEDATE, SQLCard.SQLCardType.SET,
				Arrays.asList(),
				Arrays.asList(Timestamp.from(date.toInstant()), uuidBlob)).execute();
	}
	
	public static String getMuteReason(UUID uuid) {
		Blob uuidBlob = uuidToBlob(uuid);
		return (String) new SQLCard(SQLCodeType.GET_MUTEREASON, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(uuidBlob)).execute().get(0);
	}
	
	public static void setMuteReason(UUID uuid, String reason) {
		Blob uuidBlob = uuidToBlob(uuid);
		new SQLCard(SQLCodeType.SET_MUTEREASON, SQLCard.SQLCardType.SET,
				Arrays.asList(),
				Arrays.asList(reason, uuidBlob)).execute();
	}
	
	public static Date getJoinDate(UUID uuid) {
		Blob uuidBlob = uuidToBlob(uuid);
		return ((Date) new SQLCard(SQLCodeType.GET_JOINDATE, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(uuidBlob)).execute().get(0));
	}
	
	public static int getCurrency(UUID uuid, Currency currency) {
		Blob uuidBlob = uuidToBlob(uuid);

		return (int) (long) new SQLCard(currency == Currency.POINTS ? SQLCodeType.GET_POINTS : SQLCodeType.GET_COINS, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(uuidBlob)).execute().get(0);
	}
	
	public static void setCurrency(UUID uuid, int amount, Currency currency) {
		Blob uuidBlob = uuidToBlob(uuid);

		new SQLCard(currency == Currency.POINTS ? SQLCodeType.SET_POINTS : SQLCodeType.SET_COINS, SQLCard.SQLCardType.SET,
				Arrays.asList(),
				Arrays.asList(amount, uuidBlob)).execute();
	}
	
	public static List<Permission> getGlobalPermissions(UUID uuid) {
		Blob uuidBlob = uuidToBlob(uuid);

		SQLCard card = new SQLCard(SQLCodeType.GET_GLOBAL_PERMISSIONS, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(uuidBlob));
		if (card.execute().size() < 1)
			return new ArrayList<Permission>(0);
		
		byte[] serialized = (byte[]) card.execute().get(0);

		// Deserialize permissions to a list of permissions
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(serialized);
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

			Blob uuidBlob = uuidToBlob(uuid);
			Blob permissionsBlob = objectToBlob(byteOutputStream.toByteArray());
			
			// Save serialized list of permissions in Database
			new SQLCard(SQLCodeType.SET_GLOBAL_PERMISSIONS, SQLCard.SQLCardType.SET,
					Arrays.asList(),
					Arrays.asList(permissionsBlob, uuidBlob)).execute();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (objectOutputStream != null) try { objectOutputStream.close(); } catch (IOException e) { e.printStackTrace(); }
			if (byteOutputStream != null) try { byteOutputStream.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}	
	
	public static List<Permission> getWorldPermission(UUID uuid, String worldName) {
		Blob uuidBlob = uuidToBlob(uuid);

		// Get world permissions from database
		SQLCard card = new SQLCard(SQLCodeType.GET_WORLD_PERMISSIONS, SQLCard.SQLCardType.GET,
				Arrays.asList(worldName),
				Arrays.asList(uuidBlob));
		
		// Return new empty permissions if database entry doesn't exist
		if (card.execute().size() < 1)
			return new ArrayList<Permission>(0);
		
		byte[] serialized = (byte[]) card.execute().get(0);
		
		// Deserialize to a list of permissions
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(serialized);
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
		Blob uuidBlob = uuidToBlob(uuid);

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

			Blob permissionsBlob = objectToBlob(byteOutputStream.toByteArray());

			// Save serialized list to database
			new SQLCard(SQLCodeType.SET_WORLD_PERMISSIONS, SQLCard.SQLCardType.SET,
					Arrays.asList(worldName),
					Arrays.asList(permissionsBlob, uuidBlob)).execute();
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
		Blob uuidBlob = uuidToBlob(uuid);

		// Get serialized list from database
		byte[] serialized = (byte[]) new SQLCard(SQLCodeType.GET_HOMES, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(uuidBlob)).execute().get(0);
		
		// Deserialize list
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(serialized);
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
		Blob uuidBlob = uuidToBlob(uuid);

		// Serialized home list
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		BukkitObjectOutputStream objectOutputStream = null;
		try {
			objectOutputStream = new BukkitObjectOutputStream(byteOutputStream);
			objectOutputStream.writeObject(homes);

			Blob homesBlob = objectToBlob(byteOutputStream.toByteArray());

			// Save Serialized home list to database
			new SQLCard(SQLCodeType.SET_HOMES, SQLCard.SQLCardType.SET,
					Arrays.asList(),
					Arrays.asList(homesBlob, uuidBlob)).execute();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (objectOutputStream != null) try { objectOutputStream.close(); } catch (IOException e) { e.printStackTrace(); }
			if (byteOutputStream != null) try { byteOutputStream.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	public static ItemStack[] getInventory(UUID uuid, String worldName) {
		Blob uuidBlob = uuidToBlob(uuid);

		// Get Seralized Inventory from Database
		byte[] serialized = (byte[]) new SQLCard(SQLCodeType.GET_INVENTORY, SQLCard.SQLCardType.GET,
				Arrays.asList(worldName, uuid.toString().replaceAll("-", "")),
				Arrays.asList(uuidBlob)).execute().get(0);
		
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(serialized);
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

			Blob uuidBlob = uuidToBlob(uuid);
			Blob inventoryBlob = objectToBlob(byteOutputStream.toByteArray());
			
			// Save inventory in the SQL database
			new SQLCard(SQLCodeType.SET_INVENTORY, SQLCard.SQLCardType.SET,
					Arrays.asList(worldName),
					Arrays.asList(inventoryBlob, uuidBlob)).execute();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) try { outputStream.close(); } catch (IOException e) { e.printStackTrace(); }
			if (byteOutputStream != null) try { byteOutputStream.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	public static int getRankID(UUID uuid) {
		Blob uuidBlob = uuidToBlob(uuid);

		return (int) new SQLCard(SQLCodeType.GET_RANKID, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(uuidBlob)).execute().get(0);
	}
	
	public static void setRankID(UUID uuid, int rankID) {
		Blob uuidBlob = uuidToBlob(uuid);

		new SQLCard(SQLCodeType.SET_RANKID, SQLCard.SQLCardType.SET, Arrays.asList(),
				Arrays.asList(rankID, uuidBlob)).execute();
	}
	
	public static float getTip(UUID uuid) {
		Blob uuidBlob = uuidToBlob(uuid);

		return ((BigDecimal) new SQLCard(SQLCodeType.GET_TIP, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(uuidBlob)).execute().get(0)).floatValue();
	}
	
	public static void setTip(UUID uuid, float amount) {
		Blob uuidBlob = uuidToBlob(uuid);

		new SQLCard(SQLCodeType.SET_TIP, SQLCard.SQLCardType.SET,
				Arrays.asList(),
				Arrays.asList(amount, uuidBlob)).execute();
	}
	
	public static double getXP(UUID uuid) {
		Blob uuidBlob = uuidToBlob(uuid);

		return ((BigDecimal) new SQLCard(SQLCodeType.GET_XP, SQLCard.SQLCardType.GET,
				Arrays.asList(), Arrays.asList(uuidBlob)).execute().get(0)).doubleValue();
	}
	
	public static void setXP(UUID uuid, double amount) {
		Blob uuidBlob = uuidToBlob(uuid);

		new SQLCard(SQLCodeType.SET_XP, SQLCard.SQLCardType.SET,
				Arrays.asList(),
				Arrays.asList(amount, uuidBlob)).execute();
	}
	
	public static List<Pet> getPets(UUID uuid) {
		Blob uuidBlob = uuidToBlob(uuid);

		// Get serialized list of pet IDs
		byte[] serialized = (byte[]) new SQLCard(SQLCodeType.GET_PETS, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(uuidBlob)).execute().get(0);
		
		// Deserialized list of pet IDs
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(serialized);
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

			Blob petsBlob = objectToBlob(byteOutputStream.toByteArray());

			// Save list of pet IDs (serialized) to database
			new SQLCard(SQLCodeType.SET_PETS, SQLCard.SQLCardType.SET,
					Arrays.asList(),
					Arrays.asList(petsBlob, uuidToBlob(player.getUniqueId()))).execute();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (objectOutputStream != null) try { objectOutputStream.close(); } catch (IOException e) { e.printStackTrace(); }
			if (byteOutputStream != null) try { byteOutputStream.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}

	public static void createMarriageTable() {
		new SQLCard(SQLCodeType.CREATE_MARRY_TABLE, SQLCard.SQLCardType.SET, Arrays.asList(), Arrays.asList()).execute();
	}

	public static void removeMarry(UUID causer, UUID receiver) {
		Blob causerBlob = uuidToBlob(causer);
		Blob receiverBlob = uuidToBlob(receiver);

		new SQLCard(SQLCodeType.REMOVE_MARRY, SQLCard.SQLCardType.SET, Arrays.asList(), Arrays.asList(causerBlob, receiverBlob)).execute();
		new SQLCard(SQLCodeType.REMOVE_MARRY, SQLCard.SQLCardType.SET, Arrays.asList(), Arrays.asList(receiverBlob, causerBlob)).execute();
	}

	public static void addMarry(UUID causer, UUID receiver) {
		Blob causerBlob = uuidToBlob(causer);
		Blob receiverBlob = uuidToBlob(receiver);

		new SQLCard(SQLCodeType.ADD_MARRY, SQLCard.SQLCardType.SET, Arrays.asList(), Arrays.asList(causerBlob, receiverBlob)).execute();
		new SQLCard(SQLCodeType.ADD_MARRY, SQLCard.SQLCardType.SET, Arrays.asList(), Arrays.asList(receiverBlob, causerBlob)).execute();
	}

	public static List<UUID> getMarry(UUID uuid) {
		Blob uuidBlob = uuidToBlob(uuid);

		List<Object> results = new SQLCard(SQLCodeType.GET_MARRY_PARTNERS, SQLCard.SQLCardType.GET, Arrays.asList(), Arrays.asList(uuidBlob)).execute();
		List<UUID> uuids = new LinkedList<UUID>();

		for (Object object : results) {
			UUID uuidIndex = bytesToUUID((byte[]) object);
			PrintConsole.test(uuidIndex.toString());

			uuids.add(uuidIndex);
		}

		return uuids;
	}

	private static Blob uuidToBlob(UUID uuid) {
		try {
			return new SerialBlob(uuidToBytes(uuid));
		} catch (SQLException exception) {
			exception.printStackTrace();
			return null;
		}
	}

	private static byte[] uuidToBytes(UUID uuid) {
		ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
		buffer.putLong(uuid.getMostSignificantBits());
		buffer.putLong(uuid.getLeastSignificantBits());
		return buffer.array();
	}

	private  static UUID bytesToUUID(byte[] uuidBytes) {
		ByteBuffer buffer = ByteBuffer.wrap(uuidBytes);
		return new UUID(buffer.getLong(), buffer.getLong());
	}

	private static Blob objectToBlob(byte[] bytes) {
		try {
			return new SerialBlob(bytes);
		} catch (SQLException exception) {
			exception.printStackTrace();
			return null;
		}
	}

	public static boolean isEnabled() {
		return sqlInfo.enabled;
	}
	
	public static void disconnectSQL() {
		SQLCard.disconnect();
	}
}