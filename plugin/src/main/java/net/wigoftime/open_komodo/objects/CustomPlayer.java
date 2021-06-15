package net.wigoftime.open_komodo.objects;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.config.PlayerSettingsConfig;
import net.wigoftime.open_komodo.etc.*;
import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.etc.homesystem.HomeSystem;
import net.wigoftime.open_komodo.etc.systems.*;
import net.wigoftime.open_komodo.gui.CustomGUI;
import net.wigoftime.open_komodo.objects.TpRequest.tpType;
import net.wigoftime.open_komodo.sql.SQLManager;
import net.wigoftime.open_komodo.tutorial.Tutorial;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.Map.Entry;

public class CustomPlayer {
	private final @NotNull Player player;
	private final @NotNull UUID uuid;
	private final @Nullable Date joinDate;
	
	private Instant lastActiveTime;
	private boolean isAfk;
	
	private CustomGUI activeGui;
	private Settings settings;
	
	private int pointsBalance;
	private int coinsBalance;

	private final @NotNull MailSystem personalMailSystem;
	private final @NotNull TeleportSystem teleportSystem;
	private final @NotNull NicknameSystem nicknameSystem;
	private final @NotNull ModerationSystem moderationSystem;
	private final @NotNull RankSystem rankSystem;
	private final @NotNull HomeSystem homeSystem;
	private final @NotNull DonationSystem donationSystem;
	private final @NotNull ItemSystem itemSystem;
	private final @NotNull MarriageSystem marriageSystem;

	private List<Pet> ownedPets;
	
	private static @NotNull List<CustomPlayer> buildingPlayers = new LinkedList<CustomPlayer>();
	private boolean buildMode;
	private boolean isInvisible;
	private boolean isMonitoring;
	
	private @Nullable Tutorial tutorial = null;
	
	public boolean isNew = false;
	
	public final github.scarsz.discordsrv.dependencies.jda.api.entities.@Nullable User discordUser;
	
	private static @NotNull Map<UUID, CustomPlayer> mapOfPlayers = new HashMap<UUID, CustomPlayer>();
	
