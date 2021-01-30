/*		************************************
 * 		Use Cases:
 * 		Prevent player from growing things
 * 		************************************
 */

package net.wigoftime.open_komodo.events;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class BlockFertilize implements EventExecutor {

	@Override
	public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
		BlockFertilizeEvent fertilizeEvent = (BlockFertilizeEvent) event;
		CustomPlayer causerCustomPlayer = CustomPlayer.get(fertilizeEvent.getPlayer().getUniqueId());
		
		// One possibility would be if the player has not loaded in yet.
		// To prevent errors from occurring, players who has not loaded in
		// can not proceed.
		if (causerCustomPlayer == null) {
			fertilizeEvent.setCancelled(true);
			return;
		}
		
		hasPermission(causerCustomPlayer, fertilizeEvent);
	}
	
	private void hasPermission(CustomPlayer causerCustomPlayer, BlockFertilizeEvent fertilizeEvent) {
		if (!causerCustomPlayer.getPlayer().hasPermission(Permissions.breakPerm)) {
			fertilizeEvent.setCancelled(true);
			causerCustomPlayer.getPlayer().sendMessage(Permissions.getBreakError());
			return;
		}
		
		if (!causerCustomPlayer.isBuilding()) {
			fertilizeEvent.setCancelled(true);
			return;
		}
	}

}
