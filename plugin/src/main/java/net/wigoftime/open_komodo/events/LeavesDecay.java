package net.wigoftime.open_komodo.events;

import net.wigoftime.open_komodo.Main;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.plugin.EventExecutor;

public class LeavesDecay implements EventExecutor {

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		LeavesDecayEvent decayEvent = (LeavesDecayEvent) event;
		
		if (Main.leavesDecayEnabled == false) {
			decayEvent.setCancelled(true);
		}
	}

}
