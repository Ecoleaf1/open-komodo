package net.wigoftime.open_komodo.events;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.EventExecutor;

import net.wigoftime.open_komodo.gui.GUIManager;

public class InventoryClick implements EventExecutor {

	public void execute(Listener listener, Event event) throws EventException {
		if (!(event instanceof InventoryClickEvent)) return;
		
		InventoryClickEvent clickEvent = (InventoryClickEvent) event;
		GUIManager.invItemClicked(clickEvent);


	}
	

}
