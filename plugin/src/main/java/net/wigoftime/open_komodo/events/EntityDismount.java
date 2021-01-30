package net.wigoftime.open_komodo.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.spigotmc.event.entity.EntityDismountEvent;

public class EntityDismount implements EventExecutor {

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		EntityDismountEvent dismountEvent = (EntityDismountEvent) event;
		
		Entity entity = dismountEvent.getDismounted();
		
		if (entity.getType() == EntityType.ARROW)
			entity.remove();
		
		return;
	}

}
