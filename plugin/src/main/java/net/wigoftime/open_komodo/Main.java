package net.wigoftime.open_komodo;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.FlowerPot;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

import de.tr7zw.nbtinjector.NBTInjector;
import net.wigoftime.open_komodo.actions.BugReporter;
import net.wigoftime.open_komodo.actions.Rules;
import net.wigoftime.open_komodo.chat.MessageFormat;
import net.wigoftime.open_komodo.chat.PrivateMessage;
import net.wigoftime.open_komodo.commands.BanCommand;
import net.wigoftime.open_komodo.commands.CheckBalanceCommand;
import net.wigoftime.open_komodo.commands.DelHomeCommand;
import net.wigoftime.open_komodo.commands.DisplayTagsCommand;
import net.wigoftime.open_komodo.commands.EmoteCommand;
import net.wigoftime.open_komodo.commands.FlyCommand;
import net.wigoftime.open_komodo.commands.FriendsCommand;
import net.wigoftime.open_komodo.commands.GenPayCommand;
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
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.config.WorldInventoryConfig;
import net.wigoftime.open_komodo.custommobs.CustomPetMob;
import net.wigoftime.open_komodo.etc.ActionBar;
import net.wigoftime.open_komodo.etc.CurrencyClass;
import net.wigoftime.open_komodo.etc.InventoryManagement;
import net.wigoftime.open_komodo.etc.Moderation;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.etc.PetsManager;
import net.wigoftime.open_komodo.etc.PlayerList;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.etc.RankSystem;
import net.wigoftime.open_komodo.etc.ServerScoreBoard;
import net.wigoftime.open_komodo.etc.Status_Bar;
import net.wigoftime.open_komodo.etc.TpSystem;
import net.wigoftime.open_komodo.etc.UpdateLog;
import net.wigoftime.open_komodo.events.AsyncPlayerChat;
import net.wigoftime.open_komodo.filecreation.CheckFiles;
import net.wigoftime.open_komodo.gui.GUIManager;
import net.wigoftime.open_komodo.gui.PetControl;
import net.wigoftime.open_komodo.gui.PhoneGui;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.ItemType;
import net.wigoftime.open_komodo.world.BuilderWorld;

public class Main extends JavaPlugin implements Listener 
{

	public static final String name = "Open Komodo";
	public static final String nameColoured = ChatColor.translateAlternateColorCodes('&', "&b&lOpen &a&lKomodo");
	private static final String welcomemsg = ChatColor.translateAlternateColorCodes('&', "&6Welcome back to Open Komodo!");
	private static final String firstWelcome = ChatColor.translateAlternateColorCodes('&', "&6Welcome &e%s &6to &b&lOpen &2&lKomodo!");
	
	public static final ZoneId zoneID = ZoneId.of("Australia/Sydney");
	
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	
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
	private static String joinMessage;
	private static String leaveMessage;
	
	private static boolean allowDrop;
	
	public static String dataFolderPath;
	public static File config;
	
	public static World world;
	private static Location spawnLocation;
	
	private static final String phoneName = ChatColor.translateAlternateColorCodes('&', "&e&lEPhone");
	private static final String cardName = ChatColor.translateAlternateColorCodes('&', "&6&lBank");
	
	private static final String resourcePackLink = "https://www.dropbox.com/s/zy8j6alsyk2uk4g/OpenKomodo%20Resourcepack.zip?dl=1";
	
	private static JavaPlugin plugin;
	
	// When plugin starts loading
	@Override
	public void onLoad()
	{
		//	Say that the plugin is loading
		PrintConsole.print("Loading..");
		
		// Setup all the custom NBT, loading them as well
		NBTInjector.inject();
	}
	
