package net.wigoftime.open_komodo.events;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.plugin.EventExecutor;

public class PlayerEggThrow implements EventExecutor {

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		PlayerEggThrowEvent eggThrowEvent = (PlayerEggThrowEvent) event;
		eggThrowEvent.setHatching(false);
	}

}
