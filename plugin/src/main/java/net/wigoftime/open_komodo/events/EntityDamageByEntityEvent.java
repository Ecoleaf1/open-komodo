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

import net.wigoftime.open_komodo.etc.FurnitureMangement;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class EntityDamageByEntityEvent implements EventExecutor {

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		if (!(event instanceof org.bukkit.event.entity.EntityDamageByEntityEvent))
			return;
		
		org.bukkit.event.entity.EntityDamageByEntityEvent entityDamgedByEvent = (org.bukkit.event.entity.EntityDamageByEntityEvent) event;
		
		if (entityDamgedByEvent.getDamager().getType() != EntityType.PLAYER) return;
		
		Player damager = (Player) entityDamgedByEvent.getDamager();
		
		if (!damager.hasPermission(Permissions.hurtPerm)) {
			entityDamgedByEvent.setCancelled(true);
			damager.sendMessage(Permissions.getHurtError());
			
			return;
		}
		CustomPlayer damagerCustom = CustomPlayer.get(damager.getUniqueId());
		
		// One possibility would be if the player has not loaded in yet.
		// To prevent errors from occurring, players who has not loaded in
		// can not proceed.
		if (damagerCustom == null) {
			entityDamgedByEvent.setCancelled(true);
			return;
		}
		
		if (!damagerCustom.isBuilding()) {
			entityDamgedByEvent.setCancelled(true);
			return;
		}
		
		if (FurnitureMangement.isValid(entityDamgedByEvent.getEntity())) {
			entityDamgedByEvent.setCancelled(true);
			FurnitureMangement.deleteFurniture(entityDamgedByEvent);
		}
	}

}
