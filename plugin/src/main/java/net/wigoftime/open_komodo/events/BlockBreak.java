/*		************************************
 * 		Use Cases:
 * 		Prevent players who are not in build mode to build,
 * 		Prevents builders to build
 * 		************************************
 */

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
		CustomPlayer playerCustom = CustomPlayer.get(breakEvent.getPlayer().getUniqueId());
		
		// One possibility would be if the player has not loaded in yet.
		// To prevent errors from occurring, players who has not loaded in
		// can not proceed.
		if (playerCustom == null) {
			breakEvent.setCancelled(true);
			return;
		}
		
		if (!playerCustom.getPlayer().hasPermission(Permissions.breakPerm)) {
			breakEvent.setCancelled(true);
			playerCustom.getPlayer().sendMessage(Permissions.getBreakError());
			return;
		}
		
		if (!playerCustom.isBuilding()) breakEvent.setCancelled(true);
	}

}
