package net.wigoftime.open_komodo.objects;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.mojang.authlib.yggdrasil.response.User;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.chat.MessageFormat;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.config.PlayerSettingsConfig;
import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.etc.HomeSystem;
import net.wigoftime.open_komodo.etc.Moderation;
import net.wigoftime.open_komodo.etc.NickName;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.etc.ServerScoreBoard;
import net.wigoftime.open_komodo.gui.CustomGUI;
import net.wigoftime.open_komodo.objects.TpRequest.tpType;
import net.wigoftime.open_komodo.sql.SQLCard;
import net.wigoftime.open_komodo.sql.SQLCard.SQLCardType;
import net.wigoftime.open_komodo.sql.SQLCode;
import net.wigoftime.open_komodo.sql.SQLCode.SQLCodeType;
import net.wigoftime.open_komodo.sql.SQLManager;

public class CustomPlayer
{
	private final Player player;
	private final UUID uuid;
	private final Date joinDate;
	
	private BaseComponent[] customName;
	
	private Instant lastActiveTime;
	private boolean isAfk;
	
	private Date muteDate;
	private String muteReason;
	
	private CustomGUI activeGui;
	private Settings settings;
	
	private int pointsBalance;
	private int coinsBalance;
	private int usdDonated;
	
	private Rank rank;
	private double xp;
	
	private int homeLimit = 1;
	private List<Home> homes;
	
	private List<CustomItem> ownedHats;
	private List<CustomItem> ownedTags;
	private List<CustomItem> ownedPhones;
	private List<Pet> ownedPets;
	
	private static List<CustomPlayer> buildingPlayers = new LinkedList<CustomPlayer>();
	private boolean buildMode;
	private boolean isInvisible;
	private boolean isMonitoring;
	
	private TpRequest tpRequest;
	
	public final github.scarsz.discordsrv.dependencies.jda.api.entities.User discordUser;
	
	private static Map<UUID, CustomPlayer> mapOfPlayers = new HashMap<UUID, CustomPlayer>();
	
