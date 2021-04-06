package net.wigoftime.open_komodo.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.potion.PotionEffectType;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.chat.MessageFormat;
import net.wigoftime.open_komodo.etc.CollisionSystem;
import net.wigoftime.open_komodo.etc.InventoryManagement;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.etc.PlayerList;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.etc.RankSystem;
import net.wigoftime.open_komodo.etc.ServerScoreBoard;
import net.wigoftime.open_komodo.etc.Status_Bar;
import net.wigoftime.open_komodo.etc.UpdateLog;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.Rank;
import net.wigoftime.open_komodo.sql.SQLManager;

public class PlayerJoin implements EventExecutor {
	public void execute(Listener listener, Event event) {
		PlayerJoinEvent joinEvent = (PlayerJoinEvent) event;
		
		joinEvent.setJoinMessage(null);
		joinEvent.getPlayer().setCollidable(false);
		PrintConsole.test("Is collidable? "+ joinEvent.getPlayer().isCollidable());
		
		// Teleport player to spawn
		joinEvent.getPlayer().teleport(Main.spawnLocation);
		
		joinEvent.getPlayer().sendTitle(ChatColor.AQUA + "Loading", ChatColor.DARK_AQUA + "Loading information from database", 10, 140, 10);
		
		Runnable runnable = new Runnable() {
			public void run() {
				firstJoined(joinEvent.getPlayer());
				
				if (SQLManager.isEnabled()) {
					if (!SQLManager.containsWorldPlayer(joinEvent.getPlayer().getUniqueId(), joinEvent.getPlayer().getWorld().getName()))
						SQLManager.createWorldPlayer(joinEvent.getPlayer().getUniqueId(), joinEvent.getPlayer().getWorld().getName());
				}
				
				// Create CustomPlayer to hold custom Variables.
				CustomPlayer playerCustomPlayer = new CustomPlayer(joinEvent.getPlayer());
				
				// Get saved items from inventory
				InventoryManagement.loadInventory(playerCustomPlayer, playerCustomPlayer.getPlayer().getWorld());
				
				// Put player in rank system
				RankSystem.putPlayer(playerCustomPlayer.getPlayer());
				
				// Setup player's permissions
				Permissions.setUp(playerCustomPlayer);
				
				playerCustomPlayer.setupCustomName();
				
				Runnable mainThreadSetup = new Runnable() {
					
					public void run() {
						if (!playerCustomPlayer.getSettings().isPlayerParticlesEnabled())
							Main.particlesApi.togglePlayerParticleVisibility(playerCustomPlayer.getPlayer(), true);
						
						// Add items to player's slots
						playerCustomPlayer.getPlayer().getInventory().setItem(8, playerCustomPlayer.getSettings().getPhone().getItem());
						playerCustomPlayer.getPlayer().getInventory().setItem(7, getCard());
						
						playerCustomPlayer.getPlayer().removePotionEffect(PotionEffectType.GLOWING);
						joinMessage(playerCustomPlayer, joinEvent);
						
						// Display welcome message!
						Main.displayWelcomeMessage(playerCustomPlayer.getPlayer());
						
						CollisionSystem.playerJoins(joinEvent.getPlayer());
						Status_Bar.addPlayer(playerCustomPlayer);
						PlayerList.add(playerCustomPlayer);
						// Setup the sidebar, playerlist prefix rank, etc
						playerCustomPlayer.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
						ServerScoreBoard.add(playerCustomPlayer);
						ServerScoreBoard.sync(playerCustomPlayer);
						
						// Set Gamemode to survival, in case of other worlds
						playerCustomPlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
						
						// Make players not collide to each other and unable to pickup stuff
						playerCustomPlayer.getPlayer().setCanPickupItems(false);
						
						// Checks if they read the UpdateLog before
						UpdateLog.onJoin(playerCustomPlayer.getPlayer());
						
						// Give player resourcepack
						playerCustomPlayer.getPlayer().setResourcePack(Main.resourcePackLink);
						
						playerCustomPlayer.refreshXPBar();
						playerCustomPlayer.getPlayer().sendTitle(ChatColor.YELLOW + "Finished loading", ChatColor.DARK_AQUA + "Welcome to "+Main.nameColoured+ ChatColor.DARK_AQUA + "!", 10, 140, 10);
						
						if (playerCustomPlayer.isNew) playerCustomPlayer.setTutorial(true);
					}
				};
				
				Bukkit.getScheduler().runTask(Main.getPlugin(), mainThreadSetup);
			}
		};
		
		Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), runnable);
	}
	
	private void firstJoined(Player player) {
		// Check if player exists in the SQL database or has it's own player config file
		if (!CustomPlayer.containsPlayer(player.getUniqueId())) {	
			for (Player p : Bukkit.getServer().getOnlinePlayers())
				p.sendMessage(String.format(Main.firstWelcome, player.getPlayer().getName()));
		}
	}
	
	private ItemStack getCard() {
		// Create FPBank Card
		ItemStack cardItemStack = new ItemStack(Material.INK_SAC);
		
		// Set Display and ID
		ItemMeta cardItemMeta = cardItemStack.getItemMeta();
		cardItemMeta.setCustomModelData(56);
		cardItemMeta.setDisplayName(Main.cardName);
		
		// Save Changes
		cardItemStack.setItemMeta(cardItemMeta);
		
		return cardItemStack;
	}
	
	private void joinMessage(CustomPlayer customPlayer, PlayerJoinEvent joinEvent) {
		// Get Message
		String message = Main.joinMessage;
		
		// If Message is empty, join message does not exist
		if (message == "")
			joinEvent.setJoinMessage(null);
		// Else set join message
		else {
			// Format message
			message = MessageFormat.format(customPlayer, message);
			
			// Format colours
			message = ChatColor.translateAlternateColorCodes('&', message);
			
			// Send join message
			joinEvent.setJoinMessage(message);
		}
	}
}