	// When Plugin is enabled
	@Override
	public void onEnable() 
	{
		// Say to server that it is enabled
		PrintConsole.print("Enabled");
		
		// Setup the plugin variable
		plugin = this;
		
		// Register the Events with this class, with this plugin
		Bukkit.getPluginManager().registerEvents(this, this);
		
		// Setup the serverURL (Really a path)
		dataFolderPath = getDataFolder().getAbsolutePath();
		
		// Check the files (Like if config exists) and setup the files so it is ready to be used.
		CheckFiles.checkFiles();
		
		// Setup Builderworld
		BuilderWorld.setup();
		
		// Making a File with the location of the yml file then reading the variables inside the yml file.
		config = new File(dataFolderPath+"/config.yml");
		
		PrintConsole.print("config path: " + config.getAbsolutePath());
		
		// Get Default Config info and set the variables to the info.
		YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(config);
		
		/* Get all the variables in config and load them in under Main class to avoid much more write/read. */
		
		// Load the format on how messages should look
		normalMessageFormat = yamlConfig.getConfigurationSection("Normal Message").getString("Format");
		// Load the format on how messages should look with tags
		normalTagMessageFormat = yamlConfig.getConfigurationSection("Normal Message").getString("Tag Format");
		
		// Load what the chat distance range in blocks. If 0 or lower, chat distance is turned off.
		distanceRange = yamlConfig.getConfigurationSection("Normal Message").getInt("Chat Distance");
		
		// What private message looks like to the sender
		PMSentFormat = yamlConfig.getConfigurationSection("Private Message").getString("Format (Sent)");
		// What private message looks like to the receiver
		PMReceivedFormat = yamlConfig.getConfigurationSection("Private Message").getString("Format (Received)");
		
		// Get the spawn world, the world that players spawn
		World spawnWorld = Bukkit.getWorld(yamlConfig.getConfigurationSection("Global Settings").getConfigurationSection("Spawn").getString("World"));
		// Get the coordinations to spawn
		Vector spawnVector = yamlConfig.getConfigurationSection("Global Settings").getConfigurationSection("Spawn").getVector("Location");
		// Get the Spawn location including the world
		spawnLocation = new Location(spawnWorld, spawnVector.getX(), spawnVector.getY(), spawnVector.getZ());
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
		
		// Loop through each player and set up the permissions and display the rank prefix.
		// Useful when reloading the server.
		for (Player player : Bukkit.getOnlinePlayers()) 
		{
			Permissions.setUp(player);
			ServerScoreBoard.add(player);
		}
		
		// Create CustomItem for errors.
		ItemStack is = new ItemStack(Material.INK_SAC);
		CustomItem.create(is, -99, -1, null, ItemType.PROP);
		
		Status_Bar.startLoop();
		ActionBar.startLoop();
		PetsManager.startLoop();
		
		// Get CommandMap
		
		Field mapField;
		CommandMap map;
		
		try 
		{
			mapField = this.getServer().getClass().getDeclaredField("commandMap");
		} 
		catch (NoSuchFieldException | SecurityException e) 
		{
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
		map.register("openkomodo", new MsgCommand("msg", "Send someone a message!", "/msg (Player) (Message)", new ArrayList<String>()));
		map.register("openkomodo", new SitCommand("sit", "Take a seat", "/sit", new ArrayList<String>()));
		map.register("openkomodo", new SetHomeCommand("sethome", "Set a home to teleport", "/sethome (Name)", new ArrayList<String>()));
		map.register("openkomodo", new HomeCommand("home", "Teleport to your home", "/home (Name)", new ArrayList<String>()));
		map.register("openkomodo", new DelHomeCommand("delhome", "Delete your home", "/delhome (Name)", new ArrayList<String>()));
		map.register("openkomodo", new HomesCommand("homes", "Display the list of your current homes", "/homes", new ArrayList<String>()));
		map.register("openkomodo", new HatsCommand("hats", "Display the hat menu", "/hats", new ArrayList<String>()));
		map.register("openkomodo", new PropShopCommand("propshop", "Display the Prop Shop", "/propshop", new ArrayList<String>()));
		map.register("openkomodo", new ReportCommand("report", "Report a bug", "/report", new ArrayList<String>()));
		map.register("openkomodo", new LogCommand("log", "The newest update log", "/log", new ArrayList<String>()));
		map.register("openkomodo", new FlyCommand("fly", "Toggle fly mode", "/fly", new ArrayList<String>()));
		map.register("openkomodo", new EmoteCommand("emote", "The emote command", "/emote (Emote) {Optional: Target Player}", new ArrayList<String>()));
		map.register("openkomodo", new CheckBalanceCommand("balance", "Check your balance", "/balance", new ArrayList<String>()));
		map.register("openkomodo", new SpawnCommand("spawn", "Teleport to spawn", "/spawn", new ArrayList<String>()));
		map.register("openkomodo", new TeleportToBuildWorldCommand("buildworld", "Teleport to builder's world", "/buildworld", new ArrayList<String>()));
		map.register("openkomodo", new DisplayTagsCommand("buildworld", "Teleport to builder's world", "/buildworld", new ArrayList<String>()));
		map.register("openkomodo", new TpaCommand("tpa", "Request to teleport", "/tpa (Player)", new ArrayList<String>()));
		map.register("openkomodo", new TpaAcceptCommand("tpaccept", "Request to teleport", "/tpaccept", new ArrayList<String>()));
		map.register("openkomodo", new TpaHereCommand("tpahere", "Request a player to teleport to you.", "/tpahere (Player)", new ArrayList<String>()));
		map.register("openkomodo", new FriendsCommand("friend", "A WIP Command: Display friends gui", "/friends", new ArrayList<String>()));
		map.register("openkomodo", new TagShopCommand("tagshop", "Open the tagshop", "/tagshop", new ArrayList<String>()));
		map.register("openkomodo", new PetsMenuCommand("pets", "Open the pets menu", "/pets", new ArrayList<String>()));
		map.register("openkomodo", new PayCommand("pay", "Pay a player with a currency", "/pay (Player) (Amount) (Currency Type)", new ArrayList<String>()));
		map.register("openkomodo", new MoneyCommand("money", "Info about money", "/money help", new ArrayList<String>()));
		map.register("openkomodo", new RankCommand("rank", "Info about ranks", "/rank help", new ArrayList<String>()));
		map.register("openkomodo", new RulesCommand("rules", "Display rules & terms", "/rules", new ArrayList<String>()));
		
		map.register("openkomodo_mod", new MuteCommand("mute", "mute a player", "/mute (Player) (Amount)", new ArrayList<String>()));
		map.register("openkomodo_mod", new KickCommand("adminkick", "kick a player", "/adminkick (Player) (Reason)", new ArrayList<String>()));
		map.register("openkomodo_mod", new BanCommand("ban", "ban a player", "/mute (Player) (Amount)", new ArrayList<String>()));
		map.register("openkomodo_admin", new GenPayCommand("genpay", "Generate a Player's currency", "/genpay", new ArrayList<String>()));
		map.register("openkomodo_admin", new PromoteCommand("promote", "Promote People's Ranks", "/promote {Player} {Add/Remove} {Player Name} {Permission}", new ArrayList<String>()));
		
		ArrayList<String> tpaDenyAtlas = new ArrayList<String>();
		tpaDenyAtlas.add("/tpdeny");
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
	
	@Override
	public void onDisable()
	{
		
		// Save everyone's inventory
		for (Player player : Bukkit.getOnlinePlayers())
		{
			InventoryManagement.saveInventory(player, player.getWorld());
		}
	}
	
	/* Events Below */
	
	// When something in inventory is clicked.
	@EventHandler 
	public void inventoryClick(InventoryClickEvent e) 
	{	
		GUIManager.invItemClicked(e);
	}
	
	// When someone tries to drops an item
	@EventHandler
	public void dropItem(PlayerDropItemEvent e) 
	{
		// If server allows items to be dropped, allow
		if (allowDrop)
			return;
		
		// If the player themselves has permission to drop, allow
		if (e.getPlayer().hasPermission(Permissions.dropPerm))
			return;
		
		// Otherwise, stop the player from dropping their item
		e.setCancelled(true);
		// Message the player that it isn't allowed
		e.getPlayer().sendMessage(Permissions.getDropError());
	}
	
	// When block is tried to be placed
	@EventHandler
	public void blockPlaced(BlockPlaceEvent e) 
	{
		// Get Player
		Player player = e.getPlayer();
		
		// If player has permission to drop, allow
		if (player.hasPermission(Permissions.placePerm))
			return;
		
		// Otherwise, cancel the item drop
		e.setCancelled(true);
		// Message the player that they are not allowed
		player.sendMessage(Permissions.getPlaceError());
		return;
		
	}
	
	// When player tries to break a block
	@EventHandler
	public void blockBreaked(BlockBreakEvent e) 
	{
		// Get Player
		Player player = e.getPlayer();
		
		// Allow if player has permission
		if (player.hasPermission(Permissions.breakPerm))
			return;
		
		// Otherwise, cancel the block break
		e.setCancelled(true);
		// Message the player that they are not allowed
		player.sendMessage(Permissions.getBreakError());
		return;
	}
	
	@EventHandler
	public void fertilizedEvent(BlockFertilizeEvent e)
	{
		
		Player player = e.getPlayer();
		
		if (player.hasPermission(Permissions.breakPerm))
			e.setCancelled(true);
	}
	
	@EventHandler
	public void ignitevent(BlockIgniteEvent e)
	{
		Player player = e.getPlayer();
		
		if (e.getCause() != IgniteCause.FLINT_AND_STEEL)
			return;
		
		if (player.hasPermission(Permissions.breakPerm))
			e.setCancelled(true);
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
		
		// Get Player
		Player player = (Player) e.getAttacker();
		
		// If player has permission, return
		if (player.hasPermission(Permissions.changePerm))
			return;
		
		// Cancel vehicle being destroyed
		e.setCancelled(true);
		// Send message to player that it isn't allowed
		player.sendMessage(Permissions.getChangeError());
	}
	
	// An Event for example when player takes off or put Armor Stand
	@EventHandler
	public void playerInteractAt(PlayerInteractAtEntityEvent e) 
	{
		Player player = e.getPlayer();
		
		if (e.getRightClicked() == PetsManager.getCreature(player))
		{
			e.setCancelled(true);
			PetControl.create(player);
		}
		
		if (player.hasPermission(Permissions.changePerm)) 
			return;
		
		if (e.getRightClicked().getType() == EntityType.ARMOR_STAND) 
		{
			e.setCancelled(true);
			e.getPlayer().sendMessage(Permissions.getChangeError());
			return;
		}
		
	}
	
	// If Entity dismounts something
	@EventHandler
	public void dismounted(EntityDismountEvent e)
	{
		Entity entity = e.getDismounted();
		
		if (e.getEntity().getType() != EntityType.PLAYER)
			return;
		
		if (entity.getType() != EntityType.ARROW)
			return;
		
		entity.remove();
		return;
	}
	
	@EventHandler
	public void interactedEntity(PlayerInteractEntityEvent e) 
	{
		Player player = e.getPlayer();
		Entity entity = e.getRightClicked();
		
		if (GUIManager.getEntityGui(entity, player))
		{
			e.setCancelled(true);
			return;
		}
		
		if (player.hasPermission(Permissions.changePerm))
			return;
				
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
		
		new Vector(50, 50, -250);
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
		
		// Get Player
		Player player = (Player) e.getDamager();
		
		// If player has permission to damage entity
		if (player.hasPermission(Permissions.hurtPerm)) 
			return;
		
		// Other wise, cancel it and tell player that they are not allowed
		e.setCancelled(true);
		player.sendMessage(Permissions.getHurtError());
	}
	
	@EventHandler
	public void playerMove(PlayerMoveEvent e)
	{
		Player player = e.getPlayer();
		
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
		
		// Get player
		Player player = (Player) e.getRemover();
		
		// If player has permission
		if (player.hasPermission(Permissions.changePerm))
			return;
		
		// Otherwise, canscel and send a msg that they are not allowed
		e.setCancelled(true);
		player.sendMessage(Permissions.getChangeError());
		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) 
	{	
		// Get Player
		Player player = e.getPlayer();
		
		// Get Message
		String message = joinMessage;
		
		// Unop the player to sync with the permissions
		if (player.isOp())
		player.setOp(false);
		
		// Check if player has it's own player config file, if not, they are new to the server.
		File file = new File(dataFolderPath+"/Players/"+e.getPlayer().getUniqueId()+".yml");
		if (!file.exists()) 
		{	
			PlayerConfig.create(player);
			
			for (Player p : Bukkit.getServer().getOnlinePlayers())
			{
				p.sendMessage(String.format(firstWelcome, player.getName()));
			}
		}
		
		// If created config but no joined date (Like if they were banned before coming onto the server)
		if (PlayerConfig.firstJoined(player))
		{
			PlayerConfig.setJoinDate(player);
			
			for (Player p : Bukkit.getServer().getOnlinePlayers())
			{
				p.sendMessage(String.format(firstWelcome, player.getName()));
			}
		}
		
		// Teleport player to spawn
		player.teleport(spawnLocation);
		
		// Get saved items from inventory
		InventoryManagement.loadInventory(player, player.getWorld());
		
		// Create Phone
		ItemStack phone = new ItemStack(Material.INK_SAC); 
		
		// Set display and ID
		ItemMeta meta = phone.getItemMeta();
		meta.setDisplayName(phoneName);
		meta.setCustomModelData(1);
		
		// Save changes
		phone.setItemMeta(meta);
		
		// Create FPBank Card
		ItemStack card = new ItemStack(Material.FLINT);
		
		// Set Display and ID
		ItemMeta meta2 = card.getItemMeta();
		meta2.setDisplayName(cardName);
		
		// Save Changes
		card.setItemMeta(meta2);
		
		// Add items to player's slots
		player.getInventory().setItem(8, phone);
		player.getInventory().setItem(7, card);
		
		// If Message is empty, join message does not exist
		if (message == "")
			e.setJoinMessage(null);
		// Else set join message
		else
		{
		// Format message
		message = MessageFormat.format(player, message);
		
		// Format colours
		message = ChatColor.translateAlternateColorCodes('&', message);
		
		// Send join message
		e.setJoinMessage(message);
		}
		
		// Display welcome message!
		player.sendMessage(welcomemsg);
		
		// Display rules
		Rules.display(player);
		
		// Setup the sidebar, playerlist prefix rank, etc
		ServerScoreBoard.add(player);
		Status_Bar.addPlayer(player);
		PlayerList.add(player);
		
		// Setup player's permissions
		Permissions.setUp(player);
		
		// Set Gamemode to survival, in case of other worlds
		player.setGameMode(GameMode.SURVIVAL);
		
		// Make players not collide to each other and unable to pickup stuff
		player.setCanPickupItems(false);
		player.setCollidable(false);
		
		// Put player in rank system
		RankSystem.putPlayer(player);
		
		// Checks if they read the UpdateLog before
		UpdateLog.onJoin(player);
		
		// Give player resourcepack
		player.setResourcePack(resourcePackLink);
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
		
		// Get Player
		Player player = e.getPlayer();
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
			if (player.getTargetBlock((Set<Material>) null, 20).getType() == Material.FIRE)
			{
				// If player does not have permission, cancel and return
				if (!player.hasPermission(Permissions.changePerm))
				{
					player.sendMessage(Permissions.getChangeError());
					
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
			
			if (player.hasPermission(Permissions.changePerm))
				return;
			
			if (material == Material.OAK_TRAPDOOR || material == Material.SPRUCE_TRAPDOOR || material == Material.JUNGLE_TRAPDOOR || material == Material.DARK_OAK_TRAPDOOR || material == Material.BIRCH_TRAPDOOR || material == Material.ACACIA_TRAPDOOR)
				e.setCancelled(true);
			
			if (material == Material.FLOWER_POT || material.name().startsWith("POTTED"))
			{
				player.sendMessage(Permissions.getChangeError());
				e.setCancelled(true);
			}
			
			// If Enderchest
			if (block.getType() == Material.ENDER_CHEST)
			{
				// Allow open if player has permission
				if (player.hasPermission(Permissions.changePerm))
					return;
				
				// Other wise, cancel it
				e.setCancelled(true);
				player.sendMessage(Permissions.useError);
			}
		}
		
		if (item == null)
			return;
		
		// Checks if Item could have an ID
		if (item.getItemMeta().hasCustomModelData())
		{
			// If ID is 1 (IPlay Phone)
			if (item.getItemMeta().getCustomModelData() == 1)
				// And if the item is an ink sac, open IPlay
				if (item.getType() == Material.INK_SAC) 
				{	
					e.setCancelled(true);
					PhoneGui.open(player);
					return;
				}
			
			if (item.getType() == Material.STICK)
			{
				InventoryManagement.openBagInventory(player, item.getItemMeta().getCustomModelData());
			}
		}
		
		// If Item is a flint, display balance (Card item)
		if (item.getType() == Material.FLINT) 
		{
			e.setCancelled(true);
			CurrencyClass.displayBalance(player);
			return;
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) 
	{
		// Get Player
		Player player = e.getPlayer();
		
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
		if (player.isOp())
			player.setOp(false);
		
		TpSystem.playerLeft(player);
		PrivateMessage.playerLeft(player.getUniqueId());
	}
	
	@EventHandler
	public void switchWorld(PlayerChangedWorldEvent e)
	{
		Player player = e.getPlayer();
		
		World previousWorld = e.getFrom();
		World world = player.getWorld();
		
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
		Commands.commands(e);
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
			
			return;
		}
		
		// If it is FPBank card
		if (item.getType() == Material.FLINT)
			e.setCancelled(true);
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
		
		
		if (Moderation.isBanned(uuid))
		{
			String reason = PlayerConfig.getBanReason(e.getUniqueId());
			
			if (reason == null)
				e.disallow(Result.KICK_BANNED, "You have been banned");
			else
				e.disallow(Result.KICK_BANNED, "You have been banned\nReason: "+reason);
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
	
}
