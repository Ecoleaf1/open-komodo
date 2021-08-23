package net.wigoftime.open_komodo.events;

import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.gui.GUIManager;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.EventExecutor;

public class InventoryClose implements EventExecutor {

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		InventoryCloseEvent inventoryCloseEvent = (InventoryCloseEvent) event;
		CustomPlayer player = CustomPlayer.get(((InventoryCloseEvent) event).getPlayer().getUniqueId());
		if (player.hasActiveGui()) {
			GUIManager.inventoryClosed(inventoryCloseEvent);
		}
	}

}
