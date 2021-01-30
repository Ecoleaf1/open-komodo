package net.wigoftime.open_komodo.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.EventExecutor;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.custommobs.CustomPetMob;

public class EntityDamage implements EventExecutor {

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		EntityDamageEvent damageEvent = (EntityDamageEvent) event;
		
		if (damageEvent.getEntityType() == EntityType.PLAYER) {
			Player player = (Player) damageEvent.getEntity();
			
			if (!Main.damageAllowed) {
				damageEvent.setCancelled(true);
				return;
			}
			
			if (player != null)
				if (damageEvent.getCause() == DamageCause.FALL && !Main.fallDamage)
					damageEvent.setCancelled(true);
		
			
			return;
		}
		
		if (damageEvent.getEntity() instanceof CustomPetMob) {
			damageEvent.setCancelled(true);
		}
	}

}
