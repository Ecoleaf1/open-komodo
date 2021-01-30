package net.wigoftime.open_komodo.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.plugin.EventExecutor;

import net.wigoftime.open_komodo.Main;

public class ExplosionPrime implements EventExecutor {

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		ExplosionPrimeEvent explosionEvent = (ExplosionPrimeEvent) event;
		
		if (Main.explosionEnabled == false) {
			explosionEvent.setCancelled(true);
			Bukkit.getLogger().info(explosionEvent.getEntity().getName() +" tried to blow up");
		}
	}

}
