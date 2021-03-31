package net.wigoftime.open_komodo.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.etc.PetsManager;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.gui.PetControl;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class PlayerInteractEntityEvent implements EventExecutor {

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		org.bukkit.event.player.PlayerInteractEntityEvent playerInteractEvent = (org.bukkit.event.player.PlayerInteractEntityEvent) event;
		CustomPlayer player = CustomPlayer.get(playerInteractEvent.getPlayer().getUniqueId());
		
		// If CustomPlayer format of player doesn't exist, cancel
		// Can happen if the server hasn't loaded all of the player information when they join
		if (player == null) {
			playerInteractEvent.setCancelled(true);
			return;
		}
		
		Entity entity = playerInteractEvent.getRightClicked();
		
		if (PetsManager.isPet(player.getUniqueId(), entity.getUniqueId())) {
			playerInteractEvent.setCancelled(true);
			PetControl gui = new PetControl(player);
			gui.open();
			return;
		}
		
		if (player.isBuilding()) return;
		
		switch (entity.getType()) {
		case ARMOR_STAND:
			playerInteractEvent.setCancelled(true);
			playerInteractEvent.getPlayer().sendMessage(Permissions.getChangeError());
			break;
		case ITEM_FRAME:
			playerInteractEvent.setCancelled(true);
			playerInteractEvent.getPlayer().sendMessage(Permissions.getChangeError());
			break;
		default:
			break;
		}
	}

}
