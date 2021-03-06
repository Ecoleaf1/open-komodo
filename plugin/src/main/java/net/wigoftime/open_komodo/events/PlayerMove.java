package net.wigoftime.open_komodo.events;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.etc.systems.RankSystem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class PlayerMove implements EventExecutor {
	@Override
	public void execute(Listener listener, Event event) throws EventException {
		PlayerMoveEvent moveEvent = (PlayerMoveEvent) event;
		
		Player player = moveEvent.getPlayer();
		
		CustomPlayer moverCustomPlayer = CustomPlayer.get(player.getUniqueId());
		
		// If customplayer format of it doesn't exist, stop
		// Can happen if the server hasn't loaded all of the player information when they join
		if (moverCustomPlayer == null)
			return;
		
		moverCustomPlayer.setAfk(false);
		
		if (!moverCustomPlayer.isBuilding())
		if (!moverCustomPlayer.isInTutorial())
		if (passedWorldBorder(player.getLocation())) {
			player.sendMessage(Permissions.getWorldBorderMessage());
			player.getPassengers().stream().forEach(passenger -> {
				player.removePassenger(passenger);
				passenger.teleport(Main.spawnLocation);
			});
			player.teleport(Main.spawnLocation);
		}
		
		if (player.isFlying())
			return;
		
		if (player.getFallDistance() > 0)
			return;
		
		if (moveEvent.getFrom().distance(moveEvent.getTo()) > 0.2)
		{	
			double add = 0.000015;
			RankSystem.addPendingPoints(player, add);
		}
	}
	
	private boolean passedWorldBorder(@NotNull Location playerLocation) {
		if (Main.BorderPosition1.getX() < 0) {
			if (playerLocation.getX() < Main.BorderPosition1.getX()) return true;
		} else if (playerLocation.getX() > Main.BorderPosition1.getX()) return true;
		
		if (Main.BorderPosition1.getZ() < 0) {
			if (playerLocation.getZ() < Main.BorderPosition1.getZ()) return true;
		} else if (playerLocation.getZ() > Main.BorderPosition1.getZ()) return true;
		
		if (Main.BorderPosition2.getX() < 0) {
			if (playerLocation.getX() < Main.BorderPosition2.getX()) return true;
		} else if (playerLocation.getX() > Main.BorderPosition2.getX()) return true;
			
		if (Main.BorderPosition2.getZ() < 0) {
			if (playerLocation.getZ() < Main.BorderPosition2.getZ()) return true;
		} else if (playerLocation.getZ() > Main.BorderPosition2.getZ()) return true;
			
		return false;
	}

}
