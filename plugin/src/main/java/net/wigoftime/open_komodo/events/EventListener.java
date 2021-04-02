package net.wigoftime.open_komodo.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
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
import org.bukkit.plugin.Plugin;
import org.spigotmc.event.entity.EntityDismountEvent;

import github.scarsz.discordsrv.DiscordSRV;
import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.PrintConsole;

public class EventListener implements Listener {
	private final DiscordSRVListener discordListener;
	
	public EventListener(Plugin plugin) {
		Bukkit.getPluginManager().registerEvent(PlayerJoinEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.PlayerJoin(), plugin);
		Bukkit.getPluginManager().registerEvent(PlayerDropItemEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.DropItem(), plugin);
		Bukkit.getPluginManager().registerEvent(BlockPlaceEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.BlockPlace(), plugin);
		Bukkit.getPluginManager().registerEvent(BlockBreakEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.BlockBreak(), plugin);
		Bukkit.getPluginManager().registerEvent(VehicleDestroyEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.VehicleDestroyEvent(), plugin);
		Bukkit.getPluginManager().registerEvent(PlayerInteractAtEntityEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.PlayerInteractAtEntityEvent(), plugin);
		Bukkit.getPluginManager().registerEvent(PlayerInteractEntityEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.PlayerInteractEntityEvent(), plugin);
		Bukkit.getPluginManager().registerEvent(EntityDamageByEntityEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.EntityDamageByEntityEvent(), plugin);
		Bukkit.getPluginManager().registerEvent(PlayerMoveEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.PlayerMove(), plugin);
		Bukkit.getPluginManager().registerEvent(EntityDismountEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.EntityDismount(), plugin);
		Bukkit.getPluginManager().registerEvent(BlockFadeEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.BlockFade(), plugin);
		Bukkit.getPluginManager().registerEvent(HangingBreakByEntityEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.HangingBreakByEntity(), plugin);
		Bukkit.getPluginManager().registerEvent(PlayerEggThrowEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.PlayerEggThrow(), plugin);
		Bukkit.getPluginManager().registerEvent(PlayerInteractEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.PlayerInteract(), plugin);
		Bukkit.getPluginManager().registerEvent(PlayerChangedWorldEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.PlayerChangedWorld(), plugin);
		Bukkit.getPluginManager().registerEvent(PlayerQuitEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.PlayerQuit(), plugin);
		Bukkit.getPluginManager().registerEvent(ExplosionPrimeEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.ExplosionPrime(), plugin);
		Bukkit.getPluginManager().registerEvent(EntityDamageEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.EntityDamage(), plugin);
		Bukkit.getPluginManager().registerEvent(PlayerSwapHandItemsEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.PlayerSwapHandItems(), plugin);
		Bukkit.getPluginManager().registerEvent(PlayerEditBookEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.PlayerEditBook(), plugin);
		Bukkit.getPluginManager().registerEvent(AsyncPlayerPreLoginEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.AsyncPlayerPreLogin(), plugin);
		Bukkit.getPluginManager().registerEvent(EntityDeathEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.EntityDeath(), plugin);
		Bukkit.getPluginManager().registerEvent(PlayerRespawnEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.PlayerRespawn(), plugin);
		Bukkit.getPluginManager().registerEvent(PlayerCommandPreprocessEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.CommandPreprocess(), plugin);
		Bukkit.getPluginManager().registerEvent(AsyncPlayerChatEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.AsyncPlayerChat(), plugin);
		Bukkit.getPluginManager().registerEvent(InventoryCloseEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.InventoryClose(), plugin);
		Bukkit.getPluginManager().registerEvent(BlockFertilizeEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.BlockFertilize(), plugin);
		Bukkit.getPluginManager().registerEvent(InventoryClickEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.InventoryClick(), plugin);
		
		if (Bukkit.getPluginManager().getPlugin("Votifier") == null)
		PrintConsole.print("NuVotifier isn't detected, vote rewards disabled.");
		else {
			Bukkit.getPluginManager().registerEvent(com.vexsoftware.votifier.model.VotifierEvent.class, this, EventPriority.NORMAL, new net.wigoftime.open_komodo.events.VotifierEvent(), plugin);
			PrintConsole.print("NuVotifier detected, vote rewards enabled.");
		}
		
		if (Main.getDiscordSRV() == null) {
			discordListener = null;
			return;
		}
		
		discordListener = new DiscordSRVListener();
		DiscordSRV.api.subscribe(discordListener);
	}
	
	public void disable() {
		if (discordListener == null) return;
		DiscordSRV.api.unsubscribe(discordListener);
	}
}