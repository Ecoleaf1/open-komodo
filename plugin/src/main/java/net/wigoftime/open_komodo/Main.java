package net.wigoftime.open_komodo;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.event.entity.EntityDismountEvent;

import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.wigoftime.open_komodo.actions.BugReporter;
import net.wigoftime.open_komodo.chat.MessageFormat;
import net.wigoftime.open_komodo.chat.PrivateMessage;
import net.wigoftime.open_komodo.commands.BanCommand;
import net.wigoftime.open_komodo.commands.BuildModeCommand;
import net.wigoftime.open_komodo.commands.CheckBalanceCommand;
import net.wigoftime.open_komodo.commands.CreditsCommand;
import net.wigoftime.open_komodo.commands.DelHomeCommand;
import net.wigoftime.open_komodo.commands.EmoteCommand;
import net.wigoftime.open_komodo.commands.FlyCommand;
import net.wigoftime.open_komodo.commands.GenPayCommand;
import net.wigoftime.open_komodo.commands.GenTip;
import net.wigoftime.open_komodo.commands.HatsCommand;
import net.wigoftime.open_komodo.commands.HomeCommand;
import net.wigoftime.open_komodo.commands.HomesCommand;
import net.wigoftime.open_komodo.commands.KickCommand;
import net.wigoftime.open_komodo.commands.LogCommand;
import net.wigoftime.open_komodo.commands.MoneyCommand;
import net.wigoftime.open_komodo.commands.MsgCommand;
import net.wigoftime.open_komodo.commands.MuteCommand;
import net.wigoftime.open_komodo.commands.NicknameCommand;
import net.wigoftime.open_komodo.commands.PayCommand;
import net.wigoftime.open_komodo.commands.PetCommand;
import net.wigoftime.open_komodo.commands.PetsMenuCommand;
import net.wigoftime.open_komodo.commands.PromoteCommand;
import net.wigoftime.open_komodo.commands.PropShopCommand;
import net.wigoftime.open_komodo.commands.RankCommand;
import net.wigoftime.open_komodo.commands.ReplyCommand;
import net.wigoftime.open_komodo.commands.ReportCommand;
import net.wigoftime.open_komodo.commands.RulesCommand;
import net.wigoftime.open_komodo.commands.SetHomeCommand;
import net.wigoftime.open_komodo.commands.SitCommand;
import net.wigoftime.open_komodo.commands.SpawnCommand;
import net.wigoftime.open_komodo.commands.TagShopCommand;
import net.wigoftime.open_komodo.commands.TeleportToBuildWorldCommand;
import net.wigoftime.open_komodo.commands.TpaAcceptCommand;
import net.wigoftime.open_komodo.commands.TpaCommand;
import net.wigoftime.open_komodo.commands.TpaDenycommand;
import net.wigoftime.open_komodo.commands.TpaHereCommand;
import net.wigoftime.open_komodo.commands.TpaToggleCommand;
import net.wigoftime.open_komodo.commands.VoteCommand;
import net.wigoftime.open_komodo.config.Config;
import net.wigoftime.open_komodo.custommobs.CustomPetMob;
import net.wigoftime.open_komodo.etc.ActionBar;
import net.wigoftime.open_komodo.etc.ChatAnnouncements;
import net.wigoftime.open_komodo.etc.CoinSalary;
import net.wigoftime.open_komodo.etc.CurrencyClass;
import net.wigoftime.open_komodo.etc.InventoryManagement;
import net.wigoftime.open_komodo.etc.Moderation;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.etc.PetsManager;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.etc.RankSystem;
import net.wigoftime.open_komodo.etc.ServerScoreBoard;
import net.wigoftime.open_komodo.etc.Status_Bar;
import net.wigoftime.open_komodo.events.AsyncPlayerChat;
import net.wigoftime.open_komodo.events.VotifierEvent;
import net.wigoftime.open_komodo.filecreation.CheckFiles;
import net.wigoftime.open_komodo.gui.CustomGUI;
import net.wigoftime.open_komodo.gui.GUIManager;
import net.wigoftime.open_komodo.gui.PetControl;
import net.wigoftime.open_komodo.gui.PhoneGui;
import net.wigoftime.open_komodo.objects.AFKChecker;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.ItemType;
import net.wigoftime.open_komodo.sql.SQLManager;
import net.wigoftime.open_komodo.world.BuilderWorld;

public class Main extends JavaPlugin implements Listener 
{

	public static final String name = "Open Komodo";
	//public static final String welcomemsg = String.format("%s%s%s", welcomeHeader, welcomeContext, welcomeCloser);
	
