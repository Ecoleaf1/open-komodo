/*		************************************
 * 		Use cases:
 * 		Item Drop Prevention
 * 		************************************
 */

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
		
		if (Main.allowDrop) return;
		
		CustomPlayer dropperCustomPlayer = CustomPlayer.get(dropItemEvent.getPlayer().getUniqueId());
		
		// One possibility would be if the player has not loaded in yet.
		// To prevent errors from occurring, players who has not loaded in
		// can not proceed.
		if (dropperCustomPlayer == null) {
			dropItemEvent.setCancelled(true);
			return;
		}
		
		if (dropItemEvent.getPlayer().hasPermission(Permissions.dropPerm))
		if (dropperCustomPlayer.isBuilding()) return;
		
		dropItemEvent.setCancelled(true);
		dropItemEvent.getPlayer().sendMessage(Permissions.getDropError());
	}
	
}
