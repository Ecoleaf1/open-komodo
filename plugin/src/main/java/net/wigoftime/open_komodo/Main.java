package net.wigoftime.open_komodo;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import github.scarsz.discordsrv.DiscordSRV;
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
import net.wigoftime.open_komodo.commands.DisplayXPCommand;
import net.wigoftime.open_komodo.commands.EmoteCommand;
import net.wigoftime.open_komodo.commands.FlyCommand;
import net.wigoftime.open_komodo.commands.GenPayCommand;
import net.wigoftime.open_komodo.commands.GenTip;
import net.wigoftime.open_komodo.commands.HatsCommand;
import net.wigoftime.open_komodo.commands.HomeCommand;
import net.wigoftime.open_komodo.commands.HomesCommand;
import net.wigoftime.open_komodo.commands.InvisibleCommand;
import net.wigoftime.open_komodo.commands.KickCommand;
import net.wigoftime.open_komodo.commands.LogCommand;
import net.wigoftime.open_komodo.commands.ModTeleportCommand;
import net.wigoftime.open_komodo.commands.ModTeleportHereCommand;
import net.wigoftime.open_komodo.commands.MoneyCommand;
import net.wigoftime.open_komodo.commands.MonitorChatCommand;
import net.wigoftime.open_komodo.commands.MsgCommand;
import net.wigoftime.open_komodo.commands.MuteCommand;
import net.wigoftime.open_komodo.commands.NicknameCommand;
import net.wigoftime.open_komodo.commands.OutfitTemplateCommand;
import net.wigoftime.open_komodo.commands.PayCommand;
import net.wigoftime.open_komodo.commands.PetCommand;
import net.wigoftime.open_komodo.commands.PetsMenuCommand;
import net.wigoftime.open_komodo.commands.PromoteCommand;
import net.wigoftime.open_komodo.commands.PropShopCommand;
import net.wigoftime.open_komodo.commands.RankCommand;
import net.wigoftime.open_komodo.commands.ReplyCommand;
import net.wigoftime.open_komodo.commands.ReportCommand;
import net.wigoftime.open_komodo.commands.ResourceCommand;
import net.wigoftime.open_komodo.commands.RulesCommand;
import net.wigoftime.open_komodo.commands.SetHomeCommand;
import net.wigoftime.open_komodo.commands.SitCommand;
import net.wigoftime.open_komodo.commands.SpawnCommand;
import net.wigoftime.open_komodo.commands.TagShopCommand;
import net.wigoftime.open_komodo.commands.TagsCommand;
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
import net.wigoftime.open_komodo.events.CommandPreprocess;
import net.wigoftime.open_komodo.events.VotifierEvent;
import net.wigoftime.open_komodo.filecreation.CheckFiles;
import net.wigoftime.open_komodo.gui.CustomGUI;
import net.wigoftime.open_komodo.gui.GUIManager;
import net.wigoftime.open_komodo.gui.PetControl;
import net.wigoftime.open_komodo.gui.PetsGui;
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
	private static Object discordSRV;
	public static ProtocolManager protocolManager;
	
	public static final String nameColoured = ChatColor.translateAlternateColorCodes('&', "&b&lOpen &a&lKomodo");
	public static final String firstWelcome = ChatColor.translateAlternateColorCodes('&', "&6Welcome &e%s &6to &b&lOpen &2&lKomodo!");
	
	private static String normalMessageFormat;
	private static String normalTagMessageFormat;
	
	private static int distanceRange;
	
	private static String PMSentFormat;
	private static String PMReceivedFormat;
	
	public static boolean explosionEnabled;
	public static boolean leavesDecayEnabled;
	
	public static boolean fallDamage;
	public static boolean damageAllowed;
	
	public static boolean iceMelts;
	public static String joinMessage;
	public static String leaveMessage;
	
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
		protocolManager = ProtocolLibrary.getProtocolManager();
		
		Bukkit.getPluginManager().registerEvent(PlayerJoinEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.PlayerJoin(), this);
		Bukkit.getPluginManager().registerEvent(PlayerDropItemEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.DropItem(), this);
		Bukkit.getPluginManager().registerEvent(BlockPlaceEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.BlockPlace(), this);
		Bukkit.getPluginManager().registerEvent(BlockBreakEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.BlockBreak(), this);
		Bukkit.getPluginManager().registerEvent(VehicleDestroyEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.VehicleDestroyEvent(), this);
		Bukkit.getPluginManager().registerEvent(PlayerInteractAtEntityEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.PlayerInteractAtEntityEvent(), this);
		Bukkit.getPluginManager().registerEvent(PlayerInteractEntityEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.PlayerInteractEntityEvent(), this);
		Bukkit.getPluginManager().registerEvent(EntityDamageByEntityEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.EntityDamageByEntityEvent(), this);
		Bukkit.getPluginManager().registerEvent(PlayerMoveEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.PlayerMove(), this);
		Bukkit.getPluginManager().registerEvent(EntityDismountEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.EntityDismount(), this);
		Bukkit.getPluginManager().registerEvent(BlockFadeEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.BlockFade(), this);
		Bukkit.getPluginManager().registerEvent(HangingBreakByEntityEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.HangingBreakByEntity(), this);
		Bukkit.getPluginManager().registerEvent(PlayerEggThrowEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.PlayerEggThrow(), this);
		Bukkit.getPluginManager().registerEvent(PlayerInteractEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.PlayerInteract(), this);
		Bukkit.getPluginManager().registerEvent(PlayerChangedWorldEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.PlayerChangedWorld(), this);
		Bukkit.getPluginManager().registerEvent(PlayerQuitEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.PlayerQuit(), this);
		Bukkit.getPluginManager().registerEvent(ExplosionPrimeEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.ExplosionPrime(), this);
		Bukkit.getPluginManager().registerEvent(EntityDamageEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.EntityDamage(), this);
		Bukkit.getPluginManager().registerEvent(PlayerSwapHandItemsEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.PlayerSwapHandItems(), this);
		Bukkit.getPluginManager().registerEvent(PlayerEditBookEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.PlayerEditBook(), this);
		Bukkit.getPluginManager().registerEvent(AsyncPlayerPreLoginEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.AsyncPlayerPreLogin(), this);
		Bukkit.getPluginManager().registerEvent(EntityDeathEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.EntityDeath(), this);
		Bukkit.getPluginManager().registerEvent(PlayerRespawnEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.PlayerRespawn(), this);
		Bukkit.getPluginManager().registerEvent(PlayerCommandPreprocessEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.CommandPreprocess(), this);
		Bukkit.getPluginManager().registerEvent(AsyncPlayerChatEvent.class, listener, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.AsyncPlayerChat(), this);
		
		
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
		ServerScoreBoard.createBoard();
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
	
	@EventHandler
	public void inventoryClose(InventoryCloseEvent e)
	{
		GUIManager.inventoryClosed(e);
	}
	
	// Variable Functions
	public static Object getDiscordSRV() {
		return discordSRV;
	}
	
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
		
		joinMessage = yamlConfig.getConfigurationSection("Global Settings").getString("Join Message");
		leaveMessage = yamlConfig.getConfigurationSection("Global Settings").getString("Leave Message");
		allowDrop = yamlConfig.getConfigurationSection("Global Settings").getBoolean("Allow Item Drops");
		explosionEnabled = yamlConfig.getConfigurationSection("Global Settings").getBoolean("Explosion Enabled");
		leavesDecayEnabled = yamlConfig.getConfigurationSection("Global Settings").getBoolean("Leaves Decay");
		iceMelts = yamlConfig.getConfigurationSection("Global Settings").getBoolean("Ice Melts");
		damageAllowed = yamlConfig.getConfigurationSection("Global Settings").getBoolean("Allow Damage");
		fallDamage = yamlConfig.getConfigurationSection("Global Settings").getBoolean("Fall Damage");
		
		try {
			discordSRV = DiscordSRV.getPlugin();
		} catch (NoClassDefFoundError error) {
		discordSRV = null;
		}
	}
	
	public static void displayWelcomeMessage(Player player) {
		String welcomeHeader = String.format("%s%s━━━━━━━━━━━━━━━━━━━━ %s%s Welcome back! %s%s━━━━━━━━━━━━━━", 
				ChatColor.YELLOW, ChatColor.STRIKETHROUGH, ChatColor.AQUA, ChatColor.BOLD, ChatColor.YELLOW, ChatColor.STRIKETHROUGH);
		
		BaseComponent[] components = new BaseComponent[5];
		components[0] = new TextComponent(String.format("%sTo view the rules, ", net.md_5.bungee.api.ChatColor.DARK_AQUA));
		components[0].setColor(net.md_5.bungee.api.ChatColor.DARK_AQUA);
		
		components[1] = new TextComponent(String.format("%sclick here", net.md_5.bungee.api.ChatColor.AQUA));
		components[1].setColor(net.md_5.bungee.api.ChatColor.AQUA);
		components[1].setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/rules"));
		
		components[2] = new TextComponent(String.format("%s and to view the credits ", net.md_5.bungee.api.ChatColor.DARK_AQUA));
		components[2].setColor(net.md_5.bungee.api.ChatColor.DARK_AQUA);
		
		components[3] = new TextComponent(String.format("%sclick here", ChatColor.AQUA));
		components[3].setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/credits"));
		components[3].setColor(net.md_5.bungee.api.ChatColor.AQUA);
		
		components[4] = new TextComponent(String.format(" %sfor everyone who helped out!",net.md_5.bungee.api.ChatColor.DARK_AQUA));
		components[4].setColor(net.md_5.bungee.api.ChatColor.DARK_AQUA);
		
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
		
		// Register commands
		map.register("openkomodo", new ResourceCommand("resource", "Reload or download resourcepack maunally", "/resource", new ArrayList<String>(0)));
		map.register("openkomodo", new PetCommand());
		map.register("openkomodo", new OutfitTemplateCommand("outfit", "Dress up for school!", "/outfit", new ArrayList<String>(0)));
		map.register("openkomodo", new DisplayXPCommand("xp", "Displays current xp", "/xp", new ArrayList<String>(0)));
		
		ArrayList<String> msgAtlas = new ArrayList<String>(1);
		msgAtlas.add("msg");
		msgAtlas.add("whisper");
		msgAtlas.add("tell");
		map.register("openkomodo", new MsgCommand("message", "Send someone a message!", "/msg (Player) (Message)", msgAtlas));
		
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
		map.register("openkomodo", new TpaCommand("tpa", "Request to teleport", "/tpa (Player)", Arrays.asList("teleport", "tp")));
		map.register("openkomodo", new TpaAcceptCommand("tpaccept", "Request to teleport", "/tpaccept", new ArrayList<String>(0)));
		
		map.register("openkomodo", new TpaHereCommand("tpahere", "Request a player to teleport to you.", "/tpahere (Player)", new ArrayList<String>(0)));
		map.register("openkomodo", new TpaToggleCommand("tptoggle", "Toggle tpa requests", "/tpatoggle", new ArrayList<String>(0)));
		map.register("openkomodo", new TagsCommand("tags", "Open the tags menu", "/tags", new ArrayList<String>(0)));
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
		map.register("openkomodo_mod", new BanCommand("ban", "ban a player", "/ban (Player) (Amount)", new ArrayList<String>(0)));
		
		map.register("openkomodo_mod", new ModTeleportCommand("adminteleport", "Teleport to another player without requesting", "/adminteleport {Player}", Arrays.asList("atp")));
		map.register("openkomodo_mod", new ModTeleportHereCommand("adminteleporthere", "Teleport player to you without requesting", "/adminteleporthere {Player}", Arrays.asList("atphere")));
		map.register("openkomodo_mod", new MonitorChatCommand("monitor", "Monitor chat", "/monitor", new ArrayList<String>(0)));
		map.register("openkomodo_mod", new InvisibleCommand("invisible", "Turn yourself invisible", "/invisible", Arrays.asList("invis", "vanish")));
		
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
