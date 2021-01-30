package net.wigoftime.open_komodo.events;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.EventExecutor;

public class EntityDeath implements EventExecutor {

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		EntityDeathEvent deathEvent = (EntityDeathEvent) event;
		deathEvent.getDrops().clear();
	}

}
