package net.wigoftime.open_komodo.events;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.EventExecutor;

import net.wigoftime.open_komodo.etc.InventoryManagement;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.sql.SQLManager;

public class PlayerChangedWorld implements EventExecutor {

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		PlayerChangedWorldEvent changedWorldEvent = (PlayerChangedWorldEvent) event;
		
		CustomPlayer player = CustomPlayer.get(changedWorldEvent.getPlayer().getUniqueId());
		
		// If customplayer format of it doesn't exist, cancel
		// Can happen if the server hasn't loaded all of the player information when they join
		if (player == null)
			return;
		
		World previousWorld = changedWorldEvent.getFrom();
		World world = player.getPlayer().getWorld();
		
		// Save inventory
		InventoryManagement.saveInventory(player, previousWorld);
		
		// Create new world player entry if none in teleported world
		if (SQLManager.isEnabled())
			if (!SQLManager.containsWorldPlayer(player.getUniqueId(), world.getName()))
			SQLManager.createWorldPlayer(player.getUniqueId(), world.getName());
		
		// switch out of build mode
		if (player.isBuilding())
			player.setBuilding(false);
		
		if (player.isInvisible())
			player.setInvisible(false, true);
		
		// Load inventory
		InventoryManagement.loadInventory(player, world);
		
		// Reload permissions
		Permissions.setUp(player);
		
	}

}