	public CustomPlayer(Player player) {
		this.player = player;
		uuid = player.getUniqueId();
		
		if (SQLManager.isEnabled()) {
			if (!SQLManager.containsPlayer(uuid))
				SQLManager.createPlayer(uuid);
			
			PrintConsole.test(2+"");
			if (!PlayerSettingsConfig.contains(uuid))
				PlayerSettingsConfig.create(uuid);
			
			PrintConsole.test(3+"");
			this.settings = PlayerSettingsConfig.getSettings(uuid);
			
			PrintConsole.test(4+"");
			this.joinDate = SQLManager.getJoinDate(uuid);
			
			PrintConsole.test(5+"");
			this.muteDate = SQLManager.getMuteDate(uuid);
			
			PrintConsole.test(6+"");
			this.muteReason = SQLManager.getMuteReason(uuid);
			
			PrintConsole.test(7+"");
			this.rank = Rank.getRank(SQLManager.getRankID(uuid));
			
			PrintConsole.test(8+"");
			this.xp = SQLManager.getXP(uuid);
			
			PrintConsole.test(9+"");
			usdDonated = SQLManager.getTip(uuid);
			
			PrintConsole.test(10+"");
			pointsBalance = SQLManager.getCurrency(uuid, Currency.POINTS);
			
			PrintConsole.test(11+"");
			coinsBalance = SQLManager.getCurrency(uuid, Currency.COINS);
			
			PrintConsole.test(12+"");
			List<CustomItem> ownedItems = SQLManager.getItems(uuid);
			
			List<CustomItem> listOwnedHats = new LinkedList<CustomItem>();
			List<CustomItem> listOwnedTags = new LinkedList<CustomItem>();
			List<CustomItem> listOwnedPhones = new LinkedList<CustomItem>();
			
			for (CustomItem item : ownedItems) {
				if (item == null) continue;
				
				switch (item.getType()) {
					case HAT:
						listOwnedHats.add(item);
						break;
					case TAG:
						listOwnedTags.add(item);
						break;
					case PHONE:
						listOwnedPhones.add(item);
						break;
					default:
						break;
				}
			}
			
			ownedHats = new ArrayList<CustomItem>(listOwnedHats.size());
			ownedHats.addAll(listOwnedHats);
			ownedTags = new ArrayList<CustomItem>(listOwnedTags.size());
			ownedTags.addAll(listOwnedTags);
			ownedPhones = new ArrayList<CustomItem>(listOwnedPhones.size());
			ownedPhones.addAll(listOwnedPhones);
			
			PrintConsole.test(13+"");
			this.ownedPets = SQLManager.getPets(uuid);
			
			PrintConsole.test(14+"");
			homes = SQLManager.getHomes(uuid);
		}
		else {
			if (!PlayerConfig.contains(uuid))
				PlayerConfig.createPlayerConfig(uuid);
			
			if (!PlayerSettingsConfig.contains(uuid))
				PlayerSettingsConfig.create(uuid);
			
			this.settings = PlayerSettingsConfig.getSettings(uuid);
			this.joinDate = PlayerConfig.getJoinDate(uuid);
			this.muteDate = PlayerConfig.getMuteDate(uuid);
			this.muteReason = PlayerConfig.getMuteReason(uuid);
			
			this.rank = Rank.getRank(PlayerConfig.getRankID(uuid));
			this.xp = PlayerConfig.getXP(uuid);
			
			usdDonated = PlayerConfig.getTip(uuid);
			
			pointsBalance = PlayerConfig.getCurrency(uuid, Currency.POINTS);
			coinsBalance = PlayerConfig.getCurrency(uuid, Currency.COINS);
			
			List<CustomItem> ownedItems = PlayerConfig.getItems(uuid);
			
			List<CustomItem> listOwnedHats = new LinkedList<CustomItem>();
			List<CustomItem> listOwnedTags = new LinkedList<CustomItem>();
			List<CustomItem> listOwnedPhones = new LinkedList<CustomItem>();
			
			for (CustomItem item : ownedItems) {
				switch (item.getType()) {
					case HAT:
						listOwnedHats.add(item);
						break;
					case TAG:
						listOwnedTags.add(item);
						break;
					case PHONE:
						listOwnedPhones.add(item);
						break;
					default:
						break;
				}
			}
			
			ownedHats = new ArrayList<CustomItem>(listOwnedHats.size());
			ownedHats.addAll(listOwnedHats);
			ownedTags = new ArrayList<CustomItem>(listOwnedTags.size());
			ownedTags.addAll(listOwnedTags);
			ownedPhones = new ArrayList<CustomItem>(listOwnedPhones.size());
			ownedPhones.addAll(listOwnedPhones);
			
			this.ownedPets = PlayerConfig.getPets(uuid);
			
			homes = PlayerConfig.getHomes(uuid);
		}
		
		buildMode = false;
		lastActiveTime = Instant.now();
		
		final String discordTag;
		if (Main.getDiscordSRV() == null) discordTag = null;
		else discordTag = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(uuid);
		
		if (discordTag == null) discordUser = null;
		else discordUser = DiscordUtil.getUserById(discordTag);
		
		if (!player.isOnline())
			return;
		
		mapOfPlayers.put(uuid, this);
	}
	
