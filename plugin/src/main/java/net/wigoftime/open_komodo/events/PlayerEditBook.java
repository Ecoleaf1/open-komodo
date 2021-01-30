package net.wigoftime.open_komodo.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.EventExecutor;

import net.wigoftime.open_komodo.actions.BugReporter;

public class PlayerEditBook implements EventExecutor {

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		PlayerEditBookEvent editBookEvent = (PlayerEditBookEvent) event;
		
		// Get info about book
		BookMeta meta = editBookEvent.getNewBookMeta();
		
		// If has ID, resume
		if (!meta.hasCustomModelData())
			return;
		
		// If ID matches BugReporter book, resume
		if (meta.getCustomModelData() != BugReporter.id)
			return;
		
		// Report the bug/feature
		BugReporter.complete(editBookEvent.getPlayer(), meta);
		
		// Get Player
		Player player = editBookEvent.getPlayer();
		// Get player's Inventory
		PlayerInventory inventory = player.getInventory();
		
		// Get Item where their book is (Guessing being held)
		ItemStack item;
		item = inventory.getItemInMainHand();
		
		// If doesn't exist, try get their off-hand item
		if (item == null)
		{
			item = inventory.getItemInOffHand();
			
			if (item == null)
				return;
			
		}
		
		// Resume if item has ItemMeta
		if (!item.hasItemMeta())
			return;
		
		// Get info about item
		ItemMeta meta2 = item.getItemMeta();
		
		// If item doesn't have ID
		if (!meta2.hasCustomModelData())
			return;
		
		// Get ID from item
		int id = meta2.getCustomModelData();
		
		// If ID matches the BugReporter book, resume
		if (id != BugReporter.id)
			return;
		
		// Remove Report book from Inventory
		item.setAmount(-1);
		editBookEvent.setCancelled(true);
	}

}
