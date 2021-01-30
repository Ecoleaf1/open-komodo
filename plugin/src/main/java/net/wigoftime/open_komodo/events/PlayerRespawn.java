package net.wigoftime.open_komodo.events;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.EventExecutor;

import net.wigoftime.open_komodo.Main;

public class PlayerRespawn implements EventExecutor {

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		PlayerRespawnEvent respawnEvent = (PlayerRespawnEvent) event;
		
		respawnEvent.setRespawnLocation(Main.spawnLocation);
	}

}
