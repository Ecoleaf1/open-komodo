package net.wigoftime.open_komodo.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.EventExecutor;

import net.wigoftime.open_komodo.etc.RankSystem;
import net.wigoftime.open_komodo.objects.CustomPlayer;

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
		
		if (moverCustomPlayer.isAfk())
			moverCustomPlayer.setAfk(false);
		
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

}
