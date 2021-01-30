package net.wigoftime.open_komodo.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class EntityDamageByEntityEvent implements EventExecutor {

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		org.bukkit.event.entity.EntityDamageByEntityEvent entityDamageEvent = (org.bukkit.event.entity.EntityDamageByEntityEvent) event;
		
		if (entityDamageEvent.getDamager().getType() != EntityType.PLAYER) 
			return;
		
		
		Player damager = (Player) entityDamageEvent.getDamager();
		if (!damager.hasPermission(Permissions.hurtPerm)) {
			// Other wise, cancel it and tell player that they are not allowed
			entityDamageEvent.setCancelled(true);
			damager.sendMessage(Permissions.getHurtError());
			
			return;
		}
		
		CustomPlayer player = CustomPlayer.get(damager.getUniqueId());
		
		// If customplayer format of it doesn't exist, cancel
		// Can happen if the server hasn't loaded all of the player information when they join
		if (player == null) {
			entityDamageEvent.setCancelled(true);
			return;
		}
		
		if (!player.isBuilding()) {
			entityDamageEvent.setCancelled(true);
			return;
		}
	}

}
