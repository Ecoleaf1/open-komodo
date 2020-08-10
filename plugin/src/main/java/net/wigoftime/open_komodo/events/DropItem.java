package net.wigoftime.open_komodo.events;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class DropItem implements EventExecutor {

	@Override
	public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
		PlayerDropItemEvent dropItemEvent = (PlayerDropItemEvent) event;
		
		// If server allows items to be dropped, allow
		if (Main.allowDrop)
			return;
		
		CustomPlayer dropperCustomPlayer = CustomPlayer.get(dropItemEvent.getPlayer().getUniqueId());
		
		if (dropperCustomPlayer == null)
		{
			dropItemEvent.setCancelled(true);
			return;
		}
		
		// If the player themselves has permission to drop, allow
		if (dropItemEvent.getPlayer().hasPermission(Permissions.dropPerm))
			if (dropperCustomPlayer.isBuilding())
				return;
		
		// Otherwise, stop the player from dropping their item
		dropItemEvent.setCancelled(true);
		// Message the player that it isn't allowed
		dropItemEvent.getPlayer().sendMessage(Permissions.getDropError());
	}
	
}
