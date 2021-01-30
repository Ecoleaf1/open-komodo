package net.wigoftime.open_komodo.events;

import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.etc.PetsManager;
import net.wigoftime.open_komodo.gui.PetControl;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class PlayerInteractAtEntityEvent implements EventExecutor {

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		org.bukkit.event.player.PlayerInteractAtEntityEvent playerInteractedAt = (org.bukkit.event.player.PlayerInteractAtEntityEvent) event;
		
		CustomPlayer player = CustomPlayer.get(playerInteractedAt.getPlayer().getUniqueId());
		
		if (player == null) {
			playerInteractedAt.setCancelled(true);
			return;
		}
		
		
		if (playerInteractedAt.getRightClicked() == PetsManager.getCreature(player.getPlayer())) {
			playerInteractedAt.setCancelled(true);
			PetControl gui = new PetControl(player);
			gui.open();
			return;
		}
		
		if (!player.getPlayer().hasPermission(Permissions.changePerm)) {
			if (playerInteractedAt.getRightClicked().getType() == EntityType.ARMOR_STAND) {
				playerInteractedAt.setCancelled(true);
				playerInteractedAt.getPlayer().sendMessage(Permissions.getChangeError());
				return;
			}
		}
		
		if (!player.isBuilding()) {
			playerInteractedAt.setCancelled(true);
			return;
		}
		
	}

}
