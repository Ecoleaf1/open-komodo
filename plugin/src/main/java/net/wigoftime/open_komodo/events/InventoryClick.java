package net.wigoftime.open_komodo.events;

import net.wigoftime.open_komodo.gui.GUIManager;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.EventExecutor;

public class InventoryClick implements EventExecutor {

	public void execute(Listener listener, Event event) throws EventException {
		if (!(event instanceof InventoryClickEvent)) return;
		
		InventoryClickEvent clickEvent = (InventoryClickEvent) event;
		GUIManager.invItemClicked(clickEvent);


	}
	

}
