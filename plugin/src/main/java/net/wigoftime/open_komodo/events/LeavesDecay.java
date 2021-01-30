package net.wigoftime.open_komodo.events;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.plugin.EventExecutor;

import net.wigoftime.open_komodo.Main;

public class LeavesDecay implements EventExecutor {

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		LeavesDecayEvent decayEvent = (LeavesDecayEvent) event;
		
		if (Main.leavesDecayEnabled == false) {
			decayEvent.setCancelled(true);
		}
	}

}