	public static boolean containsPlayer(UUID uuid) {
		if (SQLManager.isEnabled())
			return SQLManager.containsPlayer(uuid);
		else
			return PlayerConfig.contains(uuid);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public BaseComponent[] getCustomName() {
		if (customName == null) return null;
		
		BaseComponent[] nameClone = new BaseComponent[customName.length];
		for (int index = 0; index < customName.length; index++) {
			nameClone[index] = customName[index].duplicate();
		}
		
		return nameClone;
	}
	
	public void setupCustomName() {
		customName = SQLManager.getNickName(player);
	}
	
	public void setCustomName(BaseComponent[] name, String rawFormatName) {
		if (name == null) {
			customName = null;
			return;
		}
		
		BaseComponent[] nameClone = new BaseComponent[name.length];
		for (int index = 0; index < name.length; index++) {
			nameClone[index] = name[index].duplicate();
		}
		
		customName = nameClone;
		SQLManager.setNickName(uuid, rawFormatName);
	}
	
	public void setActivePhone(CustomItem phone) {
		player.getInventory().setItem(8, phone.getItem());
		getSettings().setPhone(phone);
		
	}
	
	public void setActiveGui(CustomGUI gui) {
		activeGui = gui;
	}
	
	public boolean hasActiveGui() {
		if (activeGui == null) return false;
		return true;
	}
	
	public CustomGUI getActiveGui() {
		return activeGui;
	}
	
	public Settings getSettings() {
		return settings;
	}
	
	public boolean isAfk() {
		return isAfk;
	}
	
	public void setAfk(boolean afk) {
		if (afk)
			this.getPlayer().sendMessage(ChatColor.GRAY + "You are now afk. While afk, Salary and potentially other rewards will be disabled.");
		else {
			if (this.isAfk)
				this.getPlayer().sendMessage(ChatColor.GRAY + "You are no longer afk, welcome back!");
			this.lastActiveTime = Instant.now();
		}
		
		isAfk = afk;
	}
	
	public Instant getLastActiveTime() {
		return lastActiveTime;
	}
	
	public Date getJoinDate()
	{
		return joinDate;
	}
	
	public Date getMuteDate() {
		return muteDate;
	}
	
	public void setMuteDate(Date date) {
		if (date == null) {
			muteDate = new Date(0);
		}
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
			public void run() {
				if (SQLManager.isEnabled())
					SQLManager.setMuteDate(uuid, date);
				else
					PlayerConfig.setMuteDate(uuid, date);
			}
		});
		