	public CustomPlayer(@NotNull Player player) {
		this.player = player;
		uuid = player.getUniqueId();

		if (!PlayerSettingsConfig.contains(uuid))
			PlayerSettingsConfig.create(uuid);

		this.settings = PlayerSettingsConfig.getSettings(uuid);

		personalMailSystem = new MailSystem(this);
		teleportSystem = new TeleportSystem(this);
		nicknameSystem = new NicknameSystem(this);
		moderationSystem = new ModerationSystem(this);

		if (SQLManager.isEnabled()) {
			if (!SQLManager.containsPlayer(uuid)) SQLManager.createPlayer(uuid);
			if (!SQLManager.containsModerationPlayer(uuid)) SQLManager.createModerationPlayer(uuid);

			List<Object> fullPlayerDetails = SQLManager.getFullMainPlayer(uuid);

			this.joinDate = (Date) fullPlayerDetails.get(3);
			moderationSystem.muteDate = SQLManager.getMuteDate(uuid);
			moderationSystem.muteReason = SQLManager.getMuteReason(uuid);
			rankSystem = new RankSystem(this, Rank.getRank((Integer) fullPlayerDetails.get(4)), ((BigDecimal) fullPlayerDetails.get(5)).doubleValue());
			donationSystem = new DonationSystem(this, ((BigDecimal) fullPlayerDetails.get(7)).floatValue());
			pointsBalance = ((Long) fullPlayerDetails.get(8)).intValue();
			coinsBalance = ((Long) fullPlayerDetails.get(9)).intValue();
			this.ownedPets = SQLManager.formatToPets((byte[]) fullPlayerDetails.get(11));
			homeSystem = new HomeSystem(this, SQLManager.formatHomesList((byte[]) fullPlayerDetails.get(12)));
			itemSystem = new ItemSystem(this,SQLManager.formatToItems((String) fullPlayerDetails.get(10), player));
			marriageSystem = new MarriageSystem(this, SQLManager.getMarry(uuid));


			// Get saved items from inventory
			InventoryManagement.loadInventory(this, player.getWorld());

			// Put player in rank system
			RankSystem.putPlayer(player);

			// Setup player's permissions
			Permissions.setUp(this);

			getNicknameSystem().setupCustomName(SQLManager.formatToNickname((String) fullPlayerDetails.get(1)));
		} else {
			this.joinDate = PlayerConfig.getJoinDate(uuid);
			moderationSystem.muteDate = PlayerConfig.getMuteDate(uuid);
			moderationSystem.muteReason = PlayerConfig.getMuteReason(uuid);
			rankSystem = new RankSystem(this, Rank.getRank(PlayerConfig.getRankID(uuid)), PlayerConfig.getXP(uuid));
			donationSystem = new DonationSystem(this,PlayerConfig.getTip(uuid));
			pointsBalance = PlayerConfig.getCurrency(uuid, Currency.POINTS);
			coinsBalance = PlayerConfig.getCurrency(uuid, Currency.COINS);
			this.ownedPets = PlayerConfig.getPets(uuid);
			homeSystem = new HomeSystem(this, PlayerConfig.getHomes(uuid));
			itemSystem = new ItemSystem(this,PlayerConfig.getItems(uuid));
			marriageSystem = new MarriageSystem(this, SQLManager.getMarry(uuid));
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

		if (getPersonalMailSystem().hasMail()) {
			player.sendMessage(String.format("%sYou got mail! Type in /mail to view your inbox!", ChatColor.GREEN));
			player.playNote(player.getLocation(), Instrument.CHIME, Note.natural(1,Note.Tone.C));
		}

		mapOfPlayers.put(uuid, this);
		marriageSystem.joined();
	}
	
	public static boolean containsPlayer(@NotNull UUID uuid) {
		if (SQLManager.isEnabled())
			return SQLManager.containsPlayer(uuid);
		else
			return PlayerConfig.contains(uuid);
	}
	
	public @NotNull Player getPlayer() {
		return player;
	}

	public @NotNull NicknameSystem getNicknameSystem() {
		return nicknameSystem;
	}

	public BaseComponent @Nullable [] getCustomName() {
		return nicknameSystem.getCustomName();
	}
	
	public void setCustomName(BaseComponent[] name, String rawFormatName) {
		nicknameSystem.setCustomName(name, rawFormatName);
	}
	
	public void setActivePhone(@NotNull CustomItem phone) {
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
		if (afk) {
			this.getPlayer().sendMessage(ChatColor.GRAY + "You are now afk. While afk, Salary and potentially other rewards will be disabled.");

			CustomPlayer playerInference = this;
			Bukkit.getScheduler().runTask(Main.getPlugin(), new Runnable() {
				@Override
				public void run() {
					ServerScoreBoard.add(playerInference);
					PlayerList.setAfk(playerInference, true);
				}
			});
		} else {
			if (this.isAfk) {
				this.getPlayer().sendMessage(ChatColor.GRAY + "You are no longer afk, welcome back!");
				PlayerList.setAfk(this, isAfk);

				CustomPlayer playerInference = this;
				Bukkit.getScheduler().runTask(Main.getPlugin(), new Runnable() {
					@Override
					public void run() {
						ServerScoreBoard.add(playerInference);
						PlayerList.setAfk(playerInference, false);
					}
				});
			}
			this.lastActiveTime = Instant.now();
		}

		isAfk = afk;
	}
	
	public Instant getLastActiveTime() {
		return lastActiveTime;
	}
	
	public @Nullable Date getJoinDate()
	{
		return joinDate;
	}
	
	public @Nullable Date getMuteDate() {
		return moderationSystem.muteDate;
	}

	public void setMuteDate(Date date) {
		moderationSystem.setMuteDate(date);
	}

	public String getMuteReason() {
		return moderationSystem.muteReason;
	}

	public void setMuteReason(String reason) {
		moderationSystem.setMuteReason(reason);
	}

	public boolean isMuted() {
		return moderationSystem.isMuted();
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

	public void tpaRequest(@NotNull CustomPlayer player, tpType type) {
		teleportSystem.tpaRequest(player, type);
	}

	public void tpaDeny() {
		teleportSystem.tpaDeny();
	}

	public void tpaAccept() {
		teleportSystem.tpaAccept();
	}

	public String getTagDisplay() {
		return settings.getTagDisplay();
	}
	
	public void setPermission(@NotNull Permission permission, World world, boolean addMode) {
		if (addMode) Permissions.addPermission(uuid, permission, world);
		else Permissions.removePermission(uuid, permission, world);
	}
	
	public void setTagDisplay(String tagDisplay) {
		settings.setTagDisplay(tagDisplay);
	}

	public @NotNull MailSystem getPersonalMailSystem() {
		return personalMailSystem;
	}
	
	public @NotNull UUID getUniqueId()
	{
		return uuid;
	}
	
	public float getDonated() {
		return donationSystem.getDonated();
	}

	public void addDonated(float amount) {
		donationSystem.addTip(amount);
		donationSystem.announceDonation(amount);
	}

	public static void addDonated(@NotNull UUID uuid, float amount) {
		DonationSystem.addTip(uuid,amount);

		OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
		if (player == null) return;
		DonationSystem.announceDonation(player.getName(),amount);
	}
	
	public @Nullable Rank getRank() {
		return rankSystem.getRank();
	}

	public @NotNull RankSystem getRankSystem() {
		return rankSystem;
	}
	
	public double getXP() {
		return rankSystem.getXP();
	}
	
	public void setXP(double xp) {
		rankSystem.setXP(xp);
	}
	
	public void refreshXPBar() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			public void run() {
				float leftoverRemaining;
				
				if (getRank() == null)
					leftoverRemaining = (float) (rankSystem.getXP() / Rank.getRank(1).getXPPrice());
				else {
					Rank rank = Rank.getRank(getRank().getID() + 1);
					
					if (rank == null)
						leftoverRemaining = 0;
					else
						leftoverRemaining = (float) (rankSystem.getXP() / rank.getXPPrice());
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
	
	public void addPet(Pet pet) {
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

	public @NotNull List<CustomItem> getItems(@NotNull ItemType type) {
		return itemSystem.getItems(type);
	}

	public @NotNull List<CustomItem> getItems() {
		return itemSystem.getItems();
	}

	public void addItem(@NotNull CustomItem addedItem) {
		itemSystem.addItem(addedItem);
	}

	public boolean hasItem(int id, @NotNull ItemType type) {
		return itemSystem.hasItem(id, type);
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
	
	public void setCurrency(int amount, @NotNull Currency type) {
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
	
	public int getCurrency(@NotNull Currency type) {
		switch (type) {
		case POINTS:
			return pointsBalance;
		case COINS:
			return coinsBalance;
		}
		
		return 0;
	}
	
	public static final String buildingError = ChatColor.DARK_RED + "Sorry, but you must disable build mode in order to open this.";
	public boolean isBuilding() {
		return buildMode;
	}
	
	public static @NotNull List<CustomPlayer> getBuildingPlayers() {
		synchronized (buildingPlayers) {
			return buildingPlayers;
		}
	}
	
	public boolean isInvisible() {
		return isInvisible;
	}
	
	public boolean isMonitoring() {
		return isMonitoring;
	}

	public void setBuilding(boolean isBuilding) {
		setBuilding(isBuilding, false);
	}
	
	public void setBuilding(boolean isBuilding, boolean isSilent) {
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
	
	public void setInvisible(boolean isInvisible) {
		setInvisible(isInvisible, false);
	}
	
	public void setInvisible(boolean isInvisible, boolean isSilent) {
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
	
	public void setMonitoring (boolean isMonitoring) {
		this.isMonitoring = isMonitoring;
		
		player.sendMessage(String.format(isMonitoring == true ? "%s» %sYou are now monitoring" : "%s» %sMonitoring stopped", ChatColor.GOLD, ChatColor.GRAY));
	}

	public void prepareDestroy() {
		mapOfPlayers.remove(this.getUniqueId());
	}
	
	public boolean isTpaRequestAllowed() {
		return settings.isTpaEnabled();
	}
	
	public void setTpaRequestAllowed(boolean isAllowed) {
		settings.setTpa(isAllowed);
	}

	public void setHomeLimit(int limit) {
		homeSystem.setHomeLimit(limit);
	}

	public int getHomeLimit() {
		return homeSystem.getHomeLimit();
	}

	public void addHome(Home home) {
		homeSystem.addHome(home);
	}

	public @Nullable Home getHome(String homeName) {
		return homeSystem.getHome(homeName);
	}

	public @Nullable List<Home> getHomes() {
		return homeSystem.getHomes();
	}

	public void teleportHome(String homeName) {
		homeSystem.teleportHome(homeName);
	}

	public void deleteHome(String homeName) {
		homeSystem.deleteHome(homeName);
	}

	public void setTutorial(boolean isInTutorial) {
		if (isInTutorial == true) {
			tutorial = new Tutorial(this);
			homeSystem.setupTutorialHomes();
			tutorial.begin();
		} else {
			tutorial = null;
			homeSystem.clearTutorialHomes();
			player.teleport(Main.spawnLocation);
		}
	}

	public void requestMarry(@NotNull CustomPlayer player) {
		marriageSystem.requestMarry(player);
	}

	public void divorce(@NotNull String username) {
		marriageSystem.divorce(username);
	}

	public void setPurposer(CustomPlayer player) {
		marriageSystem.setPurposer(player);
	}

	public @NotNull MarriageSystem getMarriageSystem() {
		return marriageSystem;
	}

	public @NotNull List<OfflinePlayer> getPartners() {return marriageSystem.getPartners();}

	public boolean isMarried(CustomPlayer targetPlayer) { return marriageSystem.isMarriedTo(targetPlayer); }

	public boolean isInTutorial() {
		return tutorial == null ? false : true;
	}
	
	public @Nullable Tutorial getTutorial() {
		return tutorial;
	}
	
	public static @Nullable Rank getRankOffline(@NotNull UUID uuid) {
		if (SQLManager.isEnabled())
		return Rank.getRank(SQLManager.getRankID(uuid));
		else
		return Rank.getRank(PlayerConfig.getRankID(uuid));
	}


	public static CustomPlayer get(UUID uuid) {
		return mapOfPlayers.get(uuid);
	}

	public static @Nullable CustomPlayer get(String username) {

		for (CustomPlayer playerIndex : mapOfPlayers.values()) {
			if (!playerIndex.getPlayer().getDisplayName().equalsIgnoreCase(username)) continue;
			return playerIndex;
		}
		return null;
	}

	public static @NotNull List<CustomPlayer> getOnlinePlayers() {
		List<CustomPlayer> list = new ArrayList<CustomPlayer>(mapOfPlayers.size());
		for (Entry<UUID, CustomPlayer> e : mapOfPlayers.entrySet()) {
			list.add(e.getValue());
		}

		return list;
	}
}
