package net.wigoftime.open_komodo.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.EventExecutor;

import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.ItemType;

public class PlayerSwapHandItems implements EventExecutor {

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		PlayerSwapHandItemsEvent swapHandItemEvent = (PlayerSwapHandItemsEvent) event;
		
		// Get Player
		Player player = swapHandItemEvent.getPlayer();
		
		// Get Item
		ItemStack item = swapHandItemEvent.getOffHandItem();
		
		if (item == null)
			return;
		
		// Get more info about Item
		ItemMeta meta = item.getItemMeta();
		
		if (meta == null)
			return;
		
		// If has ID
		if (meta.hasCustomModelData()) {
			// Get ID
			int id = meta.getCustomModelData();
			
			// Get Custom Item
			CustomItem ci = CustomItem.getCustomItem(id);
			
			// If it isn't a custom item, ignore
			if (ci == null)
				return;
			
			// If item is a hat or the phone, prevent
			if (ci.getType() == ItemType.HAT || id == 1)
				swapHandItemEvent.setCancelled(true);
			
			// If it is FPBank card
			if (item.getType() == Material.INK_SAC || id == 56)
				swapHandItemEvent.setCancelled(true);
			
			return;
		}
	}

}
