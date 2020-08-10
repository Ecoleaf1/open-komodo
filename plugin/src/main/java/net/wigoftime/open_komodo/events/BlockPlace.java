package net.wigoftime.open_komodo.events;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class BlockPlace implements EventExecutor {

	@Override
	public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
		BlockPlaceEvent placeEvent  = (BlockPlaceEvent) event;
		
		// Get Player
		CustomPlayer player = CustomPlayer.get(placeEvent.getPlayer().getUniqueId());
		
		if (player == null)
		{
			placeEvent.setCancelled(true);
			return;
		}
		
		// If player has permission to drop, allow
		if (!player.getPlayer().hasPermission(Permissions.placePerm)) {
			placeEvent.setCancelled(true);
			player.getPlayer().sendMessage(Permissions.getPlaceError());
			
			return;
		}
		
		if (!player.isBuilding())
			placeEvent.setCancelled(true);
	}

}