		muteDate = date;
	}
	
	public void setHat(ItemStack hat) {
		ItemStack currentHelmet = player.getEquipment().getHelmet();
		if (currentHelmet != null) 
		if (currentHelmet.getItemMeta().hasCustomModelData()){
			CustomItem currentCustomHelmet = CustomItem.getCustomItem(currentHelmet.getItemMeta().getCustomModelData());
			player.getEquipment().setHelmet(null);
			
			if (currentCustomHelmet.getType() == ItemType.PROP) player.getInventory().addItem(currentHelmet);
		}
		
		player.getEquipment().setHelmet(hat);
	}
	
	public void ban(Date date, String reason) {
		Moderation.ban(uuid, date, reason);
	}
	
	
	
	public String getMuteReason() {
		return muteReason;
	}
	
	public void setMuteReason(String reason) {
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
			public void run() {
				if (SQLManager.isEnabled())
					SQLManager.setMuteReason(uuid, reason);
				else
					PlayerConfig.setMuteReason(uuid, reason);
			}
		});
		
		muteReason = reason;
	}
	
	public String getTagDisplay() {
		return settings.getTagDisplay();
	}
	
	public void setPermission(Permission permission, World world, boolean addMode) {
		if (world == null)
			if (addMode)
				Permissions.addPermission(this , permission);
			else
				Permissions.removePermission(this , permission);
		else if (world.getName().equals(player.getWorld().getName()))
			if (addMode)
				Permissions.addPermission(this , permission);
			else
				Permissions.removePermission(this , permission);
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
			public void run() {
				if (world == null) {
					List<Permission> permissions;
					if (SQLManager.isEnabled())
						permissions = SQLManager.getGlobalPermissions(uuid);
					else
						permissions = PlayerConfig.getGlobalPermissions(uuid);
					
					if (addMode) permissions.add(permission);
					else if (!addMode) {
						PrintConsole.test("Addmode: false");
						for (Permission p : permissions)
							if (p.getName().equalsIgnoreCase(permission.getName())) {
								permissions.remove(p);
								PrintConsole.test("found: "+ permission.getName());
								break;
							}
					}
					if (SQLManager.isEnabled())
						SQLManager.setGlobalPermissions(uuid, permissions);
					else
						PlayerConfig.setGlobalPermissions(uuid, permissions);
				}
				else {
					List<Permission> permissions;
					if (SQLManager.isEnabled())
						permissions = SQLManager.getWorldPermission(uuid, world.getName());
					else
						permissions = PlayerConfig.getWorldPermissions(uuid, world.getName());
					
					if (addMode) permissions.add(permission);
					else for (Permission permissionForLoop : permissions)
						if (permissionForLoop.getName().equals(permission.getName())) {
							permissions.remove(permissionForLoop);
							PrintConsole.test("Removed: " + permissionForLoop.getName());
							break;
						}
					
					if (SQLManager.isEnabled()) SQLManager.setWorldPermission(uuid, permissions, world.getName());
					else PlayerConfig.setWorldPermissions(uuid, world.getName(), permissions);
				}
			}
		});
	}
	
	public void setTagDisplay(String tagDisplay) {
		settings.setTagDisplay(tagDisplay);
		
		return;
	}
	
	
	public void setHomeLimit(int limit) {
		homeLimit = limit;
	}
	
	public int getHomeLimit()
	{
		return homeLimit;
	}
	
	public void addHome(Home home) {
		if (homes.size() >= homeLimit)
		{
			getPlayer().sendMessage(String.format("%s%sHEY! %sSorry, but you maxed out your home limit.", ChatColor.RED, ChatColor.BOLD, ChatColor.DARK_RED));
			return;
		}
		
		homes.add(home);
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
			public void run() {
				if (SQLManager.isEnabled())
				SQLManager.setHomes(uuid, homes);
				else
				PlayerConfig.setHomes(uuid, homes);
			}
		});
	}
	
	public Home getHome(String homeName) {
		for (Home home : homes) {
			if (home.name.equalsIgnoreCase(homeName))
				return home;
		}
		
		return null;
	}
	
	public List<Home> getHomes() {
		return homes;
	}
	
	public void teleportHome(String homeName) {
		for (Home home : homes) {
			if (home.name.equalsIgnoreCase(homeName)) {
				player.teleport(home.location);
				return;
			}
		}
		
		player.sendMessage(HomeSystem.invaildHouse);
	}
	
	public void deleteHome(String homeName) {
		
		for (Home home : homes) {
			if (home.name.equalsIgnoreCase(homeName)) {
				homes.remove(home);
				
				Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
					public void run() {
						if (SQLManager.isEnabled())
						SQLManager.setHomes(uuid, homes);
						else
						PlayerConfig.setHomes(uuid, homes);
					}
				});
				
				player.sendMessage(ChatColor.GRAY + "Home deleted!");
				return;
			}
		}
		
		getPlayer().sendMessage(HomeSystem.invaildHouse);
		return;
		
	}
	
	public UUID getUniqueId()
	{
		return uuid;
	}
	
	public int getDonated() {
		return usdDonated;
	}
	
	public static int getDonated(UUID uuid) {
		if (SQLManager.isEnabled()) {
			if (!SQLManager.containsPlayer(uuid))
				return 0;
			
			return SQLManager.getTip(uuid);
		}
		
		if (!PlayerConfig.contains(uuid))
			return 0;
		
		return PlayerConfig.getTip(uuid);
	}
	
	public static void setDonated(UUID uuid, int amount) {
		CustomPlayer donater = CustomPlayer.get(uuid);
		
		if (SQLManager.isEnabled()) {
			int balance;
			if (donater != null)
			balance = donater.getDonated();
			else {
			if (!SQLManager.containsPlayer(uuid))
				SQLManager.createPlayer(uuid);
			balance = SQLManager.getTip(uuid);
			}
			
			int total = balance + amount;
			SQLManager.setTip(uuid, total);
			donater.usdDonated = total;
		} else {
			int balance;
			if (donater != null)
			balance = donater.getDonated();
			else {
			if (!PlayerConfig.contains(uuid))
				PlayerConfig.createPlayerConfig(uuid);
			balance = PlayerConfig.getTip(uuid);
			}
			
			int total = balance + amount;
			PlayerConfig.setTip(uuid, total);
			donater.usdDonated = total;
		}
		
		if (amount < 0)
			return;
		
		if (donater != null) {
			for (Player player : Bukkit.getOnlinePlayers())
				if (player != donater.getPlayer())
				player.sendMessage(String.format("%s%s has donated %d$ to the server! Thanks!", ChatColor.YELLOW, donater.getPlayer().getCustomName(), amount));
			
			donater.getPlayer().sendMessage(String.format("%sOh dear grand user, we kindly thank you for your generous donation", ChatColor.YELLOW));
		}
	}
	
	public Rank getRank()
	{
		return rank;
	}
	
	public double getXP() {
		return xp;
	}
	
	public void setXP(double xp) {
		
		this.xp = xp;
		
		// Set XP in SQL Database or Player Config file in another task asynchronously to reduce the server pausing
		// from latency the server to the SQL
		Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
			public void run() {
				if (SQLManager.isEnabled())
					SQLManager.setXP(uuid, xp);
				else
					PlayerConfig.setXP(uuid, xp);
			}
		});
		
		refreshXPBar();
	}
	
	public List<CustomItem> getItems(ItemType type) {
		switch (type) {
			case HAT:
				return ownedHats;
			case TAG:
				return ownedTags;
			case PHONE:
				return ownedPhones;
			default:
				PrintConsole.print(String.format("%sWARNING: getItems is none of the slection and will return empty.", ChatColor.YELLOW));
				player.sendMessage(String.format("%s» %sError, you might want to report this message: default case in switch in getItems()", ChatColor.RED, ChatColor.DARK_RED));
				return new ArrayList<CustomItem>(0);
		}
	}
	
	public List<CustomItem> getItems() {
		List<CustomItem> items = new ArrayList<CustomItem>(ownedHats.size() + ownedTags.size() + ownedPhones.size());
		items.addAll(ownedTags);
		for (CustomItem hat : ownedHats)
			items.add(hat);
		for (CustomItem phone : ownedPhones)
			items.add(phone);
		
		return items;
	}
	
	public void addItem(CustomItem boughtItemCustomItem) {
		switch (boughtItemCustomItem.getType()) {
		case HAT:
			ownedHats.add(boughtItemCustomItem);
			break;
		case TAG:
			ownedTags.add(boughtItemCustomItem);
			break;
		case PHONE:
			ownedPhones.add(boughtItemCustomItem);
			break;
		default:
			break;
		}
		
		PrintConsole.test("Adding.. "+ boughtItemCustomItem.getID());
		
		// Create a reference variable to self
		// to reference in another thread
		final CustomPlayer selfCustomPlayer = this;
		
		// Add items in SQL Database or Player Config file in another task asynchronously to reduce the server pausing
		// from latency the server to the SQL
		Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
			public void run() {
				if (SQLManager.isEnabled())
				SQLManager.setItems(selfCustomPlayer);
				else
				PlayerConfig.setItems(uuid, getItems());
			}
		});
	}
	
	public void refreshXPBar() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			public void run() {
				float leftoverRemaining;
				
				if (getRank() == null)
					leftoverRemaining = (float) (xp / Rank.getRank(1).getXPPrice());
				else {
					Rank rank = Rank.getRank(getRank().getID() + 1);
					
					if (rank == null)
						leftoverRemaining = 0;
					else
						leftoverRemaining = (float) (xp / rank.getXPPrice());
				}
				
				if (leftoverRemaining > 1)
					leftoverRemaining = 1;
				else if (leftoverRemaining < 0)
					leftoverRemaining = 0;
				
				player.setLevel(0);
				player.setExp(leftoverRemaining);
			}
		});
	}
	
	public void addPet(Pet pet)
	{
		ownedPets.add(pet);
		
		// Create a reference variable to self
		// to reference in another thread
		final CustomPlayer selfCustomPlayer = this;
		
		// Add item in SQL Database or Player Config file in another task asynchronously to reduce the server pausing
		// from latency the server to the SQL
		Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
			public void run() {
				if (SQLManager.isEnabled())
				SQLManager.setPets(selfCustomPlayer);
				else
				PlayerConfig.setPets(uuid, ownedPets);
			}
		});
	}
	
	public List<Pet> getPets() {
		return ownedPets;
	}
	
	public boolean hasPet(int id) {
		for (Pet pet : ownedPets)
			if (pet.getID() == id)
				return true;
		
		return false;
	}
	
	
	public boolean hasItem(int id, ItemType type) {
		for (CustomItem item : getItems(type))
			if (id == item.getID())
				return true;
		
		return false;
	}
	
	public void setRank(Rank rank)
	{
		// Set rank in SQL Database or in player's config file in another task asynchronously to reduce the server pausing
		// from latency the server to the SQL
		Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
			public void run() {
				if (SQLManager.isEnabled()) {
				SQLManager.setRankID(uuid, rank == null ? 0 : rank.getID());
				} else
				PlayerConfig.setRankID(uuid, rank == null ? 0 : rank.getID());
			}
		});
		
		setXP(0.0);
		
		this.rank = rank == null ? null : rank;
		
		CustomPlayer thisPlayer = this;
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			public void run() {
				Permissions.setUp(thisPlayer); 
				ServerScoreBoard.add(thisPlayer);
			}
		}, 1);
	}
	
	public static void setRankOffline(UUID uuid, int rankID) {
			// Set rank in SQL Database or in player's config file in another task asynchronously to reduce the server pausing
			// from latency the server to the SQL
			Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
				public void run() {
					if (SQLManager.isEnabled()) {
					SQLManager.setRankID(uuid, rankID);
					SQLManager.setXP(uuid, 0.0);
					} else {
					PlayerConfig.setRankID(uuid, rankID);
					PlayerConfig.setXP(uuid, 0.0);
					}
				}
			});
	}
	
	public void setCurrency(int amount, Currency type) {
		switch (type) {
			case POINTS:
				pointsBalance = amount;
				break;
			case COINS:
				coinsBalance = amount;
				break;
		}
		
		// Set currency amount in SQL Database or player config file in another task asynchronously to reduce the server pausing
		// from latency the server to the SQL
		Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
			public void run() {
				if (SQLManager.isEnabled())
				SQLManager.setCurrency(uuid, amount, type);
				else
				PlayerConfig.setCurrency(uuid, type, amount);
			}
		});
	}
	
	public int getCurrency(Currency type) {
		switch (type) {
		case POINTS:
			return pointsBalance;
		case COINS:
			return coinsBalance;
		}
		
		return 0;
	}
	
	public static final String buildingError = ChatColor.DARK_RED + "Sorry, but you must disable build mode in order to open this.";
	public boolean isBuilding()
	{
		return buildMode;
	}
	
	public static List<CustomPlayer> getBuildingPlayers() {
		synchronized (acceptedRequester) {
			return buildingPlayers;
		}
	}
	
	public boolean isInvisible()
	{
		return isInvisible;
	}
	
	public boolean isMonitoring()
	{
		return isMonitoring;
	}
	
	public void setBuilding(boolean isBuilding)
	{
		setBuilding(isBuilding, false);
	}
	
	public void setBuilding(boolean isBuilding, boolean isSilent)
	{
		buildMode = isBuilding;
		
		if (buildMode) {
			PotionEffect effect = new PotionEffect(PotionEffectType.GLOWING, 1000000, 255, true);
			getPlayer().addPotionEffect(effect);
			synchronized (buildingPlayers) {
				buildingPlayers.add(this);
			}
			
			if (!isSilent)
			getPlayer().sendMessage(String.format("%s» %sYou are now in building mode", ChatColor.GOLD, ChatColor.GRAY));
		} else {
			getPlayer().removePotionEffect(PotionEffectType.GLOWING);
			synchronized (buildingPlayers) {
				buildingPlayers.add(this);
			}
			
			if (!isSilent)
			getPlayer().sendMessage(String.format("%s» %sYou are now in roleplay mode", ChatColor.GOLD, ChatColor.GRAY));
		}
	}
	
	public void setInvisible(boolean isInvisible) 
	{
		setInvisible(isInvisible, false);
	}
	
	public void setInvisible(boolean isInvisible, boolean isSilent)
	{
		if (isInvisible) { 
			for (Player playerIndex: Bukkit.getOnlinePlayers()) {
			if (playerIndex.hasPermission(Permissions.seeOtherInvis))
				continue;
			
			playerIndex.hidePlayer(Main.getPlugin(), this.getPlayer());
			}
			
			if (!isSilent)
				getPlayer().sendMessage(String.format("%s» %sYou are now in invisible mode", ChatColor.GOLD, ChatColor.GRAY));
		}
		else { 
			for (Player playerIndex: Bukkit.getOnlinePlayers()) {
				playerIndex.showPlayer(Main.getPlugin(), this.getPlayer());
			}
			
			if (!isSilent)
				getPlayer().sendMessage(String.format("%s» %sYou are now visible", ChatColor.GOLD, ChatColor.GRAY));
		}
		
		this.isInvisible = isInvisible;
	}
	
	public void setMonitoring (boolean isMonitoring)
	{
		this.isMonitoring = isMonitoring;
		
		player.sendMessage(String.format(isMonitoring == true ? "%s» %sYou are now monitoring" : "%s» %sMonitoring stopped", ChatColor.GOLD, ChatColor.GRAY));
	}
	
	public void reload()
	{
		int rankID = SQLManager.getRankID(getUniqueId());
		
		rank = Rank.getRank(rankID);
	}
	
	public static CustomPlayer get(UUID uuid)
	{
		return mapOfPlayers.get(uuid);
	}
	
	public static List<CustomPlayer> getOnlinePlayers()
	{
		List<CustomPlayer> list = new ArrayList<CustomPlayer>(mapOfPlayers.size());
		for (Entry<UUID, CustomPlayer> e : mapOfPlayers.entrySet())
		{
			list.add(e.getValue());
		}
		
		return list;
	}
	
	private static final String noRequests = String.format("%s» %sYou don't have any tpa requests", ChatColor.YELLOW, ChatColor.GRAY);
	private static final String sentRequest = String.format("%s» %sTpa request sent to $D", ChatColor.YELLOW, ChatColor.GRAY);
	private static final String acceptedRequester = String.format("%s» %s$N accepted your teleport request", ChatColor.YELLOW, ChatColor.GRAY);
	private static final String acceptedTarget = String.format("%s» %sYou accepted $D's teleport request", ChatColor.YELLOW, ChatColor.GRAY);
	private static final String deniedRequest = String.format("%s» %sYou denied $D's request", ChatColor.YELLOW, ChatColor.GRAY);
	public static final String errorCantFindPerson = String.format("%s» %sCan't find $D", ChatColor.YELLOW, ChatColor.GRAY);
	public static final String tpaOff = String.format("%s» %sThey have disabled tpa requests", ChatColor.YELLOW, ChatColor.GRAY);
	
	public void tpaRequest(CustomPlayer requester, tpType type)
	{
		if (!settings.isTpaEnabled()) {
			requester.getPlayer().sendMessage(tpaOff);
			return;
		}
		
		tpRequest = new TpRequest(this.getPlayer(), requester.getPlayer(), type);
		
		String requesterMessage = MessageFormat.format(sentRequest, requester.getPlayer(), this.getPlayer(), null);
		requester.getPlayer().sendMessage(requesterMessage);
		BaseComponent[] requestedTpaMsg = new BaseComponent[3];
		
		if (type == tpType.TPA)
			requestedTpaMsg[0] = new TextComponent(String.format("%s» %s%s%s Would like to teleport to you\n    ", ChatColor.YELLOW, ChatColor.GRAY, requester.getPlayer().getDisplayName(), ChatColor.GRAY)); 
		else
			requestedTpaMsg[0] = new TextComponent(String.format("%s» %s%s%s Would like to teleport to them\n    ", ChatColor.YELLOW, ChatColor.GRAY, requester.getPlayer().getDisplayName(), ChatColor.GRAY)); 
		
		requestedTpaMsg[1] = new TextComponent(String.format("%s» Accept", ChatColor.DARK_GREEN));
		requestedTpaMsg[1].setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/tpaccept"));
		
		BaseComponent[] showTextAccept = {new TextComponent("Accept teleportation request")};
		requestedTpaMsg[1].setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, showTextAccept));
		
		BaseComponent[] showTextDeny = {new TextComponent("Deny teleportation request")};
		requestedTpaMsg[2] = new TextComponent(String.format("\n    %s» Deny", ChatColor.DARK_RED));
		requestedTpaMsg[2].setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/tpadeny"));
		requestedTpaMsg[2].setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, showTextDeny));
		
		this.getPlayer().spigot().sendMessage(requestedTpaMsg);
		this.getPlayer().playSound(this.getPlayer().getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, 1f, 1f);
	}
	
	public void tpaAccept()
	{
		if (tpRequest == null) {
			this.getPlayer().sendMessage(noRequests);
			return;
		}
		
		if (tpRequest.getType() == tpType.TPA)
			tpRequest.getRequester().teleport(tpRequest.getTarget());
		else if (tpRequest.getType() == tpType.TPAHERE)
			tpRequest.getTarget().teleport(tpRequest.getRequester());
		
		String requesterMessage = MessageFormat.format(acceptedRequester, this.getPlayer(), tpRequest.getRequester(), null);
		String targetMessage = MessageFormat.format(acceptedTarget, this.getPlayer(), tpRequest.getRequester(), null);
		
		tpRequest.getRequester().sendMessage(requesterMessage);
		tpRequest.getTarget().sendMessage(targetMessage);
		tpRequest.getTarget().playSound(tpRequest.getTarget().getLocation(), Sound.ENTITY_ENDER_EYE_LAUNCH, 1f, 1f);
		
		tpRequest = null;
	}
	
	public void tpaDeny()
	{
		if (tpRequest == null) {
			this.getPlayer().sendMessage(noRequests);
			return;
		}
		
		tpRequest = null;
		this.getPlayer().sendMessage(deniedRequest);
		return;
	}
	
	public void prepareDestroy()
	{
		mapOfPlayers.remove(this.getUniqueId());
	}
	
	public boolean isTpaRequestAllowed()
	{
		return settings.isTpaEnabled();
	}
	
	public void setTpaRequestAllowed(boolean isAllowed)
	{
		settings.setTpa(isAllowed);
	}
	
	public static Rank getRankOffline(UUID uuid) {
		if (SQLManager.isEnabled())
		return Rank.getRank(SQLManager.getRankID(uuid));
		else
		return Rank.getRank(PlayerConfig.getRankID(uuid));
	}
}
