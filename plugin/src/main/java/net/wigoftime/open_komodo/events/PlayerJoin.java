package net.wigoftime.open_komodo.events;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.chat.MessageFormat;
import net.wigoftime.open_komodo.etc.*;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.sql.SQLManager;
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
import org.jetbrains.annotations.NotNull;

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
	
	private void firstJoined(@NotNull Player player) {
		// Check if player exists in the SQL database or has it's own player config file
		if (!CustomPlayer.containsPlayer(player.getUniqueId())) {	
			for (Player p : Bukkit.getServer().getOnlinePlayers())
				p.sendMessage(String.format(Main.firstWelcome, player.getPlayer().getName()));
		}
	}
	
	private @NotNull ItemStack getCard() {
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
	
	private void joinMessage(@NotNull CustomPlayer customPlayer, @NotNull PlayerJoinEvent joinEvent) {
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