	public static final String nameColoured = ChatColor.translateAlternateColorCodes('&', "&b&lOpen &a&lKomodo");
	//public static final String welcomemsg = String.format("%s%s━━━━━━━━━━━━\n%sWelcome back to %s!%s\n%s%s━━━━━━━━━━━━", 
			//ChatColor.YELLOW, ChatColor.STRIKETHROUGH, ChatColor.DARK_AQUA, name, ChatColor.DARK_AQUA, creditsLine, ChatColor.YELLOW, ChatColor.STRIKETHROUGH);
	public static final String firstWelcome = ChatColor.translateAlternateColorCodes('&', "&6Welcome &e%s &6to &b&lOpen &2&lKomodo!");
	
	private static String normalMessageFormat;
	private static String normalTagMessageFormat;
	
	private static int distanceRange;
	
	private static String PMSentFormat;
	private static String PMReceivedFormat;
	
	private static boolean explosionEnabled;
	private static boolean leavesDecayEnabled;
	
	private static boolean fallDamage;
	private static boolean damageAllowed;
	
	private static boolean iceMelts;
	public static String joinMessage;
	private static String leaveMessage;
	
	public static boolean allowDrop;
	
	public static String dataFolderPath;
	public static File config;
	
	public static World world;
	public static Location spawnLocation;
	
	public static final String phoneName = ChatColor.translateAlternateColorCodes('&', "&e&lEPhone");
	public static final String cardName = ChatColor.translateAlternateColorCodes('&', "&6&lBank");
	
	public static final int defaultPoints = 720;
	public static final int defaultCoins = 0;
	
	public static String resourcePackLink;
	
	private static JavaPlugin plugin;
	public static PlayerParticlesAPI particlesApi;
	
	// When plugin starts loading
	@Override
	public void onLoad() {
		PrintConsole.print("Loading..");
	}
	
