package net.wigoftime.open_komodo.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.plugin.EventExecutor;

import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class HangingBreakByEntity implements EventExecutor {

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		if (!(event instanceof HangingBreakByEntityEvent)) return;
		
		HangingBreakByEntityEvent hangingBreakEvent = (HangingBreakByEntityEvent) event;
		
		if (hangingBreakEvent.getRemover().getType() != EntityType.PLAYER) return;
		
		CustomPlayer playerCustom = CustomPlayer.get(((Player) hangingBreakEvent.getRemover()).getUniqueId());
		
		// If customplayer format of it doesn't exist, cancel
		// Can happen if the server hasn't loaded all of the player information when they join
		if (playerCustom == null) {
			hangingBreakEvent.setCancelled(true);
			return;
		}
		
		if (!playerCustom.getPlayer().hasPermission(Permissions.changePerm)) {
			// Otherwise, canscel and send a msg that they are not allowed
			hangingBreakEvent.setCancelled(true);
			playerCustom.getPlayer().sendMessage(Permissions.getChangeError());
			
			return;
		}
		
		if (!playerCustom.isBuilding()) {
			hangingBreakEvent.setCancelled(true);
			return;
		}
	}

}
