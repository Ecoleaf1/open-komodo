package net.wigoftime.open_komodo.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.EventExecutor;

import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class VehicleDestroyEvent implements EventExecutor {

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		org.bukkit.event.vehicle.VehicleDestroyEvent breakEvent = (org.bukkit.event.vehicle.VehicleDestroyEvent) event;
		
		// If it wasn't player, return
		if (!(breakEvent.getAttacker() instanceof Player))
		{
			// Cancel vehicle being destroyed
			breakEvent.setCancelled(true);
			return;
		}
		
		// If player has permission, return
		if (!breakEvent.getAttacker().hasPermission(Permissions.changePerm)) {
			// Cancel vehicle being destroyed
			breakEvent.setCancelled(true);
			// Send message to player that it isn't allowed
			breakEvent.getAttacker().sendMessage(Permissions.getChangeError());
			
			return;
		}
		
		// Get CustomPlayer
		CustomPlayer player = CustomPlayer.get(((Player) breakEvent.getAttacker()).getUniqueId());
		
		// If customplayer format of it doesn't exist, cancel
		// Can happen if the server hasn't loaded all of the player information when they join
		if (player == null)
		{
			breakEvent.setCancelled(true);
			return;
		}
		
		if (!player.isBuilding())
		{
			breakEvent.setCancelled(true);
		}
	}

}
