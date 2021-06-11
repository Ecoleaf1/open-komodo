package net.wigoftime.open_komodo.events;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.chat.MessageFormat;
import net.wigoftime.open_komodo.chat.PrivateMessage;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.EventExecutor;

public class PlayerQuit implements EventExecutor {

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		PlayerQuitEvent quitEvent = (PlayerQuitEvent) event;
		
		// Get CustomPlayer
		CustomPlayer player = CustomPlayer.get(quitEvent.getPlayer().getUniqueId());
		
		// If player in CustomPlayer doesn't exist
		// This could happen when a player joins and CustomPlayer is still trying to be created
		// but is still waiting for information like from a SQL database
		if (player == null) {
			quitEvent.setQuitMessage(null);
			return;
		}
		
		// Get Message
		String message = Main.leaveMessage;
		
		// If message doesn't exist
		if (message == null)
			return;
		
		// If message is empty
		if (message == "")
		{
			quitEvent.setQuitMessage(null);
			return;
		}
		
		// format message
		message = MessageFormat.format(player, message);
		
		// Format colours
		message = ChatColor.translateAlternateColorCodes('&', message);
		
		// Show quit message
		quitEvent.setQuitMessage(message);
		
		// Unop the player (To sync with the permissions)
		if (player.getPlayer().isOp())
			player.getPlayer().setOp(false);
		
		PrivateMessage.playerLeft(player.getUniqueId());


		player.getMarriageSystem().quiting();
		player.prepareDestroy();
	}

}
