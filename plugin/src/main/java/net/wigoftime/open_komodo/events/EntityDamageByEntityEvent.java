/*		************************************
 * 		Use cases:
 * 		Entity Damage Protection from Players
 * 		************************************
 */

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
		
		if (entityDamageEvent.getDamager().getType() != EntityType.PLAYER) return;
		
		Player damager = (Player) entityDamageEvent.getDamager();
		
		if (!damager.hasPermission(Permissions.hurtPerm)) {
			entityDamageEvent.setCancelled(true);
			damager.sendMessage(Permissions.getHurtError());
			
			return;
		}
		CustomPlayer damagerCustom = CustomPlayer.get(damager.getUniqueId());
		
		// One possibility would be if the player has not loaded in yet.
		// To prevent errors from occurring, players who has not loaded in
		// can not proceed.
		if (damagerCustom == null) {
			entityDamageEvent.setCancelled(true);
			return;
		}
		
		if (!damagerCustom.isBuilding()) {
			entityDamageEvent.setCancelled(true);
			return;
		}
	}

}
