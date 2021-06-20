package net.wigoftime.open_komodo.sql;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.wigoftime.open_komodo.config.Config;
import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.etc.systems.MailSystem;
import net.wigoftime.open_komodo.etc.systems.NicknameSystem;
import net.wigoftime.open_komodo.objects.*;
import net.wigoftime.open_komodo.sql.SQLCode.SQLCodeType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import javax.swing.*;
import java.io.*;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

abstract public class SQLManager {
	private static final int delayAmount = 5000;
	private static @NotNull SQLInfo sqlInfo = Config.getSQLInfo();
	
	public static void setup() {
		createMainTable();
		setUpWorlds(Bukkit.getWorlds());
		createModerationTable();
		createMailTable();
		createBagInventoryTable();
		createMarriageTable();

		int version = Config.getVersion();
		if (version < 1)
			SQLManager.createModHistory();
	}
	
	public static void setUpWorlds(@NotNull List<World> worlds) {
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

	public static void createModHistory() {
		new SQLCard(SQLCodeType.ADD_MODHISTORY_COLUMN, SQLCard.SQLCardType.SET, Arrays.asList(), Arrays.asList()).execute();
	}
	
	public static @NotNull List<CustomItem> getItems(@NotNull UUID uuid) {
		Blob playerBlob = uuidToBlob(uuid);

		List<CustomItem> itemList = formatToItems((String) new SQLCard(SQLCodeType.GET_ITEMS, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(playerBlob)).execute().get(0),
				Bukkit.getOfflinePlayer(uuid).getPlayer() != null ? Bukkit.getOfflinePlayer(uuid).getPlayer() : null);
		return itemList;
	}

	public static @NotNull List<CustomItem> formatToItems(String json, @Nullable Player player) {
		JSONArray array;
		JSONParser parser = new JSONParser();

		try {
			array = (JSONArray) parser.parse(json);
		} catch (ParseException exception) {
			exception.printStackTrace();
			if (player != null)
				player.sendMessage(ChatColor.DARK_RED + "!!! ERROR, COULDN'T PARSE YOUR ITEMS, RESETTING.\nCONTACT FOR SUPPORT TO GET ITEMS BACK!");
			return new ArrayList<CustomItem>(0);
		}

		List<CustomItem> list = new ArrayList<CustomItem>(array.size());
		Iterator arrayIterator = array.iterator();
		while (arrayIterator.hasNext()) list.add(CustomItem.getCustomItem(((Long) arrayIterator.next()).intValue()));
		return list;
	}
	
	public static void setItems(@NotNull CustomPlayer player) {
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
	
	public static boolean containsPlayer(@NotNull UUID uuid) {
		Blob playerBlob = uuidToBlob(uuid);

		return new SQLCard(SQLCodeType.CONTAINS_PLAYER, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(playerBlob)).execute().size() < 1 ? false : true;
	}
	
	public static boolean containsModerationPlayer(@NotNull UUID uuid) {
		Blob playerBlob = uuidToBlob(uuid);

		SQLCard card = new SQLCard(SQLCodeType.CONTAINS_MODERATION_PLAYER, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(playerBlob));
		
		// If Player can't be found in world database
		if (card.execute().isEmpty())
			return false;
		
		return true;
	}
	
	public static boolean containsWorldPlayer(@NotNull UUID uuid, String worldName) {
		Blob playerBlob = uuidToBlob(uuid);

		SQLCard card = new SQLCard(SQLCodeType.CONTAINS_WORLD_PLAYER, SQLCard.SQLCardType.GET, Arrays.asList(worldName),
				Arrays.asList(playerBlob));
		
		// If Player can't be found in world database
		if (card.execute().isEmpty())
			return false;
		
		return true;
	}
	
	public static int createBagInventory(@NotNull UUID uuid) {
		Blob playerBlob = uuidToBlob(uuid);

		List<Object> sqlElements = new SQLCard(SQLCodeType.GET_LATEST_BAGID, SQLCard.SQLCardType.GET,
				Arrays.asList(), Arrays.asList()).execute();
		int id = (int) (long) sqlElements.get(0) +1;

		new SQLCard(SQLCodeType.CREATE_BAG_INVENTORY, SQLCard.SQLCardType.SET, Arrays.asList(),
				Arrays.asList(playerBlob, id)).execute();
		return id;
	}
	
	public static @Nullable List<ItemStack> getBagInventory(@NotNull UUID uuid, int id) {
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
	
	public static void setBagInventory(@NotNull UUID uuid, int id, List<ItemStack> inventory) {
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
	
	public static void createPlayer(@NotNull UUID uuid) {
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
	
	public static void createModerationPlayer(@NotNull UUID uuid) {
		Blob playerBlob = uuidToBlob(uuid);

		ByteArrayOutputStream byteArrayOutputStream = null;
		BukkitObjectOutputStream bukkitObjectOutputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);
			bukkitObjectOutputStream.writeObject(new LinkedList<SingleSelectionModel>());

			new SQLCard(SQLCodeType.CREATE_MODERATION_PLAYER, SQLCard.SQLCardType.SET, Arrays.asList(),
					Arrays.asList(playerBlob, new SerialBlob(byteArrayOutputStream.toByteArray()))).execute();
		} catch (IOException | SQLException exception) {
			exception.printStackTrace();
			PrintConsole.print(ChatColor.DARK_RED + "ERROR, CAN NOT CREATE MODERATION PLAYER. MODERATION MAY BE BROKEN.");
			return;
		} finally {
			if (bukkitObjectOutputStream != null) try {bukkitObjectOutputStream.close();} catch (IOException exception) { exception.printStackTrace();}
			if (byteArrayOutputStream != null) try {byteArrayOutputStream.close();} catch (IOException exception) { exception.printStackTrace();}

		}
	}
	
	public static void createWorldPlayer(@NotNull UUID uuid, String worldName) {
		Blob playerBlob = uuidToBlob(uuid);

		new SQLCard(SQLCodeType.CREATE_WORLD_PLAYER, SQLCard.SQLCardType.SET,
				Arrays.asList(worldName),
				Arrays.asList(playerBlob, playerBlob)).execute();
	}
	
	public static @NotNull Date getBanDate(@NotNull UUID uuid) {
		Blob playerBlob = uuidToBlob(uuid);

		return Date.from(((Timestamp) new SQLCard(SQLCodeType.GET_BANDATE, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(playerBlob)).execute().get(0)).toInstant());

	}
	
	public static void setBanDate(@NotNull UUID uuid, @NotNull Date date) {
		Blob playerBlob = uuidToBlob(uuid);

		new SQLCard(SQLCodeType.SET_BANDATE, SQLCard.SQLCardType.SET,
				Arrays.asList(),
				Arrays.asList(Timestamp.from(date.toInstant()).toString(), playerBlob)).execute();
	}

	public static @NotNull List<ModHistorySingle> getModHistory(@NotNull UUID uuid) {
		Blob playerBlob = uuidToBlob(uuid);

		ByteArrayInputStream byteArrayInputStream = null;
		ObjectInputStream objectInputStream = null;
		try {
			byte[] serialized = (byte[]) new SQLCard(SQLCodeType.GET_MODHISTORY, SQLCard.SQLCardType.GET, Arrays.asList(), Arrays.asList(playerBlob)).execute().get(0);
			byteArrayInputStream = new ByteArrayInputStream(serialized);
			objectInputStream = new ObjectInputStream(byteArrayInputStream);
			List<ModHistorySingle> modHistory = (List<ModHistorySingle>) objectInputStream.readObject();
			return modHistory;
		} catch (IOException | ClassNotFoundException exception) {
			exception.printStackTrace();
			PrintConsole.print(ChatColor.DARK_RED + "ERROR, COULD NOT GET PUNISHMENT LIST FROM "+uuid.toString());
			return new LinkedList<ModHistorySingle>();
		} catch (IndexOutOfBoundsException indexOutOfBoundsException) {
			PrintConsole.print(ChatColor.GOLD + "WARNING, PUNISHMENT LIST DOESN'T EXIST FROM "+uuid.toString() +", RETURNING EMPTY RESULTS");
			return new LinkedList<ModHistorySingle>();
		} finally {
			if (objectInputStream != null) try { objectInputStream.close(); } catch (IOException exception) {};
			if (byteArrayInputStream != null) try { byteArrayInputStream.close(); } catch (IOException exception) {};
		}
	}

	public static void addModHistory(@NotNull UUID uuid,@NotNull ModHistorySingle historySingle) {
		Blob playerBlob = uuidToBlob(uuid);

		List<ModHistorySingle> modHistory = getModHistory(uuid);
		PrintConsole.test("modHistory : "+ modHistory.toString());
		modHistory.add(historySingle);

		BukkitObjectOutputStream objectOutputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			objectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(modHistory);
			Blob modHistoryBlob = objectToBlob(byteArrayOutputStream.toByteArray());

			new SQLCard(SQLCodeType.SET_MODHISTORY, SQLCard.SQLCardType.SET, Arrays.asList(), Arrays.asList(modHistoryBlob, playerBlob)).execute();
		} catch (IOException exception) {
			exception.printStackTrace();
			PrintConsole.print(ChatColor.DARK_RED + "ERROR, COULD NOT SET PUNISHMENT LIST OF "+uuid.toString());
		}
	}
	
	public static @NotNull String getBanReason(@NotNull UUID uuid) {
		Blob playerBlob = uuidToBlob(uuid);

		return (String) new SQLCard(SQLCodeType.GET_BANREASON, SQLCard.SQLCardType.GET, Arrays.asList(),
				Arrays.asList(playerBlob)).execute().get(0);
	}
	
	public static void setBanReason(@NotNull UUID uuid, String reason) {
		Blob playerBlob = uuidToBlob(uuid);

		new SQLCard(SQLCodeType.SET_BANREASON, SQLCard.SQLCardType.SET,
				Arrays.asList(),
				Arrays.asList(reason, playerBlob)).execute();
	}

	public static @Nullable List<Object> getFullMainPlayer(@NotNull UUID uuid) {
		Blob playerBlob = uuidToBlob(uuid);

		return new SQLCard(SQLCodeType.GET_FULL_PLAYER, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(playerBlob)).execute();
	}
	
	public static void setNickName(@NotNull UUID uuid, String nickname) {
		Blob playerBlob = uuidToBlob(uuid);

		new SQLCard(SQLCodeType.SET_NICKNAME, SQLCard.SQLCardType.SET,
				Arrays.asList(),
				Arrays.asList(nickname, playerBlob)).execute();
	}
	
	public static BaseComponent @Nullable [] getNickName(@NotNull Player player) {
		Blob playerBlob = uuidToBlob(player.getUniqueId());

		final String nicknameRaw = (String) new SQLCard(SQLCodeType.GET_NICKNAME, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(playerBlob)).execute().get(0);

		BaseComponent[] nickname = formatToNickname(nicknameRaw);
		return formatToNickname(nicknameRaw, player);
	}

	public static @Nullable BaseComponent @Nullable [] formatToNickname(@NotNull String nicknameRaw) {
		BaseComponent[] nickname;
		if (nicknameRaw.length() == 0) {
			return null;
		};

		return NicknameSystem.translateRGBColorCodes('#', '&', nicknameRaw);
	}

	public static BaseComponent @Nullable [] formatToNickname(@NotNull String nicknameRaw, @NotNull Player player) {
		BaseComponent[] nickname = formatToNickname(nicknameRaw);
		if (nickname == null) {
			ComponentBuilder builder = new ComponentBuilder();
			builder.append(player.getDisplayName());
			return builder.create();
		}

		return nickname;
	}

	public static @NotNull List<MailWrapper> getMail(@NotNull UUID receiver) {
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

	public static void clearMail(@NotNull UUID playerUUID) {
		Blob playerBlob = uuidToBlob(playerUUID);
		new SQLCard(SQLCodeType.CLEAR_MAIL, SQLCard.SQLCardType.SET,
				Arrays.asList(),
				Arrays.asList(playerBlob)).execute();
	}

	public static int countMail(@NotNull UUID playerUUID) {
		Blob playerBlob = uuidToBlob(playerUUID);

		return (int) (long) new SQLCard(SQLCodeType.COUNT_MAIL, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(playerBlob)).execute().get(0);
	}

	public static void sendMail(@NotNull UUID recipientUUID, @NotNull UUID senderUUID, String message) {
		Blob recipientBlob = uuidToBlob(recipientUUID);
		Blob senderBlob = uuidToBlob(senderUUID);

		new SQLCard(SQLCodeType.SEND_MAIL, SQLCard.SQLCardType.SET,
				Arrays.asList(),
				Arrays.asList(recipientBlob, senderBlob, Timestamp.from(Instant.now()),message)).execute();
	}

	public static @NotNull Date getMuteDate(@NotNull UUID uuid) {
		Blob uuidBlob = uuidToBlob(uuid);

		return Date.from(((Timestamp) new SQLCard(SQLCodeType.GET_MUTEDATE, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(uuidBlob)).execute().get(0)).toInstant());
	}
	
	public static void setMuteDate(@NotNull UUID uuid, @NotNull Date date) {
		Blob uuidBlob = uuidToBlob(uuid);

		new SQLCard(SQLCodeType.SET_MUTEDATE, SQLCard.SQLCardType.SET,
				Arrays.asList(),
				Arrays.asList(Timestamp.from(date.toInstant()), uuidBlob)).execute();
	}
	
	public static @NotNull String getMuteReason(@NotNull UUID uuid) {
		Blob uuidBlob = uuidToBlob(uuid);
		return (String) new SQLCard(SQLCodeType.GET_MUTEREASON, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(uuidBlob)).execute().get(0);
	}
	
	public static void setMuteReason(@NotNull UUID uuid, String reason) {
		Blob uuidBlob = uuidToBlob(uuid);
		new SQLCard(SQLCodeType.SET_MUTEREASON, SQLCard.SQLCardType.SET,
				Arrays.asList(),
				Arrays.asList(reason, uuidBlob)).execute();
	}
	
	public static @NotNull Date getJoinDate(@NotNull UUID uuid) {
		Blob uuidBlob = uuidToBlob(uuid);
		return ((Date) new SQLCard(SQLCodeType.GET_JOINDATE, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(uuidBlob)).execute().get(0));
	}
	
	public static int getCurrency(@NotNull UUID uuid, Currency currency) {
		Blob uuidBlob = uuidToBlob(uuid);

		return (int) (long) new SQLCard(currency == Currency.POINTS ? SQLCodeType.GET_POINTS : SQLCodeType.GET_COINS, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(uuidBlob)).execute().get(0);
	}
	
	public static void setCurrency(@NotNull UUID uuid, int amount, Currency currency) {
		Blob uuidBlob = uuidToBlob(uuid);

		new SQLCard(currency == Currency.POINTS ? SQLCodeType.SET_POINTS : SQLCodeType.SET_COINS, SQLCard.SQLCardType.SET,
				Arrays.asList(),
				Arrays.asList(amount, uuidBlob)).execute();
	}
	
	public static @NotNull List<Permission> getGlobalPermissions(@NotNull UUID uuid) {
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
		} catch (@NotNull IOException | ClassNotFoundException e) {
			e.printStackTrace();
			setGlobalPermissions(uuid, new LinkedList<Permission>());
			return new LinkedList<Permission>();
		} finally {
			if (objectInputStream != null) try { objectInputStream.close(); } catch (IOException e) { e.printStackTrace(); }
			if (byteInputStream != null) try { byteInputStream.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}	
	
	public static void setGlobalPermissions(@NotNull UUID uuid, @NotNull List<Permission> permissions) {
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
	
	public static @Nullable List<Permission> getWorldPermission(@NotNull UUID uuid, String worldName) {
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
	
	public static void setWorldPermission(@NotNull UUID uuid, @NotNull List<Permission> permissions, String worldName) {
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
	
	public static @NotNull List<Home> getHomes(@NotNull UUID uuid) {
		Blob uuidBlob = uuidToBlob(uuid);

		// Get serialized list from database
		byte[] serialized = (byte[]) new SQLCard(SQLCodeType.GET_HOMES, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(uuidBlob)).execute().get(0);

		return formatHomesList(serialized);
	}

	public static @NotNull List<Home> formatHomesList(byte @NotNull [] serialized) {
		// Deserialize list
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(serialized);
		BukkitObjectInputStream objectInputStream = null;
		try {
			objectInputStream = new BukkitObjectInputStream(byteInputStream);
			List<Home> homes = (List<Home>) objectInputStream.readObject();

			return homes;
		} catch (@NotNull IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return new ArrayList<Home>(0);
		} finally {
			if (objectInputStream != null) try { objectInputStream.close(); } catch (IOException e) { e.printStackTrace(); }
			if (byteInputStream != null) try { byteInputStream.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	public static void setHomes(@NotNull UUID uuid, List<Home> homes) {
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
	
	public static ItemStack @NotNull [] getInventory(@NotNull UUID uuid, String worldName) {
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
		catch (@NotNull IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return new ItemStack[27];
		} finally {
			if (objectInputStream != null) try { objectInputStream.close(); } catch (IOException e) { e.printStackTrace(); }
			if (byteInputStream != null) try { byteInputStream.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	public static void setInventory(@NotNull UUID uuid, String worldName, ItemStack[] inventory) {
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
	
	public static int getRankID(@NotNull UUID uuid) {
		Blob uuidBlob = uuidToBlob(uuid);

		return (int) new SQLCard(SQLCodeType.GET_RANKID, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(uuidBlob)).execute().get(0);
	}
	
	public static void setRankID(@NotNull UUID uuid, int rankID) {
		Blob uuidBlob = uuidToBlob(uuid);

		new SQLCard(SQLCodeType.SET_RANKID, SQLCard.SQLCardType.SET, Arrays.asList(),
				Arrays.asList(rankID, uuidBlob)).execute();
	}
	
	public static float getTip(@NotNull UUID uuid) {
		Blob uuidBlob = uuidToBlob(uuid);

		return ((BigDecimal) new SQLCard(SQLCodeType.GET_TIP, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(uuidBlob)).execute().get(0)).floatValue();
	}
	
	public static void setTip(@NotNull UUID uuid, float amount) {
		Blob uuidBlob = uuidToBlob(uuid);

		new SQLCard(SQLCodeType.SET_TIP, SQLCard.SQLCardType.SET,
				Arrays.asList(),
				Arrays.asList(amount, uuidBlob)).execute();
	}
	
	public static double getXP(@NotNull UUID uuid) {
		Blob uuidBlob = uuidToBlob(uuid);

		return ((BigDecimal) new SQLCard(SQLCodeType.GET_XP, SQLCard.SQLCardType.GET,
				Arrays.asList(), Arrays.asList(uuidBlob)).execute().get(0)).doubleValue();
	}
	
	public static void setXP(@NotNull UUID uuid, double amount) {
		Blob uuidBlob = uuidToBlob(uuid);

		new SQLCard(SQLCodeType.SET_XP, SQLCard.SQLCardType.SET,
				Arrays.asList(),
				Arrays.asList(amount, uuidBlob)).execute();
	}
	
	public static @NotNull List<Pet> getPets(@NotNull UUID uuid) {
		Blob uuidBlob = uuidToBlob(uuid);

		// Get serialized list of pet IDs
		byte[] serialized = (byte[]) new SQLCard(SQLCodeType.GET_PETS, SQLCard.SQLCardType.GET,
				Arrays.asList(),
				Arrays.asList(uuidBlob)).execute().get(0);
		return formatToPets(serialized);
	}

	public static @NotNull List<Pet> formatToPets(byte @NotNull [] serialized) {
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
		} catch (@NotNull IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return new ArrayList<Pet>(0);
		} finally {
			if (objectInputStream != null) try { objectInputStream.close(); } catch (IOException e) { e.printStackTrace(); }
			if (byteInputStream != null) try { byteInputStream.close(); } catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	public static void setPets(@NotNull CustomPlayer player) {
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

	public static void removeMarry(@NotNull UUID causer, @NotNull UUID receiver) {
		Blob causerBlob = uuidToBlob(causer);
		Blob receiverBlob = uuidToBlob(receiver);

		new SQLCard(SQLCodeType.REMOVE_MARRY, SQLCard.SQLCardType.SET, Arrays.asList(), Arrays.asList(causerBlob, receiverBlob)).execute();
		new SQLCard(SQLCodeType.REMOVE_MARRY, SQLCard.SQLCardType.SET, Arrays.asList(), Arrays.asList(receiverBlob, causerBlob)).execute();
	}

	public static void addMarry(@NotNull UUID causer, @NotNull UUID receiver) {
		Blob causerBlob = uuidToBlob(causer);
		Blob receiverBlob = uuidToBlob(receiver);

		new SQLCard(SQLCodeType.ADD_MARRY, SQLCard.SQLCardType.SET, Arrays.asList(), Arrays.asList(causerBlob, receiverBlob)).execute();
		new SQLCard(SQLCodeType.ADD_MARRY, SQLCard.SQLCardType.SET, Arrays.asList(), Arrays.asList(receiverBlob, causerBlob)).execute();
	}

	public static @NotNull List<UUID> getMarry(@NotNull UUID uuid) {
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

	private static @Nullable Blob uuidToBlob(@NotNull UUID uuid) {
		try {
			return new SerialBlob(uuidToBytes(uuid));
		} catch (SQLException exception) {
			exception.printStackTrace();
			return null;
		}
	}

	private static byte[] uuidToBytes(@NotNull UUID uuid) {
		ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
		buffer.putLong(uuid.getMostSignificantBits());
		buffer.putLong(uuid.getLeastSignificantBits());
		return buffer.array();
	}

	private  static @NotNull UUID bytesToUUID(byte @NotNull [] uuidBytes) {
		ByteBuffer buffer = ByteBuffer.wrap(uuidBytes);
		return new UUID(buffer.getLong(), buffer.getLong());
	}

	private static @Nullable Blob objectToBlob(byte @NotNull [] bytes) {
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