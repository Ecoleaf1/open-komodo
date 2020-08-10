package net.wigoftime.open_komodo.events;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class BlockBreak implements EventExecutor {

	@Override
	public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
		BlockBreakEvent breakEvent = (BlockBreakEvent) event;
		
		// Get Player
		CustomPlayer player = CustomPlayer.get(breakEvent.getPlayer().getUniqueId());
			
		if (player == null)
		{
			breakEvent.setCancelled(true);
			return;
		}
		
		// Allow if player has permission
		if (!player.getPlayer().hasPermission(Permissions.breakPerm)) {
			breakEvent.setCancelled(true);
			player.getPlayer().sendMessage(Permissions.getBreakError());
			
			return;
		}
		
		if (!player.isBuilding())
			breakEvent.setCancelled(true);
	}

}