	// When Plugin is enabled
	@Override
	public void onEnable() {
		PrintConsole.print("Enabled");
		
		// Register the Events with this class, with this plugin
		Bukkit.getPluginManager().registerEvents(this, this);
		
		Listener listener = new Listener() {};
		
		Bukkit.getPluginManager().registerEvent(PlayerJoinEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.PlayerJoin(), this);
		Bukkit.getPluginManager().registerEvent(PlayerDropItemEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.DropItem(), this);
		Bukkit.getPluginManager().registerEvent(BlockPlaceEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.BlockPlace(), this);
		Bukkit.getPluginManager().registerEvent(BlockBreakEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.BlockBreak(), this);
		
		if (Bukkit.getPluginManager().getPlugin("Votifier") == null)
			PrintConsole.print("NuVotifier isn't detected, vote rewards disabled.");
		else {
			Bukkit.getPluginManager().registerEvent(com.vexsoftware.votifier.model.VotifierEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.VotifierEvent(), this);
			PrintConsole.print("NuVotifier detected, vote rewards enabled.");
		}
		Bukkit.getPluginManager().registerEvent(BlockFertilizeEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.BlockFertilize(), this);
		
		// Set where the plugin's datafolder is
		dataFolderPath = getDataFolder().getAbsolutePath();
		
		plugin = this;
		particlesApi = PlayerParticlesAPI.getInstance();
		
		// Setup Builderworld
		BuilderWorld.setup();
		
		// Check the files (Like if config exists) and setup the files so it is ready to be used.
		CheckFiles.checkFiles();
		
		// Setup Global Variables
		setupGlobalVariables();
		
		// Loop through each player (in customPlayer format) and set up the permissions and display the rank prefix.
		// Useful when reloading the server.
		for (Player player : Bukkit.getOnlinePlayers()) {
			CustomPlayer customPlayer = CustomPlayer.get(player.getUniqueId());
			
			Permissions.setUp(customPlayer);
			ServerScoreBoard.add(customPlayer);
		}
		
		Status_Bar.startLoop();
		ActionBar.startLoop();
		PetsManager.startLoop();
		Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), new AFKChecker(), 0, 60);
		Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), new ChatAnnouncements(), 0, 7200);
		CoinSalary.coinpayPlayersTimer();
		
		setupExtraCommands();
	}
	
	@Override
	public void onDisable() {
		// Save everyone's inventory
		for (CustomPlayer p : CustomPlayer.getOnlinePlayers())
			InventoryManagement.saveInventory(p, p.getPlayer().getWorld());
		
		// Disconnect SQL Connection as the server is shutting down.
		SQLManager.disconnectSQL();
		
		PetsManager.serverShuttingDown();
	}
	
	/* Events Below */
	
	// When something in inventory is clicked.
	@EventHandler 
	public void inventoryClick(InventoryClickEvent clickEvent) {	
		GUIManager.invItemClicked(clickEvent);
	}
	
	
	// When vehicle of any kind (Such as boats or minecarts) was attempted to be destroyed
	@EventHandler
	public void vehicleDestroy(VehicleDestroyEvent e) 
	{
		// If it wasn't player, return
		if (!(e.getAttacker() instanceof Player))
		{
			// Cancel vehicle being destroyed
			e.setCancelled(true);
			return;
		}
		
		// If player has permission, return
		if (!e.getAttacker().hasPermission(Permissions.changePerm))
		{
			// Cancel vehicle being destroyed
			e.setCancelled(true);
			// Send message to player that it isn't allowed
			e.getAttacker().sendMessage(Permissions.getChangeError());
			
			return;
		}
		
		// Get CustomPlayer
		CustomPlayer player = CustomPlayer.get(((Player) e.getAttacker()).getUniqueId());
		
		// If customplayer format of it doesn't exist, cancel
		// Can happen if the server hasn't loaded all of the player information when they join
		if (player == null)
		{
			e.setCancelled(true);
			return;
		}
		
		if (!player.isBuilding())
		{
			e.setCancelled(true);
		}
		
	}
	
	// An Event for example when player takes off or put Armor Stand
	@EventHandler
	public void playerInteractAt(PlayerInteractAtEntityEvent e) 
	{
		CustomPlayer player = CustomPlayer.get(e.getPlayer().getUniqueId());
		
		// If customplayer format of it doesn't exist, cancel
		// Can happen if the server hasn't loaded all of the player information when they join
		if (player == null)
		{
			e.setCancelled(true);
			return;
		}
		
		if (e.getRightClicked() == PetsManager.getCreature(player.getPlayer()))
		{
			e.setCancelled(true);
			PetControl gui = new PetControl(player);
			gui.open();
			return;
		}
		
		if (!player.getPlayer().hasPermission(Permissions.changePerm))
		{
			if (e.getRightClicked().getType() == EntityType.ARMOR_STAND) 
			{
				e.setCancelled(true);
				e.getPlayer().sendMessage(Permissions.getChangeError());
				return;
			}
		}
		
		if (!player.isBuilding())
		{
			e.setCancelled(true);
			return;
		}
		
	}
	
	// If Entity dismounts something
	@EventHandler
	public void dismounted(EntityDismountEvent e)
	{
		Entity entity = e.getDismounted();
		
		if (entity.getType() == EntityType.ARROW)
			entity.remove();
		
		return;
	}
	
	@EventHandler
	public void interactedEntity(PlayerInteractEntityEvent e) 
	{
		Entity entity = e.getRightClicked();
		
		if (!e.getPlayer().hasPermission(Permissions.changePerm))
		{
			Creature petCreature = PetsManager.getCreature(e.getPlayer());
			if (petCreature.getUniqueId().equals(e.getRightClicked().getUniqueId()))
				return;
			
			e.setCancelled(true);
			return;
		}
		
		CustomPlayer player = CustomPlayer.get(e.getPlayer().getUniqueId());
		
		// If customplayer format of it doesn't exist, cancel
		// Can happen if the server hasn't loaded all of the player information when they join
		if (player == null)
		{
			e.setCancelled(true);
			return;
		}
		
		if (!player.isBuilding())
		{
			if (entity.getType() == EntityType.ARMOR_STAND) 
			{
				e.setCancelled(true);
				e.getPlayer().sendMessage(Permissions.getChangeError());
				return;
			}
			
			if (entity.getType() == EntityType.ITEM_FRAME) 
			{
				e.setCancelled(true);
				e.getPlayer().sendMessage(Permissions.getChangeError());
				return;
			}
		}
	}
	
	// When blocks fade like fire and ice
	@EventHandler
	public void blockFades(BlockFadeEvent e) 
	{
		// Get Block
		Block block = e.getBlock();
		
		if (iceMelts == true)
			return;
		
		if (block.getType() == Material.ICE) 
		{
			System.out.println(e.getBlock().getType());
			e.setCancelled(true);
			return;
		}
	}
	
	// Called when an entity is damaged by an entity
	@EventHandler
	public void entityDamageByEntity(EntityDamageByEntityEvent e) 
	{
		// If the damager wasn't a player, skip
		if (e.getDamager().getType() != EntityType.PLAYER) 
			return;
		
		// Get damager
		Player damager = (Player) e.getDamager();
		
		// If player has permission to damage entity
		if (!damager.hasPermission(Permissions.hurtPerm))
		{
			// Other wise, cancel it and tell player that they are not allowed
			e.setCancelled(true);
			damager.sendMessage(Permissions.getHurtError());
			
			return;
		}
		
		CustomPlayer player = CustomPlayer.get(damager.getUniqueId());
		
		// If customplayer format of it doesn't exist, cancel
		// Can happen if the server hasn't loaded all of the player information when they join
		if (player == null)
		{
			e.setCancelled(true);
			return;
		}
		
		if (!player.isBuilding())
		{
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void playerMove(PlayerMoveEvent e)
	{
		Player player = e.getPlayer();
		
		CustomPlayer moverCustomPlayer = CustomPlayer.get(player.getUniqueId());
		
		// If customplayer format of it doesn't exist, stop
		// Can happen if the server hasn't loaded all of the player information when they join
		if (moverCustomPlayer == null)
			return;
		
		moverCustomPlayer.setAfk(false);
		
		if (moverCustomPlayer.isAfk())
			moverCustomPlayer.setAfk(false);
		
		if (player.isFlying())
			return;
		
		if (player.getFallDistance() > 0)
			return;
		
		if (e.getFrom().distance(e.getTo()) > 0.2)
		{	
			double add = 0.000015;
			RankSystem.addPendingPoints(player, add);
		}
	}
	
	// Triggered when a hanging entity is removed by an entity
	@EventHandler
	public void hangEvent(HangingBreakByEntityEvent e) 
	{
		// If remover wasn't a player, skip
		if (e.getRemover().getType() != EntityType.PLAYER)
			return;
		
		// Get player in CustomPlayer form
		CustomPlayer player = CustomPlayer.get(((Player) e.getRemover()).getUniqueId());
		
		// If customplayer format of it doesn't exist, cancel
		// Can happen if the server hasn't loaded all of the player information when they join
		if (player == null)
		{
			e.setCancelled(true);
			return;
		}
		
		// If player has permission
		if (!player.getPlayer().hasPermission(Permissions.changePerm))
		{
			// Otherwise, canscel and send a msg that they are not allowed
			e.setCancelled(true);
			player.getPlayer().sendMessage(Permissions.getChangeError());
			
			return;
		}
		
		if (!!player.isBuilding())
		{
			e.setCancelled(true);
			return;
		}
	}
	
	// If Egg thrown, set hatching to false to prevent chickens from spawning
	@EventHandler
	public void eggThrown(PlayerEggThrowEvent e)
	{
		e.setHatching(false);
	}
	
	// When player interacts
	@EventHandler
	public void onInteract(PlayerInteractEvent e) 
	{
		if (e.getAction() == Action.PHYSICAL)
		{
			if (e.getClickedBlock().getType() == Material.FARMLAND)
				e.setCancelled(true);
			
			return;
		}
		
		// Get player in CustomPlayer format
		CustomPlayer player = CustomPlayer.get(e.getPlayer().getUniqueId());
		
		// If customplayer format of it doesn't exist, cancel
		// Can happen if the server hasn't loaded all of the player information when they join
		if (player == null)
		{
			e.setCancelled(true);
			return;
		}
		
		// Get Action
		Action action = e.getAction();
		// Get Item
		ItemStack item = e.getItem();
			
		// If not left and right click on block, cancel it.
		// This is to prevent players quickly moving their cursor out right
		// when they left click on the fire to put it out.
		if (action != Action.LEFT_CLICK_BLOCK && action != Action.RIGHT_CLICK_BLOCK)
			e.setCancelled(true);
		
			// If clicked on fire
			if (player.getPlayer().getTargetBlock((Set<Material>) null, 20).getType() == Material.FIRE)
			{
				// If player does not have permission, cancel and return
				if (!player.getPlayer().hasPermission(Permissions.changePerm))
				{
					player.getPlayer().sendMessage(Permissions.getChangeError());
					
					e.setCancelled(true);
				}
				
				return;
			}
		
		// If right clicked on block
		if (action == Action.RIGHT_CLICK_BLOCK)
		{
			// Get block
			Block block = e.getClickedBlock();
			// Get Material
			Material material = block.getType();
			
			if (player.getPlayer().hasPermission(Permissions.changePerm))
			{
				if (player.isBuilding())
					return;
			}
			if (material == Material.OAK_TRAPDOOR || material == Material.SPRUCE_TRAPDOOR || material == Material.JUNGLE_TRAPDOOR || material == Material.DARK_OAK_TRAPDOOR || material == Material.BIRCH_TRAPDOOR || material == Material.ACACIA_TRAPDOOR)
			{	
				e.setCancelled(true);
				return;
			}
			
			if (material == Material.FLOWER_POT || material.name().startsWith("POTTED"))
			{
				player.getPlayer().sendMessage(Permissions.getChangeError());
				e.setCancelled(true);
				return;
			}
			
			if (material.name().endsWith("SHULKER_BOX"))
			{
				e.setCancelled(true);
				return;
			}
			
			// If Enderchest
			if (block.getType() == Material.ENDER_CHEST)
			{
				// Allow open if player has permission
				if (player.getPlayer().hasPermission(Permissions.changePerm))
					return;
				
				if (player.isBuilding())
					return;
				
				// Other wise, cancel it
				e.setCancelled(true);
				player.getPlayer().sendMessage(Permissions.useError);
				return;
			}
		}
		
		if (item == null)
			return;
		
		if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
		{
			if (item.getType() == Material.INK_SAC)
			{
				ItemMeta meta = item.getItemMeta();
				if (meta.hasCustomModelData())
				{
					CustomItem cs = CustomItem.getCustomItem(meta.getCustomModelData());
					
					if (cs == null)
						return;
					
					if (cs.isEquipable())
					{
						player.getPlayer().getInventory().setHelmet(item);
						item.setAmount(0);
					}
				}
			}
		}
		
		// Checks if Item could have an ID
		if (item.getItemMeta().hasCustomModelData())
		{
			// If ID is 1 (IPlay Phone)
			if (item.getType() == Material.INK_SAC)
			{
				// And if the item is an ink sac, open IPlay
				CustomItem customItem = CustomItem.getCustomItem(item.getItemMeta().getCustomModelData());
				
				if (customItem == null)
					return;
				
				if (customItem.getType() == ItemType.PHONE) {	
					e.setCancelled(true);
					CustomGUI gui = new PhoneGui(player);
					gui.open();
					//PhoneGui.open(player);
					return;
				}
				
				if (item.getItemMeta().getCustomModelData() == 56)
				{
					e.setCancelled(true);
					CurrencyClass.displayBalance(player);
					return;
				}
			}
			
			if (item.getType() == Material.STICK)
			{
				InventoryManagement.openBagInventory(player, item.getItemMeta().getCustomModelData());
			}
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) 
	{	
		// Get CustomPlayer
		CustomPlayer player = CustomPlayer.get(e.getPlayer().getUniqueId());
		
		// If player in CustomPlayer doesn't exist
		// This could happen when a player joins and CustomPlayer is still trying to be created
		// but is still waiting for information like from a SQL database
		if (player == null) {
			e.setQuitMessage(null);
			return;
		}
		
		// Get Message
		String message = leaveMessage;
		
		// If message doesn't exist
		if (message == null)
			return;
		
		// If message is empty
		if (message == "")
		{
			e.setQuitMessage(null);
			return;
		}
		
		// format message
		message = MessageFormat.format(player, message);
		
		// Format colours
		message = ChatColor.translateAlternateColorCodes('&', message);
		
		// Show quit message
		e.setQuitMessage(message);
		
		// Unop the player (To sync with the permissions)
		if (player.getPlayer().isOp())
			player.getPlayer().setOp(false);
		
		PrivateMessage.playerLeft(player.getUniqueId());
		player.prepareDestroy();
	}
	
	@EventHandler
	public void switchWorld(PlayerChangedWorldEvent e)
	{
		CustomPlayer player = CustomPlayer.get(e.getPlayer().getUniqueId());
		
		// If customplayer format of it doesn't exist, cancel
		// Can happen if the server hasn't loaded all of the player information when they join
		if (player == null)
			return;
		
		World previousWorld = e.getFrom();
		World world = player.getPlayer().getWorld();
		
		// Save inventory
		InventoryManagement.saveInventory(player, previousWorld);
		
		// Load inventory
		InventoryManagement.loadInventory(player, world);
		
		// Reload permissions
		Permissions.setUp(player);
		
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) 
	{
		CommandPreprocess.commands(e);
	}
	
	@EventHandler
	public void explosion(ExplosionPrimeEvent e) 
	{
		
		if (explosionEnabled == false) 
		{
			e.setCancelled(true);
			Bukkit.getLogger().info(e.getEntity().getName() +" tried to blow up");
		}
	}
	
	@EventHandler
	public void entityDamage(EntityDamageEvent e) 
	{
		if (e.getEntityType() == EntityType.PLAYER) 
		{
			Player player = (Player) e.getEntity();
			
			if (!damageAllowed)
			{
				e.setCancelled(true);
				return;
			}
			
			if (player != null)
				if (e.getCause() == DamageCause.FALL && !fallDamage)
					e.setCancelled(true);
		
			
			return;
		}
		
		if (e.getEntity() instanceof CustomPetMob)
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void leavesDecay(LeavesDecayEvent e) 
	{
		if (leavesDecayEnabled == false) {
			e.setCancelled(true);
		}
		
	}
	
	@EventHandler
	public void playerChat(AsyncPlayerChatEvent e) 
	{
		// Sending it over to the AsyncPlayerChat Class
		AsyncPlayerChat.function(e);
	}
	
	// When player swap item in offhand
	@EventHandler
	public void playerSwap(PlayerSwapHandItemsEvent e)
	{
		
		// Get Player
		Player player = e.getPlayer();
		
		// Get Item
		ItemStack item = e.getOffHandItem();
		
		if (item == null)
			return;
		
		// Get more info about Item
		ItemMeta meta = item.getItemMeta();
		
		if (meta == null)
			return;
		
		// If has ID
		if (meta.hasCustomModelData())
		{
			// Get ID
			int id = meta.getCustomModelData();
			
			// Get Custom Item
			CustomItem ci = CustomItem.getCustomItem(id);
			
			// If it isn't a custom item, ignore
			if (ci == null)
				return;
			
			// If item is a hat or the phone, prevent
			if (ci.getType() == ItemType.HAT || id == 1)
				e.setCancelled(true);
			
			// If it is FPBank card
			if (item.getType() == Material.INK_SAC || id == 56)
				e.setCancelled(true);
			
			return;
		}
	}
	
	@EventHandler
	public void inventoryClose(InventoryCloseEvent e)
	{
		GUIManager.inventoryClosed(e);
	}
	
	@EventHandler
	public void editBook(PlayerEditBookEvent e)
	{
		// Get info about book
		BookMeta meta = e.getNewBookMeta();
		
		// If has ID, resume
		if (!meta.hasCustomModelData())
			return;
		
		// If ID matches BugReporter book, resume
		if (meta.getCustomModelData() != BugReporter.id)
			return;
		
		// Report the bug/feature
		BugReporter.complete(e.getPlayer(), meta);
		
		// Get Player
		Player player = e.getPlayer();
		// Get player's Inventory
		PlayerInventory inventory = player.getInventory();
		
		// Get Item where their book is (Guessing being held)
		ItemStack item;
		item = inventory.getItemInMainHand();
		
		// If doesn't exist, try get their off-hand item
		if (item == null)
		{
			item = inventory.getItemInOffHand();
			
			if (item == null)
				return;
			
		}
		
		// Resume if item has ItemMeta
		if (!item.hasItemMeta())
			return;
		
		// Get info about item
		ItemMeta meta2 = item.getItemMeta();
		
		// If item doesn't have ID
		if (!meta2.hasCustomModelData())
			return;
		
		// Get ID from item
		int id = meta2.getCustomModelData();
		
		// If ID matches the BugReporter book, resume
		if (id != BugReporter.id)
			return;
		
		// Remove Report book from Inventory
		item.setAmount(-1);
		e.setCancelled(true);
	}
	
	@EventHandler
	public static void preLogin (AsyncPlayerPreLoginEvent e)
	{	
		UUID uuid = e.getUniqueId();
		
		if (SQLManager.isEnabled())
			if (!SQLManager.containsPlayer(uuid))
				return;
		
		boolean isBanned = Moderation.isBanned(uuid);
		
		if (isBanned) {
			String reason = Moderation.getBanReason(uuid);
			Date date = Moderation.getBanDate(uuid);
			
			if (reason == null)
				
				e.disallow(Result.KICK_BANNED, String.format("%s> %sYou have been banned\n  Due Date: %s", ChatColor.GOLD, ChatColor.DARK_RED, date.toString()));
				//e.disallow(Result.KICK_BANNED, "You have been banned Due date: "+date.toString());
			else
				e.disallow(Result.KICK_BANNED, String.format("%s> %sYou have been banned\n  Due Date: %s\n  Reason: %s", ChatColor.GOLD, ChatColor.DARK_RED, date.toString(), reason));
				//e.disallow(Result.KICK_BANNED, "You have been banned Due date: "+date.toString() + "\nReason: "+reason);
			return;
		}
	}
	
	@EventHandler
	public static void onDeath(EntityDeathEvent e)
	{
		e.getEntity();
		e.getDrops().clear();
	}
	
	// Player Respawned
	@EventHandler
	public static void respawn(PlayerRespawnEvent e)
	{
		e.setRespawnLocation(spawnLocation);
	}
	
	// Variable Functions
	
	public static String getNormalMessageFormat() {
		return normalMessageFormat;
	}
	
	public static String getTagNormalMessageFormat() {
		return normalTagMessageFormat;
	}
	
	public static int getDistanceRange() {
		return distanceRange;
	}
	
	public static String getPMSentFormat() {
		return PMSentFormat;
	}
	
	public static String getPMReceivedFormat() {
		return PMReceivedFormat;
	}
	
	public String getPluginsFolder()  {
		return getConfig().getCurrentPath();
	}
	
	public static Location getSpawnLocation() {
		return spawnLocation;
	}
	
	public static JavaPlugin getPlugin() {
		return plugin;
	}
	
	private void setupGlobalVariables() {
		
		// Making a File with the location of the yml file then reading the variables inside the yml file.
		config = new File(dataFolderPath+"/config.yml");
		
		PrintConsole.print("config path: " + config.getAbsolutePath());
		
		// Get Default Config info and set the variables to the info.
		YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(config);
		
		/* Get all the variables in config and load them in under Main class to avoid much more write/read. */
		
		// Load the format on how messages should look
		normalMessageFormat = Config.getMessageFormat();
		// Load the format on how messages should look with tags
		normalTagMessageFormat = Config.getTagMessageFormat();
		
		// Load what the chat distance range in blocks. If 0 or lower, chat distance is turned off.
		distanceRange = Config.getChatDistance();
		
		// What private message looks like to the sender
		PMSentFormat = Config.getPrivateSentMessageFormat();
		// What private message looks like to the receiver
		PMReceivedFormat = Config.getPrivateReceivedMessageFormat();
		
		resourcePackLink = Config.getResourcePackURL();
		
		// Get the Spawn location including the world
		spawnLocation = Config.getSpawnLocation();
		// Get the spawn world, the world that players spawn
		World spawnWorld = spawnLocation.getWorld();
		// Assign the default world to spawn world
		world = spawnWorld;
		
		// Set World spawnpoint
		world.setSpawnLocation(spawnLocation);
		
		// Deny mob spawning, fire spreading, and random tick speed by gamerules in world
		world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
		world.setGameRule(GameRule.DO_FIRE_TICK, false);
		world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
		world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
		world.setGameRule(GameRule.KEEP_INVENTORY, true);
		
		// Set diffculty to peaceful
		world.setDifficulty(Difficulty.PEACEFUL);
		
		// Get how the message should look when a player joins. Leaving the text empty means no message
		joinMessage = yamlConfig.getConfigurationSection("Global Settings").getString("Join Message");
		// Get how the message should look when a player leaves. Leaving the text empty means no message
		leaveMessage = yamlConfig.getConfigurationSection("Global Settings").getString("Leave Message");
		// Get if players are allowed to drop items.
		allowDrop = yamlConfig.getConfigurationSection("Global Settings").getBoolean("Allow Item Drops");
		// Get if Explosions are enabled in this server
		explosionEnabled = yamlConfig.getConfigurationSection("Global Settings").getBoolean("Explosion Enabled");
		// Get if leaves can Decay in this server
		leavesDecayEnabled = yamlConfig.getConfigurationSection("Global Settings").getBoolean("Leaves Decay");
		// Get if ice can melt in this server
		iceMelts = yamlConfig.getConfigurationSection("Global Settings").getBoolean("Ice Melts");
		// Get if damage is enabled in this server
		damageAllowed = yamlConfig.getConfigurationSection("Global Settings").getBoolean("Allow Damage");
		// Get if fall damage is enabled in this server
		fallDamage = yamlConfig.getConfigurationSection("Global Settings").getBoolean("Fall Damage");
	}
	
	public static void displayWelcomeMessage(Player player) {
		String welcomeHeader = String.format("%s%s━━━━━━━━━━━━━━━━━━━━ %s%s Welcome back! %s%s━━━━━━━━━━━━━━", 
				ChatColor.YELLOW, ChatColor.STRIKETHROUGH, ChatColor.AQUA, ChatColor.BOLD, ChatColor.YELLOW, ChatColor.STRIKETHROUGH);
		
		BaseComponent[] components = new BaseComponent[5];
		components[0] = new TextComponent(String.format("%sTo view the rules, ", net.md_5.bungee.api.ChatColor.DARK_AQUA));
		components[1] = new TextComponent(String.format("%sclick here", net.md_5.bungee.api.ChatColor.AQUA));
		components[1].setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/rules"));
		components[2] = new TextComponent(String.format("%s and to view the credits ", net.md_5.bungee.api.ChatColor.DARK_AQUA));
		components[3] = new TextComponent(String.format("%sclick here", ChatColor.AQUA));
		components[3].setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/credits"));
		components[4] = new TextComponent(String.format(" %sfor everyone who helped out!",net.md_5.bungee.api.ChatColor.DARK_AQUA));
		
		String welcomeCloser = String.format("%s%s━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━", ChatColor.YELLOW, ChatColor.STRIKETHROUGH);
		
		player.sendMessage(welcomeHeader);
		player.sendMessage("");
		player.spigot().sendMessage(components);
		player.sendMessage("");
		player.sendMessage(welcomeCloser);
	}
	
	private void setupExtraCommands() {
		// Get CommandMap
		
		Field mapField;
		CommandMap map;
		
		try {
			mapField = this.getServer().getClass().getDeclaredField("commandMap");
		} 
		catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
			return;
		}
		
		mapField.setAccessible(true);
		
		// Get CommandMap from variable CommandMap
		try 
		{
			map = (CommandMap) mapField.get(Bukkit.getServer());
		} 
		catch (IllegalArgumentException | IllegalAccessException e) 
		{
			e.printStackTrace();
			return;
		}
		
		// Register commands!
		map.register("openkomodo", new PetCommand());
		map.register("openkomodo", new MsgCommand("msg", "Send someone a message!", "/msg (Player) (Message)", new ArrayList<String>(0)));
		map.register("openkomodo", new SitCommand("sit", "Take a seat", "/sit", new ArrayList<String>(0)));
		map.register("openkomodo", new SetHomeCommand("sethome", "Set a home to teleport", "/sethome (Name)", new ArrayList<String>(0)));
		map.register("openkomodo", new HomeCommand("home", "Teleport to your home", "/home (Name)", new ArrayList<String>(0)));
		map.register("openkomodo", new DelHomeCommand("delhome", "Delete your home", "/delhome (Name)", new ArrayList<String>(0)));
		map.register("openkomodo", new HomesCommand("homes", "Display the list of your current homes", "/homes", new ArrayList<String>(0)));
		map.register("openkomodo", new HatsCommand("hats", "Display the hat menu", "/hats", new ArrayList<String>(0)));
		map.register("openkomodo", new PropShopCommand("propshop", "Display the Prop Shop", "/propshop", new ArrayList<String>(0)));
		map.register("openkomodo", new ReportCommand("report", "Report a bug", "/report", new ArrayList<String>(0)));
		map.register("openkomodo", new LogCommand("log", "The newest update log", "/log", new ArrayList<String>(0)));
		map.register("openkomodo", new FlyCommand("fly", "Toggle fly mode", "/fly", new ArrayList<String>(0)));
		map.register("openkomodo", new EmoteCommand("emote", "The emote command", "/emote (Emote) {Optional: Target Player}", new ArrayList<String>(0)));
		map.register("openkomodo", new CheckBalanceCommand("balance", "Check your balance", "/balance", new ArrayList<String>(0)));
		map.register("openkomodo", new SpawnCommand("spawn", "Teleport to spawn", "/spawn", new ArrayList<String>(0)));
		map.register("openkomodo", new TeleportToBuildWorldCommand("buildworld", "Teleport to builder's world", "/buildworld", new ArrayList<String>(0)));
		map.register("openkomodo", new TpaCommand("tpa", "Request to teleport", "/tpa (Player)", new ArrayList<String>(0)));
		map.register("openkomodo", new TpaAcceptCommand("tpaccept", "Request to teleport", "/tpaccept", new ArrayList<String>(0)));
		
		map.register("openkomodo", new TpaHereCommand("tpahere", "Request a player to teleport to you.", "/tpahere (Player)", new ArrayList<String>(0)));
		map.register("openkomodo", new TpaToggleCommand("tptoggle", "Toggle tpa requests", "/tpatoggle", new ArrayList<String>(0)));
		map.register("openkomodo", new TagShopCommand("tagshop", "Open the tagshop", "/tagshop", new ArrayList<String>(0)));
		map.register("openkomodo", new PetsMenuCommand("pets", "Open the pets menu", "/pets", new ArrayList<String>(0)));
		map.register("openkomodo", new PayCommand("pay", "Pay a player with a currency", "/pay (Player) (Amount) (Currency Type)", new ArrayList<String>(0)));
		map.register("openkomodo", new MoneyCommand("money", "Info about money", "/money help", new ArrayList<String>(0)));
		map.register("openkomodo", new RankCommand("rank", "Info about ranks", "/rank help", new ArrayList<String>(0)));
		map.register("openkomodo", new RulesCommand("rules", "Display rules & terms", "/rules", new ArrayList<String>(0)));
		map.register("openkomodo", new CreditsCommand("credits", "Display credits", "/credits", new ArrayList<String>(0)));
		map.register("openkomodo", new VoteCommand("vote", "Displays voting websites", "/vote", new ArrayList<String>(0)));
		
		map.register("openkomodo_mod", new MuteCommand("mute", "mute a player", "/mute (Player) (Amount)", new ArrayList<String>(0)));
		map.register("openkomodo_mod", new KickCommand("adminkick", "kick a player", "/adminkick (Player) (Reason)", new ArrayList<String>(0)));
		map.register("openkomodo_mod", new BanCommand("ban", "ban a player", "/mute (Player) (Amount)", new ArrayList<String>(0)));
		
		map.register("openkomodo_builder", new BuildModeCommand("build", "Toggle build mode", "/build", new ArrayList<String>(0)));
		
		map.register("openkomodo_admin", new GenPayCommand("genpay", "Generate a Player's currency", "/genpay", new ArrayList<String>(0)));
		
		map.register("openkomodo_console", new GenTip("gentip", "Generate Player's tip", "/genpay", new ArrayList<String>(0)));
		map.register("openkomodo_admin", new PromoteCommand("promote", "Promote People's Ranks", "/promote {Player} {Add/Remove} {Player Name} {Permission}", new ArrayList<String>(0)));
		
		ArrayList<String> tpaDenyAtlas = new ArrayList<String>(1);
		tpaDenyAtlas.add("tpdeny");
		map.register("openkomodo", new TpaDenycommand("tpadeny", "Request to teleport", "/tpdeny", tpaDenyAtlas));
		
		// Add nickname command atlases
		ArrayList<String> nickAtlas = new ArrayList<String>(1);
		nickAtlas.add("nick");
		
		
		// Register the nickname command
		map.register("openkomodo", new NicknameCommand("nickname", "Change your nickname", "/nickname", nickAtlas));
		
		// Add nickname command atlases
		ArrayList<String> replyAtlas = new ArrayList<String>(1);
		replyAtlas.add("r");
		
		// Register the nickname command
		map.register("openkomodo", new ReplyCommand("reply", "Reply to a player", "/reply (message)", replyAtlas));
	}
}
