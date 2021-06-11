/*		************************************
 * 		Use cases:
 * 		Entity Damage Protection,
 * 		Entity Fall Damage Protection
 * 		************************************
 */

package net.wigoftime.open_komodo.events;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.custommobs.CustomPetMob;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.EventExecutor;

public class EntityDamage implements EventExecutor {

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		EntityDamageEvent damageEvent = (EntityDamageEvent) event;
		
		// Prevent Pets from being damaged
		if (damageEvent.getEntity() instanceof CustomPetMob) {
			damageEvent.setCancelled(true);
			return;
		}
		
		if (damageEvent.getCause() == DamageCause.FALL && !Main.fallDamage)
			damageEvent.setCancelled(true);
		
		if (damageEvent.getEntityType() != EntityType.PLAYER) return;
		if (Main.damageAllowed) return;
		
		damageEvent.setCancelled(true);
	}

}
